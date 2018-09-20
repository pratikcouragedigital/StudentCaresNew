package com.studentcares.spps.model;

public class Attendance_Calender_Items {

    public String Attendance_id;
    public String Student_Id;
    public String SwipCard_No;
    public String School_Id;
    public String Machine_Id;
    public String Machine_no;
    public String Group_Id;
    public String Att_Type;
    public String Att_Date;
    public String Att_Time;
    public String Att_OutTime;
    public String Att_Status;
    public String PresentDate;
    public String Trk_Sms;
    public String PresentMonth;
    public String PresentYear;


    public Attendance_Calender_Items() {
    }

    public Attendance_Calender_Items(String Attendance_id, String Student_Id, String SwipCard_No, String School_Id, String Machine_Id, String Machine_no, String Group_Id, String Att_Type, String Att_Date, String Att_Time, String Att_OutTime, String Att_Status, String Trk_Sms, String PresentDate, String PresentMonth, String PresentYear) {

        this.Attendance_id = Attendance_id;
        this.Student_Id = Student_Id;
        this.SwipCard_No = SwipCard_No;
        this.School_Id = School_Id;

        this.Machine_Id = Machine_Id;
        this.Machine_no = Machine_no;
        this.Group_Id = Group_Id;
        this.Att_Type = Att_Type;
        this.Att_Date = Att_Date;
        this.Att_Time = Att_Time;
        this.Att_OutTime = Att_OutTime;
        this.Att_Status = Att_Status;
        this.Trk_Sms = Trk_Sms;
        this.PresentDate = PresentDate;
        this.PresentMonth = PresentMonth;
        this.PresentYear = PresentYear;

    }

    public String getAtt_OutTime() {
        return Att_OutTime;
    }
    public void setAtt_OutTime(String Att_OutTime) {
        this.Att_OutTime = Att_OutTime;
    }


    public String getPresentDate() {
        return PresentDate;
    }
    public void setPresentDate(String PresentDate) {
        this.PresentDate = PresentDate;
    }


 public String getPresentMonth() {
        return PresentMonth;
    }
    public void setPresentMonth(String PresentMonth) {
        this.PresentMonth = PresentMonth;
    }


    public String getPresentYear() {
        return PresentYear;
    }
    public void setPresentYear(String PresentYear) {
        this.PresentYear = PresentYear;
    }

    public String getTrkSms() {
        return Trk_Sms;
    }
    public void setTrkSms(String Trk_Sms) {
        this.Trk_Sms = Trk_Sms;
    }

    public String getAttStatus() {
        return Att_Status;
    }
    public void setAttStatus(String Att_Status) {
        this.Att_Status = Att_Status;
    }


    public String getAttTime() {
        return Att_Time;
    }
    public void setAttTime(String Att_Time) {
        this.Att_Time = Att_Time;
    }

    public String getAttDate() {
        return Att_Date;
    }
    public void setAttDate(String Att_Date) {
        this.Att_Date = Att_Date;
    }

    public String getAttType() {
        return Att_Type;
    }
    public void setAttType(String Att_Type) {
        this.Att_Type = Att_Type;
    }

    public String getGroupId() {
        return Group_Id;
    }
    public void setGroupId(String Group_Id) {
        this.Group_Id = Group_Id;
    }

    public String getMachineNo() {
        return Machine_no;
    }
    public void setMachineNo(String Machine_no) {
        this.Machine_no = Machine_no;
    }

    public String getMachineId() {
        return Machine_Id;
    }
    public void setMachineId(String Machine_Id) {
        this.Machine_Id = Machine_Id;
    }

//////////////////////////////////////////////////////////////////////////////////////
    public String getAttendanceId() {
        return Attendance_id;
    }

    public void setAttendanceId(String Attendance_id) {
        this.Attendance_id = Attendance_id;
    }

    public String getStudentId() {
        return Student_Id;
    }

    public void setStudentId(String Student_Id) {
        this.Student_Id = Student_Id;
    }

    public String getSwipCardNo() {
        return SwipCard_No;
    }

    public void setSwipCardNo(String SwipCard_No) {
        this.SwipCard_No = SwipCard_No;
    }

    public String getSchoolId() {
        return School_Id;
    }
    public void setSchoolId(String School_Id) {
        this.School_Id = School_Id;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
}