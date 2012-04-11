/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.filter;

import co.com.metallium.core.entity.User;
import co.com.metallium.core.service.UserService;
import co.com.metallium.core.util.ApplicationParameters;
import co.com.metallium.core.util.RegexUtil;
import co.com.metallium.core.util.StaticApplicationParameters;
import co.com.metallium.view.util.CookieUtil;
import com.metallium.utils.framework.jsf.FacesUtils;
import com.metallium.utils.framework.jsf.URLFilter;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.TimeWatch;
import com.metallium.utils.utils.UtilHelper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 20101214
 * @author Ruben
 */
public class SecurityFilter extends Ext implements Filter {

    private boolean activateNoCache = false;
    private boolean measureFilterPerformance = true;
    private FilterConfig filterConfig = null;
    private Pattern noFilterPattern = RegexUtil.noWebFilterPattern;
    public static final String AUTHENTICATED_USER = "__AUTHENTICATED_USER__";
    public static final String AUTHENTICATED = "__AUTHENTICATED__";
    public static final URLFilter urlFilter = new URLFilter();
    boolean areYouLogged = false;
    public static final String LAST_URL_REDIRECT_KEY = "__LAST_URL_REDIRECT_KEY__";
    @EJB
    public UserService userService;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Varibles de Servlet
        TimeWatch watch = null;
        if (measureFilterPerformance) {
            watch = TimeWatch.start();
        }

        //I have to make sure that I set the Character Encoding to UTF-8
        setCharacterEncoding(request, response);

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (activateNoCache) {
            setNoCache(httpResponse); //Don't cache
        }

        // URL requested
        String urlRequested = httpRequest.getRequestURI();

        //   System.out.println("Url: " + urlRequested);
        // URLs that I dont filter.
        Matcher noFilter = noFilterPattern.matcher(urlRequested);
        if (noFilter.matches()) {
            System.out.println("doFilter.noFilter: " + urlRequested);
            chain.doFilter(request, response);
            return;
        } else {
            System.out.println("doFilter.filter: " + urlRequested);
        }

        // Validate the IP address
        String ipRemoteAddress = getRemoteAddress(httpRequest);

        if (!urlFilter.ignore(urlRequested)) {

            Map<String, String> headers = extractHeaders(httpRequest);
            StaticApplicationParameters.clientIpAdd(ipRemoteAddress, headers);

            if (ApplicationParameters.isIpUnauthorized(ipRemoteAddress)) {
                httpResponse.sendError(403, "La IP " + ipRemoteAddress + " no se encuentra autorizada.");
                logAccessDenied(ipRemoteAddress, null, urlRequested, "IP no Permitida según la configuración");
                return;
            }
        }




        HttpSession session = ((HttpServletRequest) request).getSession();
        String redirectUrl = (String) session.getAttribute(LAST_URL_REDIRECT_KEY);
        boolean amIauthenticated = isAuthenticated(httpRequest.getSession());
        if (amIauthenticated && (redirectUrl != null) && !redirectUrl.isEmpty()) {
            //http://ocpsoft.com/java/jsf-java/spring-security-what-happens-after-you-log-in/
            session.removeAttribute(LAST_URL_REDIRECT_KEY);
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendRedirect(redirectUrl);
        } else {

            if (!amIauthenticated) {
                String uuid = CookieUtil.getCookieValue("metalcity.cookie", httpRequest);

                if (uuid != null) {
                    Integer userId = userService.rememberFind(uuid);
                    if (userId != null) {
                        try {
                            User user = userService.authenticateUser(userId);
                            HttpSession httpSession = httpRequest.getSession();
                            httpSession.setAttribute("__AUTHENTICATED_USER__", user);
                            httpSession.setAttribute("__PHASE_AUTH__", true);

                        } catch (Exception ex) {
                            LogHelper.makeLog(ex);
                        }
                    } else {
                        CookieUtil.deleteCookie("metalcity.cookie", httpResponse, httpRequest);
                    }
                }
            }

            chain.doFilter(request, response);


        }

        if (measureFilterPerformance) {
            LogHelper.makeLog("SecurityFilter.doFilter whooleee time: " + watch.timeDes());
        }

    }

    
    
    /**
     * 
     * http://stackoverflow.com/questions/6088299/preventing-session-fixation-on-jsf
     * 
     * @param httpResponse 
     */
    private void setNoCache(HttpServletResponse httpResponse) {
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        httpResponse.setDateHeader("Expires", 0); // Proxies.
    }

    /**
     *    what it does is that it makes everything UTF-8 encoding meaning that words
     *    with special characters like "austríaca" are mapped right and the letter í
     *    preserves itself and doesn't get encoded.
     *
     * @param request
     * @param response
     * @throws UnsupportedEncodingException 
     */
    private void setCharacterEncoding(ServletRequest request, ServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    }

    public void destroy() {
        LogHelper.makeLog("Security Filter.destrooooyyyyyy!!!");
    }

    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    private String getRemoteAddress(HttpServletRequest httpRequest) {

        String clientIp = httpRequest.getHeader("X-Forwarded-For"); //client1, proxy1, proxy2

        if (!UtilHelper.isStringEmpty(clientIp)) {

            return FacesUtils.getXForwaredForOrigin(clientIp); //client1

        } else {
            return httpRequest.getRemoteAddr();

        }


    }

    private Map<String, String> extractHeaders(HttpServletRequest httpServletRequest) {
        Map<String, String> headers = new HashMap<String, String>();
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        if (headerNames == null) {
            return headers;
        }
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String header = httpServletRequest.getHeader(name);
            headers.put(name, header);
        }
        return headers;
    }

    private boolean areYouLogged() {
        Boolean is = (Boolean) FacesUtils.getManagedBean(SecurityFilter.AUTHENTICATED);
//        request.getSession().setAttribute("user", user); // Login.



        System.out.println("doFilter.areYouLogged: " + areYouLogged);
        if (is == null) {
            return false;
        }

        return is;
    }

    private boolean isAuthenticated(HttpSession session) {

        Object obj = session.getAttribute("__AUTHENTICATED__");
        if (obj == null) {
            return false;
        }

        return (Boolean) obj;
    }
}
