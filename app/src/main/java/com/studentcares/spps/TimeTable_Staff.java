package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import com.studentcares.spps.connectivity.User_List_Request;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.sqlLite.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TimeTable_Staff extends BaseActivity implements View.OnClickListener{

    Home_Menu activity;

    SessionManager sessionManager;

    private DataBaseHelper mydb;
    private String schoolId,userType,userId;

    LinearLayout layout;
    TableLayout table;
    TextView cell;

    FloatingActionButton filterFab;
    Spinner staffListSpinner;
    String StaffId;
    String StaffName;
    private String[] staffListSpinnerArrayList;
    private Std_Div_Filter_Adapter spinnerAdapter;
    private ProgressDialog staffListSpinnerDialogBox;
    private List<String> StaffIdList = new ArrayList<String>();
    private List<String> StaffNameList = new ArrayList<String>();

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
        staffListSpinner = new Spinner(this);
        RelativeLayout container = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin, margin, margin, margin);

        staffListSpinner.setLayoutParams(params);
        container.addView(staffListSpinner);
        alert.setTitle(Html.fromHtml("<b>" + "Select Staff." + "</b>"));

        //getListOfstaffListSpinner();
        alert.setView(container);
        staffListSpinnerArrayList = new String[]{
                "Select Staff"
        };
        StaffNameList = new ArrayList<>(Arrays.asList(staffListSpinnerArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, StaffNameList);
        getListOfstaffListSpinner();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staffListSpinner.setAdapter(spinnerAdapter);
        staffListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    StaffName = parent.getItemAtPosition(position).toString();
                    StaffId = StaffIdList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                userId = StaffId;
                GetTimeTable();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });
        alert.show();
    }

    private void getListOfstaffListSpinner() {
        User_List_Request GPSStaffOutWork = new User_List_Request(this);
        GPSStaffOutWork.ShowStaffList(StaffNameList, StaffIdList, schoolId, spinnerAdapter);
    }

    private void GetTimeTable() {
        table.removeAllViews();
        String method ="Timetable_Staff";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Staff_Id", userId);
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

                                TableRow row = new TableRow(TimeTable_Staff.this);

                                cell = new android.support.v7.widget.AppCompatTextView(TimeTable_Staff.this);
                                cell.setText(Time);

                                cell.setPadding(16, 16, 16, 16);
                                cell.setTextSize(20);
                                cell.setTypeface(null, Typeface.BOLD);
                                cell.setTextColor(getResources().getColor(R.color.attAbsentMark));
                                cell.setBackgroundResource(R.drawable.border);
                                row.addView(cell);

                                JSONArray dayOrSubArray = jsonObject.getJSONArray("DaySubjectList");
                                if(dayOrSubArray.length() == 0){

                                    TableRow row2 = new TableRow(TimeTable_Staff.this);
                                    row2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1));
                                    cell = new android.support.v7.widget.AppCompatTextView(TimeTable_Staff.this);
                                    cell.setText("Recces");
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
                                        String Standard = jsonObjectDaySub.getString("Standard");
                                        String Division = jsonObjectDaySub.getString("Division");

                                        cell = new android.support.v7.widget.AppCompatTextView(TimeTable_Staff.this);

                                        if(i == 0 ){
                                            cell.setText(dayOrSub);
                                            cell.setTypeface(null, Typeface.BOLD);
                                            cell.setTextColor(getResources().getColor(R.color.attAbsentMark));
                                        }
                                        else{
                                            if(Subject.equals("")){
                                                Subject = "Off";
                                                cell.setGravity(Gravity.CENTER_HORIZONTAL);
                                                cell.setText(Subject);
                                            }
                                            else{
                                                cell.setGravity(Gravity.CENTER_HORIZONTAL);
                                                cell.setText(Html.fromHtml(Subject+"( "+Standard + " / "+ Division +" )" ));
                                            }
                                        }
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