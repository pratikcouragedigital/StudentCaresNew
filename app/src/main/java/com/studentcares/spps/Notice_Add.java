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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.connectivity.Get_StdDivSub_Sqlite;
import com.studentcares.spps.connectivity.Notice_Request_Data;
import com.studentcares.spps.imageModule.Image;
import com.studentcares.spps.internetConnectivity.NetworkChangeReceiver;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.sqlLite.DataBaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.view.View.VISIBLE;

public class Notice_Add extends BaseActivity implements View.OnClickListener, Image.OnRecyclerSetImageListener {

    ImageView firstImage;
    String firstImagePath = "";
    LinearLayout imageViewLinearLayout;
    RelativeLayout noticeDetailsLayout;

    EditText txtHeading;
    EditText txtDescription;
    EditText submitionDate;
    Button btnSubmit;
    Button btnAddImage;
    Button btnFilterOk;

    private StringBuilder date;
    String selectedDateForSubmission = "";
    String description = "";
    String heading = "";
    String userId = "";
    String schoolId = "";
    String noticeFor = "";
    String noticeForStudentGroup = "";

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    private ProgressDialog progressDialog = null;

    private DataBaseHelper mydb;

    Spinner standard;
    Spinner division;

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

    RadioButton rbForAll;
    RadioButton chkForStudent;
    RadioButton chkStaff;
    RadioButton rdGroupWise;
    RadioButton rdStdDivWise;

    RelativeLayout studentMainFilterLayout;
    RelativeLayout studentFilterLayout;
    RelativeLayout groupFilterLayout;
    RelativeLayout stdDivWiseFilterLayout;

    CheckBox forPrePrimary;
    CheckBox forPrimary;
    CheckBox forSecondary;
    CheckBox forJrCollege;

