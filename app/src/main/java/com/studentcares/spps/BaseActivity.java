package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.studentcares.spps.backup_restore.Backup_Restore;
import com.studentcares.spps.check_OnlineOrOffline.ConnectivityReceiver;
import com.studentcares.spps.check_OnlineOrOffline.MyApplication;
import com.studentcares.spps.connectivity.Data_Synchronisation;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.sqlLite.DataBaseHelper;


import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener {

    Toolbar toolbar;
    SessionManager sessionManager;
    CoordinatorLayout coordinatorLayout;
    String schoolId;
    String userId;
    String userType;
    String username;
    DataBaseHelper mydb;
    private FrameLayout contentFrame;

//    ImageView userPic;
    TextView txtUserName;
    public TextView txtActivityName;
    String ActiveUserId;
    String ActiveUserName;
    String ActiveUserPath;

    RelativeLayout isOnlineLayout;

    private static final int READ_STORAGE_PERMISSION_REQUEST = 1;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 2;
    int backupOrRestore;
    private ProgressDialog progressDialog;
    String calledFrom = "Menu";

    CircleImageView userPic;

    Handler mHandler;
    boolean connected = false;
    boolean mStopHandler = false;
    Snackbar snackbar;
    CoordinatorLayout baseActivityCoordinateLayout;

    @Override
    public void setContentView(int layoutResID) {

        mydb = new DataBaseHelper(BaseActivity.this);

        SessionManager sessionManagerNgo = new SessionManager(BaseActivity.this);
        HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
        username = typeOfUser.get(SessionManager.KEY_NAME);

        coordinatorLayout = (CoordinatorLayout) getLayoutInflater().inflate(R.layout.base_activity, null);
        contentFrame = (FrameLayout) coordinatorLayout.findViewById(R.id.contentFrame);
        txtUserName = (TextView) coordinatorLayout.findViewById(R.id.txtUserName);
        txtActivityName = (TextView) coordinatorLayout.findViewById(R.id.txtActivityName);
        baseActivityCoordinateLayout = (CoordinatorLayout) coordinatorLayout.findViewById(R.id.baseActivityCoordinateLayout);
//        userPic = (ImageView) coordinatorLayout.findViewById(R.id.userPic);
        userPic = (CircleImageView) coordinatorLayout.findViewById(R.id.userPic);
        isOnlineLayout = (RelativeLayout) coordinatorLayout.findViewById(R.id.isOnline);
        getLayoutInflater().inflate(layoutResID, contentFrame, true);

        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoProfile = new Intent(getApplicationContext(), My_Profile.class);
                startActivity(gotoProfile);
            }
        });

        toolbar = (Toolbar) coordinatorLayout.findViewById(R.id.app_bar);

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getActiveUser();
        ActiveUserId = user.get(SessionManager.KEY_USERID);  // get id
        ActiveUserName = user.get(SessionManager.KEY_NAME);  // get Name

        HashMap<String, String> user2 = sessionManager.getUserDetails();
        ActiveUserPath = user2.get(SessionManager.KEY_USERR_LOGO);
        username = user2.get(SessionManager.KEY_NAME);
        userType = user2.get(SessionManager.KEY_USERTYPE);

        checkConnection();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
//            getSupportActionBar().setHomeButtonEnabled(false);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setLogo(R.drawable.user_logo);
//            getSupportActionBar().setIcon(R.drawable.user_logo);
//            getSupportActionBar().setDisplayUseLogoEnabled(true);

            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        super.setContentView(coordinatorLayout);

        txtUserName.setText(ActiveUserName);
