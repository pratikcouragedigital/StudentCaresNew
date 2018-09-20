package com.studentcares.spps;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.github.rubensousa.floatingtoolbar.FloatingToolbarMenuBuilder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.studentcares.spps.adapter.PopupAdapter;
import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.connectivity.User_List_Request;
import com.studentcares.spps.gps_service.GetAddress_From_LatLon;
import com.studentcares.spps.gps_service.GoogleMapUtil;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.sqlLite.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.studentcares.spps.R.id.map;

public class GPS extends BaseActivity implements View.OnClickListener, OnMapReadyCallback, FloatingToolbar.ItemClickListener, GoogleMap.OnInfoWindowClickListener {

    private static final int COLOR_BLACK_ARGB = 0xff000000;

    private static final int POLYLINE_STROKE_WIDTH_PX = 3;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    SessionManager sessionManager;
    SupportMapFragment mapFragment;
    FloatingToolbar dateTimeFloatingToolbar;
    Calendar calendar;

    StringBuilder date;
    String userType, schoolId, userId, ResponseResultCurrLatLon,todaysDate, currentDate, GPSOnOrOff, StaffName,selectedDate, method = "", timeChecker = "", gpsPosDateTime;
    String serverKey = "AIzaSyCINvQi1InXas-84Z2m82RsR9C-QRjuQXA";
    double currLat, currLon, Latitude, Longitude, lastLat, lastLong;
    private int year, month, day, mHour, mMinute, currentPt = 0, currentLocationReqCounter = 0, mapCounter, i;
    int fTimeHr, fTimeMin, tTimeHr, tTimeMin;
    boolean mStopHandler = false;

    ArrayList<LatLng> movementLine = new ArrayList();
    ArrayList<String> GpsPosDateTimeArray = new ArrayList();
    ArrayList<String> adreesFromLatLong = new ArrayList();

    GoogleMap googleMap, gMap;
    Polyline RoutePolyline, polyLine;
    PolylineOptions rectOptions = new PolylineOptions();
    //LatLng origin, destination;
    Animator animator;

    List<Polyline> polylineList = new ArrayList<Polyline>();
    List<Marker> markers = new ArrayList<Marker>();
    Marker marker;

    Handler handler = new Handler();
    Handler mHandler;
    Marker selectedMarker;

    ProgressDialog progressDialog = null;
    Snackbar snackbar;
    AlertDialog.Builder alert;

    View dialogView, v;
    Button btnTimerSubmit;
    EditText txtToTime, txtFromTime;
    FloatingActionButton fabDateFilter;
    CoordinatorLayout coordinatorLayout;
    private DataBaseHelper mydb;

    String address;

    int firstCounter = 0,btnClickCounter = 0;
    CountDownTimer countDownTimer;
    TextView txtTimer,txtStaffName;
    ImageView btnStartStop;
    private int pauseCounter = 0;
    private LatLng markerPos;
    private LatLng secondPos;
    private LatLng newPosition;
    private int playCounter = 0;
    private int todayDateCounter = 0;

    Spinner staffListSpinner;
    String StaffId;
    private String[] staffListSpinnerArrayList;
    private Std_Div_Filter_Adapter spinnerAdapter;
    private ProgressDialog staffListSpinnerDialogBox;
    private List<String> StaffIdList = new ArrayList<String>();
    private List<String> StaffNameList = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gps_location);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
        userType = typeOfUser.get(SessionManager.KEY_USERTYPE);
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        userId = typeOfUser.get(SessionManager.KEY_USERID);

        mydb = new DataBaseHelper(this);

        StaffSpinnerPopUp();

        Date c = Calendar.getInstance().getTime();
