package com.studentcares.spps;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.studentcares.spps.adapter.Homework_List_Adapter_Parents;
import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.connectivity.Get_StdDivSub_Sqlite;
import com.studentcares.spps.connectivity.Homework_List_Show_Parents;
import com.studentcares.spps.internetConnectivity.CheckInternetConnection;
import com.studentcares.spps.model.Homework_Items;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.sqlLite.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Homework_List_Parents extends BaseActivity implements View.OnClickListener {

    public List<Homework_Items> homeWorkItems = new ArrayList<Homework_Items>();
    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    private ProgressDialog progressDialog = null;
    FloatingActionButton homeworkFilterFab, dateFilterFab, subjectFilterFab;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private Boolean isFabOpen = false;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    private StringBuilder date;
    String selectedSubjectId = "";
    String homeworkSubmissionDate = "";
    TextView txtSelectedFilter;

    String userId;
    String schoolId;
    String standardName;
    String divisionName;
    String counter = "1";

    Spinner subject;
    String subjectName;
    String subjectId;
    private String[] subjectArrayList;
    private Std_Div_Filter_Adapter studentWiseSubjectAdapter;
    private ProgressDialog subjectDialogBox;
    private List<String> subjectIdList = new ArrayList<String>();
    private List<String> subjectNameList = new ArrayList<String>();

    private int current_page = 1;

    private DataBaseHelper mydb;
    int count;

    private Animation mEnterAnimation, mExitAnimation;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.homework_list_p);

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

        mydb = new DataBaseHelper(this);

        SessionManager sessionManagerNgo = new SessionManager(Homework_List_Parents.this);
        HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
        userId = typeOfUser.get(SessionManager.KEY_USERID);
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        standardName = typeOfUser.get(SessionManager.KEY_STANDARD);
        divisionName = typeOfUser.get(SessionManager.KEY_DIVISION);

        //getSubjectDetails();

        recyclerView = (RecyclerView) findViewById(R.id.homeworkListRecyclerView);
        homeworkFilterFab = (FloatingActionButton) findViewById(R.id.homeworkFilterFab);
        dateFilterFab = (FloatingActionButton) findViewById(R.id.dateFilterFab);
        subjectFilterFab = (FloatingActionButton) findViewById(R.id.subjectFilterFab);
        //txtSelectedFilter = (TextView) findViewById(R.id.txtSelectedFilter);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        homeworkFilterFab.setOnClickListener(this);
        dateFilterFab.setOnClickListener(this);
        subjectFilterFab.setOnClickListener(this);

        animation = new TranslateAnimation(0f, 0f, 200f, 0f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setInterpolator(new BounceInterpolator());

//        prefManager = new PrefManager(this);
//        boolean abs = prefManager.isFirstTimeGuideHomeworkFilter();
//        if (prefManager.isFirstTimeGuideHomeworkFilter()) {
//            RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
//            lps.setMargins(margin, margin, margin, margin);
//
//            ViewTarget target = new ViewTarget(R.id.homeworkFilterFab, this);
//            sv = new ShowcaseView.Builder(this)
//                    .withMaterialShowcase()
//                    .setTarget(target)
//                    .setContentTitle(R.string.showcase_homework_title)
//                    .setContentText(R.string.showcase_homework_message)
//                    .setStyle(R.style.CustomShowcaseTheme2)
//                    .withHoloShowcase()
//                    .build();
//
//            sv.setButtonPosition(lps);
//            prefManager.setFirstTimeGuideHomewrokFilter(false);
//        }

        if (standardName.equals("") && divisionName.equals("")) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Message");
            builder.setMessage("Please Select Standard & Division.Using below Button.");
            android.app.AlertDialog alert1 = builder.create();
            alert1.show();
        } else {
            recyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.smoothScrollToPosition(0);
            reviewAdapter = new Homework_List_Adapter_Parents(homeWorkItems);
            recyclerView.setAdapter(reviewAdapter);

            if (CheckInternetConnection.getInstance(this).isOnline()) {
                getList(current_page);
            } else {
                getOffline_List();
            }
        }

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

