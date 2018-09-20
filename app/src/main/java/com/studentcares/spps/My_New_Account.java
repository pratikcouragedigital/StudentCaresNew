package com.studentcares.spps;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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


public class My_New_Account extends BaseActivity implements View.OnClickListener {

    public long TIME = 5000;
    public EditText editTextUserId;
    public EditText editTextPassword;
    public EditText txtOldMblNo;
    public EditText txtNewMblNo;
    public EditText txtuserId;
    public Button btnSignIn;
    public Button btnRemoveAccount;
    public Button btnSubmit;
    public ProgressDialog progressDialog;

    public String userId;
    public String userPassword;
    public String schoolId = "";
    public String method;
    public String loginResponseResult;

    SessionManager sessionManager;
    public String saveUserType = "";
    public String saveUserId = "";
    public String saveName = "";
    public String saveStandard = "";
    public String saveDivision = "";
    public String saveSurName = "";
    public String userContactNo = "";
    public String userFullName = "";
    public String userEmail = "";
    public String userDob = "";
    public String userPhoto = "";

    public String userAddress = "";
    public String userBloodGroup = "";
    public String userGrNo = "";
    public String userSwipeCard = "";
    public String userRollNo = "";

    public String saveSchoolId = "";
    public String schoolLogo = "";
    public String schoolName = "";
    public String schoolWebsite = "";
    public String schoolContactNo = "";
    public String schoolAddress = "";
    public String schoolEmail = "";
    public String schoolLat = "";
    public String schoolLongi = "";
    public String oldMblNo = "";
    public String newMblNo = "";
    public String isTeachingStaff = "";
    public String isClassTeacher = "";
    String DeviceName,manufacturer,model;

    Spinner removeAccountSpinner, addAccountSpinner;
    String studentName;
    String studentId;
    private String[] studentArrayList;
    private ProgressDialog studentDialogBox;
    private List<String> studentIdList = new ArrayList<String>();
    private List<String> studentNameList = new ArrayList<String>();
    ArrayAdapter<String> dataAdapte;

    RelativeLayout part1RelativeLayout;
    RelativeLayout part2RelativeLayout;
    RelativeLayout part3RelativeLayout;
    RelativeLayout part4RelativeLayout;
    RelativeLayout addAccountDetails;
    RelativeLayout removeAccountDetails;
    RelativeLayout switchAccount;
    RelativeLayout changeMobileNoLayout;

    RelativeLayout Layout1;
    RelativeLayout Layout2;
    RelativeLayout Layout3;
    RelativeLayout Layout4;
    RelativeLayout Layout5;
    RelativeLayout Layout6;

    int countForm1 = 0;
    int countForm2 = 0;
    int countForm3 = 0;
    int countForm4 = 0;

//    CardView Details1CardView;
//    CardView Details2CardView;
//    CardView Details3CardView;

    private DataBaseHelper mydb;
    private String otp, pin, senderId;
    private String userType;
    public String token = "";
    public String deviceId = "";
    int counter;
    ArrayAdapter<String> dataAdapterForRemoveAccount;
    ArrayAdapter<String> dataAdapterForSwitchAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_new_account);

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

        sessionManager = new SessionManager(this);
        HashMap<String, String> typeOfUser = sessionManager.getUserDetails();
        userType = typeOfUser.get(SessionManager.KEY_USERTYPE);
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        userId = typeOfUser.get(SessionManager.KEY_USERID);
        oldMblNo = typeOfUser.get(SessionManager.KEY_CONTACTNO);

        HashMap<String, String> userToken = sessionManager.getUserFirebaseNotificationToken();
        token = userToken.get(SessionManager.KEY_TOKEN);
        deviceId = userToken.get(SessionManager.KEY_DEVICEID);
        manufacturer = userToken.get(SessionManager.KEY_MANUFACTURER);
        model = userToken.get(SessionManager.KEY_MODEL);

        editTextUserId = (EditText) findViewById(R.id.txtLoginUserId);
        editTextPassword = (EditText) findViewById(R.id.txtLoginpassword);
        txtOldMblNo = (EditText) findViewById(R.id.txtOldMblNo);
        txtNewMblNo = (EditText) findViewById(R.id.txtMblNo);
        txtuserId = (EditText) findViewById(R.id.txtuserId);
        btnSignIn = (Button) findViewById(R.id.btnlogin);
        btnRemoveAccount = (Button) findViewById(R.id.btnRemoveAccount);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        part1RelativeLayout = (RelativeLayout) findViewById(R.id.part1RelativeLayout);
        part2RelativeLayout = (RelativeLayout) findViewById(R.id.part2RelativeLayout);
        part4RelativeLayout = (RelativeLayout) findViewById(R.id.part4RelativeLayout);
        addAccountDetails = (RelativeLayout) findViewById(R.id.addAccountDetails);
        removeAccountDetails = (RelativeLayout) findViewById(R.id.removeAccountDetails);
        switchAccount = (RelativeLayout) findViewById(R.id.switchAccount);
        changeMobileNoLayout = (RelativeLayout) findViewById(R.id.changeMobileNoLayout);
        part3RelativeLayout = (RelativeLayout) findViewById(R.id.part3RelativeLayout);
        Layout1 = (RelativeLayout) findViewById(R.id.Layout1);
        Layout2 = (RelativeLayout) findViewById(R.id.Layout2);
        Layout3 = (RelativeLayout) findViewById(R.id.Layout3);
        Layout5 = (RelativeLayout) findViewById(R.id.Layout5);
        Layout6 = (RelativeLayout) findViewById(R.id.Layout6);
