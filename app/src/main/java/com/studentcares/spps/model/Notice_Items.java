package com.studentcares.spps.model;

public class Notice_Items {

    public String firstImagePath;
    public String addedByNaame;
    public String description;
    public String title;
    public String addedDate;
    public String ListId;
    public String StandardId;
    public String DivisionId;
    public String NoticeDetailsFor;
    public String NoticeGroup;


    public Notice_Items() {
    }

    public Notice_Items(String ListId, String NoticeGroup, String StandardId, String DivisionId, String NoticeDetailsFor, String firstImagePath, String addedByNaame, String addedDate, String description, String title) {

        this.firstImagePath = firstImagePath;
        this.NoticeDetailsFor = NoticeDetailsFor;
        this.ListId = ListId;
        this.addedByNaame = addedByNaame;
        this.description = description;
        this.title = title;
        this.addedDate = addedDate;
        this.StandardId = StandardId;
        this.DivisionId = DivisionId;
        this.NoticeGroup = NoticeGroup;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        //this.firstImagePath = Image_Url.getUrl()+"notice_images/"+firstImagePath;
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

    public String getaddedByName() {
        return addedByNaame;
    }

    public void setaddedByName(String addedByNaame) {
        this.addedByNaame = addedByNaame;
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

    public String getNoticeDetailsFor() {
        return NoticeDetailsFor;
    }

    public void setNoticeDetailsFor(String NoticeDetailsFor) {
        this.NoticeDetailsFor = NoticeDetailsFor;
    }

    public String getStandardId() {
        return StandardId;
    }

    public void setStandardId(String StandardId) {
        this.StandardId = StandardId;
    }

    public String getDivisionId() {
        return DivisionId;
    }

    public void setDivisionId(String DivisionId) {
        this.DivisionId = DivisionId;
    }

    public String getNoticeGroup() {
        return NoticeGroup;
    }

    public void setNoticeGroup(String NoticeGroup) {
        this.NoticeGroup = NoticeGroup;
    }


}
