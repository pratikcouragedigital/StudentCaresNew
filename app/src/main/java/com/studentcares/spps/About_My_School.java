package com.studentcares.spps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.studentcares.spps.adapter.School_RatingReviews_Adapter;
import com.studentcares.spps.connectivity.School_Rating_Reviews_Get;
import com.studentcares.spps.model.SchoolReviewsListItems;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class About_My_School  extends AppCompatActivity implements View.OnClickListener{

    CollapsingToolbarLayout schoolDetailsCollapsingToolbar;
    CoordinatorLayout schoolDetailsCoordinatorLayout;
    AppBarLayout schoolDetailsAppBar;
    NestedScrollView schoolDetailsNestedScrollView;

    ImageButton callbutton;
    ImageButton locationMapButton;
    ImageButton rateAndReviewButton;

    LinearLayout schoolLinearLayout;
    RelativeLayout schoolFirstRelativeLayout;
    RelativeLayout schoolSecondRelativeLayout;

    ImageView ImgSchoolImage;
    TextView txtSchoolAddress;
    TextView txtSchoolWebsite;
    TextView txtSchoolName;
    TextView txtSchoolContact;
    TextView txtSchoolEmail;

    String schoolName;
    String schoolId;
    String schoolAddress;
    String schoolContactNo;
    String schoolWebSite;
    String schoolImage;
    String schoolEmail;
    String lat;
    String longi;

//    double latitude = 19.0658752;
//    double longitude = 73.0022780;

    double latitude;
    double longitude;
    public  View v;

    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    public List<SchoolReviewsListItems> schoolRatingReviewsItems = new ArrayList<SchoolReviewsListItems>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_my_school);

        // get data from session and append it
        SessionManager sessionManagerNgo = new SessionManager(About_My_School.this);
        HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
        schoolName = typeOfUser.get(SessionManager.KEY_SCHOOLNAME);
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        schoolAddress = typeOfUser.get(SessionManager.KEY_SCHOOLADDRESS);
        schoolContactNo = typeOfUser.get(SessionManager.KEY_SCHOOLCONTACTNO);
        schoolWebSite = typeOfUser.get(SessionManager.KEY_SCHOOLWEBSITE);
        schoolImage = typeOfUser.get(SessionManager.KEY_SCHOOLLOGO);
        schoolEmail = typeOfUser.get(SessionManager.KEY_SCHOOLEMAIL);
        lat = typeOfUser.get(SessionManager.KEY_SCHOOL_LAT);
        longi = typeOfUser.get(SessionManager.KEY_SCHOOL_LONGI);

        schoolDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.schoolDetailsCollapsingToolbar);
        schoolDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.schoolDetailsCoordinatorLayout);
        schoolDetailsAppBar = (AppBarLayout) findViewById(R.id.schoolDetailsAppBar);
        schoolDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.schoolDetailsNestedScrollView);

        ImgSchoolImage = (ImageView) findViewById(R.id.schoolHeaderImage);
        txtSchoolName = (TextView) findViewById(R.id.schoolName);
        txtSchoolAddress = (TextView) findViewById(R.id.schoolAddress);
        txtSchoolWebsite = (TextView) findViewById(R.id.schoolWebsite);
        txtSchoolContact = (TextView) findViewById(R.id.schoolContactNo);
        txtSchoolEmail = (TextView) findViewById(R.id.schoolEmail);
        callbutton = (ImageButton) findViewById(R.id.schoolDetailCallButton);
        locationMapButton = (ImageButton) findViewById(R.id.schoolDetailsMapButton);
        rateAndReviewButton= (ImageButton) findViewById(R.id.schoolDetailsRateNReviewButton);

        recyclerView = (RecyclerView) findViewById(R.id.schoolRateNReview);

        schoolLinearLayout = (LinearLayout) findViewById(R.id.containerLayout);
        schoolFirstRelativeLayout = (RelativeLayout) findViewById(R.id.schoolFirstRelativeLayout);
        schoolSecondRelativeLayout = (RelativeLayout) findViewById(R.id.addresslayout);

        txtSchoolName.setText(schoolName);
        txtSchoolAddress.setText(schoolAddress);
        txtSchoolWebsite.setText(schoolWebSite);
        txtSchoolContact.setText(schoolContactNo);
        txtSchoolEmail.setText(schoolEmail);

        //ImgSchoolImage.setImageResource(R.drawable.no_image);
        //Glide.with(ImgSchoolImage.getContext()).load(schoolImage).asBitmap().into(ImgSchoolImage);

        final ProgressBar imageLoader;
        imageLoader = (ProgressBar) findViewById(R.id.loading);
        if (schoolImage == null || schoolImage.equals("")){
            ImgSchoolImage.setImageResource(R.drawable.no_image);
            imageLoader.setVisibility(View.GONE);
        }else {
            Glide.with(ImgSchoolImage.getContext())
                    .asBitmap()
                    .load(schoolImage)
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
                    .into(ImgSchoolImage);
        }

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) schoolDetailsAppBar.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        schoolDetailsAppBar.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        callbutton.setOnClickListener(this);
        txtSchoolWebsite.setOnClickListener(this);
        txtSchoolContact.setOnClickListener(this);
        locationMapButton.setOnClickListener(this);
        rateAndReviewButton.setOnClickListener(this);

        reviewAdapter = new School_RatingReviews_Adapter(schoolRatingReviewsItems);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(reviewAdapter);
        getReviewsList();
    }

    private void getReviewsList() {
        try {
            School_Rating_Reviews_Get getreviewsList = new School_Rating_Reviews_Get(this);
            getreviewsList.GetSchool_Rating_Reviews(schoolRatingReviewsItems, recyclerView, reviewAdapter,  schoolId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) schoolDetailsAppBar.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(schoolDetailsCoordinatorLayout, schoolDetailsAppBar, null, 0, i, new int[]{0, 0});
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.schoolDetailCallButton){
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + schoolContactNo));
            startActivity(callIntent);
        }
        else if(v.getId() == R.id.schoolContactNo){
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + schoolContactNo));
            startActivity(callIntent);
        }
        else if(v.getId() == R.id.schoolWebsite){
            boolean validurl = Patterns.WEB_URL.matcher(schoolWebSite).matches();
            if(validurl == true){
                String url = "http://"+schoolWebSite;
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(launchBrowser);
            }else{
                Toast.makeText(this, "Its not a valid URL.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v.getId() == R.id.schoolDetailsRateNReviewButton){
            Intent gotoSchoolFeedback = new Intent(About_My_School.this,School_Feedback.class);
            startActivity(gotoSchoolFeedback);
        }
        else if(v.getId() == R.id.schoolDetailsMapButton){
             if(lat.equals("") ||lat.equals("null") || lat == null || longi.equals("") || longi.equals("null") || longi == null){
                 Toast.makeText(this, "Latitude and Longitude not available in database.", Toast.LENGTH_SHORT).show();
             }
             else{
                 latitude = Double.parseDouble(lat);
                 longitude = Double.parseDouble(longi);

                 Intent gotoClinicMap = new Intent(About_My_School.this,School_Map.class);
                 gotoClinicMap.putExtra("selectedLat",latitude);
                 gotoClinicMap.putExtra("selectedLong",longitude);
                 gotoClinicMap.putExtra("schoolName",schoolName);
                 startActivity(gotoClinicMap);
             }
        }
    }
}