package com.studentcares.spps.connectivity;


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
import com.studentcares.spps.About_My_School;

import com.studentcares.spps.Notice_List;
import com.studentcares.spps.R;
import com.studentcares.spps.School_Feedback;
import com.studentcares.spps.model.Notice_Items;
import com.studentcares.spps.model.SchoolReviewsListItems;

import com.studentcares.spps.sqlLite.DataBaseHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class School_Rating_Reviews_Get {

    private static Context context;

    private static String webMethName;
    private static String schoolId;
    private static String idOfUser;

    private static String schoolRating;
    private static String feedbackOfUser;

    private static String ResponseResultGetRatings,ResponseResultAddRating;
    private static RecyclerView.Adapter adapterGetRatings;
    private static RecyclerView recyclerViewGetRatings;
    private static List<SchoolReviewsListItems> ItemsArrayGetRatings;
    private static ProgressDialog PDBAddRating;

    public School_Rating_Reviews_Get(About_My_School about_my_school) {
        context = about_my_school;
    }

    public School_Rating_Reviews_Get(School_Feedback schoolFeedback) {
        context = schoolFeedback;
    }

    public void addreview(String userId,String idOfSchool, String schoolRatingValue, String userFeedback, ProgressDialog pd) {

        idOfUser = userId;
        schoolId = idOfSchool;
        schoolRating = schoolRatingValue;
        feedbackOfUser = userFeedback;
        PDBAddRating = pd;

        webMethName = "School_Rating_Review_Add";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("userId", idOfUser);
            jsonObject.put("ratings", schoolRating);
            jsonObject.put("review", feedbackOfUser);
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
                            PDBAddRating.dismiss();
                            String res = response.getString("responseDetails");
                            if (res.equals("Review Added Successfully.")) {

                                Intent gotoHomeList = new Intent(context, About_My_School.class);
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
                        PDBAddRating.dismiss();
                    }
                });
    }


    public void GetSchool_Rating_Reviews(List<SchoolReviewsListItems> newsItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfSchool ) {
        schoolId = idOfSchool;
        adapterGetRatings = reviewAdapter;
        recyclerViewGetRatings = recyclerView;
        ItemsArrayGetRatings = newsItems;

        webMethName = "School_Rating_Review_List";
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

                            ItemsArrayGetRatings.clear();
                            adapterGetRatings.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if (res.equals("Review Not Available.")) {
                                Toast.makeText(context, "Reviews Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        SchoolReviewsListItems schoolReviewsListItems = new SchoolReviewsListItems();
                                        schoolReviewsListItems.setschool_rating(obj.getString("Ratings"));
                                        schoolReviewsListItems.setschool_reviews(obj.getString("Reviews"));
                                        schoolReviewsListItems.setname(obj.getString("Name"));
                                        schoolReviewsListItems.setuserId(obj.getString("Id"));

                                        ItemsArrayGetRatings.add(schoolReviewsListItems);
                                        adapterGetRatings.notifyDataSetChanged();
                                    }
                                    adapterGetRatings.notifyDataSetChanged();
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

}
