/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.util;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.exception.ValidationException;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import java.io.UnsupportedEncodingException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.UrlValidator;

/**
 * 20111115
 * @author Ruben
 */
public class Assert {

    /**
     * Checks if string size is less than db field size
     *
     * @param str:
     *            field
     * @param dbLegth:
     *            size in database
     * @param msg :
     *            error message
     */
    public static void validStringLength(String str, int dbLength) throws ValidationException {
        if (StringUtils.isEmpty(str)) {
            return;
        }
        validEncodedStringByteCount(MetConfiguration.DEFAULT_ENCODING, str, dbLength);
    }

    private static void validEncodedStringByteCount(String encoding, String value, int maxBytes) throws ValidationException {
        if (value == null) {
            throw new IllegalArgumentException("value is null");
        }
        try {
            int currentByteCount = value.getBytes(encoding).length;
            if (currentByteCount > maxBytes) {
                throw new ValidationException("Comment to long, encoding: " + encoding + ", SupportedBytes: " + maxBytes + ", CurrentBytes: " + currentByteCount);
            }
        } catch (UnsupportedEncodingException ex) {
            LogHelper.makeLog(ex);
        }

    }

    /**
     * 
     * Valid Urls:
     *   http://www.google.com
     *   https://www.google.com
     *   www.google.com
     *   www.google.com/one/two
     *
     * Invalid Urls:
     *   ftp://www.google.com
     *   http:www.google.com
     *   http:/www.google.com
     *   http:/www.google.com 
     *
     * 
     */
    public static String validateLink(String link) throws ValidationException {

        if (UtilHelper.startsWithIgnoreCase(link, "http://")
                || UtilHelper.startsWithIgnoreCase(link, "https://")) {
            return link;
        }

        String link2 = "http://".concat(link); //

        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        if (!urlValidator.isValid(link2)) {
            throw new ValidationException("The url: " + link + " is invalid");

        }

        return link2;
    }

    public static void main(String args[]) {
        //isFileImageTest();
        //deleteDirTest();
        //fileExtentionTest();
        testCreateLink();
    }

    private static void testCreateLink() {
        String l = "http://www.google.com";
        try {
            System.out.println(l + "--> " + validateLink(l));
        } catch (Exception ex) {
            LogHelper.makeLog(ex);
        }


        try {
            l = "https://www.google.com";
            System.out.println(l + "--> " + validateLink(l));
        } catch (Exception ex) {
            LogHelper.makeLog(ex);
        }

        try {
            l = "www.google.com";
            System.out.println(l + "--> " + validateLink(l));
        } catch (Exception ex) {
            LogHelper.makeLog(ex);
        }
        try {
            l = "www.google.com/one/two";
            System.out.println(l + "--> " + validateLink(l));
        } catch (Exception ex) {
            LogHelper.makeLog(ex);
        }
        try {
            l = "www.google.com/one/two?p1=one&p2=two";
            System.out.println(l + "--> " + validateLink(l));
        } catch (Exception ex) {
            LogHelper.makeLog(ex);
        }
        try {
            l = "www.google.com/one/two?p1=one&p2=two|||####*/*/**/*/*@$%^^&";
            System.out.println(l + "--> " + validateLink(l));
        } catch (Exception ex) {
            LogHelper.makeLog(ex);
        }
        try {
            l = "ftp://www.google.com";
            System.out.println(l + "--> " + validateLink(l));
        } catch (Exception ex) {
            LogHelper.makeLog(ex);
        }
        try {
            l = "http:www.google.com";
            System.out.println(l + "--> " + validateLink(l));
        } catch (Exception ex) {
            LogHelper.makeLog(ex);
        }

        try {
            l = "http:/www.google.com";
            System.out.println(l + "--> " + validateLink(l));
        } catch (Exception ex) {
            LogHelper.makeLog(ex);
        }

    }
}
