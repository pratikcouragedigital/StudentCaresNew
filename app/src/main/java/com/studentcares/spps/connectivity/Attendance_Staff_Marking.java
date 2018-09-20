package com.studentcares.spps.connectivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.Attendance_Tab_Staff;
import com.studentcares.spps.Attendance_Tab_Student;
import com.studentcares.spps.R;
import com.studentcares.spps.webservice_common.T_Webservice;


import org.json.JSONException;
import org.json.JSONObject;

public class Attendance_Staff_Marking {
    private static Context context;
    private static String ResponseResult;
    private static String webMethName;
    private static String userId;
    private static String staffId;
    private static String selectedStaffIds;
    private static String schoolId;
    private static ProgressDialog progressDialog;

    public Attendance_Staff_Marking(Context c) {
        context = c;
    }

    public static void AddTodaysAttendance(String idOfUser, String idOfStaff,String idOfSchool,ProgressDialog pd) {
        userId = idOfUser;
        staffId = idOfStaff;
        schoolId = idOfSchool;
        progressDialog = pd;

        JSONObject jsonObject = new JSONObject();
        webMethName = "Attendance_Mark_Staff_Present";
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Staff_Id", userId);
            jsonObject.put("Present_Id", selectedStaffIds);
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
                            progressDialog.dismiss();
                            String res = response.getString("responseDetails");
                            if (res.equals("Attendance Added")) {

                                Toast.makeText(context, "Attendance Added Successfully.", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(context, Attendance_Tab_Staff.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(i);
                            }
                            else{
                                Intent i = new Intent(context, Attendance_Tab_Staff.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(i);
                            }
                        }
                        catch(Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    public static void Remove_Staff_Attendance(String idOfUser, String idOfStaff,String idOfSchool,ProgressDialog pd) {
        userId = idOfUser;
        selectedStaffIds = idOfStaff;
        schoolId = idOfSchool;
        progressDialog = pd;

        JSONObject jsonObject = new JSONObject();
        webMethName = "Attendance_Mark_Staff_Absent";
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Staff_Id", userId);
            jsonObject.put("Absent_Id", selectedStaffIds);
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
                            progressDialog.dismiss();
                            String res = response.getString("responseDetails");
                            if (res.equals("Attendance Added")) {

                                Toast.makeText(context, "Attendance Added Successfully.", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(context, Attendance_Tab_Staff.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(i);
                            }
                            else{
                                Intent i = new Intent(context, Attendance_Tab_Staff.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(i);
                            }
                        }
                        catch(Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
