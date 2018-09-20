package com.studentcares.spps;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.studentcares.spps.adapter.News_List_Adapter;
import com.studentcares.spps.adapter.SMS_Other_List_Adapter;
import com.studentcares.spps.adapter.SMS_Report_Adapter;
import com.studentcares.spps.connectivity.News_Request_Data;
import com.studentcares.spps.connectivity.SMS_Report_Request;
import com.studentcares.spps.model.SMS_Report_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SMS_Report extends BaseActivity implements View.OnClickListener {

    private ProgressDialog progressDialog = null;

    public List<SMS_Report_Items> listItems = new ArrayList<SMS_Report_Items>();
    RecyclerView recyclerView;
    SMS_Report_Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;

    String schoolId,userId = "",userType,fromDate,toDate,senderId;
    private StringBuilder date;

    private int current_page = 1;
    FloatingActionButton dateFilterFab;
    EditText txtSearch;
    String searchText;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_report);

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
        userType = typeOfUser.get(SessionManager.KEY_USERTYPE);
        senderId = typeOfUser.get(SessionManager.KEY_SMS_SENDERID);

        //senderId = getResources().getString(R.string.SmsSenderId);

        Date curDate = new Date();
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");

        fromDate = dFormat.format(curDate);
        toDate = fromDate;

        dateFilterFab = (FloatingActionButton) findViewById(R.id.dateFilterFab);
        dateFilterFab.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new SMS_Report_Adapter(listItems);
        recyclerView.setAdapter(reviewAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();

        getList();

        txtSearch = (EditText) findViewById(R.id.txtSearch);

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub

                searchText = String.valueOf(s);
//                Toast.makeText(v.getContext(), ""+searchText, Toast.LENGTH_SHORT).show();
                reviewAdapter.getFilter().filter(searchText);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

        });

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = getIntent();
                startActivity(intent);
                finish();
            }
        }, 20000);
    }

    private void getList() {
        try {
            SMS_Report_Request SmsReport = new SMS_Report_Request(this);
            SmsReport.GetSMSReport(listItems, recyclerView, reviewAdapter, schoolId, fromDate,toDate,senderId,progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.dateFilterFab){
            setDate();
        }
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
                date = new StringBuilder().append(year).append("-").append(monthOfYear).append("-").append(dayOfMonth);
                fromDate = date.toString();
                toDate = fromDate;
            getList();
              //  txtFromDate.setText(date);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }
}