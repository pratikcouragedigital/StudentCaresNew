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
import com.studentcares.spps.Leave_Apply;
import com.studentcares.spps.Leave_List_Own;
import com.studentcares.spps.Leave_To_Approve_List;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Leave_Items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Leave_req {

    private static Context context;
    private static String ResponseResultHolidayList;
    private static String webMethName;
    private static String holidayFor;
    private static String schoolId;
    private static ProgressDialog PDBHolidayList;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTask;
    private static List<Leave_Items> ItemsArrayForAsyncTask;

    private static String ResponseResult;
    private static String userId, staffId, leaveListId, approveOrDisApprove;
    private static String leaveReason;
    private static String userType, leaveListFor;
    private static String fromDate;
    private static String toDate;
    private static ProgressDialog progressDialog;

    public Leave_req(Leave_Apply leave_apply) {
        context = leave_apply;
    }

    public Leave_req(Leave_To_Approve_List leave_ToApprove_list) {
        context = leave_ToApprove_list;
    }

    public Leave_req(Context approveLeave) {
        context = approveLeave;
    }

    public void ApplyForLeave(String idOfUser, String idOfSchool, String typeOfUser, String reason, String leaveFromDate, String leaveToDate, ProgressDialog pDialog) {

        userId = idOfUser;
        schoolId = idOfSchool;
        userType = typeOfUser;
        leaveReason = reason;
        fromDate = leaveFromDate;
        toDate = leaveToDate;
        progressDialog = pDialog;

        webMethName = "Leave_Apply";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("User_Id", userId);
            jsonObject.put("Leave_Reason", leaveReason);
            jsonObject.put("From_Date", fromDate);
            jsonObject.put("To_Date", toDate);
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
                            if (res.equals("Leave Successfully Apply")) {

                                Intent gotoLeave = new Intent(context, Leave_List_Own.class);
                                gotoLeave.putExtra("leaveListFor", leaveListFor);
                                gotoLeave.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoLeave);

                            } else {
                                Toast.makeText(context, " " + res, Toast.LENGTH_SHORT).show();
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



    public void LeaveToApproveList(List<Leave_Items> holidaysItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfSchool, String idOfUser, ProgressDialog progressDialog) {
        schoolId = idOfSchool;
        userId = idOfUser;
        PDBHolidayList = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = holidaysItems;

        JSONObject jsonObject = new JSONObject();
        webMethName = "Leave_To_Approve_List";
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
                            PDBHolidayList.dismiss();
                            ItemsArrayForAsyncTask.clear();
                            adapterForAsyncTask.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if (res.equals("Leave List Not Found")) {
                                Toast.makeText(context, "No one yet applied for leave", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        Leave_Items leaveItems = new Leave_Items();
                                        leaveItems.setleaveReason(obj.getString("leaveReason"));
                                        leaveItems.setdepartment(obj.getString("department"));
                                        leaveItems.setstaffId(obj.getString("staffId"));
                                        leaveItems.setstaffName(obj.getString("staffName"));
                                        leaveItems.setfromDate(obj.getString("From_Date"));
                                        leaveItems.settoDate(obj.getString("To_Date"));
                                        leaveItems.setFirstImagePath(obj.getString("image"));
                                        leaveItems.setleaveListId(obj.getString("leaveListId"));
                                        leaveItems.setapproveBy(obj.getString("approveBy"));
                                        leaveItems.setapprovedOrNot(obj.getBoolean("approvedOrNot"));
                                        ItemsArrayForAsyncTask.add(leaveItems);
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
                        PDBHolidayList.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void OwnLeaveList(List<Leave_Items> holidaysItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String idOfSchool, String idOfUser, ProgressDialog progressDialog) {
        schoolId = idOfSchool;
        userId = idOfUser;
        PDBHolidayList = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = holidaysItems;

        JSONObject jsonObject = new JSONObject();

        webMethName = "Leave_List_Own";
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("userId", userId);
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
                            PDBHolidayList.dismiss();
                            ItemsArrayForAsyncTask.clear();
                            adapterForAsyncTask.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if (res.equals("Leave List Not Found")) {
                                Toast.makeText(context, "No leave yet applied", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        Leave_Items leaveItems = new Leave_Items();
                                        leaveItems.setleaveReason(obj.getString("leaveReason"));
                                        leaveItems.setdepartment(obj.getString("department"));
                                        leaveItems.setstaffId(obj.getString("staffId"));
                                        leaveItems.setstaffName(obj.getString("staffName"));
                                        leaveItems.setfromDate(obj.getString("From_Date"));
                                        leaveItems.settoDate(obj.getString("To_Date"));
                                        leaveItems.setFirstImagePath(obj.getString("image"));
                                        leaveItems.setleaveListId(obj.getString("leaveListId"));
                                        leaveItems.setapproveBy(obj.getString("approveBy"));
                                        leaveItems.setapprovedOrNot(obj.getBoolean("approvedOrNot"));
                                        ItemsArrayForAsyncTask.add(leaveItems);
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
                        PDBHolidayList.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }




    public void LeaveApprove(String idOfSchool, String idOfUser, String aord, String idOstaff, String idOfleaveList, ProgressDialog pd) {
        userId = idOfUser;
        schoolId = idOfSchool;
        staffId = idOstaff;
        leaveListId = idOfleaveList;
        approveOrDisApprove = aord;
        progressDialog = pd;

        webMethName = "Leave_Approve";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("User_Id", userId);
            jsonObject.put("Staff_Id", staffId);
            jsonObject.put("Is_Approve", approveOrDisApprove);
            jsonObject.put("Leave_List_Id", leaveListId);
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
                            if (res.equals("Leave Approve Successfully")) {
                                Intent gotoHomeList = new Intent(context, Leave_To_Approve_List.class);
                                gotoHomeList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoHomeList);
                            }
                            else {
                                Toast.makeText(context, " " + res, Toast.LENGTH_SHORT).show();
                                Intent gotoHomeList = new Intent(context, Leave_To_Approve_List.class);
                                gotoHomeList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoHomeList);
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
