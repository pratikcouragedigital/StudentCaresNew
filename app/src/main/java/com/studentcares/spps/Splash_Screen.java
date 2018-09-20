package com.studentcares.spps;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.sqlLite.DataBaseHelper;

import java.util.HashMap;


public class Splash_Screen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    ImageView image1;
    private SessionManager sessionManager;
    private boolean isTableDeleted;
    String isDataSynched="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

        //image1 = (ImageView)findViewById(R.id.logoHeader);

        sessionManager = new SessionManager(Splash_Screen.this);
        isTableDeleted = sessionManager.getIsDBTableDelete();
        if(!isTableDeleted) {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            dataBaseHelper.deleteTableAndCreateTable();
            sessionManager.setIsDBTableDelete(true);           
            String isDataSynched= "No";
            sessionManager.setSynchDate(isDataSynched);
        }

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                sessionManager.checkLogin();
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}