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
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Attendance_List_Items;
import com.studentcares.spps.singleton.AttendanceCount;
import com.studentcares.spps.singleton.Chk_Mark_Attendance;

import java.util.ArrayList;
import java.util.List;

import tourguide.tourguide.TourGuide;

public class Attendance_Student_Absent_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Attendance_List_Items> listFilteredItem;
    List<Attendance_List_Items> listItem;
    View v;
    RecyclerView.ViewHolder viewHolder;
    List<String> presentListArray = new ArrayList<String>();
    List<String> absentListArray = new ArrayList<String>();
    List<String> allStudentListArray = new ArrayList<String>();
    CounterFab  attendanceCheckFab;
    FloatingActionButton mainFabBtn;
    private static final int VIEW_TYPE_EMPTY = 1;
    private TourGuide mTourGuideHandler;
    int count = 0;
    int selectedStudentCount = 0;
    int chkCount = 0;
    String countP = "";
    AttendanceCount attendanceCount;
    Chk_Mark_Attendance chk_mark_attendance;
    String studentId;

    //    public Attendance_Student_Absent_List_Adapter(List<Attendance_List_Items> items, FloatingActionButton attendanceCheckFab, FloatingActionButton mainFabBtn) {
    public Attendance_Student_Absent_List_Adapter(List<Attendance_List_Items> items, CounterFab attendanceCheckFab) {
        this.listFilteredItem = items;
        this.listItem = items;
        this.attendanceCheckFab = attendanceCheckFab;
//        this.mainFabBtn = mainFabBtn;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            EmptyViewHolder emptyViewHolder = new EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendance_tab_stud_absent_items, viewGroup, false);
        viewHolder = new Attendance_Student_Absent_List_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            Attendance_List_Items itemOflist = listFilteredItem.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
        countP = String.valueOf(listFilteredItem.size());
        attendanceCount.setcountAbsentStudent(countP);
        allStudentListArray.clear();
        //absentListArray.clear();
        //presentListArray.clear();
        if (listFilteredItem.size() > 0) {

            for(int i=0; i < listFilteredItem.size(); i++){
                Attendance_List_Items itemOflist = listFilteredItem.get(i);

                studentId = itemOflist.getstudentId();
                allStudentListArray.add(studentId);
                chk_mark_attendance.setAllStudentListInstance(allStudentListArray);

                absentListArray.add(studentId);
                chk_mark_attendance.setAbsentListInstance(absentListArray);
            }
            return listFilteredItem.size();
        } else {
            return 1;
        }
        //return listFilteredItem.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listFilteredItem.size() == 0) {
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
        public CheckBox chkAttendanceMark;

        public View cardView;
        int attendanceStatus = 1;
        String staffId;
        String status;
        ShowcaseView sv;

        Attendance_List_Items listFilteredItem = new Attendance_List_Items();
        int chkStatus = 0;

        public ViewHolder(View itemView) {
            super(itemView);

            studentName = (TextView) itemView.findViewById(R.id.studentName);
            standard = (TextView) itemView.findViewById(R.id.standard);
            division = (TextView) itemView.findViewById(R.id.division);
            txtstudentId = (TextView) itemView.findViewById(R.id.studentId);
            studentImage = (ImageView) itemView.findViewById(R.id.studentImage);
            chkAttendanceMark = (CheckBox) itemView.findViewById(R.id.chkAttendanceMark);

            cardView = itemView;
            chkAttendanceMark.setOnCheckedChangeListener(this);
            chk_mark_attendance = new Chk_Mark_Attendance();
        }

        public void bindListDetails(final Attendance_List_Items listFilteredItem) {
            this.listFilteredItem = listFilteredItem;

            final ProgressBar imageLoader;
            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);

            studentName.setText(listFilteredItem.getstudentName());
            standard.setText(listFilteredItem.getstandard());
            division.setText(listFilteredItem.getdivision());
            txtstudentId.setText(listFilteredItem.getstudentId());

            status = listFilteredItem.getstatus();
            chk_mark_attendance.setstatus(status);

//            absentListArray = chk_mark_attendance.getAbsentListInstance();
//            int countAbsent = absentListArray.size();
//            if(absentListArray.contains(listFilteredItem.getstudentId())){
//                listFilteredItem.setSelectedCheckBox(true);
//
//            }else{
//                listFilteredItem.setSelectedCheckBox(false);
//            }


            if(status.equals("A")){
                chkAttendanceMark.setVisibility(View.GONE);
            }

            String image = listFilteredItem.getFirstImagePath();
            if (image == null || image.equals("")) {
                studentImage.setImageResource(R.drawable.student);
                imageLoader.setVisibility(View.GONE);
            } else {
                RequestOptions options = new RequestOptions().error(R.drawable.student);

                Glide.with(studentImage.getContext())
                        .asBitmap()
                        .load(listFilteredItem.getFirstImagePath())
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
            chkAttendanceMark.setOnCheckedChangeListener(null);
            chkAttendanceMark.setChecked(listFilteredItem.getSelectedCheckBox());
            chkAttendanceMark.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            listFilteredItem.setSelectedCheckBox(isChecked);
            studentId = listFilteredItem.getstudentId();
            attendanceCheckFab.setVisibility(View.VISIBLE);

            if (isChecked) {
                chkStatus = 1;
                selectedStudentCount = selectedStudentCount + 1;
//                Toast.makeText(v.getContext(), "Selected Student Count: " + selectedStudentCount, Toast.LENGTH_SHORT).show();
                attendanceCheckFab.setCount(selectedStudentCount);

                absentListArray.remove(studentId);
                chk_mark_attendance.setAbsentListInstance(absentListArray);

                presentListArray.add(studentId);
                chk_mark_attendance.setPresentListInstance(presentListArray);

            } else {
                chkStatus = 0;
                selectedStudentCount = selectedStudentCount - 1;
//                Toast.makeText(v.getContext(), "Selected Student Count: " + selectedStudentCount, Toast.LENGTH_SHORT).show();
                attendanceCheckFab.setCount(selectedStudentCount);
                presentListArray.remove(studentId);
                chk_mark_attendance.setPresentListInstance(presentListArray);

                absentListArray.add(studentId);
                chk_mark_attendance.setAbsentListInstance(absentListArray);
            }
            if(!presentListArray.isEmpty()) {
//                mainFabBtn.setVisibility(View.GONE);
                attendanceCheckFab.setVisibility(View.VISIBLE);
            }
            else {
//                 mainFabBtn.setVisibility(View.VISIBLE);
                attendanceCheckFab.setVisibility(View.GONE);
            }
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("Student list not available for this standard,division.please select another standard & division to get absent student list.");
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
                    List<Attendance_List_Items> filteredList = new ArrayList<>();
                    for (Attendance_List_Items row : listItem) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getstudentName().toLowerCase().contains(charString.toLowerCase()) || row.getstudentId().contains(charSequence)) {
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
                listFilteredItem = (ArrayList<Attendance_List_Items>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

