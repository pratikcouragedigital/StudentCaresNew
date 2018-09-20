package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.connectivity.Get_StdDivSub_Sqlite;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.singleton.StandardDivisionInstance;
import com.studentcares.spps.sqlLite.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TimeTable_Student extends BaseActivity implements View.OnClickListener{

    Home_Menu activity;

    SessionManager sessionManager;

    private DataBaseHelper mydb;
    private String schoolId,userType,userId,standardId,divisionId;

    LinearLayout layout;
    TableLayout table;
    TextView cell;
    FloatingActionButton filterFab;

    Spinner standard;
    Spinner division;
    String standardName;

    private String[] standardArrayList;
    private Std_Div_Filter_Adapter spinnerAdapter;
    private ProgressDialog standardDialogBox;
    private List<String> standardIdList = new ArrayList<String>();
    private List<String> standardNameList = new ArrayList<String>();

    String divisionName;

    private String[] divisionArrayList;
    private ProgressDialog divisionDialogBox;
    private List<String> divisionIdList = new ArrayList<String>();
    private List<String> divisionNameList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_table);

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
        userType = typeOfUser.get(SessionManager.KEY_USERTYPE);
        userId = typeOfUser.get(SessionManager.KEY_USERID);
        standardId = typeOfUser.get(SessionManager.KEY_STANDARD);
        divisionId = typeOfUser.get(SessionManager.KEY_DIVISION);
        mydb = new DataBaseHelper(this);

        layout = (LinearLayout) findViewById(R.id.linearLayout1);
        table = (TableLayout) findViewById(R.id.displayLinear);
        filterFab = (FloatingActionButton) findViewById(R.id.filterFab);
        filterFab.setOnClickListener(this);

        GetTimeTable();

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.filterFab){
            filterAlterBox();
        }
    }

    private void filterAlterBox() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        int margin = (int) getResources().getDimension(R.dimen.margin);
        RelativeLayout layout = new RelativeLayout(this);
        layout.setLayoutParams(new RelativeLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT));

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

        TextView txtView = new TextView(this);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, division.getId());
        params.setMargins(margin, margin, margin, margin);
        txtView.setLayoutParams(params);

        layout.addView(txtView);
        layout.addView(standard);
        layout.addView(division);

        alert.setTitle(Html.fromHtml("<b>" + "Select Standard & Division." + "</b>"));

        getStandarddDetails();
        getDivisionDetails();
        alert.setView(layout);

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (standardName == null || standardName.isEmpty()) {
                    Toast.makeText(TimeTable_Student.this, "Please select Standard.", Toast.LENGTH_LONG).show();
                } else if (divisionName == null || divisionName.isEmpty()) {
                    Toast.makeText(TimeTable_Student.this, "Please select Division.", Toast.LENGTH_LONG).show();
                } else {
                    GetTimeTable();
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
        //getListOfDivision();
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

    private void GetTimeTable() {
        table.removeAllViews();
       String method ="Timetable_ClassWise";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Standard_Id", standardId);
            jsonObject.put("Division_Id", divisionId);
            jsonObject.put("School_id", schoolId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = getString(R.string.url) + method;

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(method)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("responseDetails");
                            for(int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String Time = jsonObject.getString("Time");

                                TableRow row = new TableRow(TimeTable_Student.this);

                                cell = new android.support.v7.widget.AppCompatTextView(TimeTable_Student.this);
                                cell.setText(Html.fromHtml(Time + "<br>"));

                                cell.setPadding(16, 16, 16, 16);
                                cell.setTextSize(20);
                                cell.setTypeface(null, Typeface.BOLD);
                                cell.setTextColor(getResources().getColor(R.color.attAbsentMark));
                                cell.setGravity(Gravity.CENTER_HORIZONTAL);
                                cell.setBackgroundResource(R.drawable.border);
                                row.addView(cell);

                                JSONArray dayOrSubArray = jsonObject.getJSONArray("DaySubjectList");
                                if(dayOrSubArray.length() == 0){

                                    TableRow row2 = new TableRow(TimeTable_Student.this);
                                    row2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1));
                                    cell = new android.support.v7.widget.AppCompatTextView(TimeTable_Student.this);
                                    cell.setText(Html.fromHtml("Recess<br>"));
                                    cell.setTextSize(20);
                                    cell.setTypeface(null, Typeface.BOLD);
                                    cell.setTextColor(getResources().getColor(R.color.attAbsentMark));
                                    cell.setGravity(Gravity.CENTER_HORIZONTAL);
                                    row2.addView(cell);

                                    row.setBackgroundResource(R.drawable.border);
                                    row.addView(row2);
                                }
                                else{
                                    for(int j = 0; j < dayOrSubArray.length(); j++) {
                                        JSONObject jsonObjectDaySub = dayOrSubArray.getJSONObject(j);
                                        String dayOrSub = jsonObjectDaySub.getString("DaySub");
                                        String Subject = jsonObjectDaySub.getString("Subject");
                                        String Staff = jsonObjectDaySub.getString("Staff");

                                        cell = new android.support.v7.widget.AppCompatTextView(TimeTable_Student.this);

                                        if(i == 0 ){
                                            cell.setText(Html.fromHtml(dayOrSub + "<br>"));
                                            cell.setTypeface(null, Typeface.BOLD);
                                            cell.setTextColor(getResources().getColor(R.color.attAbsentMark));
                                        }
                                        else{
                                            if(Subject.equals("")){
                                                Subject = "Off";
                                                cell.setText(Html.fromHtml(Subject + "<br>"));
                                            }
                                            else{
                                                cell.setText(Html.fromHtml(Subject +"<br>("+ Staff + ")"));
                                            }
                                        }
                                        cell.setGravity(Gravity.CENTER_HORIZONTAL);
                                        cell.setTextSize(20);
                                        cell.setPadding(16, 16, 16, 16);
                                        row.addView(cell);
                                        cell.setBackgroundResource(R.drawable.border);

                                    }
                                }

                                table.addView(row);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(activity, "Error: "+ error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
