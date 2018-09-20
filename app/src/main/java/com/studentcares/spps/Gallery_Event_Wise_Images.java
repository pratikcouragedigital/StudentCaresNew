package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.studentcares.spps.adapter.Event_Images_Adapter;
import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.connectivity.Gallery_Get_Details;
import com.studentcares.spps.connectivity.Get_StdDivSub_Sqlite;
import com.studentcares.spps.model.Event_List_Items;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.singleton.GalleryStandardDivisionInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Gallery_Event_Wise_Images extends BaseActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    public List<Event_List_Items> event_List_Items = new ArrayList<Event_List_Items>();
    private ProgressDialog progressDialog;
    private Event_Images_Adapter eventImagesAdapter;
    private String eventId;
    private String schoolId;
    FloatingActionButton fabFilter;
    Spinner standard;
    Spinner division;

    String standardName="";
    String standardId="";
    private String[] standardArrayList;
    private Std_Div_Filter_Adapter spinnerAdapter;
    private ProgressDialog standardDialogBox;
    private List<String> standardIdList = new ArrayList<String>();
    private List<String> standardNameList = new ArrayList<String>();

    String divisionName="";
    String divisionId="";
    private String[] divisionArrayList;
    private ProgressDialog divisionDialogBox;
    private List<String> divisionIdList = new ArrayList<String>();
    private List<String> divisionNameList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_event_images);

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

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> typeOfUser = sessionManager.getUserDetails();
        schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);

        Intent intent = getIntent();
        eventId = intent.getStringExtra("EventId");
        standardId = GalleryStandardDivisionInstance.getStandard();
        divisionId = GalleryStandardDivisionInstance.getDivision();

        fabFilter = (FloatingActionButton) findViewById(R.id.fabFilter);
        fabFilter.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.albumImagesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView.smoothScrollToPosition(0);
        eventImagesAdapter = new Event_Images_Adapter(event_List_Items);
        recyclerView.setAdapter(eventImagesAdapter);

        getList();

    }

    private void getList() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();

        Gallery_Get_Details gallery_Event_wise_ImagesGet = new Gallery_Get_Details(this);
        gallery_Event_wise_ImagesGet.showEventImages(event_List_Items, recyclerView, eventImagesAdapter, schoolId, eventId, standardId, divisionId, progressDialog);

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fabFilter){
            filterAlterBox();
        }
    }

    private void filterAlterBox() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.MyDialogTheme);

        RelativeLayout layout = new RelativeLayout(this);
        layout.setLayoutParams(new RelativeLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT));
        int margin = (int) getResources().getDimension(R.dimen.margin);
        standard = new Spinner(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        standard.setLayoutParams(params);
        params.setMargins(margin, margin, margin, margin);
        standard.setId(Integer.parseInt("1"));


        division = new Spinner(this);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, standard.getId());
        params.setMargins(margin, margin, margin, margin);
        division.setLayoutParams(params);
        division.setId(Integer.parseInt("2"));

        TextView txtView = new TextView(this);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, division.getId());
        params.setMargins(margin, margin, margin, margin);
        txtView.setLayoutParams(params);

        layout.addView(txtView);
        layout.addView(standard);
        layout.addView(division);

        alert.setTitle(Html.fromHtml("<b>" + "Select Standard & Division." + "</b>"));

        getStandarddDetails();
        getDivisionDetails();
        alert.setView(layout);

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (standardName == null || standardName.isEmpty()) {
                    Toast.makeText(Gallery_Event_Wise_Images.this, "Please select Standard.", Toast.LENGTH_LONG).show();
                } else if (divisionName == null || divisionName.isEmpty()) {
                    Toast.makeText(Gallery_Event_Wise_Images.this, "Please select Division.", Toast.LENGTH_LONG).show();
                } else {

                    getList();
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });
        alert.show();
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
//                    Toast.makeText(Gallery_Title_List.this, "Need To call webservice with std div", Toast.LENGTH_SHORT).show();
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
}
