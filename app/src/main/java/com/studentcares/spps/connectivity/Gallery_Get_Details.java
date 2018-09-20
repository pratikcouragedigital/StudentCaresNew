package com.studentcares.spps.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.Gallery_Event_Images_Add;
import com.studentcares.spps.Gallery_Event_Wise_Images;
import com.studentcares.spps.Gallery_Title_List;
import com.studentcares.spps.News_List;
import com.studentcares.spps.R;
import com.studentcares.spps.adapter.Gallery_Title_List_Adapter;
import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.model.DashBoard_Count_Items;
import com.studentcares.spps.model.Event_List_Items;
import com.studentcares.spps.model.Event_List_Items;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Gallery_Get_Details {

    private static Context context;
    private static String ResponseResult;
    private static String webMethName;
    private static String schoolId;
    private static ProgressDialog progressDialogBox;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private RecyclerView recyclerViewForAsyncTask;
    private static List<Event_List_Items> ItemsArrayForAsyncTask;

    private static Std_Div_Filter_Adapter SpinnerAdapter;
    private static List<String> allNameList = new ArrayList<String>();
    private static List<String> allIdList = new ArrayList<String>();
    private static List<String> allCountList = new ArrayList<String>();

    private static String userId;
    private static String eventId;
    private static String eventName;
    private static ProgressDialog progressDialog;
    private File storageDir;


    public Gallery_Get_Details(Gallery_Title_List eventList) {
        context = eventList;
    }

    public Gallery_Get_Details(Gallery_Event_Images_Add gallery_event_images_add) {
        context = gallery_event_images_add;
    }

    public Gallery_Get_Details(Gallery_Event_Wise_Images gallery_event_wise_images) {
        context = gallery_event_wise_images;
    }


    public void showEventImages(List<Event_List_Items> Event_List_Items, RecyclerView recyclerView, RecyclerView.Adapter albumImageAdapter, String idOfSchool, String eventId,  String standardId,  String divisionId, ProgressDialog progressDialog) {
        schoolId = idOfSchool;
        progressDialogBox = progressDialog;
        adapterForAsyncTask = albumImageAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = Event_List_Items;
        this.eventId = eventId;

        webMethName = "Gallery_Eventwise_Image_List";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("eventId", eventId);
            jsonObject.put("Standard_Id", standardId != "" ? standardId : "0");
            jsonObject.put("Division_Id", divisionId != "" ? divisionId : "0");
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
                            if (res.equals("Event Images Not Found.")) {
                                Toast.makeText(context, "Images not available.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                try {
                                    ItemsArrayForAsyncTask.clear();
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        try {
                                            JSONObject obj = jArr.getJSONObject(count);
                                            Event_List_Items eventImages_items = new Event_List_Items();
                                            eventImages_items.setimagePath(obj.getString("imagePath"));
                                            eventImages_items.setimageId(obj.getString("imageId"));
                                            eventImages_items.seteventId(obj.getString("eventId"));
                                            ItemsArrayForAsyncTask.add(eventImages_items);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
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
                        progressDialogBox.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }



    public void postEventImages(String userId, String schoolId, String eventId, String standardId, String divisionId, final List<File> imgFileList, final ProgressDialog progressDialog) {
        this.userId = userId;
        this.schoolId = schoolId;
        this.eventId = eventId;
        this.progressDialog = progressDialog;

        webMethName = "Gallery_Eventwise_ImageFile_Add";
        if(imgFileList.size() < 10) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image);
            storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM);
            File newFile = new File(storageDir + "/no_image.png");
            try {
                OutputStream os = new FileOutputStream(newFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
            }

            for(int i = imgFileList.size(); i < 10; i++) {
                imgFileList.add(newFile);
            }
        }

        String url = context.getString(R.string.url) + webMethName;
        AndroidNetworking.upload(url)
                .addMultipartFile("img10", imgFileList.get(9))
                .addMultipartFile("img9", imgFileList.get(8))
                .addMultipartFile("img8", imgFileList.get(7))
                .addMultipartFile("img7", imgFileList.get(6))
                .addMultipartFile("img6", imgFileList.get(5))
                .addMultipartFile("img5", imgFileList.get(4))
                .addMultipartFile("img4", imgFileList.get(3))
                .addMultipartFile("img3", imgFileList.get(2))
                .addMultipartFile("img2", imgFileList.get(1))
                .addMultipartFile("img1", imgFileList.get(0))
                .addMultipartParameter("School_id",schoolId)
                .addMultipartParameter("User_id",userId)
                .addMultipartParameter("eventId",eventId)
                .addMultipartParameter("Standard_Id",standardId)
                .addMultipartParameter("Division_Id",divisionId)
                .addMultipartParameter("method",webMethName)
                .setTag(webMethName)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            String res = response.getString("responseDetails");
                            if (res.equals("Images successfully added.")) {
                                for(int i = 0; i < imgFileList.size(); i++) {
                                    imgFileList.get(i).delete();
                                }
                                Toast.makeText(context, "Event Images Successfully Added.", Toast.LENGTH_LONG).show();
                                Intent gotoNews = new Intent(context, Gallery_Title_List.class);
                                gotoNews.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoNews);

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
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void CreateEventName(String userId, String schoolId, String eventName, final ProgressDialog progressDialog) {
        this.userId = userId;
        this.schoolId = schoolId;
        this.eventName = eventName;
        this.progressDialog = progressDialog;

        webMethName = "Gallery_Event_Name_Add";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("User_id", userId);
            jsonObject.put("eventName", eventName);
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
                            if (res.equals("Event successfully created.")) {

                                Toast.makeText(context, "Event successfully created.", Toast.LENGTH_LONG).show();
                                Intent gotoNews = new Intent(context, Gallery_Event_Images_Add.class);
                                gotoNews.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoNews);

                            }else  if (res.equals("This event name already exist")) {
                                Toast.makeText(context," "+res, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(context," "+res, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
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


    public void FetchAllEvents(List<String> eventNameList, List<String> eventIdList,List<String> countList, String standardId,String divisionId,String idOfSchool, Std_Div_Filter_Adapter spinnerAdapter) {
        allNameList = eventNameList;
        allIdList = eventIdList;
        allCountList = countList;
        schoolId = idOfSchool;
        SpinnerAdapter = spinnerAdapter;

        webMethName = "Gallery_Event_Name_List_With_Count";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Standard_Id", standardId);
            jsonObject.put("Division_Id", divisionId);
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
                            String res = response.getString("responseDetails");
                            if (res.equals("Events Not Found.")) {
                                Toast.makeText(context, "Events not available.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                try {
                                    allNameList.clear();
                                    allIdList.clear();
                                    allCountList.clear();
                                    allNameList.add("Select Event");
                                    allIdList.add("0");
                                    allCountList.add("0");
                                    SpinnerAdapter.notifyDataSetChanged();

                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        try {
                                            JSONObject obj = jArr.getJSONObject(count);
                                            allNameList.add(obj.getString("eventName"));
                                            allIdList.add(obj.getString("eventId"));
                                            allCountList.add(obj.getString("count"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    SpinnerAdapter.notifyDataSetChanged();
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


    public void showAlbumList(List<Event_List_Items> event_List_Items, RecyclerView recyclerView, RecyclerView.Adapter galleryAlbumAdapter, String idOfSchool, ProgressDialog progressDialog) {
        schoolId = idOfSchool;
        progressDialogBox = progressDialog;
        adapterForAsyncTask = galleryAlbumAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = event_List_Items;

        webMethName = "Gallery_List";
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
                            if (res.equals("Events Not Found")) {
                                Toast.makeText(context, "Events and images not available.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                try {
                                    ItemsArrayForAsyncTask.clear();
                                    adapterForAsyncTask.notifyDataSetChanged();

                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        try {
                                            JSONObject obj = jArr.getJSONObject(count);
                                            Event_List_Items eventList_items = new Event_List_Items();
                                            eventList_items.seteventName(obj.getString("eventName"));
                                            eventList_items.seteventId(obj.getString("eventId"));
                                            eventList_items.setimagePath(obj.getString("image"));

                                            ItemsArrayForAsyncTask.add(eventList_items);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
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
                        progressDialogBox.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    public void showAlbumFilterList(List<Event_List_Items> event_list_items, RecyclerView recyclerView, Gallery_Title_List_Adapter galleryAlbumAdapter, String standardId, String divisionId, String idOfSchool, ProgressDialog progressDialog) {

        schoolId = idOfSchool;
        progressDialogBox = progressDialog;
        adapterForAsyncTask = galleryAlbumAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = event_list_items;

        webMethName = "Gallery_List_FilterWise";
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
                        progressDialogBox.dismiss();
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Events Not Found.")) {
                                Toast.makeText(context, "Images not available.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                try {
                                    ItemsArrayForAsyncTask.clear();
                                    adapterForAsyncTask.notifyDataSetChanged();

                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        try {
                                            JSONObject obj = jArr.getJSONObject(count);
                                            Event_List_Items eventList_items = new Event_List_Items();
                                            eventList_items.seteventName(obj.getString("eventName"));
                                            eventList_items.seteventId(obj.getString("eventId"));
                                            eventList_items.setimagePath(obj.getString("image"));

                                            ItemsArrayForAsyncTask.add(eventList_items);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
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
                        progressDialogBox.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
