package com.studentcares.spps;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.studentcares.spps.connectivity.Holiday_Details_Get;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.sqlLite.DataBaseHelper;

import java.util.Calendar;
import java.util.HashMap;

public class Holiday_Add extends Fragment implements  View.OnClickListener{

    EditText txtHolidayName;
    EditText txtFromDate;
    EditText txtToDate;
    Button btnSubmit;

    String holidayName;
    String fromDate;
    String toDate;
    String userType;
    String userId;
    String schoolId;
    String holidayFor;

    private Calendar calendar;
    private int year, month, day;
    private StringBuilder date;

    private ProgressDialog progressDialog = null;
    private DataBaseHelper mydb;

    RadioButton rbForAll;
    RadioButton rbForStudent;
    RadioButton rbStaff;
    int counter;


    public static Holiday_Add newInstance() {
        Holiday_Add fragment = new Holiday_Add();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.holidays_add, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SessionManager sessionManager = new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(SessionManager.KEY_USERID);
        userType = user.get(SessionManager.KEY_USERTYPE);
        schoolId = user.get(SessionManager.KEY_SCHOOLID);
        mydb = new DataBaseHelper(getContext());

        txtHolidayName = (EditText) getView().findViewById(R.id.txtHolidayName);
        txtFromDate = (EditText) getView().findViewById(R.id.txtFromDate);
        txtToDate = (EditText) getView().findViewById(R.id.txtToDate);
        btnSubmit = (Button) getView().findViewById(R.id.btnSubmit);
        rbForAll = (RadioButton) getView().findViewById(R.id.rbForAll);
        rbForStudent = (RadioButton) getView().findViewById(R.id.rbForStudent);
        rbStaff = (RadioButton) getView().findViewById(R.id.rbStaff);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        btnSubmit.setOnClickListener(this);

        rbForAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbForAll.isChecked()) {
                    holidayFor = "All";
                    rbForStudent.setChecked(false);
                    rbStaff.setChecked(false);
                }
            }
        });

        rbForStudent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbForStudent.isChecked()) {
                    holidayFor = "Student";
                    rbForAll.setChecked(false);
                    rbStaff.setChecked(false);
                }
            }
        });
        rbStaff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbStaff.isChecked()) {
                    holidayFor = "Staff";
                    rbForAll.setChecked(false);
                    rbForStudent.setChecked(false);
                }
            }
        });

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
        date.show(getActivity().getFragmentManager(), "Date Picker");
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
            holidayName = txtHolidayName.getText().toString();
            if(holidayName.equals("")){
                Toast.makeText(getContext(), "Please Enter Holiday Name.", Toast.LENGTH_SHORT).show();
            }
            else if(fromDate.equals("")){
                Toast.makeText(getContext(), "Please Select From Date.", Toast.LENGTH_SHORT).show();
            }
            else if(toDate.equals("")){
                Toast.makeText(getContext(), "Please Select To Date.", Toast.LENGTH_SHORT).show();
            }
            else{
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Please Wait. Holiday Is Adding.");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                try {
                    Holiday_Details_Get UAAdd_Holiday = new Holiday_Details_Get(getContext());
                    UAAdd_Holiday.addHoliday(userId,schoolId,userType,holidayName,fromDate, toDate,holidayFor,progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


}