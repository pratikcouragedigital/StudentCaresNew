package com.studentcares.spps.adapter;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.studentcares.spps.Gallery_Event_Wise_Images;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Event_List_Items;

import java.util.List;

public class Gallery_Title_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Event_List_Items> galleryAlbumItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;


    public Gallery_Title_List_Adapter(List<Event_List_Items> items) {
        this.galleryAlbumItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            EmptyViewHolder emptyViewHolder = new EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_title_list_items, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            Event_List_Items itemOflist = galleryAlbumItems.get(position);
            vHolder.bindListDetails(itemOflist );
        }
    }

    @Override
    public int getItemCount() {
        if(galleryAlbumItems.size() > 0){
            return galleryAlbumItems.size();
        }else {
            return 1;
        }
//        return galleryAlbumItems.size();
    }
    public int getItemViewType(int position) {
        if (galleryAlbumItems.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final LinearLayout mainRelativeLayout;
        public TextView titleOfAlbum;
        public ImageView imageOfAlbum;
        public View cardView;

        Event_List_Items galleryAlbumItems = new Event_List_Items();

        public ViewHolder(View itemView) {
            super(itemView);
            titleOfAlbum = (TextView) itemView.findViewById(R.id.titleOfAlbum);
            imageOfAlbum = (ImageView) itemView.findViewById(R.id.imageOfAlbum);
            mainRelativeLayout = (LinearLayout) itemView.findViewById(R.id.mainRelativeLayout);
            mainRelativeLayout.setOnClickListener(this);
        }

        public void bindListDetails(Event_List_Items galleryAlbumItems) {
            this.galleryAlbumItems = galleryAlbumItems;

            final ProgressBar imageLoader;
            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);

            String image = galleryAlbumItems.getimagePath();
            if (image == null || image.equals("")){
                imageOfAlbum.setImageResource(R.drawable.no_image);
                imageLoader.setVisibility(View.GONE);
            }else{
                RequestOptions options = new RequestOptions().error(R.drawable.no_image);

                Glide.with(imageOfAlbum.getContext())
                        .asBitmap()
                        .load(galleryAlbumItems.getimagePath())
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
                        .into(new BitmapImageViewTarget(imageOfAlbum) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        imageOfAlbum.setImageBitmap(resource);
                    }
                });
            }
            titleOfAlbum.setText(galleryAlbumItems.geteventName());
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.mainRelativeLayout){
                Intent gotoAlbumImages = new Intent(v.getContext(), Gallery_Event_Wise_Images.class);
                gotoAlbumImages.putExtra("EventId", galleryAlbumItems.geteventId());
                v.getContext().startActivity(gotoAlbumImages);
            }
        }
    }
    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("Event Images Not Available.May be Event Not Created Yet.Or Images Not Added For Event.");
        }
    }
}