//      String ActiveUserPath = "http://spps.studentcares.net/Content/Images/SchoolPanel/StaffPhotos/020160226_203457.jpg";
        RequestOptions options = new RequestOptions().centerCrop();

        Glide.with(userPic.getContext())
                .asBitmap()
                .load(ActiveUserPath)
                .apply(options)
                .into(userPic);


        if (ActiveUserPath.equals(" ") || ActiveUserPath == null || ActiveUserPath.equals("")){

            if(userType.equals("Student")){
                userPic.setImageResource(R.drawable.student);
            }
            else {
                userPic.setImageResource(R.drawable.user_logo);
            }
        }
        else{
            RequestOptions options2;
            if(userType.equals("Student")){
                options2 = new RequestOptions().error(R.drawable.student);
            }
            else {
                options2 = new RequestOptions().error(R.drawable.user_logo);
            }

            Glide.with(userPic.getContext())
                    .asBitmap()
                    .load(ActiveUserPath)
                    .apply(options2)
                    .into(userPic);
        }

        if(userType.equals("Student")){
            userPic.setBorderColor(getResources().getColor(R.color.colorwhite));
        }
        else if(userType.equals("UserAdmin")){
            userPic.setBorderColor(getResources().getColor(R.color.colorAccent));
        }
        else if(userType.equals("Staff")){
            userPic.setBorderColor(getResources().getColor(R.color.colorGreen500));
        }
        else if(userType.equals("NonTeachingStaff")){
            userPic.setBorderColor(getResources().getColor(R.color.thirdFabBtn));
        }
    }

//    public void displayAlertOnline(String isOnline, Context context)
//    {
//
//        if(isOnline.equals("Online")){
//            Toast.makeText(context, "Online", Toast.LENGTH_SHORT).show();
////            snackbar = Snackbar
////                    .make(baseActivityCoordinateLayout, "You are back to online", Snackbar.LENGTH_LONG);
////
////            snackbar.show();
//            isOnlineLayout.setBackground(context.getResources().getDrawable(R.drawable.is_online));
//
//        }
//        else{
//            Toast.makeText(context, "Offline", Toast.LENGTH_SHORT).show();
////            snackbar = Snackbar
////                    .make(baseActivityCoordinateLayout, "No Connection", Snackbar.LENGTH_LONG);
////            snackbar.show();
//            isOnlineLayout.setBackground(context.getResources().getDrawable(R.drawable.is_offline));
//        }
//    }

    @Override
    public void onClick(final View v) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.addNewStudent:
                Intent intent = new Intent(this, My_New_Account.class);
                startActivity(intent);
                return true;
                case R.id.notifications:
                Intent gotnotifications = new Intent(this, Notification.class);
                startActivity(gotnotifications);
                return true;
//            case R.id.synchDetails:
////                Toast.makeText(this, "Will Synch later.", Toast.LENGTH_SHORT).show();
//
//                GetAsynchData();
//                sessionManager.dataIsSynched(false);
//                Intent gotoHome = new Intent(this, Home_Menu.class);
//                startActivity(gotoHome);
//                return true;
//            case R.id.backup:
//                backupOrRestore = 0;
//                checkReadWritePermission();
//                return true;
//            case R.id.restore:
//                backupOrRestore = 1;
//                checkReadWritePermission();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void GetAsynchData() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Data Synchronization In Progress Please Wait.");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        try {
            Data_Synchronisation getNewsList = new Data_Synchronisation(this);
            getNewsList.GetAsynchData(schoolId, userId, calledFrom, progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void exportOrImportDB() {
        if (backupOrRestore == 0) {
            Backup_Restore backup_restore = new Backup_Restore();
            backup_restore.exportDB(this);
        } else {
            Backup_Restore backup_restore = new Backup_Restore();
            backup_restore.importDB(this);
        }
    }

    public void checkReadWritePermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestWriteStoragePermission();
        } else {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadStoragePermission();
            } else {
                exportOrImportDB();
            }
        }
    }

    private void requestReadStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            exportOrImportDB();
        } else {
            // Read permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST);
        }
    }

    private void requestWriteStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            exportOrImportDB();
        } else {
            // Write permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == WRITE_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestReadStoragePermission();
            } else {
                Toast.makeText(this, "Write storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == READ_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportOrImportDB();
            } else {
                Toast.makeText(this, "Read storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            isOnlineLayout.setBackground(getResources().getDrawable(R.drawable.is_online));
            message = "Good! Connected to Internet";
            color = Color.WHITE;
//            snackbar = Snackbar
//                    .make(baseActivityCoordinateLayout, message, Snackbar.LENGTH_LONG);
        }
        else {
            isOnlineLayout.setBackground(getResources().getDrawable(R.drawable.is_offline));
            message = "Sorry! You Are Offline.";
            color = Color.RED;
            snackbar = Snackbar
                    .make(baseActivityCoordinateLayout, message, Snackbar.LENGTH_INDEFINITE);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();

        }


    }
}