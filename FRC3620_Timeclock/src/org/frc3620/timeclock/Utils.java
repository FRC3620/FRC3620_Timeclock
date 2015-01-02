package org.frc3620.timeclock;

import java.util.*;

/**
 *
 * @author wegscd
 */
public class Utils {

    static public Date getYesterday() {
        return getPreviousDay(getStartOfDay(new Date()));
    }

    static public Date getPreviousDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    static public Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }

    static public Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
    static public Date getTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int h = calendar.get(Calendar.HOUR);
        int m = calendar.get(Calendar.MINUTE);
        int s = calendar.get(Calendar.SECOND);
        int ms = calendar.get(Calendar.MILLISECOND);
        calendar.set(1970, Calendar.JANUARY, 1, h, m, s);
        calendar.set(Calendar.MILLISECOND, ms);
        return calendar.getTime();
    }
}
