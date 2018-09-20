package com.studentcares.spps;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.adapter.Homework_List_Adapter_Teacher;
import com.studentcares.spps.connectivity.Get_StdDivSub_Sqlite;
import com.studentcares.spps.connectivity.Homework_List_Show_Teacher;
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

public class Homework_List_Teacher extends BaseActivity implements View.OnClickListener {

    private ProgressDialog progressDialog = null;
    String staffId = "";
    String schoolId;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    private StringBuilder date;
    TextView txtSelectedFilter;
    String selectedDate = "";

    Spinner standard;
    Spinner division;

    String standardName = "";
    String standardId = "";
    private String[] standardArrayList;
    private Std_Div_Filter_Adapter spinnerAdapter;
    private ProgressDialog standardDialogBox;
    private List<String> standardIdList = new ArrayList<String>();
    private List<String> standardNameList = new ArrayList<String>();

    String divisionName = "";
    String divisionId = "";
    private String[] divisionArrayList;
    private ProgressDialog divisionDialogBox;
    private List<String> divisionIdList = new ArrayList<String>();
    private List<String> divisionNameList = new ArrayList<String>();

    Spinner subject;
    String subjectName = "";
    String subjectId = "";
    private String[] subjectArrayList;
    private ProgressDialog subjectDialogBox;
    private List<String> subjectIdList = new ArrayList<String>();
    private List<String> subjectNameList = new ArrayList<String>();

    public List<Homework_Items> THomeWorkItems = new ArrayList<Homework_Items>();
    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    LinearLayout stdDivSubFilterLayout;
    LinearLayout datewiseFilterLayout;
    LinearLayout addHomeworkLayout;
    RelativeLayout emptyLayout;

    String counter = "1";
    int count;


    private int current_page = 1;

    private DataBaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homework_list_t);
        mydb = new DataBaseHelper(this);

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
        staffId = typeOfUser.get(SessionManager.KEY_USERID);
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        standardId = typeOfUser.get(SessionManager.KEY_STANDARD);
        divisionId = typeOfUser.get(SessionManager.KEY_DIVISION);

        emptyLayout = (RelativeLayout) findViewById(R.id.emptyLayout);
        stdDivSubFilterLayout = (LinearLayout) findViewById(R.id.stdDivSubFilterLayout);
        datewiseFilterLayout = (LinearLayout) findViewById(R.id.datewiseFilterLayout);
        addHomeworkLayout = (LinearLayout) findViewById(R.id.addHomeworkLayout);


        datewiseFilterLayout.setOnClickListener(this);
        stdDivSubFilterLayout.setOnClickListener(this);
        addHomeworkLayout.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.homeworkListRecyclerView);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new Homework_List_Adapter_Teacher(THomeWorkItems);
        recyclerView.setAdapter(reviewAdapter);

        if (CheckInternetConnection.getInstance(this).isOnline()) {
            getList(current_page);
        } else {
            getOffline_List();
        }

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

