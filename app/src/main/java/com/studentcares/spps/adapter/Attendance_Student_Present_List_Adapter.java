package com.studentcares.spps.adapter;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Attendance_Student_Present_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Attendance_List_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    List<String> absentListArray = new ArrayList<String>();
    FloatingActionButton attendanceCheckFab;
    FloatingActionButton mainFabBtn;
    private static final int VIEW_TYPE_EMPTY = 1;
    private TourGuide mTourGuideHandler;
    int count = 0;
    int chkCount = 0;
    int selectedStudentCount = 0;
    String countP = "";
    AttendanceCount attendanceCount;

    public Attendance_Student_Present_List_Adapter(List<Attendance_List_Items> items, FloatingActionButton attendanceCheckFab, FloatingActionButton mainFabBtn) {
        this.listItems = items;
        this.attendanceCheckFab = attendanceCheckFab;
        this.mainFabBtn = mainFabBtn;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            Attendance_Student_Present_List_Adapter.EmptyViewHolder emptyViewHolder = new Attendance_Student_Present_List_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendance_tab_stud_present_items, viewGroup, false);
        viewHolder = new Attendance_Student_Present_List_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Attendance_Student_Present_List_Adapter.ViewHolder) {
            Attendance_Student_Present_List_Adapter.ViewHolder vHolder = (Attendance_Student_Present_List_Adapter.ViewHolder) viewHolder;
            Attendance_List_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
        countP = String.valueOf(listItems.size());
        attendanceCount.setcountPresentStudent(countP);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        public TextView studentName;
        public TextView standard;
        public TextView division;
        public TextView txtstudentId;
        public ImageView studentImage;
        public TextView txtInTime;
        public TextView txtOutTime;
//      public CheckBox chkAttendanceMark;

        public View cardView;
        String studentId;

        Attendance_List_Items listItems = new Attendance_List_Items();
        Chk_Mark_Attendance chk_mark_attendance;
        int chkStatus = 0;

        public ViewHolder(View itemView) {
            super(itemView);

            studentName = (TextView) itemView.findViewById(R.id.studentName);
            standard = (TextView) itemView.findViewById(R.id.standard);
            division = (TextView) itemView.findViewById(R.id.division);
            txtInTime = (TextView) itemView.findViewById(R.id.txtInTime);
            txtOutTime = (TextView) itemView.findViewById(R.id.txtOutTime);
            txtstudentId = (TextView) itemView.findViewById(R.id.studentId);
            studentImage = (ImageView) itemView.findViewById(R.id.studentImage);
//          chkAttendanceMark = (CheckBox) itemView.findViewById(R.id.chkAttendanceMark);
            cardView = itemView;
//
        }

        public void bindListDetails(final Attendance_List_Items listItems) {
            this.listItems = listItems;

            final ProgressBar imageLoader;
            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);

            studentName.setText(listItems.getstudentName());
            standard.setText(listItems.getstandard());
            division.setText(listItems.getdivision());
            txtstudentId.setText(listItems.getstudentId());

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
                studentImage.setImageResource(R.drawable.student);
                imageLoader.setVisibility(View.GONE);
            } else {
                RequestOptions options = new RequestOptions().error(R.drawable.student);

                Glide.with(studentImage.getContext())
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
                        .into((studentImage));

            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            listItems.setSelectedCheckBox(isChecked);
            studentId = listItems.getstudentId();
            if (isChecked) {
                chkStatus = 1;
                selectedStudentCount = selectedStudentCount + 1;
                Toast.makeText(v.getContext(), "Selected Student Count: " + selectedStudentCount, Toast.LENGTH_SHORT).show();
                absentListArray.add(studentId);
                chk_mark_attendance.setAbsentListInstance(absentListArray);

            } else {
                chkStatus = 0;
                selectedStudentCount = selectedStudentCount - 1;
                Toast.makeText(v.getContext(), "Selected Student Count: " + selectedStudentCount, Toast.LENGTH_SHORT).show();
                absentListArray.remove(studentId);
                chk_mark_attendance.setAbsentListInstance(absentListArray);

            }
            if (!absentListArray.isEmpty()) {
                mainFabBtn.setVisibility(View.GONE);
                attendanceCheckFab.setVisibility(View.VISIBLE);

            } else {
                mainFabBtn.setVisibility(View.VISIBLE);
                attendanceCheckFab.setVisibility(View.GONE);
            }
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("Student List Not Available for this Standard,Division.Please Select Another Standard & Division to get present student list.");
        }
    }
}

