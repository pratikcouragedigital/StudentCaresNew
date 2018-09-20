package com.studentcares.spps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.studentcares.spps.R;
import com.studentcares.spps.model.DashBoard_Count_Items;

import java.util.List;

public class DashBoard_Count_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<DashBoard_Count_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    String schoolId,userType,userId;

    public DashBoard_Count_Adapter(List<DashBoard_Count_Items> noticeItems) {
        this.listItems = noticeItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_count_items, viewGroup, false);
        viewHolder = new DashBoard_Count_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof DashBoard_Count_Adapter.ViewHolder) {
            DashBoard_Count_Adapter.ViewHolder vHolder = (DashBoard_Count_Adapter.ViewHolder) viewHolder;
            DashBoard_Count_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist );
        }
    }

    @Override
    public int getItemCount() {

        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtGroupName;
        public TextView txtTotalStudent;
        public TextView txtPresentStudent;
        public TextView txtAbsentStudent;
        public TextView txtNotPunch;
        RelativeLayout mainRelativeLayout;


        int absent,present,total,notPunched;
        String groupName,groupID;

        public View cardView;
        DashBoard_Count_Items listItems = new DashBoard_Count_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            mainRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.mainRelativeLayout);
            txtGroupName = (TextView) itemView.findViewById(R.id.txtGroupName);
            txtTotalStudent = (TextView) itemView.findViewById(R.id.txtTotal);
            txtPresentStudent = (TextView) itemView.findViewById(R.id.txtPresent);
            txtAbsentStudent = (TextView) itemView.findViewById(R.id.txtAbsent);
            txtNotPunch = (TextView) itemView.findViewById(R.id.txtNotPunch);

            cardView = itemView;
            cardView.setOnClickListener(this);
            mainRelativeLayout.setOnClickListener(this);


        }

        public void bindListDetails(DashBoard_Count_Items listItems) {
            this.listItems = listItems;

            txtGroupName.setText(listItems.getgroupName());
            txtTotalStudent.setText(listItems.gettotalStudent());
            txtPresentStudent.setText(listItems.getpresentStudent());
            txtAbsentStudent.setText(listItems.getabsentStudent());
            txtNotPunch.setText(listItems.getnotPunchStudent());

//            groupName = listItems.getgroupName();
//            groupID = listItems.getgroupId();
//            total = Integer.parseInt(listItems.gettotalStudent());
//            notPunched = Integer.parseInt(listItems.getnotPunchStudent());
//            present = Integer.parseInt(listItems.getpresentStudent());
//            absent = Integer.parseInt(listItems.getabsentStudent());

        }

        @Override
        public void onClick(View v) {

        }
    }

}