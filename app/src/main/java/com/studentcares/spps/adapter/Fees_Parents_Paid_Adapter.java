package com.studentcares.spps.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studentcares.spps.Fees_Tab_Parents_Paid_Details;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Fees_Items;

import java.util.List;

public class Fees_Parents_Paid_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Fees_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;

    public Fees_Parents_Paid_Adapter(List<Fees_Items> items) {
        this.listItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        if (i == VIEW_TYPE_EMPTY) {
//            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
//            Fees_Parents_Paid_Adapter.EmptyViewHolder emptyViewHolder = new Fees_Parents_Paid_Adapter.EmptyViewHolder(v);
//            return emptyViewHolder;
//        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fees_tab_parents_paid_items, viewGroup, false);
        viewHolder = new Fees_Parents_Paid_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Fees_Parents_Paid_Adapter.ViewHolder) {
            Fees_Parents_Paid_Adapter.ViewHolder vHolder = (Fees_Parents_Paid_Adapter.ViewHolder) viewHolder;
            Fees_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist );
        }
    }

    @Override
    public int getItemCount() {
//        if(listItems.size() > 0){
//            return listItems.size();
//        }else {
//            return 1;
//        }
        return listItems.size();
    }
//    public int getItemViewType(int position) {
//        if (listItems.size() == 0) {
//            return VIEW_TYPE_EMPTY;
//        }
//        return super.getItemViewType(position);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtReceipt;
        public TextView txtMonth;
        public TextView txtTotalFee;
        public TextView txtPaidFee;
        public TextView txtDate;
        public TextView txtBalance;
        public TextView txtPrevBalance;

        public View cardView;

        Fees_Items listItems = new Fees_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            txtReceipt = (TextView) itemView.findViewById(R.id.txtReceipt);
            txtMonth = (TextView) itemView.findViewById(R.id.txtMonth);
            txtTotalFee = (TextView) itemView.findViewById(R.id.txtTotalFee);
            txtPaidFee = (TextView) itemView.findViewById(R.id.txtPaidFee);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtBalance = (TextView) itemView.findViewById(R.id.txtBalance);
            txtPrevBalance = (TextView) itemView.findViewById(R.id.txtPrevBalance);

            cardView = itemView;
            cardView.setOnClickListener(this);
        }

        public void bindListDetails(Fees_Items listItems) {
            this.listItems = listItems;

            txtTotalFee.setText(listItems.getgrandTotal());
            txtPaidFee.setText(listItems.getpaidAmounts());
            txtMonth.setText(listItems.getmonth());
            txtReceipt.setText(listItems.getreceiptNo());
            txtDate.setText(listItems.getcreatedDate());
            txtBalance.setText(listItems.getbalance());
            txtPrevBalance.setText(listItems.getprevBalance());

        }

        @Override
        public void onClick(View v) {
            if (this.listItems != null) {
                Intent gotoPaidfeesDetails = new Intent(v.getContext(), Fees_Tab_Parents_Paid_Details.class);
                gotoPaidfeesDetails.putExtra("ReceiptNo", listItems.getreceiptNo());
                gotoPaidfeesDetails.putExtra("Month", listItems.getmonth());
                gotoPaidfeesDetails.putExtra("PaidAmount", listItems.getpaidAmounts());
                gotoPaidfeesDetails.putExtra("Penalty", listItems.getpenalty());
                gotoPaidfeesDetails.putExtra("Balance", listItems.getbalance());
                gotoPaidfeesDetails.putExtra("PrevBalance", listItems.getprevBalance());
                gotoPaidfeesDetails.putExtra("GrandTotal", listItems.getgrandTotal());
                gotoPaidfeesDetails.putExtra("PaymentMode", listItems.getpaymentMode());
                gotoPaidfeesDetails.putExtra("ChequeNo", listItems.getchequeNo());
                gotoPaidfeesDetails.putExtra("ChequeDate", listItems.getchequeDate());
                gotoPaidfeesDetails.putExtra("ChequeBankName", listItems.getchequeBankName());
                gotoPaidfeesDetails.putExtra("ChequeBranchName", listItems.getchequeBranchName());
                gotoPaidfeesDetails.putExtra("CreatedBy", listItems.getcreatedBy());
                gotoPaidfeesDetails.putExtra("CreatedDate", listItems.getcreatedDate());
                gotoPaidfeesDetails.putExtra("Feetype", listItems.getfeetype());
                v.getContext().startActivity(gotoPaidfeesDetails);
            }

        }
    }
//    public class EmptyViewHolder extends RecyclerView.ViewHolder {
//        public EmptyViewHolder(View v) {
//            super(v);
//        }
//    }
}
