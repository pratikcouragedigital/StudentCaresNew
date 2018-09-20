package com.studentcares.spps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studentcares.spps.R;
import com.studentcares.spps.model.Fees_Items;

import java.util.List;

public class Fees_Parents_Sub_PaidDetails_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Fees_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;

    public Fees_Parents_Sub_PaidDetails_Adapter(List<Fees_Items> items) {
        this.listItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fees_tab_parents_paid_details_items, viewGroup, false);
        viewHolder = new Fees_Parents_Sub_PaidDetails_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Fees_Parents_Sub_PaidDetails_Adapter.ViewHolder) {
            Fees_Parents_Sub_PaidDetails_Adapter.ViewHolder vHolder = (Fees_Parents_Sub_PaidDetails_Adapter.ViewHolder) viewHolder;
            Fees_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist );
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView col1_txtSrNo;
        public TextView col2_txtParticulars;
        public TextView col3_txtAmount;
        public View cardView;

        Fees_Items listItems = new Fees_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            col1_txtSrNo = (TextView) itemView.findViewById(R.id.col1_txtSrNo);
            col2_txtParticulars = (TextView) itemView.findViewById(R.id.col2_txtParticulars);
            col3_txtAmount = (TextView) itemView.findViewById(R.id.col3_txtAmount);
            cardView = itemView;
        }

        public void bindListDetails(Fees_Items listItems) {
            this.listItems = listItems;
            col3_txtAmount.setText(listItems.getfeeTypeCost());
            col2_txtParticulars.setText(listItems.getfeeTypeNames());
            col1_txtSrNo.setText(listItems.getsrNo());
            String abc = listItems.getfeeTypeNames();

            if(abc.equals("Total Amount")){
                col2_txtParticulars.setTextColor(cardView.getResources().getColor(R.color.colorAccent));
                col3_txtAmount.setTextColor(cardView.getResources().getColor(R.color.colorAccent));
            }

        }
    }
}
