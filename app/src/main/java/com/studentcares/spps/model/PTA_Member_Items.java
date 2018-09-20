package com.studentcares.spps.model;

public class PTA_Member_Items {

    public String fatherImagePath;
    public String studentImagePath;
    public String contactNo;
    public String standard;
    public String division;
    public String studentId;
    public String name;
    public String Address;
    public Boolean selectedCheckBox = false;


    public PTA_Member_Items() {
    }

    public PTA_Member_Items(String studentId, String contactNo, String standard, String division, String fatherImagePath, String studentImagePath, String name, String Address) {

        this.fatherImagePath = fatherImagePath;
        this.studentImagePath = studentImagePath;
        this.studentId = studentId;
        this.standard = standard;
        this.division = division;
        this.contactNo = contactNo;
        this.name = name;
        this.Address = Address;

    }

    public String getfatherImagePath() {
        return fatherImagePath;
    }

    public void setfatherImagePath(String fatherImagePath) {
        this.fatherImagePath = fatherImagePath;
    }

    public String getstudentImagePath() {
        return studentImagePath;
    }

    public void setstudentImagePath(String studentImagePath) {
        this.studentImagePath = studentImagePath;
    }

    public String getstudentId() {
        return studentId;
    }

    public void setstudentId(String studentId) {
        this.studentId = studentId;
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

    public String getcontactNo() {
        return contactNo;
    }

    public void setcontactNo(String contactNo) {
        this.contactNo = contactNo;
    }


    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public Boolean getSelectedCheckBox() {
        return selectedCheckBox;
    }

    public void setSelectedCheckBox(Boolean selectedCheckBox) {
        this.selectedCheckBox = selectedCheckBox;
    }
}
