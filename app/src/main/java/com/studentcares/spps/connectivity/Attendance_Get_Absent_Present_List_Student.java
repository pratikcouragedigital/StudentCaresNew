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
import com.studentcares.spps.R;
import com.studentcares.spps.model.Attendance_List_Items;
import com.studentcares.spps.webservice_common.T_Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Attendance_Get_Absent_Present_List_Student {

    private static Context context;
    private static String ResponseForPresent;
    private static String ResponseForAbsent;
    private static String standard;
    private static String division;
    private static String webMethName ;
    private static String userId ;
    private static String schoolId ;
    private static ProgressDialog PDBPresentList,PDBAbsentList;

    private static RecyclerView.Adapter presentAdapter,absentAdapter;
    private static RecyclerView absentRecyclerView,presentRecyclerView;
    private static List<Attendance_List_Items> absentItemsArray,presentItemsArray;



    public Attendance_Get_Absent_Present_List_Student(Context contextPresent) {
        context = contextPresent;
    }

    public void showAbsent_Student_List(List<Attendance_List_Items> absentTAttendanseItems, RecyclerView recyclerViewAbsent, RecyclerView.Adapter reviewAdapterAbsent, String standardId, String divisionId, String idOfSchool, ProgressDialog progressDialog) {
        standard = standardId;
        division = divisionId;
        schoolId = idOfSchool;
        PDBAbsentList = progressDialog;
        absentAdapter = reviewAdapterAbsent;
        absentRecyclerView = recyclerViewAbsent;
        absentItemsArray = absentTAttendanseItems;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Standard_Id", standard);
            jsonObject.put("Division_Id", division);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String method ="Attendance_Student_Absent_List";
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
                            PDBAbsentList.dismiss();
                            absentItemsArray.clear();
                            absentAdapter.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if(res.equals("Invalid List")){
                            }else{
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        Attendance_List_Items TAttendanseItems = new Attendance_List_Items();
                                        TAttendanseItems.setstudentName(obj.getString("student_Name"));
                                        TAttendanseItems.setstandard(obj.getString("standard"));
                                        TAttendanseItems.setdivision(obj.getString("division"));
                                        TAttendanseItems.setstudentId(obj.getString("student_Id"));
                                        TAttendanseItems.setFirstImagePath(obj.getString("photo"));
                                        TAttendanseItems.setstatus(obj.getString("Attr_Status"));
                                        absentItemsArray.add(TAttendanseItems);
                                    }
                                    absentAdapter.notifyDataSetChanged();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                        catch(Exception e) {
                            PDBAbsentList.dismiss();
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        PDBAbsentList.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void showPresent_Student_List(List<Attendance_List_Items> presentTAttendanseItems, RecyclerView recyclerViewPresent, RecyclerView.Adapter reviewAdapterPresent, String standardId, String divisionId, String idOfSchool, ProgressDialog progressDialog) {
        standard = standardId;
        division = divisionId;
        schoolId = idOfSchool;
        PDBPresentList = progressDialog;
        presentAdapter = reviewAdapterPresent;
        presentRecyclerView = recyclerViewPresent;
        presentItemsArray = presentTAttendanseItems;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Standard_Id", standard);
            jsonObject.put("Division_Id", division);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String method ="Attendance_Student_Present_List";
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
                            PDBPresentList.dismiss();
                            presentItemsArray.clear();
                            presentAdapter.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if(res.equals("Invalid List")){
                            }else{
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        Attendance_List_Items TAttendanseItems = new Attendance_List_Items();
                                        TAttendanseItems.setstudentName(obj.getString("student_Name"));
                                        TAttendanseItems.setstandard(obj.getString("standard"));
                                        TAttendanseItems.setdivision(obj.getString("division"));
                                        TAttendanseItems.setstudentId(obj.getString("student_Id"));
                                        TAttendanseItems.setFirstImagePath(obj.getString("photo"));
                                        TAttendanseItems.setinTime(obj.getString("InTime"));
                                        TAttendanseItems.setoutTime(obj.getString("OutTime"));
                                        presentItemsArray.add(TAttendanseItems);
                                    }
                                    presentAdapter.notifyDataSetChanged();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                        catch(Exception e) {
                            PDBPresentList.dismiss();
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        PDBPresentList.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
