package com.studentcares.spps;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.studentcares.spps.connectivity.Holiday_Details_Get;
import com.studentcares.spps.connectivity.Leave_req;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.sqlLite.DataBaseHelper;

import java.util.Calendar;
import java.util.HashMap;

public class Leave_Apply extends BaseActivity {

    EditText txtReason;
    EditText txtFromDate;
    EditText txtToDate;
    Button btnSubmit;

    String leaveReason;
    String fromDate;
    String toDate;
    String userType;
    String userId;
    String schoolId;
    int counter;

    private Calendar calendar;
    private int year, month, day;
    private StringBuilder date;

    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave__apply);

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

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(SessionManager.KEY_USERID);
        userType = user.get(SessionManager.KEY_USERTYPE);
        schoolId = user.get(SessionManager.KEY_SCHOOLID);

        txtReason = (EditText)  findViewById(R.id.txtReason);
        txtFromDate = (EditText)  findViewById(R.id.txtFromDate);
        txtToDate = (EditText)  findViewById(R.id.txtToDate);
        btnSubmit = (Button)  findViewById(R.id.btnSubmit);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        btnSubmit.setOnClickListener(this);


        txtFromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    counter=1;
                    setDate();
                }
            }
        });

        txtToDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    counter=2;
                    setDate();
                }
            }
        });
    }

    private void setDate() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(this.getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            monthOfYear = monthOfYear + 1;
            if(counter == 1){
//                txtFromDate.setText(new StringBuilder().append(dayOfMonth).append("/")
//                        .append(monthOfYear).append("/").append(year));
                date = new StringBuilder().append(year).append("-").append(monthOfYear).append("-").append(dayOfMonth);
                fromDate = date.toString();
                txtFromDate.setText(date);
            }
            else if(counter == 2){
//                txtToDate.setText(new StringBuilder().append(dayOfMonth).append("/")
//                        .append(monthOfYear).append("/").append(year));
                date = new StringBuilder().append(year).append("-").append(monthOfYear).append("-").append(dayOfMonth);
                toDate = date.toString();
                txtToDate.setText(date);
            }
        }
    };

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btnSubmit){
            leaveReason = txtReason.getText().toString();
            if(leaveReason.equals("")){
                Toast.makeText(this, "Please Enter Holiday Name.", Toast.LENGTH_SHORT).show();
            }
            else if(fromDate.equals("")){
                Toast.makeText(this, "Please Select From Date.", Toast.LENGTH_SHORT).show();
            }
            else if(toDate.equals("")){
                Toast.makeText(this, "Please Select To Date.", Toast.LENGTH_SHORT).show();
            }
            else{
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait..");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                try {
                    Leave_req leave_req = new Leave_req(this);
                    leave_req.ApplyForLeave(userId,schoolId,userType,leaveReason,fromDate, toDate,progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
