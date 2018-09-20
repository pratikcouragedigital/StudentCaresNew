package com.studentcares.spps.connectivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;

import com.studentcares.spps.model.Fees_Items;
import com.studentcares.spps.model.Fees_Items;
import com.studentcares.spps.model.Fees_Items;
import com.studentcares.spps.model.Fees_Items;
import com.studentcares.spps.sqlLite.DataBaseHelper;
import com.studentcares.spps.webservice_common.P_Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Fees_Get_Details_Parents {

    private static Context context;
    private static String webMethName;
    private static String userId;
    private static String schoolId;

    private static int receiptNo;
    private static String feetype;
    private static int cost = 0;
    private static int totalAmount=0;
    private static DataBaseHelper mydb;

    private static ProgressDialog pdbUnpaidMonthlyFees,PDBUnpaidMonth,PDBOneTimeFees,PDBFeesPaid,PDBSubFeesPaid;
    private static String ResponseResultUnpaidMonthlyFees,ResponseResultUnpaidMonth,ResponseResultOneTimeFees,ResponseResultFeesPaid,ResponseResultSubFeesPaid;
    private static RecyclerView.Adapter adapterUnpaidMonthlyFees,adapterUnpaidMonth,adapterOneTimeFees,adapterFeesPaid,adapterSubFeesPaid;
    private static RecyclerView recyclerViewUnpaidMonthlyFees,recyclerViewUnpaidMonth,recyclerViewOneTimeFees,recyclerViewFeesPaid,recyclerViewSubFeesPaid;
    private static List<Fees_Items> ItemsArrayOneTimeFees;
    private static List<Fees_Items> ItemsArrayFeesPaid;
    private static List<Fees_Items> ItemsArraySubFeesPaid;
    private static List<Fees_Items> ItemsArrayUnpaidMonth;
    private static List<Fees_Items> ItemsArrayUnpaidMonthlyFees;


    public Fees_Get_Details_Parents(FragmentActivity activity) {
        context = activity;
    }

    public void ShowOneTimeFeesUnpaidDetails(List<Fees_Items> listItems, RecyclerView recyclerView, RecyclerView.Adapter adapter, String idOfSchool, String idOfUser, ProgressDialog progressDialog) {
        PDBOneTimeFees = progressDialog;
        adapterOneTimeFees = adapter;
        recyclerViewOneTimeFees = recyclerView;
        ItemsArrayOneTimeFees = listItems;
        schoolId = idOfSchool;
        userId = idOfUser;

        AsyncCallWSOneTimeFees task = new AsyncCallWSOneTimeFees();
        task.execute();
    }

    public static class AsyncCallWSOneTimeFees extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            webMethName = "P_Fees_pending_OneTime";
            ResponseResultOneTimeFees = P_Webservice.showOnrTimeFeesDetails(schoolId, userId, webMethName);
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            PDBOneTimeFees.dismiss();
            if (ResponseResultOneTimeFees.equals("No Network Found")) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Result");
//                builder.setMessage("There is Some Network Issues Please Try Again Later.");
//                AlertDialog alert1 = builder.create();
//                alert1.show();
            } else if (ResponseResultOneTimeFees.equals("One Time Fees Not Available For this Standard.") || ResponseResultOneTimeFees.equals("Details Not Found.")) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Result");
