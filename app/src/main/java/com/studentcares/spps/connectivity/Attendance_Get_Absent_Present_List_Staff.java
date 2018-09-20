package com.studentcares.spps.connectivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.Login;
import com.studentcares.spps.My_OTP;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Attendance_List_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class Attendance_Get_Absent_Present_List_Staff {

    private static Context context;
    private static String ResponseForPresent,ResponseForAbsent;
    private static String webMethName;
    private static String userId;
    private static String schoolId;
    private static ProgressDialog PDBPresentList,PDBAbsentList;


    private static RecyclerView.Adapter absentListAdapter, presentListAdapter;
    private static RecyclerView recyclerViewForAsyncTaskAbs,recyclerViewForAsyncTaskPre;
    private static List<Attendance_List_Items> ItemsArrayForAsyncTaskAbs,ItemsArrayForAsyncTaskPre;

    public Attendance_Get_Absent_Present_List_Staff(Context c) {
        context = c;
    }

    public void show_Staff_Absent_List(List<Attendance_List_Items> TAttendanseItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfUser, String idOfSchool, ProgressDialog progressDialog) {
        userId = idOfUser;
        schoolId = idOfSchool;
        PDBAbsentList = progressDialog;
        absentListAdapter = reviewAdapter;
        recyclerViewForAsyncTaskAbs = recyclerView;
        ItemsArrayForAsyncTaskAbs = TAttendanseItems;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String method ="Attendance_Staff_Absent_List";
        String url = context.getString(R.string.url) + method;

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(method)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            PDBAbsentList.dismiss();
                            ItemsArrayForAsyncTaskAbs.clear();
                            absentListAdapter.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if(res.equals("Invalid List")){
                                Toast.makeText(context, "Absent List Not Available", Toast.LENGTH_SHORT).show();
                            }else{
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        Attendance_List_Items TAttendanseItems = new Attendance_List_Items();
                                        TAttendanseItems.setstaffName(obj.getString("Name"));
                                        TAttendanseItems.setstaffId(obj.getString("Staff_Id"));
                                        TAttendanseItems.setFirstImagePath(obj.getString("Image"));
                                        ItemsArrayForAsyncTaskAbs.add(TAttendanseItems);
                                    }
                                    absentListAdapter.notifyDataSetChanged();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                        catch(Exception e) {
                            PDBAbsentList.dismiss();
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        PDBAbsentList.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void show_Present_Staff_List(List<Attendance_List_Items> TAttendanseItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfUser, String idOfSchool, ProgressDialog progressDialog) {
        userId = idOfUser;
        schoolId = idOfSchool;
        PDBPresentList = progressDialog;
        presentListAdapter = reviewAdapter;
        recyclerViewForAsyncTaskPre = recyclerView;
        ItemsArrayForAsyncTaskPre = TAttendanseItems;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String method ="Attendance_Staff_Present_List";
        String url = context.getString(R.string.url) + method;

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(method)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            PDBPresentList.dismiss();
                            ItemsArrayForAsyncTaskPre.clear();
                            presentListAdapter.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if(res.equals("Invalid List")){
                                Toast.makeText(context, "Present List Not Available", Toast.LENGTH_SHORT).show();
                            }else{
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        Attendance_List_Items TAttendanseItems = new Attendance_List_Items();
                                        TAttendanseItems.setstaffName(obj.getString("Name"));
                                        TAttendanseItems.setstaffId(obj.getString("Staff_Id"));
                                        TAttendanseItems.setFirstImagePath(obj.getString("Image"));
                                        TAttendanseItems.setinTime(obj.getString("InTime"));
                                        TAttendanseItems.setoutTime(obj.getString("OutTime"));
                                        ItemsArrayForAsyncTaskPre.add(TAttendanseItems);
                                    }
                                    presentListAdapter.notifyDataSetChanged();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                        catch(Exception e) {
                            PDBPresentList.dismiss();
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        PDBPresentList.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
