package com.studentcares.spps.adapter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.studentcares.spps.Fees_Tab_Teacher_Balance;
import com.studentcares.spps.Fees_Tab_Teacher_NotPaid;
import com.studentcares.spps.Fees_Tab_Parents_Paid;
import com.studentcares.spps.Fees_Tab_Parents_Unpaid;

public class Fees_PagerAdapter extends FragmentStatePagerAdapter {
    private final ViewPager viewPager;
    private final TabLayout tabLayout;
    int mNumOfTabs;
    String userType;

    public Fees_PagerAdapter(FragmentManager fm, int NumOfTabs, ViewPager viewPager, TabLayout tabLayout, String userType) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.viewPager = viewPager;
        this.tabLayout = tabLayout;
        this.userType = userType;
    }

    @Override
    public Fragment getItem(int position) {
        if(userType.equals("Student")){

            switch (position) {
                case 0:
                    Fees_Tab_Parents_Paid tab1 = new Fees_Tab_Parents_Paid();
                    return tab1;
                case 1:
                    Fees_Tab_Parents_Unpaid tab2 = new Fees_Tab_Parents_Unpaid();
                    return tab2;

                default:
                    return null;
            }
        }else{
            switch (position) {

                case 0:
                    Fees_Tab_Teacher_NotPaid tab1 = new Fees_Tab_Teacher_NotPaid();
                    tab1.newInstance(viewPager, tabLayout, this);
                    return tab1;
                case 1:
                    Fees_Tab_Teacher_Balance tab2 = new Fees_Tab_Teacher_Balance();
                    tab2.newInstance(viewPager, tabLayout, this);
                    return tab2;
//            case 2:
//                Fees_Tab_Parents_Unpaid tab3 = new Fees_Tab_Parents_Unpaid();
//                return tab3;
                default:
                    return null;
            }
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}