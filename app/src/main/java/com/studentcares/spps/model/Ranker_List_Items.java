package com.studentcares.spps.model;

public class Ranker_List_Items {

    public String firstImagePath;
    public String studentName;
    public String studentId;

    public String rank,obtainedMarks,outOffMarks,percent,grade;

    public Ranker_List_Items() {
    }

    public Ranker_List_Items(String rank,String obtainedMarks,String outOffMarks,String studentName, String firstImagePath, String studentId,String percent,String grade) {

        this.firstImagePath = firstImagePath;
        this.studentName = studentName;
        this.studentId = studentId;
        this.rank = rank;
        this.obtainedMarks = obtainedMarks;
        this.outOffMarks = outOffMarks;
        this.percent = percent;
        this.grade = grade;
    }

    public String getpercent() {
        return percent;
    }

    public void setpercent(String percent) {
        this.percent = percent;
    }

    public String getgrade() {
        return grade;
    }

    public void setgrade(String grade) {
        this.grade = grade;
    }

    public String getobtainedMarks() {
        return obtainedMarks;
    }

    public void setobtainedMarks(String obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public String getrank() {
        return rank;
    }

    public void setrank(String rank) {
        this.rank = rank;
    }

    public String getoutOffMarks() {
        return outOffMarks;
    }

    public void setoutOffMarks(String outOffMarks) {
        this.outOffMarks = outOffMarks;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        //this.firstImagePath =  Image_Url.getUrl()+"student_images/"+firstImagePath;
        this.firstImagePath =  firstImagePath;
    }

    public String getstudentName() {
        return studentName;
    }

    public void setstudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getstudentId() {
        return studentId;
    }

    public void setstudentId(String studentId) {
        this.studentId = studentId;
    }
}
