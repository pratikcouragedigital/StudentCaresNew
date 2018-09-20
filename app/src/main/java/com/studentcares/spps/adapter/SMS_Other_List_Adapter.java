package com.studentcares.spps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studentcares.spps.R;
import com.studentcares.spps.model.SMS_Inbox_Items;

import java.util.List;

public class SMS_Other_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<SMS_Inbox_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;

    private static final int VIEW_TYPE_EMPTY = 1;

    public SMS_Other_List_Adapter(List<SMS_Inbox_Items> items) {
        this.listItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            SMS_Other_List_Adapter.EmptyViewHolder emptyViewHolder = new SMS_Other_List_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sms_inbox_list_items, viewGroup, false);
        viewHolder = new SMS_Other_List_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof SMS_Other_List_Adapter.ViewHolder) {
            SMS_Other_List_Adapter.ViewHolder vHolder = (SMS_Other_List_Adapter.ViewHolder) viewHolder;
            SMS_Inbox_Items itemOflist = listItems.get(position);
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

        public TextView date;
        public TextView msgBody,txtMsgType;

        public View cardView;

        SMS_Inbox_Items listItems = new SMS_Inbox_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.txtDateTime);
            msgBody = (TextView) itemView.findViewById(R.id.txtSms);
            txtMsgType = (TextView) itemView.findViewById(R.id.txtMsgType);
            cardView = itemView;

        }

        public void bindListDetails(SMS_Inbox_Items listItems) {
            this.listItems = listItems;

            date.setText(listItems.getDate());
            msgBody.setText(listItems.getmsgBody());
            txtMsgType.setText(listItems.getmsgType());

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
            emptyTextView.setText("Sms Not Available");
        }
    }
}