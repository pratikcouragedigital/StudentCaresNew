package com.studentcares.spps;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.HashMap;

public class My_Profile extends AppCompatActivity implements View.OnClickListener{

    RelativeLayout part1RelativeLayout;
    RelativeLayout part2RelativeLayout;
//    RelativeLayout extraLabelDetailsLayout;
//    RelativeLayout extraTxtDetailsLayout;

    RelativeLayout personalDetailsRelaytiveLayout;
    RelativeLayout schoolDetailsRelativeLayout;

    int countForm1 = 0;
    int countForm2 = 0;

    ImageView userProfilePhoto;
    TextView txtSaveUserId  ;
    TextView txtUserContactNo  ;
    TextView txtUserFullName  ;
    TextView txtUserEmail  ;
    TextView txtUserDOB  ;
    TextView txtUserAddress ;
    TextView txtUserBloodGroup ;
    TextView txtUserGrNo ;
    TextView txtUserSwipeCard ;
    TextView txtUserRollNo ;
    TextView txtSchoolName  ;
    TextView txtSchoolWebsite  ;
    TextView txtSchoolContactNo  ;
    TextView txtSchoolAddress  ;
    TextView txtSchoolEmail  ;

    public String userType = "";
    public String userPicPath = "";
    public String userId = "";
    public String userFullName = "";
    public String userContactNo = "";
    public String userEmail = "";
    public String userDOB = "";
    public String userAddress= "";
    public String userBloodGroup= "";
    public String userGrNo= "";
    public String userSwipeCard= "";
    public String userRollNo= "";
    public String schoolName = "";
    public String schoolWebsite = "";
    public String schoolContactNo = "";
    public String schoolAddress = "";
    public String schoolEmail = "";

    CollapsingToolbarLayout detailsCollapsingToolbar;
    CoordinatorLayout detailsCoordinatorLayout;
    AppBarLayout detailsAppBar;
    Toolbar myProfileToolbar;
    NestedScrollView schoolDetailsNestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);

        SessionManager sessionManagerNgo = new SessionManager(My_Profile.this);
        HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
        userType = typeOfUser.get(SessionManager.KEY_USERTYPE);
        userPicPath = typeOfUser.get(SessionManager.KEY_USERR_LOGO);
        userId= typeOfUser.get(SessionManager.KEY_USERID);
        userFullName = typeOfUser.get(SessionManager.KEY_NAME);
        userContactNo = typeOfUser.get(SessionManager.KEY_CONTACTNO);
        userEmail = typeOfUser.get(SessionManager.KEY_USEREMAIL);
        userDOB = typeOfUser.get(SessionManager.KEY_USERDOB);
        userAddress= typeOfUser.get(SessionManager.KEY_USERADDRESS);
        userBloodGroup= typeOfUser.get(SessionManager.KEY_USERBLOODGROUP);
        userGrNo= typeOfUser.get(SessionManager.KEY_USERGRNO);
        userSwipeCard= typeOfUser.get(SessionManager.KEY_USERSWIPECARDNO);
        userRollNo= typeOfUser.get(SessionManager.KEY_USERROLLNO);
        schoolName = typeOfUser.get(SessionManager.KEY_SCHOOLNAME);
        schoolAddress = typeOfUser.get(SessionManager.KEY_SCHOOLADDRESS);
        schoolContactNo = typeOfUser.get(SessionManager.KEY_SCHOOLCONTACTNO);
        schoolWebsite = typeOfUser.get(SessionManager.KEY_SCHOOLWEBSITE);
        schoolEmail = typeOfUser.get(SessionManager.KEY_SCHOOLEMAIL);

        detailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.userDetailsCollapsingToolbar);
        detailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.detailsCoordinatorLayout);
        detailsAppBar = (AppBarLayout) findViewById(R.id.detailsAppBar);
        myProfileToolbar = (Toolbar) findViewById(R.id.myProfileToolbar);
        schoolDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.schoolDetailsNestedScrollView);

        part1RelativeLayout = (RelativeLayout) findViewById(R.id.part1RelativeLayout);
        part2RelativeLayout = (RelativeLayout) findViewById(R.id.part2RelativeLayout);
//        extraLabelDetailsLayout = (RelativeLayout) findViewById(R.id.extraLabelDetailsLayout);
//        extraTxtDetailsLayout = (RelativeLayout) findViewById(R.id.extraTxtDetailsLayout);
        personalDetailsRelaytiveLayout = (RelativeLayout) findViewById(R.id.personalDetailsRelaytiveLayout);
        schoolDetailsRelativeLayout = (RelativeLayout) findViewById(R.id.schoolDetailsRelativeLayout);

        userProfilePhoto = (ImageView) findViewById(R.id.image);
