package com.studentcares.spps;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studentcares.spps.adapter.Fees_Parents_OneTime_Adapter;
import com.studentcares.spps.adapter.Fees_Parents_Unpaid_Adapter;
import com.studentcares.spps.adapter.Fees_Parents_Unpaid_MonthList_Adapter;
import com.studentcares.spps.connectivity.Fees_Get_Details_Parents;
import com.studentcares.spps.model.Fees_Items;
import com.studentcares.spps.model.Fees_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fees_Tab_Parents_Unpaid extends Fragment implements View.OnClickListener{

    ProgressDialog progressDialog;
    List<Fees_Items> monthFeeUnpaidItems = new ArrayList<Fees_Items>();
    List<Fees_Items> feeMonthListItems = new ArrayList<Fees_Items>();
    RecyclerView.Adapter adapterOT;
    RecyclerView.Adapter adapterM;
    RecyclerView.Adapter adapterML;
    RecyclerView recyclerViewOT;
    RecyclerView recyclerViewM;
    RecyclerView recyclerViewML;
    String userId;
    String schoolId;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fees_tab_parents_unpaid, container, false);
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SessionManager sessionManager = new SessionManager(v.getContext().getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(SessionManager.KEY_USERID);
        schoolId = user.get(SessionManager.KEY_SCHOOLID);

        getOneTimeFeeList();
        getListOfMonth();
        getMonthLyFeeDetails();

    }

    private void getOneTimeFeeList() {
        recyclerViewOT = (RecyclerView) v.findViewById(R.id.onetimeFeeItemsRecyclerView);
        recyclerViewOT.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewOT.setLayoutManager(linearLayoutManager);
        adapterOT = new Fees_Parents_OneTime_Adapter(monthFeeUnpaidItems);

        recyclerViewOT.setAdapter(adapterOT);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();

        try {
            Fees_Get_Details_Parents _Fees_Get_DetailsParents = new Fees_Get_Details_Parents(getActivity());
            _Fees_Get_DetailsParents.ShowOneTimeFeesUnpaidDetails(monthFeeUnpaidItems, recyclerViewOT, adapterOT, schoolId,userId, progressDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getListOfMonth() {
        recyclerViewML = (RecyclerView) v.findViewById(R.id.monthNameListRecyclerView);
        recyclerViewML.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getActivity(),4);
        recyclerViewML.setLayoutManager(gridLayoutManager);
        adapterML = new Fees_Parents_Unpaid_MonthList_Adapter(feeMonthListItems);

        recyclerViewML.setAdapter(adapterML);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();

        try {
            Fees_Get_Details_Parents p_Fees_Unpaid_MonthList = new Fees_Get_Details_Parents(getActivity());
            p_Fees_Unpaid_MonthList.ShowUnpaidMonthList(feeMonthListItems, recyclerViewML, adapterML, schoolId,userId, progressDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMonthLyFeeDetails(){
        recyclerViewM = (RecyclerView) v.findViewById(R.id.monthNameFeeRecyclerView);
        recyclerViewM.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewM.setLayoutManager(linearLayoutManager);
        adapterM = new Fees_Parents_Unpaid_Adapter(monthFeeUnpaidItems);
        recyclerViewM.setAdapter(adapterM);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();

        try {
            Fees_Get_Details_Parents p_Unpaid_MonthLyFeesDetails = new Fees_Get_Details_Parents(getActivity());
            p_Unpaid_MonthLyFeesDetails.ShowUnpaidMonthlyFeesDetails(monthFeeUnpaidItems, recyclerViewM, adapterM, schoolId,userId, progressDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

    }
}
