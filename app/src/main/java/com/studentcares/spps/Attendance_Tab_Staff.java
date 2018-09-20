package com.studentcares.spps;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.studentcares.spps.adapter.Attendance_Tab_Staff_PagerAdaper;
import com.studentcares.spps.singleton.AttendanceCount;

public class Attendance_Tab_Staff extends BaseActivity {

    private View v;
    TabLayout tabLayout;
    String countPresent ="";
    String countAbsent ="";
    AttendanceCount attendanceCount;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);

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

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setTitle("Attendance");
//        }

        //countPresent = attendanceCount.getcountPresent();
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Present"));
        tabLayout.addTab(tabLayout.newTab().setText("Absent"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final boolean mStopHandler = false;
        final Handler mHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(!mStopHandler) {
                    countPresent = attendanceCount.getcountPresent_Staff();
                    countAbsent = attendanceCount.getcountAbsent_Staff();
                    tabLayout.getTabAt(0).setText("Present "+"  ("+countPresent+")");
                    tabLayout.getTabAt(1).setText("Absent "+"  ("+countAbsent+")");
                    mHandler.postDelayed(this, 2000);
                }
            }
        };
        mHandler.postDelayed(runnable, 3000);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        final Attendance_Tab_Staff_PagerAdaper adapter = new Attendance_Tab_Staff_PagerAdaper(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
