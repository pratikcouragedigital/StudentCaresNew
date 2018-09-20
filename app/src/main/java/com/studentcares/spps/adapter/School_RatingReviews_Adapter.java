package com.studentcares.spps.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.studentcares.spps.R;
import com.studentcares.spps.model.SchoolReviewsListItems;

import java.util.List;

public class School_RatingReviews_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<SchoolReviewsListItems> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;

    private static final int VIEW_TYPE_EMPTY = 1;

    public School_RatingReviews_Adapter(List<SchoolReviewsListItems> items) {
        this.listItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            School_RatingReviews_Adapter.EmptyViewHolder emptyViewHolder = new School_RatingReviews_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.school_rating_reviews_items, viewGroup, false);
        viewHolder = new School_RatingReviews_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof School_RatingReviews_Adapter.ViewHolder) {
            School_RatingReviews_Adapter.ViewHolder vHolder = (School_RatingReviews_Adapter.ViewHolder) viewHolder;
            SchoolReviewsListItems itemOflist = listItems.get(position);
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

        public TextView schoolReviews;
        public TextView userName;
        public View dividerLine;
        public int userRatings;
        LinearLayout layout;
        RelativeLayout relativeLayoutOfReviewItem;

        SchoolReviewsListItems listItems = new SchoolReviewsListItems();

        public ViewHolder(View itemView) {
            super(itemView);

            layout = (LinearLayout) itemView.findViewById(R.id.ratingNos);
            schoolReviews = (TextView) itemView.findViewById(R.id.reviews);
            userName = (TextView) itemView.findViewById(R.id.usersName);
            dividerLine = itemView.findViewById(R.id.viewdividerline);
            relativeLayoutOfReviewItem = (RelativeLayout) itemView.findViewById(R.id.reviewLayout);

        }

        public void bindListDetails(SchoolReviewsListItems listItems) {
            this.listItems = listItems;

            if(listItems.getschool_rating() == "") {
                userRatings = 0;
            }
            else {
                userRatings = Integer.parseInt(listItems.getschool_rating());
            }
            DisplayMetrics metrics = v.getContext().getResources().getDisplayMetrics();
            float dp = 20f;
            float fpixels = metrics.density * dp;
            int pixels = (int) (fpixels + 0.5f);

            for (int i = 0; i < userRatings; i++) {
                ImageView image = new ImageView(v.getContext());
                image.setLayoutParams(new android.view.ViewGroup.LayoutParams(pixels, pixels));
                // image.setLayoutParams(new android.view.ViewGroup.LayoutParams(R.dimen.starSize,R.dimen.starSize));
                image.setId(i);
                image.setImageResource(R.drawable.ratingstar_yellow);
                // Adds the view to the layout
                image.setPadding(3, 3, 3, 3);
                layout.addView(image);
            }

            schoolReviews.setText(listItems.getschool_reviews());
            userName.setText(listItems.getname());
            dividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView,emptyTextViewTitle;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextViewTitle = (TextView) v.findViewById(R.id.emptyTextViewTitle);
            emptyTextViewTitle.setVisibility(View.GONE);
            emptyTextView.setText("Reviews Not Available Yet. You can add your review for school. ");
            emptyTextView.setTextColor(v.getResources().getColor(R.color.colorBlack));

        }
    }
}