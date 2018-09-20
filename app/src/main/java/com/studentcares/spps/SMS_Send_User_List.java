package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.studentcares.spps.adapter.Send_SMS_UserList_Adapter;
import com.studentcares.spps.commonClasses.RemoveLastComma;
import com.studentcares.spps.connectivity.Send_SMS_Get_UserList;
import com.studentcares.spps.connectivity.Send_SMS_Req;
import com.studentcares.spps.model.Send_SMS_User_Items;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.singleton.Send_SMS_Check_User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SMS_Send_User_List extends BaseActivity {

    String schoolId;
    String standardId;
    String divisionId;
    String mobileNo_ForSMS;
    String id_ForSMS;
    String sms;
    String userId;
    String listOf;
    String smsType;
    String userType_storeMsg = "Student";

    Send_SMS_Check_User sendSMSCheck_User;
    List<String> selectedUserNoListArray = new ArrayList<String>();
    List<String> selectedUserIdListArray = new ArrayList<String>();

    CounterFab sendSMSCheckFab, selectAllUserFab;
    public List<Send_SMS_User_Items> studentItems = new ArrayList<Send_SMS_User_Items>();
    RecyclerView recyclerView;
    Send_SMS_UserList_Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog;

    int markAllCounter = 0;

    EditText txtSearch;
    String searchText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_send_user_list);

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
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        userId = typeOfUser.get(SessionManager.KEY_USERID);

        txtSearch = (EditText) findViewById(R.id.txtSearch);
        recyclerView = (RecyclerView) findViewById(R.id.studentListRecyclerView);
        sendSMSCheckFab = (CounterFab) findViewById(R.id.sendSMSCheckFab);
        selectAllUserFab = (CounterFab) findViewById(R.id.selectAllUserFab);

        sendSMSCheckFab.setOnClickListener(this);
        selectAllUserFab.setOnClickListener(this);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new Send_SMS_UserList_Adapter(studentItems, sendSMSCheckFab, selectAllUserFab);
        recyclerView.setAdapter(reviewAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            standardId = intent.getStringExtra("standardId");
            divisionId = intent.getStringExtra("divisionId");
            sms = intent.getStringExtra("msg");
            listOf = intent.getStringExtra("listOf");
        }

        if (listOf.equals("Student")) {
            getStudentList();
        } else {
            getStaffList();
        }

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

    }

    private void getStudentList() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();
        try {
            Send_SMS_Get_UserList studentList_ForSMS = new Send_SMS_Get_UserList(this);
            studentList_ForSMS.StudentList(studentItems, recyclerView, reviewAdapter, standardId, divisionId, schoolId, progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getStaffList() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();
        try {
            Send_SMS_Get_UserList staffList_ForSMS = new Send_SMS_Get_UserList(this);
            staffList_ForSMS.StaffList(studentItems, recyclerView, reviewAdapter, schoolId, progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sendSMSCheckFab) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Do you want to sent Message ?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SendSMSToUser();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        } else if (v.getId() == R.id.selectAllUserFab) {

            if (markAllCounter == 0) {
                markAllCounter = 1;
                int count = studentItems.size();
                selectedUserIdListArray.clear();
                selectedUserNoListArray.clear();

                Toast.makeText(this, "total: " + count, Toast.LENGTH_SHORT).show();
                for (int i = 0; i < count; i++) {
                    String id = studentItems.get(i).getId();
                    String mobile = studentItems.get(i).getmobileNo();
                    selectedUserIdListArray.add(id);
                    selectedUserNoListArray.add(mobile);
                }
            }
            else{
                markAllCounter = 0;
                selectedUserIdListArray.clear();
                selectedUserNoListArray.clear();
            }
            sendSMSCheck_User.setIdList(selectedUserIdListArray);
            sendSMSCheck_User.setNoList(selectedUserNoListArray);
            reviewAdapter.notifyDataSetChanged();

        }
    }

    public void SendSMSToUser() {
        selectedUserNoListArray = sendSMSCheck_User.getNoList();
        selectedUserIdListArray = sendSMSCheck_User.getIdList();

        RemoveLastComma removeLastComma = new RemoveLastComma(this);
        mobileNo_ForSMS = removeLastComma.CommaRemove(selectedUserNoListArray);
        id_ForSMS = removeLastComma.CommaRemove(selectedUserIdListArray);

        String msgBody = sms;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait.Message is sending.");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        if (listOf.equals("Student")) {
            smsType = "StudentWise";
            userType_storeMsg = "Student";

        } else {
            smsType = "Staff";
            userType_storeMsg = "Staff";
        }

        Send_SMS_Req PRSendSmsFromServer = new Send_SMS_Req(this);
        PRSendSmsFromServer.SendSmsFromServer(schoolId, mobileNo_ForSMS, id_ForSMS, userType_storeMsg, msgBody, smsType, userId, progressDialog);

    }
}