package com.studentcares.spps.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.PTA_Members_Tab;

import com.studentcares.spps.model.PTA_Member_Items;
import com.studentcares.spps.sqlLite.DataBaseHelper;
import com.studentcares.spps.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PTA_Members_List_Get {

    private static Context context;
    private static String ResponseResult;
    private static String webMethName;
    private static String schoolId;
    private static String ptaType;
    private static String ptaTypeSec;
    private static ProgressDialog progressDialogBox;
    private static String mobileNo_Sms;
    private static String msgBody;
    private static String ids_Notification;
    private static String userId;
    private static String msg_toType;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTask;
    private static List<PTA_Member_Items> ItemsArrayForAsyncTask;


    private static RecyclerView.Adapter adapterForAsyncTaskSec;
    private static RecyclerView recyclerViewForAsyncTaskSec;
    private static List<PTA_Member_Items> ItemsArrayForAsyncTaskSec;
    private static DataBaseHelper mydb;

    public PTA_Members_List_Get(FragmentActivity activity) {
        context = activity;
    }

    public PTA_Members_List_Get(Context primaryPTA) {
        context = primaryPTA;
    }

    public void SendSmsFromServer(String idOfSchool, String mobileNo, String id_For_notifications, String msg, String idOfUser,String toType,ProgressDialog smsProgressDialog) {

        schoolId = idOfSchool;
        mobileNo_Sms = mobileNo;
        ids_Notification = id_For_notifications;
        msgBody = msg;
        userId = idOfUser;
        msg_toType = toType;
        progressDialogBox = smsProgressDialog;

        webMethName = "PTA_Send_SMS2";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("userId", userId);
            jsonObject.put("mobileNos", mobileNo_Sms);
            jsonObject.put("ids", ids_Notification);
            jsonObject.put("msgBody", msgBody);
            jsonObject.put("toType", msg_toType);
            jsonObject.put("SMS_From", "A");
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
                        progressDialogBox.dismiss();
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Msg Successfully Sent.")) {

                                Intent gotoHomeList = new Intent(context, PTA_Members_Tab.class);
                                gotoHomeList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoHomeList);

                            } else {
                                Toast.makeText(context," "+res, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                        progressDialogBox.dismiss();
                    }
                });
    }

    public void showPtaMenList(List<PTA_Member_Items> listItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfSchool, String typeOfPta, ProgressDialog progressDialog) {

        progressDialogBox = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = listItems;
        schoolId = idOfSchool;
        ptaType = typeOfPta;

        webMethName = "PTA_Member_List";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Group_Id", ptaType);
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
                        progressDialogBox.dismiss();
                        try {
                            ItemsArrayForAsyncTask.clear();
                            adapterForAsyncTask.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if (res.equals("Not found PTA Member..!!")) {
                                Toast.makeText(context, "PTA Members Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        PTA_Member_Items items = new PTA_Member_Items();
                                        items.setstudentId(obj.getString("Student_Registration_Id"));
                                        items.setname(obj.getString("Name"));
                                        items.setcontactNo(obj.getString("Father_Moblie_No"));
                                        items.setstandard(obj.getString("Standard_Name"));
                                        items.setdivision(obj.getString("Division_Name"));
                                        items.setAddress(obj.getString("Address"));
                                        if (!obj.getString("FatherImage").isEmpty() && obj.getString("FatherImage") != null) {
                                            items.setfatherImagePath(obj.getString("FatherImage"));
                                        }
                                        if (!obj.getString("Image").isEmpty() && obj.getString("Image") != null) {
                                            items.setstudentImagePath(obj.getString("Image"));
                                        }
                                        //                            mydb = new DataBaseHelper(context);
                                        //                            if (mydb.insertNoticeDetails((obj.getString("ListId")),(obj.getString("Name")), (obj.getString("Moblie_No")), (obj.getString("Standard_Name")), (obj.getString("Division_Name")), (obj.getString("Address")), (obj.getString("FatherImage")))) {
                                        //                                Toast.makeText(context, "PTA Member Added.", Toast.LENGTH_SHORT).show();
                                        //                            } else {
                                        //                                Toast.makeText(context, "Already Exist.", Toast.LENGTH_SHORT).show();
                                        //                            }
                                        ItemsArrayForAsyncTask.add(items);
                                    }
                                    adapterForAsyncTask.notifyDataSetChanged();
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
                        progressDialogBox.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    public void showSecondaryPtaMenList(List<PTA_Member_Items> listItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfSchool, String typeOfPta) {

//        progressDialogBox = progressDialog;
        adapterForAsyncTaskSec = reviewAdapter;
        recyclerViewForAsyncTaskSec = recyclerView;
        ItemsArrayForAsyncTaskSec = listItems;
        schoolId = idOfSchool;
        ptaTypeSec = typeOfPta;

        webMethName = "PTA_Member_List";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Group_Id", ptaTypeSec);
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
                            ItemsArrayForAsyncTaskSec.clear();
                            adapterForAsyncTaskSec.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if (res.equals("Not found PTA Member..!!")) {
                                Toast.makeText(context, "PTA Members Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        PTA_Member_Items items = new PTA_Member_Items();
                                        items.setstudentId(obj.getString("Student_Registration_Id"));
                                        items.setname(obj.getString("Name"));
                                        items.setcontactNo(obj.getString("Father_Moblie_No"));
                                        items.setstandard(obj.getString("Standard_Name"));
                                        items.setdivision(obj.getString("Division_Name"));
                                        items.setAddress(obj.getString("Address"));
                                        if (!obj.getString("FatherImage").isEmpty() && obj.getString("FatherImage") != null) {
                                            items.setfatherImagePath(obj.getString("FatherImage"));
                                        }
                                        if (!obj.getString("Image").isEmpty() && obj.getString("Image") != null) {
                                            items.setstudentImagePath(obj.getString("Image"));
                                        }
                                        //                            mydb = new DataBaseHelper(context);
                                        //                            if (mydb.insertNoticeDetails((obj.getString("ListId")),(obj.getString("Name")), (obj.getString("Moblie_No")), (obj.getString("Standard_Name")), (obj.getString("Division_Name")), (obj.getString("Address")), (obj.getString("FatherImage")))) {
                                        //                                Toast.makeText(context, "PTA Member Added.", Toast.LENGTH_SHORT).show();
                                        //                            } else {
                                        //                                Toast.makeText(context, "Already Exist.", Toast.LENGTH_SHORT).show();
                                        //                            }
                                        ItemsArrayForAsyncTaskSec.add(items);
                                    }
                                    adapterForAsyncTaskSec.notifyDataSetChanged();
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
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
