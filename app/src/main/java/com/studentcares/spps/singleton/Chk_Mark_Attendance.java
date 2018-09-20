package com.studentcares.spps.singleton;

import java.util.ArrayList;
import java.util.List;

public class Chk_Mark_Attendance {

    private static List<String> absentList = new ArrayList<String>();
    private static List<String> presentList = new ArrayList<String>();
    private static List<String> AllStudentList = new ArrayList<String>();
    private static List<String> absentStaffList = new ArrayList<String>();
    private static List<String> selectedStudentList = new ArrayList<String>();
    private static String status;

    public static  String getstatus(){
        return status;
    }

    public static  void setstatus (String attStatus){
        status = attStatus;
    }

    public static List<String> getAllStudentListInstance() {
        return AllStudentList;
    }

    public static void setAllStudentListInstance(List<String> AllStudentListArray) {
        AllStudentList = AllStudentListArray;
    }

    public static List<String> getPresentListInstance() {
        return presentList;
    }

    public static void setPresentListInstance(List<String> presentListArray) {
        presentList = presentListArray;
    }

    public static List<String> getAbsentListInstance() {
        return absentList;
    }

    public static void setAbsentListInstance(List<String> absentListArray) {
        absentList = absentListArray;
    }

    public static List<String> getAbsentStaffListInstance() {
        return absentStaffList;
    }

    public static void setAbsentStaffListInstance(List<String> absentStaffListArray) {
        absentStaffList = absentStaffListArray;
    }

    public static List<String> getselectedStudentList() {
        return selectedStudentList;
    }

    public static void setselectedStudentList(List<String> selectedStudentListArray) {
        selectedStudentList = selectedStudentListArray;
    }
}
