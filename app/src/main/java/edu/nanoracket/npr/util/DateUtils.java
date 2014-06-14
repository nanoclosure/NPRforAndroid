package edu.nanoracket.npr.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {

    private static final String TAG = "DateUtils";

    public static String getPubDate(String str){
        SimpleDateFormat sdf =
                new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzzz");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(str));
        } catch (ParseException e) {
            Log.i(TAG, "failed to convert string to calendar");
        }
        return String.format(new Locale("en", "US"), "%1$tb %1$td %tr", calendar);
    }
}
