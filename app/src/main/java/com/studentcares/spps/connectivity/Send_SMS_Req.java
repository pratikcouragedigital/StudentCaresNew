package com.studentcares.spps.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.R;
import com.studentcares.spps.SMS_Report;
import com.studentcares.spps.SMS_Send_User_List;
import com.studentcares.spps.SMS_Send;


import org.json.JSONException;
import org.json.JSONObject;

public class Send_SMS_Req {

    private static Context context;
    private static ProgressDialog progressDialog;
    private static String schoolId;
    private static String mobile_ForSMS;
    private static String msgBody;
    private static String type;
    private static String webMethName;
    private static String ResponseResult;

    private static String id_ForSMS;
    private static String addedBy;
    private static String userType_storeMsg;

    public Send_SMS_Req(SMS_Send_User_List PRStudentList_forSMS) {
        context = PRStudentList_forSMS;
    }

    public Send_SMS_Req(SMS_Send PR_smsSend) {
        context = PR_smsSend;
    }

//    public Send_SMS_Req(PR_ForSMS_Staff_List PRStaff_list_forSMS) {
//        context = PRStaff_list_forSMS;
//    }

    public void SendSmsFromServeGroupWiser(String idOfSchool, String groupIds_ForSMS, String msg, String typeForMsg, String userId, ProgressDialog smsProgressDialog) {

        schoolId = idOfSchool;
        mobile_ForSMS = groupIds_ForSMS;
        userType_storeMsg = typeForMsg;
        msgBody = msg;
        addedBy = userId;
        progressDialog = smsProgressDialog;

        webMethName = "Send_SMS_Groupwise2";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("addedBy", addedBy);
            jsonObject.put("groupIds", mobile_ForSMS);
            jsonObject.put("toType", userType_storeMsg);
            jsonObject.put("msgBody", msgBody);
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
                        progressDialog.dismiss();
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Msg Successfully Sent.")) {
                                Toast.makeText(context,"Msg Successfully Sent.", Toast.LENGTH_SHORT).show();
                                Intent first = new Intent(context, SMS_Report.class);
                                first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(first);
                            }
                            else {
//                                Toast.makeText(context," "+res, Toast.LENGTH_SHORT).show();
                                Intent first = new Intent(context, SMS_Report.class);
                                first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(first);
                            }
                        } catch (Exception e) {
                            e.getMessage();
//                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                            Intent first = new Intent(context, SMS_Report.class);
                            first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(first);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
//                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        Intent first = new Intent(context, SMS_Report.class);
                        first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(first);
                    }
                });
    }


    public void SendSmsAllStudent(String schoolId, String standardId, String divisionId, String userType_storeMsg, String msgBody, String userId, final ProgressDialog progressDialog) {
        webMethName = "Send_SMS_Classwise2";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);

            jsonObject.put("standardId", standardId);
            jsonObject.put("divisionId", divisionId);
            jsonObject.put("msgBody", msgBody);
            jsonObject.put("addedBy", userId);
            jsonObject.put("toType", userType_storeMsg);
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
                        progressDialog.dismiss();
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Msg Successfully Sent.")) {
                                Toast.makeText(context,"Msg Successfully Sent.", Toast.LENGTH_SHORT).show();
                                Intent first = new Intent(context, SMS_Report.class);
                                first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(first);

                            } else {
                                Intent first = new Intent(context, SMS_Report.class);
                                first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(first);
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            //Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                            Intent first = new Intent(context, SMS_Report.class);
                            first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(first);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                        //Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        Intent first = new Intent(context, SMS_Send.class);
                        first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(first);
                    }
                });
    }

    public void SendSmsFromServer(String idOfSchool, String mobileNo, String id, String userType, String msg, String typeForMsg, String userId, ProgressDialog smsProgressDialog) {
        webMethName = "Send_SMS_Selected_User2";
        schoolId = idOfSchool;
        mobile_ForSMS = mobileNo;
        id_ForSMS = id;
        userType_storeMsg = userType;
        msgBody = msg;
        type = typeForMsg;
        addedBy = userId;
        progressDialog = smsProgressDialog;


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("toMobileNos", mobile_ForSMS);
            jsonObject.put("toIds", id_ForSMS);
            jsonObject.put("toType", userType_storeMsg);
            jsonObject.put("msgBody", msgBody);
            jsonObject.put("addedBy", addedBy);
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
                        progressDialog.dismiss();
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Msg Successfully Sent.")) {
                                Toast.makeText(context,"Msg Successfully Sent.", Toast.LENGTH_SHORT).show();
                                Intent first = new Intent(context, SMS_Report.class);
                                first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(first);

                            } else {
//                                Toast.makeText(context," "+res, Toast.LENGTH_SHORT).show();
                                Intent first = new Intent(context, SMS_Report.class);
                                first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(first);
                            }
                        } catch (Exception e) {
                            e.getMessage();
//                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                            Intent first = new Intent(context, SMS_Report.class);
                            first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(first);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
//                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        Intent first = new Intent(context, SMS_Report.class);
                        first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(first);
                    }
                });
    }

    public void SendSmsAllStaff(String schoolId,String userType_storeMsg, String msgBody, String userId, final ProgressDialog progressDialog) {
        webMethName = "Send_SMS_Staff_All2";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("msgBody", msgBody);
            jsonObject.put("userId", userId);
            jsonObject.put("toType", userType_storeMsg);
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
                        progressDialog.dismiss();
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Msg Successfully Sent.")) {
                                Toast.makeText(context,"Msg Successfully Sent.", Toast.LENGTH_SHORT).show();
                                Intent first = new Intent(context, SMS_Report.class);
                                first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(first);

                            } else {
//                                Toast.makeText(context," "+res, Toast.LENGTH_SHORT).show();
                                Intent first = new Intent(context, SMS_Report.class);
                                first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(first);
                            }
                        } catch (Exception e) {
                            e.getMessage();
//                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                            Intent first = new Intent(context, SMS_Report.class);
                            first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(first);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
//                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        Intent first = new Intent(context, SMS_Report.class);
                        first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(first);
                    }
                });
    }

    public void SendMsgToAll(String idOfSchool, String msg, String smsType, String userId, ProgressDialog smsProgressDialog) {
        schoolId = idOfSchool;
        msgBody = msg;
        addedBy = userId;
        type = smsType;
        progressDialog = smsProgressDialog;

        webMethName = "Send_SMS_All2";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("msgBody", msgBody);
            jsonObject.put("userId", addedBy);
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
                        progressDialog.dismiss();
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Msg Successfully Sent.")) {
                                Toast.makeText(context,"Msg Successfully Sent.", Toast.LENGTH_SHORT).show();
                                Intent first = new Intent(context, SMS_Report.class);
                                first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(first);

                            } else {
//                                Toast.makeText(context," "+res, Toast.LENGTH_SHORT).show();
                                Intent first = new Intent(context, SMS_Report.class);
                                first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(first);
                            }
                        } catch (Exception e) {
                            e.getMessage();
//                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                            Intent first = new Intent(context, SMS_Report.class);
                            first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(first);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
//                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        Intent first = new Intent(context, SMS_Report.class);
                        first.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(first);
                    }
                });
    }

}