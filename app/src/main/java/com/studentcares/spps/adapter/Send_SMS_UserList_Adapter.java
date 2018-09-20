package com.studentcares.spps.adapter;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.studentcares.spps.BaseActivity;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Attendance_List_Items;
import com.studentcares.spps.model.Send_SMS_User_Items;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.singleton.Send_SMS_Check_User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tourguide.tourguide.TourGuide;

public class Send_SMS_UserList_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Send_SMS_User_Items> listFilteredItem;
    List<Send_SMS_User_Items> listItem;
    View v;
    RecyclerView.ViewHolder viewHolder;
    List<String> selectedUserNoArray = new ArrayList<String>();
    List<String> selectedUserIdArray = new ArrayList<String>();
    CounterFab sendSMSCheckFab,selectAllUserFab;
    
    private static final int VIEW_TYPE_EMPTY = 1;

    private TourGuide mTourGuideHandler;
    int count=0;
    int chkCount=0;
    int selectedIdCount = 0;

    public Send_SMS_UserList_Adapter(List<Send_SMS_User_Items> items, CounterFab sendSMSCheckFab, CounterFab selectAllUserFab) {
        this.listFilteredItem = items;
        this.listItem = items;
        this.sendSMSCheckFab = sendSMSCheckFab;
        this.selectAllUserFab = selectAllUserFab;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            Send_SMS_UserList_Adapter.EmptyViewHolder emptyViewHolder = new Send_SMS_UserList_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sms_send_user_list_items, viewGroup, false);
        viewHolder = new Send_SMS_UserList_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof Send_SMS_UserList_Adapter.ViewHolder) {
            Send_SMS_UserList_Adapter.ViewHolder vHolder = (Send_SMS_UserList_Adapter.ViewHolder) viewHolder;
            Send_SMS_User_Items itemOflist = listFilteredItem.get(position);
            vHolder.bindListDetails(itemOflist );
        }
    }

    @Override
    public int getItemCount() {
//        selectedUserIdArray.clear();
//        selectedUserNoArray.clear();
        if(listFilteredItem.size() > 0){
            return listFilteredItem.size();
        }else {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{

        public TextView studentName;
        public TextView standard;
        public TextView division;
        public TextView txtstudentId;
        public ImageView studentImage;
        public CheckBox chkAttendanceMark;

        public View cardView;
        int SmsStatus = 1;
        String studentId;
        String mobileNo;
        String schoolId,userType;

        Send_SMS_User_Items listFilteredItem = new Send_SMS_User_Items();
        Send_SMS_Check_User sendSMSCheck_User;
        int chkStatus = 0;

        public ViewHolder(View itemView) {
            super(itemView);

            SessionManager sessionManagerNgo = new SessionManager(v.getContext());
            HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
            userType = typeOfUser.get(SessionManager.KEY_USERTYPE);


            studentName = (TextView) itemView.findViewById(R.id.studentName);
//            standard = (TextView) itemView.findViewById(R.id.standard);
//            division = (TextView) itemView.findViewById(R.id.division);
            txtstudentId = (TextView) itemView.findViewById(R.id.studentId);
            studentImage = (ImageView) itemView.findViewById(R.id.studentImage);
            chkAttendanceMark = (CheckBox) itemView.findViewById(R.id.chkAttendanceMark);

            cardView = itemView;
            chkAttendanceMark.setOnCheckedChangeListener(this);
            sendSMSCheck_User = new Send_SMS_Check_User();

        }

        public void bindListDetails(Send_SMS_User_Items listFilteredItem) {
            this.listFilteredItem = listFilteredItem;

            final ProgressBar imageLoader;
            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);

            studentName.setText(listFilteredItem.getName());
//            standard.setText(listFilteredItem.getstandard());
//            division.setText(listFilteredItem.getdivision());
            txtstudentId.setText(listFilteredItem.getId());

            selectedUserNoArray = sendSMSCheck_User.getNoList();
            selectedUserIdArray = sendSMSCheck_User.getIdList();

            int count = selectedUserIdArray.size();
            if(selectedUserIdArray.contains(listFilteredItem.getId())){
                listFilteredItem.setSelectedCheckBox(true);
                sendSMSCheckFab.setCount(count);
            }else{
                selectedUserIdArray.clear();
                selectedUserNoArray.clear();
                selectedIdCount = 0;
                listFilteredItem.setSelectedCheckBox(false);
                sendSMSCheckFab.setCount(count);
            }

            String image=listFilteredItem.getFirstImagePath();
            if (image == null || image.equals("")){
                if(userType.equals("Student")){
                    studentImage.setImageResource(R.drawable.student);
                }
                else {
                    studentImage.setImageResource(R.drawable.user_logo);
                }
                imageLoader.setVisibility(View.GONE);
            }
            else {
                RequestOptions options;
                if(userType.equals("Student")){
                    options = new RequestOptions().centerCrop().error(R.drawable.student);
                }
                else {
                    options = new RequestOptions().centerCrop().error(R.drawable.user_logo);
                }

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
                        .into(new BitmapImageViewTarget(studentImage) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(studentImage.getContext().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                studentImage.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            }
            chkAttendanceMark.setOnCheckedChangeListener(null);
            chkAttendanceMark.setChecked(listFilteredItem.getSelectedCheckBox());
            chkAttendanceMark.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            listFilteredItem.setSelectedCheckBox(isChecked);
            studentId = listFilteredItem.getId();
            mobileNo = listFilteredItem.getmobileNo();

            if(isChecked) {
                chkStatus = 1;
                selectedIdCount = selectedIdCount + 1;
                selectedUserNoArray.add(mobileNo);
                selectedUserIdArray.add(studentId);
                sendSMSCheckFab.setCount(selectedIdCount);
                sendSMSCheck_User.setNoList(selectedUserNoArray);
                sendSMSCheck_User.setIdList(selectedUserIdArray);
            }
            else {
                chkStatus = 0;
                selectedIdCount = selectedIdCount - 1;
                selectedUserNoArray.remove(mobileNo);
                selectedUserIdArray.remove(studentId);
                sendSMSCheckFab.setCount(selectedIdCount);
                sendSMSCheck_User.setNoList(selectedUserNoArray);
                sendSMSCheck_User.setIdList(selectedUserIdArray);
            }

            if(!selectedUserNoArray.isEmpty()) {
                sendSMSCheckFab.setVisibility(View.VISIBLE);
                selectAllUserFab.setVisibility(View.VISIBLE);
            }
            else {
                sendSMSCheckFab.setVisibility(View.GONE);
                selectAllUserFab.setVisibility(View.GONE);
                Toast.makeText(v.getContext(), "If You Want To Sent Then Please Select At least One Member. ", Toast.LENGTH_SHORT).show();
            }

//            if(!selectedUserNoArray.isEmpty()) {
//                sendSMSCheckFab.setVisibility(View.VISIBLE);
//            }
//            else {
//                sendSMSCheckFab.setVisibility(View.GONE);
//            }
        }
    }
    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("Student list not available for this standard & division. You can change standard & division b clicking on the below button.");
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
                    List<Send_SMS_User_Items> filteredList = new ArrayList<>();
                    for (Send_SMS_User_Items row : listItem) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getId().contains(charSequence)) {
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
                listFilteredItem = (ArrayList<Send_SMS_User_Items>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

