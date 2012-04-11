/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.util;


import com.metallium.utils.framework.utilities.LogHelper;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * cookie utility methods
 */
public class CookieUtil {

    
    public static final int SEVEN_DAYS_IN_SECONDS = 7 * 24 * 60 * 60;

    public static final int NINETY_DAYS_IN_SECONDS = 90 * 24 * 60 * 60;

    public static final int ONE_YEAR_IN_SECONDS = 365 * 24 * 60 * 60;

    public static final String EVENT_COOKIE_PREFIX = "eventid";

    /**
     * getCookieDomain
     * Returns cookie domain depending on host name. for metalcity hosts it is .metalcity.net
     **/
    public static String getCookieDomain(HttpServletRequest request) {
        String cookieDomain = request.getHeader("host");
        if (cookieDomain == null || "".equals(cookieDomain)) {
            cookieDomain = ".metalcity.net";
        } else if (cookieDomain.indexOf(".") > 0) {
            cookieDomain = cookieDomain.substring(cookieDomain.indexOf("."));
        }

        if (cookieDomain.indexOf(":") > 0) {
            cookieDomain = cookieDomain.substring(0, cookieDomain.indexOf(":"));
        }
        
        LogHelper.makeLog("getCookieDomain(): Cookie Domain:" + cookieDomain);
        return cookieDomain;
    }

