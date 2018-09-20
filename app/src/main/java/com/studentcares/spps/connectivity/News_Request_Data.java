package com.studentcares.spps.connectivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.*;
import com.studentcares.spps.model.Homework_Items;
import com.studentcares.spps.model.News_Items;
import com.studentcares.spps.sqlLite.DataBaseHelper;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class News_Request_Data {

    private static int currentPage;
    private static ProgressDialog progressDialogBox;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTask;
    private static List<News_Items> ItemsArrayForAsyncTask;
    private static DataBaseHelper mydb;


    private static Context context;
    private static String ResponseResult;
    private static String webMethName;
    private static String userId;
    private static String schoolId;
    private static String title;
    private static String description;
    private static String date;
    private static String image;
    private static ProgressDialog progressDialog;


    public News_Request_Data(News_Add news_add) {
        context = news_add;
    }

    public News_Request_Data(News_List news_list) {
        context = news_list;
    }


    public void AddSchoolNews(String idOfUser, String idOfSchool, String heading, String newsDescription, String selectedDateForSubmission, String firstImagePath, final ProgressDialog progressdialog) {
        userId = idOfUser;
        schoolId = idOfSchool;
        title = heading;
        description = newsDescription;
        date = selectedDateForSubmission;
        image = firstImagePath;
        progressDialog = progressdialog;

        webMethName = "News_Add";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("User_Id", userId);
            jsonObject.put("Added_Date", date);
            jsonObject.put("Title", title);
            jsonObject.put("Description", description);
            jsonObject.put("Image", image);
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
                            progressdialog.dismiss();
                            String res = response.getString("responseDetails");
                            if (res.equals("Successfully News Added.")) {

                                Intent gotoHomeList = new Intent(context, News_List.class);
                                gotoHomeList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoHomeList);

                            } else {
                                Toast.makeText(context," "+res, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            progressdialog.dismiss();
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressdialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void showNewsList(List<News_Items> newsItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfSchool, int pageno, ProgressDialog progressDialog) {
        schoolId = idOfSchool;
        currentPage = pageno;
        progressDialogBox = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = newsItems;

        webMethName = "News_List";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("student_id", userId);
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
                            if (res.equals("Not found News..!!")) {
                                Toast.makeText(context, "News Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        News_Items news_Items = new News_Items();
                                        news_Items.setListId(obj.getString("Id"));
                                        news_Items.settitle(obj.getString("Title"));
                                        news_Items.setaddedDate(obj.getString("Added_Date"));
                                        news_Items.setaddedByName(obj.getString("Name"));
                                        news_Items.setdescription(obj.getString("Descripton"));
                                        news_Items.setFirstImagePath(obj.getString("Image"));

                                        mydb = new DataBaseHelper(context);
                                        mydb.insertNewsDetails((obj.getString("Id")),(obj.getString("Title")), (obj.getString("Descripton")), (obj.getString("Added_Date")), (obj.getString("Name")), (obj.getString("Image")));

                                        ItemsArrayForAsyncTask.add(news_Items);
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

}
