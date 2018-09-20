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

import com.studentcares.spps.adapter.News_List_Adapter;
import com.studentcares.spps.connectivity.News_Request_Data;
import com.studentcares.spps.model.News_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class News_List extends BaseActivity implements View.OnClickListener {

    private ProgressDialog progressDialog = null;

    public List<News_Items> newsItems = new ArrayList<News_Items>();
    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;

    String schoolId;
    String userId = "";
    String userType;

    private int current_page = 1;
    FloatingActionButton addNewsFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);

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

        addNewsFab = (FloatingActionButton) findViewById(R.id.addNewsFab);
        addNewsFab.setVisibility(View.GONE);

        //UserAdmin
        if (userType.equals("UserAdmin") || userType.equals("Principle")) {
            addNewsFab.setVisibility(View.VISIBLE);
            addNewsFab.setOnClickListener(this);

            /// one time guidelines
//            prefManager = new PrefManager(this);
//            boolean abs = prefManager.isFirstTimeGuideAddNotice();
//            if (prefManager.isFirstTimeGuideAddNotice()) {
//                RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//                lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//                int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
//                lps.setMargins(margin, margin, margin, margin);
//
//                ViewTarget target = new ViewTarget(R.id.addNewsFab, this);
//                sv = new ShowcaseView.Builder(this)
//                        .withMaterialShowcase()
//                        .setTarget(target)
//                        .setContentTitle(R.string.showcase_news_title)
//                        .setContentText(R.string.showcase_news_message)
//                        .setStyle(R.style.CustomShowcaseTheme2)
//                        .withHoloShowcase()
//                        .build();
//                sv.setButtonPosition(lps);
//                prefManager.setFirstTimeGuideAddNotice(false);
//            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.newsRecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new News_List_Adapter(newsItems);
        recyclerView.setAdapter(reviewAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();

        getList(current_page);
//        recyclerView.addOnScrollListener(new HomeworkList_ScrollListener(linearLayoutManager, current_page) {
//
//            @Override
//            public void onLoadMore(int current_page) {
//                getList(current_page);
//            }
//        });
    }

    private void getList(int current_page) {
        try {
            News_Request_Data getNewsList = new News_Request_Data(this);
            getNewsList.showNewsList(newsItems, recyclerView, reviewAdapter,  schoolId, current_page, progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addNewsFab) {
            Intent addNoticePage = new Intent(this, News_Add.class);
            startActivity(addNoticePage);

        }
    }
}