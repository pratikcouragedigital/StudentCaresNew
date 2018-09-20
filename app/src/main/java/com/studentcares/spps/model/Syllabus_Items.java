package com.studentcares.spps.model;

public class Syllabus_Items {

    public String pdfPath;
    public String stadId;
    public String subjectId;
    public String ListId;
    public String title;


    public Syllabus_Items() {
    }

    public Syllabus_Items(String ListId, String pdfPath, String stadId, String subjectId, String title) {

        this.pdfPath = pdfPath;
        this.ListId = ListId;
        this.stadId = stadId;
        this.subjectId = subjectId;
        this.title = title;
    }

    public String getpdfPath() {
        return pdfPath;
    }

    public void setpdfPath(String pdfPath) {
        // this.pdfPath = Image_Url.getUrl() + "notice_images/" + pdfPath;
        this.pdfPath = pdfPath;
    }

    public String getListId() {
        return ListId;
    }

    public void setListId(String ListId) {
        this.ListId = ListId;
    }

    public String getstadId() {
        return stadId;
    }

    public void setstadId(String stadId) {
        this.stadId = stadId;
    }

    public String getsubjectId() {
        return subjectId;
    }

    public void setsubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

}
