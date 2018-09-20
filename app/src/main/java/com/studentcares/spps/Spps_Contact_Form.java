package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.studentcares.spps.connectivity.StudentCares_Feedback_ContactUs;
import com.studentcares.spps.internetConnectivity.NetworkChangeReceiver;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.HashMap;

public class Spps_Contact_Form extends BaseActivity implements View.OnClickListener {

    EditText txtName;
    EditText txtEmail;
    EditText txtContactNo;
    EditText txtMessage;
    Button btnSubmit;

    String name = "";
    String contactNo = "";
    String email = "";
    String message = "";
    String userId = "";
    String schoolId = "";

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spps_contact_form);

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

        SessionManager sessionManagerNgo = new SessionManager(this);
        HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
        userId = typeOfUser.get(SessionManager.KEY_USERID);
        email = typeOfUser.get(SessionManager.KEY_USEREMAIL);
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        name = typeOfUser.get(SessionManager.KEY_NAME);
        contactNo = typeOfUser.get(SessionManager.KEY_CONTACTNO);

        txtName = (EditText) findViewById(R.id.txtName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtContactNo = (EditText) findViewById(R.id.txtContactNo);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        if(!email.equals("null") || !email.equals("") || email != null){
            txtEmail.setText(email);
        }
        txtName.setText(name);
        txtContactNo.setText(contactNo);
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.btnSubmit) {
            name = txtName.getText().toString();
            email = txtEmail.getText().toString();
            contactNo = txtContactNo.getText().toString();
            message = txtMessage.getText().toString();

            if(name.equals("") || name == null){
                Toast.makeText(this, "Please Enter your Name.", Toast.LENGTH_SHORT).show();
            }
            else if(email.equals("") || email == null){
                Toast.makeText(this, "Please Enter your Email", Toast.LENGTH_SHORT).show();
            }
            else if(contactNo.equals("") || contactNo == null){
                Toast.makeText(this, "Please Enter your Contact No", Toast.LENGTH_SHORT).show();
            }
            else if(message.equals("") || message == null){
                Toast.makeText(this, "Please Enter your Message", Toast.LENGTH_SHORT).show();
            }
            else{
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please wait.");
                progressDialog.show();
                progressDialog.show();
                message = "Message: "+message + ". UserDetails: " +" School Id :"+schoolId +" UserId :"+userId +" Username: "+name +" Contact No: "+contactNo +" Email: "+email ;
                StudentCares_Feedback_ContactUs spps_FeedbackContactUs = new StudentCares_Feedback_ContactUs(Spps_Contact_Form.this);
                spps_FeedbackContactUs.sppsContactUs(message,progressDialog);
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = this.getPackageManager();
        ComponentName component = new ComponentName(this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = this.getPackageManager();
        ComponentName component = new ComponentName(this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}