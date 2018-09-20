package com.studentcares.spps.connectivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.studentcares.spps.sqlLite.DataBaseHelper;
import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Get_StdDivSub_Sqlite {

    private static Context context;
    private static Std_Div_Filter_Adapter SpinnerAdapter;
    private static List<String> allNameList = new ArrayList<String>();
    private static List<String> allIdList = new ArrayList<String>();
    private static ProgressDialog progressDialogBox;

    String userSelectedStandard;
    String userSelectedDivision;
    String schoolId;

    private DataBaseHelper mydb;

    public Get_StdDivSub_Sqlite(FragmentActivity activity) {
        context = activity;
    }

    public Get_StdDivSub_Sqlite(Context princ_attendance) {
        context = princ_attendance;
    }


    public void FetchAllstandard(List<String> standardNameList, List<String> standardIdList, String idOfSchool, Std_Div_Filter_Adapter spinnerAdapter) {
        allNameList = standardNameList;
        allIdList = standardIdList;
        schoolId = idOfSchool;
        SpinnerAdapter = spinnerAdapter;
        mydb = new DataBaseHelper(context);

        JSONArray userListArray = null;
        try {
            allNameList.clear();
            allIdList.clear();
            SpinnerAdapter.notifyDataSetChanged();
            userListArray = mydb.getAllStandardForSpinner();

            allNameList.add("Standard");
            allIdList.add("0");
            for (int i = 0; i < userListArray.length(); i++) {
                try {
                    JSONObject obj = userListArray.getJSONObject(i);
                    allNameList.add(obj.getString("Standard_Name"));
                    allIdList.add(obj.getString("StandardId"));
                    SpinnerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void FetchAllDivision(List<String> nameList, List<String> idList, String idOfSchool, Std_Div_Filter_Adapter spinnerAdapter, String standardId, ProgressDialog divisionDialogBox) {
        allNameList = nameList;
        allIdList = idList;
        schoolId = idOfSchool;
        userSelectedStandard = standardId;
        SpinnerAdapter = spinnerAdapter;
        progressDialogBox = divisionDialogBox;
        mydb = new DataBaseHelper(context);

        JSONArray userListArray = null;
        try {
            allNameList.clear();
            allIdList.clear();
            SpinnerAdapter.notifyDataSetChanged();
            userListArray = mydb.getAllDivisionForSpinner(userSelectedStandard);
            progressDialogBox.dismiss();
            allNameList.add("Division");
            allIdList.add("0");
            for (int i = 0; i < userListArray.length(); i++) {
                try {
                    JSONObject obj = userListArray.getJSONObject(i);
                    allNameList.add(obj.getString("Division_Name"));
                    allIdList.add(obj.getString("DivisionId"));
                    SpinnerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void FetchAllSubject(List<String> nameList, List<String> idList, String idOfSchool, Std_Div_Filter_Adapter spinnerAdapter, String standardId, String divisionId ) {
        allNameList = nameList;
        allIdList = idList;
        schoolId = idOfSchool;
        SpinnerAdapter = spinnerAdapter;
        userSelectedStandard = standardId;
        userSelectedDivision = divisionId;
        mydb = new DataBaseHelper(context);

        JSONArray userListArray = null;
        try {
            allNameList.clear();
            allIdList.clear();
            SpinnerAdapter.notifyDataSetChanged();
            userListArray = mydb.getAllSubjectForSpinner(userSelectedStandard,userSelectedDivision);

            allNameList.add("Subject");
            allIdList.add("0");
            for (int i = 0; i < userListArray.length(); i++) {
                try {
                    JSONObject obj = userListArray.getJSONObject(i);
                    allNameList.add(obj.getString("Subject_Name"));
                    allIdList.add(obj.getString("SubjectId"));
                    SpinnerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void FetchSubjectForSyllabus(List<String> nameList, List<String> idList, String idOfSchool, Std_Div_Filter_Adapter spinnerAdapter, String standardId ) {
        allNameList = nameList;
        allIdList = idList;
        schoolId = idOfSchool;
        SpinnerAdapter = spinnerAdapter;
        userSelectedStandard = standardId;
        mydb = new DataBaseHelper(context);

        JSONArray userListArray = null;
        try {
            allNameList.clear();
            allIdList.clear();
            SpinnerAdapter.notifyDataSetChanged();
            userListArray = mydb.getSubjectForSyllabus(userSelectedStandard);
            allNameList.add("Subject");
            allIdList.add("0");
            for (int i = 0; i < userListArray.length(); i++) {
                try {
                    JSONObject obj = userListArray.getJSONObject(i);
                    allNameList.add(obj.getString("Subject_Name"));
                    allIdList.add(obj.getString("SubjectId"));
                    SpinnerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