//        prefManager = new PrefManager(this);
//        boolean abs = prefManager.isFirstTimeGuide_T_HomeworkFilter();
//        if (prefManager.isFirstTimeGuide_T_HomeworkFilter()) {
//            RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
//            lps.setMargins(margin, margin, margin, margin);
//
//            ViewTarget target = new ViewTarget(R.id.mainFabBtn, this);
//            sv = new ShowcaseView.Builder(this)
//                    .withMaterialShowcase()
//                    .setTarget(target)
//                    .setContentTitle(R.string.showcase_t_homework_title)
//                    .setContentText(R.string.showcase_t_homework_message)
//                    .setStyle(R.style.CustomShowcaseTheme2)
//                    .withHoloShowcase()
//                    .build();
//            sv.setButtonPosition(lps);
//            prefManager.setFirstTimeGuide_T_HomewrokFilter(false);
//        }
    }

    private void getOffline_List() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();
        JSONArray listArray = null;
        try {
            listArray = mydb.getAllHomeworkList(staffId);
            progressDialog.dismiss();
            if (listArray.length() == 0) {
                Toast.makeText(getApplicationContext(), "List Is Not Available.", Toast.LENGTH_LONG).show();
            } else {
                for (int i = 0; i < listArray.length(); i++) {
                    try {
                        JSONObject obj = listArray.getJSONObject(i);
                        Homework_Items HomeWorkItems = new Homework_Items();
                        HomeWorkItems.setsubjectName(obj.getString("subject_Name"));
                        HomeWorkItems.setstandard(obj.getString("standard"));
                        HomeWorkItems.setdivision(obj.getString("division"));
                        HomeWorkItems.setteacherName(obj.getString("teacher_Name"));
                        HomeWorkItems.setsubmissionDate(obj.getString("submission_Date"));
                        HomeWorkItems.setaddedDate(obj.getString("addedDate"));
                        HomeWorkItems.sethomework(obj.getString("homework"));
                        HomeWorkItems.sethomeworkTitle(obj.getString("homeworkTitle"));
                        if (!obj.getString("homeworkImage").isEmpty() && obj.getString("homeworkImage") != null) {
                            HomeWorkItems.setFirstImagePath(obj.getString("homeworkImage"));
                        }

                        THomeWorkItems.add(HomeWorkItems);
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
        progressDialog.setMessage("Please Wait.");
        progressDialog.show();
        try {
            Homework_List_Show_Teacher showHomeworkDetails = new Homework_List_Show_Teacher(Homework_List_Teacher.this);
            showHomeworkDetails.showHomeworkListForTeacher(THomeWorkItems, recyclerView, reviewAdapter, standardId, divisionId, subjectId,schoolId, selectedDate, counter, staffId, current_page, progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFilterList(int current_page,String stdId,String divId,String subId,String selectedDate) {
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new Homework_List_Adapter_Teacher(THomeWorkItems);
        recyclerView.setAdapter(reviewAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();
        try {
            Homework_List_Show_Teacher TShowHomeworkDetails = new Homework_List_Show_Teacher(Homework_List_Teacher.this);
            TShowHomeworkDetails.showHomeworkListForTeacher(THomeWorkItems, recyclerView, reviewAdapter, stdId, divId, subId,schoolId, selectedDate, counter, staffId, current_page, progressDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getStandarddDetails() {
        standardArrayList = new String[]{
                "Standard"
        };
        standardNameList = new ArrayList<>(Arrays.asList(standardArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, standardNameList);
        getListOfStandard();
        //getListOfStandardOffline();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        standard.setAdapter(spinnerAdapter);
        standard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    standardName = parent.getItemAtPosition(position).toString();
                    standardId = standardIdList.get(position);
                    getListOfDivision();
                   // getListOfDivisionOffline();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getListOfStandard() {
        Get_StdDivSub_Sqlite standardSpinnerList = new Get_StdDivSub_Sqlite(this);
        standardSpinnerList.FetchAllstandard(standardNameList, standardIdList, schoolId,spinnerAdapter);
    }

    private void getDivisionDetails() {
        divisionArrayList = new String[]{
                "Division"
        };
        divisionNameList = new ArrayList<>(Arrays.asList(divisionArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, divisionNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        division.setAdapter(spinnerAdapter);
        division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    divisionName = parent.getItemAtPosition(position).toString();
                    divisionId = divisionIdList.get(position);
                    getListOfSubject();
                    //getListOfSubjectOffline();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getListOfDivision() {
        divisionDialogBox = ProgressDialog.show(this, "", "Fetching Division Please Wait...", true);
        Get_StdDivSub_Sqlite divisionSpinnerList = new Get_StdDivSub_Sqlite(this);
        divisionSpinnerList.FetchAllDivision(divisionNameList, divisionIdList,schoolId, spinnerAdapter, standardId, divisionDialogBox);
    }

    private void getSubjectDetails() {
        subjectArrayList = new String[]{
                "Subject"
        };
        subjectNameList = new ArrayList<>(Arrays.asList(subjectArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, subjectNameList);
       // getListOfSubject();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject.setAdapter(spinnerAdapter);
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
    }

    private void getListOfSubject() {
        Get_StdDivSub_Sqlite subjectSpinnerList = new Get_StdDivSub_Sqlite(this);
        subjectSpinnerList.FetchAllSubject(subjectNameList, subjectIdList,schoolId, spinnerAdapter, standardId, divisionId);
    }

    @Override
    public void onClick(final View v) {

        if (v.getId() == R.id.addHomeworkLayout) {
            Intent addHomework = new Intent(this, Homework_Add_Teacher.class);
            startActivity(addHomework);
        }
        else if (v.getId() == R.id.datewiseFilterLayout) {
            setDate(v);
        }
        else if (v.getId() == R.id.stdDivSubFilterLayout) {

            //Dynamic Spinner
            final AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.MyDialogTheme);

            ScrollView sv = new ScrollView(this);
            sv.setFillViewport(true);

            RelativeLayout layout = new RelativeLayout(this);
            layout.setLayoutParams(new RelativeLayout.LayoutParams(
                    CoordinatorLayout.LayoutParams.MATCH_PARENT,
                    CoordinatorLayout.LayoutParams.WRAP_CONTENT));

            int margin = (int) getResources().getDimension(R.dimen.margin);
            standard = new Spinner(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            standard.setLayoutParams(params);
            params.setMargins(margin, margin, margin, margin);
            standard.setId(Integer.parseInt("1"));

            division = new Spinner(this);
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, standard.getId());
            params.setMargins(margin, margin, margin, margin);
            division.setLayoutParams(params);
            division.setId(Integer.parseInt("2"));

            subject = new Spinner(this);
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, division.getId());
            params.setMargins(margin, margin, margin, margin);
            subject.setLayoutParams(params);
            subject.setId(Integer.parseInt("3"));

            TextView txtView = new TextView(this);
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, subject.getId());
            params.setMargins(margin, margin, margin, margin);
            txtView.setLayoutParams(params);

            layout.addView(txtView);

            layout.addView(standard);
            layout.addView(division);
            layout.addView(subject);
            sv.addView(layout);

            alert.setTitle(Html.fromHtml("<b>" + "Select Standard, Division & Subject." + "</b>"));

            getStandarddDetails();
            getDivisionDetails();
            getSubjectDetails();

            alert.setView(sv);

            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    current_page = 1;
                    counter = "2";
                    if (standardName == null || standardName.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please select Standard.", Toast.LENGTH_LONG).show();
                    } else if (divisionName == null || divisionName.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please select Division.", Toast.LENGTH_LONG).show();
                    }else if (subjectName == null || subjectName.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please select Division.", Toast.LENGTH_LONG).show();
                    } else {
                        // getFilterList(current_page);
                        if (CheckInternetConnection.getInstance(getApplicationContext()).isOnline()) {
                            selectedDate ="";
                            getFilterList(current_page,standardId,divisionId,subjectId,selectedDate);
                        } else {
                            count = 1;
                            JSONArray listArray = null;
                            try {
                                listArray = mydb.getAllFilterWiseHomeworkList(staffId, subjectName, count);
                                progressDialog.dismiss();
                                if (listArray.length() == 0) {
                                    Toast.makeText(getApplicationContext(), "List Is Not Available.", Toast.LENGTH_LONG).show();
                                } else {
                                    for (int i = 0; i < listArray.length(); i++) {
                                        try {
                                            JSONObject obj = listArray.getJSONObject(i);
                                            Homework_Items HomeWorkItems = new Homework_Items();
                                            HomeWorkItems.setsubjectName(obj.getString("subject_Name"));
                                            HomeWorkItems.setstandard(obj.getString("standard"));
                                            HomeWorkItems.setdivision(obj.getString("division"));
                                            HomeWorkItems.setteacherName(obj.getString("teacher_Name"));
                                            HomeWorkItems.setsubmissionDate(obj.getString("submission_Date"));
                                            HomeWorkItems.setaddedDate(obj.getString("addedDate"));
                                            HomeWorkItems.sethomework(obj.getString("homework"));
                                            HomeWorkItems.sethomeworkTitle(obj.getString("homeworkTitle"));
                                            if (!obj.getString("homeworkImage").isEmpty() && obj.getString("homeworkImage") != null) {
                                                HomeWorkItems.setFirstImagePath(obj.getString("homeworkImage"));
                                            }

                                            THomeWorkItems.add(HomeWorkItems);
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
        selectedDate = date.toString();

        current_page = 1;
        counter = "2";
        standardId="";
        divisionId="";
        subjectId="";
        //getFilterList(current_page);
        if (CheckInternetConnection.getInstance(getApplicationContext()).isOnline()) {
            getFilterList(current_page,standardId,divisionId,subjectId,selectedDate);
        } else {
            count = 2;
            JSONArray listArray = null;
            try {
                listArray = mydb.getAllFilterWiseHomeworkList(staffId, selectedDate, count);
                progressDialog.dismiss();
                if (listArray.length() == 0) {
                    Toast.makeText(getApplicationContext(), "List Is Not Available.", Toast.LENGTH_LONG).show();
                } else {
                    for (int i = 0; i < listArray.length(); i++) {
                        try {
                            JSONObject obj = listArray.getJSONObject(i);
                            Homework_Items HomeWorkItems = new Homework_Items();
                            HomeWorkItems.setsubjectName(obj.getString("subject_Name"));
                            HomeWorkItems.setstandard(obj.getString("standard"));
                            HomeWorkItems.setdivision(obj.getString("division"));
                            HomeWorkItems.setteacherName(obj.getString("teacher_Name"));
                            HomeWorkItems.setsubmissionDate(obj.getString("submission_Date"));
                            HomeWorkItems.setaddedDate(obj.getString("addedDate"));
                            HomeWorkItems.sethomework(obj.getString("homework"));
                            HomeWorkItems.sethomeworkTitle(obj.getString("homeworkTitle"));
                            if (!obj.getString("homeworkImage").isEmpty() && obj.getString("homeworkImage") != null) {
                                HomeWorkItems.setFirstImagePath(obj.getString("homeworkImage"));
                            }

                            THomeWorkItems.add(HomeWorkItems);
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

}
