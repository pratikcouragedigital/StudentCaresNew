package com.studentcares.spps.connectivity;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.GPS;
import com.studentcares.spps.R;
import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User_List_Request {

    private static Context context;


    private static String webMethName;

    private static List<String> allNameList = new ArrayList<String>();
    private static List<String> allIdList = new ArrayList<String>();
    private static List<String> allStudentNameList = new ArrayList<String>();
    private static List<String> allStudentIdList = new ArrayList<String>();
    private static Std_Div_Filter_Adapter SpinnerAdapter;
    private static Std_Div_Filter_Adapter SpinnerAdapterStudent;
    String schoolId,standardId,divisionId;

    public User_List_Request(GPS gps) {
        context = gps;
    }


    public User_List_Request(FragmentActivity activity) {
        context = activity;
    }


    public void ShowStaffList(List<String> staffNameList, List<String> staffIdList, String idOfSchool, Std_Div_Filter_Adapter spinnerAdapter) {
        allNameList = staffNameList;
        allIdList = staffIdList;
        schoolId = idOfSchool;
        SpinnerAdapter = spinnerAdapter;

        webMethName = "GPS_Outwork_Staff_List";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = context.getString(R.string.url) + webMethName;
        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            allIdList.add("0");

                            String res = response.getString("responseDetails");
                            if (res.equals("Staff List Not Available.")) {
                                Toast.makeText(context, "Staff List Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);

                                        allIdList.add(obj.getString("Staff_Id"));
                                        allNameList.add(obj.getString("Staff_Name"));
                                    }

                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }


                        } catch (Exception e) {
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void ShowStudentList(List<String> studentNameList, List<String> studentIdList, String idOfSchool, String stdId, String divId, Std_Div_Filter_Adapter spinnerAdapter) {
        allStudentNameList = studentNameList;
        allStudentIdList = studentIdList;
        schoolId = idOfSchool;
        standardId = stdId;
        divisionId = divId;
        SpinnerAdapterStudent = spinnerAdapter;

        webMethName = "ClassWise_Student_List";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Standard_Id", standardId);
            jsonObject.put("Division_Id", divisionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = context.getString(R.string.url) + webMethName;
        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response2) {
                        try {
                            allStudentNameList.clear();
                            allStudentIdList.clear();
                            allStudentIdList.add("0");
                            allStudentNameList.add("Student Name");

                            String res2 = response2.getString("responseDetails");
                            if (res2.equals("Student List Not Available.")) {
                                Toast.makeText(context, "Student List Not Available", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                try {
                                    JSONArray jArr = response2.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);

                                        allStudentIdList.add(obj.getString("Student_Id"));
                                        allStudentNameList.add(obj.getString("Student_Name"));
                                    }

                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }

                        }
                        catch (Exception e) {
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });

    }

}
