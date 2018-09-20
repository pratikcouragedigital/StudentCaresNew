package com.studentcares.spps.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.ImageButton;
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
import com.studentcares.spps.Full_Image_Activity;
import com.studentcares.spps.R;
import com.studentcares.spps.model.PTA_Member_Items;
import com.studentcares.spps.singleton.Check_Student_Staff_For_Sms;

import java.util.ArrayList;
import java.util.List;

import tourguide.tourguide.TourGuide;

public class PTA_Mem_Adapter_Primary extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<PTA_Member_Items> listFilteredItem;
    List<PTA_Member_Items> listItem;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;
    List<String> ptaMemMobileListArray = new ArrayList<String>();
    List<String> ptaMemIdsListArray = new ArrayList<String>();
//    FloatingActionButton fabSendMsg_Notification;
    CounterFab fabCounter;
    private TourGuide mTourGuideHandler;
    int count=0;
    int selectedIdCount = 0;

    public PTA_Mem_Adapter_Primary(List<PTA_Member_Items> items, CounterFab fabCounter) {
        this.listFilteredItem = items;
        this.listItem = items;
//        this.fabSendMsg_Notification = fabSendMsg_Notification;
        this.fabCounter = fabCounter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            EmptyViewHolder emptyViewHolder = new EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pta_members_list_items, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            PTA_Member_Items itemOflist = listFilteredItem.get(position);
            vHolder.bindListDetails(itemOflist );
        }
    }

    @Override
    public int getItemCount() {
//        ptaMemMobileListArray.clear();
//        ptaMemIdsListArray.clear();
        if(listFilteredItem.size() > 0){
            return listFilteredItem.size();
        }else {
            return 1;
        }
//        return listFilteredItem.size();
    }
    public int getItemViewType(int position) {
        if (listFilteredItem.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        public TextView name;
        public TextView contactNo;
        public ImageView image;
        public ImageButton addAttendanceBtn;
        public TextView standard;
        public TextView division;
        public TextView address;
        public CheckBox chk_send_sms;

        int chkStatus = 0;
        int SmsStatus = 1;
        String studentId;
        String mobileNo;

        public View cardView;
        Check_Student_Staff_For_Sms check_Student_Staff_For_Sms;

        PTA_Member_Items listFilteredItem = new PTA_Member_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            contactNo = (TextView) itemView.findViewById(R.id.contactNo);
            image = (ImageView) itemView.findViewById(R.id.image);
            standard = (TextView) itemView.findViewById(R.id.standard);
            division = (TextView) itemView.findViewById(R.id.division);
            address = (TextView) itemView.findViewById(R.id.address);
            chk_send_sms = (CheckBox) itemView.findViewById(R.id.chk_send_sms);

            cardView = itemView;
            image.setOnClickListener(this);

            chk_send_sms.setOnCheckedChangeListener(this);
            check_Student_Staff_For_Sms = new Check_Student_Staff_For_Sms();
        }

        public void bindListDetails(PTA_Member_Items listFilteredItem) {
            this.listFilteredItem = listFilteredItem;

            final ProgressBar imageLoader;
            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);
            String imagepath=listFilteredItem.getstudentImagePath();
            if (imagepath == null || imagepath.equals("")){
                image.setImageResource(R.drawable.student);
                imageLoader.setVisibility(View.GONE);
            }
            else{
                RequestOptions options = new RequestOptions().centerCrop().error(R.drawable.student);

                Glide.with(image.getContext())
                        .asBitmap()
                        .load(listFilteredItem.getstudentImagePath())
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
                        .into(new BitmapImageViewTarget(image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(image.getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        image.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }

            //image.setImageResource(R.drawable.user_logo);
            name.setText(listFilteredItem.getname());
            contactNo.setText(listFilteredItem.getcontactNo());
            standard.setText("Std: "+listFilteredItem.getstandard());
            division.setText("Div: "+listFilteredItem.getdivision());
            address.setText(listFilteredItem.getAddress());

            chk_send_sms.setOnCheckedChangeListener(null);
            chk_send_sms.setChecked(listFilteredItem.getSelectedCheckBox());
            chk_send_sms.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.image){
                String imagetoSave="Profile";
                Intent i = new Intent(v.getContext(), Full_Image_Activity.class);
                i.putExtra("Image", listFilteredItem.getstudentImagePath());
                i.putExtra("ImageFolder", imagetoSave);
                v.getContext().startActivity(i);
            }
            else if(v.getId() == R.id.contactNo){
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + listFilteredItem.getcontactNo()));
                v.getContext().startActivity(callIntent);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            listFilteredItem.setSelectedCheckBox(isChecked);
            studentId = listFilteredItem.getstudentId();
            mobileNo = listFilteredItem.getcontactNo();
//            fabSendMsg_Notification.setVisibility(View.VISIBLE);
            fabCounter.setVisibility(View.VISIBLE);

            if(isChecked) {
                chkStatus = 1;
                selectedIdCount = selectedIdCount + 1;
                ptaMemMobileListArray.add(mobileNo);
                ptaMemIdsListArray.add(studentId);
                fabCounter.setCount(selectedIdCount);
                check_Student_Staff_For_Sms.setStudentListInstance(ptaMemIdsListArray);
                check_Student_Staff_For_Sms.setPtaMobileListInstance(ptaMemMobileListArray);
            }
            else {
                chkStatus = 0;
                selectedIdCount = selectedIdCount - 1;
                ptaMemMobileListArray.remove(mobileNo);
                ptaMemIdsListArray.remove(studentId);
                fabCounter.setCount(selectedIdCount);
                check_Student_Staff_For_Sms.setStudentListInstance(ptaMemIdsListArray);
                check_Student_Staff_For_Sms.setPtaMobileListInstance(ptaMemMobileListArray);
            }
            if(!ptaMemMobileListArray.isEmpty()) {
//                fabSendMsg_Notification.setVisibility(View.VISIBLE);
                fabCounter.setVisibility(View.VISIBLE);

            }
            else {
//                fabSendMsg_Notification.setVisibility(View.GONE);
                fabCounter.setVisibility(View.GONE);
                Toast.makeText(v.getContext(), "If You Want To Sent SMS_InBox_Tab_Attendance Then Please Select At least One Member. ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("PTA Member Not Available.");
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
                    List<PTA_Member_Items> filteredList = new ArrayList<>();
                    for (PTA_Member_Items row : listItem) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getname().toLowerCase().contains(charString.toLowerCase()) || row.getstudentId().contains(charSequence)) {
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
                listFilteredItem = (ArrayList<PTA_Member_Items>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

