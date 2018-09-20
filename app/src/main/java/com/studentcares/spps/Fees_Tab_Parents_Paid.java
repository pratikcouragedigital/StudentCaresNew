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

import com.studentcares.spps.adapter.Fees_Parents_Paid_Adapter;
import com.studentcares.spps.connectivity.Fees_Get_Details_Parents;
import com.studentcares.spps.model.Fees_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fees_Tab_Parents_Paid extends Fragment implements View.OnClickListener{

    ProgressDialog progressDialog;
    List<Fees_Items> feesPaid_Items = new ArrayList<Fees_Items>();
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    String userId;
    String schoolId;
    LinearLayoutManager linearLayoutManager;

    String ptaType="1";
    private View v;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fees_tab_parents_paid, container, false);
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SessionManager sessionManager = new SessionManager(v.getContext().getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(SessionManager.KEY_USERID);
        schoolId = user.get(SessionManager.KEY_SCHOOLID);

        recyclerView = (RecyclerView) v.findViewById(R.id.feesPaidRecyclerView);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Fees_Parents_Paid_Adapter(feesPaid_Items);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();

        getList();
    }

    private void getList(){
        try {
            Fees_Get_Details_Parents p_Fees_Paid_Details = new Fees_Get_Details_Parents(getActivity());
            p_Fees_Paid_Details.ShowFeesPaidDetails(feesPaid_Items, recyclerView, adapter, schoolId, userId,progressDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

    }
}
