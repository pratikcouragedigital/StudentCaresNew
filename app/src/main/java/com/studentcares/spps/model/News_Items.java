package com.studentcares.spps.model;

public class News_Items {

    public String firstImagePath;
    public String description;
    public String title;
    public String addedDate;
    public String addedByName;
    public String ListId;


    public News_Items() {
    }

    public News_Items(String ListId, String firstImagePath, String addedDate, String description, String title, String addedByName) {

        this.firstImagePath = firstImagePath;
        this.ListId = ListId;
        this.description = description;
        this.title = title;
        this.addedDate = addedDate;
        this.addedByName = addedByName;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
       // this.firstImagePath = Image_Url.getUrl() + "notice_images/" + firstImagePath;
        this.firstImagePath = firstImagePath;
    }

    public String getListId() {
        return ListId;
    }

    public void setListId(String ListId) {
        this.ListId = ListId;
    }

    public String getaddedDate() {
        return addedDate;
    }

    public void setaddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getaddedByName() {
        return addedByName;
    }

    public void setaddedByName(String addedByName) {
        this.addedByName = addedByName;
    }


}