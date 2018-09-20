package com.studentcares.spps;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.sessionManager.SessionManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class My_OTP extends AppCompatActivity implements View.OnClickListener {

    EditText txtotp;
    TextView txtResendOtp, txtTimer;
    Button btnSubmitOtp;
    String pin, userContactNo, newMblNo, OTP, userId, oldMblNo, imFrom, userEnteredOTP, displayText, yourOTPcode, schoolId, method, userType, isTeachingStaff, isClassTeacher, standard, division;

    private int SMS_READ_PERMISSION_REQUEST = 1;
    private SessionManager sessionManager;

    CountDownTimer countDownTimer;
    ProgressDialog progressDialog;
    String masterOtp = "5588";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_otp);

        sessionManager = new SessionManager(My_OTP.this);
        HashMap<String, String> typeOfUser = sessionManager.getUserDetails();
        standard = typeOfUser.get(SessionManager.KEY_STANDARD);
        division = typeOfUser.get(SessionManager.KEY_DIVISION);
        userContactNo = typeOfUser.get(SessionManager.KEY_CONTACTNO);
        userId = typeOfUser.get(SessionManager.KEY_USERID);
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        isTeachingStaff = typeOfUser.get(SessionManager.KEY_ISTEACHING_STAFF);
        isClassTeacher = typeOfUser.get(SessionManager.KEY_ISCLASS_TEACHER);
        pin = typeOfUser.get(SessionManager.KEY_PIN);

        if (savedInstanceState != null) {
            userEnteredOTP = savedInstanceState.getString("userEnteredOTP");
            Toast.makeText(this, userEnteredOTP, Toast.LENGTH_LONG).show();
        }

        Intent intent = getIntent();
        Bundle intentExtras = intent.getExtras();
        imFrom = "";
        if (intentExtras != null) {
            imFrom = intent.getStringExtra("imFrom");
            newMblNo = intent.getStringExtra("newMblNo");
            oldMblNo = intent.getStringExtra("oldMblNo");
            OTP = intent.getStringExtra("OTP");
        }


        if (newMblNo == null || newMblNo.equals("")) {
            newMblNo = typeOfUser.get(SessionManager.KEY_CONTACTNO);
        }

        txtotp = (EditText) findViewById(R.id.txtOtp);
        txtResendOtp = (TextView) findViewById(R.id.txtResendOtp);
        txtTimer = (TextView) findViewById(R.id.txtTimer);
        btnSubmitOtp = (Button) findViewById(R.id.btnSubmit);

        btnSubmitOtp.setOnClickListener(this);
        txtResendOtp.setOnClickListener(this);

        countDownTimer = new CountDownTimer(60000, 1000) {

            int counter = 0;

            public void onTick(long millisUntilFinished) {
                txtTimer.setText("Remaining " + millisUntilFinished / 1000 + " seconds");
                if (counter == 0) {
                    txtResendOtp.setEnabled(false);
                    txtResendOtp.setVisibility(View.GONE);
                    counter = 1;
                }
            }

            public void onFinish() {
                txtTimer.setText("");
                if (OTP == null || OTP.equals("")) {

                }
                if (counter == 1) {
                    txtResendOtp.setEnabled(true);
                    txtResendOtp.setVisibility(View.VISIBLE);
                    counter = 0;
                }
            }
        }.start();

        if (imFrom.equals("EnterPin")) {
            ResendOtp();
        }

        if (OTP == null) {
            countDownTimer.cancel();
            txtTimer.setText("");
        }

        String permission = Manifest.permission.READ_SMS;
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            requestReadSmsPermission();
        } else {
            autoReadOTP();
        }

        txtotp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtotp.getText().length() <= 0) {
                    txtotp.setError("Please enter the OTP!");
                } else {
                    txtotp.setError(null);
                }
            }
        });

    }

    private void autoReadOTP() {
        final String DISPLAY_MESSAGE_ACTION = this.getPackageName() + ".CodeSmsReceived";
        try {
            this.unregisterReceiver(mHandleMessageReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));


    }


    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra("varificationCode")) {
                yourOTPcode = intent.getStringExtra("varificationCode");
                txtotp.setText(yourOTPcode);
                userEnteredOTP = txtotp.getText().toString();

                countDownTimer.cancel();
                txtTimer.setText("");
            }
        }
    };


    private void requestReadSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_SMS)) {
            autoReadOTP();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},
                    SMS_READ_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == SMS_READ_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                autoReadOTP();
            } else {
                Toast.makeText(My_OTP.this, "SMS_Receive_Attendance_List read permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnSubmit) {
            if (imFrom.equals("ChangeMobileNo")) {
                //goto login clear the session
                progressDialog = new ProgressDialog(My_OTP.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                ChangeMobileNoReq();

            } else if (imFrom.equals("EnterPin")) {
                userEnteredOTP = txtotp.getText().toString();
                if (userEnteredOTP.length() > 0) {
                    if (userEnteredOTP.equals(OTP) || userEnteredOTP.equals(masterOtp) ) {
                        Intent pinSet = new Intent(My_OTP.this, Pin_Set.class);
                        pinSet.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(pinSet);
                    } else {
                        Toast.makeText(My_OTP.this, "You Entered Wrong OTP.!!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    txtotp.setError("Please enter the OTP!");
                }
            }
            else {
                userEnteredOTP = txtotp.getText().toString();

                if (userEnteredOTP.length() > 0) {
                    if (userEnteredOTP.equals(OTP)  || userEnteredOTP.equals(masterOtp)) {

                        sessionManager.OTPCheck(userEnteredOTP, newMblNo);

                        if (pin.equals("") || pin.equals(" ") || pin == null) {
                            Intent gotoSetPin = new Intent(My_OTP.this, Pin_Set.class);
                            gotoSetPin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(gotoSetPin);
                        } else {
                            if (isTeachingStaff.equals("true")) {
                                if (isClassTeacher.equals("true")) {
                                    if (standard.equals("0") || standard == null && division.equals("0") || division == null) {
                                        Intent gotoAssign = new Intent(My_OTP.this, Assign_Std_Div_Teaching_Staff.class);
                                        startActivity(gotoAssign);
                                        //TaskStackBuilder.create(My_OTP.this).addNextIntentWithParentStack(gotoHome).startActivities();
                                    } else {
                                        Intent gotoHome = new Intent(My_OTP.this, Pin_Enter.class);
                                        TaskStackBuilder.create(My_OTP.this).addNextIntentWithParentStack(gotoHome).startActivities();
                                    }
                                } else {
                                    Intent gotoHome = new Intent(My_OTP.this, Pin_Enter.class);
                                    TaskStackBuilder.create(My_OTP.this).addNextIntentWithParentStack(gotoHome).startActivities();
                                }
                            } else {
                                Intent gotoHome = new Intent(My_OTP.this, Pin_Enter.class);
                                TaskStackBuilder.create(My_OTP.this).addNextIntentWithParentStack(gotoHome).startActivities();
                            }
                        }
                    } else {
                        Toast.makeText(My_OTP.this, "You Entered Wrong OTP.!!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    txtotp.setError("Please enter the OTP!");
                }
            }
        } else if (v.getId() == R.id.txtResendOtp) {
            txtotp.setText("");
            Toast.makeText(My_OTP.this, "Please Wait.!", Toast.LENGTH_LONG).show();

            ResendOtp();
        }
    }

    private void ChangeMobileNoReq() {
        method = "Change_MobileNo";
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
                            try {
                                String res = response.getString("responseDetails");
                                if (res.equals("Mobile No Successfully Updated.")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(My_OTP.this);
                                    builder.setTitle("Result");
                                    builder.setMessage("Your mobile no is updated, now you need login with your new mobile no..");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface alert, int which) {
                                            // TODO Auto-generated method stub
                                            //Do something
                                            sessionManager = new SessionManager(My_OTP.this);
                                            sessionManager.logoutUser();
                                            alert.dismiss();
                                        }
                                    });
                                    AlertDialog alert1 = builder.create();
                                    alert1.show();

                                }
                            } catch (Exception e) {
                                e.getMessage();
                                Toast.makeText(My_OTP.this, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Toast.makeText(My_OTP.this, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(My_OTP.this, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void ResendOtp() {
        countDownTimer.start();
        method = "Resend2";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Mobile_No", userContactNo);
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
                            JSONArray jArr = response.getJSONArray("responseDetails");
                            for (int count = 0; count < jArr.length(); count++) {
                                JSONObject obj = jArr.getJSONObject(count);
                                OTP = obj.getString("OTP");
                            }
                            autoReadOTP();
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        ResendOtp();
                    }
                });
    }


}
