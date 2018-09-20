package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.studentcares.spps.adapter.Attendance_Tab_Stud_PagerAdapter;
import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.adapter.Attendance_Student_Present_List_Adapter;
import com.studentcares.spps.commonClasses.RemoveLastComma;
import com.studentcares.spps.connectivity.Attendance_Student_Marking;
import com.studentcares.spps.connectivity.Get_StdDivSub_Sqlite;
import com.studentcares.spps.connectivity.Attendance_Get_Absent_Present_List_Student;
import com.studentcares.spps.interfaces.HideFabButton;
import com.studentcares.spps.model.Attendance_List_Items;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.singleton.Chk_Mark_Attendance;
import com.studentcares.spps.singleton.StandardDivisionInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import tourguide.tourguide.TourGuide;

public class Attendance_Tab_Stud_Present_List extends Fragment implements View.OnClickListener, HideFabButton {

    private ProgressDialog progressDialog = null;
    String staffId = "";
    Spinner standard;
    Spinner division;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    private StringBuilder date;
    EditText txtSelectedFilter;
    String selectedDate = "";

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

    public List<Attendance_List_Items> TAttendanseItems = new ArrayList<Attendance_List_Items>();
    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    RelativeLayout emptyLayout;

    String counter = "1";
    String schoolId;

    FloatingActionButton mainFabBtn;
    FloatingActionButton attendanceCheckFab;
    Chk_Mark_Attendance chk_mark_attendance;
    List<String> absentListArray;
    private String studentId;
    private View v;
    private TourGuide mTourGuideHandler;
    private ViewPager viewPager;
    private Attendance_Tab_Stud_PagerAdapter adapter;
    private TabLayout tabLayout;

    public static HideFabButton hideFabButton;

    public void newInstance(ViewPager viewPager, TabLayout tabLayout, Attendance_Tab_Stud_PagerAdapter adapter) {
        this.viewPager = viewPager;
        this.tabLayout = tabLayout;
        this.adapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.attendance_tab_stud_present, container, false);
        hideFabButton = this;
        mainFabBtn = (FloatingActionButton) v.findViewById(R.id.mainFabBtn);
        return v;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StandardDivisionInstance standardDivisionInstance = new StandardDivisionInstance();
        standardId = standardDivisionInstance.getStandard();
        divisionId = standardDivisionInstance.getDivision();

