package com.studentcares.spps.sliderActivity;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_FIRST_TIME_GUIDE_T_HOMEWORK_FILTER = "IsFirstTimeGuideTHomeworkFilter";
    private static final String IS_FIRST_TIME_GUIDE_HOMEWORK_FILTER = "IsFirstTimeGuideHomeworkFilter";
    private static final String IS_FIRST_TIME_GUIDE_ADD_NOTICE = "IsFirstTimeGuideAddNotice";
    private static final String IS_FIRST_TIME_GUIDE_ATTENDANCE = "IsFirstTimeGuideAttendance";
    private static final String IS_FIRST_TIME_GUIDE_ABSENTLIST = "IsFirstTimeGuideAbsentList";
    private static final String IS_FIRST_TIME_GUIDE_UA_ABSENTLIST = "IsFirstTimeGuideUaAbsentList";
    private static final String IS_FIRST_TIME_GUIDE_ADD_CHILD = "IsFirstTimeGuideAddChild";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeGuideAttendance(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_GUIDE_ATTENDANCE, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeGuideAttendance() {
        return pref.getBoolean(IS_FIRST_TIME_GUIDE_ATTENDANCE, true);
    }

    public void setFirstTimeGuideHomewrokFilter(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_GUIDE_HOMEWORK_FILTER, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeGuide_T_HomeworkFilter() {
        return pref.getBoolean(IS_FIRST_TIME_GUIDE_T_HOMEWORK_FILTER, true);
    }


    public void setFirstTimeGuide_T_HomewrokFilter(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_GUIDE_T_HOMEWORK_FILTER, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeGuideHomeworkFilter() {
        return pref.getBoolean(IS_FIRST_TIME_GUIDE_HOMEWORK_FILTER, true);
    }


    public void setFirstTimeGuideAbsentList(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_GUIDE_ABSENTLIST, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeGuideAbsentList() {
        return pref.getBoolean(IS_FIRST_TIME_GUIDE_ABSENTLIST, true);
    }


    public void setFirstTimeGuideAddNotice(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_GUIDE_ADD_NOTICE, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeGuideAddNotice() {
        return pref.getBoolean(IS_FIRST_TIME_GUIDE_ADD_NOTICE, true);
    }

    public void setFirstTimeGuide_Ua_AbsentList(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_GUIDE_UA_ABSENTLIST, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeGuide_Ua_AbsentList() {
        return pref.getBoolean(IS_FIRST_TIME_GUIDE_UA_ABSENTLIST, true);
    }

    public void setFirstTimeGuide_AddChild(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_GUIDE_ADD_CHILD, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeGuide_AddChild() {
        return pref.getBoolean(IS_FIRST_TIME_GUIDE_ADD_CHILD, true);
    }

}
