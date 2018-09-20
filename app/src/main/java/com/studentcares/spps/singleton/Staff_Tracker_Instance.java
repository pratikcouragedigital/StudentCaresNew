package com.studentcares.spps.singleton;


public class Staff_Tracker_Instance {

    private static boolean isGpsTurnOn = true;

    public static boolean getisGpsTurnOn() {
        return isGpsTurnOn;
    }

    public static void setisGpsTurnOn(boolean isGpsTurnOnrCounter) {
        isGpsTurnOn = isGpsTurnOnrCounter;
    }



}
