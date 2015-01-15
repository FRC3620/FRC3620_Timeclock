package org.frc3620.timeclock;

import com.thoughtworks.xstream.XStream;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
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

    static public Date getStartOfPreviousDay() {
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

        zapHMSS(calendar);

        return calendar.getTime();
    }

    static public Date getMidDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        zapHMSS(calendar);

        calendar.add(Calendar.HOUR_OF_DAY, +12);

        return calendar.getTime();
    }

    static public Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        zapHMSS(calendar);

        calendar.add(Calendar.DAY_OF_MONTH, +1);
        calendar.add(Calendar.MILLISECOND, -1);

        return calendar.getTime();
    }

    static public Date getTimeOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.YEAR, 1970);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        Date rv = calendar.getTime();
        logger.info("getTimeOfDay {} -> {}", date, rv);
        return rv;
    }

    static void zapHMSS(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0); // clear does not work on hour, see javadocs
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
    }

    static public Date getStartOfWeek(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        zapHMSS(calendar);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        Date rv = calendar.getTime();
        logger.info("getStartOfWeek {} -> {}", date, rv);
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
                synchronized (sdf) {
                    return sdf.format(o);
                }
            } else {
                return o.toString();
            }
        }
    }

    public static String calledFromWhere() {
        Throwable t = new Throwable();
        t.fillInStackTrace();
        StackTraceElement[] ste = t.getStackTrace();
        for (StackTraceElement ste1 : ste) {
            logger.info("called from {}", ste1);
        }
        return "";
    }

    private static XStream xStream = new XStream();
    
    public static String xml(Object o) {
        return xStream.toXML(o);
    }

}
