package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.commonClasses.RemoveLastComma;
import com.studentcares.spps.connectivity.Get_StdDivSub_Sqlite;
import com.studentcares.spps.connectivity.Send_SMS_Req;
import com.studentcares.spps.sessionManager.SessionManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SMS_Send extends BaseActivity implements View.OnClickListener {

    View dialogView;
    EditText txtMsgBody;
    String msgBody;
    String schoolId;
    Button btnSend;
    private ProgressDialog progressDialog;

    String groupIds_ForSMS;
    String userType_storeMsg ="Student";
    String userId;
    
    Button btnFilterOk;
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

    RadioButton rdGroupWise;
    RadioButton rdStdDivWise;
//    RadioButton rbAll;
    RadioButton rbStaff;
    RadioButton rbStudent;
    RadioButton rdStudentAll;
    RadioButton rdStudentParticular;
    RadioButton rdStaffAll;
    RadioButton rdStaffParticular;

    CheckBox forPrePrimary;
    CheckBox forPrimary;
    CheckBox forSecondary;
    CheckBox forJrCollege;

    RelativeLayout groupFilterLayout;
    RelativeLayout classWiseFilter;
    RelativeLayout stdDivLayout;
    RelativeLayout stdDivWiseFilterLayout;
    RelativeLayout studentMainFilterLayout;
    RelativeLayout staffMainFilterLayout;

    SessionManager sessionManager;
    private List<String> groupIdList = new ArrayList<String>();

    String listOf= "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_send);

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

        sessionManager = new SessionManager(this);
        HashMap<String, String> typeOfUser = sessionManager.getUserDetails();
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        userId = typeOfUser.get(SessionManager.KEY_USERID);

        txtMsgBody = (EditText) findViewById(R.id.txtMsg);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnSend) {

            msgBody = txtMsgBody.getText().toString();

            if (msgBody.equals("") || msgBody == null) {
                Toast.makeText(this, "Please Enter Your Msg.", Toast.LENGTH_SHORT).show();
            } else {
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                dialogView = inflater.inflate(R.layout.sms_send_filter, null);
                getLayoutDetails();
                alert.setView(dialogView);
                alert.show();
            }
        }
        else if (v.getId() == R.id.btnFilterOk) {

            if (rdGroupWise.isChecked() == true) {

                if (forPrePrimary.isChecked() == true || forPrimary.isChecked() == true || forSecondary.isChecked() == true || forJrCollege.isChecked() == true) {

                    RemoveLastComma removeLastComma = new RemoveLastComma(this);
                    groupIds_ForSMS = removeLastComma.CommaRemove(groupIdList);

                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Please Wait,Message is Sending.");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);

                    Send_SMS_Req PRSendSmsFromServer = new Send_SMS_Req(this);
                    PRSendSmsFromServer.SendSmsFromServeGroupWiser(schoolId, groupIds_ForSMS, msgBody,userType_storeMsg, userId,progressDialog);
                }
                else {
                    Toast.makeText(this, "Please Select Group.", Toast.LENGTH_SHORT).show();
                }
            }
            else if (rdStudentParticular.isChecked() == true) {
                if (standardName == null || standardName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select Standard.", Toast.LENGTH_LONG).show();
                }
                else if (divisionName == null || divisionName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select Division.", Toast.LENGTH_LONG).show();
                }
                else {
                    listOf= "Student";
                    Intent gotoStudentList = new Intent(this, SMS_Send_User_List.class);
                    gotoStudentList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    gotoStudentList.putExtra("standardId", standardId);
                    gotoStudentList.putExtra("divisionId", divisionId);
                    gotoStudentList.putExtra("listOf", listOf);
                    gotoStudentList.putExtra("msg", msgBody);
                    startActivity(gotoStudentList);
                }
            }
            else if (rdStudentAll.isChecked() == true) {
                if (standardName == null || standardName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select Standard.", Toast.LENGTH_LONG).show();
                }
                else if (divisionName == null || divisionName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select Division.", Toast.LENGTH_LONG).show();
                }
                else {
                    //send to all student
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Please Wait,Message Is Sending.");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);

                    Send_SMS_Req PRSendSmsFromServer = new Send_SMS_Req(this);
                    PRSendSmsFromServer.SendSmsAllStudent(schoolId,standardId,divisionId, userType_storeMsg,msgBody, userId,progressDialog);
                    Toast.makeText(this, "Msg Sent to all student of selected class", Toast.LENGTH_SHORT).show();
                }
            }
            else if (rdStaffAll.isChecked() == true){
                userType_storeMsg = "Staff";
                //send to all staff
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait,Message Is Sending.");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                Send_SMS_Req PRSendSmsFromServer = new Send_SMS_Req(this);
                PRSendSmsFromServer.SendSmsAllStaff(schoolId,userType_storeMsg, msgBody, userId,progressDialog);
                Toast.makeText(this, "Msg sent to all staff.", Toast.LENGTH_SHORT).show();
            }
            else if (rdStaffParticular.isChecked() == true){
                listOf= "Staff";
                Intent gotoStudentList = new Intent(this, SMS_Send_User_List.class);
                gotoStudentList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                gotoStudentList.putExtra("standardId", standardId);
                gotoStudentList.putExtra("divisionId", divisionId);
                gotoStudentList.putExtra("listOf", listOf);
                gotoStudentList.putExtra("msg", msgBody);
                startActivity(gotoStudentList);
            }
//            else if (rbAll.isChecked() == true){
//                String smsType ="All";
//                Send_SMS_Req PRSendSmsFromServer = new Send_SMS_Req(this);
//                PRSendSmsFromServer.SendMsgToAll(schoolId,msgBody,smsType,userId,progressDialog);
//            }
            else {
                Toast.makeText(getApplicationContext(), "You were nothing selected.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getLayoutDetails() {
//      rbAll = (RadioButton)dialogView.findViewById(R.id.rbAll);
        rbStaff = (RadioButton)dialogView.findViewById(R.id.rbStaff);
        rbStudent = (RadioButton)dialogView.findViewById(R.id.rbStudent);
        rdGroupWise = (RadioButton) dialogView.findViewById(R.id.rdGroupWise);
        rdStdDivWise = (RadioButton) dialogView.findViewById(R.id.rdStdDivWise);
        rdStudentAll = (RadioButton) dialogView.findViewById(R.id.rdStudentAll);
        rdStaffAll = (RadioButton) dialogView.findViewById(R.id.rdStaffAll);
        rdStaffParticular = (RadioButton) dialogView.findViewById(R.id.rdStaffParticular);
        rdStudentParticular = (RadioButton) dialogView.findViewById(R.id.rdStudentParticular);
        standard = (Spinner) dialogView.findViewById(R.id.standard);
        division = (Spinner) dialogView.findViewById(R.id.division);

        forPrePrimary = (CheckBox) dialogView.findViewById(R.id.forPrePrimary);
        forPrimary = (CheckBox) dialogView.findViewById(R.id.forPrimary);
        forSecondary = (CheckBox) dialogView.findViewById(R.id.forSecondary);
        forJrCollege = (CheckBox) dialogView.findViewById(R.id.forJrCollege);

        groupFilterLayout = (RelativeLayout) dialogView.findViewById(R.id.groupFilterLayout);
        classWiseFilter = (RelativeLayout) dialogView.findViewById(R.id.classWiseFilter);
        stdDivLayout = (RelativeLayout) dialogView.findViewById(R.id.stdDivLayout);
        stdDivWiseFilterLayout = (RelativeLayout) dialogView.findViewById(R.id.stdDivWiseFilterLayout);
        studentMainFilterLayout = (RelativeLayout) dialogView.findViewById(R.id.studentMainFilterLayout);
        staffMainFilterLayout = (RelativeLayout) dialogView.findViewById(R.id.staffMainFilterLayout);

        btnFilterOk = (Button) dialogView.findViewById(R.id.btnFilterOk);
        btnFilterOk.setOnClickListener(this);

        classWiseFilter.setVisibility(View.GONE);
        stdDivLayout.setVisibility(View.GONE);
        stdDivWiseFilterLayout.setVisibility(View.GONE);
        groupFilterLayout.setVisibility(View.GONE);
        studentMainFilterLayout.setVisibility(View.GONE);
        staffMainFilterLayout.setVisibility(View.GONE);

//        rbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (rbAll.isChecked()) {
//                    rbStudent.setChecked(false);
//                    rbStaff.setChecked(false);
//                    rdStdDivWise.setChecked(false);
//                    rdGroupWise.setChecked(false);
//                    rdGroupWise.setChecked(false);
//                    forPrePrimary.setChecked(false);
//                    forPrimary.setChecked(false);
//                    forSecondary.setChecked(false);
//                    forJrCollege.setChecked(false);
//                    groupFilterLayout.setVisibility(View.GONE);
//                    stdDivLayout.setVisibility(View.GONE);
//                    studentMainFilterLayout.setVisibility(View.GONE);
//                }
//            }
//        });

        rbStaff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbStaff.isChecked()) {
//                    rbAll.setChecked(false);
                    rbStudent.setChecked(false);
                    rdStdDivWise.setChecked(false);
                    rdGroupWise.setChecked(false);
                    rdGroupWise.setChecked(false);
                    forPrePrimary.setChecked(false);
                    forPrimary.setChecked(false);
                    forSecondary.setChecked(false);
                    forJrCollege.setChecked(false);
                    groupFilterLayout.setVisibility(View.GONE);
                    stdDivLayout.setVisibility(View.GONE);
                    classWiseFilter.setVisibility(View.GONE);
                    studentMainFilterLayout.setVisibility(View.GONE);
                    staffMainFilterLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        rdStaffAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rdStaffAll.isChecked()) {
                    rdStaffParticular.setChecked(false);
                }
            }
        });

        rdStaffParticular.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rdStaffParticular.isChecked()) {
                    rdStaffAll.setChecked(false);
                }
            }
        });

        rbStudent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbStudent.isChecked()) {
                    rbStaff.setChecked(false);
//                    rbAll.setChecked(false);
                    studentMainFilterLayout.setVisibility(View.VISIBLE);
                    staffMainFilterLayout.setVisibility(View.GONE);
                }
            }
        });
