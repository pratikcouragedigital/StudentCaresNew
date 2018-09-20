package com.studentcares.spps;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.studentcares.spps.internetConnectivity.CheckInternetConnection;
import com.studentcares.spps.model.Attendance_Calender_Items;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.sqlLite.DataBaseHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import tourguide.tourguide.TourGuide;

public class My_Attendance extends BaseActivity implements OnClickListener {

    private TextView currentMonth;
    private ImageView prevMonth,nextMonth,imgPieChart,imgBarGraph;

    private GridView calendarView,gridCalendar;
    private GridCellAdapter adapter;
    private Calendar _calendar;

    String ResponseResult,webMethName,currentMonthForAttendanceDetails,currentYearForAttendanceDetails;
    String userId,schoolId,userType,swipeCardNo;
    int thisYear,thisMonth,thisDay,responseCounter = 1,month, year;
    int totalDays = 0,totalSchoolDaysInMonth,totalWeeklyOffDay,totalPresentDay,totalLateMarksDay,totalHolidaysDay,totalAbsentDay;

    private List<String> absentDateList = new ArrayList<String>();
    private List<String> presentDateList = new ArrayList<String>();
    private List<String> lateMarkDateList = new ArrayList<String>();
    private List<String> holidayDateList = new ArrayList<String>();
    private List<String> daysInMonthList = new ArrayList<String>();
    private List<String> schoolDaysInMonthList = new ArrayList<String>();
    private List<String> sondaysInMonth = new ArrayList<String>();
    private List<String> weeklyOffList = new ArrayList<String>();

    private List<Attendance_Calender_Items> itemsOfAttendance = new ArrayList<Attendance_Calender_Items>();
    private static final String dateTemplate = "MMMM yyyy";
    private ProgressDialog progressDialog = null;

    private DataBaseHelper mydb;
    PieChart pieChart;
    BarChart barChart;

    RelativeLayout pieChartLayout,barGraphLayout;

    JSONArray listArray = null;

