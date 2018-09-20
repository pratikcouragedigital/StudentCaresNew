package com.studentcares.spps;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.connectivity.Get_StdDivSub_Sqlite;
import com.studentcares.spps.connectivity.User_List_Request;
import com.studentcares.spps.sessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Graph_Tab_Daily_Student extends Fragment implements View.OnClickListener{


    private Calendar calendar;
    private int year, month, day;

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

    private View v;
    LinearLayout spinnerLayout,lateMarkLayout;
    String userId, userType, schoolId, selectedDate;
    String method, selectedUserType = "Student", TotalCount, PresentCount, AbsentCount, LateMarkCount;


    EditText txtDate;
    TextView txtTotalCount, txtPresentDays, txtAbsentDays, txtLateMarks;
    private ProgressDialog progressDialog;
    RelativeLayout graphLayout,pieChartLayout;

    PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.graph_tab_daily_student_staff, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SessionManager sessionManagerNgo = new SessionManager(v.getContext());
        HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
        userId = typeOfUser.get(SessionManager.KEY_USERID);
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        userType = typeOfUser.get(SessionManager.KEY_USERTYPE);

        txtDate = (EditText) v.findViewById(R.id.txtDate);
        standard = (Spinner) v.findViewById(R.id.standard);
        division = (Spinner) v.findViewById(R.id.division);

        spinnerLayout = (LinearLayout) v.findViewById(R.id.spinnerLayout);
        lateMarkLayout = (LinearLayout) v.findViewById(R.id.lateMarkLayout);
        graphLayout = (RelativeLayout) v.findViewById(R.id.graphLayout);
        pieChartLayout = (RelativeLayout) v.findViewById(R.id.pieChartLayout);
        pieChart = (PieChart) v.findViewById(R.id.chart);

        txtTotalCount = (TextView) v.findViewById(R.id.txtTotalCount);
        txtPresentDays = (TextView) v.findViewById(R.id.txtPresentDays);
        txtAbsentDays = (TextView) v.findViewById(R.id.txtAbsentDays);
        txtLateMarks = (TextView) v.findViewById(R.id.txtLateMarks);

        graphLayout.setVisibility(View.GONE);
        pieChartLayout.setVisibility(View.GONE);
        lateMarkLayout.setVisibility(View.GONE);
        txtDate.setOnClickListener(this);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        String currentDate = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);
        selectedDate = DateFormatter.ChangeDateFormat(currentDate);
        txtDate.setText(selectedDate);

        getStandardDetails();
        getDivisionDetails();

    }

    private void getStandardDetails() {
        standardArrayList = new String[]{
                "Standard"
        };
        standardNameList = new ArrayList<>(Arrays.asList(standardArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(this.getActivity(), R.layout.spinner_item, standardNameList);
        getListOfStandard();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        standard.setAdapter(spinnerAdapter);
        standard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    standardName = parent.getItemAtPosition(position).toString();
                    standardId = standardIdList.get(position);
                    getDivisionDetails();
                    getListOfDivision();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getListOfStandard() {
        Get_StdDivSub_Sqlite standardSpinnerList = new Get_StdDivSub_Sqlite(this.getActivity());
        standardSpinnerList.FetchAllstandard(standardNameList, standardIdList, schoolId, spinnerAdapter);
    }

    private void getDivisionDetails() {
        divisionArrayList = new String[]{
                "Division"
        };
        divisionNameList = new ArrayList<>(Arrays.asList(divisionArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(this.getActivity(), R.layout.spinner_item, divisionNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        division.setAdapter(spinnerAdapter);
        division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    divisionName = parent.getItemAtPosition(position).toString();
                    divisionId = divisionIdList.get(position);
                    GetDailyGraph();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getListOfDivision() {
        divisionDialogBox = ProgressDialog.show(this.getActivity(), "", "Fetching Division Please Wait...", true);
        Get_StdDivSub_Sqlite divisionSpinnerList = new Get_StdDivSub_Sqlite(this.getActivity());
        divisionSpinnerList.FetchAllDivision(divisionNameList, divisionIdList, schoolId, spinnerAdapter, standardId, divisionDialogBox);
    }


    private void GetDailyGraph() {

        progressDialog = new ProgressDialog(v.getContext());
        progressDialog.setMessage("Please Wait.");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        method = "Graph_Daily_Classwise";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Date", selectedDate);
            jsonObject.put("Standard_Id", standardId);
            jsonObject.put("Division_Id", divisionId);
            jsonObject.put("School_id", schoolId);
        }
        catch (JSONException e) {
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
                            progressDialog.dismiss();
                            String res = response.getString("responseDetails");
                            if (res.equals("Data Not Available.")) {
                                graphLayout.setVisibility(View.GONE);
                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                builder.setTitle("Result");
                                builder.setMessage("Graph Data Not Available For This Date.");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface alert, int which) {
                                        // TODO Auto-generated method stub
                                        //Do something
                                        alert.dismiss();
                                    }
                                });
                                AlertDialog alert1 = builder.create();
                                alert1.show();
                            } else {

                                try {
                                    JSONObject obj = response.getJSONObject("responseDetails");

                                    TotalCount = obj.getString("Total");
                                    PresentCount = obj.getString("Present");
                                    AbsentCount = obj.getString("Absent");

                                    txtTotalCount.setText(TotalCount);
                                    txtPresentDays.setText(PresentCount);
                                    txtAbsentDays.setText(AbsentCount);
                                   // txtLateMarks.setText(LateMarkCount);
                                    graphLayout.setVisibility(View.VISIBLE);
                                    pieChartLayout.setVisibility(View.VISIBLE);
                                    showPieChart();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Toast.makeText(v.getContext(), "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(v.getContext(), "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void showPieChart() {

        int total = Integer.parseInt(TotalCount);
        int present = Integer.parseInt(PresentCount);
        int absent = Integer.parseInt(AbsentCount);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(total, 0));
        entries.add(new Entry(present, 1));
        entries.add(new Entry(absent, 2));

        PieDataSet dataset = new PieDataSet(entries, "");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Total");
        labels.add("Present");
        labels.add("Absent");

        PieData data = new PieData(labels, dataset);
        data.setValueTextSize(12);
        data.setValueTextColor(getResources().getColor(R.color.colorBlack));

        final int[] pieChartColor = {
                getResources().getColor(R.color.attTotalDays),
                getResources().getColor(R.color.colorGreen500),
                getResources().getColor(R.color.attAbsentMark),
        };
        dataset.setColors(pieChartColor);
        pieChart.setDescription(" ");
        pieChart.setData(data);
        pieChart.animateY(5000);
    }

    @Override
    public void onClick(final View v) {

        if(v.getId() == R.id.txtDate){
            Calendar mcurrentDate = Calendar.getInstance();
            year = mcurrentDate.get(Calendar.YEAR);
            month = mcurrentDate.get(Calendar.MONTH);
            day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    selectedDate = String.valueOf(selectedyear) + "-" + String.valueOf(selectedmonth+1) + "-" + String.valueOf(selectedday);

                    selectedDate = DateFormatter.ChangeDateFormat(selectedDate);
                    txtDate.setText(selectedDate);

                    if (standardName == null || standardName.isEmpty()) {
                        Toast.makeText(v.getContext(), "Please select Standard.", Toast.LENGTH_LONG).show();
                    } else if (divisionName == null || divisionName.isEmpty()) {
                        Toast.makeText(v.getContext(), "Please select Division.", Toast.LENGTH_LONG).show();
                    }else{
                        GetDailyGraph();
                    }

                }
            }, year, month, day);
            mDatePicker.show();
        }
    }

}

