package com.studentcares.spps;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.studentcares.spps.internetConnectivity.NetworkChangeReceiver;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.HashMap;

public class Pin_Enter extends BaseActivity implements View.OnClickListener {

    EditText txtPin;
    TextView txtResetPin;
    Button btnSubmit;
    String pin = "";
    String userEnteredPin = "";
    String userId = "";
    String schoolId = "";
    String contactNo;

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
        setContentView(R.layout.pin_enter);

        SessionManager sessionManagerNgo = new SessionManager(this);
        HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
        userId = typeOfUser.get(SessionManager.KEY_USERID);
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        pin = typeOfUser.get(SessionManager.KEY_PIN);
        contactNo = typeOfUser.get(SessionManager.KEY_PIN);

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

        txtPin = (EditText) findViewById(R.id.txtPin);
        txtResetPin = (TextView) findViewById(R.id.txtResetPin);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        txtResetPin.setOnClickListener(this);
    }
    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.btnSubmit) {
            userEnteredPin = txtPin.getText().toString();

            int pinLength = userEnteredPin.length();

            if(userEnteredPin.equals("") || userEnteredPin == null){
                Toast.makeText(this, "Please Enter your Pin.", Toast.LENGTH_SHORT).show();
            }
//            else if(pinLength > 4 && pinLength < 4){
            else if(pinLength != 4){
                txtPin.setError("Please Set 4 Digits Pin.");
            }
            else if(!userEnteredPin.equals(pin)){
                Toast.makeText(this, "You Entered Wrong Pin.", Toast.LENGTH_SHORT).show();
            }

            else{
                Intent gotoHome = new Intent(this,Home_Menu.class);
                startActivity(gotoHome);
            }
        }
        else if (v.getId() == R.id.txtResetPin) {

            Toast.makeText(this, "OTP sent on your registered no.", Toast.LENGTH_SHORT).show();
            String imFrom = "EnterPin";
            Intent gotoOTP = new Intent(this,My_OTP.class);
            gotoOTP.putExtra("imFrom",imFrom);
            gotoOTP.putExtra("newMblNo","");
            gotoOTP.putExtra("oldMblNo","");
            gotoOTP.putExtra("OTP","");
            startActivity(gotoOTP);
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