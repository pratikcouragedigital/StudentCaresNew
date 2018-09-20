package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.studentcares.spps.connectivity.Pin_Request;
import com.studentcares.spps.connectivity.StudentCares_Feedback_ContactUs;
import com.studentcares.spps.internetConnectivity.NetworkChangeReceiver;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.HashMap;

public class Pin_Set extends BaseActivity implements View.OnClickListener {

    EditText txtPin;
    EditText txtConfirmPin;
    Button btnSubmit;
    String pin = "";
    String userType,userId = "";
    String schoolId = "";
    String confirmPin;

    private ProgressDialog progressDialog;
	boolean doubleBackToExitPressedOnce = false;


	@Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_set);

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
        userType = typeOfUser.get(SessionManager.KEY_USERTYPE);
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        //pin = typeOfUser.get(SessionManager.KEY_PIN);

        txtPin = (EditText) findViewById(R.id.txtPin);
        txtConfirmPin = (EditText) findViewById(R.id.txtConfirmPin);

       
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
    }
    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.btnSubmit) {
            pin = txtPin.getText().toString();
            confirmPin = txtConfirmPin.getText().toString();

            int pinLength = pin.length();
            int confirmPinLength = confirmPin.length();

            if(pin.equals("") || pin == null){
                Toast.makeText(this, "Please Enter your Pin", Toast.LENGTH_SHORT).show();
            }
//            else if(pinLength > 4 && pinLength < 4 ){
            else if(pinLength != 4){
                txtPin.setError("Please Set 4 Digits Pin.");
            }
            else if(confirmPin.equals("") || confirmPin == null){
                Toast.makeText(this, "Please Enter your Confirm, pin", Toast.LENGTH_SHORT).show();
            }
//            else if(confirmPinLength > 4 && confirmPinLength < 4){
            else if(confirmPinLength  != 4){
                txtConfirmPin.setError("Please Set 4 Digits Pin.");
            }
            else if(!pin.equals(confirmPin)){
                Toast.makeText(this, "Please Enter Same Pin", Toast.LENGTH_SHORT).show();
            }


            else{

                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Please wait.");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);

                    Pin_Request pinRequest = new Pin_Request(Pin_Set.this);
                    pinRequest.SetPin(schoolId,userType,userId,confirmPin,progressDialog);
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
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}
