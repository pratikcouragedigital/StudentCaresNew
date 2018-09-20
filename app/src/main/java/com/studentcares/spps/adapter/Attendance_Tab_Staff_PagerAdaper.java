package com.studentcares.spps.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.studentcares.spps.Attendance_Tab_Staff_Absent_List;
import com.studentcares.spps.Attendance_Tab_Staff_Present_List;

public class Attendance_Tab_Staff_PagerAdaper extends FragmentStatePagerAdapter

{
    String countP="";
    String countA="";
    int mNumOfTabs;
    private Fragment currentFragment;

    public Attendance_Tab_Staff_PagerAdaper(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Attendance_Tab_Staff_Present_List tab1 = new Attendance_Tab_Staff_Present_List();
                return tab1;
            case 1:
                Attendance_Tab_Staff_Absent_List tab3 = new Attendance_Tab_Staff_Absent_List();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if(getCurrentFragment() != object) {
            currentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }
}