//        Details1CardView = (CardView) findViewById(R.id.Details1CardView);
//        Details2CardView = (CardView) findViewById(R.id.Details2CardView);
//        Details3CardView = (CardView) findViewById(R.id.Details3CardView);
        removeAccountSpinner = (Spinner) findViewById(R.id.removeAccountSpinner);
        addAccountSpinner = (Spinner) findViewById(R.id.addAccountSpinner);

        part1RelativeLayout.setOnClickListener(this);
        part2RelativeLayout.setOnClickListener(this);
        part4RelativeLayout.setOnClickListener(this);
        part3RelativeLayout.setOnClickListener(this);
        Layout5.setOnClickListener(this);
        Layout6.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        btnRemoveAccount.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

//        Details1CardView.setVisibility(View.GONE);
//        Details2CardView.setVisibility(View.GONE);
//        Details3CardView.setVisibility(View.GONE);
        addAccountDetails.setVisibility(View.GONE);
        removeAccountDetails.setVisibility(View.GONE);
        switchAccount.setVisibility(View.GONE);
        changeMobileNoLayout.setVisibility(View.GONE);

        txtuserId.setText(userId);
        txtOldMblNo.setText(oldMblNo);

//        if (!userType.equals("Student")) {
//            Layout1.setVisibility(View.GONE);
//            Layout2.setVisibility(View.GONE);
//            Details3CardView.setVisibility(View.VISIBLE);
//        } else {
//            Layout1.setVisibility(View.VISIBLE);
//            Layout2.setVisibility(View.VISIBLE);
//
//        }

        counter = 1;
        mydb = new DataBaseHelper(this);
        getUserForSpinner();
        SwitchUserAccount();
    }

    private void SwitchUserAccount() {
        studentNameList.clear();
        studentIdList.clear();
        JSONArray userListArray = null;
        try {
            userListArray = mydb.getAllUserForSpinner();
            studentNameList.add("Select User");
            studentIdList.add("0");

            for (int i = 0; i < userListArray.length(); i++) {
                try {
                    JSONObject obj = userListArray.getJSONObject(i);
                    studentNameList.add(obj.getString("Name"));
                    studentIdList.add(obj.getString("userId"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, studentNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addAccountSpinner.setAdapter(dataAdapter);

        addAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    studentName = parent.getItemAtPosition(position).toString();
                    studentId = studentIdList.get(position);
                    sessionManager.ActiveUser(studentId, studentName);

                    //change session details
                    JSONArray selectedUserDetailsArray = null;
                    try {
                        selectedUserDetailsArray = mydb.getSelectedUserDetails(studentId);
                        for (int i = 0; i < selectedUserDetailsArray.length(); i++) {
                            try {
                                JSONObject obj = selectedUserDetailsArray.getJSONObject(i);

                                String saveUserType = obj.getString("Usertype");
                                String saveUserId = obj.getString("userId");
                                String userFullName = obj.getString("Name");
                                String saveStandard = obj.getString("Standard");
                                String saveDivision = obj.getString("Division");
                                String userContactNo = obj.getString("MobileNo");
                                String userEmail = obj.getString("Emailid");
                                String userDob = obj.getString("Dob");
                                String userPhoto = obj.getString("Image");
                                String userAddress = obj.getString("Address");
                                String userBloodGroup = obj.getString("BloodGroup");
                                String userGrNo = obj.getString("GrNo");
                                String userSwipeCard = obj.getString("SwipeCardNo");
                                String userRollNo = obj.getString("RolNo");
                                String saveSchoolId = obj.getString("School_Code");
                                String schoolLogo = obj.getString("School_Logo");
                                String schoolName = obj.getString("School_Name");
                                String schoolWebsite = obj.getString("school_Website");
                                String schoolContactNo = obj.getString("School_Phone_No");
                                String schoolAddress = obj.getString("School_Address");
                                String schoolEmail = obj.getString("School_Email");
                                String isTeachingStaff = obj.getString("isTeachingStaff");
                                String latitude = obj.getString("School_Lat");
                                String longitude = obj.getString("School_Longi");
                                String isClassTeacher = obj.getString("isClass_Teacher");
                                String pin = obj.getString("PIN");
                                String senderId = obj.getString("SenderId");
                                sessionManager.createUserLoginSession(saveUserId, saveUserType, userFullName, saveStandard, saveDivision, userContactNo, userEmail, userDob, userAddress, userBloodGroup, userGrNo, userSwipeCard, userRollNo, userPhoto, saveSchoolId, schoolLogo, schoolName, schoolContactNo, schoolAddress, schoolWebsite, schoolEmail, isTeachingStaff, latitude, longitude, isClassTeacher, pin, senderId);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent refreshActivity = new Intent(getApplicationContext(), Pin_Enter.class);
                    //refreshActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finishAffinity();
                    startActivity(refreshActivity);

                    //do same for image
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getUserForSpinner() {
        studentNameList.clear();
        studentIdList.clear();
        JSONArray userListArray = null;
        try {
            userListArray = mydb.getAllUserForSpinner();

            for (int i = 0; i < userListArray.length(); i++) {
                JSONObject obj2 = userListArray.getJSONObject(i);
                studentNameList.add(obj2.getString("Name"));
                studentIdList.add(obj2.getString("userId"));
                try {
                    if (counter == 2) {
                        if (i == 0) {
                            String studId = obj2.getString("userId");
                            String studName = obj2.getString("Name");
                            studentId = studId;
                            studentName = studName;
                            JSONArray selectedUserDetailsArray = null;
                            try {
                                selectedUserDetailsArray = mydb.getSelectedUserDetails(studId);

                                for (int j = 0; j < selectedUserDetailsArray.length(); j++) {
                                    try {
                                        JSONObject obj = selectedUserDetailsArray.getJSONObject(j);

                                        String saveUserType = obj.getString("Usertype");
                                        String saveUserId = obj.getString("userId");
                                        String userFullName = obj.getString("Name");
                                        String saveStandard = obj.getString("Standard");
                                        String saveDivision = obj.getString("Division");
                                        String userContactNo = obj.getString("MobileNo");
                                        String userEmail = obj.getString("Emailid");
                                        String userDob = obj.getString("Dob");
                                        String userPhoto = obj.getString("Image");
                                        String userAddress = obj.getString("Address");
                                        String userBloodGroup = obj.getString("BloodGroup");
                                        String userGrNo = obj.getString("GrNo");
                                        String userSwipeCard = obj.getString("SwipeCardNo");
                                        String userRollNo = obj.getString("RolNo");
                                        String saveSchoolId = obj.getString("School_Code");
                                        String schoolLogo = obj.getString("School_Logo");
                                        String schoolName = obj.getString("School_Name");
                                        String schoolWebsite = obj.getString("school_Website");
                                        String schoolContactNo = obj.getString("School_Phone_No");
                                        String schoolAddress = obj.getString("School_Address");
                                        String schoolEmail = obj.getString("School_Email");
                                        String schoolLat = obj.getString("School_Lat");
                                        String schoolLongi = obj.getString("School_Longi");
                                        String teachingStaff = obj.getString("isTeachingStaff");
                                        String classTeacher = obj.getString("isClass_Teacher");
                                        String pin = obj.getString("PIN");
                                        String senderId = obj.getString("SenderId");
                                        sessionManager.createUserLoginSession(saveUserId, saveUserType, userFullName, saveStandard, saveDivision, userContactNo, userEmail, userDob, userAddress, userBloodGroup, userGrNo, userSwipeCard, userRollNo, userPhoto, saveSchoolId, schoolLogo, schoolName, schoolContactNo, schoolAddress, schoolWebsite, schoolEmail, teachingStaff, schoolLat, schoolLongi, classTeacher, pin, senderId);
                                        sessionManager.ActiveUser(studentId, studentName);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent refreshActivity = new Intent(this, Pin_Enter.class);
                            startActivity(refreshActivity);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dataAdapterForRemoveAccount = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, studentNameList);
        dataAdapterForRemoveAccount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        removeAccountSpinner = (Spinner) findViewById(R.id.addAccountSpinner);
        removeAccountSpinner.setAdapter(dataAdapterForRemoveAccount);

        removeAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    studentName = parent.getItemAtPosition(position).toString();
                    studentId = studentIdList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onClick(final View v) {

        if (v.getId() == R.id.part1RelativeLayout) {

            if (countForm1 == 0) {
//                Details1CardView.setVisibility(View.VISIBLE);
                addAccountDetails.setVisibility(View.VISIBLE);

                countForm1 = 1;
            } else {
//                Details1CardView.setVisibility(View.GONE);
                addAccountDetails.setVisibility(View.GONE);
                countForm1 = 0;
            }
        } else if (v.getId() == R.id.part2RelativeLayout) {

            if (countForm2 == 0) {
//                Details2CardView.setVisibility(View.VISIBLE);
                removeAccountDetails.setVisibility(View.VISIBLE);
                countForm2 = 1;
            } else {
//                Details2CardView.setVisibility(View.GONE);
                removeAccountDetails.setVisibility(View.GONE);
                countForm2 = 0;
            }
        } else if (v.getId() == R.id.part4RelativeLayout) {

            if (countForm4 == 0) {
//                Details2CardView.setVisibility(View.VISIBLE);
                switchAccount.setVisibility(View.VISIBLE);
                countForm4 = 1;
            } else {
//                Details2CardView.setVisibility(View.GONE);
                switchAccount.setVisibility(View.GONE);
                countForm4 = 0;
            }
        } else if (v.getId() == R.id.part3RelativeLayout) {

            if (countForm3 == 0) {
                changeMobileNoLayout.setVisibility(View.VISIBLE);
                countForm3 = 1;
            } else {
                changeMobileNoLayout.setVisibility(View.GONE);
                countForm3 = 0;
            }
        }
        else if (v.getId() == R.id.Layout5) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Download our school app to get your data. \n\n https://play.google.com/store/apps/details?id="+getPackageName()+"");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        else if (v.getId() == R.id.Layout6) {
            sessionManager = new SessionManager(v.getContext());
            sessionManager.logoutUser();
        }

        if (v.getId() == R.id.btnSubmit) {
            oldMblNo = txtOldMblNo.getText().toString();
            newMblNo = txtNewMblNo.getText().toString();
            userId = txtuserId.getText().toString();

            if (txtOldMblNo.getText().toString().isEmpty()) {
                Toast.makeText(My_New_Account.this, "Please enter your old.", Toast.LENGTH_LONG).show();
            } else if (txtNewMblNo.getText().toString().isEmpty()) {
                Toast.makeText(My_New_Account.this, "Please enter your new mobile no.", Toast.LENGTH_LONG).show();
            } else if (txtuserId.getText().toString().isEmpty()) {
                Toast.makeText(My_New_Account.this, "Please enter registration id.", Toast.LENGTH_LONG).show();
            } else {
                progressDialog = new ProgressDialog(My_New_Account.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                ChangeMobileNoReq();
            }
        }

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
                    Toast.makeText(My_New_Account.this, "Please Enter New Student Id.", Toast.LENGTH_LONG).show();
                } else if (editTextPassword.getText().toString().isEmpty()) {
                    Toast.makeText(My_New_Account.this, "Please Enter Password.", Toast.LENGTH_LONG).show();
                } else {
                    schoolId = userId.substring(0, userId.length() - 4);

                    progressDialog = new ProgressDialog(My_New_Account.this);
                    progressDialog.setMessage("Checking Wait...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);

                    LoginRequest();
                }
            }
//            else if (v.getId() == R.id.btnSubmit) {
//
//                oldMblNo = txtOldMblNo.getText().toString();
//                newMblNo = txtNewMblNo.getText().toString();
//                if (oldMblNo.equals("")) {
//                    Toast.makeText(this, "Please Enter Your Old Registered No.", Toast.LENGTH_SHORT).show();
//                } else if (newMblNo.equals("")) {
//                    Toast.makeText(this, "Please Enter Your New No.", Toast.LENGTH_SHORT).show();
//                } else {
//                    ChangeMblNoAsyncCallWS task = new ChangeMblNoAsyncCallWS();
//                    task.execute();
//                }
//            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet connection!");
            builder.setMessage("Please Check Your Internet Connection.");
            AlertDialog alert = builder.create();
            alert.show();
        }

        if (v.getId() == R.id.btnRemoveAccount) {
            if (studentName == null || studentName.equals("")) {
                Toast.makeText(this, "Please Select Username To Remove", Toast.LENGTH_SHORT).show();
            } else {
                if (removeAccountSpinner.getAdapter().getCount() - 1 == 0) {
                    //logout bcz 0 user
                    sessionManager = new SessionManager(this);
                    sessionManager.logoutUser();
                } else {
                    // delete selected user name from sqlite
                    boolean result = mydb.DeleteUserAccount(studentId);
                    if (result == true) {
                        RemoveDeviceDetails();

                        //String id = mydb.getDefaultUser();
                        JSONArray selectedUserDetailsArray = null;
                        try {
                            selectedUserDetailsArray = mydb.getDefaultUserDetails();
                            for (int i = 0; i < selectedUserDetailsArray.length(); i++) {
                                try {
                                    JSONObject obj = selectedUserDetailsArray.getJSONObject(i);

                                    String saveUserType = obj.getString("Usertype");
                                    String saveUserId = obj.getString("userId");
                                    String userFullName = obj.getString("Name");
                                    String saveStandard = obj.getString("Standard");
                                    String saveDivision = obj.getString("Division");
                                    String userContactNo = obj.getString("MobileNo");
                                    String userEmail = obj.getString("Emailid");
                                    String userDob = obj.getString("Dob");
                                    String userPhoto = obj.getString("Image");
                                    String userAddress = obj.getString("Address");
                                    String userBloodGroup = obj.getString("BloodGroup");
                                    String userGrNo = obj.getString("GrNo");
                                    String userSwipeCard = obj.getString("SwipeCardNo");
                                    String userRollNo = obj.getString("RolNo");
                                    String saveSchoolId = obj.getString("School_Code");
                                    String schoolLogo = obj.getString("School_Logo");
                                    String schoolName = obj.getString("School_Name");
                                    String schoolWebsite = obj.getString("school_Website");
                                    String schoolContactNo = obj.getString("School_Phone_No");
                                    String schoolAddress = obj.getString("School_Address");
                                    String schoolEmail = obj.getString("School_Email");
                                    String schoolLat = obj.getString("School_Lat");
                                    String schoolLongi = obj.getString("School_Longi");
                                    String isTeachingStaff = obj.getString("isTeachingStaff");
                                    String isClassTeacher = obj.getString("isClass_Teacher");
                                    String pin = obj.getString("PIN");
                                    String senderId = obj.getString("SenderId");
                                    sessionManager.createUserLoginSession(saveUserId, saveUserType, userFullName, saveStandard, saveDivision, userContactNo, userEmail, userDob, userAddress, userBloodGroup, userGrNo, userSwipeCard, userRollNo, userPhoto, saveSchoolId, schoolLogo, schoolName, schoolContactNo, schoolAddress, schoolWebsite, schoolEmail, isTeachingStaff, schoolLat, schoolLongi, isClassTeacher, pin, senderId);
                                    sessionManager.ActiveUser(saveUserId, userFullName);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        counter = 2;
                        studentNameList.remove(studentName);
                        ArrayAdapter<String> dataAdapterForRemoveAccount = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, studentNameList);
                        dataAdapterForRemoveAccount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        removeAccountSpinner.setAdapter(dataAdapterForRemoveAccount);
                        getUserForSpinner();
                        Toast.makeText(this, "Successfully Removed.", Toast.LENGTH_SHORT).show();

                        Intent gotohome = new Intent(this, Pin_Enter.class);
                        gotohome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(gotohome);


                    } else {
                        Toast.makeText(this, "Please Try Again Later.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void RemoveDeviceDetails() {

        method = "Remove_User_Device_Details";
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
                        //AddDeviceDetails();
                        error.getErrorDetail();
                        Toast.makeText(My_New_Account.this, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void ChangeMobileNoReq() {
        method = "Change_MobileNo_Get_OTP2";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("User_Id", userId);
            jsonObject.put("Old_MobileNo", oldMblNo);
            jsonObject.put("New_MobileNo", newMblNo);
            jsonObject.put("School_id", schoolId);
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
                            String res = response.getString("responseDetails");
                            if (res.equals("Old No Is Not Valid.")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(My_New_Account.this);
                                builder.setTitle("Result");
                                builder.setMessage("Old mobile no is not correct.");
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
                            } else {

                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        otp = obj.getString("OTP");
                                    }

                                    String imFrom = "ChangeMobileNo";
                                    Intent gotoOTP = new Intent(My_New_Account.this, My_OTP.class);
                                    gotoOTP.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    gotoOTP.putExtra("OTP", otp);
                                    gotoOTP.putExtra("newMblNo", newMblNo);
                                    gotoOTP.putExtra("oldMblNo", oldMblNo);
                                    gotoOTP.putExtra("userId", userId);
                                    gotoOTP.putExtra("imFrom", imFrom);
                                    startActivity(gotoOTP);

                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Toast.makeText(My_New_Account.this, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(My_New_Account.this, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void LoginRequest() {
        method = "Login2";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Id", userId);
            jsonObject.put("Password", userPassword);
            jsonObject.put("School_id", schoolId);
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
                            String res = response.getString("responseDetails");
                            if (res.equals("Invalid UserName & Password")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(My_New_Account.this);
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
                            } else {
                                sessionManager = new SessionManager(My_New_Account.this);
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
                                        otp = obj.getString("OTP");
                                        isTeachingStaff = obj.getString("IsTeachingStaff");
                                        isClassTeacher = obj.getString("IsClassTeacher");
                                        pin = obj.getString("PIN");
                                        senderId = obj.getString("SenderId");

                                        sessionManager.createUserLoginSession(saveUserId, saveUserType, userFullName, saveStandard, saveDivision, userContactNo, userEmail, userDob, userAddress, userBloodGroup, userGrNo, userSwipeCard, userRollNo, userPhoto, saveSchoolId, schoolLogo, schoolName, schoolContactNo, schoolAddress, schoolWebsite, schoolEmail, isTeachingStaff, schoolLat, schoolLongi, isClassTeacher, pin, senderId);
                                        mydb.insertUserDetails(saveUserType, saveUserId, userFullName, saveStandard, saveDivision, userContactNo, userEmail, userDob, userAddress, userBloodGroup, userGrNo, userSwipeCard, userRollNo, userPhoto, saveSchoolId, schoolLogo, schoolName, schoolWebsite, schoolContactNo, schoolAddress, schoolEmail, isTeachingStaff, schoolLat, schoolLongi, isClassTeacher, pin, senderId);
                                        sessionManager.ActiveUser(saveUserId, userFullName);

                                        AddDeviceDetails();

                                        Intent gotoOTP = new Intent(My_New_Account.this, My_OTP.class);
                                        gotoOTP.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        String imFrom = "AddAccount";

                                        gotoOTP.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        gotoOTP.putExtra("OTP", otp);
                                        gotoOTP.putExtra("imFrom", imFrom);
                                        gotoOTP.putExtra("newMblNo", "");
                                        gotoOTP.putExtra("oldMblNo", "");
                                        startActivity(gotoOTP);

                                    }
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Toast.makeText(My_New_Account.this, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(My_New_Account.this, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void AddDeviceDetails() {
        method = "Add_User_Device_Details";
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

        PackageManager pm = My_New_Account.this.getPackageManager();
        ComponentName component = new ComponentName(My_New_Account.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();

        PackageManager pm = My_New_Account.this.getPackageManager();
        ComponentName component = new ComponentName(My_New_Account.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
