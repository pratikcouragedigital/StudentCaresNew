package com.studentcares.spps.singleton;

public class CheckUserStatus {

    private static int status = 0;
    private static int accountStatus = 0;

    public static int getCheckUserStatus() {
        return status;
    }

    public static void setCheckUserStatus(int statusCounter) {
        status = statusCounter;
    }

    public static int getCheckUserAccountStatus() {
        return accountStatus;
    }

    public static void setCheckUserAccountStatus(int statusAccountCounter) {
        accountStatus = statusAccountCounter;
    }
}
