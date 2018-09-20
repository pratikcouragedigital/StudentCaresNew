package com.studentcares.spps.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;

import com.studentcares.spps.Fees_Tab;
import com.studentcares.spps.model.Fees_Items;
import com.studentcares.spps.sqlLite.DataBaseHelper;
import com.studentcares.spps.webservice_common.T_Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Fees_Get_Details_Teacher {
    
    private static Context context;
    private static String ResponseResult_NotPaidFees;
    private static String ResponseResult_BalanceFees;
    private static String webMethName;
    private static String standard;
    private static String division;
    private static String schoolId;
    private static String feetype;
    private static ProgressDialog progressDialogBox;
    private static ProgressDialog progressDialogBoxBalanced;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView.Adapter adapterForAsyncTaskBalanced;
    private static RecyclerView recyclerViewForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTaskBalanced;
    private static List<Fees_Items> ItemsArrayForAsyncTask;
    private static List<Fees_Items> ItemsArrayForAsyncTaskBalanced;
    private static DataBaseHelper mydb;

    private static String mobileNo_Sms;
    private static String msgBody;
    private static String ids_Notification;
    private static String ResponseResult;
    private static String userId;
    private static String type;

    public Fees_Get_Details_Teacher(Context activity) {
        context = activity;
    }

    public Fees_Get_Details_Teacher(FragmentActivity activity) {
        context = activity;
    }

    public void SendSmsFromServer(String idOfSchool, String mobileNo, String id_For_notifications, String msg, String idOfUser,String msgtype,ProgressDialog smsProgressDialog) {

        schoolId = idOfSchool;
        mobileNo_Sms = mobileNo;
        ids_Notification = id_For_notifications;
        msgBody = msg;
        userId = idOfUser;
        type = msgtype;
        progressDialogBox = smsProgressDialog;

        AsyncCallWS task = new AsyncCallWS();
        task.execute();
    }

    public static class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            webMethName = "T_Fees_Send_MSG";
            ResponseResult = T_Webservice.Fees_Send_MSG(schoolId,mobileNo_Sms,ids_Notification,msgBody,userId,type,webMethName);
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            progressDialogBox.dismiss();
            super.onPostExecute(res);
//            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
//            builder.setTitle("Result");
//            builder.setMessage(ResponseResult);
//            android.app.AlertDialog alert1 = builder.create();
//            alert1.show();
            if (ResponseResult.equals("Msg Successfully Sent.")) {
                Intent first = new Intent(context, Fees_Tab.class);
                context.startActivity(first);

            }
        }
    }

    public void Balance_FeesDetails(List<Fees_Items> fees_items, RecyclerView recyclerView, RecyclerView.Adapter adapter, String idOfSchool, String std, String div, ProgressDialog progressDialog) {
        progressDialogBoxBalanced = progressDialog;
        adapterForAsyncTaskBalanced = adapter;
        recyclerViewForAsyncTaskBalanced = recyclerView;
        ItemsArrayForAsyncTaskBalanced = fees_items;
        schoolId = idOfSchool;
        standard = std;
        division = div;

        AsyncCallWS_BalanceFees task = new AsyncCallWS_BalanceFees();
        task.execute();
    }

    public static class AsyncCallWS_BalanceFees extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            webMethName = "Fees_Tab_Teacher_Balance";
            ResponseResult_BalanceFees = T_Webservice.T_Fees_Balance_StudentList(schoolId, standard, division, webMethName);
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            progressDialogBoxBalanced.dismiss();
            if (ResponseResult_BalanceFees.equals("No Network Found")) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Result");
//                builder.setMessage("There is Some Network Issues Please Try Again Later.");
//                AlertDialog alert1 = builder.create();
//                alert1.show();
            } else if (ResponseResult_BalanceFees.equals("Details Not Found.")) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Result");
//                builder.setMessage("Balance fees student details not available.");
//                AlertDialog alert1 = builder.create();
//                alert1.show();
            } else {
                try {
                    ItemsArrayForAsyncTaskBalanced.clear();
                    JSONObject jsonObj = new JSONObject(ResponseResult_BalanceFees);
                    JSONArray jsonArray = jsonObj.getJSONArray("BalancedFeeData");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Fees_Items items = new Fees_Items();
                            items.setstudentName(obj.getString("Student_Name"));
                            items.setstudentId(obj.getString("Student_Id"));
                            items.setstandard_Id(obj.getString("Standard_Id"));
                            items.setstandard_Name(obj.getString("Standard_Name"));
                            items.setdivision_Id(obj.getString("Division_Id"));
                            items.setdivision_Name(obj.getString("Division_Name"));
                            items.setbalanceFess(obj.getString("Balance"));
                            items.setMobileNo(obj.getString("Mobile_No"));
                            items.setFirstImagePath(obj.getString("Student_Image"));

                            ItemsArrayForAsyncTaskBalanced.add(items);
                            adapterForAsyncTaskBalanced.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void NotPaid_FeesDetails(List<Fees_Items> listItems, RecyclerView recyclerView, RecyclerView.Adapter adapter, String idOfSchool, String std, String div, ProgressDialog progressDialog) {
        progressDialogBox = progressDialog;
        adapterForAsyncTask = adapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = listItems;
        schoolId = idOfSchool;
        standard = std;
        division = div;

        AsyncCallWS_NotPaidFees task = new AsyncCallWS_NotPaidFees();
        task.execute();
    }

    public static class AsyncCallWS_NotPaidFees extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            webMethName = "Fees_Tab_Teacher_NotPaid";
            ResponseResult_NotPaidFees = T_Webservice.T_Fees_NotPaid_StudentList(schoolId, standard, division, webMethName);
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            progressDialogBox.dismiss();
            if (ResponseResult_NotPaidFees.equals("No Network Found")) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Result");
//                builder.setMessage("There is Some Network Issues Please Try Again Later.");
//                AlertDialog alert1 = builder.create();
//                alert1.show();
            } else if (ResponseResult_NotPaidFees.equals("Paid Fees Details Not Found.")) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Result");
//                builder.setMessage("Fees details Not Available.");
//                AlertDialog alert1 = builder.create();
//                alert1.show();
            } else {
                try {
                    ItemsArrayForAsyncTask.clear();
                    JSONObject jsonObj = new JSONObject(ResponseResult_NotPaidFees);
                    JSONArray jsonArray = jsonObj.getJSONArray("NotPaidFeeData");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Fees_Items items = new Fees_Items();
                            items.setstudentName(obj.getString("Student_Name"));
                            items.setstudentId(obj.getString("Student_Id"));
                            items.setstandard_Id(obj.getString("Standard_Id"));
                            items.setstandard_Name(obj.getString("Standard_Name"));
                            items.setdivision_Id(obj.getString("Division_Id"));
                            items.setdivision_Name(obj.getString("Division_Name"));
                            items.setMobileNo(obj.getString("Mobile_No"));
                            items.setFirstImagePath(obj.getString("Student_Image"));
                            items.settotalFees(obj.getString("Total"));
                            items.setmonths(obj.getString("Months"));

                            ItemsArrayForAsyncTask.add(items);
                            adapterForAsyncTask.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


}
