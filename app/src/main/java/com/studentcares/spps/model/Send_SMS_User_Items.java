package com.studentcares.spps.model;

public class Send_SMS_User_Items {

    public String firstImagePath;
    public String standard;
    public String division;
    public String mobileNo;
    public Boolean selectedCheckBox = false;
    public String name;
    public String id;



    public Send_SMS_User_Items() {
    }

    public Send_SMS_User_Items(String name, String id, String standard, String division, String firstImagePath, String mobileNo) {

        this.firstImagePath = firstImagePath;
        this.name = name;
        this.id = id;
        this.standard = standard;
        this.division = division;
        this.mobileNo = mobileNo;

    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        //this.firstImagePath =  Image_Url.getUrl()+"student_images/"+firstImagePath;
        this.firstImagePath =  firstImagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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



    public String getmobileNo() {
        return mobileNo;
    }

    public void setmobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Boolean getSelectedCheckBox() {
        return selectedCheckBox;
    }

    public void setSelectedCheckBox(Boolean selectedCheckBox) {
        this.selectedCheckBox = selectedCheckBox;
    }
}