// student std div (Class wise) wise
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
                    stdDivWiseFilterLayout.setVisibility(View.GONE);
                    classWiseFilter.setVisibility(View.VISIBLE);
                    stdDivLayout.setVisibility(View.VISIBLE);
                    getStandarddDetails();
                    getDivisionDetails();
                }
            }
        });

        rdStudentAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rdStudentAll.isChecked()) {
                    rdStudentParticular.setChecked(false);
                }
            }
        });

        rdStudentParticular.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rdStudentParticular.isChecked()) {
                    rdStudentAll.setChecked(false);
                }
            }
        });


// student Group wise
        rdGroupWise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rdGroupWise.isChecked()) {
                    rdStdDivWise.setChecked(false);
                    groupFilterLayout.setVisibility(View.VISIBLE);
                    classWiseFilter.setVisibility(View.GONE);
                }
            }
        });


        forPrePrimary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (forPrePrimary.isChecked()) {
                    rdStdDivWise.setChecked(false);
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
                    rdStdDivWise.setChecked(false);
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
                    rdStdDivWise.setChecked(false);
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
                    rdStdDivWise.setChecked(false);
                    groupIdList.add("4");
                } else {
                    groupIdList.remove("4");
                }
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
                    stdDivWiseFilterLayout.setVisibility(View.VISIBLE);
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
}