//                builder.setMessage("Fees details Not Available.");
//                AlertDialog alert1 = builder.create();
//                alert1.show();
            }else if (ResponseResultOneTimeFees.equals("Paid One time Fees")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Result");
                builder.setMessage("You have Paid OneTime Fees.");
                AlertDialog alert1 = builder.create();
                alert1.show();
            } else {
                try {
                    ItemsArrayOneTimeFees.clear();
                    JSONArray jsonArray = new JSONArray(ResponseResultOneTimeFees);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Fees_Items items = new Fees_Items();
                            items.setFeeTypeID(obj.getString("FeeTypeID"));
                            items.setfeeTypeCost(obj.getString("FeeTypeCost"));
                            items.setfeeTypeName(obj.getString("FeeTypeName"));
                            ItemsArrayOneTimeFees.add(items);
                            adapterOneTimeFees.notifyDataSetChanged();
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

    public void ShowFeesPaidDetails(List<Fees_Items> listItems, RecyclerView recyclerView, RecyclerView.Adapter adapter, String idOfSchool, String idOfUser, ProgressDialog progressDialog) {
        PDBFeesPaid = progressDialog;
        adapterFeesPaid = adapter;
        recyclerViewFeesPaid = recyclerView;
        ItemsArrayFeesPaid = listItems;
        schoolId = idOfSchool;
        userId = idOfUser;

        AsyncCallWSFeesPaid task = new AsyncCallWSFeesPaid();
        task.execute();
    }

    public static class AsyncCallWSFeesPaid extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            webMethName = "P_Fees_List_Paid";
            ResponseResultFeesPaid = P_Webservice.showPaidFeesDetails(schoolId, userId, webMethName);
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            PDBFeesPaid.dismiss();
            if (ResponseResultFeesPaid.equals("No Network Found")) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Result");
//                builder.setMessage("There is Some Network Issues Please Try Again Later.");
//                AlertDialog alert1 = builder.create();
//                alert1.show();
            } else if (ResponseResultFeesPaid.equals("Paid Fees Details Not Found.")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Result");
                builder.setMessage("Fees details Not Available.");
                AlertDialog alert1 = builder.create();
                alert1.show();
            } else {
                try {
                    ItemsArrayFeesPaid.clear();
                    JSONArray jsonArray = new JSONArray(ResponseResultFeesPaid);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Fees_Items items = new Fees_Items();
                            items.setreceiptNo(obj.getString("ReceiptNo"));
                            items.setmonth(obj.getString("Month"));
                            items.setpaidAmount(obj.getString("PaidAmount"));
                            items.setpenalty(obj.getString("Penalty"));
                            items.setbalance(obj.getString("Balance"));
                            items.setprevBalance(obj.getString("PrevBalance"));
                            items.setgrandTotal(obj.getString("GrandTotal"));
                            items.setpaymentMode(obj.getString("PaymentMode"));
                            items.setchequeNo(obj.getString("ChequeNo"));
                            items.setchequeDate(obj.getString("ChequeDate"));
                            items.setchequeBankName(obj.getString("ChequeBankName"));
                            items.setchequeBranchName(obj.getString("ChequeBranchName"));
                            items.setcreatedBy(obj.getString("CreatedBy"));
                            items.setcreatedDate(obj.getString("CreatedDate"));
                            items.setfeetype(obj.getString("Feetype"));

                            ItemsArrayFeesPaid.add(items);
                            adapterFeesPaid.notifyDataSetChanged();
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

    public void ShowSubFeesPaidDetails(List<Fees_Items> listItems, RecyclerView recyclerView, RecyclerView.Adapter adapter, String idOfSchool, String idOfUser, String noOfReceipt, String typeOfFee, ProgressDialog progressDialog) {
        PDBSubFeesPaid = progressDialog;
        adapterSubFeesPaid = adapter;
        recyclerViewSubFeesPaid = recyclerView;
        ItemsArraySubFeesPaid = listItems;
        schoolId = idOfSchool;
        userId = idOfUser;
        receiptNo = Integer.parseInt(noOfReceipt);
        feetype = typeOfFee;

        AsyncCallWSSubFeesPaid task = new AsyncCallWSSubFeesPaid();
        task.execute();
    }

    public static class AsyncCallWSSubFeesPaid extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            webMethName = "Fees_Tab_Parents_Paid_Details";
            ResponseResultSubFeesPaid = P_Webservice.showPaidSubFeesDetails(schoolId, userId,receiptNo,feetype, webMethName);
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            PDBSubFeesPaid.dismiss();
            if (ResponseResultSubFeesPaid.equals("No Network Found")) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Result");
//                builder.setMessage("There is Some Network Issues Please Try Again Later.");
//                AlertDialog alert1 = builder.create();
//                alert1.show();
            } else if (ResponseResultSubFeesPaid.equals("Details Not Found.")) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Result");
//                builder.setMessage("Details Not Found.");
//                AlertDialog alert1 = builder.create();
//                alert1.show();
            } else {
                try {
                    ItemsArraySubFeesPaid.clear();
                    JSONArray jsonArray = new JSONArray(ResponseResultSubFeesPaid);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Fees_Items items = new Fees_Items();
                            items.setsrNo(obj.getString("SrNo"));
                            items.setreceiptNo(obj.getString("ReceiptNo"));
                            items.setfeeTypeCost(obj.getString("FeeTypeCost"));
                            items.setfeeTypeName(obj.getString("FeeTypeName"));

                            cost = Integer.parseInt(obj.getString("FeeTypeCost"));
                            totalAmount = totalAmount + cost;
                            ItemsArraySubFeesPaid.add(items);

                            if(jsonArray.length() - 1 == i) {
                                String amount = String.valueOf(totalAmount);
                                items = new Fees_Items();
                                items.setfeeTypeName("Total Amount");
                                items.setfeeTypeCost(amount);
                                ItemsArraySubFeesPaid.add(items);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapterSubFeesPaid.notifyDataSetChanged();

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                //  p_Paid_SubFee_TotalAmt.settotalamount(totalAmount);
            }
        }
    }

    public void ShowUnpaidMonthList(List<Fees_Items> listItems, RecyclerView recyclerView, RecyclerView.Adapter adapter, String idOfSchool, String idOfUser, ProgressDialog progressDialog) {
        PDBUnpaidMonth = progressDialog;
        adapterUnpaidMonth = adapter;
        recyclerViewUnpaidMonth = recyclerView;
        ItemsArrayUnpaidMonth = listItems;
        schoolId = idOfSchool;
        userId = idOfUser;

        AsyncCallWSUnPaidMoth task = new AsyncCallWSUnPaidMoth();
        task.execute();
    }

    public static class AsyncCallWSUnPaidMoth extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            webMethName = "P_Fees_Unpaid_Month_List";
            ResponseResultUnpaidMonth = P_Webservice.ShowUnpaidFeesMonthList(schoolId, userId, webMethName);
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            PDBUnpaidMonth.dismiss();
            if (ResponseResultUnpaidMonth.equals("No Network Found")) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Result");
//                builder.setMessage("There is Some Network Issues Please Try Again Later.");
//                AlertDialog alert1 = builder.create();
//                alert1.show();
            }else if (ResponseResultUnpaidMonth.equals("You Paid All Fees")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Result");
                builder.setMessage("You have Paid Monthly Fees.");
                AlertDialog alert1 = builder.create();
                alert1.show();
            } else {
                try {
                    ItemsArrayUnpaidMonth.clear();
                    JSONArray jsonArray = new JSONArray(ResponseResultUnpaidMonth);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Fees_Items items = new Fees_Items();
                            items.setMonthName(obj.getString("Value")); // month name
                            ItemsArrayUnpaidMonth.add(items);
                            adapterUnpaidMonth.notifyDataSetChanged();
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

    public void ShowUnpaidMonthlyFeesDetails(List<Fees_Items> listItems, RecyclerView recyclerView, RecyclerView.Adapter adapter, String idOfSchool, String idOfUser, ProgressDialog progressDialog) {
        pdbUnpaidMonthlyFees = progressDialog;
        adapterUnpaidMonthlyFees = adapter;
        recyclerViewUnpaidMonthlyFees = recyclerView;
        ItemsArrayUnpaidMonthlyFees = listItems;
        schoolId = idOfSchool;
        userId = idOfUser;

        AsyncCallWSUnpaidMonthlyFees task = new AsyncCallWSUnpaidMonthlyFees();
        task.execute();
    }

    public static class AsyncCallWSUnpaidMonthlyFees extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            webMethName = "P_Fees_MonthLy_Details";
            ResponseResultUnpaidMonthlyFees = P_Webservice.showUnpaidMothlyFeeDetail(schoolId, userId, webMethName);
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            pdbUnpaidMonthlyFees.dismiss();
            if (ResponseResultUnpaidMonthlyFees.equals("No Network Found")) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Result");
//                builder.setMessage("There is Some Network Issues Please Try Again Later.");
//                AlertDialog alert1 = builder.create();
//                alert1.show();
            } else if (ResponseResultUnpaidMonthlyFees.equals("Month Wise Fees Not Decides.")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Result");
                builder.setMessage("Fees details Not Available.");
                AlertDialog alert1 = builder.create();
                alert1.show();
            } else {
                try {

                    ItemsArrayUnpaidMonthlyFees.clear();
                    JSONArray jsonArray = new JSONArray(ResponseResultUnpaidMonthlyFees);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Fees_Items items = new Fees_Items();
                            items.setFeeTypeID(obj.getString("FeeTypeID"));
                            items.setfeeTypeCost(obj.getString("FeeTypeCost"));
                            items.setfeeTypeName(obj.getString("FeeTypeName"));
                            ItemsArrayUnpaidMonthlyFees.add(items);
                            adapterUnpaidMonthlyFees.notifyDataSetChanged();
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
