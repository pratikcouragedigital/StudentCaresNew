package com.studentcares.spps.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.Notification;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Notification_Items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Get_Notification_List {

    private static Context context;
    private static String ResponseResult;
    private static String webMethName ;

    private static String schoolId ;
    private static ProgressDialog progressDialogBox;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTask;
    private static List<Notification_Items> ItemsArrayForAsyncTask;

    public Get_Notification_List(Notification notification) {
        context = notification;
    }


    public void NotificationList(List<Notification_Items> pdfItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfSchool, ProgressDialog progressDialog) {
        progressDialogBox = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = pdfItems;

        schoolId = idOfSchool;

        JSONObject jsonObject = new JSONObject();

        webMethName ="All_School_Notification";
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
                            progressDialogBox.dismiss();
                            ItemsArrayForAsyncTask.clear();
                            adapterForAsyncTask.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if(res.equals("No All School Notification Found")){
                                Toast.makeText(context, "Notification Not Available.", Toast.LENGTH_SHORT).show();
                            }else{
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);

                                        Notification_Items notificationItems = new Notification_Items();
                                        notificationItems.setListId(obj.getString("Notification_Id"));
                                        notificationItems.settitle(obj.getString("Title"));
                                        notificationItems.setmessage(obj.getString("Description"));

                                        ItemsArrayForAsyncTask.add(notificationItems);
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
