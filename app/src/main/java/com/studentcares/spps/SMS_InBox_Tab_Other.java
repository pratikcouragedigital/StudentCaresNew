package com.studentcares.spps;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.studentcares.spps.adapter.SMS_Other_List_Adapter;
import com.studentcares.spps.connectivity.SMS_Inbox_Request;
import com.studentcares.spps.model.SMS_Inbox_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class SMS_InBox_Tab_Other extends Fragment implements View.OnClickListener{

    ProgressDialog progressDialog;
    public List<SMS_Inbox_Items> listItems = new ArrayList<SMS_Inbox_Items>();
    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    private View v;
    private int current_page = 1;
    String schoolId,userId,userType,selectedDate="";
    FloatingActionButton filterFabButton;
    private int year, month, day;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.sms_inbox_list, container, false);
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SessionManager sessionManager = new SessionManager(v.getContext().getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(SessionManager.KEY_USERID);
        schoolId = user.get(SessionManager.KEY_SCHOOLID);
        userType = user.get(SessionManager.KEY_USERTYPE);

        filterFabButton = (FloatingActionButton) v.findViewById(R.id.filterFabButton);
        filterFabButton.setOnClickListener(this);

        filterFabButton.setVisibility(View.GONE);

        recyclerView = (RecyclerView) v.findViewById(R.id.smsListRecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new SMS_Other_List_Adapter(listItems);
        recyclerView.setAdapter(reviewAdapter);

        getList(current_page,selectedDate);
    }

    private void getList(int current_page, String selectedDate) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();
        try {
            SMS_Inbox_Request getList = new SMS_Inbox_Request(getActivity());
            if(selectedDate.equals("")){
                getList.GetOtherSMSList(listItems, recyclerView, reviewAdapter,userId, schoolId,userType, current_page, progressDialog);
            }else{
                getList.GetOtherSMSListDateWise(listItems, recyclerView, reviewAdapter,userId, schoolId,userType, current_page, this.selectedDate, progressDialog);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.filterFabButton) {

            Calendar mcurrentDate = Calendar.getInstance();
            year = mcurrentDate.get(Calendar.YEAR);
            month = mcurrentDate.get(Calendar.MONTH);
            day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    selectedDate = String.valueOf(selectedyear) + "-" + String.valueOf(selectedmonth+1) + "-" + String.valueOf(selectedday);

                    selectedDate = DateFormatter.ChangeDateFormat(selectedDate);

                    Toast.makeText(getActivity(), ""+selectedDate, Toast.LENGTH_SHORT).show();
                    getList(current_page,selectedDate);
                }
            }, year, month, day);
            mDatePicker.show();

        }
    }


}
