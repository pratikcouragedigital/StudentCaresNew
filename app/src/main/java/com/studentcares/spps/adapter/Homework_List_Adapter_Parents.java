package com.studentcares.spps.adapter;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.studentcares.spps.model.Homework_Items;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import tourguide.tourguide.TourGuide;

public class Homework_List_Adapter_Parents extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Homework_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;
    private TourGuide mTourGuideHandler;
    private Animation mEnterAnimation, mExitAnimation;
    Animation animation;

    ImageView homeworkImage;
    int tooltipCounter=0;


    public Homework_List_Adapter_Parents(List<Homework_Items> yaatraItems) {
        this.listItems = yaatraItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            EmptyViewHolder emptyViewHolder = new EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homework_list_items_p, viewGroup, false);
        viewHolder = new Homework_List_Adapter_Parents.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            Homework_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
        if (listItems.size() > 0) {
            return listItems.size();
        } else {
            return 1;
        }
//        return listItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listItems.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView subjectName;
        public TextView teacherName;
        public TextView dateOfSubmission;
        public TextView dateOfAdded;
        public TextView homeworkTitle;
        //public expandableText homework;
        public TextView homework;


        public View cardView;

        Homework_Items listItems = new Homework_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            subjectName = (TextView) itemView.findViewById(R.id.subjectName);
            teacherName = (TextView) itemView.findViewById(R.id.teacherName);
            dateOfSubmission = (TextView) itemView.findViewById(R.id.dateOfSubmission);
            dateOfAdded = (TextView) itemView.findViewById(R.id.addedDate);
            homeworkTitle = (TextView) itemView.findViewById(R.id.homeworkTitle);
            // homework = (expandableText) itemView.findViewById(R.id.homework);
            homework = (TextView) itemView.findViewById(R.id.homework);
            homeworkImage = (ImageView) itemView.findViewById(R.id.homeworkImage);

            cardView = itemView;
            cardView.setOnClickListener(this);
            homeworkImage.setOnClickListener(this);
        }

        public void bindListDetails(Homework_Items listItems) {
            this.listItems = listItems;
            final ProgressBar imageLoader;
            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);

            String image = listItems.getFirstImagePath();
            if (image == null || image.equals("")) {
                homeworkImage.setImageResource(R.drawable.no_image);
                imageLoader.setVisibility(View.GONE);
            } else {
                RequestOptions options = new RequestOptions().error(R.drawable.no_image);

                Glide.with(homeworkImage.getContext())
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
                        .into(homeworkImage);
            }

            subjectName.setText(listItems.getsubjectName());
            teacherName.setText(listItems.getteacherNames());
            homework.setText(listItems.gethomework());
            homeworkTitle.setText(listItems.gethomeworkTitle());

            String selectedDate = listItems.getsubmissionDate();
            selectedDate = DateFormatter.ChangeDateFormat(selectedDate);

            dateOfSubmission.setText(selectedDate);
            dateOfAdded.setText(listItems.getaddedDate());

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.homeworkImage) {
                String imagetoSave = "Homework";
                Intent i = new Intent(v.getContext(), Full_Image_Activity.class);
                i.putExtra("Image", listItems.getFirstImagePath());
                i.putExtra("ImageFolder", imagetoSave);
                v.getContext().startActivity(i);

            } else if (this.listItems != null) {
//                Intent petFullInformation = new Intent(v.getContext(), P_Homework_Show_Details.class);
//                petFullInformation.putExtra("subjectName", listItems.getsubjectName());
//                petFullInformation.putExtra("Image", listItems.getFirstImagePath());
//                petFullInformation.putExtra("teacherName", listItems.getteacherNames());
//                petFullInformation.putExtra("dateOfSubmission", listItems.getsubmissionDate());
//                petFullInformation.putExtra("dateOfAdded", listItems.getaddedDate());
//                petFullInformation.putExtra("homework", listItems.gethomework());
//                petFullInformation.putExtra("homeworkTitle", listItems.gethomeworkTitle());
//
//                v.getContext().startActivity(petFullInformation);
            }

        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("Homework List Not Available.");
        }
    }
}