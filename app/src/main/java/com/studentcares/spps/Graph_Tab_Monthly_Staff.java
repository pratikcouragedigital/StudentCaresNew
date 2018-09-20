package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.studentcares.spps.connectivity.User_List_Request;
import com.studentcares.spps.sessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Graph_Tab_Monthly_Staff extends Fragment implements View.OnClickListener {

    public String[] monthNameArray = new String[]{"Select Month", "January", "February", "March", "April",
            "May", "June", "July", "August", "September",
            "October", "November", "December"};

    public int[] monthId = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    String monthNo="";

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    Spinner monthListSpinner;
    Spinner userListSpinner;
    Button btnSubmit;
    String StaffStudentId="";
    String StaffStudentName;
    private Std_Div_Filter_Adapter spinnerAdapter;
    private String[] userListSpinnerArrayList;
    private ProgressDialog userListSpinnerDialogBox;
    private List<String> StaffStudentIdList = new ArrayList<String>();
    private List<String> StaffStudentNameList = new ArrayList<String>();

    LinearLayout spinnerLayout,lateMarkLayout;
    String userId,userType,schoolId,month_name;
    String method, selectedUserType = "Staff", TotalCount, PresentCount, AbsentCount, LateMarkCount;

    TextView txtTotalCount,txtPresentDays,txtAbsentDays,txtLateMarks;
    private ProgressDialog progressDialog;
    RelativeLayout graphLayout, pieChartLayout;

    private View v;

    PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.graph_monthly_student_staff, container, false);
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

        spinnerLayout = (LinearLayout) v.findViewById(R.id.spinnerLayout);
        lateMarkLayout = (LinearLayout) v.findViewById(R.id.lateMarkLayout);
        userListSpinner = (Spinner) v.findViewById(R.id.userListSpinner);
        monthListSpinner = (Spinner) v.findViewById(R.id.monthListSpinner);
        graphLayout = (RelativeLayout) v.findViewById(R.id.graphLayout);
        btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
        pieChartLayout = (RelativeLayout) v.findViewById(R.id.pieChartLayout);
        pieChart = (PieChart) v.findViewById(R.id.chart);

        txtTotalCount = (TextView) v.findViewById(R.id.txtTotalCount);
        txtPresentDays = (TextView) v.findViewById(R.id.txtPresentDays);
        txtAbsentDays = (TextView) v.findViewById(R.id.txtAbsentDays);
        txtLateMarks = (TextView) v.findViewById(R.id.txtLateMarks);


        btnSubmit.setOnClickListener(this);

        lateMarkLayout.setVisibility(View.GONE);
        graphLayout.setVisibility(View.GONE);
        pieChartLayout.setVisibility(View.GONE);
        spinnerLayout.setVisibility(View.GONE);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        String currMonth = String.valueOf(month + 1);

        getStaffListDetails();
        getMonthList();


    }

    private void getMonthList() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, monthNameArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthListSpinner.setAdapter(dataAdapter);
        monthListSpinner.setSelection(month + 1);

        monthListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    month_name = parent.getItemAtPosition(position).toString();
                    monthNo = String.valueOf(position);
                    getStaffListDetails();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
//                month_name = month_date.format(calendar.getTime());
            }
        });
    }


    private void getStaffListDetails() {
        userListSpinnerArrayList = new String[]{
                "Staff Name"
        };
        StaffStudentNameList = new ArrayList<>(Arrays.asList(userListSpinnerArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(this.getActivity(), R.layout.spinner_item, StaffStudentNameList);
        getListOfStaffSpinner();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userListSpinner.setAdapter(spinnerAdapter);
        userListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    StaffStudentName = parent.getItemAtPosition(position).toString();
                    StaffStudentId = StaffStudentIdList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getListOfStaffSpinner() {
        User_List_Request staffList = new User_List_Request(this.getActivity());
        staffList.ShowStaffList(StaffStudentNameList, StaffStudentIdList, schoolId, spinnerAdapter);
    }

    private void GetMonthlyGraph() {

        if (month_name.equals("") || month_name == null) {
            Toast.makeText(this.getActivity(), "Please Select Month.", Toast.LENGTH_SHORT).show();
        } else {

            progressDialog = new ProgressDialog(v.getContext());
            progressDialog.setMessage("Please Wait.");
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            method = "Graph_Monthly_UserWise";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("User_Id", StaffStudentId);
                jsonObject.put("User_Type", selectedUserType);
                jsonObject.put("School_id", schoolId);
                jsonObject.put("Month", monthNo);
                jsonObject.put("Year", "2018");
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
                                progressDialog.dismiss();
                                String res = response.getString("responseDetails");
                                if (res.equals("Data Not Available.")) {
                                    graphLayout.setVisibility(View.GONE);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                    builder.setTitle("Result");
                                    builder.setMessage("Graph Data Not Available For This Month.");
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
//                                        for (int count = 0; count < jArr.length(); count++) {
//                                            JSONObject obj = jArr.getJSONObject(count);

                                        TotalCount = obj.getString("TotalSchoolDays");
                                        PresentCount = obj.getString("PresentDays");
                                        AbsentCount = obj.getString("AbsentDays");
//                                            LateMarkCount = obj.getString("LateMarkCount");
//                                        }

                                        txtTotalCount.setText(TotalCount);
                                        txtPresentDays.setText(PresentCount);
                                        txtAbsentDays.setText(AbsentCount);
//                                        txtLateMarks.setText(LateMarkCount);
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
    }

    private void showPieChart() {

        int total = Integer.parseInt(TotalCount);
        int present = Integer.parseInt(PresentCount);
        int absent = Integer.parseInt(AbsentCount);
//        int late = Integer.parseInt(LateMarkCount);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(total, 0));
        entries.add(new Entry(present, 1));
        entries.add(new Entry(absent, 2));
//        entries.add(new Entry(late, 3));

        PieDataSet dataset = new PieDataSet(entries, "");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Total");
        labels.add("Present");
        labels.add("Absent");
//        labels.add("Late Marks");

        PieData data = new PieData(labels, dataset);
        data.setValueTextSize(12);
        data.setValueTextColor(getResources().getColor(R.color.colorBlack));

        final int[] pieChartColor = {
                getResources().getColor(R.color.attTotalDays),
                getResources().getColor(R.color.colorGreen500),
                getResources().getColor(R.color.attAbsentMark),
//                getResources().getColor(R.color.attLateMark),
        };
        dataset.setColors(pieChartColor);
        pieChart.setDescription(" ");
        pieChart.setData(data);
        pieChart.animateY(5000);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSubmit){
            if(StaffStudentId.equals("")){
                Toast.makeText(getContext(), "Please Select Staff", Toast.LENGTH_SHORT).show();
            }else if(monthNo.equals("")){
                Toast.makeText(getContext(), "Please Select Month", Toast.LENGTH_SHORT).show();
            }else{
                GetMonthlyGraph();
            }
        }
    }
}
