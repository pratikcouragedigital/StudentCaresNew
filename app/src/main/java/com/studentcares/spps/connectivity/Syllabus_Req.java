package com.studentcares.spps.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.R;
import com.studentcares.spps.Study_Material;
import com.studentcares.spps.Syllabus;
import com.studentcares.spps.model.Syllabus_Items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Syllabus_Req {

    private static Context context;
    private static String ResponseResult;
    private static String webMethName ;

    private static String userId ;
    private static String schoolId ;

    private static String subjectId ;

    private static String standardId;

    private static ProgressDialog progressDialogBox;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTask;
    private static List<Syllabus_Items> ItemsArrayForAsyncTask;

    public Syllabus_Req(Syllabus syllabus) {
        context = syllabus;
    }


    public void ShowSyllabusPDFList(List<Syllabus_Items> pdfItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfUser, String idOfSchool, String stdId,  ProgressDialog progressDialog) {
        progressDialogBox = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = pdfItems;

        userId = idOfUser;
        schoolId = idOfSchool;
        standardId = stdId;

        JSONObject jsonObject = new JSONObject();
        webMethName ="Syllabus_PDF_List";
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Standard_Id", standardId);
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
                            if(res.equals("PDF List Not Available.")){
                                Toast.makeText(context, "PDF Not Available", Toast.LENGTH_SHORT).show();
                            }else{
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        Syllabus_Items syllabusPDFItems = new Syllabus_Items();
                                        syllabusPDFItems.setListId(obj.getString("Syllabus_Id"));
                                        syllabusPDFItems.settitle(obj.getString("Title"));
                                        if (!obj.getString("PDF_Path").isEmpty() && obj.getString("PDF_Path") != null) {
                                            syllabusPDFItems.setpdfPath(obj.getString("PDF_Path"));
                                        }
                                        ItemsArrayForAsyncTask.add(syllabusPDFItems);
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
