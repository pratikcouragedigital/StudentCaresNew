package com.studentcares.spps;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.studentcares.spps.connectivity.GPS_StaffOutWork;
import com.studentcares.spps.gps_service.GPS_Location_Alert;
import com.studentcares.spps.gps_service.LocationMonitoringService;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.singleton.Staff_Tracker_Instance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GPS_Staff_OutWork extends BaseActivity implements  OnMapReadyCallback , View.OnClickListener {

    private static final String TAG = GPS_Staff_OutWork.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;


    private boolean mAlreadyStartedService = false;
    private TextView mMsgView;
    GoogleMap googleMap;
    LatLng latLng;
    double currLat = 0, currLongi = 0, lastLat = 0, lastLongi = 0,Latitude,Longitude;

    SessionManager sessionManager;
    SupportMapFragment mapFragment;
    ImageView btnStartStop;
    boolean isGpsTurnOn;
    Staff_Tracker_Instance staff_Tracker_Instance;

    private static String schoolId,userType,userId,locationDate,locationTime;
    int locationCounter = 0;
    Activity mContext = GPS_Staff_OutWork.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_staff_outwork);
        mMsgView = (TextView) findViewById(R.id.latitudeTextView);

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

        sessionManager = new SessionManager(this);
        HashMap<String, String> typeOfUser = sessionManager.getUserDetails();
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        userType = typeOfUser.get(SessionManager.KEY_USERTYPE);
        userId = typeOfUser.get(SessionManager.KEY_USERID);

        staff_Tracker_Instance = new Staff_Tracker_Instance();
        isGpsTurnOn = staff_Tracker_Instance.getisGpsTurnOn();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        btnStartStop = (ImageView) findViewById(R.id.btnStartStop);
        btnStartStop.setOnClickListener(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);
                        float accuracy = Float.parseFloat(intent.getStringExtra(LocationMonitoringService.EXTRA_ACCURACY));

                        if (latitude != null && longitude != null) {

                            currLat = Double.parseDouble(latitude);
                            currLongi = Double.parseDouble(longitude);

                            if (currLat == lastLat && currLongi == lastLongi) {
                                //Toast.makeText(GPS_Staff_OutWork.this, "Location not updated", Toast.LENGTH_SHORT).show();
                            }
                            else {
//                                locationCounter ++;
//                                String msg = "Updated Location: " + currLat + "," + currLongi;
//                                Toast.makeText(GPS_Staff_OutWork.this, msg, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(GPS_Staff_OutWork.this,"counter: "+ locationCounter, Toast.LENGTH_SHORT).show();
                                // You can now create a LatLng Object for use with maps
                                latLng = new LatLng(currLat, currLongi);
                                lastLat = currLat;
                                lastLongi = currLongi;
                                googleMap.addMarker(new MarkerOptions().position(latLng)
                                        .title("Your Current Position"));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.5f));

                                Date c = Calendar.getInstance().getTime();

                                SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d");
                                locationDate = df.format(c);

                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                locationTime = sdf.format(new Date());

                                //save location on server Db
                                GPS_StaffOutWork gps_StaffOutWork = new  GPS_StaffOutWork(GPS_Staff_OutWork.this);
                                gps_StaffOutWork.AddStaffOutworkLocation(userId,schoolId,accuracy,lastLat,lastLongi,locationDate,locationTime);
                            }

                            mMsgView.setText("Location Tracking Started" + "\n Latitude : " + latitude + "\n Longitude: " + longitude+"\n Counter:"+locationCounter);
                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );
    }


    @Override
    public void onResume() {
        super.onResume();

        startStep1();
    }

    private void startStep1() {

        //Check whether this user has installed Google play service which is being used by Location updates.
        if (isGooglePlayServicesAvailable()) {

            //Passing null to indicate that it is executing for the first time.
            startStep2(null);

        } else {
            Toast.makeText(getApplicationContext(), "No Google Playservice found. Please install it.", Toast.LENGTH_LONG).show();
        }
    }

    private Boolean startStep2(DialogInterface dialog) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            promptInternetConnect();
            return false;
        }

        if (dialog != null) {
            dialog.dismiss();
        }

        //Yes there is active internet connection. Next check Location is granted by user or not.

        if (checkPermissions()) { //Yes permissions are granted by the user. Go to the next step.
           //to turn on gps
            GPS_Location_Alert GPSLocationAlert = new GPS_Location_Alert(mContext);
            startStep3();
        } else {  //No user has not granted the permissions yet. Request now.
            requestPermissions();
        }
        return true;
    }

    private void promptInternetConnect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GPS_Staff_OutWork.this);
        builder.setTitle("No internet");
        builder.setMessage("Please make sure that you have active internet connection.");

        String positiveText = "Refresh";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Block the Application Execution until user grants the permissions
                        if (startStep2(dialog)) {

                            //Now make sure about location permission.
                            if (checkPermissions()) {

                                //Step 2: Start the Location Monitor Service
                                //Everything is there to start the service.
                                startStep3();
                            } else if (!checkPermissions()) {
                                requestPermissions();
                            }

                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startStep3() {

        //And it will be keep running until you close the entire application from task manager.
        //This method will executed only once.

        if (!mAlreadyStartedService && mMsgView != null) {

            if(isGpsTurnOn == true){
                mMsgView.setText("Location Tracking Started");

                //Start location sharing service to app server.........
                Intent intent = new Intent(this, LocationMonitoringService.class);
                startService(intent);

                mAlreadyStartedService = true;
                //Ends................................................
            }
            else{
                stopService(new Intent(this, LocationMonitoringService.class));
                mAlreadyStartedService = false;
            }
        }
    }

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionState2 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermissions() {

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);


        // Provide an additional rationale to the img_user. This would happen if the img_user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale || shouldProvideRationale2) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(GPS_Staff_OutWork.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the img_user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(GPS_Staff_OutWork.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If img_user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.i(TAG, "Permission granted, updates requested, starting location updates");
                startStep3();

            } else {
                // Permission denied.

                // Notify the img_user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the img_user for permission (device policy or "Never ask
                // again" prompts). Therefore, a img_user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }


    @Override
    public void onDestroy() {

        //Stop location sharing service to app server.........

//        stopService(new Intent(this, LocationMonitoringService.class));
//        mAlreadyStartedService = false;
        //Ends................................................

        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap gm) {
        googleMap = gm;

        LatLng location = new LatLng(currLat, currLongi);
        googleMap.addMarker(new MarkerOptions().position(location)
                .title("Your Current Position"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16.5f));

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnStartStop){
            if(isGpsTurnOn == true){
                isGpsTurnOn = false;
                staff_Tracker_Instance.setisGpsTurnOn(isGpsTurnOn);
                btnStartStop.setImageResource(R.drawable.gps_on);
                Toast.makeText(this, "GPS Stop", Toast.LENGTH_SHORT).show();

                //stop tracking location
                stopService(new Intent(this, LocationMonitoringService.class));
                mAlreadyStartedService = false;
            }
            else{

                isGpsTurnOn = true;
                staff_Tracker_Instance.setisGpsTurnOn(isGpsTurnOn);
                btnStartStop.setImageResource(R.drawable.gps_off);
                Toast.makeText(this, "GPS Start", Toast.LENGTH_SHORT).show();

                //start tracking location
                Intent intent = new Intent(this, LocationMonitoringService.class);
                startService(intent);
            }
        }
    }
}
