package com.studentcares.spps;

import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.studentcares.spps.adapter.Attendance_Tab_Stud_PagerAdapter;
import com.studentcares.spps.internetConnectivity.NetworkChangeReceiver;
import com.studentcares.spps.singleton.AttendanceCount;

public class Attendance_Tab_Student extends BaseActivity {

    private View v;
    TabLayout tabLayout;
    String countPresent ="";
    String countAbsent ="";
    AttendanceCount attendanceCount;
    private ViewPager viewPager;
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
                    countPresent = attendanceCount.getcountPresentStudent();
                    countAbsent = attendanceCount.getcountAbsentStudent();
                    tabLayout.getTabAt(0).setText("Present "+"  ("+countPresent+")");
                    tabLayout.getTabAt(1).setText("Absent "+"  ("+countAbsent+")");
                    mHandler.postDelayed(this, 2000);
                }
            }
        };
        mHandler.postDelayed(runnable, 3000);
        viewPager = (ViewPager) findViewById(R.id.pager);

//        prefManager = new PrefManager(this);
//        boolean abs = prefManager.isFirstTimeGuide_Ua_AbsentList();
//        if (prefManager.isFirstTimeGuide_Ua_AbsentList()) {
//            RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
//            lps.setMargins(margin, margin, margin, margin);
//
//            // ViewTarget target = new ViewTarget(R.id.mainFabBtn, this);
//            sv = new ShowcaseView.Builder(this)
//                    .withMaterialShowcase()
////                    .setTarget(target)
//                    .setContentTitle(R.string.showcase_ua_absentStudent_title)
//                    .setContentText(R.string.showcase_ua_absentStudent_message)
//                    .setStyle(R.style.CustomShowcaseTheme2)
//                    .withHoloShowcase()
//                    .build();
//            sv.setButtonPosition(lps);
//            prefManager.setFirstTimeGuide_Ua_AbsentList(false);
//        }

        Attendance_Tab_Stud_PagerAdapter adapter = new Attendance_Tab_Stud_PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount(), viewPager, tabLayout);
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
}