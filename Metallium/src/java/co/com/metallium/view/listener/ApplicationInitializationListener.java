/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.listener;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.service.GeneralService;
import co.com.metallium.core.util.MetUtilHelper;
import co.com.metallium.core.util.StaticApplicationParameters;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.FileUtil;
import java.util.Enumeration;
import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application life cycle listener.
 * @author Ruben
 */
@WebListener
public class ApplicationInitializationListener implements ServletContextListener, HttpSessionListener {

    @EJB
    GeneralService generalService;
//    @EJB
//    MemoryTimer memoryTimer;

    public void contextInitialized(ServletContextEvent sce) {
        try {

            LogHelper.makeLog(" ... Initializing ... " + MetConfiguration.VERSION);

            //I Initialize all the application parameters from the database.
            generalService.initializeApplicationParameters();

            //Now I configure the context path and the image serlvet context path.
            String contextPath = sce.getServletContext().getContextPath();
            String imageServletPath = MetUtilHelper.removeBackSlashesFromFilePath(contextPath + MetConfiguration.WEB_IMAGE_SERVLET_PATH + "/");
            MetConfiguration.WEB_CONTEXT_PATH = contextPath; //this should be --> /metallium
            MetConfiguration.WEB_IMAGE_SERVLET_PATH = imageServletPath; //this should be --> /metallium/img/
            MetConfiguration.FAVICON_URL = MetConfiguration.STATIC_CONTENT_DOMAIN_NAME + MetConfiguration.WEB_IMAGE_SERVLET_PATH + MetConfiguration.FAVICON_URL; //this should be --> http://metalcity.net/img/resources/favicon.png

            //I Initialize all the application timers.
         //   memoryTimer.initializeTimer();

            LogHelper.makeLog("The application was succesfully initialized. " + MetConfiguration.VERSION);
            configurationCheck();
            webxmlCheck(sce);

        } catch (Exception ex) {
            String msg = "Fuck Re Fuck";
            LogHelper.makeLog(msg + ", the application couldn't be started ..." + ex.getMessage(), ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }

    }

    public void contextDestroyed(ServletContextEvent sce) {
        LogHelper.makeLog("... contextDestroyed ...");
    }

    public void sessionCreated(HttpSessionEvent se) {
//        LogHelper.makeLog("... sessionCreated ..." + se.getSession().getId());
        StaticApplicationParameters.createWebSession();
    }

    public void sessionDestroyed(HttpSessionEvent se) {
//        LogHelper.makeLog("... sessionDestroyed ..." + se.getSession().getId());
        StaticApplicationParameters.destroyWebSession();
    }

    private void configurationCheck() {
        System.out.println("");
        System.out.println("**********Configuration Info**********");
        System.out.println(" Are we in development? " + MetConfiguration.ARE_WE_IN_DEVELOPMENT);
        System.out.println(" Static Content Domain name: " + MetConfiguration.STATIC_CONTENT_DOMAIN_NAME);
        System.out.println(" Domain name: " + MetConfiguration.WEB_DOMAIN_NAME);
        System.out.println(" Context path: " + MetConfiguration.WEB_CONTEXT_PATH);
        System.out.println(" Is Context path enabled? " + MetConfiguration.WEB_CONTEXT_ENABLED);
        System.out.println(" Web page access: " + MetUtilHelper.getWebDomainName());

        System.out.println(" File System: " + MetConfiguration.FILE_SYSTEM);

//        System.out.println(" News Image Dir: " + MetConfiguration.FILE_SYSTEM + MetConfiguration.NEWS_IMAGE_DIR);
//        System.out.println(" News Image Temp Dir: " + MetConfiguration.FILE_SYSTEM + MetConfiguration.NEWS_IMAGE_TEMP_DIR);
//        System.out.println(" Events Image Dir: " + MetConfiguration.FILE_SYSTEM + MetConfiguration.EVENTS_IMAGE_DIR);
//        System.out.println(" Events Image Temp Dir: " + MetConfiguration.FILE_SYSTEM + MetConfiguration.EVENTS_IMAGE_TEMP_DIR);

        System.out.println(" Support Email: " + MetConfiguration.EMAIL_SUPPORT);

    }

    private void webxmlCheck(ServletContextEvent sce) {
        System.out.println("");
        System.out.println("**********web.xml Info****************");

        String uploadDirectory = sce.getServletContext().getFilterRegistration("PrimeFaces FileUpload Filter").getInitParameter("uploadDirectory");
        String thresholdSize = sce.getServletContext().getFilterRegistration("PrimeFaces FileUpload Filter").getInitParameter("thresholdSize");
        System.out.println(" PrimeFaces FileUpload Filter ");
        System.out.println(" uploadDirectory: " + uploadDirectory);
        System.out.println(" thresholdSize: " + thresholdSize);
        System.out.println("**************************************");
        System.out.println("");

    }
}
