package com.studentcares.spps;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.studentcares.spps.sessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Graph_Tab_Daily_Staff extends Fragment implements View.OnClickListener{



    private Calendar calendar;
    private int year, month, day;

    private View v;
    LinearLayout spinnerLayout;
    String userId, userType, schoolId, selectedDate;
    String method, selectedUserType = "Staff", TotalCount, PresentCount, AbsentCount, LateMarkCount;

    EditText txtDate;
    TextView txtTotalCount, txtPresentDays, txtAbsentDays, txtLateMarks;
//    private ProgressDialog progressDialog;
    RelativeLayout graphLayout,pieChartLayout;
    LinearLayout lateMarkLayout;

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

        pieChart = (PieChart) v.findViewById(R.id.chart);

        txtDate = (EditText) v.findViewById(R.id.txtDate);
        spinnerLayout = (LinearLayout) v.findViewById(R.id.spinnerLayout);
        graphLayout = (RelativeLayout) v.findViewById(R.id.graphLayout);
        pieChartLayout = (RelativeLayout) v.findViewById(R.id.pieChartLayout);
        lateMarkLayout = (LinearLayout) v.findViewById(R.id.lateMarkLayout);

        txtTotalCount = (TextView) v.findViewById(R.id.txtTotalCount);
        txtPresentDays = (TextView) v.findViewById(R.id.txtPresentDays);
        txtAbsentDays = (TextView) v.findViewById(R.id.txtAbsentDays);
        txtLateMarks = (TextView) v.findViewById(R.id.txtLateMarks);

        spinnerLayout.setVisibility(View.GONE);
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

        GetDailyGraph(selectedDate);

    }

    public void GetDailyGraph(String selectedDate) {

//        progressDialog = new ProgressDialog(v.getContext());
//        progressDialog.setMessage("Please Wait.");
//        progressDialog.show();
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);

        method = "Graph_Daily_Staff";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Date", selectedDate);
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
//                            progressDialog.dismiss();
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
//                                    txtLateMarks.setText(LateMarkCount);
                                    graphLayout.setVisibility(View.VISIBLE);
                                    pieChartLayout.setVisibility(View.VISIBLE);
                                    showPieChart();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }

                            }

                        } catch (Exception e) {
//                            progressDialog.dismiss();
                            e.getMessage();
                            Toast.makeText(v.getContext(), "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
//                        progressDialog.dismiss();
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
    public void onClick(View v) {
        if(v.getId() == R.id.txtDate){

//            selectedDate = DateFormatter.GetDateFormat(v.getContext(),txtDate);
            //txtDate.setText(selectedDate);

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
                    GetDailyGraph(selectedDate);
                }
            }, year, month, day);
            mDatePicker.show();
        }
    }
}

