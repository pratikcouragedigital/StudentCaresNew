package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.connectivity.Get_Std_Div_Teaching_Staff;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Assign_Std_Div_Teaching_Staff extends AppCompatActivity implements View.OnClickListener {

    private RadioButton subjectTeacherRadioButton;
    private RadioButton classTeacherRadioButton;
    private Spinner standardSpinner;
    private Spinner divisionSpinner;
    private RelativeLayout radioFilterLayout;
    private RelativeLayout spinnerFilterLayout;
    private Button submitButton;
    private String[] standardArrayList;
    private ArrayList<String> standardNameList = new ArrayList<String>();
    private Std_Div_Filter_Adapter spinnerAdapter;
    private String standardName;
    private List<String> standardIdList = new ArrayList<String>();
    private String standardId;
    private ProgressDialog standardDialogBox;
    private String[] divisionArrayList;
    private ArrayList<String> divisionNameList = new ArrayList<String>();
    private String divisionName;
    private String divisionId;
    private List<String> divisionIdList = new ArrayList<String>();
    private ProgressDialog divisionDialogBox;
    private String schoolId;
    private ProgressDialog assignStandardDivisionDialogBox;
    private String staffId;
    private boolean isClassTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assign_std_div_teaching_staff);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        schoolId = userDetails.get(SessionManager.KEY_SCHOOLID);
        staffId = userDetails.get(SessionManager.KEY_USERID);

        subjectTeacherRadioButton = (RadioButton) findViewById(R.id.subjectTeacherRadioButton);
        classTeacherRadioButton = (RadioButton) findViewById(R.id.classTeacherRadioButton);

        standardSpinner = (Spinner) findViewById(R.id.standardSpinner);
        divisionSpinner = (Spinner) findViewById(R.id.divisionSpinner);

        radioFilterLayout = (RelativeLayout) findViewById(R.id.radioFilterLayout);
        spinnerFilterLayout = (RelativeLayout) findViewById(R.id.spinnerFilterLayout);

        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

        spinnerFilterLayout.setVisibility(View.GONE);

        getStandardDetails();
        getDivisionDetails();


        subjectTeacherRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (subjectTeacherRadioButton.isChecked()) {
                    classTeacherRadioButton.setChecked(false);
                    spinnerFilterLayout.setVisibility(View.GONE);
                    isClassTeacher = true;
                }
            }
        });

        classTeacherRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (classTeacherRadioButton.isChecked()) {
                    subjectTeacherRadioButton.setChecked(false);
                    spinnerFilterLayout.setVisibility(View.VISIBLE);
                    isClassTeacher = false;
                }
            }
        });


    }

    private void getStandardDetails() {
        standardArrayList = new String[]{
                "Standard"
        };
        standardNameList = new ArrayList<>(Arrays.asList(standardArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, standardNameList);
        getListOfStandard();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        standardSpinner.setAdapter(spinnerAdapter);
        standardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        standardDialogBox = ProgressDialog.show(this, "", "Fetching Standard & Division, Please Wait...", true);
        Get_Std_Div_Teaching_Staff standardSpinnerList = new Get_Std_Div_Teaching_Staff(this);
        standardSpinnerList.FetchAllstandard(standardNameList, standardIdList, schoolId,spinnerAdapter, standardDialogBox);
    }

    private void getDivisionDetails() {
        divisionArrayList = new String[]{
                "Division"
        };
        divisionNameList = new ArrayList<>(Arrays.asList(divisionArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, divisionNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisionSpinner.setAdapter(spinnerAdapter);
        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        Get_Std_Div_Teaching_Staff divisionSpinnerList = new Get_Std_Div_Teaching_Staff(this);
        divisionSpinnerList.FetchAllDivision(divisionNameList, divisionIdList,schoolId, spinnerAdapter, standardId,divisionDialogBox);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.submitButton) {
            if(classTeacherRadioButton.isChecked()){
                if (standardName == null || standardName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select Standard.", Toast.LENGTH_LONG).show();
                } else if (divisionName == null || divisionName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select Division.", Toast.LENGTH_LONG).show();
                }
                else{
                    assignStandardDivisionDialogBox = ProgressDialog.show(this, "", "Updating Please Wait...", true);
                    //Toast.makeText(this, ""+standardId+" "+ divisionId, Toast.LENGTH_SHORT).show();
                    Get_Std_Div_Teaching_Staff TAssign_standard_division_details = new Get_Std_Div_Teaching_Staff(this);
                    TAssign_standard_division_details.AssignStandardDivision(standardId, divisionId, staffId, schoolId, assignStandardDivisionDialogBox);
                }
            }
            else if(subjectTeacherRadioButton.isChecked()) {
                Intent gotoHome =  new Intent(this, Home_Menu.class);
                gotoHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gotoHome);
            }
            else {
                Toast.makeText(getApplicationContext(), "You were nothing selected.", Toast.LENGTH_LONG).show();
            }
        }
    }
}