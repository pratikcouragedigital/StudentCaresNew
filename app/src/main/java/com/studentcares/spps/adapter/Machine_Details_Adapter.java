package com.studentcares.spps.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studentcares.spps.R;
import com.studentcares.spps.model.Machine_Info_Items;

import java.util.List;

public class Machine_Details_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Machine_Info_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;

    private static final int VIEW_TYPE_EMPTY = 1;

    public Machine_Details_Adapter(List<Machine_Info_Items> items) {
        this.listItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            Machine_Details_Adapter.EmptyViewHolder emptyViewHolder = new Machine_Details_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.machine_details_items, viewGroup, false);
        viewHolder = new Machine_Details_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Machine_Details_Adapter.ViewHolder) {
            Machine_Details_Adapter.ViewHolder vHolder = (Machine_Details_Adapter.ViewHolder) viewHolder;
            Machine_Info_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist );
        }
    }

    @Override
    public int getItemCount() {
        if(listItems.size() > 0){
            return listItems.size();
        }else {
            return 1;
        }
//        return listItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listItems.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView txtMachineNo;
        public TextView txtMachineStatus;

        public View cardView;

        Machine_Info_Items listItems = new Machine_Info_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            txtMachineNo = (TextView) itemView.findViewById(R.id.txtMachineNo);
            txtMachineStatus = (TextView) itemView.findViewById(R.id.txtMachineStatus);
            
            cardView = itemView;
        }

        public void bindListDetails(Machine_Info_Items listItems) {
            this.listItems = listItems;

            txtMachineNo.setText(listItems.getMachine_No());
            txtMachineStatus.setText(listItems.getStatus());

            String status = listItems.getStatus();
            if(status.equals("Active")){
                txtMachineNo.setTextColor(v.getResources().getColor(R.color.colorGreen500));
                txtMachineStatus.setTextColor(v.getResources().getColor(R.color.colorGreen500));
            }else{
                txtMachineNo.setTextColor(v.getResources().getColor(R.color.attAbsentMark));
                txtMachineStatus.setTextColor(v.getResources().getColor(R.color.attAbsentMark));
            }
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("News List Not Available");
        }
    }
}