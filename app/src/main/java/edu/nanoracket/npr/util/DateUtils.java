package edu.nanoracket.npr.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {

    public static String getPubDate(String str){
        String pubDateStr;
        SimpleDateFormat sdf =
                new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzzz");
        Date date = new Date();

        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if(hour > 12){
            hour = hour % 12;
            if(minute < 10){
                pubDateStr = formatMonth(month,
                        new Locale("en", "US")) + " "
                        + Integer.toString(day) + " "
                        + Integer.toString(hour) + ":0"
                        + Integer.toString(minute) + "PM";
            }else{
                pubDateStr = formatMonth(month,
                        new Locale("en", "US")) + " "
                        + Integer.toString(day) + " "
                        + Integer.toString(hour) + ":"
                        + Integer.toString(minute) + "PM";
            }
        }else if(hour < 12){
            if(minute < 10){
                pubDateStr = formatMonth(month,
                        new Locale("en", "US")) + " "
                        + Integer.toString(day) + " "
                        + Integer.toString(hour) + ":0"
                        + Integer.toString(minute) + "AM";
            }else{
                pubDateStr = formatMonth(month,
                        new Locale("en", "US")) + " "
                        + Integer.toString(day) + " "
                        + Integer.toString(hour) + ":"
                        + Integer.toString(minute) + "AM";
            }
        }else{
            if(minute < 10){
                pubDateStr = formatMonth(month,
                        new Locale("en", "US")) + " "
                        + Integer.toString(day) + " "
                        + Integer.toString(hour) + ":0"
                        + Integer.toString(minute) + "PM";
            }else{
                pubDateStr = formatMonth(month,
                        new Locale("en", "US")) + " "
                        + Integer.toString(day) + " "
                        + Integer.toString(hour) + ":"
                        + Integer.toString(minute) + "PM";
            }
        }
        return pubDateStr;
    }

    public static String formatMonth(int month, Locale locale) {
        DateFormat formatter = new SimpleDateFormat("MMMM", locale);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MONTH, month);
        return formatter.format(calendar.getTime());
    }
}
