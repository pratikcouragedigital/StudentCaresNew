package com.studentcares.spps.otpReader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.studentcares.spps.R;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.HashMap;

public class SmsReceiver extends BroadcastReceiver {
    String senderId = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        SessionManager sessionManager = new SessionManager(context);
        HashMap<String, String> typeOfUser = sessionManager.getUserDetails();
        senderId = typeOfUser.get(SessionManager.KEY_SMS_SENDERID);
        //senderId = context.getResources().getString(R.string.SmsSenderId);
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();           //---get the SMS_Receive_Attendance_List message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                //---retrieve the SMS_Receive_Attendance_List message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        if (Build.VERSION.SDK_INT <= 22) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        } else {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], bundle.getString("format"));
                        }
                        msg_from = msgs[i].getOriginatingAddress();
                        if (msg_from.contains(senderId)) {
                            String msgBody = msgs[i].getMessageBody();
                            //String pinNo = msgBody.substring(msgBody.indexOf('"') + 1, msgBody.indexOf('"', msgBody.indexOf('"') + 2));
                            String pinNo = msgBody.replaceAll("[^0-9]", "");
                            Log.d("SMS_Receive_Attendance_List", "From -" + msg_from + " : Body- " + msgBody);
                            //CodeVerification.insertCode(pinNo);

                            // Broadcast to Auto read Code sms_list
                            final String DISPLAY_MESSAGE_ACTION = context.getPackageName() + ".CodeSmsReceived";
                            Intent intentCodeSms = new Intent(DISPLAY_MESSAGE_ACTION);
                            intentCodeSms.putExtra("varificationCode", pinNo);
                            context.sendBroadcast(intentCodeSms);
                        }
                    }
                } catch (Exception e) {
                    Log.d("Exception caught", e.getMessage());
                }
            }
        }
    }
}
