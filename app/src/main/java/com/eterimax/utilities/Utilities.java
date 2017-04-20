package com.eterimax.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

// Noninstantiable utility class
public final class Utilities {

    public static final String DATE_FORMAT_SERVER = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    // Suppress default constructor for noninstantiability
    private Utilities() {
        throw new AssertionError();
    }

    public static boolean hasInternet(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static String simpleServerDayFormat(String serverDate) {
        Calendar calendar = getCalendarObject(serverDate, DATE_FORMAT_SERVER);
        String simpleDateFormat = "MMM dd";
        SimpleDateFormat sdf = new SimpleDateFormat(simpleDateFormat, Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private static Calendar getCalendarObject(String date, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }
}
