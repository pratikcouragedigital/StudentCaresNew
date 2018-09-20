package com.studentcares.spps.connectivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.Holiday;
import com.studentcares.spps.Notice_List;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Holidays_Items;
import com.studentcares.spps.model.Notice_Items;
import com.studentcares.spps.sqlLite.DataBaseHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Holiday_Details_Get {

    private static Context context;
    private static String ResponseResultHolidayList;
    private static String webMethName;
    private static String holidayFor;
    private static String schoolId;
    private static ProgressDialog PDBHolidayList;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTask;
    private static List<Holidays_Items> ItemsArrayForAsyncTask;

    private static String ResponseResult;
    private static String userId;
    private static String holidayName;
    private static String userType;
    private static String fromDate;
    private static String toDate;
    private static ProgressDialog progressDialog;


    public Holiday_Details_Get(FragmentActivity activity) {
        context = activity;
    }

    public Holiday_Details_Get(Context context) {
        context = context;
    }

    public void addHoliday(String idOfUser, String idOfSchool, String typeOfUser, String nameOfHoliday, String holidayFromDate, String holidayToDate, String holidayForUser, ProgressDialog pDialog) {
        userId = idOfUser;
        schoolId = idOfSchool;
        userType = typeOfUser;
        holidayName = nameOfHoliday;
        fromDate = holidayFromDate;
        toDate = holidayToDate;
        holidayFor = holidayForUser;
        progressDialog = pDialog;

        webMethName = "Holiday_Add";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("User_Id", userId);
            jsonObject.put("Holiday_Name", holidayName);
            jsonObject.put("From_Date", fromDate);
            jsonObject.put("To_Date", toDate);
            jsonObject.put("Usertype", holidayFor);
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
                        progressDialog.dismiss();
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Successfully Holiday Added.")) {

                                Intent gotoHomeList = new Intent(context, Holiday.class);
                                gotoHomeList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoHomeList);

                            } else {
                                Toast.makeText(context," "+res, Toast.LENGTH_SHORT).show();
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
                        progressDialog.dismiss();
                    }
                });
    }


    public void showHolidayList(List<Holidays_Items> holidaysItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String userHoliday, String idOfSchool, ProgressDialog progressDialog) {
        holidayFor = userHoliday;
        schoolId = idOfSchool;
        PDBHolidayList = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = holidaysItems;

        webMethName = "Holiday_List";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Usertype", holidayFor);
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
                            PDBHolidayList.dismiss();
                            ItemsArrayForAsyncTask.clear();
                            adapterForAsyncTask.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if (res.equals("Not found Holiday")) {
                                Toast.makeText(context, "Holiday Not Added", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        Holidays_Items holidaysItems = new Holidays_Items();
                                        holidaysItems.setholidayName(obj.getString("Holiday_Name"));
                                        holidaysItems.setholidayId(obj.getString("Id"));
                                        holidaysItems.setfromDate(obj.getString("From_Date"));
                                        holidaysItems.settoDate(obj.getString("To_Date"));
                                        ItemsArrayForAsyncTask.add(holidaysItems);
                                    }
                                    adapterForAsyncTask.notifyDataSetChanged();
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
                        PDBHolidayList.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }


}
