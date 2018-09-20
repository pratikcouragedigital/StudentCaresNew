package com.studentcares.spps;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studentcares.spps.adapter.Holiday_Adapter;
import com.studentcares.spps.connectivity.Holiday_Details_Get;
import com.studentcares.spps.model.Holidays_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Holiday_List_Student extends Fragment implements View.OnClickListener {
    ProgressDialog progressDialog;
    List<Holidays_Items> holidaysItems = new ArrayList<Holidays_Items>();
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    String userId;
    String schoolId;
    String userType;
    String holidayFor ="Student";
    LinearLayoutManager linearLayoutManager;

    private View v;

    public static Holiday_List_Student newInstance() {
        Holiday_List_Student fragment = new Holiday_List_Student();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.holiday_list, container, false);
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SessionManager sessionManager = new SessionManager(v.getContext().getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(SessionManager.KEY_USERID);
        userType = user.get(SessionManager.KEY_USERTYPE);
        schoolId = user.get(SessionManager.KEY_SCHOOLID);

        recyclerView = (RecyclerView) getView().findViewById(R.id.holidayRecyclerView);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Holiday_Adapter(holidaysItems);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();

        getList();
    }

    private void getList(){
        try {
            Holiday_Details_Get UAHoliday_List = new Holiday_Details_Get(getActivity());
            UAHoliday_List.showHolidayList(holidaysItems, recyclerView, adapter,holidayFor,schoolId, progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

    }

}