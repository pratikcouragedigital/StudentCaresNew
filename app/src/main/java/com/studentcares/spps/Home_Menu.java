package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.UpdateClickListener;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.studentcares.spps.adapter.Home_Menu_Adapter;
import com.studentcares.spps.connectivity.Data_Synchronisation;
import com.studentcares.spps.connectivity.GPS_StaffOutWork;
import com.studentcares.spps.firebase.MyFirebaseMesagingService;
import com.studentcares.spps.gps_service.LocationMonitoringService;
import com.studentcares.spps.internetConnectivity.CheckInternetConnection;
import com.studentcares.spps.model.HomeGrid_Items;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.sqlLite.DataBaseHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Home_Menu extends BaseActivity implements View.OnClickListener {

    Home_Menu activity;
    private RecyclerView  recyclerView;
    private GridLayoutManager gridLayoutManager;
    public List<HomeGrid_Items> listItems = new ArrayList<HomeGrid_Items>();
    private Home_Menu_Adapter listAdapter;

    public Integer[] mThumbIds;
    public String[] title;
    public int[] groupId;
    private String packageName;
    private URL url;
    int currentPage =1;

    SessionManager sessionManager;

    private String NewsResponse;

    private DataBaseHelper mydb;
    private String webMethName;
    private String schoolId,userType,userId,newsDetails = "";
    TextView txtNews;
    LinearLayout newsLayout;
    private ProgressDialog progressDialog;
    String calledFrom =  "Home";
    String VersionCode="";
    String VersionName="";

    CoordinatorLayout coordinatorLayout;

    String positiveButtonText = "";
    Drawable icon = null;
    String msg = "";
    String isDataSynched="";
    boolean doubleBackToExitPressedOnce = false;


 @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_menu);

        sessionManager = new SessionManager(this);
        HashMap<String, String> typeOfUser = sessionManager.getUserDetails();
        HashMap<String, String> isSynch = sessionManager.getSynchDate();
        isDataSynched = isSynch.get(SessionManager.IS_DATA_SYNCHED);

        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        userType = typeOfUser.get(SessionManager.KEY_USERTYPE);
        userId = typeOfUser.get(SessionManager.KEY_USERID);
        mydb = new DataBaseHelper(this);

        String isDataSynched = isSynch.get(SessionManager.IS_DATA_SYNCHED);

		 if(isDataSynched == null){
            isDataSynched = "No";
        }
		
        if(isDataSynched.equals("No")){
            GetAsynchData();
        }

        if (userType.equals("Student")) {
            mThumbIds = new Integer[]{
                    R.drawable.home_attendance, R.drawable.home_message,R.drawable.home_homework,
                    R.drawable.home_fees,
                    R.drawable.home_timetable,R.drawable.home_news, R.drawable.home_notice,
                    R.drawable.home_holiday, R.drawable.home_gallery,R.drawable.home_study_material,
                    R.drawable.home_syllabus,R.drawable.home_ranker,
                    R.drawable.home_profile,R.drawable.home_aboutschool, R.drawable.home_studentcare,

            };
            title = new String[] {"Attendance","SMS","Homework","Fees", "Time Table",
                    "News", "Notice", "Holiday","Gallery", "Study Material","Syllabus",
                    "Ranker",
                     "Profile", "About School", "StudentCares"};

            groupId = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,13,14,15};
        }
        else if (userType.equals("UserAdmin") || userType.equals("Staff")) {
           mThumbIds = new  Integer[]  {
                    R.drawable.home_dashboard, R.drawable.home_machine, R.drawable.home_attendance,
                    R.drawable.home_graph,R.drawable.home_message, R.drawable.home_homework,
                    R.drawable.home_timetable,R.drawable.home_news, R.drawable.home_notice,
                    R.drawable.home_holiday,R.drawable.home_pta, R.drawable.home_fees,
                    R.drawable.home_gallery,R.drawable.home_study_material,R.drawable.home_syllabus,
                    R.drawable.home_ranker,R.drawable.home_leave,R.drawable.home_gps,
                    R.drawable.home_profile, R.drawable.home_aboutschool,R.drawable.home_studentcare,
            };
           title = new String[] {"Dashboard", "Machine Details", "Attendance",
                    "Graph","SMS","Homework","Time Table", "News", "Notice",
                   "Holiday", "PTA", "Fees","Gallery","Study Material","Syllabus",
                   "Ranker","Leave",
                   "GPS","Profile","About School", "StudentCares"};

           groupId = new int[]  {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,17,18,19,20,21};
        }
        else if (userType.equals("NonTeachingStaff")) {
            mThumbIds = new  Integer[] {
                    R.drawable.home_dashboard, R.drawable.home_machine, R.drawable.home_attendance,
                    R.drawable.home_message, R.drawable.home_news, R.drawable.home_notice,
                    R.drawable.home_holiday, R.drawable.home_gallery, R.drawable.home_ranker,R.drawable.home_leave,
                    R.drawable.home_gps, R.drawable.home_profile,
                    R.drawable.home_aboutschool, R.drawable.home_studentcare,
            };
            title = new String[]  {"Dashboard", "Machine Details", "Attendance",
                    "SMS", "News", "Notice", "Holiday",
                    "Gallery","Ranker", "Leave","GPS","Profile", "About School", "StudentCares"};

            groupId = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,12,13,14};
        }

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
        NewsList();

        CheckIsAppUpdated();
        createAppFolder();

