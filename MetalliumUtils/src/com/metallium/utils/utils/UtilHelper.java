/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.utils;

import com.metallium.utils.framework.utilities.LogHelper;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Ruben
 */
public class UtilHelper {

    /**
     * <p>Case insensitive check if a String starts with a specified prefix.</p>
     *
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case insensitive.</p>
     *
     * <pre>
     * StringUtils.startsWithIgnoreCase(null, null)      = true
     * StringUtils.startsWithIgnoreCase(null, "abcdef")  = false
     * StringUtils.startsWithIgnoreCase("abc", null)     = false
     * StringUtils.startsWithIgnoreCase("abc", "abcdef") = true
     * StringUtils.startsWithIgnoreCase("abc", "ABCDEF") = true
     * </pre>
     *
     * @see java.lang.String#startsWith(String)
     * @param str  the String to check, may be null
     * @param prefix the prefix to find, may be null
     * @return <code>true</code> if the String starts with the prefix, case insensitive, or
     *  both <code>null</code>
     * @since 2.4
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return startsWith(str, prefix, true);
    }

    /**
     * <p>Check if a String starts with a specified prefix (optionally case insensitive).</p>
     *
     * @see java.lang.String#startsWith(String)
     * @param str  the String to check, may be null
     * @param prefix the prefix to find, may be null
     * @param ignoreCase inidicates whether the compare should ignore case
     *  (case insensitive) or not.
     * @return <code>true</code> if the String starts with the prefix or
     *  both <code>null</code>
     */
    private static boolean startsWith(String str, String prefix, boolean ignoreCase) {
        if (str == null || prefix == null) {
            return (str == null && prefix == null);
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
    }
    // Empty checks
    //-----------------------------------------------------------------------

    /**
     * <p>Checks if a String is empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the String.
     * That functionality is available in isBlank().</p>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isStringEmpty(String string) {
        if (string == null || string.trim().length() < 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isStringNotEmpty(String string) {
        return !isStringEmpty(string);
    }



    /**
     *
     * @param one
     * @param two
     * @return true = if both Strings are not Null and they are Equal case sensitive
     */
    public static boolean areStringsEqual(String one, String two) {
        
        boolean answser = false;

        if (!isStringEmpty(one) && !isStringEmpty(two) ) {
            if (one.compareTo(two) == 0) {
                return true;
            }
        }

        return answser;
    }

    public static boolean areStringsNotEqual(String one, String two) {
        return !areStringsEqual(one, two);
    }



    /**
     * Checks to see if Integer One and Integer Two are Equal Numbers!
     *
     * @param one
     * @param two
     * @return true = Integer One is Equal to TWO
     *         false =  They are not equal numbers.
     */
    public static boolean areIntegersEqual(Integer one, Integer two) {
        boolean answser = false;

        if (one != null && two != null) {
            if (one.compareTo(two) == 0) {
                return true;
            }
        }

        return answser;
    }

    /**
     * Tests to see if integer ONE is greater than integer TWO
     *
     * @param one
     * @param two
     *
     * @return true = ONE is Greater than TWO
     *         false = ONE is Equal or less than TWO
     */
    public static boolean isIntegerSuperior(Integer one, Integer two) {
        boolean answser = false;

        if (one != null && two != null) {
            if (one.compareTo(two) > 0) {
                return true;
            }
        }

        return answser;
    }

    /**
     * Compares a Date against the actual time.
     * The Time IS UP when the Date entered is BEHIND the actual time.
     *
     * @param date to compare, to see if the Time is UP
     * @return true, the Date you sent if behind than the actual time
     *         false, the Date you sent is ahead the actual time OR
     *                the Date is NULL.
     */
    public static boolean isTimeUp(Date date) {
        if (date == null) {
            return false;
        } else {
            return Calendar.getInstance().getTime().compareTo(date) > 0 ? true : false;

        }
    }

    /**
     * Add a certain amount of minutes to the current Date.
     * 
     * @param minutes
     * @return
     */
    public static Date addMinutesToDate(int minutes) {
        GregorianCalendar date = new GregorianCalendar();
        date.add(Calendar.MINUTE, minutes);
        return date.getTime();
    }

    /**
     * I took this from: http://www.kodejava.org/examples/90.html
     *
     * This method returns the DIference in Minutes between a Given date and the
     * Actual Date. I basically use it to know how many more minutes a user has
     * to finish his ban time.
     *
     * Example: Actual time: 7:00 pm
     *          Date parameter: 7:34 pm
     *          return: 34
     *
     * @param date = Date that I am going to compare against the actual time.
     * @return number of minutes of difference between the date entered and
     * the actual time. Remember this difference can be positive or negative.
     *
     * Its negative if the Date is in the past.
     * Its positive if the Date is in the future.
     *
     */
    public static Long dateDifferenceInMinutes(Date date) {

        if (date == null){
            return null;
        }

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal2.setTime(date);
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();

        long diff = milis2 - milis1;

        long diffMinutes = diff / (60 * 1000);

        return diffMinutes;

    }

    public static String reflectionGetStringPropertyFromClass(Class c, String sField) {
        String answer = null;

        Field field;
        try {
            field = c.getField(sField);
            answer = field.get(c).toString();
        } catch (Exception ex) {
            LogHelper.makeLog(ex);
            answer = "";
        }
        return answer;

    }

}
