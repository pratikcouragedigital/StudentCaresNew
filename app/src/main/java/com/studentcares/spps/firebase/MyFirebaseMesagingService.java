package com.studentcares.spps.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.studentcares.spps.Leave_List_Own;
import com.studentcares.spps.Notification;
import com.studentcares.spps.GPS_Staff_OutWork;
import com.studentcares.spps.Homework_List_Parents;
import com.studentcares.spps.Homework_List_Teacher;
import com.studentcares.spps.News_List;
import com.studentcares.spps.Notice_Details;
import com.studentcares.spps.R;
import com.studentcares.spps.SMS_InBox_Tab;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.singleton.NotificationCounter_Instance;

import java.util.HashMap;


public class MyFirebaseMesagingService extends FirebaseMessagingService {

    Bitmap bitmap;
    SessionManager sessionManager;
    String userId, userType;

    public int smsListCount = 0;
    public int homeworkCount = 0;
    public int newsCount = 0;
    public int noticeCount = 0;

    public String smsListRead = "Read";
    public String homeworkRead = "Read";
    public String newsRead = "Read";
    public String noticeRead = "Read";
    public NotificationCounter_Instance notificationCount;
    public static final String ACTION_LOCATION_BROADCAST = MyFirebaseMesagingService.class.getName() + "MyFirebaseMesagingService";
    public static final String EXTRA_COUNT = "extra_count";
    public static final String EXTRA_READ = "extra_read";
    public static final String EXTRA_IMFROM = "extra_imFrom";
    String imFrom = "";

    int newsNotificationCounter = 0;

    private static final String REG_TOKEN ="REG_TOKEN";