//            SimpleDateFormat df = new SimpleDateFormat("d-M-yyyy");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d");
        selectedDate = df.format(c);


        txtTimer = (TextView) findViewById(R.id.txtTimer);
        txtStaffName = (TextView) findViewById(R.id.txtStaffName);
        btnStartStop = (ImageView) findViewById(R.id.btnStartStop);
        btnStartStop.setOnClickListener(this);

        fabDateFilter = (FloatingActionButton) findViewById(R.id.fabDateFilter);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        dateTimeFloatingToolbar = (FloatingToolbar) findViewById(R.id.dateTimeFloatingToolbar);
        fabDateFilter.setOnClickListener(this);

        fabDateFilter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                dateTimeFloatingToolbar.attachFab(fabDateFilter);
                dateTimeFloatingToolbar.setMenu(new FloatingToolbarMenuBuilder(getApplicationContext())
                        .addItem(R.id.play, R.drawable.play,"Play")
                        .addItem(R.id.pause, R.drawable.pause,"Pause")
                        .addItem(R.id.stop, R.drawable.stop, "Stop")
                        .addItem(R.id.time, R.drawable.time, "Time")
                        .addItem(R.id.date, R.drawable.calender_white, "Date")
                        .build());
                dateTimeFloatingToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                dateTimeFloatingToolbar.enableAutoHide(false);
                return false;
            }
        });
        dateTimeFloatingToolbar.setClickListener(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);

        Date curDate = new Date();
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");

        currentDate = dFormat.format(curDate);
        selectedDate = currentDate;

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        int currMonth = month + 1;
        todaysDate = String.valueOf(year) +"-"+String.valueOf(currMonth)+"-"+String.valueOf(day);
    }

    private void StaffSpinnerPopUp() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        int margin = (int) getResources().getDimension(R.dimen.margin);
        staffListSpinner = new Spinner(this);
        RelativeLayout container = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin, margin, margin, margin);

        staffListSpinner.setLayoutParams(params);
        container.addView(staffListSpinner);
        alert.setTitle(Html.fromHtml("<b>" + "Select Staff." + "</b>"));

        //getListOfstaffListSpinner();
        alert.setView(container);
        staffListSpinnerArrayList = new String[]{
                "Select Staff"
        };
        StaffNameList = new ArrayList<>(Arrays.asList(staffListSpinnerArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, StaffNameList);
        getListOfstaffListSpinner();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staffListSpinner.setAdapter(spinnerAdapter);
        staffListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    StaffName = parent.getItemAtPosition(position).toString();
                    StaffId = StaffIdList.get(position);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // String subject = edittext.getText().toString();
                txtStaffName.setText(StaffName);
                getDateWiseLatLongFromServer();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });
        alert.show();
    }

    private void getListOfstaffListSpinner() {
        User_List_Request GPSStaffOutWork = new User_List_Request(this);
        GPSStaffOutWork.ShowStaffList(StaffNameList, StaffIdList, schoolId, spinnerAdapter);
    }

    private void UpdateLocationTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
