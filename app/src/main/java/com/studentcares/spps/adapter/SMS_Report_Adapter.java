package com.studentcares.spps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studentcares.spps.R;
import com.studentcares.spps.model.SMS_Report_Items;

import java.util.ArrayList;
import java.util.List;
import android.widget.Filter;

public class SMS_Report_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<SMS_Report_Items> listFilteredItem;
    List<SMS_Report_Items> listItem;
    View v;
    RecyclerView.ViewHolder viewHolder;

    private static final int VIEW_TYPE_EMPTY = 1;

    public SMS_Report_Adapter(List<SMS_Report_Items> items) {
        this.listFilteredItem = items;
        this.listItem = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            SMS_Report_Adapter.EmptyViewHolder emptyViewHolder = new SMS_Report_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sms_report_items, viewGroup, false);
        viewHolder = new SMS_Report_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof SMS_Report_Adapter.ViewHolder) {
            SMS_Report_Adapter.ViewHolder vHolder = (SMS_Report_Adapter.ViewHolder) viewHolder;
            SMS_Report_Items itemOflist = listFilteredItem.get(position);
            vHolder.bindListDetails(itemOflist );
        }
    }

    @Override
    public int getItemCount() {
        if(listFilteredItem.size() > 0){
            return listFilteredItem.size();
        }else {
            return 1;
        }
//        return listFilteredItem.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listFilteredItem.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtMobile,txtStatus,txtMessage;

        public View cardView;

        SMS_Report_Items listFilteredItem = new SMS_Report_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            txtMobile = (TextView) itemView.findViewById(R.id.txtMobile);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
            cardView = itemView;

        }

        public void bindListDetails(SMS_Report_Items listFilteredItem) {
            this.listFilteredItem = listFilteredItem;

            txtMobile.setText(listFilteredItem.getmobileNo());
            //txtStatus.setText(listFilteredItem.getsmsStatus());
            txtMessage.setText(listFilteredItem.getsms());

            String Status = listFilteredItem.getsmsStatus();
            char first = Status.charAt(0);
            String abc = Status.substring(0,3);
            txtStatus.setText(abc+"");

//            if(listFilteredItem.getsmsStatus().equals("Submitted")){
//                txtMobile.setTextColor(v.getContext().getResources().getColor(R.color.refresh_progress_1));
//                txtStatus.setTextColor(v.getContext().getResources().getColor(R.color.refresh_progress_1));
//                txtMessage.setTextColor(v.getContext().getResources().getColor(R.color.refresh_progress_1));
//            }
//            else if(listFilteredItem.getsmsStatus().equals("Failed")){
//                txtMobile.setTextColor(v.getContext().getResources().getColor(R.color.attAbsentMark));
//                txtStatus.setTextColor(v.getContext().getResources().getColor(R.color.attAbsentMark));
//                txtMessage.setTextColor(v.getContext().getResources().getColor(R.color.attAbsentMark));
//            }
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
            emptyTextView.setText("SMS Report Not Available");
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFilteredItem = listItem;
                } else {
                    List<SMS_Report_Items> filteredList = new ArrayList<>();
                    for (SMS_Report_Items row : listItem) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getsmsStatus().toLowerCase().contains(charString.toLowerCase()) || row.getmobileNo().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    listFilteredItem = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFilteredItem;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFilteredItem = (ArrayList<SMS_Report_Items>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}