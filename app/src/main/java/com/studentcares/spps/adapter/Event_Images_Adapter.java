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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.studentcares.spps.Full_Image_Activity;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Event_List_Items;

import java.util.List;

public class Event_Images_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Event_List_Items> albumImageItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;


    public Event_Images_Adapter(List<Event_List_Items> items) {
        this.albumImageItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        if (i == VIEW_TYPE_EMPTY) {
//            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
//            EmptyViewHolder emptyViewHolder = new EmptyViewHolder(v);
//            return emptyViewHolder;
//        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_event_wise_image_items, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return  viewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            Event_List_Items itemOflist = albumImageItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
//        if(albumImageItems.size() > 0){
//            return albumImageItems.size();
//        }else {
//            return 1;
//        }
        return albumImageItems.size();
    }
//    public int getItemViewType(int position) {
//        if (albumImageItems.size() == 0) {
//            return VIEW_TYPE_EMPTY;
//        }
//        return super.getItemViewType(position);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView albumImages;
        public View cardView;

        Event_List_Items albumImageItems = new Event_List_Items();

        public ViewHolder(View itemView) {
            super(itemView);
            albumImages = (ImageView) itemView.findViewById(R.id.albumImages);
            cardView = itemView;
            albumImages.setOnClickListener(this);
        }

        public void bindListDetails(Event_List_Items albumImageItems) {
            this.albumImageItems = albumImageItems;

            final ProgressBar imageLoader;
            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);

            String image = albumImageItems.getimagePath();
            if (image == null || image.equals("")){
                albumImages.setImageResource(R.drawable.no_image);
                imageLoader.setVisibility(View.GONE);
            }else{
                RequestOptions options = new RequestOptions().centerCrop().error(R.drawable.no_image);

                Glide.with(albumImages.getContext())
                        .asBitmap()
                        .load(albumImageItems.getimagePath())
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
                        .into(new BitmapImageViewTarget(albumImages) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        albumImages.setImageBitmap(resource);
                    }
                });
            }
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.albumImages){
                String imagetoSave="Event";
                Intent i = new Intent(v.getContext(), Full_Image_Activity.class);
                i.putExtra("Image", albumImageItems.getimagePath());
                i.putExtra("ImageFolder", imagetoSave);
                v.getContext().startActivity(i);
            }
        }
    }

//    public class EmptyViewHolder extends RecyclerView.ViewHolder {
//        public EmptyViewHolder(View v) {
//            super(v);
//        }
//    }
}