        SessionManager sessionManagerNgo = new SessionManager(v.getContext().getApplicationContext());
        HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
        staffId = typeOfUser.get(SessionManager.KEY_USERID);
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);

        if(standardId == "" && divisionId == "") {
            standardId = typeOfUser.get(SessionManager.KEY_STANDARD);
            divisionId = typeOfUser.get(SessionManager.KEY_DIVISION);
        }

        recyclerView = (RecyclerView) v.findViewById(R.id.attendansekListRecyclerView);
        emptyLayout = (RelativeLayout) v.findViewById(R.id.emptyLayout);
        attendanceCheckFab = (FloatingActionButton) v.findViewById(R.id.attendanceCheckFab);

        mainFabBtn.setOnClickListener(this);
        attendanceCheckFab.setOnClickListener(this);

        if (standardId.equals("") && divisionId.equals("") || standardId.equals("0") && divisionId.equals("0")) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            builder.setTitle("Message");
            builder.setMessage("Please Select Standard & Division.Using below Button.");
            android.app.AlertDialog alert1 = builder.create();
            alert1.show();
            emptyLayout.setVisibility(View.VISIBLE);

        } else {
            getFilterList();
        }

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        chk_mark_attendance = new Chk_Mark_Attendance();
    }

    private void getFilterList() {
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new Attendance_Student_Present_List_Adapter(TAttendanseItems, attendanceCheckFab,mainFabBtn);
        recyclerView.setAdapter(reviewAdapter);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();
        try {
            Attendance_Get_Absent_Present_List_Student showAttendanseDetails = new Attendance_Get_Absent_Present_List_Student(getContext());
            showAttendanseDetails.showPresent_Student_List(TAttendanseItems, recyclerView, reviewAdapter, standardId, divisionId, schoolId, progressDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getStandarddDetails() {
        standardArrayList = new String[]{
                "Standard"
        };
        standardNameList = new ArrayList<>(Arrays.asList(standardArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(getActivity(), R.layout.spinner_item, standardNameList);
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
        Get_StdDivSub_Sqlite standardSpinnerList = new Get_StdDivSub_Sqlite(getActivity());
        standardSpinnerList.FetchAllstandard(standardNameList, standardIdList, schoolId, spinnerAdapter);
    }

    private void getDivisionDetails() {
        divisionArrayList = new String[]{
                "Division"
        };
        divisionNameList = new ArrayList<>(Arrays.asList(divisionArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(getActivity(), R.layout.spinner_item, divisionNameList);
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
        divisionDialogBox = ProgressDialog.show(getActivity(), "", "Fetching Division Please Wait...", true);
        Get_StdDivSub_Sqlite divisionSpinnerList = new Get_StdDivSub_Sqlite(getActivity());
        divisionSpinnerList.FetchAllDivision(divisionNameList, divisionIdList, schoolId, spinnerAdapter, standardId, divisionDialogBox);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.mainFabBtn) {

            //Dynamic Spinner
            final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
            int margin = (int) getResources().getDimension(R.dimen.margin);
            RelativeLayout layout = new RelativeLayout(getActivity());
            layout.setLayoutParams(new RelativeLayout.LayoutParams(
                    CoordinatorLayout.LayoutParams.MATCH_PARENT,
                    CoordinatorLayout.LayoutParams.WRAP_CONTENT));

            standard = new Spinner(getActivity());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            standard.setLayoutParams(params);
            params.setMargins(margin, margin, margin, margin);
            standard.setId(Integer.parseInt("1"));


            division = new Spinner(getActivity());
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, standard.getId());
            params.setMargins(margin, margin, margin, margin);
            division.setLayoutParams(params);
            division.setId(Integer.parseInt("2"));

            TextView txtView = new TextView(getActivity());
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
                        Toast.makeText(getActivity(), "Please select Standard.", Toast.LENGTH_LONG).show();
                    } else if (divisionName == null || divisionName.isEmpty()) {
                        Toast.makeText(getActivity(), "Please select Division.", Toast.LENGTH_LONG).show();
                    } else {
                        StandardDivisionInstance standardDivisionInstance = new StandardDivisionInstance();
                        standardDivisionInstance.setStandard(standardId);
                        standardDivisionInstance.setDivision(divisionId);
                        mainFabBtn.setVisibility(View.GONE);
                        tabLayout.getTabAt(1).select();
                        Attendance_Tab_Stud_Absent_List.hideFabButton.hideFab(true);
                        viewPager.setVisibility(View.GONE);
                        Attendance_Tab_Stud_Absent_List attendance_tabStud_absent_list = new Attendance_Tab_Stud_Absent_List();
                        attendance_tabStud_absent_list.newInstance(viewPager, tabLayout, adapter);
                        getFragmentManager().beginTransaction().replace(R.id.studentAbsentCoordinatorLayout, attendance_tabStud_absent_list).commit();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tabLayout.getTabAt(0).select();
                                Attendance_Tab_Stud_Present_List attendance_tabStud_present_list = new Attendance_Tab_Stud_Present_List();
                                attendance_tabStud_present_list.newInstance(viewPager, tabLayout, adapter);
                                getFragmentManager().beginTransaction().replace(R.id.studentPresentCoordinatorLayout, attendance_tabStud_present_list).commit();
                                viewPager.setVisibility(View.VISIBLE);
                            }
                        }, 500);
                    }
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });
            alert.show();
        } else if (view.getId() == R.id.attendanceCheckFab) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Do you want to mark as a absent?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    removeAttendance();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }
    }

    public void removeAttendance() {

        RemoveLastComma removeLastComma = new RemoveLastComma(getActivity());
        studentId = removeLastComma.CommaRemove(absentListArray);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait, Selected student attendance is making as a absent");
        progressDialog.show();
        String fromClass = "Attendance_Tab_Stud_Present_List";
        Attendance_Student_Marking removeAttendance = new Attendance_Student_Marking(getContext());
        removeAttendance.Remove_Student_Attendance(staffId, studentId, fromClass,schoolId,progressDialog);
    }

    @Override
    public void hideFab(boolean status) {
        if(status) {
            mainFabBtn.setVisibility(View.GONE);
        }
    }
}