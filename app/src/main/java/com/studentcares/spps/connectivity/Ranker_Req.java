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
import com.androidnetworking.model.Progress;
import com.studentcares.spps.Homework_List_Teacher;
import com.studentcares.spps.R;
import com.studentcares.spps.Ranker_Add;
import com.studentcares.spps.Rankers;
import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.model.Ranker_List_Items;
import com.studentcares.spps.model.Rankers_Items;
import com.studentcares.spps.model.Send_SMS_User_Items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Ranker_Req {

    private static ProgressDialog progressDialog = null;
    private static RecyclerView recylerViewForAsyncTask;
    private static RecyclerView.Adapter adapterForasynchTask;
    private static List<Rankers_Items> listItemsForAsynchTask;
    private static String schoolId, userid;
    Context context;

    public Ranker_Req(Rankers rankers) {
        context = rankers;
    }

    public Ranker_Req(Ranker_Add ranker_add) {
        context = ranker_add;
    }

    public void GetRankerList(List<Rankers_Items> listItems, RecyclerView recyclerView, RecyclerView.Adapter adapter, String schoolId, final ProgressDialog progressDialog) {
        recylerViewForAsyncTask = recyclerView;
        listItemsForAsynchTask= listItems;
        adapterForasynchTask = adapter;

        String method = "Top_Ranker_List";
        String url = context.getString(R.string.url)+method;
        JSONObject obj = new JSONObject();
        try {
            obj.put("School_id", schoolId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(url)
                .addJSONObjectBody(obj)
                .setPriority(Priority.IMMEDIATE)
                .setTag(method)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        listItemsForAsynchTask.clear();
                        adapterForasynchTask.notifyDataSetChanged();
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("No Ranker Data Found")) {
                                Toast.makeText(context, "Ranker Not Available.", Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray jsonArray = response.getJSONArray("responseDetails");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Rankers_Items items = new Rankers_Items();

                                    items.setstandard(jsonObject.getString("Standard_Name"));
                                    items.setdivision(jsonObject.getString("Division_Name"));

                                    JSONArray jArray = jsonObject.getJSONArray("Top_Rankers");
                                    int jArrSize = jArray.length();

                                    List<Ranker_List_Items> rankerList = new ArrayList<Ranker_List_Items>();

                                    if(jArrSize != 0){
                                        for(int a = 0; a < jArray.length(); a++){
                                            Ranker_List_Items rankerListItems = new Ranker_List_Items();
                                            JSONObject obj = jArray.getJSONObject(a);
                                            rankerListItems.setFirstImagePath(obj.getString("Image"));
                                            rankerListItems.setstudentName(obj.getString("Student_Name"));
                                            rankerListItems.setstudentId(obj.getString("Student_Id"));
                                            rankerListItems.setrank(obj.getString("Rank"));
                                            rankerListItems.setobtainedMarks(obj.getString("Obtain_Marks"));
                                            rankerListItems.setoutOffMarks(obj.getString("OutOf_Marks"));
                                            rankerListItems.setpercent(obj.getString("Percentage"));
                                            rankerListItems.setgrade(obj.getString("Grade"));

                                            rankerList.add(rankerListItems);
                                        }
                                        items.setRankerListItems(rankerList);
                                        listItemsForAsynchTask.add(items);
                                    }
                                    adapterForasynchTask.notifyDataSetChanged();
                                }

                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        anError.getMessage();
                        Toast.makeText(context, "Exception" + anError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void FetchStudent(final List<String> studentNameList,final List<String> studentIdList, String schoolId, final Std_Div_Filter_Adapter spinnerAdapter, String standardId, String divisionId) {

        String webMethName = "Send_SMS_Student_List";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("standardId", standardId);
            jsonObject.put("divisionId", divisionId);
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
                            studentNameList.clear();
                            studentIdList.clear();
                            spinnerAdapter.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if (res.equals("Student List Not Available.")) {
                                Toast.makeText(context, "Student List Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    studentNameList.add("Select Student");
                                    studentIdList.add("0");

                                    for (int count = 0; count < jArr.length(); count++) {

                                        JSONObject obj = jArr.getJSONObject(count);
                                        studentNameList.add(obj.getString("student_Name"));
                                        studentIdList.add(obj.getString("student_Id"));
                                        spinnerAdapter.notifyDataSetChanged();
                                    }
                                    spinnerAdapter.notifyDataSetChanged();
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

    public void AddRanker(String staffId, String schoolId, String standardId, String divisionId, String studentId, String rank, String obtMarks, String outOfMarks, String percentage, String grade, final ProgressDialog progressDialog) {

       String webMethName = "Add_Ranker";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Staff_Id", staffId);
            jsonObject.put("Standard_Id", standardId);
            jsonObject.put("Division_Id", divisionId);
            jsonObject.put("Student_Id", studentId);
            jsonObject.put("Rank", rank);
            jsonObject.put("Obtained_Marks", obtMarks);
            jsonObject.put("OutOf_Marks", outOfMarks);
            jsonObject.put("Percentage", percentage);
            jsonObject.put("Grade", grade);
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
                            progressDialog.dismiss();
                            String res = response.getString("responseDetails");
                            if (res.equals("Ranker Added Successfully.")) {
                                Toast.makeText(context,"Ranker Added Successfully.", Toast.LENGTH_SHORT).show();
                                Intent gotoHomeList = new Intent(context, Rankers.class);
                                gotoHomeList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoHomeList);
                            } else {
                                Toast.makeText(context," "+res, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
