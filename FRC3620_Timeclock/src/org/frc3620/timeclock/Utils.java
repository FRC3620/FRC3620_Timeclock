package org.frc3620.timeclock;

import java.text.SimpleDateFormat;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wegscd
 */
public class Utils {

    public static Logger logger = LoggerFactory.getLogger(Utils.class);

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
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    static public Date getMidDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 12, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
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
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        int s = calendar.get(Calendar.SECOND);
        int ms = calendar.get(Calendar.MILLISECOND);
        calendar.set(1970, Calendar.JANUARY, 1, h, m, s);
        calendar.set(Calendar.MILLISECOND, ms);
        Date rv = calendar.getTime();
        logger.info("getTimeOfDay {} -> {}", date, rv);
        return rv;
    }

    static public Date makeCompositeDate(Date day, Date time) {
        Calendar dayCalendar = Calendar.getInstance();
        Calendar timeCalendar = Calendar.getInstance();
        dayCalendar.setTime(day);
        timeCalendar.setTime(time);
        dayCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        dayCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        dayCalendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
        dayCalendar.clear(Calendar.MILLISECOND);
        return dayCalendar.getTime();
    }

    static public Date dropFractionalSeconds(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
        static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS z");

    public static String diagDate(Object o) {
        if (null == o) {
            return "(null)";
        } else {
            if (o instanceof Date) {
                synchronized(sdf) {
                    return sdf.format(o);
                }
            } else {
                return o.toString();
            }
        }
    }


}
