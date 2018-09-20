package com.studentcares.spps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studentcares.spps.DateFormatter;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Holidays_Items;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Holiday_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Holidays_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;

    public Holiday_Adapter(List<Holidays_Items> items) {
        this.listItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            EmptyViewHolder emptyViewHolder = new EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.holiday_list_items, viewGroup, false);
        viewHolder = new Holiday_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            Holidays_Items itemOflist = listItems.get(position);
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
//
    @Override
    public int getItemViewType(int position) {
        if (listItems.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHolidayName;
        public TextView txtHolidayId;
        public TextView txtFromDate;
        public TextView txtToDate;

        public View cardView;

        Holidays_Items listItems = new Holidays_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            txtHolidayName = (TextView) itemView.findViewById(R.id.holidayName);
            txtFromDate = (TextView) itemView.findViewById(R.id.fromDate);
            txtToDate = (TextView) itemView.findViewById(R.id.toDate);
            cardView = itemView;
        }

        public void bindListDetails(Holidays_Items listItems) {
            this.listItems = listItems;
            txtHolidayName.setText(listItems.getholidayName());

            String selectedDate = listItems.getfromDate();
            selectedDate = DateFormatter.ChangeDateFormat3(selectedDate);
            txtFromDate.setText(selectedDate);

            String selectedDate2 = listItems.gettoDate();
            selectedDate2 = DateFormatter.ChangeDateFormat3(selectedDate2);
            txtToDate.setText(selectedDate2);
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("Holiday List Not Available.");
            emptyTextView.setTextColor(v.getResources().getColor(R.color.colorwhite));
        }
    }
}

