package com.studentcares.spps.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.studentcares.spps.Full_Image_Activity;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Ranker_List_Items;
import com.studentcares.spps.model.Rankers_Items;

import java.util.List;

import io.reactivex.annotations.Nullable;

public class Ranker_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Ranker_List_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;

    private static final int VIEW_TYPE_EMPTY = 1;
    int adpaterPos ;

    public Ranker_List_Adapter(List<Ranker_List_Items> items) {
        this.listItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            Ranker_List_Adapter.EmptyViewHolder emptyViewHolder = new Ranker_List_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ranker_list_items, viewGroup, false);
        viewHolder = new Ranker_List_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof Ranker_List_Adapter.ViewHolder) {
            adpaterPos = position;
            Ranker_List_Adapter.ViewHolder vHolder = (Ranker_List_Adapter.ViewHolder) viewHolder;
            Ranker_List_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist);
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

        public TextView txtStudentName1,txtPercent,txtGrade;
        public TextView txtStudentId;
        public TextView txtStudent1Marks;
        public ImageView image1,imgRank;

        public View cardView;

        Ranker_List_Items listItems = new Ranker_List_Items();

        public ViewHolder(View itemView) {
            super(itemView);

//          txtStudentId = (TextView) itemView.findViewById(R.id.txtStudentId);
            txtStudent1Marks = (TextView) itemView.findViewById(R.id.txtStudent1Marks);
            txtStudentName1 = (TextView) itemView.findViewById(R.id.txtStudentName1);
            txtPercent = (TextView) itemView.findViewById(R.id.txtPercent);
            txtGrade = (TextView) itemView.findViewById(R.id.txtGrade);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            imgRank = (ImageView) itemView.findViewById(R.id.imgRank);

            cardView = itemView;
            image1.setOnClickListener(this);
        }

        public void bindListDetails(Ranker_List_Items listItems) {
            this.listItems = listItems;

            int rank = Integer.parseInt(listItems.getrank());
            if(rank == 1){
                imgRank.setImageResource(R.drawable.rank_1);
            }
            else if(rank == 2){
                imgRank.setImageResource(R.drawable.rank_2);
            }
            else if(rank == 3){
                imgRank.setImageResource(R.drawable.rank_3);
            }
             else if(rank == 4){
                imgRank.setImageResource(R.drawable.rank_4);
            }
            else if(rank == 5){
                imgRank.setImageResource(R.drawable.rank_5);
            }

            final ProgressBar image1Loader;
            image1Loader = (ProgressBar) itemView.findViewById(R.id.loading);

            String image1path=listItems.getFirstImagePath();
            if ( image1path == null || image1path.equals("") || image1path.equals(" ")){
                image1.setImageResource(R.drawable.student);
                image1Loader.setVisibility(View.GONE);
            }
            else{
                RequestOptions options = new RequestOptions().error(R.drawable.student);

                Glide.with(image1.getContext())
                        .asBitmap()
                        .load(listItems.getFirstImagePath())
                        .apply(options)
                        .listener(new RequestListener<Bitmap>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                image1Loader.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                image1Loader.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(image1);
            }
//            txtStudentId.setText(listItems.getstudentId());
            txtStudentName1.setText(listItems.getstudentName());

            String marks = "";
            String obtMarks = listItems.getobtainedMarks();
            String outMarks = listItems.getoutOffMarks();

            if(obtMarks != null){
                if(outMarks.equals("")){
                    marks = obtMarks;
                    txtStudent1Marks.setText(marks);
                }
                else{
                    marks = obtMarks +" / "+outMarks;
                    txtStudent1Marks.setText("Marks: "+marks);
                }
            }
            else{
                txtStudent1Marks.setVisibility(View.GONE);
            }

            String grade =  listItems.getgrade();
            if(grade != null){
                txtGrade.setText("Grade: "+grade);
            }else{
                txtGrade.setVisibility(View.GONE);
            }

            String percent =  listItems.getpercent();
            if(percent != null){
                txtPercent.setText("Percentage: "+percent +" %");
            }else{
                txtPercent.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
//            if(v.getId() == R.id.image1){
//                String image1toSave="Ranker";
//                Intent i = new Intent(v.getContext(), Full_Image_Activity.class);
//                i.putExtra("Image", listItems.getFirstImagePath());
//                i.putExtra("ImageFolder", image1toSave);
//                v.getContext().startActivity(i);
//            }
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("Ranker List Not Available");
        }
    }
}