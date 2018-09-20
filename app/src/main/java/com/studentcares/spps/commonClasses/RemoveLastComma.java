package com.studentcares.spps.commonClasses;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import java.util.List;

public class RemoveLastComma {


    String data;
    public Context context;

    public RemoveLastComma(FragmentActivity activity) {
        context = activity;
    }


    public  String CommaRemove (List<String> ptaListFor_Notification){

        int countId = ptaListFor_Notification.size();
        StringBuilder groups_notification = new StringBuilder();
        for (int i= 0; i < countId; i++) {
            String value = ptaListFor_Notification.get(i);
            if(i != countId-1){
                groups_notification.append(value + ",");
            }else{
                groups_notification.append(value);
            }
        }
        data = groups_notification.toString();

        return data;
    }
}
