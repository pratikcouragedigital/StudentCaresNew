package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.studentcares.spps.connectivity.School_Rating_Reviews_Get;
import com.studentcares.spps.internetConnectivity.NetworkChangeReceiver;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.HashMap;

public class School_Feedback extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    RatingBar schoolRating;
    EditText txtFeedbackOfUser;
    Button feedbackSubmit;

    String userFeedback = "";
    String userId = "";
    String schoolId = "";
    String userEmail = "";
    String schoolRatingValue = "";
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_feedback);

        ActivityInfo activityInfo = null;
        try {
            activityInfo = getPackageManager().getActivityInfo(
                    getComponentName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String title = activityInfo.loadLabel(getPackageManager())
                .toString();

        txtActivityName.setText(title);

        SessionManager sessionManagerNgo = new SessionManager(this);
        HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
        userId = typeOfUser.get(SessionManager.KEY_USERID);
        userEmail = typeOfUser.get(SessionManager.KEY_USEREMAIL);
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);

        schoolRating = (RatingBar) findViewById(R.id.schoolRatingBar);
        txtFeedbackOfUser = (EditText) findViewById(R.id.feedbackOfUser);
        feedbackSubmit = (Button) findViewById(R.id.feedbackSubmit);
        feedbackSubmit.setOnClickListener(this);

        schoolRating.performClick();
        schoolRating.setOnTouchListener(this);

    }

//    public void addListenerOnRatingBar() {
//
//
//        schoolRating.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    float touchPositionX = event.getX();
//                    float width = schoolRating.getWidth();
//                    float starsf = (touchPositionX / width) * 5.0f;
//                    int stars = (int) starsf + 1;
//                    schoolRating.setRating(stars);
//                    schoolRatingValue = String.valueOf(stars);
//                    v.setPressed(false);
//
//                }
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    v.setPressed(true);
//                }
//
//                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
//                    v.setPressed(false);
//                }
//                return true;
//            }
//        });
//    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.feedbackSubmit) {
            userFeedback = txtFeedbackOfUser.getText().toString();
           // schoolRatingValue = String.valueOf(schoolRatingValue);

            if (userFeedback.equals("") || userFeedback == null) {
                Toast.makeText(this, "Please Enter your Review", Toast.LENGTH_SHORT).show();
            }else if (schoolRatingValue.equals("") || schoolRatingValue == null) {
                Toast.makeText(this, "Please Add Rating.(Select Stars.)", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(this.getString(R.string.progress_msg));
                progressDialog.show();

                School_Rating_Reviews_Get addSchoolFeedback = new School_Rating_Reviews_Get(this);
                addSchoolFeedback.addreview(userId, schoolId, schoolRatingValue, userFeedback, progressDialog);
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = this.getPackageManager();
        ComponentName component = new ComponentName(this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = this.getPackageManager();
        ComponentName component = new ComponentName(this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float touchPositionX = event.getX();
            float width = schoolRating.getWidth();
            float starsf = (touchPositionX / width) * 5.0f;
            int stars = (int) starsf + 1;
            schoolRating.setRating(stars);
            schoolRatingValue = String.valueOf(stars);
            v.setPressed(false);

        }
        return true;
    }
}