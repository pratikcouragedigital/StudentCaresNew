package com.studentcares.spps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studentcares.spps.R;
import com.studentcares.spps.model.Fees_Items;

import java.util.List;

public class Fees_Parents_Unpaid_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Fees_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;

    public Fees_Parents_Unpaid_Adapter(List<Fees_Items> items) {
        this.listItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        if (i == VIEW_TYPE_EMPTY) {
//            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
//            Fees_Parents_Unpaid_Adapter.EmptyViewHolder emptyViewHolder = new Fees_Parents_Unpaid_Adapter.EmptyViewHolder(v);
//            return emptyViewHolder;
//        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fees_tab_parents_unpaid_monthfee_subitems, viewGroup, false);
        viewHolder = new Fees_Parents_Unpaid_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Fees_Parents_Unpaid_Adapter.ViewHolder) {
            Fees_Parents_Unpaid_Adapter.ViewHolder vHolder = (Fees_Parents_Unpaid_Adapter.ViewHolder) viewHolder;
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtFeeName;
        public TextView txtFeeCost;
        public View cardView;

        Fees_Items listItems = new Fees_Items();

        public ViewHolder(View itemView) {
            super(itemView);
            txtFeeName = (TextView) itemView.findViewById(R.id.txtFeeName);
            txtFeeCost = (TextView) itemView.findViewById(R.id.txtFeeCost);
            cardView = itemView;
        }

        public void bindListDetails(Fees_Items listItems) {
            this.listItems = listItems;
            txtFeeName.setText(listItems.getfeeTypeNames());
            txtFeeCost.setText(listItems.getfeeTypeCost() +" Rs.");
        }
    }
//    public class EmptyViewHolder extends RecyclerView.ViewHolder {
//        public EmptyViewHolder(View v) {
//            super(v);
//        }
//    }
}
