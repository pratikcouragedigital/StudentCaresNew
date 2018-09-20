package com.studentcares.spps;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.connectivity.Get_StdDivSub_Sqlite;
import com.studentcares.spps.connectivity.Homework_List_Show_Teacher;
import com.studentcares.spps.imageModule.Image;
import com.studentcares.spps.internetConnectivity.NetworkChangeReceiver;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Homework_Add_Teacher extends BaseActivity implements View.OnClickListener, Image.OnRecyclerSetImageListener {

    ImageView firstImage;
    String firstImagePath = "";
    LinearLayout imageViewLinearLayout;

    EditText txtHomeworkTitle;
    EditText txtHomework;
    EditText submitionDate;
    Button btnAddHomework;
    Button btnAddHomeworkImage;

    private StringBuilder date;
    String selectedDateForSubmission = "";
    String homework = "";
    String homeworkTitle = "";
    String staffId = "";
    String schoolId = "";

    Spinner standard;
    Spinner division;
    Spinner subject;

    String standardName;
    String standardId;
    private String[] standardArrayList;
    private Std_Div_Filter_Adapter spinnerAdapter;
    private ProgressDialog standardDialogBox;
    private List<String> standardIdList = new ArrayList<String>();
    private List<String> standardNameList = new ArrayList<String>();

    String divisionName;
    String divisionId;
    private String[] divisionArrayList;
    private ProgressDialog divisionDialogBox;
    private List<String> divisionIdList = new ArrayList<String>();
    private List<String> divisionNameList = new ArrayList<String>();

    String subjectName;
    String subjectId;
    private String[] subjectArrayList;
    //private DivisionSpinnerAdapter subjectSpinnerAdapter;
    private ProgressDialog subjectDialogBox;
    private List<String> subjectIdList = new ArrayList<String>();
    private List<String> subjectNameList = new ArrayList<String>();

    private Calendar calendar;
    private int year, month, day;

    private ProgressDialog progressDialog = null;

    Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homework_add_t);

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

        txtHomework = (EditText) findViewById(R.id.txtHomework);
        firstImage = (ImageView) findViewById(R.id.firstHomeWorkImage);
        txtHomeworkTitle = (EditText) findViewById(R.id.txtHomeworkTitle);
        submitionDate = (EditText) findViewById(R.id.txtSubmissionDate);
        btnAddHomework = (Button) findViewById(R.id.btnAddHomework);
        btnAddHomeworkImage = (Button) findViewById(R.id.btnAddHomeworkImage);
        standard = (Spinner) findViewById(R.id.spinnerForStandard);
        division = (Spinner) findViewById(R.id.spinnerForDivision);
        subject = (Spinner) findViewById(R.id.spinnerForSubject);
        imageViewLinearLayout = (LinearLayout) findViewById(R.id.imageViewLinearLayout);

        imageViewLinearLayout.setVisibility(View.GONE);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        btnAddHomework.setOnClickListener(this);
        btnAddHomeworkImage.setOnClickListener(this);

        submitionDate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                setDate(view);
                return false;
            }
        });
        getStandarddDetails();
        getDivisionDetails();
        getSubjectDetails();

    }

    private void getStandarddDetails() {
        standardArrayList = new String[]{
                "Standard"
        };
        standardNameList = new ArrayList<>(Arrays.asList(standardArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, standardNameList);
        getListOfStandard();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        standard.setAdapter(spinnerAdapter);
        standard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    standardName = parent.getItemAtPosition(position).toString();
                    standardId = standardIdList.get(position);
                    getListOfDivision();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getListOfStandard() {
        Get_StdDivSub_Sqlite standardSpinnerList = new Get_StdDivSub_Sqlite(this);
        standardSpinnerList.FetchAllstandard(standardNameList, standardIdList, schoolId, spinnerAdapter);
    }

    private void getDivisionDetails() {
        divisionArrayList = new String[]{
                "Division"
        };
        divisionNameList = new ArrayList<>(Arrays.asList(divisionArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, divisionNameList);
        // getListOfDivision();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        division.setAdapter(spinnerAdapter);
        division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    divisionName = parent.getItemAtPosition(position).toString();
                    divisionId = divisionIdList.get(position);
                    getListOfSubject();
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
        divisionSpinnerList.FetchAllDivision(divisionNameList, divisionIdList, schoolId, spinnerAdapter, standardId, divisionDialogBox);
    }

    private void getSubjectDetails() {
        subjectArrayList = new String[]{
                "Subject"
        };
        subjectNameList = new ArrayList<>(Arrays.asList(subjectArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, subjectNameList);
        //getListOfSubject();
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
        subjectSpinnerList.FetchAllSubject(subjectNameList, subjectIdList, schoolId, spinnerAdapter, standardId, divisionId);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnAddHomework) {
            homework = txtHomework.getText().toString();
            homeworkTitle = txtHomeworkTitle.getText().toString();

            if (standardName == null || standardName.isEmpty()) {
                Toast.makeText(this, "Please select Standard.", Toast.LENGTH_LONG).show();
            } else if (divisionName == null || divisionName.isEmpty()) {
                Toast.makeText(this, "Please select Division.", Toast.LENGTH_LONG).show();
            } else if (subjectName == null || subjectName.isEmpty()) {
                Toast.makeText(this, "Please select Subject.", Toast.LENGTH_LONG).show();
            } else if (selectedDateForSubmission == "") {
                Toast.makeText(this, "Please Select Date Of Submission.", Toast.LENGTH_SHORT).show();
            } else if (homework.equals("")) {
                Toast.makeText(this, "Please Enter Homework.", Toast.LENGTH_SHORT).show();
            } else if (homeworkTitle.equals("")) {
                Toast.makeText(this, "Please Enter Homework Title.", Toast.LENGTH_SHORT).show();
            } else {

                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait.Homework Is Adding.");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                try {
                    Homework_List_Show_Teacher TAddHomeworkDetails = new Homework_List_Show_Teacher(this);
                    TAddHomeworkDetails.addHomework(homework, selectedDateForSubmission, staffId, schoolId, standardId, divisionId, subjectId, homeworkTitle, firstImagePath, progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (v.getId() == R.id.btnAddHomeworkImage) {
                image = new Image(this, "HomeWork", this);
                image.getImage();
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

        submitionDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));

            date = new StringBuilder().append(year).append("-").append(month).append("-").append(day);

        selectedDateForSubmission = date.toString();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        PackageManager pm = this.getPackageManager();
        ComponentName component = new ComponentName(this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onResume() {
        super.onResume();
        PackageManager pm = this.getPackageManager();
        ComponentName component = new ComponentName(this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    @TargetApi(23)
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        image.getActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        image.getRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onRecyclerImageSet(Bitmap imageToShow, String imageBase64String) {
        imageViewLinearLayout.setVisibility(View.VISIBLE);
        firstImage.setImageBitmap(imageToShow);
        this.firstImagePath = imageBase64String;
    }
}