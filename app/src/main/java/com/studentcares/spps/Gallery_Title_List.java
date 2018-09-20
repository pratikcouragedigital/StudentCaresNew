package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.studentcares.spps.adapter.Gallery_Title_List_Adapter;
import com.studentcares.spps.adapter.Std_Div_Filter_Adapter;
import com.studentcares.spps.connectivity.Gallery_Get_Details;
import com.studentcares.spps.connectivity.Get_StdDivSub_Sqlite;
import com.studentcares.spps.model.Event_List_Items;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.singleton.GalleryStandardDivisionInstance;
import com.studentcares.spps.singleton.StandardDivisionInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Gallery_Title_List extends BaseActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    public List<Event_List_Items> event_List_Items = new ArrayList<Event_List_Items>();
    private ProgressDialog progressDialog;
    private Gallery_Title_List_Adapter galleryAlbumAdapter;
    FloatingActionButton fabAddEventImages;
    String userType,schoolId;
    private final int PICK_IMAGE_MULTIPLE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_title_list);

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
        userType = typeOfUser.get(SessionManager.KEY_USERTYPE);

        fabAddEventImages = (FloatingActionButton) findViewById(R.id.fabAddEventImages);
        fabAddEventImages.setVisibility(View.GONE);

        if(userType.equals("UserAdmin") || userType.equals("Staff")){
            fabAddEventImages.setOnClickListener(this);
            fabAddEventImages.setVisibility(View.VISIBLE);
        }
        recyclerView = (RecyclerView) findViewById(R.id.galleryAlbumRecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.smoothScrollToPosition(0);
        galleryAlbumAdapter = new Gallery_Title_List_Adapter(event_List_Items);
        recyclerView.setAdapter(galleryAlbumAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();

        Gallery_Get_Details fetchEventList = new Gallery_Get_Details(this);
        fetchEventList.showAlbumList(event_List_Items, recyclerView, galleryAlbumAdapter, schoolId, progressDialog);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fabAddEventImages){
            Intent gotoAddEventImages = new Intent(this, Gallery_Event_Images_Add.class);
            startActivity(gotoAddEventImages);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

