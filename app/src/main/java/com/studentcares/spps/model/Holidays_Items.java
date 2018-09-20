package com.studentcares.spps.model;

public class Holidays_Items {

    public String firstImagePath;
    public String holidayName;
    public String fromDate;
    public String toDate;
    public String holidayId;

    public Holidays_Items() {
    }

    public Holidays_Items(String holidayName, String firstImagePath, String holidayId, String fromDate, String toDate) {

        this.firstImagePath = firstImagePath;
        this.holidayName = holidayName;
        this.holidayId = holidayId;
        this.fromDate = fromDate;
        this.toDate = toDate;

    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        //  this.firstImagePath =  Image_Url.getUrl()+"staff_images/"+firstImagePath;
        this.firstImagePath = firstImagePath;
    }

    public String getholidayName() {
        return holidayName;
    }

    public void setholidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getholidayId() {
        return holidayId;
    }

    public void setholidayId(String holidayId) {
        this.holidayId = holidayId;
    }

    public String getfromDate() {
        return fromDate;
    }

    public void setfromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String gettoDate() {
        return toDate;
    }

    public void settoDate(String toDate) {
        this.toDate = toDate;
    }

}
