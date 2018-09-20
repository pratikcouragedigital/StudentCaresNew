package com.studentcares.spps.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.Homework_List_Parents;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Attendance_List_Items;
import com.studentcares.spps.model.Homework_Items;
import com.studentcares.spps.sqlLite.DataBaseHelper;
import com.studentcares.spps.webservice_common.P_Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Homework_List_Show_Parents {

    private static Context context;
    private static String ResponseResult;
    private static String webMethName ;
    private static String counter ;
    private static String userId ;
    private static String schoolId ;
    private static int currentPage ;
    private static String homeworkSubjectId ;
    private static String homeworkCreatedDate;
    private static String std;
    private static String div;
    private static ProgressDialog progressDialogBox;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTask;
    private static List<Homework_Items> ItemsArrayForAsyncTask;
    private static DataBaseHelper mydb;

    public Homework_List_Show_Parents(Homework_List_Parents _homework_listP) {
        context = _homework_listP;
    }

    public void showHomeworkForParents(List<Homework_Items> homeWorkItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfUser, String idOfSchool, String selectedSubjectId, String selectedDate, String count, int current_Page,String standardName,String divisionName, ProgressDialog progressDialog) {
        progressDialogBox = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = homeWorkItems;

        counter = count;
        userId = idOfUser;
        schoolId = idOfSchool;
        currentPage = current_Page;
        homeworkSubjectId = selectedSubjectId;
        homeworkCreatedDate = selectedDate;
        std = standardName;
        div = divisionName;

        JSONObject jsonObject = new JSONObject();
        if(counter == "1"){
            webMethName ="Homework_Student_Default_List";
            try {
                jsonObject.put("School_id", schoolId);
                jsonObject.put("student_id", userId);
                jsonObject.put("Standard_Id", std);
                jsonObject.put("Division_Id", div);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(counter == "2"){
            webMethName ="Homework_Student_Filterwise_List";
            try {
                jsonObject.put("School_id", schoolId);
                jsonObject.put("student_id", userId);
                jsonObject.put("Standard_Id", std);
                jsonObject.put("Division_Id", div);
                jsonObject.put("Subject_Id", homeworkSubjectId);
                jsonObject.put("submission_Date", homeworkCreatedDate);
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
                            if(res.equals("Invalid List")){
                                Toast.makeText(context, "Homework  Not Available", Toast.LENGTH_SHORT).show();
                            }else{
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        Homework_Items homeWorkItems = new Homework_Items();
                                        homeWorkItems.setListId(obj.getString("ListId"));
                                        homeWorkItems.setsubjectName(obj.getString("subject_Name"));
                                        homeWorkItems.setteacherName(obj.getString("teacher_Name"));
                                        homeWorkItems.setstandard(obj.getString("standard"));
                                        homeWorkItems.setdivision(obj.getString("division"));
                                        homeWorkItems.setsubmissionDate(obj.getString("submission_Date"));
                                        homeWorkItems.setaddedDate(obj.getString("addedDate"));
                                        homeWorkItems.sethomework(obj.getString("homework"));
                                        homeWorkItems.sethomeworkTitle(obj.getString("homeworkTitle"));
                                        if (!obj.getString("homeworkImage").isEmpty() && obj.getString("homeworkImage") != null) {
                                            homeWorkItems.setFirstImagePath(obj.getString("homeworkImage"));
                                        }
                                        mydb = new DataBaseHelper(context);
                                        mydb.insertHomeworkDetails (userId,(obj.getString("ListId")),(obj.getString("standard")),(obj.getString("division")),(obj.getString("subject_Name")), (obj.getString("teacher_Name")),(obj.getString("submission_Date")), (obj.getString("addedDate")),(obj.getString("homework")),(obj.getString("homeworkTitle")),(obj.getString("homeworkImage")));

                                        ItemsArrayForAsyncTask.add(homeWorkItems);
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


