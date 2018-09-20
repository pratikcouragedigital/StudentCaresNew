package com.studentcares.spps.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.studentcares.spps.SMS_InBox_Tab_Attendance;
import com.studentcares.spps.SMS_InBox_Tab_Other;

public class SMS_Recieve_Pager_Adapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public SMS_Recieve_Pager_Adapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                SMS_InBox_Tab_Attendance tab1 = new SMS_InBox_Tab_Attendance();
                return tab1;
            case 1:
                SMS_InBox_Tab_Other tab3 = new SMS_InBox_Tab_Other();
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
