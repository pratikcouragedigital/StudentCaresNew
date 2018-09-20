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
import com.studentcares.spps.Attendance_Tab_Student;
import com.studentcares.spps.Homework_Add_Teacher;
import com.studentcares.spps.Homework_List_Teacher;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Homework_Items;
import com.studentcares.spps.sqlLite.DataBaseHelper;
import com.studentcares.spps.webservice_common.T_Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Homework_List_Show_Teacher {

    private static Context context;
    private static String ResponseResult;
    private static String standard;
    private static String division;
    private static String subject;
    private static String date;
    private static String counter;
    private static String webMethName;
    private static String userId;
    private static String schoolId;
    private static int currentPage;
    private static ProgressDialog progressDialogBox;


    private static String homework;
    private static String TitleOfHomework;
    private static String submissionDate;
    private static String staffId;
    private static String firstImaheOfHomework;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTask;
    private static List<Homework_Items> ItemsArrayForAsyncTask;
    private static DataBaseHelper mydb;

    public Homework_List_Show_Teacher(Homework_List_Teacher _homework_listT) {
        context = _homework_listT;
    }

    public Homework_List_Show_Teacher(Homework_Add_Teacher _homework_addT) {
        context = _homework_addT;
    }


    public void showHomeworkListForTeacher(List<Homework_Items> tHomeWorkItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String standardId, String divisionId, String subjectId, String idOfSchool, String selectedDate, String count, String staffId, int current_Page, ProgressDialog progressDialog) {
        standard = standardId;
        division = divisionId;
        counter = count;
        userId = staffId;
        schoolId = idOfSchool;
        subject = subjectId;
        date = selectedDate;
        currentPage = current_Page;
        progressDialogBox = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = tHomeWorkItems;

        JSONObject jsonObject = new JSONObject();
        if (counter == "1") {
            webMethName = "Homework_Staff_Default_List";
            try {
                jsonObject.put("School_id", schoolId);
                jsonObject.put("staff_Id", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (counter == "2") {
            webMethName = "Homework_Staff_Filterwise_List";
            try {
                jsonObject.put("School_id", schoolId);
                jsonObject.put("staff_Id", userId);
                jsonObject.put("Standard_Id", standard);
                jsonObject.put("Division_Id", division);
                jsonObject.put("Subject_Id", subject);
                jsonObject.put("submission_Date", date);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                            if (res.equals("Invalid List")) {
                                Toast.makeText(context, "Homework Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        Homework_Items THomeWorkItems = new Homework_Items();
                                        THomeWorkItems.setListId(obj.getString("ListId"));
                                        THomeWorkItems.setsubjectName(obj.getString("subject_Name"));
                                        THomeWorkItems.setstandard(obj.getString("standard"));
                                        THomeWorkItems.setdivision(obj.getString("division"));
                                        THomeWorkItems.setteacherName(obj.getString("teacher_Name"));
                                        THomeWorkItems.setsubmissionDate(obj.getString("submission_Date"));
                                        THomeWorkItems.setaddedDate(obj.getString("addedDate"));
                                        THomeWorkItems.sethomework(obj.getString("homework"));
                                        THomeWorkItems.sethomeworkTitle(obj.getString("homeworkTitle"));
                                        if (!obj.getString("homeworkImage").isEmpty() && obj.getString("homeworkImage") != null) {
                                            THomeWorkItems.setFirstImagePath(obj.getString("homeworkImage"));
                                        }
                                        mydb = new DataBaseHelper(context);
                                        mydb.insertHomeworkDetails(userId, (obj.getString("ListId")), (obj.getString("standard")), (obj.getString("division")), (obj.getString("subject_Name")), (obj.getString("teacher_Name")), (obj.getString("submission_Date")), (obj.getString("addedDate")), (obj.getString("homework")), (obj.getString("homeworkTitle")), (obj.getString("homeworkImage")));

                                        ItemsArrayForAsyncTask.add(THomeWorkItems);
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

    public void addHomework(String homeworkDescription, String selectedDateForSubmission, String IdOfStaff, String IdOfSchool, String Standard, String Division, String Subject, String homeworkTitle, String firstImage, ProgressDialog progressDialog) {
        homework = homeworkDescription;
        TitleOfHomework = homeworkTitle;
        submissionDate = selectedDateForSubmission;
        staffId = IdOfStaff;
        schoolId = IdOfSchool;
        standard = Standard;
        division = Division;
        subject = Subject;
        firstImaheOfHomework = firstImage;
        progressDialogBox = progressDialog;

        webMethName = "Homework_Add";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Staff_Id", staffId);
            jsonObject.put("Standard_Id", standard);
            jsonObject.put("Division_Id", division);
            jsonObject.put("Subject_Id", subject);
            jsonObject.put("Submission_Date", submissionDate);
            jsonObject.put("Homework_Title", TitleOfHomework);
            jsonObject.put("Homework_Description", homework);
            jsonObject.put("Image", firstImaheOfHomework);
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
                            String res = response.getString("responseDetails");
                            if (res.equals("Homework Added Successfully.")) {

                                Intent gotoHomeList = new Intent(context, Homework_List_Teacher.class);
                                gotoHomeList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoHomeList);

                            } else {
                                Toast.makeText(context," "+res, Toast.LENGTH_SHORT).show();
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
