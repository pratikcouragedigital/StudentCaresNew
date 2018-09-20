package com.studentcares.spps;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.internetConnectivity.NetworkChangeReceiver;
import com.studentcares.spps.sessionManager.SessionManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Emergency_Exit extends BaseActivity implements View.OnClickListener {

    EditText txtRegistrationId;
    EditText txtReason;
    Button btnSubmit;
    RadioButton rbStaff;
    RadioButton rbStudent;

    private static String reason = "";
    private static String registrationId = "";
    private static String  userId;
    private static String schoolId;
    private static String EmergencyExitUserType;
    private static String ResponseResult;
    private static ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_exit);

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
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);

        txtReason = (EditText) findViewById(R.id.txtReason);
        txtRegistrationId = (EditText) findViewById(R.id.txtRegistrationId);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        rbStaff = (RadioButton) findViewById(R.id.rbStaff);
        rbStudent = (RadioButton) findViewById(R.id.rbStudent);

        btnSubmit.setOnClickListener(this);

        rbStaff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbStaff.isChecked()) {
                    rbStudent.setChecked(false);
                    EmergencyExitUserType= "Staff";
                }
            }
        });

        rbStudent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbStudent.isChecked()) {
                    rbStaff.setChecked(false);
                    EmergencyExitUserType= "Student";
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnSubmit) {
            reason = txtReason.getText().toString();
            registrationId = txtRegistrationId.getText().toString();

            if (reason.equals("")) {
                Toast.makeText(this, "Please Enter Reason.", Toast.LENGTH_SHORT).show();
            } else if (registrationId.equals("")) {
                Toast.makeText(this, "Please Enter Registration Id.", Toast.LENGTH_SHORT).show();
            } else {

                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);


                AddEmergencyExit();
            }
        }
    }

    private void AddEmergencyExit() {
        String method = "Attendance_Emergency_Exit";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("School_id", schoolId);
            jsonObject.put("type", EmergencyExitUserType);
            jsonObject.put("registrationId", registrationId);
            jsonObject.put("reason", reason);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.url) + method;

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(method)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            try {
                                ResponseResult = response.getString("responseDetails");
                                if (ResponseResult.equals("Not Valid Registration Id..") ) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Emergency_Exit.this);
                                    builder.setTitle("Result");
                                    builder.setMessage("Please enter valid registration id..");
                                    AlertDialog alert1 = builder.create();
                                    alert1.show();
                                }
                                else if (ResponseResult.equals("No Network Found")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Emergency_Exit.this);
                                    builder.setTitle("Result");
                                    builder.setMessage("There is Some Network Issues Please Try Again Later.");
                                    AlertDialog alert1 = builder.create();
                                    alert1.show();
                                }
                                else if (ResponseResult.equals("In Time not marked")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Emergency_Exit.this);
                                    builder.setTitle("Result");
                                    builder.setMessage("In time not marked");
                                    AlertDialog alert1 = builder.create();
                                    alert1.show();
                                }
                                else if (ResponseResult.equals("Emergency Attendance Successfully Added.")) {

                                    Toast.makeText(Emergency_Exit.this, "Successfully Done.", Toast.LENGTH_SHORT).show();
                                    Intent gotoHome = new Intent(Emergency_Exit.this,Home_Menu.class);
                                    gotoHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(gotoHome);

                                }

                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                        catch(Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Toast.makeText(Emergency_Exit.this, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(Emergency_Exit.this, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        PackageManager pm = this.getPackageManager();
        ComponentName component = new ComponentName(this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onResume() {
        super.onResume();
        PackageManager pm = this.getPackageManager();
        ComponentName component = new ComponentName(this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
