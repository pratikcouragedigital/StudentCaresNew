package com.studentcares.spps;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.studentcares.spps.connectivity.News_Request_Data;
import com.studentcares.spps.imageModule.Image;
import com.studentcares.spps.internetConnectivity.NetworkChangeReceiver;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.sqlLite.DataBaseHelper;

import java.util.Calendar;
import java.util.HashMap;

public class News_Add extends BaseActivity implements View.OnClickListener, Image.OnRecyclerSetImageListener  {

    ImageView firstImage;

    String firstImagePath = "";

    LinearLayout imageViewLinearLayout;

    EditText txtHeading;
    EditText txtDescription;
    EditText submitionDate;
    Button btnSubmit;
    Button btnAddImage;

    private StringBuilder date;
    String selectedDateForSubmission = "";
    String description = "";
    String heading = "";
    String userId = "";
    String schoolId = "";

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    private ProgressDialog progressDialog = null;

    private DataBaseHelper mydb;
	Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_add);

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
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);

        mydb = new DataBaseHelper(this);

        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtHeading = (EditText) findViewById(R.id.txtHeading);
        submitionDate = (EditText) findViewById(R.id.txtSubmissionDate);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnAddImage = (Button) findViewById(R.id.btnAddImage);
        firstImage = (ImageView) findViewById(R.id.firstNewsImage);

        imageViewLinearLayout = (LinearLayout) findViewById(R.id.imageViewLinearLayout);
			 imageViewLinearLayout.setVisibility(View.GONE);										   

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        btnSubmit.setOnClickListener(this);
        btnAddImage.setOnClickListener(this);

        submitionDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                setDate(view);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnSubmit) {
            description = txtDescription.getText().toString();
            heading = txtHeading.getText().toString();

            if (selectedDateForSubmission == "") {
                Toast.makeText(this, "Please Select Date.", Toast.LENGTH_SHORT).show();
            } else if (description.equals("")) {
                Toast.makeText(this, "Please Enter Description.", Toast.LENGTH_SHORT).show();
            } else if (heading.equals("")) {
                Toast.makeText(this, "Please Enter Heading.", Toast.LENGTH_SHORT).show();
            } else {

                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait. News Is Adding.");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                try {
                    News_Request_Data UAAddNews = new News_Request_Data(News_Add.this);
                    UAAddNews.AddSchoolNews(userId,schoolId,heading,description, selectedDateForSubmission, firstImagePath,progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (v.getId() == R.id.btnAddImage) {
            image = new Image(this, "News", this);
            image.getImage();
        }

    }
    @SuppressWarnings("deprecation")

    public void setDate(View view) {
        showDialog(999);
//        Toast.makeText(getApplicationContext(), "Select Date", Toast.LENGTH_SHORT)
//                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {

        submitionDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        date = new StringBuilder().append(year).append("-").append(month).append("-").append(day);
        selectedDateForSubmission = date.toString();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        PackageManager pm = this.getPackageManager();
        ComponentName component = new ComponentName(this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onResume() {
        super.onResume();
        PackageManager pm = this.getPackageManager();
        ComponentName component = new ComponentName(this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

	@Override
    @TargetApi(23)
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        image.getActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        image.getRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onRecyclerImageSet(Bitmap imageToShow, String imageBase64String) {
        imageViewLinearLayout.setVisibility(View.VISIBLE);
        firstImage.setImageBitmap(imageToShow);
        this.firstImagePath = imageBase64String;
    }
}