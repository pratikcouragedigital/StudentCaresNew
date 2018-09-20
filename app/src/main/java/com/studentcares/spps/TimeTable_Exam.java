package com.studentcares.spps;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
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
import com.studentcares.spps.expandableText.ExpandableText;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.sqlLite.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TimeTable_Exam extends BaseActivity implements View.OnClickListener {

    private SessionManager sessionManager;
    private String schoolId;
    private String standardId;
    private DataBaseHelper mydb;
    private LinearLayout layout;
    private TableLayout table;
    private FloatingActionButton filterFab;
    private String userType;
    private Spinner standard;
    private String standardName;
    private String[] standardArrayList;
    private List<String> standardIdList = new ArrayList<String>();
    private List<String> standardNameList = new ArrayList<String>();
    private Std_Div_Filter_Adapter spinnerAdapter;
    private TextView cell;
    Home_Menu activity;
    private TextView examType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_table_exam);

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
        standardId = typeOfUser.get(SessionManager.KEY_STANDARD);
        mydb = new DataBaseHelper(this);

        examType = (TextView) findViewById(R.id.examType);
        layout = (LinearLayout) findViewById(R.id.linearLayout1);
        table = (TableLayout) findViewById(R.id.displayLinear);
        filterFab = (FloatingActionButton) findViewById(R.id.filterFab);
        filterFab.setOnClickListener(this);

        if(userType.equals("Student")) {
            filterFab.setVisibility(View.GONE);
        }
        else {
            filterFab.setVisibility(View.VISIBLE);
        }

        GetExamTimeTable();
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

        TextView txtView = new TextView(this);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, standard.getId());
        params.setMargins(margin, margin, margin, margin);
        txtView.setLayoutParams(params);

        layout.addView(txtView);
        layout.addView(standard);

        alert.setTitle(Html.fromHtml("<b>" + "Select Standard." + "</b>"));

        getStandarddDetails();
        alert.setView(layout);

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (standardName == null || standardName.isEmpty()) {
                    Toast.makeText(TimeTable_Exam.this, "Please select Standard.", Toast.LENGTH_LONG).show();
                } else {
                    GetExamTimeTable();
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

    private void GetExamTimeTable() {
        table.removeAllViews();
        String method ="Timetable_Exam";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Standard_Id", standardId);
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
                                String Date = jsonObject.getString("Date");
                                String Days = jsonObject.getString("Days");
                                String Start_Time = jsonObject.getString("Start_Time");
                                String End_Time = jsonObject.getString("End_Time");
                                String Subject = jsonObject.getString("Subject");

                                TableRow row = new TableRow(TimeTable_Exam.this);

                                cell = new android.support.v7.widget.AppCompatTextView(TimeTable_Exam.this);
                                cell.setText(Date);
                                if(i == 0) {
                                    cell.setTypeface(null, Typeface.BOLD);
                                    cell.setTextColor(getResources().getColor(R.color.attAbsentMark));
                                }
                                cell.setGravity(Gravity.CENTER_HORIZONTAL);
                                cell.setPadding(16, 16, 16, 16);
                                cell.setTextSize(20);
                                cell.setBackgroundResource(R.drawable.border);
                                row.addView(cell);

                                cell = new android.support.v7.widget.AppCompatTextView(TimeTable_Exam.this);
                                cell.setText(Days);
                                if(i == 0) {
                                    cell.setTypeface(null, Typeface.BOLD);
                                    cell.setTextColor(getResources().getColor(R.color.attAbsentMark));
                                }
                                cell.setGravity(Gravity.CENTER_HORIZONTAL);
                                cell.setPadding(16, 16, 16, 16);
                                cell.setTextSize(20);
                                cell.setBackgroundResource(R.drawable.border);
                                row.addView(cell);

                                cell = new android.support.v7.widget.AppCompatTextView(TimeTable_Exam.this);
                                cell.setText(Start_Time);
                                if(i == 0) {
                                    cell.setTypeface(null, Typeface.BOLD);
                                    cell.setTextColor(getResources().getColor(R.color.attAbsentMark));
                                }
                                cell.setGravity(Gravity.CENTER_HORIZONTAL);
                                cell.setPadding(16, 16, 16, 16);
                                cell.setTextSize(20);
                                cell.setBackgroundResource(R.drawable.border);
                                row.addView(cell);

                                cell = new android.support.v7.widget.AppCompatTextView(TimeTable_Exam.this);
                                cell.setText(End_Time);
                                if(i == 0) {
                                    cell.setTypeface(null, Typeface.BOLD);
                                    cell.setTextColor(getResources().getColor(R.color.attAbsentMark));
                                }
                                cell.setGravity(Gravity.CENTER_HORIZONTAL);
                                cell.setPadding(16, 16, 16, 16);
                                cell.setTextSize(20);
                                cell.setBackgroundResource(R.drawable.border);
                                row.addView(cell);

                                cell = new android.support.v7.widget.AppCompatTextView(TimeTable_Exam.this);
                                cell.setText(Subject);
                                if(i == 0) {
                                    cell.setTypeface(null, Typeface.BOLD);
                                    cell.setTextColor(getResources().getColor(R.color.attAbsentMark));
                                }
                                cell.setGravity(Gravity.CENTER_HORIZONTAL);
                                cell.setPadding(16, 16, 16, 16);
                                cell.setTextSize(20);
                                cell.setBackgroundResource(R.drawable.border);
                                row.addView(cell);

                                table.addView(row);
                            }
                            examType.setText(jsonArray.getJSONObject(1).getString("Exam_Type"));
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