    /**
     * writeCookie
     * Sets a cookie for the currently requested session
     **/
    public static void writeCookie(String name, String value, int maxAgeSecs, HttpServletResponse response,
            HttpServletRequest request) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setDomain(getCookieDomain(request));
        cookie.setMaxAge(maxAgeSecs);
        response.addCookie(cookie);
    }

    public static void createVshowTestCookie(String name, String value, int maxAgeSecs, HttpServletResponse resonse,
            HttpServletRequest request) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setDomain(getCookieDomain(request));
        cookie.setMaxAge(-1);
        resonse.addCookie(cookie);
    }

    /**
     * Creates a browser session cookie
     * @param name
     * @param value
     * @param response
     */
    public static void writeBrowserSessionCookie(String name, String value, HttpServletResponse response,
            HttpServletRequest request) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setDomain(getCookieDomain(request));
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

    /**
     * writeCookie
     * Sets a cookie for the currently requested session
     **/
    public static void writeCookie(String name,
            String value,
            String domain,
            int maxAgeSecs,
            HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(maxAgeSecs);

        response.addCookie(cookie);

    }

    /**
     * writeSecureCookie
     * Sets a cookie with the secure flag on - so the cookie is only transmitted over SSL
     **/
    public static void writeSecureCookie(String name, String value, int maxAgeSecs, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setDomain(".metalcity.net");
        cookie.setMaxAge(maxAgeSecs);
        cookie.setSecure(true);

        response.addCookie(cookie);

    }

    public static Cookie createEventCookie(long eventId, long eventUserId, HttpServletRequest request) {
        Cookie cookie = new Cookie(EVENT_COOKIE_PREFIX + eventId, String.valueOf(eventUserId));
        cookie.setMaxAge(NINETY_DAYS_IN_SECONDS);
        cookie.setPath("/");
        cookie.setDomain(getCookieDomain(request));
        return cookie;
    }

    public static void writeEventCookie(long eventId, long eventUserId, HttpServletResponse response,
            HttpServletRequest request) {
        writeCookie(EVENT_COOKIE_PREFIX + eventId, String.valueOf(eventUserId), NINETY_DAYS_IN_SECONDS, response,
                request);
    }

    public static String getCookieValue(String cookieName, HttpServletRequest request) {
        //if not is session, look for standard cookie
        Cookie cookies[] = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equalsIgnoreCase(cookieName)) {
                return cookies[i].getValue();
            }
        }
        return null;
    }

    public static boolean cookieExists(String cookieName, HttpServletRequest request) {
        //if not is session, look for standard cookie
        Cookie cookies[] = request.getCookies();
        boolean cookieExists = false;
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equalsIgnoreCase(cookieName)) {
                cookieExists = true;
                break;
            }
        }
        return cookieExists;
    }

    public static void setCookiePath(String cookieName, String path, HttpServletRequest request,
            HttpServletResponse response) {
        Cookie cookies[] = request.getCookies();

        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equalsIgnoreCase(cookieName)) {
                cookies[i].setPath(path);
                cookies[i].setDomain(getCookieDomain(request));
                cookies[i].setMaxAge(cookies[i].getMaxAge());
                response.addCookie(cookies[i]);
            }
        }
    }

    public static Integer getCookieAge(String cookieName, HttpServletRequest request) {

        Cookie cookies[] = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equalsIgnoreCase(cookieName)) {
                return cookies[i].getMaxAge();
            }
        }
        return null;
    }

    public static void deleteCookie(String cookieName, HttpServletResponse response, HttpServletRequest request) {

        Cookie cookie = new Cookie(cookieName, "");
        cookie.setPath("/");
        cookie.setDomain(getCookieDomain(request));
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static Cookie deleteAutoLoginCookie(String cookieName, String cookieValue, String tradeshowCode,
            HttpServletRequest request) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/vshow/" + tradeshowCode + "/newlogin/autoLogin");
        cookie.setDomain(getCookieDomain(request));
        cookie.setMaxAge(0);
        return cookie;
    }

    public static void deleteCookies(List<String> cookieNames, HttpServletResponse response, HttpServletRequest request) {

        for (String cookieName : cookieNames) {
            deleteCookie(cookieName, response, request);
        }
    }

    public static final Cookie createAdminCookie(Long adminId, HttpServletRequest request) {
        Cookie cookie = new Cookie(Constants.ADMIN_ID, String.valueOf(adminId));
        cookie.setPath("/");
        cookie.setDomain(getCookieDomain(request));
        cookie.setMaxAge(-1);

        return cookie;
    }

    public static final Cookie createRefererCheckPassedCookie(Long tradeshowId, HttpServletRequest request) {
        Cookie cookie = new Cookie(Constants.REFERER_CHECK_COOKIE + tradeshowId, "Y");
        cookie.setPath("/");
        cookie.setDomain(getCookieDomain(request));
        cookie.setMaxAge(-1);

        return cookie;
    }

    public static final Cookie createAutoLoginCookie(Long eventUserId, Long eventId, String path,
            HttpServletRequest request) {
        Cookie cookie = new Cookie(Constants.USER_ID + eventId, String.valueOf(eventUserId));
        cookie.setPath(path);
        cookie.setDomain(getCookieDomain(request));
        cookie.setMaxAge(-1);

        return cookie;
    }

    public static final Cookie createUserLoginCookie(Long eventUserId, Long eventId, HttpServletRequest request) {
        Cookie cookie = new Cookie(Constants.USER_ID + eventId, String.valueOf(eventUserId));
        cookie.setPath("/");
        cookie.setDomain(getCookieDomain(request));
        cookie.setMaxAge(-1);
        return cookie;
    }

    public static final Cookie createUserLoginCookie(Long eventUserId, Long eventId, int maxAgeSecs,
            HttpServletRequest request) {
        Cookie cookie = new Cookie(Constants.USER_ID + eventId, String.valueOf(eventUserId));
        cookie.setPath("/");
        cookie.setDomain(getCookieDomain(request));
        cookie.setMaxAge(maxAgeSecs);
        return cookie;
    }

    public static final Cookie createUserAuthCookie(Long eventId, Long eventUserId, HttpServletRequest request) {
        String encryptedAuthToken = AuthUtil.generateAuthKey(eventId, eventUserId);//5*60*1000 (5 minutes)
        Cookie cookie = new Cookie(Constants.AUTH_TOKEN + eventId, encryptedAuthToken);
        cookie.setPath("/");
        cookie.setDomain(getCookieDomain(request));
        cookie.setMaxAge(-1);
        return cookie;
    }

    public static final Cookie createUserAuthCookie(Long eventId, Long eventUserId, Long tokenExpiry,
            HttpServletRequest request) {
        String encryptedAuthToken = AuthUtil.generateAuthKey(eventId, eventUserId, tokenExpiry);//5*60*1000 (5 minutes)
        Cookie cookie = new Cookie(Constants.AUTH_TOKEN + eventId, encryptedAuthToken);
        cookie.setPath("/");
        cookie.setDomain(getCookieDomain(request));
        cookie.setMaxAge(-1);
        return cookie;
    }

    public static final Cookie createUserLocaleCookie(Long eventUserId, Long eventId, String locale,
            HttpServletRequest request) {
        Cookie cookie = new Cookie(Constants.LOCALE_PREFERENCE_COOKIE + eventId, locale);
        cookie.setPath("/");
        cookie.setDomain(getCookieDomain(request));
        cookie.setMaxAge(-1);
        return cookie;
    }

    public static final Cookie createUserLocaleCookieForDeletion(Long eventUserId, Long eventId, String locale,
            HttpServletRequest request) {
        Cookie cookie = new Cookie(Constants.LOCALE_PREFERENCE_COOKIE + eventId, locale);
        cookie.setPath("/");
        cookie.setDomain(getCookieDomain(request));
        cookie.setMaxAge(0);
        return cookie;
    }

    public static final Cookie createUserLocationCookie(Long eventUserId, String locationString,
            HttpServletRequest request) {
        Cookie cookie = new Cookie(Constants.LOCATION_COOKIE + eventUserId, locationString);
        cookie.setPath("/");
        cookie.setDomain(getCookieDomain(request));
        cookie.setMaxAge(-1);
        return cookie;
    }

    public static final String getUserLocaleCookieValue(Long eventId, HttpServletRequest request) {
        return getCookieValue(Constants.LOCALE_PREFERENCE_COOKIE + eventId, request);
    }

    public static final String getUserAuthCookieValue(Long eventId, HttpServletRequest request) {
        return getCookieValue(Constants.AUTH_TOKEN + eventId, request);
    }

    public static final String getLocationCookieValue(Long eventUserId, HttpServletRequest request) {
        return getCookieValue(Constants.LOCATION_COOKIE + eventUserId, request);
    }

    public static final String getTYSCookieValue(HttpServletRequest request) {
        return getCookieValue(Constants.VSHOW_TYS_STATUS_COOKIE, request);
    }

    public static final List<String> getLogoutCookieNamesList(Long tradeshowId, Long userId, boolean includeUserCookie) {

        List<String> cookieList = new LinkedList<String>();
        if (tradeshowId == null || tradeshowId == 0L) {
            return cookieList;
        }
        if (includeUserCookie) {
            cookieList.add(Constants.USER_ID + tradeshowId);
            cookieList.add(Constants.LOCATION_COOKIE + userId);
            cookieList.add(Constants.AUTH_TOKEN + tradeshowId);
        }
        cookieList.add(Constants.ORIGINAL_RELATIVE_URI + tradeshowId);

        return cookieList;
    }
}
