package com.studentcares.spps.singleton;

public class FeesStandardDivisionInstance {
    private static String standard = "";
    private static String division = "";

    public static String getStandard() {
        return standard;
    }
    public static void setStandard(String standardId) {
        standard = standardId;
    }

    public static String getDivision() {
        return division;
    }
    public static void setDivision(String divisionId) {
        division = divisionId;
    }
}
