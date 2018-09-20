package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.studentcares.spps.adapter.Holiday_Adapter;
import com.studentcares.spps.connectivity.Holiday_Details_Get;
import com.studentcares.spps.model.Holidays_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Holiday extends BaseActivity {

    String userType;
    String userId;
    String schoolId;

    private ProgressDialog progressDialog = null;
    List<Holidays_Items> holidaysItems = new ArrayList<Holidays_Items>();
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager sessionManagerNgo = new SessionManager(this);
        HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
        userType = typeOfUser.get(SessionManager.KEY_USERTYPE);
        userId = typeOfUser.get(SessionManager.KEY_USERID);
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);

        if(userType.equals("Student")){
            setContentView(R.layout.holiday_list);

            recyclerView = (RecyclerView) findViewById(R.id.holidayRecyclerView);
            recyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);

            adapter = new Holiday_Adapter(holidaysItems);
            recyclerView.setAdapter(adapter);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(this.getString(R.string.progress_msg));
            progressDialog.show();

            getList();

        }else{
//            if(userType.equals("UserAdmin")){
//                setContentView(R.layout.holiday);
//            }
//            else if (userType.equals("Staff") ||  userType.equals("NonTeachingStaff")) {
//                setContentView(R.layout.t_holidays);
//            }
            setContentView(R.layout.holiday);
            BottomNavigationView bottomNavigationView = (BottomNavigationView)
                    findViewById(R.id.bottomNavigation);

            if(userType.equals("UserAdmin")){

                bottomNavigationView.inflateMenu(R.menu.ua_bottom_navigation_items);
                bottomNavigationView.setOnNavigationItemSelectedListener
                        (new BottomNavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                Fragment selectedFragment = null;
                                switch (item.getItemId()) {
                                    case R.id.ua_i1_studentHoliday:
                                        selectedFragment = Holiday_List_Student.newInstance();
                                        break;
                                    case R.id.ua_i2_staffHoliday:
                                        selectedFragment = Holiday_List_Staff.newInstance();
                                        break;
                                    case R.id.ua_i3_addHoliday:
                                        selectedFragment = Holiday_Add.newInstance();
                                        break;
                                }
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_layout, selectedFragment);
                                transaction.commit();
                                return true;
                            }
                        });
            }else if (userType.equals("Staff") ||  userType.equals("NonTeachingStaff")) {

                bottomNavigationView.inflateMenu(R.menu.t_bottom_navigation_items);
                bottomNavigationView.setOnNavigationItemSelectedListener
                        (new BottomNavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                Fragment selectedFragment = null;
                                switch (item.getItemId()) {
                                    case R.id.t_i1_studentHoliday:
                                        selectedFragment = Holiday_List_Student.newInstance();
                                        break;
                                    case R.id.t_i2_staffHoliday:
                                        selectedFragment = Holiday_List_Staff.newInstance();
                                        break;
                                }
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_layout, selectedFragment);
                                transaction.commit();
                                return true;
                            }
                        });
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, Holiday_List_Student.newInstance());
            transaction.commit();
        }
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

    }
    private void getList(){
        try {
            Holiday_Details_Get UAHoliday_List = new Holiday_Details_Get(this);
            UAHoliday_List.showHolidayList(holidaysItems, recyclerView, adapter,userType,schoolId, progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}