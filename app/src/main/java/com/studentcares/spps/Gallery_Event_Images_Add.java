package com.studentcares.spps;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;
import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.adapter.Gallery_Event_Images_Adapter;
import com.studentcares.spps.connectivity.Gallery_Get_Details;
import com.studentcares.spps.connectivity.Get_StdDivSub_Sqlite;
import com.studentcares.spps.imageModule.Image;
import com.studentcares.spps.sessionManager.SessionManager;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import id.zelory.compressor.Compressor;
import tourguide.tourguide.ChainTourGuide;

public class Gallery_Event_Images_Add extends BaseActivity implements View.OnClickListener {

    private final int PICK_IMAGE_MULTIPLE = 1;
    private List<String> imagesBase64List = new ArrayList<String>();
    private Gallery_Event_Images_Adapter eventAdapter;
    private Spinner spinnerEvent;
    private RecyclerView eventRecyclerView;
    private FloatingActionButton addEventImagesFab;
    private FloatingActionButton submitImagesFab;
    private Button createEventButton;
    private GridLayoutManager gridLayoutManager;

    String eventId = "";
    int eventCount = 0;
    private String[] eventArrayList;
    private Std_Div_Filter_Adapter eventSpinnerAdapter;
    private List<String> eventIdList = new ArrayList<String>();
    private List<String> eventNameList = new ArrayList<String>();
    private List<String> eventCountList = new ArrayList<String>();
    private String userId;
    private String schoolId;
    private String eventName = "";
    TextView txtCreateEvent;
    EditText txtEventName;
    File imageFile;
    List<File> imgFileList = new ArrayList<File>();
    File imgFile1, imgFile2, imgFile3, imgFile4, imgFile5, imgFile6, imgFile7, imgFile8, imgFile9, imgFile10;

    public ChainTourGuide mTourGuideHandler;
    public Activity mActivity;
    private Animation mEnterAnimation, mExitAnimation;

    private ProgressDialog progressDialog = null;
    private String timeStamp;
    private File storageDir;

    Spinner standard;
    Spinner division;

    String standardName;
    String standardId;
    private String[] standardArrayList;
    private Std_Div_Filter_Adapter spinnerAdapter;
    private ProgressDialog standardDialogBox;
    private List<String> standardIdList = new ArrayList<String>();
    private List<String> standardNameList = new ArrayList<String>();

