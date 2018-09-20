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
import com.studentcares.spps.expandableText.ExpandableText;
import com.studentcares.spps.model.News_Items;

import java.util.List;

public class News_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<News_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;

    private static final int VIEW_TYPE_EMPTY = 1;

    public News_List_Adapter(List<News_Items> items) {
        this.listItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            EmptyViewHolder emptyViewHolder = new EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_items, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            News_Items itemOflist = listItems.get(position);
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

    @Override
    public int getItemViewType(int position) {
        if (listItems.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView dateOfAdded;
        public TextView title;
        public TextView addedByName;
        public ExpandableText description;
        //public TextView description;
        public ImageView image;
        RelativeLayout imageLayout;

        public View cardView;

        News_Items listItems = new News_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            dateOfAdded = (TextView) itemView.findViewById(R.id.timing);
            title = (TextView) itemView.findViewById(R.id.title);
            addedByName = (TextView) itemView.findViewById(R.id.addedByName);
            description = (ExpandableText) itemView.findViewById(R.id.description);
            //description = (TextView) itemView.findViewById(R.id.description);
            image = (ImageView) itemView.findViewById(R.id.image);
            imageLayout = (RelativeLayout) itemView.findViewById(R.id.imageLayout);
            cardView = itemView;
            image.setOnClickListener(this);
        }

        public void bindListDetails(News_Items listItems) {
            this.listItems = listItems;

            final ProgressBar imageLoader;
            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);

            String imagepath=listItems.getFirstImagePath();
            if (imagepath.equals(" ") || imagepath == null || imagepath.equals("")){
                image.setImageResource(R.drawable.no_image);
                imageLoader.setVisibility(View.GONE);
                imageLayout.setVisibility(View.GONE);
            }
            else{
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

//            image.setImageResource(R.drawable.no_image);

            String selectedDate = listItems.getaddedDate();
            selectedDate = DateFormatter.ChangeDateFormat(selectedDate);

            dateOfAdded.setText(selectedDate);


            description.setText(listItems.getdescription());
            addedByName.setText(listItems.getaddedByName());
            title.setText(listItems.gettitle());
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.image){
                String imagetoSave="News";
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
            emptyTextView.setText("News List Not Available");
        }
    }
}