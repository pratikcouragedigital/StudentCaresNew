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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.studentcares.spps.connectivity.Ranker_Req;
import com.studentcares.spps.imageModule.Image;
import com.studentcares.spps.internetConnectivity.NetworkChangeReceiver;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Ranker_Add extends BaseActivity implements View.OnClickListener , AdapterView.OnItemSelectedListener{

    EditText txtObtMarks,txtOutOfMarks,txtPercentage,txtGrade;
    Button btnAddRanker;

    String obtMarks = "";
    String outOfMarks = "";
    String staffId = "";
    String schoolId = "";
    String grade = "";
    String percentage = "";
    String rank = "";

    Spinner standard,division,studentSpinner,spinnerRank;

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

    String studentName;
    String studentId;
    private String[] studentArrayList;
    //private DivisionSpinnerAdapter studentSpinnerSpinnerAdapter;
    private ProgressDialog studentDialogBox;
    private List<String> studentIdList = new ArrayList<String>();
    private List<String> studentNameList = new ArrayList<String>();

    String[]rankArray = {"1", "2","3", "4","5"};
    private ProgressDialog progressDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranker_add);

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

        txtOutOfMarks = (EditText) findViewById(R.id.txtOutOfMarks);
        txtObtMarks = (EditText) findViewById(R.id.txtObtMarks);
        txtPercentage = (EditText) findViewById(R.id.txtPercentage);
        txtGrade = (EditText) findViewById(R.id.txtGrade);
        btnAddRanker = (Button) findViewById(R.id.btnAddRanker);

        standard = (Spinner) findViewById(R.id.spinnerForStandard);
        division = (Spinner) findViewById(R.id.spinnerForDivision);
        studentSpinner = (Spinner) findViewById(R.id.spinnerForStudent);
        spinnerRank = (Spinner) findViewById(R.id.spinnerRank);

        btnAddRanker.setOnClickListener(this);
        spinnerRank.setOnItemSelectedListener(this);

        getStandardDetails();
        getDivisionDetails();
        getStudentDetails();

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.
                R.layout.simple_spinner_dropdown_item ,rankArray);
        spinnerRank.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        rank = rankArray[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getStandardDetails() {
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

    private void getStudentDetails() {
        studentArrayList = new String[]{
                "Select Student"
        };
        studentNameList = new ArrayList<>(Arrays.asList(studentArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, studentNameList);
        //getListOfSubject();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        studentSpinner.setAdapter(spinnerAdapter);
        studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    studentName = parent.getItemAtPosition(position).toString();
                    studentId = studentIdList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getListOfSubject() {
        Ranker_Req rankerReq = new Ranker_Req(this);
        rankerReq.FetchStudent(studentNameList, studentIdList, schoolId, spinnerAdapter, standardId, divisionId);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnAddRanker) {
            obtMarks = txtObtMarks.getText().toString();
            outOfMarks = txtOutOfMarks.getText().toString();
            percentage = txtPercentage.getText().toString();
            grade = txtGrade.getText().toString();

            if (standardName == null || standardName.isEmpty()) {
                Toast.makeText(this, "Please select Standard.", Toast.LENGTH_LONG).show();
            }
            else if (divisionName == null || divisionName.isEmpty()) {
                Toast.makeText(this, "Please select Division.", Toast.LENGTH_LONG).show();
            }
            else if (studentName == null || studentName.isEmpty()) {
                Toast.makeText(this, "Please select Student.", Toast.LENGTH_LONG).show();
            }
            else if (rank == null || rank.isEmpty()) {
                Toast.makeText(this, "Please select Rank.", Toast.LENGTH_LONG).show();
            }
            else {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait.Ranker Is Adding.");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                try {
                    Ranker_Req rankerReq = new Ranker_Req(this);
                    rankerReq.AddRanker(staffId, schoolId, standardId, divisionId, studentId,rank,obtMarks, outOfMarks,percentage,grade, progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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

}