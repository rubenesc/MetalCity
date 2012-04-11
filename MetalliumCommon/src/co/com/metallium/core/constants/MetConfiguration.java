/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.constants;

import java.util.Locale;

/**
 *
 * @author Ruben 
 */
public class MetConfiguration {

    public static String VERSION = "Metallium Version 1.0 - 2011.12.04 _\\,,/";
    public static boolean ARE_WE_IN_DEVELOPMENT = false;
    public static boolean SHOW_NETWORKS = true;
    public static boolean APPLICATION_SCOPE_ACTIVE = false;
    public static boolean XXX = true;

    //Constants
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static Integer COMMENT_MAX_LENGTH = 2000;
    public static Integer WALL_POST_MAX_LENGTH = 3000;
    public static Integer NEWS_EVENT_MAX_LENGTH = 8000;
    public static String WEB_USER_ACTIVATION_SERVLET_PATH = "/user/activate"; //Servlet path that activates a user (ActivateUserServlet it has to match!!!). This is more like a constant so it is not in the DB. I


    //Variables that are in DB
    public static String WEB_DOMAIN_NAME = null; //It contains the domain name of the application. In this case it will probably be 'metalcity.net' :)
    public static String WEB_CONTEXT_PATH = null; //It contains the context path like for example "metallium", which we obtain from the web tier.
    public static boolean WEB_CONTEXT_ENABLED = false; //tells me if I need the to use 'WEB_CONTEXT_PATH' when Im building some urls. It is not needed because the Application server ignores the context because the context is set as default.

    //It contains the domain name where the static content is going to be served. It will also be probably the same as the WEB_DOMAIN_NAME but in the future for better performce this should change
    //Static content can be for example CSS files, images etc.
    public static String STATIC_CONTENT_DOMAIN_NAME = null;

    //1. context (in this case "metallium")
    //2. (word "img" because this is the name of the Servlettttt)
    //3. image file system relative path based on the user id
    //4. name of the image
    //... all these steps are initialized in the ApplicationInitializationListener
    public static String WEB_IMAGE_SERVLET_PATH = "/img"; //It contains the context to invoke the Image Servlet.
    public static String fileSystemSeparator = "/";  // this is the universal file separator, works for linux and windows.
    public static int MAX_NUMBER_ROWS_IN_SEARCH_NEWS = 9;
    public static int MAX_NUMBER_ROWS_IN_SEARCH_EVENTS = 9;
    public static int MAX_NUMBER_ROWS_IN_SEARCH_NEWS_COMMENTS = 20;
    public static int MAX_NUMBER_ROWS_IN_SEARCH_USER_WALL_COMMENTS = 20;
    public static int MAX_NUMBER_ROWS_IN_SEARCH_NEWS_CONTROL_ROOM = 20;
    public static int MAX_NUMBER_ROWS_IN_SEARCH_IMAGE_GALLERY_PICS = 120;
    public static int MAX_NUMBER_ROWS_IN_SEARCH_IMAGE_GALLERIES = 120;
    public static int MAX_NUMBER_ROWS_IN_SEARCH_PROFILE_FRIENDS_LIST = 8;
    public static int MAX_NUMBER_ROWS_IN_SEARCH_GENERAL = 30;
    public static int MAX_NUMBER_ROWS_IN_SEARCH_USER_NOTIFICATIONS = 50;
    public static int IMAGE_NEWS_MAX_WIDTH_SIZE = 500;
    public static int IMAGE_NEWS_THUMBNAIL_MAX_WIDTH_SIZE = 210;
    public static int IMAGE_NEWS_THUMBNAIL_MAX_HEIGHT_SIZE = 150;
    public static int IMAGE_EVENT_MAX_WIDTH_SIZE = 500;
    public static int IMAGE_EVENT_THUMBNAIL_MAX_WIDTH_SIZE = 210;
    public static int IMAGE_EVENT_THUMBNAIL_MAX_HEIGHT_SIZE = 150;
    public static int IMAGE_MAX_WIDTH_SIZE = 700;
    public static int IMAGE_THUMBNAIL_MAX_WIDTH_SIZE = 150;
    public static int IMAGE_PROFILE_THUMBNAIL_MAX_WIDTH_SIZE = 100; //profileS
    public static int IMAGE_PROFILE_MAX_WIDTH_SIZE = 280;           //profileM
    public static int IMAGE_MAX_UPLOAD_SIZE = 3000000;
    public static String NEWS_IMAGE_TEMP_DIR = "news/images/temp"; //Folder inside the News directoy where we leave uploaded temp images which will be deleted every once in a while
    public static String NEWS_IMAGE_DIR = "news/images/001/001";
    public static String EVENTS_IMAGE_TEMP_DIR = "events/images/temp"; //Folder inside the Events directoy where we leave uploaded temp images which will be deleted every once in a while
    public static String EVENTS_IMAGE_DIR = "events/images/001/001";
    public static String WALL_IMAGE_TEMP_DIR = "temp/images/wall";  


    public static String FILE_SYSTEM = "E://Desarrollo/Dunkelheit/FileSystem/";
    public static String LOCALE_FILE_NAME = "messages"; //This is the name of the file on faces-config "<resource-bundle>"
    public static String DEFAULT_LOCALE_LANGUAGE = "en"; //"es" "en"
    public final static Locale DEFAULT_LOCALE = new Locale(DEFAULT_LOCALE_LANGUAGE);
    public static Integer DEFAULT_NETWORK = 2; //1 = Colombia //2 = USA
    public static String DEFAULT_COUNTRY = "XX"; //XX = blank
    public static String DEFAULT_CITY = "Metal City";
    public static String DEFAULT_USER_PROFILE_PIC = "users/common/profileS.jpg";
    public static String DEFAULT_USER_PROFILE_AVATAR_PIC = "users/common/profileM.jpg";
    public static String DEFAULT_IMAGE_GALLERY_COVER = "users/common/photogallery.jpg";
    public static String EMAIL_SUPPORT = "admin@metalcity.net";
    //Comments
    //This means that
    //I can write 'MAX_NUMBER_COMMENTS_IN_TIME' comments in 'COMMENTS_TIME_VALIDATION_IN_MINUTES' minutes
    //I can write '8' coments in '2' minutes, that is the restriction.
    public static int MAX_NUMBER_COMMENTS_IN_TIME = 10;
    public static int COMMENTS_TIME_VALIDATION_IN_MINUTES = 2;
    public static int CALM_DOWN_EXCEPTION_LIMIT = 5; //After this number of alerts and the guy doesnt quit, I ban him for a while
    //Notifications
    public static int NOTIFICATION_SEARCH_SINCE_LAST_X_DAYS = 8; //I will only retrieve from the DB notifications that are between the current date and this date.
    //Events
    public static int EVENT_SEARCH_NEXT_X_DAYS = 8; //I will only retrieve from the DB events that are between the current date and this date.
    //Timers
    public static int TIMER_MEMORY_STATS_INTERVAL = 3; //This is the interval in minutes.

 
    //web stuff
    public static String FAVICON_URL = "resources/favicon.png"; //When the application starts up this is going to become --> STATIC_CONTENT_DOMAIN_NAME + WEB_IMAGE_SERVLET_PATH + FAVICON_URL

}
