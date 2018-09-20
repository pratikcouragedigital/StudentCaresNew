package com.studentcares.spps.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.Attendance_Tab_Student;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Attendance_List_Items;
import com.studentcares.spps.webservice_common.T_Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Attendance_Student_Marking {
    private static Context context;
    private static String ResponseResult;
    private static String webMethName;
    private static String userId;
    private static String schoolId;
    private static String presentStudentIds;
    private static String absentStudentIds;
	private static String status = "";
	private static String studentId = "";
	private static String fromClassName = "";
    private  static ProgressDialog progressDialog;


    public Attendance_Student_Marking(Context presentList) {
        context = presentList;
    }

    public static void AddTodaysAttendance(String staffId, String presentIdOfStudent, String absentIdOfStudent,String fromClass,String btnStatus, String idOfSchool,ProgressDialog pd) {
        userId = staffId;
        presentStudentIds = presentIdOfStudent;
        absentStudentIds = absentIdOfStudent;
        schoolId = idOfSchool;
        fromClassName = fromClass;
        status = btnStatus;
        progressDialog = pd;

        JSONObject jsonObject = new JSONObject();

        if(status.equals("MarkAllAbsent")){
            webMethName = "Attendance_Mark_All_Absent";
            try {
                jsonObject.put("School_id", schoolId);
                jsonObject.put("Staff_Id", userId);
                jsonObject.put("Absent_Id", absentStudentIds);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(status.equals("MarkAllPresent")){
            webMethName = "Attendance_Mark_All_Present";
            try {
                jsonObject.put("School_id", schoolId);
                jsonObject.put("Staff_Id", userId);
                jsonObject.put("Present_Id", presentStudentIds);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(status.equals("MarkAbsentPresent")){
            webMethName = "Attendance_Mark_Present_Absent";
            try {
                jsonObject.put("School_id", schoolId);
                jsonObject.put("Staff_Id", userId);
                jsonObject.put("Present_Id", presentStudentIds);
                jsonObject.put("Absent_Id", absentStudentIds);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

                                SendAttendanceMSG();
                                Toast.makeText(context, "Attendance Added Successfully.", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(context, Attendance_Tab_Student.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(i);
                            }
                            else{
                                Intent i = new Intent(context, Attendance_Tab_Student.class);
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



    private static void SendAttendanceMSG() {

        JSONObject jsonObject = new JSONObject();

        if(status.equals("MarkAllAbsent")){
            webMethName = "Attendance_Send_SMS_Mark_All_Absent2";
            try {
                jsonObject.put("School_id", schoolId);
                jsonObject.put("Staff_Id", userId);
                jsonObject.put("Absent_Id", absentStudentIds);
                jsonObject.put("SMS_From", "A");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(status.equals("MarkAllPresent")){
            webMethName = "Attendance_Send_SMS_Mark_All_Present2";
            try {
                jsonObject.put("School_id", schoolId);
                jsonObject.put("Staff_Id", userId);
                jsonObject.put("Present_Id", presentStudentIds);
                jsonObject.put("SMS_From", "A");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(status.equals("MarkAbsentPresent")){
            webMethName = "Attendance_Send_SMS_Mark_Present_Absent2";
            try {
                jsonObject.put("School_id", schoolId);
                jsonObject.put("Staff_Id", userId);
                jsonObject.put("Present_Id", presentStudentIds);
                jsonObject.put("Absent_Id", absentStudentIds);
                jsonObject.put("SMS_From", "A");
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                       //do nothing
                        Toast.makeText(context, "SMS Sent..", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    public static void Remove_Student_Attendance(String staffId, String IdOfStudent,String fromClass, String idOfSchool,ProgressDialog pd) {
        userId = staffId;
        studentId = IdOfStudent;
        schoolId = idOfSchool;
        fromClassName = fromClass;
        progressDialog = pd;

        JSONObject jsonObject = new JSONObject();


        webMethName = "Attendance_Student_Remove";
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Staff_Id", userId);
            jsonObject.put("Absent_Id", absentStudentIds);
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
                            if (res.equals("Attendance Removed Successfully")) {

                                Intent i = new Intent(context, Attendance_Tab_Student.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(i);
                            }
                            else{
                                Intent i = new Intent(context, Attendance_Tab_Student.class);
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