    String divisionName;
    String divisionId;
    private String[] divisionArrayList;
    private ProgressDialog divisionDialogBox;
    private List<String> divisionIdList = new ArrayList<String>();
    private List<String> divisionNameList = new ArrayList<String>();

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_event_wise_images_add);

        ActivityInfo activityInfo = null;
        try {
            activityInfo = getPackageManager().getActivityInfo(
                    getComponentName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String title = activityInfo.loadLabel(getPackageManager())
                .toString();

        txtActivityName.setText(title);

        standard = (Spinner) findViewById(R.id.standard);
        division = (Spinner) findViewById(R.id.division);
        spinnerEvent = (Spinner) findViewById(R.id.spinnerEvent);
        txtCreateEvent = (TextView) findViewById(R.id.txtCreateEvent);
        txtEventName = (EditText) findViewById(R.id.txtEventName);
        eventRecyclerView = (RecyclerView) findViewById(R.id.eventRecyclerView);
        addEventImagesFab = (FloatingActionButton) findViewById(R.id.addEventImagesFab);
        submitImagesFab = (FloatingActionButton) findViewById(R.id.submitImagesFab);
        createEventButton = (Button) findViewById(R.id.createEventButton);

        SpannableString content = new SpannableString("Wants to create an event?");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtCreateEvent.setText(content);

        addEventImagesFab.setOnClickListener(this);
        submitImagesFab.setOnClickListener(this);
        createEventButton.setOnClickListener(this);
        txtCreateEvent.setOnClickListener(this);

        txtEventName.setVisibility(View.GONE);
        createEventButton.setVisibility(View.GONE);

        SessionManager sessionManagerNgo = new SessionManager(this);
        HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
        userId = typeOfUser.get(SessionManager.KEY_USERID);
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);

        //runOverlay_ContinueMethod();
        mEnterAnimation = new AlphaAnimation(0f, 1f);
        mEnterAnimation.setDuration(600);
        mEnterAnimation.setFillAfter(true);

        mExitAnimation = new AlphaAnimation(1f, 0f);
        mExitAnimation.setDuration(600);
        mExitAnimation.setFillAfter(true);

        getStandarddDetails();
        getDivisionDetails();
        getEventSpinnerDetails();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addEventImagesFab) {
//            if(eventId.equals("")){
//                Toast.makeText(this, "Please select event!", Toast.LENGTH_LONG);
//            }
            if (eventName.equals("") || eventName == null || eventName.isEmpty()) {
                Toast.makeText(this, "Please Select Event!.", Toast.LENGTH_LONG).show();
            } else {
                FishBun.with(this)
                        .setImageAdapter(new GlideAdapter())
                        .setAllViewTitle("All")
                        .setMaxCount(eventCount)
                        .setActionBarColor(Color.parseColor("#673AB7"), Color.parseColor("#512DA8"), false)
                        .setActionBarTitle("Image Library  ")
                        .textOnImagesSelectionLimitReached("You are not allowed to select more images!")
                        .textOnNothingSelected("You did not select any images")
                        .startAlbum();
            }
        } else if (view.getId() == R.id.submitImagesFab) {
            if (eventId.equals("")) {
                Toast.makeText(this, "Please select event!", Toast.LENGTH_LONG);
            } else {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait. Images Are Adding..");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                Gallery_Get_Details UAAdd_event_images = new Gallery_Get_Details(this);
                UAAdd_event_images.postEventImages(userId, schoolId, eventId, standardId, divisionId, imgFileList, progressDialog);
            }
        } else if (view.getId() == R.id.txtCreateEvent) {
            txtEventName.setVisibility(View.VISIBLE);
            createEventButton.setVisibility(View.VISIBLE);
            submitImagesFab.setVisibility(View.GONE);
            addEventImagesFab.setVisibility(View.GONE);
            spinnerEvent.setSelection(0);
        } else if (view.getId() == R.id.createEventButton) {
            eventName = txtEventName.getText().toString();
            if (eventName.equals("") || eventName == null) {
                Toast.makeText(this, "Please Enter Event Name.", Toast.LENGTH_LONG).show();
            } else {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait.");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                Gallery_Get_Details UAAdd_event_images = new Gallery_Get_Details(this);
                UAAdd_event_images.CreateEventName(userId, schoolId, eventName, progressDialog);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == Define.ALBUM_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
                ArrayList<Parcelable> path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                for (i = 0; i < path.size(); i++) {
                    Uri uri = (Uri) path.get(i);
                    String filePath = getRealPathFromURI(uri);
                    uri = Uri.fromFile(new File(filePath));
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    storageDir = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM);
                    File newFile = new File(storageDir + "/" + timeStamp + i + ".png");
                    try {
                        OutputStream os = new FileOutputStream(newFile);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                        os.flush();
                        os.close();
                    } catch (Exception e) {
                        Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                    }
                    imageCompress(newFile);
                }

                addEventImagesFab.setVisibility(View.GONE);
                submitImagesFab.setVisibility(View.VISIBLE);

                eventRecyclerView.setHasFixedSize(true);
                gridLayoutManager = new GridLayoutManager(this, 3);
                eventRecyclerView.setLayoutManager(gridLayoutManager);

                eventRecyclerView.smoothScrollToPosition(0);
                eventAdapter = new Gallery_Event_Images_Adapter(imgFileList, addEventImagesFab, submitImagesFab);
                eventRecyclerView.setAdapter(eventAdapter);
            }
            else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    public void imageCompress(File sourceFile) {
        Bitmap tempBitmap = null;
        try {
            tempBitmap = new Compressor(this)
                    .setCompressFormat(Bitmap.CompressFormat.PNG)
                    .compressToBitmap(sourceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            OutputStream os = new FileOutputStream(sourceFile);
            tempBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        imgFileList.add(sourceFile);
    }


    private void getStandarddDetails() {
        standardArrayList = new String[]{
                "Standard"
        };
        standardNameList = new ArrayList<>(Arrays.asList(standardArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, standardNameList);
        getListOfStandard();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        standard.setAdapter(spinnerAdapter);
        standard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    standardName = parent.getItemAtPosition(position).toString();
                    standardId = standardIdList.get(position);
                    getDivisionDetails();
                    getListOfDivision();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getListOfStandard() {
        Get_StdDivSub_Sqlite standardSpinnerList = new Get_StdDivSub_Sqlite(this);
        standardSpinnerList.FetchAllstandard(standardNameList, standardIdList, schoolId, spinnerAdapter);
    }

    private void getDivisionDetails() {
        divisionArrayList = new String[]{
                "Division"
        };
        divisionNameList = new ArrayList<>(Arrays.asList(divisionArrayList));
        spinnerAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, divisionNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        division.setAdapter(spinnerAdapter);
        division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    divisionName = parent.getItemAtPosition(position).toString();
                    divisionId = divisionIdList.get(position);
                    getEventSpinnerDetails();
                    getListOfEvents();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getListOfDivision() {
        divisionDialogBox = ProgressDialog.show(this, "", "Fetching Division Please Wait...", true);
        Get_StdDivSub_Sqlite divisionSpinnerList = new Get_StdDivSub_Sqlite(this);
        divisionSpinnerList.FetchAllDivision(divisionNameList, divisionIdList, schoolId, spinnerAdapter, standardId, divisionDialogBox);
    }

    private void getEventSpinnerDetails() {
        eventArrayList = new String[]{
                "Select Event"
        };
        eventNameList = new ArrayList<>(Arrays.asList(eventArrayList));
        eventSpinnerAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, eventNameList);
        eventSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEvent.setAdapter(eventSpinnerAdapter);

        spinnerEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    eventName = parent.getItemAtPosition(position).toString();
                    eventId = eventIdList.get(position);
                    eventCount = Integer.parseInt(eventCountList.get(position));

                    if (eventCount == 0) {
                        Toast.makeText(Gallery_Event_Images_Add.this, "You cannot add images because you already add 10 images for this event.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Gallery_Event_Images_Add.this, "You can add only "+eventCount+" images.", Toast.LENGTH_SHORT).show();
                        txtEventName.setVisibility(View.GONE);
                        createEventButton.setVisibility(View.GONE);
                        submitImagesFab.setVisibility(View.GONE);
                        addEventImagesFab.setVisibility(View.VISIBLE);
                        txtEventName.setText("");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getListOfEvents() {
        Gallery_Get_Details UAGet_event_list = new Gallery_Get_Details(this);
        UAGet_event_list.FetchAllEvents(eventNameList, eventIdList, eventCountList,standardId,divisionId, schoolId, eventSpinnerAdapter);
    }
}