    private TourGuide mTourGuideHandler;
    private Animation mEnterAnimation, mExitAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_attendance);

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

        SessionManager sessionManagerNgo = new SessionManager(My_Attendance.this);
        HashMap<String, String> typeOfUser = sessionManagerNgo.getActiveUser();
        HashMap<String, String> typeOfUser2 = sessionManagerNgo.getUserDetails();
        userId = typeOfUser.get(SessionManager.KEY_USERID);
        schoolId = typeOfUser2.get(SessionManager.KEY_SCHOOLID);
        userType = typeOfUser2.get(SessionManager.KEY_USERTYPE);
        swipeCardNo = typeOfUser2.get(SessionManager.KEY_USERSWIPECARDNO);

        mydb = new DataBaseHelper(this);
        pieChart = (PieChart) findViewById(R.id.chart);
        barChart = (BarChart) findViewById(R.id.barChart);
        imgPieChart = (ImageView) findViewById(R.id.imgPieChart);
        imgBarGraph = (ImageView) findViewById(R.id.imgBarGraph);
        pieChartLayout = (RelativeLayout) findViewById(R.id.pieChartLayout);
        barGraphLayout = (RelativeLayout) findViewById(R.id.barGraphLayout);
        gridCalendar = (GridView) findViewById(R.id.calendar);
        pieChartLayout.setVisibility(View.GONE);
        barGraphLayout.setVisibility(View.VISIBLE);

        Calendar calendar = Calendar.getInstance();
        thisYear = calendar.get(Calendar.YEAR);
        thisMonth = calendar.get(Calendar.MONTH) + 1;
        thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        calendarView = (GridView) this.findViewById(R.id.calendar);
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);

        //runOverlay_ContinueMethod();
        mEnterAnimation = new AlphaAnimation(0f, 1f);
        mEnterAnimation.setDuration(600);
        mEnterAnimation.setFillAfter(true);

        mExitAnimation = new AlphaAnimation(1f, 0f);
        mExitAnimation.setDuration(600);
        mExitAnimation.setFillAfter(true);

        // Initialised calender for current month
        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);

        currentMonth = (TextView) this.findViewById(R.id.currentMonth);
        currentMonth.setText(DateFormat.format(dateTemplate, _calendar.getTime()));

        absentDateList.clear();
        presentDateList.clear();
        lateMarkDateList.clear();
        holidayDateList.clear();
        daysInMonthList.clear();
        weeklyOffList.clear();
        schoolDaysInMonthList.clear();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        getAttendanceDetails(month, year);
        adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year, itemsOfAttendance);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);

    }

    private void setGridCellAdapterToDate(int month, int year) {

        absentDateList.clear();
        presentDateList.clear();
        lateMarkDateList.clear();
        holidayDateList.clear();
        daysInMonthList.clear();
        schoolDaysInMonthList.clear();
        totalDays = 0;

        adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year, itemsOfAttendance);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(DateFormat.format(dateTemplate, _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == prevMonth) {
            if (month <= 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            absentDateList.clear();
            presentDateList.clear();
            lateMarkDateList.clear();
            holidayDateList.clear();
            daysInMonthList.clear();
            weeklyOffList.clear();
            schoolDaysInMonthList.clear();

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(this.getString(R.string.progress_msg));
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            getAttendanceDetails(month, year);
            setGridCellAdapterToDate(month, year);
        }
        if (v == nextMonth) {
            if (month > 11) {
                month = 1;
                year++;
            } else {
                month++;
            }
            absentDateList.clear();
            presentDateList.clear();
            lateMarkDateList.clear();
            holidayDateList.clear();
            daysInMonthList.clear();
            weeklyOffList.clear();
            schoolDaysInMonthList.clear();

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(this.getString(R.string.progress_msg));
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            getAttendanceDetails(month, year);
            setGridCellAdapterToDate(month, year);

        }
//        if (v == btnHome) {
//            Intent gotoHome = new Intent(this, MainActivity.class);
//            startActivity(gotoHome);
//        }
        else if (v.getId() == R.id.imgPieChart) {

            pieChartLayout.setVisibility(View.VISIBLE);
            barGraphLayout.setVisibility(View.GONE);
            //showPieChart();
        } else if (v.getId() == R.id.imgBarGraph) {

            pieChartLayout.setVisibility(View.GONE);
            barGraphLayout.setVisibility(View.VISIBLE);
            //showBarGraph();
        }
    }

    private void getAttendanceDetails(int month, int year) {
        currentMonthForAttendanceDetails = String.valueOf(month);
        currentYearForAttendanceDetails = String.valueOf(year);

        absentDateList.clear();
        presentDateList.clear();
        lateMarkDateList.clear();
        holidayDateList.clear();
        daysInMonthList.clear();
        weeklyOffList.clear();
        schoolDaysInMonthList.clear();

        if (CheckInternetConnection.getInstance(this).isOnline()) {
            AttendanceDetails();

        } else {
            AsyncCallWSFor_OfflineAttendance task = new AsyncCallWSFor_OfflineAttendance();
            task.execute();
        }
    }

    private void AttendanceDetails() {

        webMethName ="Attendance_Monthwise";
        int currentMonthForAttendanceDetails = month;
        int currentYearForAttendanceDetails = year;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("student_id", userId);
            jsonObject.put("Month", currentMonthForAttendanceDetails);
            jsonObject.put("year", currentYearForAttendanceDetails);
            jsonObject.put("School_id", schoolId);
            jsonObject.put("UserType", userType);
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
                            if(res.equals("Data Not Found!")){
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(My_Attendance.this);
                                builder.setTitle("Result");
                                builder.setMessage("Data Not Found For This Month!");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface alert, int which) {
                                        // TODO Auto-generated method stub
                                        //Do something
                                        alert.dismiss();
                                    }
                                });
                                android.support.v7.app.AlertDialog alert1 = builder.create();
                                alert1.show();
                            }else{
                                itemsOfAttendance.clear();
                                try {
                                    responseCounter = 0;
                                    JSONArray jsonArray = response.getJSONArray("responseDetails");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        try {
                                            JSONObject obj = jsonArray.getJSONObject(i);
                                            Attendance_Calender_Items attendanceItems = new Attendance_Calender_Items();
                                            attendanceItems.setAttendanceId(obj.getString("Attendance_id"));
                                            attendanceItems.setStudentId(obj.getString("Student_Id"));
                                            attendanceItems.setSwipCardNo(obj.getString("SwipCard_No"));
                                            attendanceItems.setSchoolId(obj.getString("School_Id"));
                                            attendanceItems.setMachineId(obj.getString("Machine_Id"));
                                            attendanceItems.setMachineNo(obj.getString("Machine_no"));
                                            attendanceItems.setAttType(obj.getString("Att_Type"));
                                            attendanceItems.setAttDate(obj.getString("Att_Date"));
                                            attendanceItems.setAttTime(obj.getString("Att_Time"));
                                            attendanceItems.setAtt_OutTime(obj.getString("OutTime"));
                                            attendanceItems.setAttStatus(obj.getString("Att_Status"));
                                            attendanceItems.setTrkSms(obj.getString("Trk_Sms"));
                                            attendanceItems.setPresentDate(obj.getString("PresentDate"));
                                            attendanceItems.setPresentMonth(obj.getString("PresentMonth"));
                                            attendanceItems.setPresentYear(obj.getString("PresentYear"));

                                            if (obj.getString("Att_Status").equals("P")) {
                                                presentDateList.add(obj.getString("PresentDate"));
                                            }
                                            else if (obj.getString("Att_Status").equals("M")) {
                                                presentDateList.add(obj.getString("PresentDate"));
                                            }
                                            else if (obj.getString("Att_Status").equals("L")) {
                                                lateMarkDateList.add(obj.getString("PresentDate"));
                                            }
                                            else if (obj.getString("Att_Status").equals("H")) {
                                                holidayDateList.add(obj.getString("PresentDate"));
                                            }
                                            else if (obj.getString("Att_Status").equals("W")) {
                                                weeklyOffList.add(obj.getString("PresentDate"));
                                            }
                                            itemsOfAttendance.add(attendanceItems);
                                            adapter.notifyDataSetChanged();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }

                        }
                        catch(Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Toast.makeText(My_Attendance.this, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(My_Attendance.this, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public class AsyncCallWSFor_OfflineAttendance extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                listArray = mydb.getAttenadanceDetailsMonthWise(userId, currentMonthForAttendanceDetails, currentYearForAttendanceDetails);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            if (listArray.length() == 0) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Attendance Details Not Available For this Month.", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(My_Attendance.this);
                builder.setTitle("Result");
                builder.setMessage("Attendance Details Not Available For this Month.");
                AlertDialog alert1 = builder.create();
                alert1.show();
                responseCounter = 1;
            } else {
                int dataCount = listArray.length();
                responseCounter = 0;
                for (int i = 0; i < listArray.length(); i++) {
                    try {
                        JSONObject obj = listArray.getJSONObject(i);

                        Attendance_Calender_Items attendanceItems = new Attendance_Calender_Items();
                        attendanceItems.setAttendanceId(obj.getString("Attendance_id"));
                        attendanceItems.setStudentId(obj.getString("Student_Id"));
                        attendanceItems.setSwipCardNo(obj.getString("SwipCard_No"));
                        attendanceItems.setSchoolId(obj.getString("School_Id"));
                        attendanceItems.setMachineId(obj.getString("Machine_Id"));
                        attendanceItems.setMachineNo(obj.getString("Machine_no"));
                        attendanceItems.setAttType(obj.getString("Att_Type"));
                        attendanceItems.setAttDate(obj.getString("Att_Date"));
                        attendanceItems.setAttTime(obj.getString("Att_Time"));
                        attendanceItems.setAtt_OutTime(obj.getString("Att_OutTime"));
                        attendanceItems.setAttStatus(obj.getString("Att_Status"));
                        attendanceItems.setTrkSms(obj.getString("Trk_Sms"));
                        attendanceItems.setPresentDate(obj.getString("PresentDate"));
                        attendanceItems.setPresentMonth(obj.getString("PresentMonth"));
                        attendanceItems.setPresentYear(obj.getString("PresentYear"));

                        if (obj.getString("Att_Status").equals("P")) {
                            presentDateList.add(obj.getString("PresentDate"));
                        } else if (obj.getString("Att_Status").equals("M")) {
                            presentDateList.add(obj.getString("PresentDate"));
                        } else if (obj.getString("Att_Status").equals("L")) {
                            lateMarkDateList.add(obj.getString("PresentDate"));
                        }
                        else if (obj.getString("Att_Status").equals("H")) {
                            holidayDateList.add(obj.getString("PresentDate"));
                        }
                        else if (obj.getString("Att_Status").equals("W")) {
                            weeklyOffList.add(obj.getString("PresentDate"));
                        }
                        itemsOfAttendance.add(attendanceItems);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Inner Class Adapter

    public class GridCellAdapter extends BaseAdapter implements OnClickListener {
        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button gridcell;
        //private TextView num_events_per_day;
        GridView calendarGrid;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
        List<Attendance_Calender_Items> itemsDetails;
        Attendance_Calender_Items listItems;

        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId, int month, int year, List<Attendance_Calender_Items> itemsOfAttendance) {
            super();
            this._context = context;
            this.list = new ArrayList<String>();
            this.itemsDetails = itemsOfAttendance;
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            // Print Month
            printMonth(month, year);
            // Find Number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);

        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        private void printMonth(int mm, int yy) {
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm - 1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
            }
            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {

                String mydate = (String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
                SimpleDateFormat inFormat = new SimpleDateFormat("d MMM yyyy");
                Date date = null;
                try {
                    date = inFormat.parse(mydate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                String currentDayName = outFormat.format(date);
                if (!currentDayName.equals("Sunday")) {
                    schoolDaysInMonthList.add(String.valueOf(i));
                } else {
                    sondaysInMonth.add(String.valueOf(i));
                }

                if (i == getCurrentDayOfMonth()) {
                    list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                }
            }
            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }

        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            return map;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (itemsDetails.size() != 0 && position <= itemsDetails.size() - 1) {
                listItems = new Attendance_Calender_Items();
                listItems = itemsDetails.get(position);
            }

            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.my_attendance_gridcell, parent, false);
            }
//            num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
            // Get a reference to the Day gridcell
            gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);

            gridcell.setTag(new Integer(position));

            // ACCOUNT FOR SPACING
            String[] day_color = list.get(position).split("-");
            String color = day_color[1];
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];

            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);

            String myCurrentDate = theday + " " + themonth + " " + theyear;
            SimpleDateFormat inFormat = new SimpleDateFormat("d MMM yyyy");
            Date date = null;
            try {
                date = inFormat.parse(myCurrentDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            String currentDayName = outFormat.format(date);

            // to avoid similar data of list {"ptk", "a","b","ptk"}result={"ptk", "a","b"}
            Set<String> hsForPresentDateList = new HashSet<>();
            hsForPresentDateList.addAll(presentDateList);
            presentDateList.clear();
            presentDateList.addAll(hsForPresentDateList);

            schoolDaysInMonthList.removeAll(holidayDateList);
            schoolDaysInMonthList.removeAll(weeklyOffList);
            totalSchoolDaysInMonth = schoolDaysInMonthList.size();
            //totalDaysInMonth.setText("Total School Days : " + totalSchoolDaysInMonth);

            totalPresentDay = presentDateList.size();
//            presentDaysInMonth.setText("Present Days : " + totalPresentDay);
//            presentDaysInMonth.setTextColor(getResources().getColor(R.color.colorGreen500));

            totalLateMarksDay = lateMarkDateList.size();
            totalHolidaysDay = holidayDateList.size();
            //lateMarksInMonth.setText("Late Marks : " + totalLateMarksDay);
            //lateMarksInMonth.setTextColor(getResources().getColor(R.color.fabBtnColor));

            totalWeeklyOffDay = weeklyOffList.size();

            absentDateList.clear();
            absentDateList = new ArrayList<>(schoolDaysInMonthList);
            absentDateList.removeAll(presentDateList);
            absentDateList.removeAll(lateMarkDateList);
            absentDateList.removeAll(holidayDateList);
            absentDateList.removeAll(weeklyOffList);

            if (month > thisMonth && year > thisYear) {
                absentDateList.clear();
                presentDateList.clear();
                lateMarkDateList.clear();
                holidayDateList.clear();
                weeklyOffList.clear();
            }

            if (responseCounter == 1) {
                absentDateList.clear();
                presentDateList.clear();
                lateMarkDateList.clear();
                holidayDateList.clear();
                weeklyOffList.clear();
            } else {
                if (month == thisMonth) {
                    absentDateList.clear();
                    for (int i = 1; i <= thisDay; i++) {
                        String day = String.valueOf(i);
                        absentDateList.add(day);
                    }
                    absentDateList.removeAll(presentDateList);
                    absentDateList.removeAll(lateMarkDateList);
                    absentDateList.removeAll(sondaysInMonth);
                    absentDateList.removeAll(holidayDateList);
                    weeklyOffList.removeAll(holidayDateList);
                }
            }

            totalAbsentDay = absentDateList.size();
//            absentDaysInMonth.setText("Absent Days : " + totalAbsentDay);
//            absentDaysInMonth.setTextColor(getResources().getColor(R.color.colorred500));

            if (month == thisMonth && year == thisYear) {
                if (day_color[1].equals("WHITE")) {
                    if (!currentDayName.equals("Sunday")) {
                        int day = Integer.parseInt(day_color[0]);
                        if (day < thisDay) {

                            if(!weeklyOffList.contains(String.valueOf(day))){
                                gridcell.setOnClickListener(this);
                            }

                        }
                    }
                } else if (day_color[1].equals("BLUE")) {
                    if (!currentDayName.equals("Sunday")) {
                        int day = Integer.parseInt(day_color[0]);
                        if (day == thisDay) {

                            if(!weeklyOffList.contains(String.valueOf(day))){
                                gridcell.setOnClickListener(this);
                            }
                        }
                    }
                }
            } else if (day_color[1].equals("WHITE")) {
                if (!currentDayName.equals("Sunday")) {
                    int day = Integer.parseInt(day_color[0]);

                    if(!weeklyOffList.contains(String.valueOf(day))){
                        gridcell.setOnClickListener(this);
                    }


                }
            }

            imgPieChart.setOnClickListener(My_Attendance.this);
            imgBarGraph.setOnClickListener(My_Attendance.this);

            showPieChart();
            showBarGraph();

            //for event
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                if (eventsPerMonthMap.containsKey(theday)) {
//                    num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
                    Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
//                    num_events_per_day.setText(numEvents.toString());
                }
            }
            // Set the Day GridCell
            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);

            //checking to add day for remaining space of month and add next n previous months date

            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(getResources().getColor(R.color.lightgray02));
                gridcell.setBackgroundColor(getResources().getColor(R.color.colorwhite));
            }
            //checking for actual days of month
            else if (day_color[1].equals("WHITE")) {
                if (currentDayName.equals("Sunday")) {
                    String currentDay = day_color[0];
                    if (holidayDateList.contains(currentDay)) {
                        gridcell.setBackgroundColor(getResources().getColor(R.color.thirdFabBtn));
                    } else {
                        gridcell.setTextColor(getResources().getColor(R.color.attAbsentMark));
                        gridcell.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                    }

                } else {
                    if (responseCounter == 1) {
                        absentDateList.clear();
                        gridcell.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                    }
                    else {
                        String currentDay = day_color[0];
                        //for present
                        if (presentDateList.contains(currentDay)) {
                            gridcell.setBackgroundColor(getResources().getColor(R.color.colorGreen500));
                        }
                        //for late marks
                        else if (lateMarkDateList.contains(currentDay)) {
                            gridcell.setBackgroundColor(getResources().getColor(R.color.attLateMark));
                        } else if (holidayDateList.contains(currentDay)) {
                            gridcell.setBackgroundColor(getResources().getColor(R.color.thirdFabBtn));
                        }
                        else if (weeklyOffList.contains(currentDay)) {
                            gridcell.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        }
                        //for absent
                        else if (absentDateList.contains(currentDay)) {
                            if (month == thisMonth && year == thisYear) {
                                int day = Integer.parseInt(currentDay);
                                if (thisDay < day) {
                                    gridcell.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                                } else {
                                    gridcell.setBackgroundColor(getResources().getColor(R.color.attAbsentMark));
                                }
                            } else if (month > thisMonth && year > thisYear) {
                                absentDateList.clear();
                                gridcell.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                            } else {
                                gridcell.setBackgroundColor(getResources().getColor(R.color.attAbsentMark));
                            }
                        } else {
                            gridcell.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                        }
                    }
                }
            }
            //checking for current date
            else if (day_color[1].equals("BLUE")) {

                if (currentDayName.equals("Sunday")) {
                    String currentDay = day_color[0];
                    if (holidayDateList.contains(currentDay)) {
                        gridcell.setBackgroundColor(getResources().getColor(R.color.thirdFabBtn));
                    }
                    else {
                        gridcell.setTextColor(getResources().getColor(R.color.attAbsentMark));
                        gridcell.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                    }
                }
                else {
                    if (responseCounter == 1) {
                        absentDateList.clear();
                        gridcell.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                    } else {
                        if (thisMonth < month && year == thisYear) {
                            absentDateList.clear();
                            gridcell.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                        } else {
                            String currentDay = day_color[0];
                            //for present
                            if (presentDateList.contains(currentDay)) {
                                gridcell.setBackgroundColor(getResources().getColor(R.color.colorGreen500));
                            }
                            //for late marks
                            else if (lateMarkDateList.contains(currentDay)) {
                                gridcell.setBackgroundColor(getResources().getColor(R.color.attLateMark));
                            }
                            else if (holidayDateList.contains(currentDay)) {
                                gridcell.setBackgroundColor(getResources().getColor(R.color.thirdFabBtn));
                            }
                            else if (weeklyOffList.contains(currentDay)) {
                                gridcell.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            }
                            //for absent
                            else if (absentDateList.contains(currentDay)) {
                                gridcell.setBackgroundColor(getResources().getColor(R.color.attAbsentMark));
                            } else {
                                gridcell.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                            }
                        }
                    }
                }
            }
            return row;
        }

        @Override
        public void onClick(View view) {

            //add event for each date and show it{timing}
            if (view.getId() == R.id.calendar_day_gridcell) {
                // Attendance_Calender_Items listItems = new Attendance_Calender_Items();

                String position = (String) view.getTag();
                SimpleDateFormat inFormat = new SimpleDateFormat("d-MMM-yyyy");
                Date date = null;
                try {
                    date = inFormat.parse(position);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat outFormat = new SimpleDateFormat("d");
                String day = outFormat.format(date);
                String inTime = "";
                String clickedDate = "";
                String outTime = "";
                String attendanceType = "";
                String isHoliday = "";

                //Attendance_Calender_Items listData = new Attendance_Calender_Items();
                //listData = itemsDetails.contains(day);
                for (int i = 0; i < itemsDetails.size(); i++) {
                    listItems = itemsDetails.get(i);
                    if (listItems.getPresentDate().equals(day)) {
                        inTime = "In Time : " + listItems.getAttTime();
                        clickedDate = "Date : " + listItems.getAttDate();
                        outTime = "Out Time : " + listItems.getAtt_OutTime();
                        attendanceType = "Type : " + listItems.getAttType();
                        isHoliday = listItems.getPresentDate();

                        if (holidayDateList.contains(isHoliday)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(My_Attendance.this);
                            builder.setTitle("Message");
                            builder.setMessage("It is holiday,School is closed.");
                            AlertDialog alert1 = builder.create();
                            alert1.show();
                        }else{
                            final AlertDialog.Builder alert = new AlertDialog.Builder(My_Attendance.this, R.style.MyDialogTheme);

                            RelativeLayout layout = new RelativeLayout(My_Attendance.this);
                            layout.setLayoutParams(new RelativeLayout.LayoutParams(
                                    CoordinatorLayout.LayoutParams.MATCH_PARENT,
                                    CoordinatorLayout.LayoutParams.MATCH_PARENT));

                            TextView txtClickedDate = new TextView(My_Attendance.this);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            txtClickedDate.setLayoutParams(params);
                            params.setMargins(50, 50, 20, 20);
                            txtClickedDate.setId(Integer.parseInt("1"));
                            txtClickedDate.setText(clickedDate);

                            TextView txtInTime = new TextView(My_Attendance.this);
                            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.BELOW, txtClickedDate.getId());
                            params.setMargins(50, 50, 20, 20);
                            txtInTime.setLayoutParams(params);
                            txtInTime.setId(Integer.parseInt("2"));
                            txtInTime.setText(inTime);

                            TextView txtOutTime = new TextView(My_Attendance.this);
                            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.BELOW, txtInTime.getId());
                            params.setMargins(50, 50, 20, 20);
                            txtOutTime.setLayoutParams(params);
                            txtOutTime.setId(Integer.parseInt("3"));
                            txtOutTime.setText(outTime);

                            TextView txtAttendanceType = new TextView(My_Attendance.this);
                            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.BELOW, txtOutTime.getId());
                            params.setMargins(50, 50, 20, 20);
                            txtAttendanceType.setLayoutParams(params);
                            txtAttendanceType.setId(Integer.parseInt("4"));
                            txtAttendanceType.setText(attendanceType);

                            layout.addView(txtClickedDate);
                            layout.addView(txtInTime);
                            layout.addView(txtOutTime);
                            layout.addView(txtAttendanceType);

                            alert.setTitle(Html.fromHtml("<b>" + "Details" + "</b>"));
                            alert.setView(layout);
                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // recreate();
                                }
                            });
                            alert.show();
                        }
                    }
                }

            }
            String date_month_year = (String) view.getTag();
            try {
                Date parsedDate = dateFormatter.parse(date_month_year);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }
    }

    private void showPieChart() {

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(totalSchoolDaysInMonth, 0));
        entries.add(new Entry(totalPresentDay, 1));
        entries.add(new Entry(totalAbsentDay, 2));
        entries.add(new Entry(totalLateMarksDay, 3));
        entries.add(new Entry(totalHolidaysDay, 4));

        PieDataSet dataset = new PieDataSet(entries, "");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("School Days");
        labels.add("Present");
        labels.add("Absent");
        labels.add("Late Marks");
        labels.add("Holiday");

        PieData data = new PieData(labels, dataset);
        data.setValueTextSize(12);
        data.setValueTextColor(getResources().getColor(R.color.colorBlack));

        final int[] pieChartColor = {
                getResources().getColor(R.color.attTotalDays),
                getResources().getColor(R.color.colorGreen500),
                getResources().getColor(R.color.attAbsentMark),
                getResources().getColor(R.color.attLateMark),
                getResources().getColor(R.color.thirdFabBtn)
        };
        dataset.setColors(pieChartColor);
        pieChart.setDescription(" ");
        pieChart.setData(data);
        pieChart.animateY(5000);
    }

    private void showBarGraph() {

        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Total Days");
        xAxis.add("Present");
        xAxis.add("Absent");
        xAxis.add("Late");
        xAxis.add("Holiday");

        BarData barData = new BarData(xAxis, getBarDataSet());
        barChart.setData(barData);
        barChart.setDescription(" ");
        barChart.animateXY(5000, 5000);
        barChart.invalidate();

    }

    private ArrayList<BarDataSet> getBarDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> value = new ArrayList<>();
        BarEntry schoolDays = new BarEntry(totalSchoolDaysInMonth, 0); // School Days
        value.add(schoolDays);
        BarEntry present = new BarEntry(totalPresentDay, 1); // Present
        value.add(present);
        BarEntry absent = new BarEntry(totalAbsentDay, 2); // Absent
        value.add(absent);
        BarEntry lateMarks = new BarEntry(totalLateMarksDay, 3); // Late Marks
        value.add(lateMarks);
        BarEntry holidays = new BarEntry(totalHolidaysDay, 4); // Late Marks
        value.add(holidays);

        final int[] barGraphColor = {
                getResources().getColor(R.color.attTotalDays),
                getResources().getColor(R.color.colorGreen500),
                getResources().getColor(R.color.colorred500),
                getResources().getColor(R.color.fabBtnColor),
                getResources().getColor(R.color.thirdFabBtn)
        };

        BarDataSet barDataSet2 = new BarDataSet(value, "Legends");
        barDataSet2.setColors(barGraphColor);
        barDataSet2.setValueTextSize(12);
        barDataSet2.setValueTextColor(getResources().getColor(R.color.colorBlack));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet2);

        return dataSets;
    }
}
