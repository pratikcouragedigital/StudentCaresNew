package com.studentcares.spps.model;

public class Homework_Items {

    public String firstImagePath;
    public String subjectName;
    public String standard;
    public String division;
    public String teacherName;
    public String submissionDate;
    public String homework;
    public String homeworkTitle;
    public String addedDate;
    public String ListId;


    public Homework_Items() {
    }

    public Homework_Items(String ListId, String subjectName, String standard, String division, String firstImagePath, String teacherName, String submissionDate, String addedDate, String homework, String homeworkTitle) {

        this.firstImagePath = firstImagePath;
        this.ListId = ListId;
        this.subjectName = subjectName;
        this.standard = standard;
        this.division = division;
        this.submissionDate = submissionDate;
        this.teacherName = teacherName;
        this.homework = homework;
        this.homeworkTitle = homeworkTitle;
        this.addedDate = addedDate;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        //this.firstImagePath =  Image_Url.getUrl()+"homework_images/"+firstImagePath;
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



    public String getsubjectName() {
        return subjectName;
    }

    public void setsubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getstandard() {
        return standard;
    }

    public void setstandard(String standard) {
        this.standard = standard;
    }

    public String getdivision() {
        return division;
    }

    public void setdivision(String division) {
        this.division = division;
    }

    public String getteacherNames() {
        return teacherName;
    }

    public void setteacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getsubmissionDate() {
        return submissionDate;
    }

    public void setsubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String gethomework() {
        return homework;
    }
    public void sethomework(String homework) {
        this.homework = homework;
    }


    public String gethomeworkTitle() {
        return homeworkTitle;
    }
    public void sethomeworkTitle(String homeworkTitle) {
        this.homeworkTitle = homeworkTitle;
    }

}
