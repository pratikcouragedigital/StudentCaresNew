package com.studentcares.spps.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.Machine_Details;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Attendance_List_Items;
import com.studentcares.spps.model.Machine_Info_Items;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Machine_Details_Get {

    private static Context context;
    private static String ResponseResult;
    private static String standard;
    private static String webMethName;
    private static String schoolId;
    private static ProgressDialog progressDialogBox;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTask;
    private static List<Machine_Info_Items> ItemsArrayForAsyncTask;



    public Machine_Details_Get(Machine_Details machineDetails) {
        context = machineDetails;
    }


    public void showMachineDetails(List<Machine_Info_Items> listItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfSchool, ProgressDialog progressDialog) {
        schoolId = idOfSchool;
        progressDialogBox = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = listItems;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String method ="Machine_Details";
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
                            progressDialogBox.dismiss();
                            ItemsArrayForAsyncTask.clear();
                            adapterForAsyncTask.notifyDataSetChanged();

                            String res = response.getString("responseDetails");
                            if(res.equals("Invalid List")){
                            }else{
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        Machine_Info_Items machineInfoItems = new Machine_Info_Items();
                                        machineInfoItems.setMachine_No(obj.getString("Machine_No"));
                                        machineInfoItems.setStatus(obj.getString("Status"));
                                        ItemsArrayForAsyncTask.add(machineInfoItems);
                                    }
                                    adapterForAsyncTask.notifyDataSetChanged();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                        catch(Exception e) {
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

}
