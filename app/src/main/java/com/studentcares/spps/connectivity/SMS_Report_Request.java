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
import com.studentcares.spps.SMS_Report;
import com.studentcares.spps.model.SMS_Report_Items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.List;

public class SMS_Report_Request {

    private static Context context;
    private static RecyclerView.Adapter adapterForAsyncTaskAtt;
    private static RecyclerView recyclerViewForAsyncTaskAtt;
    private static List<SMS_Report_Items> ItemsArrayForAsyncTaskAtt;

    public SMS_Report_Request(SMS_Report sms_report) {

        context = sms_report;
    }

    public void GetSMSReport(List<SMS_Report_Items> listItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String schoolId, String fromDate, String toDate, String senderId, final ProgressDialog progressDialog) {
        ItemsArrayForAsyncTaskAtt = listItems;
        adapterForAsyncTaskAtt = reviewAdapter;
        recyclerViewForAsyncTaskAtt = recyclerView;

        String webMethName = "SMS_Report2";
//        String parameters = "senderid=" + senderId + "&xml=yes&dateval=" + fromDate + " - " + toDate;
//        String url = context.getString(R.string.urlSMSReport)+parameters;
        String url = context.getString(R.string.url)+webMethName;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("fromDate", fromDate);
//            jsonObject.put("toDate", toDate);
//            jsonObject.put("senderId", senderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                            ItemsArrayForAsyncTaskAtt.clear();
                            adapterForAsyncTaskAtt.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if (res.equals("Report Not Available.")) {
                                Toast.makeText(context, "SMS Report Not Available", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                int count;
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        SMS_Report_Items smsItems = new SMS_Report_Items();
                                        smsItems.setsmsStatus(obj.getString("Status"));
                                        smsItems.setsms(obj.getString("Message"));
                                        smsItems.setmobileNo(obj.getString("Mobileno"));
                                        smsItems.setmobileNo(obj.getString("User_Id"));
                                        smsItems.setDate(obj.getString("SMS_Date"));

                                        ItemsArrayForAsyncTaskAtt.add(smsItems);
                                    }
                                    Toast.makeText(context, "count: "+count, Toast.LENGTH_SHORT).show();
                                    adapterForAsyncTaskAtt.notifyDataSetChanged();
                                } catch (JSONException ex) {
                                    progressDialog.dismiss();
                                    ex.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Toast.makeText(context, "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Script Error: " + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
