package com.studentcares.spps.model;

public class GPS_Outwork_Items {

    public String latitude;
    public String longitude;
    public String staffId;
    public String staffName;
    public String date;


    public GPS_Outwork_Items() {
    }

    public GPS_Outwork_Items(String latitude, String longitude, String date, String staffId, String staffName) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.staffId = staffId;
        this.staffName = staffName;
        this.date = date;
    }

    public String getlatitude() {
        return latitude;
    }

    public void setlatitude(String latitude) {
        this.latitude = latitude;
    }


    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getlongitude() {
        return longitude;
    }

    public void setlongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getstaffId() {
        return staffId;
    }

    public void setstaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getstaffName() {
        return staffName;
    }

    public void setstaffName(String staffName) {
        this.staffName = staffName;
    }


}
