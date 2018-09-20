package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.internetConnectivity.CheckInternetConnection;
import com.studentcares.spps.internetConnectivity.NetworkChangeReceiver;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.sqlLite.DataBaseHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private long TIME = 5000;
    private EditText editTextUserId;
    private EditText editTextPassword;
    // private TextView txtForgotPassword;
    private Button btnSignIn;
    private ProgressDialog progressDialog;

    SessionManager sessionManager;
    public String saveUserType,saveUserId,saveName,saveStandard,saveDivision,saveSurName,userContactNo,userFullName,userEmail,userPhoto;
    public String saveSchoolId,schoolLogo,schoolName,schoolWebsite,schoolContactNo,schoolAddress,schoolEmail,schoolLat,schoolLongi;
    public String userAddress,userDob,userBloodGroup,userGrNo,userSwipeCard,userRollNo,otp,pin,token,deviceId,isTeachingStaff,isClassTeacher;
    private String userId,schoolId,userPassword,method,DeviceName,manufacturer,model,senderId;

    private List<String> studentIdList = new ArrayList<String>();
    private List<String> studentNameList = new ArrayList<String>();

    private DataBaseHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        sessionManager = new SessionManager(Login.this);
        HashMap<String, String> userToken = sessionManager.getUserFirebaseNotificationToken();
        token = userToken.get(SessionManager.KEY_TOKEN);
        deviceId = userToken.get(SessionManager.KEY_DEVICEID);
        manufacturer = userToken.get(SessionManager.KEY_MANUFACTURER);
        model = userToken.get(SessionManager.KEY_MODEL);

        schoolId = getString(R.string.schoolId);
        editTextUserId = (EditText) findViewById(R.id.txtLoginUserId);
        editTextPassword = (EditText) findViewById(R.id.txtLoginpassword);
//        txtForgotPassword = (TextView) findViewById(R.id.lblforgetpassword);
        btnSignIn = (Button) findViewById(R.id.btnlogin);

        //txtForgotPassword.setVisibility(View.GONE);
        btnSignIn.setOnClickListener(this);
