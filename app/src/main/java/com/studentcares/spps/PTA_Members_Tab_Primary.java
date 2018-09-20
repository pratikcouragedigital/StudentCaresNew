package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.studentcares.spps.adapter.PTA_Mem_Adapter_Primary;
import com.studentcares.spps.commonClasses.RemoveLastComma;
import com.studentcares.spps.connectivity.PTA_Members_List_Get;
import com.studentcares.spps.model.PTA_Member_Items;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.singleton.Check_Student_Staff_For_Sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tourguide.tourguide.TourGuide;

public class PTA_Members_Tab_Primary extends Fragment implements View.OnClickListener{

    ProgressDialog progressDialog;
    List<PTA_Member_Items> ptaMemItems = new ArrayList<PTA_Member_Items>();
    PTA_Mem_Adapter_Primary adapter;
    RecyclerView recyclerView;
    String userId;
    String schoolId;
    LinearLayoutManager linearLayoutManager;
    //FloatingActionButton fabSendMsg_Notification;
    CounterFab fabCounter;

    String ptaType="2";
    private View v;
    String mobileNo_For_sms;
    String id_For_notifications;

    Check_Student_Staff_For_Sms check_Student_Staff_For_Sms;
    List<String> ptaListFor_SMS;
    List<String> ptaListFor_Notification;
    private TourGuide mTourGuideHandler;

    EditText txtSearch;
    String searchText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.pta_members_list, container, false);

        //fabSendMsg_Notification = (FloatingActionButton) v.findViewById(R.id.fabSendMsg_Notification);
        Animation animation = new TranslateAnimation(0f, 0f, 200f, 0f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setInterpolator(new BounceInterpolator());
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SessionManager sessionManager = new SessionManager(v.getContext().getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(SessionManager.KEY_USERID);
        schoolId = user.get(SessionManager.KEY_SCHOOLID);

        fabCounter  = (CounterFab ) v.findViewById(R.id.fabCounter);

        recyclerView = (RecyclerView) getView().findViewById(R.id.ptaMemRecyclerView);
        //fabSendMsg_Notification.setOnClickListener(this);
        fabCounter.setOnClickListener(this);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new PTA_Mem_Adapter_Primary(ptaMemItems,fabCounter);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();
        getList();

        txtSearch = (EditText) v.findViewById(R.id.txtSearch);

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub

                searchText = String.valueOf(s);
//                Toast.makeText(v.getContext(), ""+searchText, Toast.LENGTH_SHORT).show();
                adapter.getFilter().filter(searchText);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

        });
    }

    private void getList(){
        try {
            //get list of pta mem of Primary
            PTA_Members_List_Get showListDetails = new PTA_Members_List_Get(getContext());
            showListDetails.showPtaMenList(ptaMemItems, recyclerView, adapter, schoolId,ptaType, progressDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
            if(v.getId()==  R.id.fabCounter){
                ptaListFor_SMS = check_Student_Staff_For_Sms.getPtaMobileListInstance();
                ptaListFor_Notification = check_Student_Staff_For_Sms.getStudentListInstance();

                if(!ptaListFor_SMS.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to sent Message ?");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            send_sms_PTA();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }else {
                    Toast.makeText(v.getContext(), "If You Want To Sent SMS_Receive_Attendance_List Then Please Select At least One Member. ", Toast.LENGTH_SHORT).show();
                }

            }
    }

    public void send_sms_PTA() {
        RemoveLastComma removeLastComma = new RemoveLastComma(getActivity());
        mobileNo_For_sms = removeLastComma.CommaRemove(ptaListFor_Notification);
        id_For_notifications = removeLastComma.CommaRemove(ptaListFor_Notification);

        Intent gotoSendMsg = new Intent(getContext(), PTA_Send_Msg.class);
        gotoSendMsg.putExtra("mobileNo_For_sms",mobileNo_For_sms);
        gotoSendMsg.putExtra("id_For_notifications",id_For_notifications);
        startActivity(gotoSendMsg);
    }
}
