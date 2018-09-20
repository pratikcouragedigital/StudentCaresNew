package com.studentcares.spps.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.studentcares.spps.Full_Image_Activity;
import com.studentcares.spps.R;
import com.studentcares.spps.expandableText.ExpandableText;
import com.studentcares.spps.model.HomeGrid_Items;
import com.studentcares.spps.model.Rankers_Items;
import com.studentcares.spps.model.Rankers_Items;

import java.util.ArrayList;
import java.util.List;

public class Ranker_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Rankers_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;

    private static final int VIEW_TYPE_EMPTY = 1;

    public Ranker_Adapter(List<Rankers_Items> items) {
        this.listItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            EmptyViewHolder emptyViewHolder = new EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ranker_stdwise_list, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Ranker_Adapter.ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            Rankers_Items itemOflist = listItems.get(position);

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

        public TextView txtStd,txtDiv;
        RecyclerView recyclerView;
        RecyclerView.Adapter adapter;
        private GridLayoutManager gridLayoutManager;
        List<Rankers_Items> rankerListItems = new ArrayList<Rankers_Items>();
        public View cardView;
        Rankers_Items listItems = new Rankers_Items();

        public ViewHolder(View itemView) {
            super(itemView);
            txtStd = (TextView) itemView.findViewById(R.id.txtStd);
            txtDiv = (TextView) itemView.findViewById(R.id.txtDiv);
            cardView = itemView;
        }

        public void bindListDetails(Rankers_Items listItems) {
            this.listItems = listItems;

            rankerListItems.add(listItems);
            txtStd.setText("Std: "+listItems.getstandard());
            txtDiv.setText( "Div: "+ listItems.getdivision());
            recyclerView = (RecyclerView) itemView.findViewById(R.id.rankerListRecyclerView);
            recyclerView.setHasFixedSize(true);
            gridLayoutManager = new GridLayoutManager(itemView.getContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setNestedScrollingEnabled(false);

            recyclerView.smoothScrollToPosition(0);
            adapter = new Ranker_List_Adapter(listItems.getRankerListItems());
            recyclerView.setAdapter(adapter);

            //AddGridMenu();
        }

//        private void AddGridMenu() {
//            rankerListItems.clear();
//            int size = rankerListItems.size();
//            if(size != 0){
//
//                for (int i = 0; i < size; i++) {
//                    Rankers_Items gridViewItems = new Rankers_Items();
//                    gridViewItems.setFirstImagePath(listItems.getFirstImagePath());
//                    gridViewItems.setstudentName(listItems.getstudentName());
//                    gridViewItems.setoutOffMarks(listItems.getoutOffMarks());
//                    gridViewItems.setobtainedMarks(listItems.getobtainedMarks());
//                    rankerListItems.add(gridViewItems);
//                }
//                adapter.notifyDataSetChanged();
//            }
//        }

        @Override
        public void onClick(View v) {

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