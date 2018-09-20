package com.studentcares.spps.singleton;

import java.util.ArrayList;
import java.util.List;

public class Send_SMS_Check_User {

    private static List<String> PtaMobileList = new ArrayList<String>();


    private static List<String> noList = new ArrayList<String>();
    private static List<String> idList = new ArrayList<String>();


    public static List<String> getNoList() {
        return noList;
    }

    public static void setNoList(List<String> noListArray) {
        noList = noListArray;
    }

    public static List<String> getIdList() {
        return idList;
    }

    public static void setIdList(List<String> idListArray) {
        idList = idListArray;
    }



    public static List<String> getPtaMobileListInstance() {
        return PtaMobileList;
    }

    public static void setPtaMobileListInstance(List<String> PtaMobileListArray) {
        PtaMobileList = PtaMobileListArray;
    }


}
