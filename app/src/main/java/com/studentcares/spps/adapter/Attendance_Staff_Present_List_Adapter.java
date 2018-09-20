package com.studentcares.spps.adapter;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Attendance_List_Items;
import com.studentcares.spps.singleton.AttendanceCount;
import com.studentcares.spps.singleton.Chk_Mark_Attendance;
import java.util.ArrayList;
import java.util.List;

import tourguide.tourguide.TourGuide;

public class Attendance_Staff_Present_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Attendance_List_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;

    List<String> absentStaffListArray = new ArrayList<String>();
    FloatingActionButton attendanceStaffCheckFab;

    private TourGuide mTourGuideHandler;
    int count=0;
    int chkCount=0;
    int selectedStudentCount=0;
    String countP="";
    AttendanceCount attendanceCount;

    public Attendance_Staff_Present_List_Adapter(List<Attendance_List_Items> items, FloatingActionButton attendanceStaffCheckFab) {
        this.listItems = items;
        this.attendanceStaffCheckFab = attendanceStaffCheckFab;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            Attendance_Staff_Present_List_Adapter.EmptyViewHolder emptyViewHolder = new Attendance_Staff_Present_List_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendance_tab_staff_present_items, viewGroup, false);
        viewHolder = new Attendance_Staff_Present_List_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Attendance_Staff_Present_List_Adapter.ViewHolder) {
            Attendance_Staff_Present_List_Adapter.ViewHolder vHolder = (Attendance_Staff_Present_List_Adapter.ViewHolder) viewHolder;
            Attendance_List_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
        countP = String.valueOf(listItems.size());
        attendanceCount.setcountPresent_Staff(countP);
        absentStaffListArray.clear();
        if (listItems.size() > 0) {
            return listItems.size();
        } else {
            return 1;
        }
        //return listItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listItems.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

        public TextView txtStaffName;
        public TextView txtStudentId;
        public ImageView satffImage;
        public CheckBox addAttendanceBtn;
        public TextView standard;
        public TextView division;
        public TextView txtInTime;
        public TextView txtOutTime;
        FloatingActionButton attendanceCheckFab;

        public View cardView;
        int attendanceStatus = 1;
        String staffIdForAttendance;
        String userId;
        String schoolId;
        List<String> absentListArray = new ArrayList<String>();


        Attendance_List_Items listItems = new Attendance_List_Items();
        Chk_Mark_Attendance chk_mark_attendance;
        int chkStatus = 0;

        public ViewHolder(View itemView) {
            super(itemView);

            txtStaffName = (TextView) itemView.findViewById(R.id.staffName);
            txtStudentId = (TextView) itemView.findViewById(R.id.staffId);
            txtInTime = (TextView) itemView.findViewById(R.id.txtInTime);
            txtOutTime = (TextView) itemView.findViewById(R.id.txtOutTime);
            satffImage = (ImageView) itemView.findViewById(R.id.staffImage);
            cardView = itemView;
        }

        public void bindListDetails(final Attendance_List_Items listItems) {
            this.listItems = listItems;

            final ProgressBar imageLoader;
            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);

            txtStaffName.setText(listItems.getstaffName());
            txtStudentId.setText(listItems.getstaffId());

            String inTime = listItems.getinTime();
            String outTime = listItems.getoutTime();

            if (inTime.equals("")) {
                txtInTime.setVisibility(View.GONE);
            }
            else{
                txtInTime.setText("In Time :"+listItems.getinTime());
            }
            if (outTime.equals("")) {
                txtOutTime.setVisibility(View.GONE);
            }
            else{
                txtOutTime.setText("Out Time :"+listItems.getoutTime());
            }

            String image = listItems.getFirstImagePath();
            if (image == null || image.equals("")) {
                satffImage.setImageResource(R.drawable.user_logo);
                imageLoader.setVisibility(View.GONE);
            } else {
                RequestOptions options = new RequestOptions().error(R.drawable.user_logo);

                Glide.with(satffImage.getContext())
                        .asBitmap()
                        .load(listItems.getFirstImagePath())
                        .apply(options)
                        .listener(new RequestListener<Bitmap>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                imageLoader.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                imageLoader.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into((satffImage));

            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            listItems.setSelectedCheckBox(isChecked);
            staffIdForAttendance = listItems.getstaffId();
            if (isChecked) {
                chkStatus = 1;
                selectedStudentCount = selectedStudentCount + 1;
                Toast.makeText(v.getContext(), "Selected Staff Count: " + selectedStudentCount , Toast.LENGTH_SHORT).show();
                absentStaffListArray.add(staffIdForAttendance);
                chk_mark_attendance.setAbsentStaffListInstance(absentStaffListArray);
            } else {
                chkStatus = 0;
                selectedStudentCount = selectedStudentCount - 1;
                Toast.makeText(v.getContext(), "Selected Staff Count: " + selectedStudentCount , Toast.LENGTH_SHORT).show();
                absentStaffListArray.remove(staffIdForAttendance);
                chk_mark_attendance.setAbsentStaffListInstance(absentStaffListArray);
            }
            if (!absentStaffListArray.isEmpty()) {
                attendanceStaffCheckFab.setVisibility(View.VISIBLE);
            } else {
                attendanceStaffCheckFab.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.staffImage){
                //Dynamic Spinner

            }
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("Today's Present Staff List Not Available.");
        }
    }
}
