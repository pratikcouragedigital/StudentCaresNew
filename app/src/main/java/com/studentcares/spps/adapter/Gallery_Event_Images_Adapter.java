package com.studentcares.spps.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.studentcares.spps.R;

import java.io.File;
import java.util.List;

public class Gallery_Event_Images_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<File> imgFileList;
    private final FloatingActionButton addEventImagesFab;
    private final FloatingActionButton postEventImagesFab;
    private View v;
    private RecyclerView.ViewHolder viewHolder;

    public Gallery_Event_Images_Adapter(List<File> imgFileList, FloatingActionButton addEventImagesFab, FloatingActionButton postEventImagesFab) {
        this.imgFileList = imgFileList;
        this.addEventImagesFab = addEventImagesFab;
        this.postEventImagesFab = postEventImagesFab;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_event_wise_selected_images, viewGroup, false);
        viewHolder = new Gallery_Event_Images_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Gallery_Event_Images_Adapter.ViewHolder vHolder = (Gallery_Event_Images_Adapter.ViewHolder) viewHolder;
        File imgFile = imgFileList.get(position);
        vHolder.bindListDetails(imgFile);
    }

    @Override
    public int getItemCount() {
        return imgFileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView eventImage;

        public ViewHolder(View itemView) {
            super(itemView);

            eventImage = (ImageView) itemView.findViewById(R.id.eventImage);
        }


        public void bindListDetails(File imgFile) {
            eventImage.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
        }
    }
}
