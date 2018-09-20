package com.studentcares.spps.singleton;

import java.util.ArrayList;
import java.util.List;

public class Check_Student_Staff_For_Sms {

    private static List<String> studentList = new ArrayList<String>();
    private static List<String> PtaMobileList = new ArrayList<String>();
    private static List<String> staffList = new ArrayList<String>();
    private static List<String> staff_student_id = new ArrayList<String>();

    public static List<String> getPtaMobileListInstance() {
        return PtaMobileList;
    }

    public static void setPtaMobileListInstance(List<String> PtaMobileListArray) {
        PtaMobileList = PtaMobileListArray;
    }

    public static List<String> getStudentListInstance() {
        return studentList;
    }

    public static void setStudentListInstance(List<String> studentListArray) {
        studentList = studentListArray;
    }


    public static List<String> getstaffList() {
        return staffList;
    }

    public static void setstaffList(List<String> staffListArray) {
        staffList = staffListArray;
    }

    public static List<String> getstaff_student_id() {
        return staff_student_id;
    }

    public static void setstaff_student_id(List<String> staff_student_idArray) {
        staff_student_id = staff_student_idArray;
    }

}
