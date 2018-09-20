package com.studentcares.spps.singleton;

public class AttendanceCount {
    private static final AttendanceCount ourInstance = new AttendanceCount();
    private static String staff_countPresent ="0";
    private static String staff_countAbsent ="0";
    private static String student_countPresent ="0";
    private static String student_countAbsent ="0";

    public static String getcountAbsent_Staff() {
        return staff_countAbsent;
    }

    public static void setcountAbsent_Staff(String ACounter) {
        staff_countAbsent = ACounter;
    }

    public static String getcountPresent_Staff() {
        return staff_countPresent;
    }

    public static void setcountPresent_Staff(String PCounter) {
        staff_countPresent = PCounter;
    }



    public static String getcountAbsentStudent() {
        return student_countAbsent;
    }

    public static void setcountAbsentStudent(String ACounter_student) {
        student_countAbsent = ACounter_student;
    }

    public static String getcountPresentStudent() {
        return student_countPresent;
    }

    public static void setcountPresentStudent(String PCounter_Student) {
        student_countPresent = PCounter_Student;
    }



    public static AttendanceCount getInstance() {
        return ourInstance;
    }

    private AttendanceCount() {
    }
}
