package com.studentcares.spps;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


public class UpdateUI extends Service {

    public static final String ACTION_LOCATION_BROADCAST = UpdateUI.class.getName() + "UpdateUI";
    public static final String EXTRA_COUNT = "extra_count";
    public static final String EXTRA_READ = "extra_read";
    public static final String EXTRA_IMFROM = "extra_imFrom";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public  void  UpdateUIForNotificationRead(String count, String read, String imFrom){
        sendMessageToUI(count, read, imFrom);
    }

    public void sendMessageToUI(String count, String read, String imFrom) {

        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_COUNT, count);
        intent.putExtra(EXTRA_READ, read);
        intent.putExtra(EXTRA_IMFROM, imFrom);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