//                if(cureentPosCounter == 0){
//                    txtTimer.setText("Refreshing in " + millisUntilFinished / 1000 + " seconds");
//                }
                txtTimer.setText("Refreshing in " + millisUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
//                if(cureentPosCounter == 0){
//                    getUpdatedCurrentLocation();
//                    countDownTimer.start();
//                }
                getUpdatedCurrentLocation();
                countDownTimer.start();
            }
        }.start();
    }

    @Override
    public void onMapReady(final GoogleMap gm) {

        if (mapCounter == 0) {
            //show current position with data
            googleMap = gm;
            if (googleMap != null) {
                googleMap.clear();
            }

            showCurrentPosition();
        } else if (mapCounter == 1) {
            //show playback data
            if (movementLine.size() > 0) {
                mStopHandler = true;
                animator = new Animator();
                googleMap = gm;
                googleMap.clear();

                markers.clear();

                if (playCounter == 1) {
                    for (i = 0; i < movementLine.size(); i++) {
                        //addMarkerToMap(movementLine.get(i), GpsPosDateTimeArray.get(i), adreesFromLatLong.get(i));
                        addMarkerToMap(movementLine.get(i), GpsPosDateTimeArray.get(i));
                    }

                    RoutePolyline = googleMap.addPolyline(new PolylineOptions()
                            .addAll(movementLine));
                    stylePolyline(RoutePolyline);

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            animator.startAnimation(true);
                        }
                    });
                } else {
                    RoutePolyline = googleMap.addPolyline(new PolylineOptions()
                            .addAll(movementLine));
                    stylePolyline(RoutePolyline);

                    currLat = movementLine.get(movementLine.size() - 1).latitude;
                    currLon = movementLine.get(movementLine.size() - 1).longitude;

                    GetAddress_From_LatLon getAddress_From_LatLon = new GetAddress_From_LatLon(this);
                    address = getAddress_From_LatLon.getAddressFromLatLong(currLat, currLon);

                    marker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(currLat, currLon))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gps_fixed_pink_500_24dp))
                            .title("Last Position")
                            .snippet(address));
                    marker.showInfoWindow();

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currLat, currLon), 18));
                }
            }
        } else if (mapCounter == 2) {
            //default map with school and home location without playback data
            mStopHandler = true;
            googleMap = gm;
            googleMap.clear();

//            movementLine.add(origin);
//            movementLine.add(destination);
//            GoogleMapUtil.fixZoomForLatLngs(googleMap, movementLine);
        }

        gm.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
        gm.setOnInfoWindowClickListener(GPS.this);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void addMarkerToMap(LatLng latLng, String time) {

        double lat = latLng.latitude;
        double lon = latLng.longitude;
        GetAddress_From_LatLon getAddress_From_LatLon = new GetAddress_From_LatLon(this);
        String addr = getAddress_From_LatLon.getAddressFromLatLong(lat, lon);
        //String addr = "";

        String positionDetails = "Timing : \n" + time + "\n\nAddress : \n" + addr;
        Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng)
                .title("Details")
                .snippet(positionDetails));
        markers.add(marker);
        //marker.showInfoWindow();
    }

    public void showCurrentPosition() {
//        Double lat = movementLine.get(movementLine.size() - 1).latitude;
//        Double lng = movementLine.get(movementLine.size() - 1).longitude;

        GetAddress_From_LatLon getAddress_From_LatLon = new GetAddress_From_LatLon(this);
//        address = getAddress_From_LatLon.getAddressFromLatLong(currLat, currLon);
        address = "";

        marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(currLat, currLon))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gps_fixed_pink_500_24dp))
                .title("Current Position")
                .snippet(address));
        marker.showInfoWindow();

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currLat, currLon), 18));
        //animateMarker(googleMap, marker, new LatLng(currLat, currLon), false);
    }

    private void getUpdatedCurrentLocation() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        try {
            StaffCurrentLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDateWiseLatLongFromServer() {

        try {
            playCounter = 0;
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait");
            progressDialog.show();

            StaffLocationDateWise();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTimeWiseLatLongFromServer() {

        try {
            playCounter = 0;
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
            StaffLocationTimeWise();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDate(View view) {
        showDialog(999);
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
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        date = new StringBuilder().append(year).append("-").append(month).append("-").append(day);
        selectedDate = date.toString();

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date strDate = simpleDateFormat.parse(selectedDate);
             if (!new Date().before(strDate)) {
//                if (currentLocationReqCounter == 0) {
//                    countDownTimer.cancel();
                    getDateWiseLatLongFromServer();
//                }
                Toast.makeText(this, "Date-" + selectedDate, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Please select previous date!", Toast.LENGTH_LONG).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnTimerSubmit) {

            String ftime = txtFromTime.getText().toString();
            String ttime = txtToTime.getText().toString();

            if (ftime.equals("") || txtFromTime == null) {
                Toast.makeText(this, "Please Select From Time.", Toast.LENGTH_SHORT).show();
            } else if (ttime.equals("") || txtToTime == null) {
                Toast.makeText(this, "Please Select To Time.", Toast.LENGTH_SHORT).show();
            } else {
                getTimeWiseLatLongFromServer();
            }
        }
        else if (v.getId() == R.id.btnStartStop) {

            if (btnClickCounter == 0) {
                btnClickCounter = 1;
                sessionManager.Set_GPS_Staff_TurnOnOff(String.valueOf(btnClickCounter));
                btnStartStop.setImageResource(R.drawable.gps_off);
                // Send Notification to turn on GPS to the staff. staffId,StaffName
                GPSOnOrOff = "On";
                sendNotificationToStopGPS();
                Toast.makeText(this, "GPS Turn On", Toast.LENGTH_SHORT).show();
            } else {
                btnClickCounter = 0;
                sessionManager.Set_GPS_Staff_TurnOnOff(String.valueOf(btnClickCounter));
                btnStartStop.setImageResource(R.drawable.gps_on);
                // Send Notification to turn off GPS to the staff.staffId,StaffName
                GPSOnOrOff = "Off";
                sendNotificationToStopGPS();
                Toast.makeText(this, "GPS Turn Off", Toast.LENGTH_SHORT).show();

            }
        }

//        else if (v.getId() == R.id.btnFilterOk) {
//            if (StaffName == null || StaffName.isEmpty()) {
//                Toast.makeText(this, "Please select Standard.", Toast.LENGTH_LONG).show();
//            }
//            else{
//                // staffId and date
//                txtStaffName.setText(StaffName);
//                getDateWiseLatLongFromServer();
//
//            }
//        }
    }

    private void sendNotificationToStopGPS() {
        JSONObject jsonObject = new JSONObject();

        String webMethName = "GPS_Turn_On_Off";
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Staff_Id", StaffId);
            jsonObject.put("GPSOnOrOff", GPSOnOrOff);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.url) + webMethName;

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            String res = response.getString("responseDetails");
                            if (res.equals("Notification Sent Successfully.")) {

                                snackbar = Snackbar
                                        .make(fabDateFilter, "GPS Successfully Turned"+GPSOnOrOff, Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else {
                                snackbar = Snackbar
                                        .make(fabDateFilter, "Please try again later", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Toast.makeText(GPS.this, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(GPS.this, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.play) {

            if (movementLine.isEmpty() == true) {
                Toast.makeText(this, "Nothing to play.", Toast.LENGTH_LONG).show();
            } else {
                if (pauseCounter == 0) {
                    playCounter = 1;
                    if (animator != null) {
                        animator.removeMapAndPolylines();
                    }
                    googleMap.clear();
                    mapCounter = 1;
                    mapFragment.getMapAsync(this);
                } else {
                    animator.startAnimation(true);
                }
            }
        } else if (menuItem.getItemId() == R.id.pause) {
            if (movementLine.isEmpty() == true) {
                Toast.makeText(this, "Nothing to pause.", Toast.LENGTH_LONG).show();
            } else {
                pauseCounter = 1;
                animator.stopAnimation();
                animator.removeAnimator();
//                pauseAnimation();
            }
        } else if (menuItem.getItemId() == R.id.stop) {
            if (movementLine.isEmpty() == true) {
                Toast.makeText(this, "Nothing to stop.", Toast.LENGTH_LONG).show();
            } else {
                pauseCounter = 0;
                animator.stopAnimation();
                animator.removeMapAndPolylines();
            }

        } else if (menuItem.getItemId() == R.id.time) {

            alert = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            dialogView = inflater.inflate(R.layout.gps_timer_dialoge, null);
            FilterTime_LayoutDetails();
            alert.setView(dialogView);
            alert.show();
        } else if (menuItem.getItemId() == R.id.date) {
            firstCounter = 1;
            setDate(v);
        }
    }


    private void FilterTime_LayoutDetails() {

        btnTimerSubmit = (Button) dialogView.findViewById(R.id.btnTimerSubmit);
        txtFromTime = (EditText) dialogView.findViewById(R.id.txtFromTime);
        txtToTime = (EditText) dialogView.findViewById(R.id.txtToTime);

        btnTimerSubmit.setOnClickListener(this);

        txtFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeChecker = "FromTime";
                setTime(timeChecker);
            }
        });

        txtToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeChecker = "ToTime";
                setTime(timeChecker);
            }
        });
    }

    private void setTime(String timeCheckerStr) {

        timeChecker = timeCheckerStr;
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (timeChecker.equals("FromTime")) {
                            fTimeHr = hourOfDay;
                            fTimeMin = minute;
                            txtFromTime.setText(fTimeHr + ":" + fTimeMin);
                        } else if (timeChecker.equals("ToTime")) {
                            tTimeHr = hourOfDay;
                            tTimeMin = minute;
                            txtToTime.setText(tTimeHr + ":" + tTimeMin);
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onItemLongClick(MenuItem menuItem) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, marker.getSnippet(), Toast.LENGTH_LONG).show();
    }


    private void StaffCurrentLocation() {
        JSONObject jsonObject = new JSONObject();

        String webMethName = "GPS_Outwork_Staff_Current_Location";
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Staff_Id", StaffId);
            jsonObject.put("Date", selectedDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.url) + webMethName;

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            String res = response.getString("responseDetails");
                            if (res.equals("Location Not Available.")) {
                                //Toast.makeText(GPS.this, "Location Not Updated", Toast.LENGTH_SHORT).show();
                                snackbar = Snackbar
                                        .make(coordinatorLayout, "Location Not Available.", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);

                                        Latitude = Double.parseDouble((obj.getString("Latitude")));
                                        Longitude = Double.parseDouble((obj.getString("Longitude")));
                                        String locationDate = obj.getString("Location_Date");
                                        String locationTime = obj.getString("Location_Time");
                                        gpsPosDateTime = locationDate + " " + locationTime;
                                        GpsPosDateTimeArray.add(gpsPosDateTime);
                                    }
                                    if (lastLat != currLat && lastLong != currLon) {
                                        lastLat = currLat;
                                        lastLong = currLon;
                                        movementLine.add(new LatLng(Latitude, Longitude));
                                        mapFragment.getMapAsync(GPS.this);
                                    } else {
                                        snackbar = Snackbar
                                                .make(fabDateFilter, "Location is not updated.", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }

                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Toast.makeText(GPS.this, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(GPS.this, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void StaffLocationDateWise() {
        JSONObject jsonObject = new JSONObject();

        String webMethName = "GPS_Outwork_Staff_DateWise_Location";
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Staff_Id", StaffId);
            jsonObject.put("Date", selectedDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.url) + webMethName;

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            movementLine.clear();
                            GpsPosDateTimeArray.clear();
                            String res = response.getString("responseDetails");
                            if (res.equals("Location Not Available.")) {
                                snackbar = Snackbar
                                        .make(fabDateFilter, "Data Not Available For This Date.", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                todayDateCounter = 0;// no data
                                movementLine.clear();
                                GpsPosDateTimeArray.clear();
                                adreesFromLatLong.clear();
                                mapCounter = 2;
                                mapFragment.getMapAsync(GPS.this);
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);

                                        Latitude = Double.parseDouble((obj.getString("Latitude")));
                                        Longitude = Double.parseDouble((obj.getString("Longitude")));
                                        String locationDate = obj.getString("Location_Date");
                                        String locationTime = obj.getString("Location_Time");
                                        gpsPosDateTime = locationDate + " " + locationTime;
                                        GpsPosDateTimeArray.add(gpsPosDateTime);
                                        movementLine.add(new LatLng(Latitude, Longitude));
                                    }

                                    mapCounter = 1;
                                    todayDateCounter = 1;
                                    if (selectedDate.equals(todaysDate)){
                                        UpdateLocationTimer();
                                    }
                                    progressDialog.dismiss();
                                    mapFragment.getMapAsync(GPS.this);
                                    snackbar = Snackbar
                                            .make(fabDateFilter, "Done", Snackbar.LENGTH_LONG);
                                    snackbar.show();

                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Toast.makeText(GPS.this, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(GPS.this, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void StaffLocationTimeWise() {
        JSONObject jsonObject = new JSONObject();

        String webMethName = "GPS_Outwork_Staff_Location_TimeWise";
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Staff_Id", StaffId);
            jsonObject.put("Date", selectedDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.url) + webMethName;

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            movementLine.clear();
                            GpsPosDateTimeArray.clear();
                            String res = response.getString("responseDetails");
                            if (res.equals("Location Not Available.")) {
                                progressDialog.dismiss();
                                snackbar = Snackbar
                                        .make(fabDateFilter, "Data Not Available For This Time", Snackbar.LENGTH_LONG);

                                snackbar.show();
                                movementLine.clear();
                                GpsPosDateTimeArray.clear();
                                mapCounter = 2;
                                mapFragment.getMapAsync(GPS.this);
                            } else {
                                try {

                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);

                                        Latitude = Double.parseDouble((obj.getString("Latitude")));
                                        Longitude = Double.parseDouble((obj.getString("Longitude")));
                                        String locationDate = obj.getString("Location_Date");
                                        String locationTime = obj.getString("Location_Time");
                                        gpsPosDateTime = locationDate + " " + locationTime;

                                        GpsPosDateTimeArray.add(gpsPosDateTime);
                                        movementLine.add(new LatLng(Latitude, Longitude));
                                    }

                                    progressDialog.dismiss();
                                    mapCounter = 1;
                                    mapFragment.getMapAsync(GPS.this);

                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Toast.makeText(GPS.this, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(GPS.this, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

//    private static void animateMarker(GoogleMap googleMap, final Marker marker, final LatLng toPosition,
//                                      final boolean hideMarker) {
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        Projection proj = googleMap.getProjection();
//        Point startPoint = proj.toScreenLocation(marker.getPosition());
//        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
//        final long duration = 500;
//
//        final Interpolator interpolator = new LinearInterpolator();
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float) elapsed
//                        / duration);
//                double lng = t * toPosition.longitude + (1 - t)
//                        * startLatLng.longitude;
//                double lat = t * toPosition.latitude + (1 - t)
//                        * startLatLng.latitude;
//                marker.setPosition(new LatLng(lat, lng));
//
//                if (t < 1.0) {
//                    // Post again 16ms later.
//                    handler.postDelayed(this, 16);
//                } else {
//                    if (hideMarker) {
//                        marker.setVisible(false);
//                    } else {
//                        marker.setVisible(true);
//                    }
//                }
//            }
//        });
//    }


    private void stylePolyline(Polyline polyline) {
        String type = "";
        // Get the data object stored with the polyline.
        if (polyline.getTag() != null) {
            type = polyline.getTag().toString();
        }

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "A":
                // Use a custom bitmap as the cap at the start of the line.
                polyline.setStartCap(
                        new CustomCap(
                                BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow), 10));
                break;
            case "B":
                // Use a round cap at the start of the line.
                polyline.setStartCap(new RoundCap());
                break;
        }

        polyline.setEndCap(new RoundCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(COLOR_BLACK_ARGB);
        polyline.setJointType(JointType.ROUND);
    }

    private Location convertLatLngToLocation(LatLng latLng) {
        Location loc = new Location("someLoc");
        loc.setLatitude(latLng.latitude);
        loc.setLongitude(latLng.longitude);
        return loc;
    }

    private float bearingBetweenLatLngs(LatLng begin, LatLng end) {
        Location beginL = convertLatLngToLocation(begin);
        Location endL = convertLatLngToLocation(end);

        return beginL.bearingTo(endL);
    }

//    public void addMarkerToMap(LatLng latLng, String s, String addr) {


    private void highLightMarker(int index) {

        if (index < movementLine.size() - 1) {
            highLightMarker(markers.get(index), markers.get(index + 1));
        } else {
            highLightMarker(markers.get(index), markers.get(index));
        }
    }

    private void highLightMarker(Marker marker, Marker nextMarker) {
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.boy));
        marker.setVisible(true);
        //marker.showInfoWindow();

        if (marker != nextMarker) {
            nextMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            nextMarker.setVisible(true);

//            rectOptions = new PolylineOptions().add(nextMarker.getPosition()).pattern(PATTERN_POLYLINE_DOTTED);
//            gMap.addPolyline(rectOptions);
        }

        this.selectedMarker = marker;
    }

    private void resetMarkers() {
        for (Marker marker : this.markers) {
            //marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            marker.setVisible(false);
        }
    }

//    GoogleMap.CancelableCallback MyCancelableCallback =
//            new GoogleMap.CancelableCallback() {
//
//                @Override
//                public void onCancel() {
//                    System.out.println("onCancelled called");
//                }
//
//                @Override
//                public void onFinish() {
//
//                    if (++currentPt < markers.size()) {
//                        float targetBearing = bearingBetweenLatLngs(googleMap.getCameraPosition().target, markers.get(currentPt).getPosition());
//
//                        LatLng targetLatLng = markers.get(currentPt).getPosition();
//                        //float targetZoom = zoomBar.getProgress();
//
//                        Toast.makeText(GPS.this, "currentPt " + currentPt, Toast.LENGTH_SHORT).show();
//                        System.out.println("currentPt  = " + currentPt);
//                        System.out.println("size  = " + markers.size());
//                        //Create a new CameraPosition
//                        CameraPosition cameraPosition =
//                                new CameraPosition.Builder()
//                                        .target(targetLatLng)
//                                        .tilt(currentPt < markers.size() - 1 ? 30 : 0)
//                                        .bearing(targetBearing)
//                                        .zoom(googleMap.getCameraPosition().zoom)
//                                        .build();
//
//                        googleMap.animateCamera(
//                                CameraUpdateFactory.newCameraPosition(cameraPosition),
//                                3000,
//                                MyCancelableCallback);
//                        System.out.println("Animate to: " + markers.get(currentPt).getPosition() + "\n" +
//                                "Bearing: " + targetBearing);
//
//                        markers.get(currentPt).showInfoWindow();
//
//                    } else {
//                        //info.setText("onFinish()");
//                    }
//                }
//            };

    public class Animator implements Runnable {

        private int ANIMATE_SPEEED = 2000;
        private int BEARING_OFFSET = 20;

        private final Interpolator interpolator = new LinearInterpolator();

        int currentIndex = 0;

        float tilt = 30;

        long start = SystemClock.uptimeMillis();

        LatLng endLatLng = null;
        LatLng beginLatLng = null;

        boolean showPolyline = false;
        private Marker trackingMarker;

        public void reset() {
            resetMarkers();
            start = SystemClock.uptimeMillis();
            currentIndex = 0;
            endLatLng = getEndLatLng();
            beginLatLng = getBeginLatLng();
        }

        public void stop() {
//            trackingMarker.remove();
            //handler.removeCallbacks(animator);
        }

        public void removeMapAndPolylines() {
            if (polylineList.size() != 0) {
                for (Polyline line : polylineList) {
                    line.remove();
                }
                polylineList.clear();
            }
            if (gMap != null) {
                gMap.clear();
            }
            removeAnimator();
            mapCounter = 0;
            mapFragment.getMapAsync(GPS.this);
        }

        public void removeAnimator() {
            handler.removeCallbacks(animator);
        }

        public void initialize(boolean showPolyLine) {

            if (pauseCounter == 1) {
                trackingMarker.remove();
                markerPos = newPosition;
                secondPos = markers.get(currentIndex + 1).getPosition();
            } else {
                gMap = googleMap;
                reset();
                this.showPolyline = showPolyLine;

//            Marker marker = markers.get(0);
//            marker.setVisible(true);
//            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.boy));

                highLightMarker(0);

                if (showPolyLine) {
                    polyLine = initializePolyLine();
                }

                // We first need to put the camera in the correct position for the first run (we need 2 markers for this).....

                markerPos = markers.get(0).getPosition();
                secondPos = markers.get(1).getPosition();
            }

            setupCameraPositionForMovement(markerPos, secondPos);
        }

        private void setupCameraPositionForMovement(LatLng markerPos, LatLng secondPos) {

            float bearing = bearingBetweenLatLngs(markerPos, secondPos);

            trackingMarker = gMap.addMarker(new MarkerOptions().position(markerPos)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.boy))
                    .alpha(0.8f)
                    .title("title")
                    .snippet("snippet"));

            CameraPosition cameraPosition =
                    new CameraPosition.Builder()
                            .target(markerPos)
                            .bearing(bearing)
                            .tilt(30)
                            .zoom(gMap.getCameraPosition().zoom >= 16 ? gMap.getCameraPosition().zoom : 16)
                            .build();

            float[] results = new float[1];
            Location.distanceBetween(markerPos.latitude, markerPos.longitude, secondPos.latitude, secondPos.longitude, results);

            getMovingTime(results);

            gMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(cameraPosition),
                    1500,
                    new GoogleMap.CancelableCallback() {

                        @Override
                        public void onFinish() {
                            System.out.println("finished camera");
                            if (pauseCounter == 0) {
                                animator.reset();
                            }
                            Handler handler = new Handler();
                            handler.post(animator);
                        }

                        @Override
                        public void onCancel() {
                            System.out.println("cancelling camera");
                        }
                    }
            );
        }

        private Polyline initializePolyLine() {
            rectOptions.add(markers.get(0).getPosition()).pattern(null);
            return gMap.addPolyline(rectOptions);
        }

        private void updatePolyLine(LatLng latLng) {
            List<LatLng> points = polyLine.getPoints();
            points.add(latLng);
            polyLine.setPoints(points);
            polylineList.add(polyLine);
        }


        public void stopAnimation() {
            animator.stop();
        }

        public void startAnimation(boolean showPolyLine) {
            if (movementLine.size() > 2) {
                animator.initialize(showPolyLine);
            }
        }

        @Override
        public void run() {

            float[] results = new float[1];
            Location.distanceBetween(beginLatLng.latitude, beginLatLng.longitude, endLatLng.latitude, endLatLng.longitude, results);

            getMovingTime(results);

            long elapsed = SystemClock.uptimeMillis() - start;
            double t = interpolator.getInterpolation((float) elapsed / ANIMATE_SPEEED);

            double lat = t * endLatLng.latitude + (1 - t) * beginLatLng.latitude;
            double lng = t * endLatLng.longitude + (1 - t) * beginLatLng.longitude;
            newPosition = new LatLng(lat, lng);

            trackingMarker.setPosition(newPosition);

            markers.get(0).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.boy));
            markers.get(0).setVisible(true);

            if (showPolyline) {
//                updatePolyLine(newPosition);
            }

            float bearingL = bearingBetweenLatLngs(beginLatLng, endLatLng);

            CameraPosition cameraPosition =
                    new CameraPosition.Builder()
                            .target(newPosition) // changed this...
                            .bearing(bearingL)
                            .tilt(tilt)
                            .zoom(gMap.getCameraPosition().zoom)
                            .build();

            gMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(cameraPosition),
                    1,
                    null
            );

            // It's not possible to move the marker + center it through a cameraposition update while another camerapostioning was already happening.
            //navigateToPoint(newPosition,tilt,bearing,currentZoom,false);
            //navigateToPoint(newPosition,false);

            if (t < 1) {
                handler.postDelayed(this, 16);
            } else {

                System.out.println("Move to next marker.... current = " + currentIndex + " and size = " + markers.size());
                // imagine 5 elements -  0|1|2|3|4 currentindex must be smaller than 4
                if (currentIndex < markers.size() - 2) {

                    currentIndex++;

                    endLatLng = getEndLatLng();
                    beginLatLng = getBeginLatLng();

//                    results = new float[1];
//                    Location.distanceBetween(beginLatLng.latitude, beginLatLng.longitude, endLatLng.latitude, endLatLng.longitude, results);
//
//                    getMovingTime(results);

                    start = SystemClock.uptimeMillis();

//                    LatLng begin = getBeginLatLng();
//                    LatLng end = getEndLatLng();

//                    float bearingL = bearingBetweenLatLngs(begin, end);

                    highLightMarker(currentIndex);

//                    CameraPosition cameraPosition =
//                            new CameraPosition.Builder()
//                                    .target(end) // changed this...
//                                    .bearing(bearingL)
//                                    .tilt(tilt)
//                                    .zoom(gMap.getCameraPosition().zoom)
//                                    .build();
//
//                    gMap.animateCamera(
//                            CameraUpdateFactory.newCameraPosition(cameraPosition),
//                            1500,
//                            null
//                    );

                    start = SystemClock.uptimeMillis();
                    handler.postDelayed(animator, 16);

                } else {
                    currentIndex++;
                    highLightMarker(currentIndex);
                    markers.get(movementLine.size() - 1).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gps_fixed_pink_500_24dp));
                    GoogleMapUtil.fixZoomForMarkers(gMap, markers);
                    pauseCounter = 0;
                    stopAnimation();
                }

            }
        }

        private LatLng getEndLatLng() {
            return markers.get(currentIndex + 1).getPosition();
        }

        private LatLng getBeginLatLng() {
            return markers.get(currentIndex).getPosition();
        }

        private void getMovingTime(float[] results) {
            int result = Math.round(results[0]);
            if (result < 50) {
                ANIMATE_SPEEED = 1000;
            } else if (result < 100) {
                ANIMATE_SPEEED = 2000;
            } else if (result < 300) {
                ANIMATE_SPEEED = 3000;
            } else if (result < 600) {
                ANIMATE_SPEEED = 4000;
            } else if (result < 1000) {
                ANIMATE_SPEEED = 6000;
            } else if (result < 3000) {
                ANIMATE_SPEEED = 10000;
            } else if (result < 5000) {
                ANIMATE_SPEEED = 15000;
            } else if (result < 10000) {
                ANIMATE_SPEEED = 20000;
            } else if (result < 15000) {
                ANIMATE_SPEEED = 25000;
            } else if (result < 20000) {
                ANIMATE_SPEEED = 30000;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
