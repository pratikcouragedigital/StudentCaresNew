package com.studentcares.spps.model;


public class Attendance_List_Items {

    public String firstImagePath;
    public String studentName;
    public String standard;
    public String division;
    public String studentId;
    public String status;
    public String staffId,staffName,inTime,outTime;
    public Boolean selectedCheckBox = false;


    public Attendance_List_Items() {
    }

    public Attendance_List_Items(String staffId,String staffName,String inTime,String studentName, String standard, String division, String firstImagePath, String studentId, String status, String outTime) {

        this.firstImagePath = firstImagePath;
        this.studentName = studentName;
        this.standard = standard;
        this.division = division;
        this.studentId = studentId;
        this.status = status;
        this.staffId = staffId;
        this.staffName = staffName;
        this.inTime = inTime;
        this.outTime = outTime;

    }

    public String getstaffName() {
        return staffName;
    }

    public void setstaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getstaffId() {
        return staffId;
    }

    public void setstaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getinTime() {
        return inTime;
    }

    public void setinTime(String inTime) {
        this.inTime = inTime;
    }

    public String getoutTime() {
        return outTime;
    }

    public void setoutTime(String outTime) {
        this.outTime = outTime;
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

    public String getstudentId() {
        return studentId;
    }

    public void setstudentId(String studentId) {
        this.studentId = studentId;
    }

    public Boolean getSelectedCheckBox() {
        return selectedCheckBox;
    }

    public void setSelectedCheckBox(Boolean selectedCheckBox) {
        this.selectedCheckBox = selectedCheckBox;
    }


    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        this.status = status;
    }
}
