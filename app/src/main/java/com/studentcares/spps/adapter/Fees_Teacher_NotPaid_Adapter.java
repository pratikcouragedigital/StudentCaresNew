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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Fees_Items;
import com.studentcares.spps.singleton.Check_Fees_Student_Send_SMS;

import java.util.ArrayList;
import java.util.List;

import tourguide.tourguide.TourGuide;

public class Fees_Teacher_NotPaid_Adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Fees_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    List<String> studentListArray = new ArrayList<String>();
    List<String> mobileNotArray = new ArrayList<String>();
    List<String> msgArray = new ArrayList<String>();
    FloatingActionButton attendanceCheckFab;
    FloatingActionButton mainFabBtn;
    private static final int VIEW_TYPE_EMPTY = 1;
    private TourGuide mTourGuideHandler;
    int count = 0;
    int chkCount = 0;

    public Fees_Teacher_NotPaid_Adapter(List<Fees_Items> items, FloatingActionButton attendanceCheckFab, FloatingActionButton mainFabBtn) {
        this.listItems = items;
        this.attendanceCheckFab = attendanceCheckFab;
        this.mainFabBtn = mainFabBtn;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            Fees_Teacher_NotPaid_Adapter.EmptyViewHolder emptyViewHolder = new Fees_Teacher_NotPaid_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fees_tab_teacher_not_paid_list_items, viewGroup, false);
        viewHolder = new Fees_Teacher_NotPaid_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Fees_Teacher_NotPaid_Adapter.ViewHolder) {
            Fees_Teacher_NotPaid_Adapter.ViewHolder vHolder = (Fees_Teacher_NotPaid_Adapter.ViewHolder) viewHolder;
            Fees_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
        studentListArray.clear();
        mobileNotArray.clear();
        msgArray.clear();
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
        public TextView txtMonthsName;
        public TextView txtTotalFees;
        public ImageView studentImage;
        public CheckBox chkAttendanceMark;

        public View cardView;
        String studentId;
        String mobileNo;

        Fees_Items listItems = new Fees_Items();
        Check_Fees_Student_Send_SMS check_Fees_Send_SMS;
        int chkStatus = 0;

        public ViewHolder(View itemView) {
            super(itemView);

            studentName = (TextView) itemView.findViewById(R.id.studentName);
            standard = (TextView) itemView.findViewById(R.id.standard);
            division = (TextView) itemView.findViewById(R.id.division);
            txtstudentId = (TextView) itemView.findViewById(R.id.studentId);
            txtMonthsName = (TextView) itemView.findViewById(R.id.txtMonthsName);
            txtTotalFees = (TextView) itemView.findViewById(R.id.txtTotalFees);
            studentImage = (ImageView) itemView.findViewById(R.id.studentImage);
            chkAttendanceMark = (CheckBox) itemView.findViewById(R.id.chkAttendanceMark);
            cardView = itemView;
            chkAttendanceMark.setOnCheckedChangeListener(this);
            check_Fees_Send_SMS = new Check_Fees_Student_Send_SMS();

        }


        public void bindListDetails(Fees_Items listItems) {
            this.listItems = listItems;

            final ProgressBar imageLoader;
            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);

            studentName.setText(listItems.getstudentName());
            standard.setText(listItems.getstandard_Name());
            division.setText(listItems.getdivision_Name());
            txtstudentId.setText(listItems.getstudentId());
            txtMonthsName.setText(listItems.getmonths());
            txtTotalFees.setText(listItems.gettotalFees());

            String image = listItems.getFirstImagePath();
            if (image == null || image.equals("")) {
                studentImage.setImageResource(R.drawable.no_image);
                imageLoader.setVisibility(View.GONE);
            } else {
                RequestOptions options = new RequestOptions().error(R.drawable.no_image);

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

            studentId = listItems.getstudentId();
            mobileNo = listItems.getMobileNo();
            String studentName= listItems.getstudentName();
            String balance= listItems.gettotalFees();
            String monthsNames = listItems.getmonths();

            String months = monthsNames.replaceAll(",", " ");
            String msg = "Dear Parents Most respectfully it is stated that an amount of " +balance+ " Rupees is pending for the month "+months+" of your child "+studentName+". We earnestly request you to kindly settle the payment as early as possible so that your child can smoothly continue his studies.";

            if (isChecked) {
                chkStatus = 1;
                studentListArray.add(studentId);
                mobileNotArray.add(mobileNo);
                msgArray.add(msg);
                check_Fees_Send_SMS.setStudentListInstance(studentListArray);
                check_Fees_Send_SMS.setmobileNoList(mobileNotArray);
                check_Fees_Send_SMS.setmsgList(msgArray);
            } else {
                chkStatus = 0;
                studentListArray.remove(studentId);
                mobileNotArray.remove(mobileNo);
                msgArray.remove(msg);
                check_Fees_Send_SMS.setStudentListInstance(studentListArray);
                check_Fees_Send_SMS.setmobileNoList(mobileNotArray);
                check_Fees_Send_SMS.setmsgList(msgArray);
            }
            if (!studentListArray.isEmpty()) {
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
            emptyTextView.setText("Student list not available for this standard,division.please select another standard & division to get fees not paid student list.");
        }
    }
}
