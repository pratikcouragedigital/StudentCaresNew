package com.studentcares.spps.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.studentcares.spps.Graph_Tab_Monthly_Staff;
import com.studentcares.spps.Graph_Tab_Monthly_Student;

public class Graph_Tab_Monthly_PagerAdapter extends FragmentStatePagerAdapter

{
    String countP="";
    String countA="";
    int mNumOfTabs;
    private Fragment currentFragment;

    public Graph_Tab_Monthly_PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Graph_Tab_Monthly_Student tab1 = new Graph_Tab_Monthly_Student();
                return tab1;
            case 1:
                Graph_Tab_Monthly_Staff tab3 = new Graph_Tab_Monthly_Staff();
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