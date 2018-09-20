package com.studentcares.spps.model;

public class SMS_Report_Items {

    public String id;
    public String msgType;
    public String smsStatus;
    public String mobileNo;
    public String sms;
    public String date;



    public SMS_Report_Items() {
    }

    public SMS_Report_Items(String msgType,String date,String id, String smsStatus, String sms, String mobileNo) {


        this.id = id;
        this.smsStatus = smsStatus;
        this.sms = sms;
        this.mobileNo = mobileNo;
        this.date = date;
        this.msgType = msgType;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getsmsStatus() {
        return smsStatus;
    }

    public void setsmsStatus(String smsStatus) {
        this.smsStatus = smsStatus;
    }

    public String getsms() {
        return sms;
    }

    public void setsms(String sms) {
        this.sms = sms;
    }

    public String getmobileNo() {
        return mobileNo;
    }

    public void setmobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getmsgType() {
        return msgType;
    }

    public void setmsgType(String msgType) {
        this.msgType = msgType;
    }


}