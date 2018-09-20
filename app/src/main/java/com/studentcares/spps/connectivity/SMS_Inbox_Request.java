package com.studentcares.spps.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.R;
import com.studentcares.spps.internetConnectivity.CheckInternetConnection;
import com.studentcares.spps.model.SMS_Inbox_Items;
import com.studentcares.spps.sqlLite.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SMS_Inbox_Request {

    private static Context context;
    private static String webMethName, selectedDateAtt, selectedDateOther;
    private static ProgressDialog progressDialogAtt, progressDialogOther;

    private static RecyclerView.Adapter adapterForAsyncTaskAtt;
    private static RecyclerView.Adapter adapterForAsyncTaskOther;
    private static RecyclerView recyclerViewForAsyncTaskAtt;
    private static RecyclerView recyclerViewForAsyncTaskOther;
    private static List<SMS_Inbox_Items> ItemsArrayForAsyncTaskAtt;
    private static List<SMS_Inbox_Items> ItemsArrayForAsyncTaskOther;

    private static String schoolId, userId, userType;
    private static DataBaseHelper mydb;


    public SMS_Inbox_Request(Context sms_receive_attendance_list) {
        context = sms_receive_attendance_list;
    }

    public void GetAttendanceSMSList(List<SMS_Inbox_Items> listItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfUser, String idOfSchool, String typeOfUser, int current_page, ProgressDialog pd) {
        schoolId = idOfSchool;
        userId = idOfUser;
        userType = typeOfUser;
        progressDialogAtt = pd;
        adapterForAsyncTaskAtt = reviewAdapter;
        recyclerViewForAsyncTaskAtt = recyclerView;
        ItemsArrayForAsyncTaskAtt = listItems;
        mydb = new DataBaseHelper(context);

        if (CheckInternetConnection.getInstance(context).isOnline()) {
            AttendanceSMSList();
        } else {
            AttendanceSMSListOffline();
        }
    }

    public void GetAttendanceSMSListDateWise(List<SMS_Inbox_Items> listItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfUser, String idOfSchool, String typeOfUser, int current_page, String selectedDate, ProgressDialog pd) {
        schoolId = idOfSchool;
        userId = idOfUser;
        userType = typeOfUser;
        progressDialogAtt = pd;
        adapterForAsyncTaskAtt = reviewAdapter;
        recyclerViewForAsyncTaskAtt = recyclerView;
        ItemsArrayForAsyncTaskAtt = listItems;
        mydb = new DataBaseHelper(context);
        selectedDateAtt = selectedDate;

        String msgType = "Attendance";
        JSONArray msgList = null;
        try {

            msgList = mydb.GetDateWiseMsg(userId, msgType, selectedDateAtt);

            for (int j = 0; j < msgList.length(); j++) {
                try {
                    JSONObject obj = msgList.getJSONObject(j);

                    String Date = obj.getString("Date");
                    String Time = obj.getString("Time");
                    String Msg = obj.getString("Message");
                    String Msg_Type = obj.getString("Message_Type");
                    String Id = obj.getString("ID");

                    SMS_Inbox_Items leaveItems = new SMS_Inbox_Items();
                    leaveItems.setDate(Date);
                    leaveItems.settime(Time);
                    leaveItems.setmsgBody(Msg);
                    leaveItems.setmsgType(Msg_Type);
                    leaveItems.setId(Id);
                    ItemsArrayForAsyncTaskAtt.add(leaveItems);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            progressDialogAtt.dismiss();
            adapterForAsyncTaskAtt.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void AttendanceSMSListOffline() {
        String msgType = "Attendance";
        JSONArray msgList = null;
        try {

            msgList = mydb.GetAllMsg(userId, msgType);

            for (int j = 0; j < msgList.length(); j++) {
                try {
                    JSONObject obj = msgList.getJSONObject(j);

                    String Date = obj.getString("Date");
                    String Time = obj.getString("Time");
                    String Msg = obj.getString("Message");
                    String Msg_Type = obj.getString("Message_Type");
                    String Id = obj.getString("ID");

                    SMS_Inbox_Items leaveItems = new SMS_Inbox_Items();
                    leaveItems.setDate(Date);
                    leaveItems.settime(Time);
                    leaveItems.setmsgBody(Msg);
                    leaveItems.setmsgType(Msg_Type);
                    leaveItems.setId(Id);
                    ItemsArrayForAsyncTaskAtt.add(leaveItems);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            progressDialogAtt.dismiss();
            adapterForAsyncTaskAtt.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void AttendanceSMSList() {

        JSONObject jsonObject = new JSONObject();

        if (userType.equals("Student")) {
            webMethName = "Attendance_Student_SMS_List";
        } else {
            webMethName = "Attendance_Staff_SMS_List";
        }

        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("User_Id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = context.getString(R.string.url) + webMethName;

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialogAtt.dismiss();
                            ItemsArrayForAsyncTaskAtt.clear();
                            adapterForAsyncTaskAtt.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if (res.equals("Msg Not Available.")) {
                                Toast.makeText(context, "SMS Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        SMS_Inbox_Items leaveItems = new SMS_Inbox_Items();

                                        String Id = obj.getString("ID");
                                        String Msg_Type = "Attendance";
                                        String Msg = obj.getString("Message");
                                        String Date = obj.getString("Date");
                                        String Time = obj.getString("Time");

                                        leaveItems.setDate(Date);
                                        leaveItems.settime(Time);
                                        leaveItems.setmsgBody(Msg);
                                        leaveItems.setmsgType(Msg_Type);
                                        leaveItems.setId(Id);

                                        mydb = new DataBaseHelper(context);
                                        mydb.InsertSMS(Id, Msg_Type, Msg, Date, Time, userId);

                                        ItemsArrayForAsyncTaskAtt.add(leaveItems);
                                    }
                                    adapterForAsyncTaskAtt.notifyDataSetChanged();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialogAtt.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void GetOtherSMSListDateWise(List<SMS_Inbox_Items> listItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfUser, String idOfSchool, String typeOfUser, int current_page, String selectedDate, ProgressDialog pd) {
        schoolId = idOfSchool;
        userId = idOfUser;
        userType = typeOfUser;
        progressDialogOther = pd;
        adapterForAsyncTaskOther = reviewAdapter;
        recyclerViewForAsyncTaskOther = recyclerView;
        ItemsArrayForAsyncTaskOther = listItems;
        mydb = new DataBaseHelper(context);
        selectedDateOther = selectedDate;

        String msgType = "Other";
        JSONArray msgList = null;
        try {

            msgList = mydb.GetDateWiseMsg(userId, msgType, selectedDateOther);

            for (int j = 0; j < msgList.length(); j++) {
                try {
                    JSONObject obj = msgList.getJSONObject(j);

                    String Date = obj.getString("Date");
                    String Time = obj.getString("Time");
                    String Msg = obj.getString("Message");
                    String Msg_Type = obj.getString("Message_Type");
                    String Id = obj.getString("ID");

                    SMS_Inbox_Items leaveItems = new SMS_Inbox_Items();
                    leaveItems.setDate(Date);
                    leaveItems.settime(Time);
                    leaveItems.setmsgBody(Msg);
                    leaveItems.setmsgType(Msg_Type);
                    leaveItems.setId(Id);
                    ItemsArrayForAsyncTaskOther.add(leaveItems);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            progressDialogOther.dismiss();
            adapterForAsyncTaskOther.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GetOtherSMSList(List<SMS_Inbox_Items> listItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfUser, String idOfSchool, String typeOfUser, int current_page, ProgressDialog pd) {
        schoolId = idOfSchool;
        userId = idOfUser;
        userType = typeOfUser;
        progressDialogOther = pd;
        adapterForAsyncTaskOther = reviewAdapter;
        recyclerViewForAsyncTaskOther = recyclerView;
        ItemsArrayForAsyncTaskOther = listItems;
        mydb = new DataBaseHelper(context);

        if (CheckInternetConnection.getInstance(context).isOnline()) {
            OtherSMSList();
        } else {
            OtherSMSListOffline();
        }
    }

    private void OtherSMSListOffline() {

        String msgType = "Other";
        JSONArray msgList = null;
        try {
            msgList = mydb.GetAllMsg(userId, msgType);

            for (int j = 0; j < msgList.length(); j++) {
                try {
                    JSONObject obj = msgList.getJSONObject(j);

                    String Date = obj.getString("Date");
                    String Time = obj.getString("Time");
                    String Msg = obj.getString("Message");
                    String Msg_Type = obj.getString("Message_Type");
                    String Id = obj.getString("ID");

                    SMS_Inbox_Items leaveItems = new SMS_Inbox_Items();
                    leaveItems.setDate(Date);
                    leaveItems.settime(Time);
                    leaveItems.setmsgBody(Msg);
                    leaveItems.setmsgType(Msg_Type);
                    leaveItems.setId(Id);
                    ItemsArrayForAsyncTaskOther.add(leaveItems);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            progressDialogOther.dismiss();
            adapterForAsyncTaskOther.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void OtherSMSList() {

        JSONObject jsonObject = new JSONObject();
        webMethName = "Other_SMS_List";
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("User_Id", userId);
            jsonObject.put("User_Type", userType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = context.getString(R.string.url) + webMethName;

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialogOther.dismiss();
                            ItemsArrayForAsyncTaskOther.clear();
                            adapterForAsyncTaskOther.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if (res.equals("Msg Not Available.")) {
                                Toast.makeText(context, "SMS Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        SMS_Inbox_Items leaveItems = new SMS_Inbox_Items();
                                        String Id = obj.getString("ID");
                                        String Msg_Type = obj.getString("Message_Type");
                                        String Msg = obj.getString("Message");
                                        String date = obj.getString("DateTime");

                                        String Date = date.replace("T"," ");
                                        leaveItems.setDate(Date);
                                        leaveItems.setmsgBody(Msg);
                                        leaveItems.setmsgType(Msg_Type);
                                        leaveItems.setId(Id);
                                        ItemsArrayForAsyncTaskOther.add(leaveItems);

                                        mydb = new DataBaseHelper(context);
                                        mydb.InsertSMS(Id, Msg_Type, Msg, Date, "", userId);
                                    }
                                    adapterForAsyncTaskOther.notifyDataSetChanged();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialogOther.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorBody(), Toast.LENGTH_LONG).show();
                    }
                });

    }

}
