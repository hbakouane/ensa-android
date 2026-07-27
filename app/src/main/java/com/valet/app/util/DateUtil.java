package com.valet.app.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("h:mm a", Locale.getDefault());
    private static final SimpleDateFormat FULL_FORMAT =
            new SimpleDateFormat("EEE, MMM d 'at' h:mm a", Locale.getDefault());

    public static String formatDate(long timestamp) {
        return DATE_FORMAT.format(new Date(timestamp));
    }

    public static String formatTime(long timestamp) {
        return TIME_FORMAT.format(new Date(timestamp));
    }

    public static String formatFull(long timestamp) {
        return FULL_FORMAT.format(new Date(timestamp));
    }

    public static String formatRelative(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = timestamp - now;

        if (diff < 0) return "Past";

        long days = TimeUnit.MILLISECONDS.toDays(diff);
        if (days == 0) {
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            if (hours == 0) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                return minutes <= 1 ? "In a minute" : "In " + minutes + " min";
            }
            return hours == 1 ? "In 1 hour" : "In " + hours + " hours";
        }
        if (days == 1) return "Tomorrow";
        if (days < 7) return "In " + days + " days";

        return formatDate(timestamp);
    }

    public static long daysFromNow(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, days);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long daysFromNowAtHour(int days, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, days);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}
