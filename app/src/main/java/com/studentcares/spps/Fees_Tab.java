package com.studentcares.spps;

import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.studentcares.spps.adapter.Fees_PagerAdapter;
import com.studentcares.spps.internetConnectivity.NetworkChangeReceiver;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.HashMap;

public class Fees_Tab extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Toolbar toolbar;
    SessionManager sessionManager;
    String userType;
    Fees_PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fees_tab);

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
//            getSupportActionBar().setTitle("Fees");
//        }
        sessionManager = new SessionManager(this);
        HashMap<String, String> typeOfUser2 = sessionManager.getUserDetails();
        schoolId = typeOfUser2.get(SessionManager.KEY_SCHOOLID);
        userType = typeOfUser2.get(SessionManager.KEY_USERTYPE);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);

        if (userType.equals("Student")){
            tabLayout.addTab(tabLayout.newTab().setText("Paid"));
            tabLayout.addTab(tabLayout.newTab().setText("Unpaid"));

        }else{
            tabLayout.addTab(tabLayout.newTab().setText("Not Paid"));
            tabLayout.addTab(tabLayout.newTab().setText("Balance"));
            //tabLayout.addTab(tabLayout.newTab().setText("Paid"));


        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new Fees_PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount(), viewPager, tabLayout,userType);
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