    private List<String> groupIdList = new ArrayList<String>();

    Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_add);

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

        studentFilterLayout = (RelativeLayout) findViewById(R.id.studentFilterLayout);
        forPrePrimary = (CheckBox) findViewById(R.id.forPrePrimary);
        forPrimary = (CheckBox) findViewById(R.id.forPrimary);
        forSecondary = (CheckBox) findViewById(R.id.forSecondary);
        forJrCollege = (CheckBox) findViewById(R.id.forJrCollege);

        mydb = new DataBaseHelper(this);

        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtHeading = (EditText) findViewById(R.id.txtHeading);
        submitionDate = (EditText) findViewById(R.id.txtSubmissionDate);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnAddImage = (Button) findViewById(R.id.btnAddImage);
        btnFilterOk = (Button) findViewById(R.id.btnFilterOk);
        firstImage = (ImageView) findViewById(R.id.firstNoticeImage);

        rbForAll = (RadioButton) findViewById(R.id.rbForAll);
        chkForStudent = (RadioButton) findViewById(R.id.chkForStudent);
        chkStaff = (RadioButton) findViewById(R.id.chkStaff);

        rdGroupWise = (RadioButton) findViewById(R.id.rdGroupWise);
        rdStdDivWise = (RadioButton) findViewById(R.id.rdStdDivWise);
        standard = (Spinner) findViewById(R.id.standard);
        division = (Spinner) findViewById(R.id.division);

        groupFilterLayout = (RelativeLayout) findViewById(R.id.groupFilterLayout);
        stdDivWiseFilterLayout = (RelativeLayout) findViewById(R.id.stdDivWiseFilterLayout);
        studentMainFilterLayout = (RelativeLayout) findViewById(R.id.studentMainFilterLayout);

        imageViewLinearLayout = (LinearLayout) findViewById(R.id.imageViewLinearLayout);
        noticeDetailsLayout = (RelativeLayout) findViewById(R.id.noticeDetailsLayout);

        imageViewLinearLayout.setVisibility(View.GONE);
        noticeDetailsLayout.setVisibility(View.GONE);

        stdDivWiseFilterLayout.setVisibility(View.GONE);
        groupFilterLayout.setVisibility(View.GONE);
        studentMainFilterLayout.setVisibility(View.GONE);

        studentFilterLayout.setVisibility(View.GONE);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        btnSubmit.setOnClickListener(this);
        btnAddImage.setOnClickListener(this);
        btnFilterOk.setOnClickListener(this);


        getStandarddDetails();
        getDivisionDetails();

        rbForAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbForAll.isChecked()) {
                    noticeFor = "All";
                    standardId = "";
                    divisionId = "";
                    noticeForStudentGroup = "";
                    chkForStudent.setChecked(false);
                    chkStaff.setChecked(false);
                    rdStdDivWise.setChecked(false);
                    rdGroupWise.setChecked(false);
                    forPrePrimary.setChecked(false);
                    forPrimary.setChecked(false);
                    forSecondary.setChecked(false);
                    forJrCollege.setChecked(false);
                    studentMainFilterLayout.setVisibility(View.GONE);
                    studentFilterLayout.setVisibility(View.GONE);
                    stdDivWiseFilterLayout.setVisibility(View.GONE);
                    groupFilterLayout.setVisibility(View.GONE);

                }
            }
        });

        chkForStudent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chkForStudent.isChecked()) {
                    noticeFor = "Student";
                    studentMainFilterLayout.setVisibility(VISIBLE);
                    studentFilterLayout.setVisibility(View.VISIBLE);
                    rbForAll.setChecked(false);
                    chkStaff.setChecked(false);

                } else {
                    studentMainFilterLayout.setVisibility(View.GONE);
                    studentFilterLayout.setVisibility(View.GONE);
                    forPrePrimary.setChecked(false);
                    forPrimary.setChecked(false);
                    forSecondary.setChecked(false);
                    forJrCollege.setChecked(false);
                }
            }
        });
        chkStaff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chkStaff.isChecked()) {
                    noticeFor = "Staff";
                    noticeForStudentGroup = "";
                    standardId = "";
                    divisionId = "";
                    rbForAll.setChecked(false);
                    chkForStudent.setChecked(false);
                    rdStdDivWise.setChecked(false);
                    rdGroupWise.setChecked(false);
                    forPrePrimary.setChecked(false);
                    forPrimary.setChecked(false);
                    forSecondary.setChecked(false);
                    forJrCollege.setChecked(false);
                    studentMainFilterLayout.setVisibility(View.GONE);
                    studentFilterLayout.setVisibility(View.GONE);
                    stdDivWiseFilterLayout.setVisibility(View.GONE);
                    groupFilterLayout.setVisibility(View.GONE);

                }
            }
        });

        rdStdDivWise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rdStdDivWise.isChecked()) {
                    rdGroupWise.setChecked(false);
                    forPrePrimary.setChecked(false);
                    forPrimary.setChecked(false);
                    forSecondary.setChecked(false);
                    forJrCollege.setChecked(false);
                    groupFilterLayout.setVisibility(View.GONE);
                    stdDivWiseFilterLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        rdGroupWise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rdGroupWise.isChecked()) {
                    rdStdDivWise.setChecked(false);
                    groupFilterLayout.setVisibility(View.VISIBLE);
                    stdDivWiseFilterLayout.setVisibility(View.GONE);
                }
            }
        });

        forPrePrimary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (forPrePrimary.isChecked()) {
                    noticeFor = "Student";
                    //noticeForStudentGroup = noticeForStudentGroup + " 1";
                    rbForAll.setChecked(false);
                    chkStaff.setChecked(false);
                    groupIdList.add("1");
                } else {
                    groupIdList.remove("1");
                }
            }
        });

        forPrimary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (forPrimary.isChecked()) {
                    noticeFor = "Student";
                    //noticeForStudentGroup = noticeForStudentGroup + " 2";
                    rbForAll.setChecked(false);
                    chkStaff.setChecked(false);
                    groupIdList.add("2");
                } else {
                    groupIdList.remove("2");
                }
            }
        });
        forSecondary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (forSecondary.isChecked()) {
                    noticeFor = "Student";
                    //noticeForStudentGroup = noticeForStudentGroup +" 3";
                    rbForAll.setChecked(false);
                    chkStaff.setChecked(false);
                    groupIdList.add("3");
                } else {
                    groupIdList.remove("3");
                }
            }
        });

        forJrCollege.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (forJrCollege.isChecked()) {
                    noticeFor = "Student";
                    //noticeForStudentGroup = noticeForStudentGroup + " 4";
                    rbForAll.setChecked(false);
                    chkStaff.setChecked(false);
                    groupIdList.add("4");
                } else {
                    groupIdList.remove("4");
                }
            }
        });

        submitionDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                setDate(view);
                return false;
            }
        });
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
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        division.setAdapter(spinnerAdapter);
        division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    divisionName = parent.getItemAtPosition(position).toString();
                    divisionId = divisionIdList.get(position);
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

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnSubmit) {
            description = txtDescription.getText().toString();
            heading = txtHeading.getText().toString();

            if (selectedDateForSubmission == "") {
                Toast.makeText(this, "Please Select Date.", Toast.LENGTH_SHORT).show();
            } else if (description.equals("")) {
                Toast.makeText(this, "Please Enter Description.", Toast.LENGTH_SHORT).show();
            } else if (heading.equals("")) {
                Toast.makeText(this, "Please Enter Heading.", Toast.LENGTH_SHORT).show();
            } else {

                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait.Notice Is Adding.");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                try {
                    Notice_Request_Data UAAddNotice = new Notice_Request_Data(this);
                    UAAddNotice.AddSchoolNotice(userId, schoolId, heading, description, selectedDateForSubmission, firstImagePath, noticeFor, noticeForStudentGroup, standardId, divisionId, progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if (v.getId() == R.id.btnAddImage) {
            image = new Image(this, "Notice", this);
            image.getImage();
        }
        else if (v.getId() == R.id.btnFilterOk) {

            if (rdGroupWise.isChecked() == true) {

                if (forPrePrimary.isChecked() == true || forPrimary.isChecked() == true || forSecondary.isChecked() == true || forJrCollege.isChecked() == true) {
                    noticeDetailsLayout.setVisibility(VISIBLE);
                    StringBuilder groups = new StringBuilder();
                    for (String value : groupIdList) {
                        groups.append(value + ",");
                    }
                    noticeForStudentGroup = groups.toString();
                    standardId = "";
                    divisionId = "";

                } else {
                    Toast.makeText(this, "Please Select Group.", Toast.LENGTH_SHORT).show();
                }
            }

            else if (rdStdDivWise.isChecked() == true) {
                if (standardName == null || standardName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select Standard.", Toast.LENGTH_LONG).show();
                } else if (divisionName == null || divisionName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select Division.", Toast.LENGTH_LONG).show();
                } else {
                    noticeForStudentGroup = "";
                    noticeDetailsLayout.setVisibility(VISIBLE);
                }
            }
            else if (chkStaff.isChecked() == true) {
                noticeFor = "Staff";
                noticeDetailsLayout.setVisibility(VISIBLE);
            }
            else if (rbForAll.isChecked() == true) {
                noticeFor = "All";
                noticeDetailsLayout.setVisibility(VISIBLE);
            }
            else {
                Toast.makeText(getApplicationContext(), "You were nothing selected.", Toast.LENGTH_LONG).show();
            }
        }
    }

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
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
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