// home menu
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        recyclerView = (RecyclerView) findViewById(R.id.wishListRecyclerView);
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.smoothScrollToPosition(0);
        listAdapter = new Home_Menu_Adapter(listItems, activity);
        recyclerView.setAdapter(listAdapter);

        AddGridMenu();

        //marquee news
        txtNews = (TextView) findViewById(R.id.txtNews);
        newsLayout = (LinearLayout) findViewById(R.id.newsLayout);
        newsDetails = "Loading";
        txtNews.setSelected(true);
        newsLayout.setOnClickListener(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        int count = Integer.parseInt(intent.getStringExtra(MyFirebaseMesagingService.EXTRA_COUNT));
                        String read = intent.getStringExtra(MyFirebaseMesagingService.EXTRA_READ);
                        String imfrom = intent.getStringExtra(MyFirebaseMesagingService.EXTRA_IMFROM);

                        if (count != 0) {
//                            listAdapter.notifyItemChanged(8);
                            listAdapter.notifyDataSetChanged();
//                            Toast.makeText(context, ""+imfrom+" Count: "+count, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new IntentFilter(MyFirebaseMesagingService.ACTION_LOCATION_BROADCAST)
        );

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        int count = Integer.parseInt(intent.getStringExtra(UpdateUI.EXTRA_COUNT));
                        String read = intent.getStringExtra(UpdateUI.EXTRA_READ);
                        String imfrom = intent.getStringExtra(UpdateUI.EXTRA_IMFROM);

                        if (count == 0) {
//                            listAdapter.notifyItemChanged(8);
                            listAdapter.notifyDataSetChanged();
//                            Toast.makeText(context, ""+imfr om+" Count: "+count, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new IntentFilter(UpdateUI.ACTION_LOCATION_BROADCAST)
        );

        try {
            VersionName = getPackageManager().getPackageInfo(getPackageName(),0).versionName;
            VersionCode = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        AddUpdater();

    }

    private void AddUpdater() {
        positiveButtonText = "Update";
        msg = "Update is available you need to update the app.";
        icon = getResources().getDrawable(R.drawable.ic_system_update_white_24dp);

        webMethName = "App_Updater";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Version_Name", VersionName);
            jsonObject.put("Version_Code", VersionCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.url) + webMethName;

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Update Available.")) {

                                new MaterialStyledDialog.Builder(Home_Menu.this)
                                        .setTitle("Confirmation!")
                                        .setDescription(msg)
                                        .setPositiveText(positiveButtonText)
                                        .setIcon(icon)
                                        .setCancelable(false)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                try {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                } catch (android.content.ActivityNotFoundException anfe) {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                }
                                            }
                                        })
                                        .show();
                            } else {

                            }
                        } catch (Exception e) {
                            e.getMessage();
                            Toast.makeText(Home_Menu.this, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                        Toast.makeText(Home_Menu.this, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void NewsList() {
        webMethName = "News_List";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("student_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.url) + webMethName;

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Not found News..!!")) {
                                newsDetails = "";
                                newsDetails = "News Not Available.";
                                txtNews.setText(newsDetails);
                            } else {
                                try {
                                    newsDetails = "";
                                    JSONArray jsonArray = response.getJSONArray("responseDetails");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        String newsNo = String.valueOf(i+1);
                                        String newsTitle = "",newsDescription = "",newsDate="";
                                        try {
                                            JSONObject obj = jsonArray.getJSONObject(i);
                                            newsTitle = obj.getString("Title");
                                            newsDate = obj.getString("Added_Date");
                                            newsDescription = obj.getString("Descripton");

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        newsDetails = newsDetails + newsNo +" "+newsDate+" "+ newsTitle +": "+ newsDescription +". ";
                                    }
                                    txtNews.setText(newsDetails);
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            Toast.makeText(Home_Menu.this, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                        Toast.makeText(Home_Menu.this, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void GetAsynchData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Data Synchronization In Progress Please Wait.");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        try {
            Data_Synchronisation getNewsList = new Data_Synchronisation(this);
            getNewsList.GetAsynchData(schoolId,userId,calledFrom,progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CheckIsAppUpdated() {
        packageName = getPackageName();

        try {
            url = new URL("https://play.google.com/store/apps/details?id=" + packageName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        AppUpdater appUpdater = new AppUpdater(this)
                .setDisplay(Display.DIALOG)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .setTitleOnUpdateAvailable("Update available")
                .setContentOnUpdateAvailable("Check out the latest version available of my app!")
                .setTitleOnUpdateNotAvailable("Update not available")
                .setContentOnUpdateNotAvailable("No update available. Check for updates again later!")
                .setButtonUpdate("Update now?")
                .setButtonUpdateClickListener(new UpdateClickListener(this, UpdateFrom.GOOGLE_PLAY, url))
                .setButtonDismiss(null)
                .setButtonDoNotShowAgain(null)
                .setCancelable(false);
        appUpdater.start();
    }

    private void createAppFolder() {
        String mainFolderName = getString(R.string.mainFolderName); //"/StudentCares";
        File studentCaresFolder = new File(Environment.getExternalStorageDirectory() + mainFolderName);
        File imageFolder = new File(Environment.getExternalStorageDirectory() + mainFolderName + "/Images");
        File databaseFolder = new File(Environment.getExternalStorageDirectory() + mainFolderName + "/Database");
        File syllabusFolder = new File(Environment.getExternalStorageDirectory() + mainFolderName + "/Syllabus");
        File studyMaterialFolder = new File(Environment.getExternalStorageDirectory() + mainFolderName + "/Study_Material");
        File homeworkImageFolder = new File(Environment.getExternalStorageDirectory() + mainFolderName + "/Images/Homework");
        File profileImageFolder = new File(Environment.getExternalStorageDirectory() + mainFolderName + "/Images/Profile");
        File noticeImageFolder = new File(Environment.getExternalStorageDirectory() + mainFolderName + "/Images/Notice");
        File schoolImageFolder = new File(Environment.getExternalStorageDirectory() + mainFolderName + "/Images/Event");
        File newsImageFolder = new File(Environment.getExternalStorageDirectory() + mainFolderName + "/Images/News");

        if (!studentCaresFolder.exists()) {
            if (studentCaresFolder.mkdir()) ; //directory is created;
        }
        if (!imageFolder.exists()) {
            if (imageFolder.mkdir()) ; //Images directory is created;
        }
        if (!databaseFolder.exists()) {
            if (databaseFolder.mkdir()) ; //Images directory is created;
        }
        if (!homeworkImageFolder.exists()) {
            if (homeworkImageFolder.mkdir()) ; //Images directory is created;
        }
        if (!profileImageFolder.exists()) {
            if (profileImageFolder.mkdir()) ; //Images directory is created;
        }
        if (!schoolImageFolder.exists()) {
            if (schoolImageFolder.mkdir()) ; //School Gallery_Title_List directory is created;
        }
        if (!noticeImageFolder.exists()) {
            if (noticeImageFolder.mkdir()) ; //Notice_List directory is created;
        }
        if (!newsImageFolder.exists()) {
            if (newsImageFolder.mkdir()) ; //Notice_List directory is created;
        }
        if (!syllabusFolder.exists()) {
            if (syllabusFolder.mkdir()) ; //Syllabus directory is created;
        }
        if (!studyMaterialFolder.exists()) {
            if (studyMaterialFolder.mkdir()) ; //Study_material directory is created;
        }
    }

    private void AddGridMenu() {

        int size = mThumbIds.length;

        for (int i = 0; i < size; i++) {
            HomeGrid_Items gridViewItems = new HomeGrid_Items();
            gridViewItems.setFirstImagePath(mThumbIds[i]);
            gridViewItems.settitle(title[i]);
            gridViewItems.setgridId(groupId[i]);
            listItems.add(gridViewItems);
        }
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.newsLayout){
            Intent gotoNews = new Intent(this,News_List.class);
            startActivity(gotoNews);
        }
    }


}
