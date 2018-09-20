package com.studentcares.spps.model;


public class Leave_Items {

    public String firstImagePath;
    public String leaveReason;
    public String staffName;
    public String fromDate;
    public String toDate;
    public String staffId;
    public String department;
    public String leaveListId;
    public String approveBy;
    public boolean approvedOrNot;

    public Leave_Items() {
    }

    public Leave_Items(String leaveListId,String staffName, String firstImagePath,String leaveReason, String staffId, String fromDate, String toDate, String department, String approveBy, boolean approvedOrNot) {

        this.firstImagePath = firstImagePath;
        this.leaveReason = leaveReason;
        this.staffName = staffName;
        this.staffId = staffId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.department = department;
        this.leaveListId = leaveListId;
        this.approveBy = approveBy;
        this.approvedOrNot = approvedOrNot;

    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        //  this.firstImagePath =  Image_Url.getUrl()+"staff_images/"+firstImagePath;
        this.firstImagePath = firstImagePath;
    }

    public String getstaffName() {
        return staffName;
    }

    public void setstaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getleaveReason() {
        return leaveReason;
    }

    public void setleaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public String getstaffId() {
        return staffId;
    }

    public void setstaffId(String staffId) {
        this.staffId = staffId;
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


    public String getdepartment() {
        return department;
    }

    public void setdepartment(String department) {
        this.department = department;
    }


    public String getleaveListId() {
        return leaveListId;
    }

    public void setleaveListId(String leaveListId) {
        this.leaveListId = leaveListId;
    }


    public String getapproveBy() {
        return approveBy;
    }

    public void setapproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    public boolean getapprovedOrNot() {
        return approvedOrNot;
    }

    public void setapprovedOrNot(boolean approvedOrNot) {
        this.approvedOrNot = approvedOrNot;
    }

}