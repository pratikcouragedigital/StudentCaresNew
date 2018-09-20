package com.studentcares.spps.model;

public class Notification_Items  {

   
    public String title;
    public String message;
    public String ListId;


    public Notification_Items() {
    }

    public Notification_Items(String ListId, String message, String title ) {


        this.ListId = ListId;
        this.title = title;
        this.message = message;
    }



    public String getListId() {
        return ListId;
    }

    public void setListId(String ListId) {
        this.ListId = ListId;
    }

    public String getmessage() {
        return message;
    }

    public void setmessage(String message) {
        this.message = message;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }


}