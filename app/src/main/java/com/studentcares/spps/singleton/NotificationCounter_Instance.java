package com.studentcares.spps.singleton;

public class NotificationCounter_Instance {
    private static final NotificationCounter_Instance ourInstance = new NotificationCounter_Instance();

    public static NotificationCounter_Instance getInstance() {
        return ourInstance;
    }

    private NotificationCounter_Instance() {
    }

    private static int attendanceCount = 0;
    private static int smsListCount = 0;
    private static int homeworkCount = 0;
    private static int newsCount = 0;
    private static int noticeCount = 0;

    private static String attendanceRead = "UnRead";
    private static String smsListRead = "UnRead";
    private static String homeworkRead = "UnRead";
    private static String newsRead = "UnRead";
    private static String noticeRead = "UnRead";


    public static int  getattendanceCount() {
        return attendanceCount;
    }
    public static void setattendanceCount(int attCount) {
        attendanceCount = attCount;
    }

    public static int  getsmsListCount() {
        return smsListCount;
    }
    public static void setsmsListCount(int smsCount) {
        smsListCount = smsCount;
    }

    public static int  gethomeworkCount() {
        return homeworkCount;
    }
    public static void sethomeworkCount(int hwkCount) {
        homeworkCount = hwkCount;
    }

    public static int  getnewsCount() {
        return newsCount;
    }
    public static void setnewsCount(int nwsCount) {
        newsCount = nwsCount;
    }
    public static int  getnoticeCount() {
        return noticeCount;
    }
    public static void setnoticeCount(int ntcCount) {
        noticeCount = ntcCount;
    }


    public static String  getattendanceRead() {
        return attendanceRead;
    }
    public static void setattendanceRead(String attRead) {
        attendanceRead = attRead;
    }

    public static String  getsmsListRead() {
        return smsListRead;
    }
    public static void setsmsListRead(String smsRead) {
        smsListRead = smsRead;
    }

    public static String  gethomeworkRead() {
        return homeworkRead;
    }
    public static void sethomeworkRead(String hwkRead) {
        homeworkRead = hwkRead;
    }

    public static String  getnewsRead() {
        return newsRead;
    }
    public static void setnewsRead(String nwsRead) {
        newsRead = nwsRead;
    }
    public static String  getnoticeRead() {
        return noticeRead;
    }
    public static void setnoticeRead(String ntcRead) {
        noticeRead = ntcRead;
    }
}
