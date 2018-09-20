package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import com.studentcares.spps.adapter.Notice_List_Adapter;
import com.studentcares.spps.connectivity.Notice_Request_Data;
import com.studentcares.spps.model.Notice_Items;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.sqlLite.DataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notice_List extends BaseActivity implements View.OnClickListener {

    public List<Notice_Items> noticeListItems = new ArrayList<Notice_Items>();
    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    private ProgressDialog progressDialog = null;

    FloatingActionButton addNoticeFilterFab;

    private DataBaseHelper mydb;
    int count;
    String schoolId;
    String userType;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_list);

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

        SessionManager sessionManagerNgo = new SessionManager(Notice_List.this);
        HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
        userType = typeOfUser.get(SessionManager.KEY_USERTYPE);
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        userId = typeOfUser.get(SessionManager.KEY_USERID);

        addNoticeFilterFab = (FloatingActionButton) findViewById(R.id.addNoticeFilterFab);
        addNoticeFilterFab.setVisibility(View.GONE);

        //UserAdmin and Principal
        if (userType.equals("UserAdmin") || userType.equals("Principle")|| userType.equals("Staff")) {
            addNoticeFilterFab.setVisibility(View.VISIBLE);
            addNoticeFilterFab.setOnClickListener(this);

            Animation animation = new TranslateAnimation(0f, 0f, 200f, 0f);
            animation.setDuration(1000);
            animation.setFillAfter(true);
            animation.setInterpolator(new BounceInterpolator());

//
//            prefManager = new PrefManager(this);
//            boolean abs = prefManager.isFirstTimeGuideAddNotice();
//            if (prefManager.isFirstTimeGuideAddNotice()) {
//                RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//                lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//                int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
//                lps.setMargins(margin, margin, margin, margin);
//
//                ViewTarget target = new ViewTarget(R.id.addNoticeFilterFab, this);
//                sv = new ShowcaseView.Builder(this)
//                        .withMaterialShowcase()
//                        .setTarget(target)
//                        .setContentTitle(R.string.showcase_notice_title)
//                        .setContentText(R.string.showcase_notice_message)
//                        .setStyle(R.style.CustomShowcaseTheme2)
//                        .withHoloShowcase()
//                        .build();
//                sv.setButtonPosition(lps);
//                prefManager.setFirstTimeGuideAddNotice(false);
//            }
        }

        mydb = new DataBaseHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.noticeListRecyclerView);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new Notice_List_Adapter(noticeListItems);
        recyclerView.setAdapter(reviewAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();

        getList();
    }

    private void getList() {
        try {
            Notice_Request_Data getNoticeList = new Notice_Request_Data(Notice_List.this);
            getNoticeList.showNotice(noticeListItems, recyclerView, reviewAdapter, schoolId,userType,userId, progressDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addNoticeFilterFab) {

            if(userType.equals("Staff")){
                Intent addNoticePage = new Intent(Notice_List.this, Notice_Add_Teacher.class);
                startActivity(addNoticePage);
            }else{
                Intent addNoticePage = new Intent(Notice_List.this, Notice_Add.class);
                startActivity(addNoticePage);
            }


        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
