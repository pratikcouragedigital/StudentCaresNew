package com.studentcares.spps;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.studentcares.spps.adapter.Homework_List_Adapter_Parents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatter {


    public static String dateChanged;
    public static int year, month, day;



    public static String ChangeDateFormat(String selectedDate) {

        SimpleDateFormat inputSimpleDateFormat = new SimpleDateFormat("yyyy-M-d");
        SimpleDateFormat outputSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        dateChanged = selectedDate;
        Date date = null;
        try {
            date = inputSimpleDateFormat.parse(selectedDate);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        dateChanged = outputSimpleDateFormat.format(date);

        return dateChanged;
    }
     public static String ChangeDateFormat2(String selectedDate) {

        SimpleDateFormat inputSimpleDateFormat = new SimpleDateFormat("M/d/yyyy");
        SimpleDateFormat outputSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        dateChanged = selectedDate;
        Date date = null;
        try {
            date = inputSimpleDateFormat.parse(selectedDate);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        dateChanged = outputSimpleDateFormat.format(date);

        return dateChanged;
    }
    public static String ChangeDateFormat3(String selectedDate) {

        SimpleDateFormat inputSimpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat outputSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        dateChanged = selectedDate;
        Date date = null;
        try {
            date = inputSimpleDateFormat.parse(selectedDate);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        dateChanged = outputSimpleDateFormat.format(date);

        return dateChanged;
    }

    public static String ChangeDateFormat4(String selectedDate) {

        SimpleDateFormat inputSimpleDateFormat = new SimpleDateFormat("yyyy-M-d hh:ss:s");
        SimpleDateFormat outputSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        dateChanged = selectedDate;
        Date date = null;
        try {
            date = inputSimpleDateFormat.parse(selectedDate);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        dateChanged = outputSimpleDateFormat.format(date);

        return dateChanged;
    }

}
