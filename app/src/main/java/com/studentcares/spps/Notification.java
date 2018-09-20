package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.studentcares.spps.adapter.News_List_Adapter;
import com.studentcares.spps.adapter.Notification_Adapter;
import com.studentcares.spps.connectivity.Get_Notification_List;
import com.studentcares.spps.connectivity.News_Request_Data;
import com.studentcares.spps.model.Notification_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notification extends BaseActivity {
    private TextView notificationTitle;
    private TextView notificationMessage;
    private String title;
    private String message;

    RelativeLayout attendanceMsgLayout,listLayout;
    private ProgressDialog progressDialog = null;

    public List<Notification_Items> listItems = new ArrayList<Notification_Items>();
    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_list);

        ActivityInfo activityInfo = null;
        try {
            activityInfo = getPackageManager().getActivityInfo(
                    getComponentName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        title = activityInfo.loadLabel(getPackageManager())
                .toString();
        txtActivityName.setText(title);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        schoolId = user.get(SessionManager.KEY_SCHOOLID);


        attendanceMsgLayout = (RelativeLayout) findViewById(R.id.attendanceMsgLayout);
        listLayout = (RelativeLayout) findViewById(R.id.listLayout);

        notificationTitle = (TextView) findViewById(R.id.notificationTitle);
        notificationMessage = (TextView) findViewById(R.id.notificationMessage);
        attendanceMsgLayout.setVisibility(View.GONE);
        listLayout.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (null != intent) {
            title = intent.getStringExtra("Title");
            message = intent.getStringExtra("Message");

            if(title != null){
                attendanceMsgLayout.setVisibility(View.VISIBLE);
                listLayout.setVisibility(View.GONE);
                notificationTitle.setText(title);
                notificationMessage.setText(message);
            }
            else{
                attendanceMsgLayout.setVisibility(View.GONE);
                listLayout.setVisibility(View.VISIBLE);

                recyclerView = (RecyclerView) findViewById(R.id.notificationRecyclerView);
                recyclerView.setHasFixedSize(true);
                linearLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(linearLayoutManager);

                recyclerView.smoothScrollToPosition(0);
                reviewAdapter = new Notification_Adapter(listItems);
                recyclerView.setAdapter(reviewAdapter);

                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(this.getString(R.string.progress_msg));
                progressDialog.show();

                getList();
            }
        }
    }

    private void getList() {
        try {
            Get_Notification_List GetNotificationList = new Get_Notification_List(this);
            GetNotificationList.NotificationList(listItems, recyclerView, reviewAdapter,  schoolId, progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
