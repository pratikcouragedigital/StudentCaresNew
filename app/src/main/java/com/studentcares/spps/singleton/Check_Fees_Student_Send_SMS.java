package com.studentcares.spps.singleton;

import java.util.ArrayList;
import java.util.List;

public class Check_Fees_Student_Send_SMS {
    private static List<String> studentList = new ArrayList<String>();
    private static List<String> mobileNoList = new ArrayList<String>();
    private static List<String> msgList = new ArrayList<String>();

    public static List<String> getStudentListInstance() {
        return studentList;
    }

    public static void setStudentListInstance(List<String> studentListArray) {
        studentList = studentListArray;
    }


    public static List<String> getmobileNoList() {
        return mobileNoList;
    }

    public static void setmobileNoList(List<String> mobileNoListArray) {
        mobileNoList = mobileNoListArray;
    }


    public static List<String> getmsgList() {
        return msgList;
    }

    public static void setmsgList(List<String> msgListArray) {
        msgList = msgListArray;
    }
}
