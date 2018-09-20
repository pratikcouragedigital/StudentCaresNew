package com.studentcares.spps.model;

public class SMS_Inbox_Items {


    public String standard;
    public String division;
    public String mobileNo;
    public String name;
    public String id;
    public String msgBody;
    public String date;
    public String time;
    public String msgType;


    public SMS_Inbox_Items() {
    }

    public SMS_Inbox_Items(String msgType, String msgBody, String date, String time, String name, String id, String standard, String division, String mobileNo) {


        this.name = name;
        this.id = id;
        this.standard = standard;
        this.division = division;
        this.mobileNo = mobileNo;
        this.msgBody = msgBody;
        this.date = date;
        this.time = time;
        this.msgType = msgType;

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


    public String getmsgBody() {
        return msgBody;
    }

    public void setmsgBody(String msgBody) {
        this.msgBody = msgBody;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String gettime() {
        return time;
    }

    public void settime(String time) {
        this.time = time;
    }

    public String getmsgType() {
        return msgType;
    }

    public void setmsgType(String msgType) {
        this.msgType = msgType;
    }


}
