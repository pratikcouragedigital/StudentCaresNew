package com.studentcares.spps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.studentcares.spps.adapter.News_List_Adapter;
import com.studentcares.spps.adapter.Ranker_Adapter;
import com.studentcares.spps.connectivity.Ranker_Req;
import com.studentcares.spps.model.Rankers_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Rankers extends BaseActivity implements View.OnClickListener{

    ProgressDialog progressDialog = null;
    List<Rankers_Items> listItems = new ArrayList<Rankers_Items>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;

    SessionManager sessionManager;
    FloatingActionButton btnAddRanker;
    String schoolId,userId,userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rankers);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        schoolId = user.get(SessionManager.KEY_SCHOOLID);
        userId =user.get(SessionManager.KEY_USERID);
        userType = user.get(SessionManager.KEY_USERTYPE);

        ActivityInfo activityInfo = null;
        try{
            activityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        String title = activityInfo.loadLabel(getPackageManager()).toString();
        txtActivityName.setText(title);

        btnAddRanker = (FloatingActionButton) findViewById(R.id.btnAddRanker);
        btnAddRanker.setVisibility(View.GONE);

        if(userType.equals("Student")){
            btnAddRanker.setVisibility(View.GONE);
        }
        else{
            btnAddRanker.setVisibility(View.VISIBLE);
            btnAddRanker.setOnClickListener(this);
        }

        recyclerView = (RecyclerView) findViewById(R.id.rankerRecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.smoothScrollToPosition(0);
        adapter = new Ranker_Adapter(listItems);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait.");
        progressDialog.show();

        getList();

    }

    private void getList() {
        try{
            Ranker_Req ranker_Req = new Ranker_Req(this);
            ranker_Req.GetRankerList(listItems,recyclerView,adapter,schoolId,progressDialog);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId() == R.id.btnAddRanker){
            Intent gotoAddRanker = new Intent(this,Ranker_Add.class);
            startActivity(gotoAddRanker);

        }
    }
}
