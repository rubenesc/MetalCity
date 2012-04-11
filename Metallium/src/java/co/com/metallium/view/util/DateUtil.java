/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;

/**
 *
 * @author jjoshi
 */
public class DateUtil {

    private static Map<TimeZone, Date[]> daylightDates = new HashMap<TimeZone, Date[]>();

    private static final int START_DATE = 0;

    private static final int END_DATE = 1;

    private static final int MILLISECOND_IN_DAYS = 1000 * 60 * 60 * 24;

    public static final SimpleDateFormat SQL_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy hh:mm:ss a z";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    static {
        String[] timeZoneIds = TimeZone.getAvailableIDs();
        for (int i = 0; i < timeZoneIds.length; i++) {
            TimeZone timeZone = TimeZone.getTimeZone(timeZoneIds[i]);
            if (!timeZone.useDaylightTime()) {
                continue;
            }
            GregorianCalendar gc = new GregorianCalendar(timeZone);
            //2008, 1, 1, 0, 0, 0
            gc.set(Calendar.MONTH, 0);
            gc.set(Calendar.DATE, 1);
            gc.set(Calendar.HOUR, 0);
            gc.set(Calendar.MINUTE, 0);
            gc.set(Calendar.SECOND, 0);
            gc.set(Calendar.MILLISECOND, 0);
            int currentYear = gc.get(Calendar.YEAR);
            Date dlStart = null;
            Date dlEnd = null;
            boolean lastTimeInDST = false;
            /**
             * For the current year, walk each hour of the year
             * looking for the time when the dst switch happens.
             * comparing the dst state with the previous hour and the current hour
             */
            while (gc.get(Calendar.YEAR) == currentYear) {
                boolean currentTimeInDST = timeZone.inDaylightTime(gc.getTime());
                if (lastTimeInDST && !currentTimeInDST) {
                    dlEnd = gc.getTime();
                } else if (!lastTimeInDST && currentTimeInDST) {
                    dlStart = gc.getTime();
                }
                lastTimeInDST = currentTimeInDST;
                gc.setTimeInMillis(gc.getTimeInMillis() + (1 * 60 * 60 * 1000)); // move to the next hour
            }
            Date[] times = new Date[2];
            times[START_DATE] = dlStart;
            times[END_DATE] = dlEnd;
            daylightDates.put(timeZone, times);
        }
    }

    public static final boolean isValidTimeZone(String timeZoneId) {
        String[] timeZoneIds = TimeZone.getAvailableIDs();
        for (String tz : timeZoneIds) {
            if (tz.equals(timeZoneId)) {
                return true;
            }
        }
        return false;
    }

    public static final boolean isCurrentDateInRange(long startDate, long endDate) {
        Date currentDate = new Date();
        return new Date(startDate).before(currentDate) && new Date(endDate).after(currentDate);
    }

    public static final boolean isCurrentDateInRange(Long startDate, Long endDate) {
        return isCurrentDateInRange(startDate.longValue(), endDate.longValue());
    }

    public static final boolean isAfterCurrentDate(long date) {
        return new Date(date).after(new Date());
    }

    public static final boolean isBeforeCurrentDate(long date) {
        return new Date(date).before(new Date());
    }

    public static final Date getDaylightStartDate(TimeZone t) {
        if (daylightDates.get(t) != null) {
            return daylightDates.get(t)[START_DATE];
        } else {
            return null;
        }
    }

    public static final Date getDaylightEndDate(TimeZone t) {
        if (daylightDates.get(t) != null) {
            return daylightDates.get(t)[END_DATE];
        } else {
            return null;
        }
    }

    /**
     * These formatDate methods are being called from report-excel.xsl and possibly other,
     * be very careful in changing the method signatures.
     * @param dateMilliseconds
     * @param timezoneId
     * @param format
     * @return
     */
    public static final String formatDate(long dateMilliseconds, String timezoneId, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        return formatDate(dateMilliseconds, timezoneId, formatter);
    }

    public static final String formatDate(long dateMilliseconds, String timezoneId, DateFormat dateFormatter) {
        Date date = new Date(dateMilliseconds);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(timezoneId));
        return dateFormatter.format(date);
    }

    public static final String formatDate(long dateMilliseconds, String timezoneId) {
        return formatDate(dateMilliseconds, timezoneId, new SimpleDateFormat(DEFAULT_DATE_FORMAT));
    }

    /***
     * Used in XSL
     */
    public static final String getCurrentDate(String format, String locale) {
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat(format, new Locale(locale));
        return formatter.format(date);
    }


    //using in webcast.xsl
    public static final String yearFromDate() {
        Calendar cal = Calendar.getInstance();
        return Integer.toString(cal.get(Calendar.YEAR));
    }

    public static final Date trimTimePart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static final Date trimSecondsPart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static final Date rollToStartOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static final Date rollToStartOfDay(Date date, TimeZone timeZone) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 1);
        return cal.getTime();
    }

    public static final Date rollToEndOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    public static final Date rollToEndOfDay(Date date, TimeZone timeZone) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    public static final long millisecondsToMinute(long milliseconds) {
        double oneMintuteInMilliseconds = 60000.0;
        return (long) java.lang.Math.round(milliseconds / oneMintuteInMilliseconds);
    }

    public static final Date getCurrentTimeStamp() {
        return new Date();
    }

    public static final Date getTime(Date date, TimeZone timezone) {
        Calendar cal = Calendar.getInstance(timezone);
        cal.setTime(date);
        //truncating the seconds part
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static final String convertToSQLSyntax(Date date) {
        StringBuilder builder = new StringBuilder();
        builder.append("to_date('");
        builder.append(SQL_DATE_FORMAT.format(date)).append("',");
        builder.append("'yyyy/mm/dd HH24:MI:SS')");
        return builder.toString();
    }

    public static final String getDisplayNameForTimezone(String timezoneId, int longOrShort) {
        TimeZone tz = TimeZone.getTimeZone(timezoneId);
        if (tz.inDaylightTime(new Date())) {
            return tz.getDisplayName(true, longOrShort);
        } else {
            return tz.getDisplayName(false, longOrShort);
        }
    }

    public static final Date addTime(Long time, Long extraTime) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date(time + extraTime));
        return cal.getTime();
    }

    public static final Date addSubstractDays(Long dateInMilliseconds, int daysToAddOrSubstract) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(dateInMilliseconds);
        cal.add(Calendar.DATE, daysToAddOrSubstract);
        return cal.getTime();
    }

    public static final Date addSubtractMonths(Long dateInMilliseconds, int monthsToAddOrSubstract) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(dateInMilliseconds);
        cal.add(Calendar.MONTH, monthsToAddOrSubstract);
        return cal.getTime();
    }

    public static final Date addSubtractYears(Long dateInMilliseconds, int yearsToAddOrSubstract) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(dateInMilliseconds);
        cal.add(Calendar.YEAR, yearsToAddOrSubstract);
        return cal.getTime();
    }

    //Returns the number of days passed since the passed in date.
    public static Long getNumOfDaysPassed(Date date) {
        Date currentDate = Calendar.getInstance().getTime();
        Long noOfDays = ((currentDate.getTime() - date.getTime())
                / MILLISECOND_IN_DAYS);
        return noOfDays;
    }
}
