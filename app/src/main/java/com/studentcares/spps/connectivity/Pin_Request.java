package com.studentcares.spps.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.About_StudentCares;
import com.studentcares.spps.Login;
import com.studentcares.spps.Pin_Enter;
import com.studentcares.spps.Pin_Set;
import com.studentcares.spps.R;
import com.studentcares.spps.sessionManager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class Pin_Request {

    SessionManager sessionManager;
    private static Context context;
    private static ProgressDialog progressDialog;
    private static String userId,userType;
    private static String schoolId;
    private static String pin;
    private static String webMethName;

    public Pin_Request(Pin_Set pin_set) {
        context = pin_set;
    }

    public void SetPin(String idOfSchhol, String typeOfUser,String idOfUser, String confirmPin, ProgressDialog pd) {
        schoolId = idOfSchhol;
        userType = typeOfUser;
        userId = idOfUser;
        pin = confirmPin;
        progressDialog = pd;

        webMethName = "Set_User_Pin";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("User_Id", userId);
            jsonObject.put("User_Type", userType);
            jsonObject.put("Security_Pin", pin);
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
                            if (res.equals("Security Pin Successfully Updated.")) {

                                Toast.makeText(context, "Pin Successfully Set", Toast.LENGTH_SHORT).show();
                                Intent gotoEnterPin = new Intent(context, Pin_Enter.class);
                                context.startActivity(gotoEnterPin);
                                sessionManager = new SessionManager(context);
                                sessionManager.SetPIN(pin);

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
