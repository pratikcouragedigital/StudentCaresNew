package com.studentcares.spps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.studentcares.spps.adapter.Home_Sub_Menu_Adapter;
import com.studentcares.spps.model.HomeGrid_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Home_Sub_Menus extends BaseActivity {

    public Integer[] mThumbIds;
    public String[] title;
    public int[] titleId;
    String subMenuFor, userType,subtitle;

    private RecyclerView attendanceMenuRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    public List<HomeGrid_Items> listItems = new ArrayList<HomeGrid_Items>();
    private Home_Sub_Menu_Adapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_sub_menu);


        SessionManager sessionManagerNgo = new SessionManager(this);
        HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
        userId = typeOfUser.get(SessionManager.KEY_USERID);
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
        userType = typeOfUser.get(SessionManager.KEY_USERTYPE);

        Intent intent = getIntent();
        if (null != intent) {
            subMenuFor = intent.getStringExtra("subMenuFor");
        }

        if (subMenuFor.equals("Attendance")) {
            if (userType.equals("UserAdmin")) {
                mThumbIds = new Integer[]{
                        R.drawable.menu_attendance_student, R.drawable.menu_attendance_staff,
                        R.drawable.menu_attendance_emergency_exit,
                        R.drawable.menu_attendance_own,
                };
                title = new String[]{"Student Attendance", "Staff Attendance", "Emergency Exit", "Own Attendance"};

                titleId = new int[]{1, 2, 3, 4};
            }
            else {
                mThumbIds = new Integer[]{
                        R.drawable.menu_attendance_student,
                        R.drawable.menu_attendance_emergency_exit,
                        R.drawable.menu_attendance_own,
                };
                title = new String[]{"Student Attendance", "Emergency Exit", "Own Attendance"};

                titleId = new int[]{1, 2, 3};
            }

        }
        else if (subMenuFor.equals("Graph")) {

            mThumbIds = new Integer[]{
                    R.drawable.menu_graph_daily, R.drawable.menu_graph_monthly,
            };
            title = new String[]{"Monthly Graph", "Daily Graph"};

            titleId = new int[]{1, 2};

        }
        else if (subMenuFor.equals("SMS")) {
            mThumbIds = new Integer[]{
                    R.drawable.menu_sms_send, R.drawable.menu_sms_receive, R.drawable.menu_sms_report,
            };
            title = new String[]{"SMS Send", "SMS Receive","SMS Report"};

            titleId = new int[]{1, 2,3};
        }
        else if (subMenuFor.equals("TimeTable")) {
            if (userType.equals("Student")) {
                mThumbIds = new Integer[]{
                        R.drawable.menu_timetable_own, R.drawable.menu_timetable_exam,
                };
                title = new String[]{"Class Wise Timetable", "Exam Timetable"};

                titleId = new int[]{1, 2};
            } else {
                mThumbIds = new Integer[]{
                        R.drawable.menu_timetable_classwise, R.drawable.menu_timetable_exam,
                        R.drawable.menu_timetable_own,
                };
                title = new String[]{"Class Wise Timetable", "Exam Timetable", "Own Timetable"};

                titleId = new int[]{1, 2, 3};
            }
        }

        else if (subMenuFor.equals("GPS")) {

            mThumbIds = new Integer[]{
                    R.drawable.menu_gps_personal, R.drawable.menu_gps_bus, R.drawable.menu_gps_myoutwork, R.drawable.menu_staff_tracker,
            };
            title = new String[]{"Personal GPS Tracker", "Bus GPS Tracker", "My Outwork Location", "Track Staff"};

            titleId = new int[]{1, 2, 3, 4};

        }

        else if (subMenuFor.equals("Leave")) {

            if (userType.equals("UserAdmin")) {

                mThumbIds = new Integer[]{
                        R.drawable.menu_leave_apply, R.drawable.menu_leave_approve, R.drawable.menu_leave_own,
                };
                title = new String[]{"Apply Leave", "Approve Leave", "Own Leave List"};

                titleId = new int[]{1, 2, 3};
            } else {
                mThumbIds = new Integer[]{
                        R.drawable.menu_leave_apply, R.drawable.menu_leave_own,
                };
                title = new String[]{"Apply Leave", "Own Leave List"};

                titleId = new int[]{1, 2};
            }

        }

        attendanceMenuRecyclerView = (RecyclerView) findViewById(R.id.attendanceMenuRecyclerView);
        attendanceMenuRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        attendanceMenuRecyclerView.setLayoutManager(linearLayoutManager);
        attendanceMenuRecyclerView.setNestedScrollingEnabled(false);

        attendanceMenuRecyclerView.smoothScrollToPosition(0);
        listAdapter = new Home_Sub_Menu_Adapter(listItems, subMenuFor);
        attendanceMenuRecyclerView.setAdapter(listAdapter);

        AddGridMenu();

        ActivityInfo activityInfo = null;
        try {
            activityInfo = getPackageManager().getActivityInfo(
                    getComponentName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String title = activityInfo.loadLabel(getPackageManager())
                .toString();

        txtActivityName.setText(subMenuFor+" "+title);
    }

    private void AddGridMenu() {

        int size = mThumbIds.length;

        for (int i = 0; i < size; i++) {
            HomeGrid_Items gridViewItems = new HomeGrid_Items();
            gridViewItems.setFirstImagePath(mThumbIds[i]);
            gridViewItems.settitle(title[i]);
            gridViewItems.setgridId(titleId[i]);
            listItems.add(gridViewItems);
        }
        listAdapter.notifyDataSetChanged();
    }

}
