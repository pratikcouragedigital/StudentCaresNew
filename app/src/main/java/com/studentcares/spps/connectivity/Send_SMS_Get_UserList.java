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
import com.studentcares.spps.SMS_Send_User_List;
import com.studentcares.spps.model.Send_SMS_User_Items;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Send_SMS_Get_UserList {

    private static Context context;
    private static String schoolId;
    private static String standardId;
    private static String divisionId;
    private static String webMethName = "";
    private static String ResponseResult;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTask;
    private static List<Send_SMS_User_Items> ItemsArrayForAsyncTask;
    private static ProgressDialog progressDialogBox;

    public Send_SMS_Get_UserList(SMS_Send_User_List PRStudentList_forSMS) {
        context = PRStudentList_forSMS;
    }

    public void StudentList(List<Send_SMS_User_Items> studentItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String standard, String division, String idOfSchool, ProgressDialog progressDialog) {
        standardId = standard;
        divisionId = division;
        schoolId = idOfSchool;
        progressDialogBox = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = studentItems;

        webMethName = "Send_SMS_Student_List";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("standardId", standardId);
            jsonObject.put("divisionId", divisionId);
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
                            if (res.equals("Student List Not Available.")) {
                                Toast.makeText(context, "Student List Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        Send_SMS_User_Items studentFor_SMS_Items = new Send_SMS_User_Items();
                                        Send_SMS_User_Items PRStudentFor_SMS_Items = new Send_SMS_User_Items();
                                        PRStudentFor_SMS_Items.setName(obj.getString("student_Name"));
//                            PRStudentFor_SMS_Items.setstandard(obj.getString("standard"));
//                            PRStudentFor_SMS_Items.setdivision(obj.getString("division"));
                                        PRStudentFor_SMS_Items.setId(obj.getString("student_Id"));
                                        PRStudentFor_SMS_Items.setFirstImagePath(obj.getString("photo"));
                                        PRStudentFor_SMS_Items.setmobileNo(obj.getString("mobileNo"));
                                        ItemsArrayForAsyncTask.add(PRStudentFor_SMS_Items);

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

    public void StaffList(List<Send_SMS_User_Items> studentItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfSchool, ProgressDialog progressDialog) {
        schoolId = idOfSchool;
        progressDialogBox = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = studentItems;

        webMethName = "Send_SMS_Staff_List";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
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
                            if (res.equals("Staff List Not Available.")) {
                                Toast.makeText(context, "Staff List Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        Send_SMS_User_Items studentFor_SMS_Items = new Send_SMS_User_Items();
                                        studentFor_SMS_Items.setName(obj.getString("staff_Name"));
                                        studentFor_SMS_Items.setId(obj.getString("staff_Id"));
                                        studentFor_SMS_Items.setFirstImagePath(obj.getString("photo"));
                                        studentFor_SMS_Items.setmobileNo(obj.getString("mobileNo"));
                                        ItemsArrayForAsyncTask.add(studentFor_SMS_Items);
                                        adapterForAsyncTask.notifyDataSetChanged();
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

}
