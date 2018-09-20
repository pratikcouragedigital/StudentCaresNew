package com.studentcares.spps.model;


public class Event_List_Items {
    public String imagePath;
    public String eventName;
    public String eventId;

    public String imageId;


    public Event_List_Items() {
    }

    public Event_List_Items(String imagePath, String eventName, String eventId, String imageId) {
        this.imagePath = imagePath;
        this.eventName = eventName;
        this.eventId = eventId;
        this.imageId = imageId;
    }

    public String getimageId() {
        return imageId;
    }

    public void setimageId(String imageId) {
        this.imageId = imageId;
    }

    public String getimagePath() {
        return imagePath;
    }

    public void setimagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String geteventName() {
        return eventName;
    }

    public void seteventName(String eventName) {
        this.eventName = eventName;
    }

    public String geteventId() {
        return eventId;
    }

    public void seteventId(String eventId) {
        this.eventId = eventId;
    }
}