//        txtForgotPassword.setOnClickListener(this);

        mydb = new DataBaseHelper(this);
    }

    @Override
    public void onClick(final View v) {

        if (CheckInternetConnection.getInstance(this).isOnline()) {
            if (v.getId() == R.id.btnlogin) {
                v.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setEnabled(true);
                    }
                }, TIME);
                userId = editTextUserId.getText().toString();
                userPassword = editTextPassword.getText().toString();

                if (editTextUserId.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Please Enter Your User Id.", Toast.LENGTH_LONG).show();
                } else if (editTextPassword.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Please Enter Password.", Toast.LENGTH_LONG).show();
                } else {
                    schoolId = userId.substring(0, userId.length() - 4);

                    progressDialog = new ProgressDialog(Login.this);
                    progressDialog.setMessage("Logging In.");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);

                    LoginRequest();
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet connection!");
            builder.setMessage("Please Check Your Internet Connection.");
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void LoginRequest() {
        method = "Login2";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Id", userId);
            jsonObject.put("Password", userPassword);
            jsonObject.put("School_id", schoolId);
        }
        catch (JSONException e) {
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
                            String res = response.getString("responseDetails");
                            if(res.equals("Invalid UserName & Password")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                builder.setTitle("Result");
                                builder.setMessage("Invalid UserName or Password");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface alert, int which) {
                                        // TODO Auto-generated method stub
                                        //Do something
                                        alert.dismiss();
                                    }
                                });
                                AlertDialog alert1 = builder.create();
                                alert1.show();
                            }
                            else{
                                sessionManager = new SessionManager(Login.this);
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);

                                        saveUserType = obj.getString("Usertype");
                                        saveUserId = obj.getString("Id");
                                        saveName = obj.getString("Name");
                                        saveSurName = obj.getString("Surname");
                                        userFullName = saveName + " " + saveSurName;
                                        int std = obj.getInt("StandardId");
                                        int div = obj.getInt("DivisionId");
                                        saveStandard = String.valueOf(std);
                                        saveDivision = String.valueOf(div);
                                        userContactNo = obj.getString("MobileNo");
                                        userEmail = obj.getString("Emailid");
                                        userDob = obj.getString("Dob");
                                        userPhoto = obj.getString("Image");
                                        userAddress = obj.getString("Address");
                                        userBloodGroup = obj.getString("Blood_Group");
                                        userGrNo = obj.getString("GR_No");
                                        userSwipeCard = obj.getString("Swipe_Card");
                                        userRollNo = obj.getString("Roll_No");
                                        saveSchoolId = obj.getString("School_Code");
                                        schoolLogo = obj.getString("Logo");
                                        schoolName = obj.getString("School_Name");
                                        schoolWebsite = obj.getString("school_Website");
                                        schoolContactNo = obj.getString("School_Phone_No");
                                        schoolAddress = obj.getString("School_Address");
                                        schoolEmail = obj.getString("School_Email");
                                        schoolLat = obj.getString("Latitude");
                                        schoolLongi = obj.getString("Longitude");
                                        pin = obj.getString("PIN");
                                        otp = obj.getString("OTP");
                                        senderId = obj.getString("SenderId");
                                        isTeachingStaff = obj.getString("IsTeachingStaff");
                                        isClassTeacher = obj.getString("IsClassTeacher");

                                        sessionManager.createUserLoginSession(saveUserId, saveUserType, userFullName,saveStandard,saveDivision, userContactNo, userEmail,userDob,userAddress,userBloodGroup,userGrNo,userSwipeCard,userRollNo,userPhoto,saveSchoolId, schoolLogo, schoolName, schoolContactNo, schoolAddress, schoolWebsite, schoolEmail,isTeachingStaff,schoolLat,schoolLongi,isClassTeacher,pin,senderId);
                                        mydb.insertUserDetails(saveUserType, saveUserId, userFullName,saveStandard,saveDivision, userContactNo, userEmail,userDob,userAddress,userBloodGroup,userGrNo,userSwipeCard,userRollNo, userPhoto, saveSchoolId, schoolLogo, schoolName, schoolWebsite, schoolContactNo, schoolAddress, schoolEmail,isTeachingStaff,schoolLat,schoolLongi,isClassTeacher,pin,senderId);
                                        sessionManager.ActiveUser(saveUserId, userFullName);

                                        AddDeviceDetails();
                                        Intent gotoOTP = new Intent(Login.this, My_OTP.class);
                                        gotoOTP.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        String imFrom = "Login";

                                        gotoOTP.putExtra("imFrom",imFrom);
                                        gotoOTP.putExtra("newMblNo","");
                                        gotoOTP.putExtra("oldMblNo","");
                                        gotoOTP.putExtra("OTP",otp);
                                        startActivity(gotoOTP);

                                    }
                                }
                                catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                        catch(Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Toast.makeText(Login.this, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(Login.this, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void AddDeviceDetails() {
        method ="Add_User_Device_Details";
        DeviceName =manufacturer +" "+ model;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("User_id", userId);
            jsonObject.put("TokenNo", token);
            jsonObject.put("Device_id", deviceId);
            jsonObject.put("UserType", saveUserType);
            jsonObject.put("School_id", schoolId);
            jsonObject.put("DeviceName", DeviceName);
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
                        //do nothing
                        try {
                            String res = response.getString("responseDetails");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        AddDeviceDetails();
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();

        PackageManager pm = Login.this.getPackageManager();
        ComponentName component = new ComponentName(Login.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

    }

    @Override
    protected void onResume() {
        super.onResume();

        PackageManager pm = Login.this.getPackageManager();
        ComponentName component = new ComponentName(Login.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
