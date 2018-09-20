package com.studentcares.spps.connectivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.DashBoard_Count;
import com.studentcares.spps.R;
import com.studentcares.spps.model.DashBoard_Count_Items;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DashBoard_Count_Get {

    private static Context context;
    private static String ResponseResult;

    private static String webMethName;
    private static String schoolId;
    private static ProgressDialog progressDialogBox;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTask;
    private static List<DashBoard_Count_Items> ItemsArrayForAsyncTask;


    public DashBoard_Count_Get(DashBoard_Count dashBoardCount) {
        context = dashBoardCount;
    }

    public void ShowDashBordData(List<DashBoard_Count_Items> dashBoard_Count_items, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfSchool, ProgressDialog progressDialog) {
//    public void ShowDashBordData(List<DashBoard_Count_Items> dashBoard_Count_items, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfSchool) {

        progressDialogBox = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = dashBoard_Count_items;
        schoolId = idOfSchool;

        webMethName = "DashBoardCount";
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
                        progressDialogBox.dismiss();
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Group Masters Not Available")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Result");
                                builder.setMessage("Count Not Available");
                                AlertDialog alert1 = builder.create();
                                alert1.show();
                            }
                            else {
                                try {
                                    ItemsArrayForAsyncTask.clear();
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        try {
                                            JSONObject obj = jArr.getJSONObject(count);
                                            DashBoard_Count_Items dashBoardItems = new DashBoard_Count_Items();

                                            dashBoardItems.setgroupName(obj.getString("groupName"));
                                            dashBoardItems.settotalStudent(obj.getString("totalStudent"));
                                            dashBoardItems.setpresentStudent(obj.getString("presentStudent"));
                                            dashBoardItems.setabsentStudent(obj.getString("absentStudent"));
                                            dashBoardItems.setnotPunchStudent(obj.getString("notPunchStudent"));
                                            dashBoardItems.setgroupId(obj.getString("group_Id"));
                                            ItemsArrayForAsyncTask.add(dashBoardItems);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
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
                        progressDialogBox.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });

    }
}
