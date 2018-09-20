package com.studentcares.spps.adapter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.studentcares.spps.Attendance_Tab_Stud_Absent_List;
import com.studentcares.spps.Attendance_Tab_Stud_Present_List;


public class Attendance_Tab_Stud_PagerAdapter extends FragmentStatePagerAdapter {

    private final ViewPager viewPager;
    private final TabLayout tabLayout;
    int mNumOfTabs;

    public Attendance_Tab_Stud_PagerAdapter(FragmentManager fm, int NumOfTabs, ViewPager viewPager, TabLayout tabLayout) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.viewPager = viewPager;
        this.tabLayout = tabLayout;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Attendance_Tab_Stud_Present_List tab1 = new Attendance_Tab_Stud_Present_List();
                tab1.newInstance(viewPager, tabLayout, this);
                return tab1;
            case 1:
                Attendance_Tab_Stud_Absent_List tab2 = new Attendance_Tab_Stud_Absent_List();
                tab2.newInstance(viewPager, tabLayout, this);
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}