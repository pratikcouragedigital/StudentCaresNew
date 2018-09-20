package com.studentcares.spps.model;


public class DashBoard_Count_Items {

    public String totalStudent;
    public String absentStudent;
    public String presentStudent;
    public String notPunchStudent;
    public String groupName;
    public String group_Id;
    public String std,stdName;
    public String div;
    public String divName;


    public DashBoard_Count_Items() {
    }

    public DashBoard_Count_Items(String totalStudent, String absentStudent, String presentStudent, String notPunchStudent, String groupName, String group_Id, String std, String stdName, String div, String divName) {

        this.totalStudent = totalStudent;
        this.absentStudent = absentStudent;
        this.presentStudent = presentStudent;
        this.notPunchStudent = notPunchStudent;
        this.groupName = groupName;
        this.group_Id = group_Id;
        this.std = std;
        this.stdName = stdName;
        this.div = div;
        this.divName = divName;

    }



    public String gettotalStudent() {
        return totalStudent;
    }

    public void settotalStudent(String totalStudent) {
        this.totalStudent = totalStudent;
    }

    public String getabsentStudent() {
        return absentStudent;
    }

    public void setabsentStudent(String absentStudent) {
        this.absentStudent = absentStudent;
    }

    public String getpresentStudent() {
        return presentStudent;
    }

    public void setpresentStudent(String presentStudent) {
        this.presentStudent = presentStudent;
    }

    public String getnotPunchStudent() {
        return notPunchStudent;
    }

    public void setnotPunchStudent(String notPunchStudent) {
        this.notPunchStudent = notPunchStudent;
    }

    public String getgroupName() {
        return groupName;
    }

    public void setgroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getgroupId() {
        return group_Id;
    }

    public void setgroupId(String group_Id) {
        this.group_Id = group_Id;
    }

    public String getstd() {
        return std;
    }

    public void setstd(String std) {
        this.std = std;
    }

    public String getdiv() {
        return div;
    }

    public void setdiv(String div) {
        this.div = div;
    }

    public String getdivName() {
        return divName;
    }

    public void setdivName(String divName) {
        this.divName = divName;
    }

    public String getstdName() {
        return stdName;
    }

    public void setstdName(String stdName) {
        this.stdName = stdName;
    }
}