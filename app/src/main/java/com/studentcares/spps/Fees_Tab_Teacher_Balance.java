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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.adapter.Fees_Teacher_Balance_Adapter;
import com.studentcares.spps.adapter.Fees_PagerAdapter;
import com.studentcares.spps.commonClasses.RemoveLastComma;
import com.studentcares.spps.connectivity.Get_StdDivSub_Sqlite;
import com.studentcares.spps.connectivity.Fees_Get_Details_Teacher;
import com.studentcares.spps.interfaces.HideFabButton;
import com.studentcares.spps.model.Fees_Items;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.singleton.Check_Fees_Student_Send_SMS;
import com.studentcares.spps.singleton.FeesStandardDivisionInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Fees_Tab_Teacher_Balance extends Fragment implements View.OnClickListener, HideFabButton {

    ProgressDialog progressDialog;
    List<Fees_Items> fees_Items = new ArrayList<Fees_Items>();
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    String staffId;
    String schoolId;
    String studentId;
    String mobileNo;
    LinearLayoutManager linearLayoutManager;

    private View v;

    Spinner standard;
    Spinner division;

    String standardName = "";
    String standardId = "";
    private String[] standardArrayList;
    private Std_Div_Filter_Adapter spinnerAdapter;
    private ProgressDialog standardDialogBox;
    private List<String> standardIdList = new ArrayList<String>();
    private List<String> standardNameList = new ArrayList<String>();

    String divisionName = "";
    String divisionId = "";
    private String[] divisionArrayList;
    private ProgressDialog divisionDialogBox;
    private List<String> divisionIdList = new ArrayList<String>();
    private List<String> divisionNameList = new ArrayList<String>();

    FloatingActionButton mainFabBtn;
    FloatingActionButton sendSMSFab;
    Check_Fees_Student_Send_SMS chk_mark_attendance;
    List<String> studentListArray;
    List<String> mobileNoArray;
    List<String> msgArray;
    String msg;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Fees_PagerAdapter _fees_pagerAdapterT;

    public static HideFabButton hideFabButton;

    public void newInstance(ViewPager viewPager, TabLayout tabLayout, Fees_PagerAdapter _fees_pagerAdapterT) {
        this.viewPager = viewPager;
        this.tabLayout = tabLayout;
        this._fees_pagerAdapterT = _fees_pagerAdapterT;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fees_tab_teacher_balance_list, container, false);
        hideFabButton = this;
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FeesStandardDivisionInstance feesStandardDivisionInstance = new FeesStandardDivisionInstance();
        standardId = feesStandardDivisionInstance.getStandard();
        divisionId = feesStandardDivisionInstance.getDivision();

        SessionManager sessionManager = new SessionManager(v.getContext().getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        staffId = user.get(SessionManager.KEY_USERID);
        schoolId = user.get(SessionManager.KEY_SCHOOLID);

        if(standardId == "" && divisionId == "") {
            standardId = user.get(SessionManager.KEY_STANDARD);
            divisionId = user.get(SessionManager.KEY_DIVISION);
        }

        recyclerView = (RecyclerView) v.findViewById(R.id.feesListRecyclerView);
        recyclerView.setHasFixedSize(true);

        mainFabBtn = (FloatingActionButton) v.findViewById(R.id.mainFabBtn);
        sendSMSFab = (FloatingActionButton) v.findViewById(R.id.sendSMSFab);

        mainFabBtn.setOnClickListener(this);
        sendSMSFab.setOnClickListener(this);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Fees_Teacher_Balance_Adapter(fees_Items,sendSMSFab,mainFabBtn);
        recyclerView.setAdapter(adapter);

        if (standardId.equals("") && divisionId.equals("") || standardId.equals("0") && divisionId.equals("0")) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            builder.setTitle("Message");
            builder.setMessage("Please Select Standard & Division.Using below Button.");
            android.app.AlertDialog alert1 = builder.create();
            alert1.show();
        } else {
            getList();
        }
    }

    private void getList(){
        adapter = new Fees_Teacher_Balance_Adapter(fees_Items,sendSMSFab,mainFabBtn);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();

        try {
            Fees_Get_Details_Teacher t_Fees_Get_Balance_Details = new Fees_Get_Details_Teacher(getActivity());
            t_Fees_Get_Balance_Details.Balance_FeesDetails(fees_Items, recyclerView, adapter, schoolId, standardId,divisionId,progressDialog);
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
    public void onClick(View v) {

        if (v.getId() == R.id.mainFabBtn) {

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
                        //getList();

                        FeesStandardDivisionInstance feesStandardDivisionInstance = new FeesStandardDivisionInstance();
                        feesStandardDivisionInstance.setStandard(standardId);
                        feesStandardDivisionInstance.setDivision(divisionId);
                        mainFabBtn.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).select();
                        Fees_Tab_Teacher_NotPaid.hideFabButton.hideFab(true);
                        viewPager.setVisibility(View.GONE);
                        Fees_Tab_Teacher_NotPaid _fees__studentListNotPaid = new Fees_Tab_Teacher_NotPaid();
                        _fees__studentListNotPaid.newInstance(viewPager, tabLayout, _fees_pagerAdapterT);
                        getFragmentManager().beginTransaction().replace(R.id.feesNotPaidCoordinatorLayout, _fees__studentListNotPaid).commit();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tabLayout.getTabAt(1).select();
                                Fees_Tab_Teacher_Balance _fees__studentListBalanceT = new Fees_Tab_Teacher_Balance();
                                _fees__studentListBalanceT.newInstance(viewPager, tabLayout, _fees_pagerAdapterT);
                                getFragmentManager().beginTransaction().replace(R.id.feesBalancedCoordinatorLayout, _fees__studentListBalanceT).commit();
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
        } else if (v.getId() == R.id.sendSMSFab) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Do you want to send message?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //EnterMessage();
                    SendMsg();
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

    public void EnterMessage() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        int margin = (int) getResources().getDimension(R.dimen.margin);
        RelativeLayout layout = new RelativeLayout(getActivity());
        layout.setLayoutParams(new RelativeLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT));

        final TextView txtMsg = new TextView(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtMsg.setLayoutParams(params);
        params.setMargins(margin, margin, margin, margin);
        txtMsg.setId(Integer.parseInt("1"));


        layout.addView(txtMsg);

        alert.setTitle(Html.fromHtml("<b>" + "Enter Message."));
        alert.setView(layout);

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                msg = txtMsg.getText().toString();
                if (msg.equals("")) {
                    Toast.makeText(getActivity(), "Please select Standard.", Toast.LENGTH_LONG).show();
                } else {
                    SendMsg();
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

    private void SendMsg() {
        studentListArray = chk_mark_attendance.getStudentListInstance();
        mobileNoArray = chk_mark_attendance.getmobileNoList();
        msgArray = chk_mark_attendance.getmsgList();


        RemoveLastComma removeLastComma = new RemoveLastComma(getActivity());
        studentId = removeLastComma.CommaRemove(studentListArray);
        mobileNo = removeLastComma.CommaRemove(mobileNoArray);
        msg = removeLastComma.CommaRemove(msgArray);

        String type = "balanceFee";

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait, message is sending.");
        progressDialog.show();

        Fees_Get_Details_Teacher removeAttendance = new Fees_Get_Details_Teacher(getContext());
        removeAttendance.SendSmsFromServer(schoolId,mobileNo,studentId,msg,staffId,type,progressDialog);
    }

    @Override
    public void hideFab(boolean status) {
        if(status) {
            mainFabBtn.setVisibility(View.GONE);
        }
    }
}