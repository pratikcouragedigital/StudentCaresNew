package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.studentcares.spps.adapter.DashBoard_Count_Adapter;
import com.studentcares.spps.connectivity.DashBoard_Count_Get;
import com.studentcares.spps.model.DashBoard_Count_Items;
import com.studentcares.spps.model.HomeGrid_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashBoard_Count extends BaseActivity {

    private RecyclerView dashboardCountRecyclerView;
    private GridLayoutManager  gridLayoutManagerDashboard;
    public List<HomeGrid_Items> listItems = new ArrayList<HomeGrid_Items>();
    public List<DashBoard_Count_Items> dashBoard_Count_Items = new ArrayList<DashBoard_Count_Items>();
    RecyclerView.Adapter dashBoardAdapter;
    private static ProgressDialog progressDialogBox;

    SessionManager sessionManager;
    private String schoolId;
    private String userType;
    private String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_count);

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
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        userType = typeOfUser.get(SessionManager.KEY_USERTYPE);
        userId = typeOfUser.get(SessionManager.KEY_USERID);

        //dashboard count
        dashboardCountRecyclerView = (RecyclerView) findViewById(R.id.dashboardCountRecyclerView);

        dashboardCountRecyclerView.setHasFixedSize(true);
        gridLayoutManagerDashboard = new GridLayoutManager(this, 2);
        dashboardCountRecyclerView.setLayoutManager(gridLayoutManagerDashboard);

        dashboardCountRecyclerView.smoothScrollToPosition(0);
        dashBoardAdapter = new DashBoard_Count_Adapter(dashBoard_Count_Items);
        dashboardCountRecyclerView.setAdapter(dashBoardAdapter);

        progressDialogBox = new ProgressDialog(this);
        progressDialogBox.setMessage(this.getString(R.string.progress_msg));
        progressDialogBox.show();

        DashBoard_Count_Get getDashBoardData = new DashBoard_Count_Get(this);
        getDashBoardData.ShowDashBordData(dashBoard_Count_Items, dashboardCountRecyclerView, dashBoardAdapter, schoolId,progressDialogBox);
    }
}
