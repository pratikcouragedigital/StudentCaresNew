package com.studentcares.spps.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.Notice_Add;
import com.studentcares.spps.Notice_Add_Teacher;
import com.studentcares.spps.Notice_List;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Notice_Items;
import com.studentcares.spps.sqlLite.DataBaseHelper;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Notice_Request_Data {

    private static Context context;
    private static String ResponseResult;
    private static String webMethName;
    private static String schoolId;
    private static String userType;
    private static String userId;
    private static ProgressDialog progressDialogBox;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTask;
    private static List<Notice_Items> ItemsArrayForAsyncTask;
    private static DataBaseHelper mydb;

    private static String title;
    private static String noticeDescription;
    private static String noticeDate;
    private static String image;
    private static String noticeFor;
    private static String standardId,divisionId;
    private static String noticeForGroup;
    private static ProgressDialog progressDialog;

    public Notice_Request_Data(Notice_List _noticeList) {
        context = _noticeList;
    }

    public Notice_Request_Data(Notice_Add notice_add) {
        context = notice_add;
    }

    public Notice_Request_Data(Notice_Add_Teacher notice_add_teacher) {
        context = notice_add_teacher;
    }


    public void showNotice(List<Notice_Items> noticeListItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfSchool,String typeOfUser,String idOfUser,ProgressDialog progressDialog) {

        progressDialogBox = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = noticeListItems;
        schoolId = idOfSchool;
        userType = typeOfUser;
        userId = idOfUser;

        webMethName = "Notice_List";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("User_Id", userId);
            jsonObject.put("User_Type", userType);
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
                            ItemsArrayForAsyncTask.clear();
                            adapterForAsyncTask.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if (res.equals("Not found Notice..!!")) {
                                Toast.makeText(context, "Notice List Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        Notice_Items noticeItems = new Notice_Items();
                                        noticeItems.setListId(obj.getString("NoticeDetailsId"));
                                        noticeItems.settitle(obj.getString("Title"));
                                        noticeItems.setdescription(obj.getString("Description"));
                                        noticeItems.setaddedDate(obj.getString("Added_Date"));
                                        noticeItems.setaddedByName(obj.getString("Name"));
                                        noticeItems.setStandardId(obj.getString("StandardId"));
                                        noticeItems.setDivisionId(obj.getString("DivisionId"));
                                        noticeItems.setNoticeGroup(obj.getString("NoticeGroup"));
                                        noticeItems.setNoticeDetailsFor(obj.getString("NoticeDetailsFor"));

                                        if (!obj.getString("Image").isEmpty() && obj.getString("Image") != null) {
                                            noticeItems.setFirstImagePath(obj.getString("Image"));
                                        }
                                        mydb = new DataBaseHelper(context);
                                        mydb.insertNoticeDetails((obj.getString("NoticeDetailsId")),(obj.getString("Title")), (obj.getString("Description")), (obj.getString("Added_Date")), (obj.getString("Name")), (obj.getString("Image")));

                                        ItemsArrayForAsyncTask.add(noticeItems);
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

    public void AddSchoolNotice(String idOfUser, String idOfSchool, String heading, String description, String selectedDateForSubmission, String firstImagePath, String forNotice, String noticeForStudentGroup,String std,String div, ProgressDialog progressdialog) {
        userId = idOfUser;
        schoolId = idOfSchool;
        title = heading;
        noticeDescription = description;
        noticeDate = selectedDateForSubmission;
        image = firstImagePath;
        noticeFor = forNotice;
        standardId = std;
        divisionId = div;
        noticeForGroup = noticeForStudentGroup;
        progressDialog = progressdialog;

        webMethName = "Notice_Add";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("User_Id", userId);
            jsonObject.put("Added_Date", noticeDate);
            jsonObject.put("Title", title);
            jsonObject.put("Description", noticeDescription);
            jsonObject.put("Image", image);
            jsonObject.put("NoticeFor", noticeFor);
            jsonObject.put("StandardId", standardId);
            jsonObject.put("DivisionId", divisionId);
            jsonObject.put("NoticeGroupId", noticeForGroup);
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
                            if (res.equals("Successfully Notice Added.")) {

                                Intent gotoHomeList = new Intent(context, Notice_List.class);
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
}
