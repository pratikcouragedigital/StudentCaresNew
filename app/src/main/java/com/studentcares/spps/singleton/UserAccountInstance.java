package com.studentcares.spps.singleton;


public class UserAccountInstance {

    private static String nameInstance;
    private static String firstnameInstance;
    private static String lastNameInstance;

    private static UserAccountInstance ourInstance = new UserAccountInstance();

    public static UserAccountInstance getInstance() {
        return ourInstance;
    }

    private UserAccountInstance() {

    }
    public static String  getName() {
        return nameInstance;
    }
    public static void setName(String name) {
        nameInstance = name;
    }

    public static String  getFirstName() {
        return firstnameInstance;
    }
    public static void setFirstName(String firstName) {
        firstnameInstance = firstName;
    }

    public static String  getLastName() {
        return lastNameInstance;
    }
    public static void setLastName(String lastName) {
        lastNameInstance = lastName;
    }
}
