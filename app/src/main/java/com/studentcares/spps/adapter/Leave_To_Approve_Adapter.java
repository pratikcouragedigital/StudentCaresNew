package com.studentcares.spps.adapter;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.studentcares.spps.DateFormatter;
import com.studentcares.spps.R;
import com.studentcares.spps.connectivity.Leave_req;
import com.studentcares.spps.model.Leave_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.HashMap;
import java.util.List;

public class Leave_To_Approve_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Leave_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;

    public Leave_To_Approve_Adapter(List<Leave_Items> items) {
        this.listItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            Leave_To_Approve_Adapter.EmptyViewHolder emptyViewHolder = new Leave_To_Approve_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leave_list_items, viewGroup, false);
        viewHolder = new Leave_To_Approve_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Leave_To_Approve_Adapter.ViewHolder) {
            Leave_To_Approve_Adapter.ViewHolder vHolder = (Leave_To_Approve_Adapter.ViewHolder) viewHolder;
            Leave_Items itemOflist = listItems.get(position);
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

    //
    @Override
    public int getItemViewType(int position) {
        if (listItems.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtName;
        public TextView txtId;
        public TextView txtFromDate;
        public TextView txtToDate;
        public TextView txtDepartment;
        public TextView txtReason;
        public ImageView image;
        RelativeLayout layoutApproveDisapprove, layoutApproveBy;
        String approveOrDisApprove = "", schoolId, userId, staffId, leaveListId, userType;
        Button btnApprove, btnDisapprove, txtApproveBy;
        ProgressDialog progressDialog;
        SessionManager sessionManager;
        public View cardView;
        Leave_Items listItems = new Leave_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            sessionManager = new SessionManager(v.getContext());
            HashMap<String, String> typeOfUser = sessionManager.getUserDetails();
            schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
            userType = typeOfUser.get(SessionManager.KEY_USERTYPE);
            userId = typeOfUser.get(SessionManager.KEY_USERID);

            image = (ImageView) itemView.findViewById(R.id.image);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtId = (TextView) itemView.findViewById(R.id.txtId);
            txtDepartment = (TextView) itemView.findViewById(R.id.txtDepartment);
            txtReason = (TextView) itemView.findViewById(R.id.txtReason);
            txtFromDate = (TextView) itemView.findViewById(R.id.txtFromDate);
            txtToDate = (TextView) itemView.findViewById(R.id.txtToDate);
            layoutApproveDisapprove = (RelativeLayout) itemView.findViewById(R.id.layoutApproveDisapprove);
            layoutApproveBy = (RelativeLayout) itemView.findViewById(R.id.layoutApproveBy);
            btnApprove = (Button) itemView.findViewById(R.id.btnApprove);
            btnDisapprove = (Button) itemView.findViewById(R.id.btnDisapprove);
            txtApproveBy = (Button) itemView.findViewById(R.id.txtApproveBy);
            cardView = itemView;

            layoutApproveDisapprove.setVisibility(View.GONE);
            layoutApproveBy.setVisibility(View.GONE);

            btnApprove.setOnClickListener(this);
            btnDisapprove.setOnClickListener(this);
        }

        public void bindListDetails(Leave_Items listItems) {
            this.listItems = listItems;

            txtName.setText(listItems.getstaffName());
            txtId.setText(listItems.getstaffId());
            txtDepartment.setText(listItems.getdepartment());
            txtReason.setText(listItems.getleaveReason());

            boolean isApprovedOrNot = listItems.getapprovedOrNot();

            if (isApprovedOrNot == true) {
                layoutApproveBy.setVisibility(View.VISIBLE);
                layoutApproveDisapprove.setVisibility(View.GONE);
                txtApproveBy.setText("Approve By " + listItems.getapproveBy());
            } else {

                layoutApproveBy.setVisibility(View.GONE);
                layoutApproveDisapprove.setVisibility(View.VISIBLE);
                //txtApproveBy.setText("Not Approved Yet");
            }

            final ProgressBar imageLoader;
            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);
            String imagepath=listItems.getFirstImagePath();
            if (imagepath == null || imagepath.equals("")){
                image.setImageResource(R.drawable.user_logo);
                imageLoader.setVisibility(View.GONE);
            }
            else{
                RequestOptions options = new RequestOptions().centerCrop().error(R.drawable.user_logo);

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

            String selectedDate = listItems.getfromDate();
            selectedDate = DateFormatter.ChangeDateFormat(selectedDate);
            txtFromDate.setText(selectedDate);

            String selectedDate2 = listItems.gettoDate();
            selectedDate2 = DateFormatter.ChangeDateFormat(selectedDate2);
            txtToDate.setText(selectedDate2);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnApprove) {

                staffId = listItems.getstaffId();
                leaveListId = listItems.getleaveListId();
                approveOrDisApprove = "Yes";
                progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setMessage("Leave Approving Please Wait");
                progressDialog.show();
                Leave_req leave_req = new Leave_req(v.getContext());
                leave_req.LeaveApprove(schoolId, userId, approveOrDisApprove, staffId, leaveListId, progressDialog);
            } else if (v.getId() == R.id.btnDisapprove) {

                staffId = listItems.getstaffId();
                leaveListId = listItems.getleaveListId();
                approveOrDisApprove = "No";
                progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setMessage("Leave Disapproving Please Wait");
                progressDialog.show();
                Leave_req leave_req = new Leave_req(v.getContext());
                leave_req.LeaveApprove(schoolId, userId, approveOrDisApprove, staffId, leaveListId, progressDialog);
            }
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("No one yet applied for leave.");

        }
    }
}