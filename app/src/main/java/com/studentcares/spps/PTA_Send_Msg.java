package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.studentcares.spps.connectivity.PTA_Members_List_Get;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.HashMap;

public class PTA_Send_Msg extends BaseActivity implements View.OnClickListener {

    EditText txtMsgBody;
    String msgBody;
    String schoolId;
    String userId;
    String userType_storeMsg = "PTA";
    Button btnSend;
    private ProgressDialog progressDialog;
    String mobileNo_For_sms;
    String id_For_notifications;
    SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_send);

        ActivityInfo activityInfo = null;
        try {
            activityInfo = getPackageManager().getActivityInfo(
                    getComponentName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String title = activityInfo.loadLabel(getPackageManager())
                .toString();

        txtActivityName.setText(title);

        Intent intent = getIntent();
        if (null != intent) {
            mobileNo_For_sms = intent.getStringExtra("mobileNo_For_sms");
            id_For_notifications = intent.getStringExtra("id_For_notifications");
        }
        sessionManager = new SessionManager(this);
        HashMap<String, String> typeOfUser = sessionManager.getUserDetails();
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        userId = typeOfUser.get(SessionManager.KEY_USERID);

        txtMsgBody = (EditText) findViewById(R.id.txtMsg);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnSend) {
            msgBody = txtMsgBody.getText().toString();
            if (msgBody.equals("") || msgBody == null) {
                Toast.makeText(this,"Please Enter Your Msg.", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait, Message Is Sending.");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                PTA_Members_List_Get sendSmsFromServer = new PTA_Members_List_Get(this);
                sendSmsFromServer.SendSmsFromServer(schoolId, mobileNo_For_sms,id_For_notifications, msgBody, userId,userType_storeMsg, progressDialog);
            }
        }
    }
}