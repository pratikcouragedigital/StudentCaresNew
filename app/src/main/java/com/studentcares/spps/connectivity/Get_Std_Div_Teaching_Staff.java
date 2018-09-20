package com.studentcares.spps.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.Assign_Std_Div_Teaching_Staff;
import com.studentcares.spps.Home_Menu;
import com.studentcares.spps.R;
import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.sqlLite.DataBaseHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Get_Std_Div_Teaching_Staff {

    private static Context context;
    private static Std_Div_Filter_Adapter SpinnerAdapter;
    private static List<String> allStd_NameList = new ArrayList<String>();
    private static List<String> allStd_IdList = new ArrayList<String>();


    private static List<String> allDiv_NameList = new ArrayList<String>();
    private static List<String> allDiv_IdList = new ArrayList<String>();

    private static ProgressDialog progressDialogBox;
    private static ProgressDialog progressDialogBoxD;

    String userSelectedStandard;
    String userSelectedDivision;
    String schoolId;
    String ResponseResultForStandard;
    String ResponseResultForDivision;
    String methodName= "";


    private static String standard;
    private static String division;
    private static String staffId;
    private static ProgressDialog assignStandardDivisionProgressDialog;
    private static String webMethName;
    private static String ResponseResult;
    private static SessionManager sessionManager;
    private static DataBaseHelper mydb;



    public Get_Std_Div_Teaching_Staff(Assign_Std_Div_Teaching_Staff _assign_stdDivTeachingStaff) {
        context = _assign_stdDivTeachingStaff;
    }

    public void FetchAllstandard(List<String> standardNameList, List<String> standardIdList, String idOfSchool, Std_Div_Filter_Adapter spinnerAdapter, ProgressDialog standardDialogBox) {
        allStd_NameList = standardNameList;
        allStd_IdList = standardIdList;
        schoolId = idOfSchool;
        SpinnerAdapter = spinnerAdapter;
        progressDialogBox = standardDialogBox;
        methodName = "Get_All_Standard";

        webMethName = "Get_All_Standard";
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
                            if (res.equals("Not found Standard..!!")) {

                            }
                            else {
                                try {
                                    allStd_IdList.add("0");
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        try {
                                            JSONObject obj = jArr.getJSONObject(count);
                                            allStd_IdList.add(obj.getString("StandardId"));
                                            allStd_NameList.add(obj.getString("StandardName"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

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

    public void FetchAllDivision(List<String> nameList, List<String> idList, String idOfSchool, Std_Div_Filter_Adapter spinnerAdapter, String standardId, ProgressDialog divisionDialogBox) {
        allDiv_NameList = nameList;
        allDiv_IdList = idList;
        schoolId = idOfSchool;
        userSelectedStandard = standardId;
        SpinnerAdapter = spinnerAdapter;
        progressDialogBoxD = divisionDialogBox;
        methodName = "Get_All_Division_StandardWise";

        webMethName = "Get_All_Division_StandardWise";
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
                        progressDialogBoxD.dismiss();
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Not found Division..!!")) {

                            }
                            else {
                                try {
                                    allDiv_IdList.add("0");
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        try {
                                            JSONObject obj = jArr.getJSONObject(count);
                                            allDiv_IdList.add(obj.getString("divisionId"));
                                            allDiv_NameList.add(obj.getString("divisionName"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

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
                        progressDialogBoxD.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void AssignStandardDivision(String standardId, String divisionId, String idOfStaff, String idOfSchool, ProgressDialog assignStandardDivisionDialogBox) {
        standard = standardId;
        division = divisionId;
        staffId = idOfStaff;
        schoolId = idOfSchool;
        assignStandardDivisionProgressDialog = assignStandardDivisionDialogBox;

        webMethName = "Assign_Standard_Division_Teaching_Staff";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("standard_Id", standard);
            jsonObject.put("division_Id", division);
            jsonObject.put("staff_Id", staffId);
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
                        assignStandardDivisionProgressDialog.dismiss();
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Standard and Division Added")) {

                                sessionManager = new SessionManager(context);
                                sessionManager.AssignStdDiv_IsTeaching(standard,division);
                                mydb = new DataBaseHelper(context);
                                try {
                                    mydb.updateStd_div_isTeaching(standard,division,staffId);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(context, Home_Menu.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);

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
                        assignStandardDivisionProgressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
