package com.studentcares.spps.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.studentcares.spps.DateFormatter;
import com.studentcares.spps.Full_Image_Activity;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Notice_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Notice_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Notice_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;

    public Notice_List_Adapter(List<Notice_Items> noticeItems) {
        this.listItems = noticeItems;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            EmptyViewHolder emptyViewHolder = new EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notice_items, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            Notice_Items itemOflist = listItems.get(position);
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
    public int getItemViewType(int position) {
        if (listItems.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView addedByName;
        public TextView dateOfAdded;
        public TextView title;
        //public expandableText description;
        public TextView description;
        public TextView txtNoticeFor;
       // public TextView txtNoticeGroupOrClass;
        public ImageView image;
        RelativeLayout imageLayout,noticeForLayout;
        SessionManager sessionManager;
        String userType;

        public View cardView;

        Notice_Items listItems = new Notice_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            sessionManager = new SessionManager(itemView.getContext());
            HashMap<String, String> typeOfUser = sessionManager.getUserDetails();
            userType = typeOfUser.get(SessionManager.KEY_USERTYPE);

            addedByName = (TextView) itemView.findViewById(R.id.addedByName);
            dateOfAdded = (TextView) itemView.findViewById(R.id.addedDate);
            title = (TextView) itemView.findViewById(R.id.noticeTitle);
            //txtNoticeGroupOrClass = (TextView) itemView.findViewById(R.id.txtNoticeGroupOrClass);

            description = (TextView) itemView.findViewById(R.id.description);
            txtNoticeFor = (TextView) itemView.findViewById(R.id.txtNoticeFor);
            image = (ImageView) itemView.findViewById(R.id.noticeImage);
            imageLayout = (RelativeLayout) itemView.findViewById(R.id.imageLayout);
            noticeForLayout = (RelativeLayout) itemView.findViewById(R.id.noticeForLayout);

            cardView = itemView;
            cardView.setOnClickListener(this);
            image.setOnClickListener(this);

        }

        public void bindListDetails(Notice_Items listItems) {
            this.listItems = listItems;

            addedByName.setText("From: "+listItems.getaddedByName());
            description.setText(listItems.getdescription());
            title.setText(listItems.gettitle());
            txtNoticeFor.setText(listItems.getNoticeDetailsFor());

            String selectedDate = listItems.getaddedDate();
            selectedDate = DateFormatter.ChangeDateFormat(selectedDate);

            dateOfAdded.setText("Date: "+selectedDate);
            if(userType.equals("Student")){
                noticeForLayout.setVisibility(View.GONE);
            }

            final  ProgressBar imageLoader;
            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);

            String imagepath=listItems.getFirstImagePath();
            if (imagepath == null || imagepath.equals(" ")){
                image.setImageResource(R.drawable.no_image);
                imageLoader.setVisibility(View.GONE);
                imageLayout.setVisibility(View.GONE);
            }else{
                RequestOptions options = new RequestOptions().error(R.drawable.no_image);

                Glide.with(image.getContext())
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
                        .into(image);

            }

            String groupOrClass = "";
            String noticeGroup = listItems.getNoticeGroup();
            String std = listItems.getStandardId();
            String div = listItems.getDivisionId();

//            if(noticeGroup.equals("")){
//                groupOrClass = "Class "+std +" Div "+div;
//            }
//            else{
//                groupOrClass = noticeGroup;
//            }
//            if(std.equals("")){
//
//                if(noticeGroup.equals("1,")){
//                    groupOrClass = "Pri-Primary";
//                }
//                else if(noticeGroup.equals("1,2,")){
//                    groupOrClass = "Pri-Primary,Primary";
//                }
//                else if(noticeGroup.equals("1,2,3,")){
//                    groupOrClass = "Pri-Primary,Primary, Secondary";
//                }
//                else if(noticeGroup.equals("1,2,3,4,")){
//                    groupOrClass = "Pri-Primary, Primary, Secondary, Jr.College";
//                }
//            }




            //txtNoticeGroupOrClass.setText(groupOrClass);

        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.noticeImage){
                String imagetoSave="Notice";
                Intent i = new Intent(v.getContext(), Full_Image_Activity.class);
                i.putExtra("Image", listItems.getFirstImagePath());
                i.putExtra("ImageFolder", imagetoSave);
                v.getContext().startActivity(i);
            }
        }
    }
    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("Notice List Not Available");
        }
    }
}