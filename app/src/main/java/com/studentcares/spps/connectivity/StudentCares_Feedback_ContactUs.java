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
import com.studentcares.spps.About_StudentCares;
import com.studentcares.spps.Notice_List;
import com.studentcares.spps.R;
import com.studentcares.spps.Spps_Contact_Form;
import com.studentcares.spps.Spps_Feedback_Form;


import org.json.JSONException;
import org.json.JSONObject;

public class StudentCares_Feedback_ContactUs {

    private static Context context;
    private static ProgressDialog progressDialog;
    private static String userId;
    private static String schoolId;
    private static String name;
    private static String email;
    private static String contactNo;
    private static String message;
    private static String counter;
    private static String webMethName;
    private static String ResponseResult;

    public StudentCares_Feedback_ContactUs(Spps_Contact_Form spps_Contact_Form) {
        context = spps_Contact_Form;
    }

    public StudentCares_Feedback_ContactUs(Spps_Feedback_Form spps_feedback_form) {
        context = spps_feedback_form;
    }

    public void sppsContactUs(String messageOfUser, ProgressDialog contactUsProgressDialog) {
        message = messageOfUser;
        progressDialog = contactUsProgressDialog;

        webMethName = "StudentCares_Contactus";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Message", message);
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
                        progressDialog.dismiss();
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Contact Send Successfully.")) {

                                Toast.makeText(context, "Thank you, We will contact you ASAP..", Toast.LENGTH_SHORT).show();
                                Intent gotoStudentcares = new Intent(context, About_StudentCares.class);
                                context.startActivity(gotoStudentcares);

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
                        progressDialog.dismiss();
                    }
                });
    }

    public void addSppsFeedback(String feedback, ProgressDialog feedbackProgressDialog) {
        message = feedback;
        progressDialog = feedbackProgressDialog;

        webMethName = "StudentCares_Feedback";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Message", message);
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
                        progressDialog.dismiss();
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Feedback Send Successfully.")) {

                                Toast.makeText(context, "Thank you for giving your important feedback..", Toast.LENGTH_SHORT).show();

                                Intent gotoStudentcares = new Intent(context, About_StudentCares.class);
                                context.startActivity(gotoStudentcares);

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
                        progressDialog.dismiss();
                    }
                });
    }

}