//        txtUserFullName = (TextView) findViewById(R.id.name);
        txtSaveUserId = (TextView) findViewById(R.id.userId);
        txtUserRollNo = (TextView) findViewById(R.id.txtRollNo);
        txtUserGrNo = (TextView) findViewById(R.id.txtGrNo);
        txtUserSwipeCard = (TextView) findViewById(R.id.txtSwipeCardNo);
        txtUserBloodGroup = (TextView) findViewById(R.id.txtBloodGroup);
        txtUserDOB = (TextView) findViewById(R.id.txtDob);
        txtUserContactNo = (TextView) findViewById(R.id.txtContactNo);
        txtUserEmail = (TextView) findViewById(R.id.txtEmail);
        txtUserAddress = (TextView) findViewById(R.id.txtAddress);

        txtSchoolName = (TextView) findViewById(R.id.schoolName);
        txtSchoolContactNo = (TextView) findViewById(R.id.schoolContactNo);
        txtSchoolAddress = (TextView) findViewById(R.id.schoolAddress);
        txtSchoolEmail = (TextView) findViewById(R.id.schoolEmailId);
        txtSchoolWebsite = (TextView) findViewById(R.id.schoolWebsite);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) detailsAppBar.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        detailsAppBar.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        txtSaveUserId.setText(userId);
        //txtUserFullName.setText(userFullName);
        txtUserContactNo.setText(userContactNo);
        txtUserEmail.setText(userEmail);
        txtUserDOB.setText(userDOB);
        txtUserAddress.setText(userAddress);
        txtUserBloodGroup.setText(userBloodGroup);
        txtUserGrNo.setText(userGrNo);
        txtUserSwipeCard.setText(userSwipeCard);
        txtUserRollNo.setText(userRollNo);
        txtSchoolName.setText(schoolName);
        txtSchoolWebsite.setText(schoolWebsite);
        txtSchoolContactNo.setText(schoolContactNo);
        txtSchoolAddress.setText(schoolAddress);
        txtSchoolEmail.setText(schoolEmail);

        setSupportActionBar(myProfileToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myProfileToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        detailsCollapsingToolbar.setTitle(userFullName);

//        Glide.with(userProfilePhoto.getContext()).load(userPicPath).asBitmap().centerCrop().into(new BitmapImageViewTarget(userProfilePhoto) {
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable =
//                        RoundedBitmapDrawableFactory.create(userPic.getContext().getResources(), resource);
//                circularBitmapDrawable.setCircular(true);
//                userProfilePhoto.setImageDrawable(circularBitmapDrawable);
//            }
//        });
        RequestOptions options = new RequestOptions().centerCrop();

        Glide.with(userProfilePhoto.getContext())
                .asBitmap()
                .load(userPicPath)
                .apply(options)
                .into(userProfilePhoto);

        part1RelativeLayout.setOnClickListener(this);
        part2RelativeLayout.setOnClickListener(this);
        userProfilePhoto.setOnClickListener(this);

        personalDetailsRelaytiveLayout.setVisibility(View.GONE);
        schoolDetailsRelativeLayout.setVisibility(View.GONE);

//        if(userType.equals("Student")){
//            extraLabelDetailsLayout.setVisibility(View.VISIBLE);
//            extraTxtDetailsLayout.setVisibility(View.VISIBLE);
//        }else{
//            extraLabelDetailsLayout.setVisibility(View.GONE);
//            extraTxtDetailsLayout.setVisibility(View.GONE);
//        }
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) detailsAppBar.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(detailsCoordinatorLayout, detailsAppBar, null, 0, i, new int[]{0, 0});
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.part1RelativeLayout) {

            if (countForm1 == 0) {
                personalDetailsRelaytiveLayout.setVisibility(View.VISIBLE);
                countForm1 = 1;
            } else {
                personalDetailsRelaytiveLayout.setVisibility(View.GONE);
                countForm1 = 0;
            }
        } else if (v.getId() == R.id.part2RelativeLayout) {

            if (countForm2 == 0) {
                schoolDetailsRelativeLayout.setVisibility(View.VISIBLE);
                countForm2 = 1;
            } else {
                schoolDetailsRelativeLayout.setVisibility(View.GONE);
                countForm2 = 0;
            }
        }
        else if(v.getId() == R.id.image){
            String imagetoSave="Profile";
            Intent gotoFulImage = new Intent(My_Profile.this, Full_Image_Activity.class);
            gotoFulImage.putExtra("Image", userPicPath);
            gotoFulImage.putExtra("ImageFolder", imagetoSave);
            v.getContext().startActivity(gotoFulImage);
        }
    }
}
