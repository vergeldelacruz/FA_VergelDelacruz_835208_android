package com.example.fa_vergeldelacruz_835208_android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for converting date to string or vice versa.
 */
public class DateUtil {
    private static String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Converts the Date into string
     * @param date
     * @return
     */
    public static String convertToDateString(Date date) {
        SimpleDateFormat simpleDate =  new SimpleDateFormat(DATE_FORMAT);
        return simpleDate.format(date);
    }

    /**
     * Converts String into a Date
     * @param dateString
     * @return
     */
    public static Date stringToDate(String dateString) {
        SimpleDateFormat format =  new SimpleDateFormat(DATE_FORMAT);
        Date finalDate = null;
        try {
            finalDate = format.parse(dateString);
        } catch (ParseException e) {
        }
        return finalDate;
    }
}
