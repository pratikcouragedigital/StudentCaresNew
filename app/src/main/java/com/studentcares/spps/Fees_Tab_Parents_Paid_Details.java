package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.studentcares.spps.adapter.Fees_Parents_Sub_PaidDetails_Adapter;
import com.studentcares.spps.connectivity.Fees_Get_Details_Parents;
import com.studentcares.spps.model.Fees_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fees_Tab_Parents_Paid_Details extends BaseActivity implements View.OnClickListener {

    public List<Fees_Items> feesPaidDetailsItems = new ArrayList<Fees_Items>();
    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    private ProgressDialog progressDialog = null;

    TextView txtPaymentReceivedBy;
    TextView txtPaymentReceivedDate;
    TextView txtPrevBalance;
    TextView txtPenalty;
    TextView txtGrandTotal;
    TextView txtBalance;
    TextView txtPaidAmount;

    String paymentReceivedBy;
    String paymentReceivedDate;
    public String receiptNo;
    public String month;
    public String paidAmount;
    public String penalty;
    public String balance;
    public String prevBalance;
    public String grandTotal;
    public String paymentMode;
    public String chequeNo;
    public String chequeDate;
    public String chequeBankName;
    public String chequeBranchName;
    public String feetype;
    String userId;
    String schoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fees_tab_parents_paid_details);

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

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(SessionManager.KEY_USERID);
        schoolId = user.get(SessionManager.KEY_SCHOOLID);

        recyclerView = (RecyclerView) findViewById(R.id.feesPaidDetailsRecyclerView);
        txtPaymentReceivedBy = (TextView) findViewById(R.id.txtPaymentReceivedBy);
        txtPaymentReceivedDate = (TextView) findViewById(R.id.txtPaymentReceivedDate);
        txtPrevBalance = (TextView) findViewById(R.id.txtPrevBalance);
        txtPenalty = (TextView) findViewById(R.id.txtPenalty);
        txtGrandTotal = (TextView) findViewById(R.id.txtGrandTotal);
        txtBalance = (TextView) findViewById(R.id.txtBalance);
        txtPaidAmount = (TextView) findViewById(R.id.txtPaidAmount);

        Intent intent = getIntent();
        if (null != intent) {
            paymentReceivedBy = intent.getStringExtra("CreatedBy");
            paymentReceivedDate = intent.getStringExtra("CreatedDate");
            receiptNo = intent.getStringExtra("ReceiptNo");
            month = intent.getStringExtra("Month");
            paidAmount = intent.getStringExtra("PaidAmount");
            penalty = intent.getStringExtra("Penalty");
            balance = intent.getStringExtra("Balance");
            prevBalance = intent.getStringExtra("PrevBalance");
            grandTotal = intent.getStringExtra("GrandTotal");
            paymentMode = intent.getStringExtra("PaymentMode");
            chequeNo = intent.getStringExtra("ChequeNo");
            chequeDate = intent.getStringExtra("ChequeDate");
            chequeBankName = intent.getStringExtra("ChequeBankName");
            chequeBranchName = intent.getStringExtra("ChequeBranchName");
            feetype = intent.getStringExtra("Feetype");
        }


        txtPaymentReceivedBy.setText(paymentReceivedBy);
        txtPaymentReceivedDate.setText(paymentReceivedDate);
        txtPrevBalance.setText(prevBalance);
        txtPenalty.setText(penalty);
        txtGrandTotal.setText(grandTotal);
        txtBalance.setText(balance);
        txtPaidAmount.setText(paidAmount);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new Fees_Parents_Sub_PaidDetails_Adapter(feesPaidDetailsItems);
        recyclerView.setAdapter(reviewAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();
        getFeeSubItems();
    }

    private void getFeeSubItems() {
        try {
            Fees_Get_Details_Parents p_Fees_SubPaid_Details = new Fees_Get_Details_Parents(Fees_Tab_Parents_Paid_Details.this);
            p_Fees_SubPaid_Details.ShowSubFeesPaidDetails(feesPaidDetailsItems, recyclerView, reviewAdapter, schoolId,userId,receiptNo, feetype,progressDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}