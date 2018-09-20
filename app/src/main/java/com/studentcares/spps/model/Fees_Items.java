package com.studentcares.spps.model;

public class Fees_Items {

    public String firstImagePath;
    public String studentName;
    public String standard_Id;
    public String standard_Name;
    public String division_Id;
    public String division_Name;
    public String studentId;
    public String totalFees;
    public String months;
    public String balanceFess;
    public String mobileNo;

    public String MonthName;

    public String receiptNo;
    public String month;
    public String paidAmount;
    public String penalty;
    public String balance;
    public String prevBalance;
    public String grandTotal;
    public String paymentMode;
    public String chequeNo;
    public String chequeDate;
    public String chequeBankName;
    public String chequeBranchName;
    public String createdBy;
    public String createdDate;
    public String feetype;

    public String feeTypeCost;
    public String feeTypeName;
    public String srNo;
    public String totalAmount;

    public String FeeTypeID;


    public Fees_Items() {

    }

    public Fees_Items(String FeeTypeID, String feeTypeCost, String feeTypeName, String srNo, String totalAmount, String prevBalance, String month, String chequeBankName, String chequeBranchName, String receiptNo, String paidAmount, String penalty, String chequeDate, String balance, String paymentMode, String grandTotal, String chequeNo, String createdBy, String createdDate, String feetype, String MonthName, String studentName, String standard_Name, String standard_Id, String division_Name, String division_Id, String firstImagePath, String studentId, String totalFees, String months, String balanceFess, String mobileNo) {

        this.firstImagePath = firstImagePath;
        this.studentName = studentName;
        this.standard_Name = standard_Name;
        this.standard_Id = standard_Id;
        this.division_Name = division_Name;
        this.division_Id = division_Id;
        this.studentId = studentId;
        this.totalFees = totalFees;
        this.months = months;
        this.balanceFess = balanceFess;
        this.mobileNo = mobileNo;

        this.MonthName = MonthName;

        this.receiptNo = receiptNo;
        this.prevBalance = prevBalance;
        this.chequeBankName = chequeBankName;
        this.chequeBranchName = chequeBranchName;
        this.month = month;
        this.penalty = penalty;
        this.paidAmount = paidAmount;
        this.balance = balance;
        this.paymentMode = paymentMode;
        this.grandTotal = grandTotal;
        this.chequeNo = chequeNo;
        this.chequeDate = chequeDate;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.feetype = feetype;

        this.feeTypeCost = feeTypeCost;
        this.feeTypeName = feeTypeName;
        this.srNo = srNo;
        this.totalAmount = totalAmount;

        this.FeeTypeID = FeeTypeID;


    }

    public String getFeeTypeID() {
        return FeeTypeID;
    }

    public void setFeeTypeID(String FeeTypeID) {
        this.FeeTypeID = FeeTypeID;
    }

    public String getsrNo() {
        return srNo;
    }

    public void setsrNo(String srNo) {
        this.srNo = srNo;
    }


    public String getfeeTypeCost() {
        return feeTypeCost;
    }

    public void setfeeTypeCost(String feeTypeCost) {
        this.feeTypeCost = feeTypeCost;
    }

    public String getfeeTypeNames() {
        return feeTypeName;
    }

    public void setfeeTypeName(String feeTypeName) {
        this.feeTypeName = feeTypeName;
    }

    public String gettotalAmount() {
        return totalAmount;
    }

    public void settotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getfeetype() {
        return feetype;
    }

    public void setfeetype(String feetype) {
        this.feetype = feetype;
    }

    public String getcreatedDate() {
        return createdDate;
    }

    public void setcreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getcreatedBy() {
        return createdBy;
    }

    public void setcreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getreceiptNo() {
        return receiptNo;
    }

    public void setreceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getprevBalance() {
        return prevBalance;
    }

    public void setprevBalance(String prevBalance) {
        this.prevBalance = prevBalance;
    }

    public String getchequeDate() {
        return chequeDate;
    }

    public void setchequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getchequeBankName() {
        return chequeBankName;
    }

    public void setchequeBankName(String chequeBankName) {
        this.chequeBankName = chequeBankName;
    }

    public String getchequeBranchName() {
        return chequeBranchName;
    }

    public void setchequeBranchName(String chequeBranchName) {
        this.chequeBranchName = chequeBranchName;
    }

    public String getmonth() {
        return month;
    }

    public void setmonth(String month) {
        this.month = month;
    }

    public String getpaidAmounts() {
        return paidAmount;
    }

    public void setpaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getpenalty() {
        return penalty;
    }

    public void setpenalty(String penalty) {
        this.penalty = penalty;
    }

    public String getbalance() {
        return balance;
    }

    public void setbalance(String balance) {
        this.balance = balance;
    }

    public String getpaymentMode() {
        return paymentMode;
    }

    public void setpaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getgrandTotal() {
        return grandTotal;
    }

    public void setgrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getchequeNo() {
        return chequeNo;
    }

    public void setchequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getMonthName() {
        return MonthName;
    }

    public void setMonthName(String MonthName) {
        this.MonthName = MonthName;
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

    public String getstandard_Name() {
        return standard_Name;
    }

    public void setstandard_Name(String standard_Name) {
        this.standard_Name = standard_Name;
    }


    public String getstandard_Id() {
        return standard_Id;
    }

    public void setstandard_Id(String standard_Id) {
        this.standard_Id = standard_Id;
    }

    public String getdivision_Name() {
        return division_Name;
    }

    public void setdivision_Name(String division_Name) {
        this.division_Name = division_Name;
    }


    public String getdivision_Id() {
        return division_Id;
    }

    public void setdivision_Id(String division_Id) {
        this.division_Id = division_Id;
    }

    public String getstudentId() {
        return studentId;
    }

    public void setstudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getbalanceFess() {
        return balanceFess;
    }

    public void setbalanceFess(String balanceFess) {
        this.balanceFess = balanceFess;
    }

    public String getmonths() {
        return months;
    }

    public void setmonths(String months) {
        this.months = months;
    }

    public String gettotalFees() {
        return totalFees;
    }

    public void settotalFees(String totalFees) {
        this.totalFees = totalFees;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

}
