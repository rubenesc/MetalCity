/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.util;

import com.metallium.utils.framework.utilities.LogHelper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jjoshi
 */
public class AuthUtil {


    public static String generateAuthKey(Long eventId, Long eventUserId) {
        String authClearText = eventId + "|" + eventUserId + "|" + DateUtil.addTime(
                DateUtil.getCurrentTimeStamp().getTime(), 300000L).getTime();//5*60*1000 (5 minutes)

        String encodedAuthKey = CipherUtil.encrypt(authClearText);

        try {
            encodedAuthKey = URLEncoder.encode(encodedAuthKey, Constants.DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException ex) {
            //ignore this exception since the passed in formatting is a supportd one.
        }
        return encodedAuthKey;
    }

    public static String generateAuthKey(Long eventId, Long eventUserId, Long expiryTime) {
        String authClearText = eventId + "|" + eventUserId + "|" + expiryTime;

        String encodedAuthKey = CipherUtil.encrypt(authClearText);

        try {
            encodedAuthKey = URLEncoder.encode(encodedAuthKey, Constants.DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException ex) {
            LogHelper.makeLog("Unsupported Encoding/Decoding format. ", ex);
        }
        return encodedAuthKey;
    }

    public static String[] decryptAuthKey(String authKey, Boolean authValueDecoded) {
        String decodedAuthKey = authKey;
        if (!authValueDecoded) {
            try {
                decodedAuthKey = URLDecoder.decode(authKey, Constants.DEFAULT_ENCODING);
            } catch (UnsupportedEncodingException ex) {
                LogHelper.makeLog("Unsupported Encoding/Decoding format. ", ex);
            }
        }
        String decryptedAuthKey = CipherUtil.decrypt(decodedAuthKey);
        return decryptedAuthKey.split("\\|");
    }

    public static String encodeAuthKey(String authKey){
        String encodedAuthKey = authKey;
         try {
                encodedAuthKey = URLEncoder.encode(authKey, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                LogHelper.makeLog("Unsupported Encoding/Decoding format. ", ex);
                //ignore this exception since the passed in formatting is a supportd one.
            }
        return encodedAuthKey;
    }
}