//        recyclerView.addOnScrollListener(new HomeworkList_ScrollListener(linearLayoutManager, current_page) {
//
//            @Override
//            public void onLoadMore(int current_page) {
//                if (counter.equals("1")) {
//                    getList(current_page);
//                } else if (counter.equals("2")) {
//                    getFilterList(current_page);
//                }
//            }
//        });
//        subject.setVisibility(View.GONE);
    }

    private void getOffline_List() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();
        JSONArray listArray = null;
        try {
            listArray = mydb.getAllHomeworkList(userId);
            progressDialog.dismiss();
            if (listArray.length() == 0) {
                Toast.makeText(getApplicationContext(), "List Is Not Available.", Toast.LENGTH_LONG).show();
            } else {
                for (int i = 0; i < listArray.length(); i++) {
                    try {
                        JSONObject obj = listArray.getJSONObject(i);
                        Homework_Items THomeWorkItems = new Homework_Items();
                        THomeWorkItems.setsubjectName(obj.getString("subject_Name"));
                        THomeWorkItems.setstandard(obj.getString("standard"));
                        THomeWorkItems.setdivision(obj.getString("division"));
                        THomeWorkItems.setteacherName(obj.getString("teacher_Name"));
                        THomeWorkItems.setsubmissionDate(obj.getString("submission_Date"));
                        THomeWorkItems.setaddedDate(obj.getString("addedDate"));
                        THomeWorkItems.sethomework(obj.getString("homework"));
                        THomeWorkItems.sethomeworkTitle(obj.getString("homeworkTitle"));
                        if (!obj.getString("homeworkImage").isEmpty() && obj.getString("homeworkImage") != null) {
                            THomeWorkItems.setFirstImagePath(obj.getString("homeworkImage"));
                        }
                        homeWorkItems.add(THomeWorkItems);
                        reviewAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getList(int current_page) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();
        try {
            Homework_List_Show_Parents showHomeworkDetails = new Homework_List_Show_Parents(Homework_List_Parents.this);
            showHomeworkDetails.showHomeworkForParents(homeWorkItems, recyclerView, reviewAdapter, userId,schoolId, selectedSubjectId, homeworkSubmissionDate, counter, current_page,standardName,divisionName, progressDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFilterList(int current_page, String filterededSubjectId,String submissionDate) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();
        try {
            Homework_List_Show_Parents showHomeworkDetails = new Homework_List_Show_Parents(Homework_List_Parents.this);
            showHomeworkDetails.showHomeworkForParents(homeWorkItems, recyclerView, reviewAdapter, userId,schoolId, filterededSubjectId, submissionDate, counter, current_page,standardName,divisionName, progressDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSubjectDetails() {
        subjectArrayList = new String[]{
                "Select Subject"
        };
        subjectNameList = new ArrayList<>(Arrays.asList(subjectArrayList));
        studentWiseSubjectAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, subjectNameList);
        getListOfSubject();
        studentWiseSubjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject.setAdapter(studentWiseSubjectAdapter);
    }

    private void getListOfSubject() {
        Get_StdDivSub_Sqlite subjectSpinnerList = new Get_StdDivSub_Sqlite(this);
        subjectSpinnerList.FetchAllSubject(subjectNameList, subjectIdList,schoolId, studentWiseSubjectAdapter, standardName, divisionName);
    }



    @Override
    public void onClick(final View v) {

        int id = v.getId();
        switch (id) {
            case R.id.homeworkFilterFab:
                animateFAB();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mEnterAnimation = new AlphaAnimation(0f, 1f);
                        mEnterAnimation.setDuration(600);
                        mEnterAnimation.setFillAfter(true);

                        mExitAnimation = new AlphaAnimation(1f, 0f);
                        mExitAnimation.setDuration(600);
                        mExitAnimation.setFillAfter(true);
                    }
                }, 500);
                break;
            case R.id.dateFilterFab:
                break;
            case R.id.subjectFilterFab:
                break;
        }

        if (v.getId() == R.id.dateFilterFab) {
            setDate(v);
        } else if (v.getId() == R.id.subjectFilterFab) {

            //Dynamic Spinner
            final AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.MyDialogTheme);
            int margin = (int) getResources().getDimension(R.dimen.margin);
            subject = new Spinner(this);
            RelativeLayout container = new RelativeLayout(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(margin, margin, margin, margin);

            subject.setLayoutParams(params);
            container.addView(subject);
            alert.setTitle(Html.fromHtml("<b>" + "Select Subject." + "</b>"));

            getSubjectDetails();
            alert.setView(container);
            subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        subjectName = parent.getItemAtPosition(position).toString();
                        subjectId = subjectIdList.get(position);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // String subject = edittext.getText().toString();
                    current_page = 1;
                    counter = "2";
                    selectedSubjectId = subjectId;
                    homeworkSubmissionDate="";
                    if (CheckInternetConnection.getInstance(getApplicationContext()).isOnline()) {
                        getFilterList(current_page,selectedSubjectId,homeworkSubmissionDate);
                    } else {
                        count = 1;
                        JSONArray listArray = null;
                        try {
                            listArray = mydb.getAllFilterWiseHomeworkList(userId, subjectName, count);
                            progressDialog.dismiss();
                            if (listArray.length() == 0) {
                                Toast.makeText(getApplicationContext(), "List Is Not Available.", Toast.LENGTH_LONG).show();
                            } else {
                                for (int i = 0; i < listArray.length(); i++) {
                                    try {
                                        JSONObject obj = listArray.getJSONObject(i);
                                        Homework_Items THomeWorkItems = new Homework_Items();
                                        THomeWorkItems.setsubjectName(obj.getString("subject_Name"));
                                        THomeWorkItems.setstandard(obj.getString("standard"));
                                        THomeWorkItems.setdivision(obj.getString("division"));
                                        THomeWorkItems.setteacherName(obj.getString("teacher_Name"));
                                        THomeWorkItems.setsubmissionDate(obj.getString("submission_Date"));
                                        THomeWorkItems.setaddedDate(obj.getString("addedDate"));
                                        THomeWorkItems.sethomework(obj.getString("homework"));
                                        THomeWorkItems.sethomeworkTitle(obj.getString("homeworkTitle"));
                                        if (!obj.getString("homeworkImage").isEmpty() && obj.getString("homeworkImage") != null) {
                                            THomeWorkItems.setFirstImagePath(obj.getString("homeworkImage"));
                                        }
                                        homeWorkItems.add(THomeWorkItems);
                                        reviewAdapter.notifyDataSetChanged();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });
            alert.show();
        }
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
//        Toast.makeText(getApplicationContext(), "Select Date", Toast.LENGTH_SHORT)
//                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {

        //txtSelectedFilter.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
        date = new StringBuilder().append(year).append("-").append(month).append("-").append(day);
        String selectedDate = date.toString();

        counter = "2";
        current_page = 1;
        homeworkSubmissionDate = selectedDate;
        selectedSubjectId="";
        //getFilterList(current_page);
        if (CheckInternetConnection.getInstance(getApplicationContext()).isOnline()) {
            getFilterList(current_page,selectedSubjectId,homeworkSubmissionDate);
        } else {
            count = 2;
            JSONArray listArray = null;
            try {
                listArray = mydb.getAllFilterWiseHomeworkList(userId, selectedDate, count);
                progressDialog.dismiss();
                if (listArray.length() == 0) {
                    Toast.makeText(getApplicationContext(), "List Is Not Available.", Toast.LENGTH_LONG).show();
                } else {
                    for (int i = 0; i < listArray.length(); i++) {
                        try {
                            JSONObject obj = listArray.getJSONObject(i);
                            Homework_Items THomeWorkItems = new Homework_Items();
                            THomeWorkItems.setsubjectName(obj.getString("subject_Name"));
                            THomeWorkItems.setstandard(obj.getString("standard"));
                            THomeWorkItems.setdivision(obj.getString("division"));
                            THomeWorkItems.setteacherName(obj.getString("teacher_Name"));
                            THomeWorkItems.setsubmissionDate(obj.getString("submission_Date"));
                            THomeWorkItems.setaddedDate(obj.getString("addedDate"));
                            THomeWorkItems.sethomework(obj.getString("homework"));
                            THomeWorkItems.sethomeworkTitle(obj.getString("homeworkTitle"));
                            if (!obj.getString("homeworkImage").isEmpty() && obj.getString("homeworkImage") != null) {
                                THomeWorkItems.setFirstImagePath(obj.getString("homeworkImage"));
                            }
                            homeWorkItems.add(THomeWorkItems);
                            reviewAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void animateFAB() {
        if (isFabOpen) {

            homeworkFilterFab.startAnimation(rotate_backward);
            dateFilterFab.startAnimation(fab_close);
            subjectFilterFab.startAnimation(fab_close);
            dateFilterFab.setClickable(false);
            subjectFilterFab.setClickable(false);
            isFabOpen = false;
        } else {
//            runOverlay_ContinueMethod2();
            homeworkFilterFab.startAnimation(rotate_forward);
            dateFilterFab.startAnimation(fab_open);
            subjectFilterFab.startAnimation(fab_open);
            dateFilterFab.setClickable(true);
            subjectFilterFab.setClickable(true);
            isFabOpen = true;
        }
    }


}
