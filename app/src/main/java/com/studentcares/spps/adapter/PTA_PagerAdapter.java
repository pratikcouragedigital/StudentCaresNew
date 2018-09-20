package com.studentcares.spps.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.studentcares.spps.PTA_Members_Tab_Primary;
import com.studentcares.spps.PTA_Members_Tab_Secondary;


public class PTA_PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PTA_PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PTA_Members_Tab_Primary tab1 = new PTA_Members_Tab_Primary();
                return tab1;
            case 1:
                PTA_Members_Tab_Secondary tab3 = new PTA_Members_Tab_Secondary();
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