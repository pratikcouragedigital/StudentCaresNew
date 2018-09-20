package com.studentcares.spps.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.studentcares.spps.Graph_Tab_Daily_Staff;
import com.studentcares.spps.Graph_Tab_Daily_Student;


public class Graph_Tab_Daily_PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public Graph_Tab_Daily_PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Graph_Tab_Daily_Student tab1 = new Graph_Tab_Daily_Student();
                return tab1;
            case 1:
                Graph_Tab_Daily_Staff tab3 = new Graph_Tab_Daily_Staff();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}