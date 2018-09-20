package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.studentcares.spps.adapter.Holiday_Adapter;
import com.studentcares.spps.adapter.Leave_Adapter;
import com.studentcares.spps.adapter.Leave_To_Approve_Adapter;
import com.studentcares.spps.connectivity.Holiday_Details_Get;
import com.studentcares.spps.connectivity.Leave_req;
import com.studentcares.spps.model.Leave_Items;
import com.studentcares.spps.model.Leave_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Leave_To_Approve_List extends BaseActivity {

    ProgressDialog progressDialog;
    List<Leave_Items> listItems = new ArrayList<Leave_Items>();
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    String userId;
    String schoolId;
    String userType,leaveListFor;

    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_list);

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

        recyclerView = (RecyclerView) findViewById(R.id.leaveListRecyclerView);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Leave_To_Approve_Adapter(listItems);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();

        getList();
    }

    private void getList(){
        try {
            Leave_req leave_req = new Leave_req(this);
            leave_req.LeaveToApproveList(listItems, recyclerView, adapter,schoolId,userId,progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
