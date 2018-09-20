package com.studentcares.spps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studentcares.spps.R;
import com.studentcares.spps.model.Notification_Items;

import java.util.List;

public class Notification_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Notification_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;

    private static final int VIEW_TYPE_EMPTY = 1;

    public Notification_Adapter(List<Notification_Items> items) {
        this.listItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        if (i == VIEW_TYPE_EMPTY) {
//            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_list_items, viewGroup, false);
//            Notification_Adapter.EmptyViewHolder emptyViewHolder = new Notification_Adapter.EmptyViewHolder(v);
//            return emptyViewHolder;
//        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_list_items, viewGroup, false);
        viewHolder = new Notification_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Notification_Adapter.ViewHolder) {
            Notification_Adapter.ViewHolder vHolder = (Notification_Adapter.ViewHolder) viewHolder;
            Notification_Items itemOflist = listItems.get(position);
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

//    @Override
//    public int getItemViewType(int position) {
//        if (listItems.size() == 0) {
//            return VIEW_TYPE_EMPTY;
//        }
//        return super.getItemViewType(position);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        
        public TextView txtMessage,txtTitle;

        public View cardView;

        Notification_Items listItems = new Notification_Items();

        public ViewHolder(View itemView) {
            super(itemView);
            
            txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            cardView = itemView;

        }

        public void bindListDetails(Notification_Items listItems) {
            this.listItems = listItems;
            
            txtMessage.setText(listItems.getmessage());
            txtTitle.setText(listItems.gettitle());

        }

        @Override
        public void onClick(View v) {

        }
    }

//    public class EmptyViewHolder extends RecyclerView.ViewHolder {
//        public EmptyViewHolder(View v) {
//            super(v);
//            TextView emptyTextView;
//            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
//            emptyTextView.setText("Notification Not Available.");
//        }
//    }
}