    String deviceId;
    String token;
    String manufacturer,model,fingerPrint,device,id;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        token = s;
        manufacturer = Build.MANUFACTURER;
        model = Build.MODEL;
        fingerPrint = Build.FINGERPRINT;
        deviceId = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);

        createDeviceId();
    }

    public void createDeviceId() {
        sessionManager = new SessionManager(this);
        sessionManager.createUserFirebaseNotificationToken(token, deviceId,manufacturer,model,fingerPrint);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        showNotification(remoteMessage);
    }

    private void sendMessageToUI(String count, String read, String imFrom) {

        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_COUNT, count);
        intent.putExtra(EXTRA_READ, read);
        intent.putExtra(EXTRA_IMFROM, imFrom);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void showNotification(RemoteMessage remoteMessage) {
        sessionManager = new SessionManager(this);
        HashMap<String, String> typeOfUser = sessionManager.getUserDetails();
        userId = typeOfUser.get(SessionManager.KEY_USERID);
        userType = typeOfUser.get(SessionManager.KEY_USERTYPE);

        String message = remoteMessage.getData().get("NOTIFICATION_TYPE");
        if (remoteMessage.getData().get("NOTIFICATION_TYPE").trim().equals("OPEN_ACTIVITY_NOTICE_DETAILS")) {
            int notificationID = Integer.parseInt(remoteMessage.getData().get("NOTIFICATION_NOTICE_ID"));

            HashMap<String, String> notice = sessionManager.getnoticeCount();
            noticeCount = Integer.parseInt(notice.get(SessionManager.noticeCount));
            noticeRead = notice.get(SessionManager.noticeRead);

            if (noticeRead.equals("Read")) {
                noticeCount = 1;
                noticeRead = "UnRead";
                sessionManager.setnoticeCount(String.valueOf(noticeCount), noticeRead);
            } else {
                noticeRead = "UnRead";
                noticeCount = noticeCount + 1;
                sessionManager.setnoticeCount(String.valueOf(noticeCount), noticeRead);
            }

            Intent intent = new Intent(this, Notice_Details.class);
            intent.putExtra("Title", remoteMessage.getData().get("NOTIFICATION_TITLE"));
            intent.putExtra("Description", remoteMessage.getData().get("NOTIFICATION_DESCRIPTION"));
            intent.putExtra("Image", remoteMessage.getData().get("NOTIFICATION_IMAGE"));
            intent.putExtra("DateOfAdded", remoteMessage.getData().get("NOTIFICATION_DATEOFADDED"));
            intent.putExtra("AddedByName", remoteMessage.getData().get("NOTIFICATION_ADDEDBYNAME"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                bitmap = Glide.with(this)
                        .asBitmap()
                        .load(remoteMessage.getData().get("NOTIFICATION_IMAGE"))
                        .into(100, 100).get();
            } catch (Exception e) {
                e.getMessage();
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this);
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher_short);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_short));
            } else {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
            }
            notificationBuilder.setContentTitle(remoteMessage.getData().get("NOTIFICATION_TITLE"));
            notificationBuilder.setContentText(remoteMessage.getData().get("NOTIFICATION_DESCRIPTION"));
            notificationBuilder.setPriority(2);
            //notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(remoteMessage.getData().get("NOTIFICATION_DESCRIPTION")));
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("NOTIFICATION_DESCRIPTION")));
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setLights(Color.GREEN, 1000, 1000);
            notificationBuilder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(notificationID, notificationBuilder.build());

            imFrom = "Notice";
            sendMessageToUI(String.valueOf(noticeCount), noticeRead, imFrom);
        }
        else if (remoteMessage.getData().get("NOTIFICATION_TYPE").trim().equals("OPEN_ACTIVITY_NEWS_DETAILS")) {
            int notificationID = Integer.parseInt(remoteMessage.getData().get("NOTIFICATION_NEWS_ID"));

            if (newsNotificationCounter == 0) {

                HashMap<String, String> news = sessionManager.getnewsCount();
                newsCount = Integer.parseInt(news.get(sessionManager.newsCount));
                newsRead = news.get(SessionManager.newsRead);

                if (newsRead.equals("Read")) {
                    newsCount = 1;
                    newsRead = "UnRead";
                    sessionManager.setnewsCount(String.valueOf(newsCount), newsRead);
                } else {
                    newsRead = "UnRead";
                    newsCount = newsCount + 1;
                    sessionManager.setnewsCount(String.valueOf(newsCount), newsRead);
                }


                Intent intent = new Intent(this, News_List.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    bitmap = Glide.with(this)
                            .asBitmap()
                            .load(remoteMessage.getData().get("NOTIFICATION_IMAGE"))
                            .into(100, 100).get();
                } catch (Exception e) {
                    e.getMessage();
                }

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this);
                notificationBuilder.setSmallIcon(R.drawable.ic_launcher_short);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_short));
                } else {
                    notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
                }
                notificationBuilder.setContentTitle(remoteMessage.getData().get("NOTIFICATION_TITLE"));
                notificationBuilder.setContentText(remoteMessage.getData().get("NOTIFICATION_DESCRIPTION"));
                notificationBuilder.setPriority(2);
                //notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(remoteMessage.getData().get("NOTIFICATION_DESCRIPTION")));
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("NOTIFICATION_DESCRIPTION")));
                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setSound(defaultSoundUri);
                notificationBuilder.setLights(Color.GREEN, 1000, 1000);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    notificationBuilder.setGroup("News");
                }
                notificationBuilder.setContentIntent(pendingIntent);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel mChannel = new NotificationChannel("NEWS_CHANNEL", "Test Channel", importance);
                    mChannel.setDescription("Testing notification channel");
                    manager.createNotificationChannel(mChannel);
                    notificationBuilder.setChannelId("NEWS_CHANNEL");
                }
                manager.notify(notificationID, notificationBuilder.build());

                imFrom = "News";
                sendMessageToUI(String.valueOf(newsCount), newsRead, imFrom);
                newsNotificationCounter = 1;
            }
        }
        else if (remoteMessage.getData().get("NOTIFICATION_TYPE").trim().equals("OPEN_ACTIVITY_HOMEWORK_DETAILS")) {
            int notificationID = Integer.parseInt(remoteMessage.getData().get("NOTIFICATION_HOMEWORK_ID"));
            Intent intent;

            HashMap<String, String> homework = sessionManager.gethomeworkCount();
            homeworkCount = Integer.parseInt(homework.get(SessionManager.homeworkCount));
            homeworkRead = homework.get(SessionManager.homeworkRead);

            if (homeworkRead.equals("Read")) {
                homeworkCount = 1;
                homeworkRead = "UnRead";
                sessionManager.sethomeworkCount(String.valueOf(homeworkCount), homeworkRead);

            } else {
                homeworkRead = "UnRead";
                homeworkCount = homeworkCount + 1;
                sessionManager.sethomeworkCount(String.valueOf(homeworkCount), homeworkRead);
            }

            if (userType.equals("Student")) {
                intent = new Intent(this, Homework_List_Parents.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else {
                intent = new Intent(this, Homework_List_Teacher.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            try {
                bitmap = Glide.with(this)
                        .asBitmap()
                        .load(remoteMessage.getData().get("NOTIFICATION_IMAGE"))
                        .into(100, 100).get();
            } catch (Exception e) {
                e.getMessage();
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this);
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher_short);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_short));
            } else {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
            }
            notificationBuilder.setContentTitle(remoteMessage.getData().get("NOTIFICATION_TITLE"));
            notificationBuilder.setContentText(remoteMessage.getData().get("NOTIFICATION_DESCRIPTION"));
            notificationBuilder.setPriority(2);
            //notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(remoteMessage.getData().get("NOTIFICATION_DESCRIPTION")));
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("NOTIFICATION_DESCRIPTION")));
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setLights(Color.GREEN, 1000, 1000);
            notificationBuilder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(notificationID, notificationBuilder.build());

            imFrom = "Homework";
            sendMessageToUI(String.valueOf(homeworkCount), homeworkRead, imFrom);
        }
        else if (remoteMessage.getData().get("NOTIFICATION_TYPE").trim().equals("OPEN_ACTIVITY_MESSAGE_DETAILS")) {
            int notificationID = Integer.parseInt(remoteMessage.getData().get("NOTIFICATION_ID"));

            HashMap<String, String> smsList = sessionManager.getsmsListCount();
            smsListCount = Integer.parseInt(smsList.get(SessionManager.smsListCount));
            smsListRead = smsList.get(SessionManager.smsListRead);

            if (smsListRead.equals("Read")) {
                smsListCount = 1;
                smsListRead = "UnRead";
                sessionManager.setsmsListCount(String.valueOf(smsListCount), smsListRead);

            } else {
                smsListRead = "UnRead";
                smsListCount = smsListCount + 1;
                sessionManager.setsmsListCount(String.valueOf(smsListCount), smsListRead);
            }

            Intent intent = new Intent(this, SMS_InBox_Tab.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this);
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher_short);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_short));
            } else {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
            }
            notificationBuilder.setContentTitle(remoteMessage.getData().get("NOTIFICATION_TITLE"));
            notificationBuilder.setContentText(remoteMessage.getData().get("NOTIFICATION_DESCRIPTION"));
            notificationBuilder.setPriority(2);
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("NOTIFICATION_DESCRIPTION")));
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setLights(Color.GREEN, 1000, 1000);
            notificationBuilder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(notificationID, notificationBuilder.build());

            imFrom = "SMS";
            sendMessageToUI(String.valueOf(smsListCount), smsListRead, imFrom);
        }
        else if (remoteMessage.getData().get("NOTIFICATION_TYPE").trim().equals("OPEN_ACTIVITY_STAFF_OUTWORK_GPS")) {
            int notificationID = 0;

            Intent intent = new Intent(this, GPS_Staff_OutWork.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this);
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher_short);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_short));
            } else {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
            }
            notificationBuilder.setContentTitle(remoteMessage.getData().get("NOTIFICATION_TITLE"));
            notificationBuilder.setContentText(remoteMessage.getData().get("NOTIFICATION_DESCRIPTION"));
            notificationBuilder.setPriority(2);
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("NOTIFICATION_DESCRIPTION")));
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setLights(Color.GREEN, 1000, 1000);
            notificationBuilder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(notificationID, notificationBuilder.build());
        }
        else if (remoteMessage.getData().get("NOTIFICATION_TYPE").trim().equals("OPEN_ACTIVITY_LEAVE_APPROVE")) {
            int notificationID = 0;

            Intent intent = new Intent(this, Leave_List_Own.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this);
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher_short);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_short));
            } else {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
            }
            notificationBuilder.setContentTitle(remoteMessage.getData().get("NOTIFICATION_TITLE"));
            notificationBuilder.setContentText(remoteMessage.getData().get("NOTIFICATION_DESCRIPTION"));
            notificationBuilder.setPriority(2);
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("NOTIFICATION_DESCRIPTION")));
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setLights(Color.GREEN, 1000, 1000);
            notificationBuilder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(notificationID, notificationBuilder.build());
        }
        // this code is for test purpose from firebase console.
        else if (remoteMessage.getNotification() != null) {
            Intent intent = new Intent(this, Notification.class);
            intent.putExtra("Title", remoteMessage.getNotification().getTitle());
            intent.putExtra("Message", remoteMessage.getNotification().getBody());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this);
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher_short);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_short));
            } else {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
            }
            notificationBuilder.setContentTitle(remoteMessage.getNotification().getTitle());
            notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
            notificationBuilder.setPriority(2);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setLights(Color.GREEN, 1000, 1000);
            notificationBuilder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0, notificationBuilder.build());
        }
    }
}