package com.example.tbiapphome;


import java.text.*;
import java.util.Date;
public class CompareTwoDatesTest {
    public static boolean compareTwoDates(String enteredDate, String currentDate) throws ParseException {
        SimpleDateFormat sdformat = new SimpleDateFormat("dd-MM-yyyy");
        Date d1 = sdformat.parse(enteredDate);
        Date d2 = sdformat.parse(currentDate);
        //System.out.println("The date 1 is: " + sdformat.format(d1));
        //System.out.println("The date 2 is: " + sdformat.format(d2));
        if(d1.compareTo(d2) > 0) {
            return true;
        } else if(d1.compareTo(d2) < 0) {
            return false;
        } else if(d1.compareTo(d2) == 0) {
            return true;
        }
        return true;
    }
}