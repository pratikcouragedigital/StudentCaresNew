package com.studentcares.spps.connectivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.GPS;
import com.studentcares.spps.GPS_Staff_OutWork;
import com.studentcares.spps.R;
import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.model.GPS_Outwork_Items;
import com.studentcares.spps.sqlLite.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GPS_StaffOutWork {

    private static Context context;
    private static String ResponseResult;
    private static String webMethName;
    private static double Latitude, Longitude;
    private static String schoolId, staffId, date,time;
    float accuracy;
    private static ProgressDialog progressDialogBox;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTask;
    private static List<GPS_Outwork_Items> ItemsArrayForAsyncTask;


    private static List<String> allNameList = new ArrayList<String>();
    private static List<String> allIdList = new ArrayList<String>();
    private static Std_Div_Filter_Adapter SpinnerAdapter;


    public GPS_StaffOutWork(GPS_Staff_OutWork gps_staff_outWork) {
        context = gps_staff_outWork;
    }

    public void GetStaffLocation(List<GPS_Outwork_Items> listItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfSchool, String idOfStaff, String selectedDate, ProgressDialog progressDialog) {

        schoolId = idOfSchool;
        staffId = idOfStaff;
        date = selectedDate;
        progressDialogBox = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = listItems;

        JSONObject jsonObject = new JSONObject();

        webMethName = "GPS_Outwork_Get_Staff_Location";
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Staff_Id", staffId);
            jsonObject.put("Date", date);

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
                            progressDialogBox.dismiss();
                            ItemsArrayForAsyncTask.clear();
                            adapterForAsyncTask.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if (res.equals("Location Not Available.")) {
                                Toast.makeText(context, "Location Not Available For This Date.", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        GPS_Outwork_Items gpsOutworkItems = new GPS_Outwork_Items();
                                        gpsOutworkItems.setlatitude(obj.getString("Latitude"));
                                        gpsOutworkItems.setlongitude(obj.getString("Longitude"));
                                        gpsOutworkItems.setdate(obj.getString("Date"));
                                        ItemsArrayForAsyncTask.add(gpsOutworkItems);
                                    }
                                    adapterForAsyncTask.notifyDataSetChanged();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            progressDialogBox.dismiss();
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialogBox.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void AddStaffOutworkLocation(String IdOfStaff, String IdOfSchool,float gpsAccuracy, double lat, double lon, String locationDate, String locationTime) {

        staffId = IdOfStaff;
        schoolId = IdOfSchool;
        Latitude = lat;
        Longitude = lon;
        date = locationDate;
        time = locationTime;
        accuracy = gpsAccuracy;

        webMethName = "GPS_Outwork_Add_Staff_Location";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Staff_Id", staffId);
            jsonObject.put("Latitude", Latitude);
            jsonObject.put("Longitude", Longitude);
            jsonObject.put("Location_Date", date);
            jsonObject.put("Location_Time", time);
            jsonObject.put("Accuracy", accuracy);

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
                            String res = response.getString("responseDetails");
                            if (res.equals("Location Added Successfully.")) {

                                String abc = "";
                            }
//                            Location Added Successfully.
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

}
