/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public interface Constants {

    public static final String FLEX_GUARD_URI = "/vshow";

    public static final String VSHOW_ASSETS_PATH = "event";

    public static final String MGR_APP_CONTEXT = "appContext";

    // Formats
    public static final DateFormat DATE_ONLY_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    public static final DateFormat TIME_ONLY_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public static final DateFormat UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");

    public static final String DEFAULT_ENCODING = "UTF-8";

    //Report Constants
    public static final String MAX_SIM_ATTENDEES = "maxSimAttendees";

    public static final String AVERAGE_VISIT = "averageVisitDuration";
    // Default TimeZone and TimeZone list.
    // NOTE: Should this be set here, or in the client?

    public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getDefault();

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    //report code type
    public static final String SHOW_DASHBOARD = "SHOW_DASHBOARD";

    public static final String SPONSOR_DASHBOARD = "SPONSOR_DASHBOARD";

    // request parameters and/or session keys
    public static final String BC_DOWNLOADED_ITEMS = "downloadedItems";

    public static final String LOCATION_CODE = "locationCode";

    public static final String LOCATION_ID = "locationId";

    public static final String LOCATION_SUB_ID = "locationSubId";

    public static final String CHAT_RECEIVER_ID = "chatReceiverId";

    public static final String EVENT_ID = "eventId";

    public static final String USER_ROLE_ID = "userRoleId";

    public static final String EVENT_SESSION_ID = "eventSessionId";

    public static final String KEY = "key";

    public static final String COMMAND = "command";

    public static final String ACTION = "action";

    public static final String ID = "id";

    public static final String FG = "fg";

    public static final String BG = "bg";

    public static final String TRADESHOW_ID = "tradeshowId";

    public static final String TRADESHOW_SUMMARY = "summary";

    public static final String TRADESHOW_ORGANIZER = "organizer";

    public static final String SHOW_ID = "showId";

    public static final String TRADESHOW_TITLE = "title";

    public static final String APP_TITLE = "appTitle";

    public static final String SPONSOR_TITLE = "sponTitle";

    public static final String CURRENT_TRADESHOW_ID = "currentTradeshowId";

    public static final String RESOURCE_ID = "resourceId";

    public static final String FORUM_ID = "forumId";

    public static final String TOPIC_ID = "topicId";

    public static final String POST_ID = "postId";

    public static final String BOOTH_ID = "boothId";

    public static final String SPONSOR_ID = "sponsorId";

    public static final String PRESENTATION_ID = "presentationId";

    public static final String COLOR = "color";

    public static final String NAME = "name";

    public static final String TRADESHOW_CODE = "tsc";

    public static final String BOOTH_CODE = "bc";

    public static final Long BOOTH_VIEW_ID = 40L;

    public static final String XML = "xml";

    public static final String JSON = "json";

    public static final String FILE = "file";

    public static final String FORUM_COMMAND = "forum";

    public static final String POST_COMMAND = "post";

    public static final String TOPIC_COMMAND = "topic";

    public static final String USER_ID = "userId";

    public static final String AUTH_TOKEN = "auth_";

    public static final String AUTH_PARAM = "auth";

    public static final String CURRENT_USER_ID = "currentUserId";

    public static final String ADMIN_ID = "adminId";

    public static final String FOR_ADMIN_ID = "forAdminId";

    public static final String VSHOW_ADMIN_ID = "vshowAdminId";

    public static final String LOCATION = "location";

    public static final String MEDIA_METRIC_SESSION_ID = "mediaMetricSessionId";

    public static final String EMAIL_ID = "emailId";

    public static final String VTS_CLIENT_CODE = "code";

    public static final String NICKNAME = "nickname";

    public static final String CHAT_SESSION_ID = "chatSessionId";

    public static final String GIVEAWAY_ID = "giveawayId";

    public static final String SURVEY_ID = "surveyId";

    public static final String APP_PROPERTIES = "appProperties";

    public static final String SOURCE_VIEW_CODE = "sourceViewCode";

    public static final String ERROR_MESSAGE = "errorMessage";

    public static final String ERROR_CODE = "errorCode";

    public static final Long DEFAULT_WEBCAST_PRIZE_ACTIVITY_ID = 3L;

    public static final Long DEFAULT_DOCUMENT_PRIZE_ACTIVITY_ID = 4L;

    public static final Long DEFAULT_LOCATION_VISITED_ACTIVITY_ID = 5L;

    public static final Long DEFAULT_BOOTH_VISITED_ACTIVITY_ID = 2L;

    public static final Long DEFAULT_SCHEDULED_CHAT_ACTIVITY_ID = 6L;

    public static final Long DEFAULT_LOCATION_CHAT_ACTIVITY_ID = 7L;

    public static final String PRIZE_ID = "prizeId";

    public static final String PRIZE_ACTIVITY_ID = "prizeActivityId";

    public static final String PRIZE_DRAWING_ID = "prizeDrawingId";

    public static final String PRIZE_LEVEL_ID = "prizeLevelId";

    public static final String PASSWORD = "password";

    public static final String PREVIEW_PASSWORD = "previewPassword";

    public static final String OLD_PASSWORD = "oldPassword";

    public static final String NEW_PASSWORD = "newPassword";

    public static final String EMAIL = "email";

    public static final String OLD_EMAIL = "oldEmail";

    public static final String NEW_EMAIL = "newEmail";

    public static final String ASSET_ID = "assetId";

    public static final String USER_NAME = "userName";

    public static final String MODULE = "module";

    public static final String CLIENT_ID = "clientId";

    public static final String FORMAT = "format";

    public static final String RESPONSE_FORMAT = "f";

    public static final String POST_URL = "postUrl";

    public static final String NEXT_COMMAND = "nextCommand";

    public static final String AS_ADMIN = "asadmin";

    public static final String QUESTION_ID = "questionId";

    public static final String ANSWER_CODE = "answerCode";

    public static final String MEDIA_URL_ID = "mediaUrlId";

    public static final String NUM_STARS = "numStars";

    public static final String ID_OPTION_INPUT = "idOptionInput";

    public static final String CONTENT_ID = "contentId"; // CONTENT_HIT_TRACK_DETAILS.CONTENT_NAME 

    public static final String ACTION_TYPE = "actionType"; // CONTENT_HIT_TRACK_DETAILS.ACTION and used for Hotspot

    public static final String HIT_DETAILS = "details"; // CONTENT_HIT_TRACK_DETAILS.details and used for widgets

    public static final String CONTENT_TYPE = "contentType"; // CONTENT_HIT_TRACK_DETAILS.LOCATION

    public static final String BOOTH_SESSION_ID = "boothSessionId"; // CONTENT_HIT_TRACK_DETAILS.MEDIA_URL

    // used for Hotspot Types like polls,surveys,resources etc
    public static final String HOTSPOT_TYPE = "hotspotType";

    public static final String LOGIN = "login";

    public static final String TARGET = "target";

    public static final String DISPLAY_TYPE_CODE = "profileType";

    public static final String SIMPLE_DISPLAY_ELEMENT_ID = "displayElementId";

    public static final String TIER_ID = "tierId";

    public static final String MARQUEE_ID = "marqueeId";

    public static final String MARQUEE_SET_ID = "marqueeSetId";

    public static final String MARQUEE_MESSAGE_ID = "marqueeMessageId";

    public static final String BOOTH_TEMPLATE_ID = "boothTemplateId";

    // Codes
    public static final String VTS_CODE = "vts";

    public static final String EVENT_COMMENT_CODE = "eventcomment";

    public static final String PROPERTY = "property";

    // XML variable keys
    public static final String EVENT_CATEGORY_CODE_ID = "eventCategoryCodeId";

    // Misc
    public static final String YES = "YES";

    public static final Long DEFAULT_SESSION_ID = 1L;

    public static final String RICH_TEXT_PREFIX = "vts.tradeshow.richtext.";

    public static final String DEFAULT_VALIDATION_TYPE_CODE = "none";

    //XMPP
    public static final String XMPP_PRESENCE_TYPE_AVAILABLE = "available";

    public static final String XMPP_PRESENCE_TYPE_UNAVAILABLE = "unavailable";

    public static final String USER_LOCATION_BOOTH = "booth";

    public static final String USER_LOCATION_AUDITORIUM = "auditorium";

    public static final String USER_LOCATION_HOME = "home";

    public static final String USER_LOCATION_EXHIBITS = "exhibits";

    public static final String USER_LOCATION_RESOURCECENTER = "resourceCenter";

    public static final String USER_LOCATION_SPONSORHALL = "sponsorHall";

    public static final String USER_LOCATION_COMMCENTER = "communicationsCenter";

    public static final String LOCATION_LOGOUT = "logout";

    public static final String LOCATION_LOGIN = "login";

    public static final String LOCATION_ALL = "all";

    public static final String CONTENT_TRACKING_ACTION_PRESENCE = "presence";

    public static final String CONTENT_TRACKING_HIT_DETAILS_CONNECTION_TYPE = "xmpp_conn_type";

    public static final String WEBLOGIC_LOGIN_DONE = "weblogic-login-done";

    public static final String XMPP_SOCKET_CONNECTION_TYPE = "SOCKET";

    public static final String XMPP_BOSH_CONNECTION_TYPE = "BOSH";

    public static final String FILE_SEPARATOR = System.getProperty("file.separator", "/");

    public static final String TSC_BAD_OR_MISSING = "missing or invalid tradeshow code";

    public static final String RESOURCE_CATEGORY_ID = "resourceCategoryId";

    public static final String RESOURCE_TARCK_ID = "trackId";

    public static final String RESOURCE_TYPE = "resourceType";

    public static final String ORIGINAL_RELATIVE_URI = "originalRelativeUri";

    public static final String BOOTH_SESSION_NAME_PREFIX = "booth_";

    public static final String XMPP_SERVER_HOST_PROPERTY = "xmpp.server.host";

    public static final String XMPP_SERVER_DOMAIN_PROPERTY = "xmpp.server.domain";

    public static final String XMPP_RPC_PORT_PROPERTY = "xmpp.rpc.port";

    public static final String XMPP_RPC_TIMEOUT = "xmpp.rpc.timeout";

    // HOTSPOT TYPES
    public static final String HOTSPOT_CD = "hotspot";

    public static final String HOTSPOT_TYPE_TEXT = "text";

    public static final String HOTSPOT_TYPE_IMAGE = "image";

    public static final String HOTSPOT_TYPE_ANIMATION = "animation";

    public static final String HOTSPOT_TYPE_HOTSPOTS = "hotspots";

    public static final String HOTSPOT_TYPE_TEXTBTN = "textBtn";

    //BRIEFCASE
    public static final String PARAMETER_EMAIL = "email";

    public static final String PARAMETER_BC_ITEM_TYPES_FOR_USER = "bcItemTypesForUser";

    public static final String PARAMETER_BC_ITEM_TYPES_ALL = "bcItemTypes"; //briefcase item types

    public static final String PARAMETER_USER_INFO_MAP = "userInfoMap";

    public static final String XSLT_PARAMETER_BC_ITEM_NAME = "bcItem";

    public static final String XSLT_PARAMETER_BC_ITEM_BOOTHS = "bcItem_booths";

    public static final String XSLT_PARAMETER_BC_ITEM_WEBCASTS = "bcItem_webcasts";

    public static final String XSLT_PARAMETER_BC_ITEM_CHATS = "bcItem_chats";

    public static final String XSLT_PARAMETER_BC_ITEM_DOCUMENTS = "bcItem_documents";

    public static final String XSLT_PARAMETER_BC_ITEM_GIVEAWAYS = "bcItem_giveaways";

    public static final String XSLT_PARAMETER_BC_ITEM_CONTACTS = "bcItem_contacts";

    public static final String COPYRIGHT_I18N_KEY = "vshow.general.text.copyright";

    public static final String BRIEFCASE_ITEM_TYPE_RESOURCE_DOCUMENT = "DOCUMENT";

    public static final String BRIEFCASE_ITEM_TYPE_RESOURCE_WEBCAST = "WEBCAST";

    // Track Types
    public static final String TRACK_TYPE_NAVIGATION = "navigation";

    public static final String TRACK_TYPE_HOTSPOT = "hotspot";

    //Media catagory and url code
    public static final String MEDIA_URL_CD_NAVIGATION = "navigation";

    public static final String MEDIA_CATEGORY_CD_NAVIGATION = "image";

    // Navigation Types
    public static final String NAVIGATION_TYPE_SUBNAV = "subnavtext";

    public static final String NAVIGATION_TYPE_TOPNAV = "topnavtext";

    public static final String NAVIGATION_TYPE_IMAGE = "image";

    public static final String NAVIGATION_FEATURE_CHAT_NOTIFY = "chatnotify";

    public static final String NAVIGATION_FEATURE_MESSAGE_NOTIFY = "messagenotify";

    public static final String NAVIGATION_TYPE_TEXT = "text";

    public static final String NAVIGATION_ID = "navigationId";

    // Admin Constants
    public static final String DISPLAY_PROFILE_ID = "displayProfileId";

    // Media Url stuff
    public static final String IMAGES_CODE = "images";

    public static final String HOTSPOT_MEDIA_URL_CD = "hotspot";

    public static final String DOCUMENTS_MEDIA_URL_CD = "documents";

    public static final String LOCATION_BG_MEDIA_CATEGORY_CD = "locationBG";

    public static final String BG_IMAGE_CD = "backgroundImage";

    public static final String LOBBY_BG_IMAGE_CD = "lobbyBGImage";

    public static final String LOCATION_SECOND_BG_MEDIA_CATEGORY_CD = "locationSecondBG";

    public static final String RESOURCE_MEDIA_CATEGORY_CD = "resource";

    public static final String LOGO_CATEGORY = "exhibitHallLogo";

    public static final String EXHIBIT_HALL_CATEGORY = "exhibitHallBackground";

    public static final String BOOTH_CATEGORY = "boothBackground";

    public static final String SURVEY_COMMENT = "<!--##survey##-->";

    public static final String POLL_CD = "poll";

    // Prize Activities
    public static final String ACTIVITY_VCARD_EXCHANGE = "VcardExchanged";

    public static final String ACTIVITY_BOOTH_VISITED = "BoothVisited";

    public static final String ACTIVITY_WEBCAST_VIEWED = "WebcastViewed";

    public static final String ACTIVITY_DOCUMENT_OPENED = "DocumentOpened";

    public static final String ACTIVITY_LOCATION_VISITED = "LocationVisited";

    public static final String ACTIVITY_SCHEDULE_CHAT_ATTENDED = "ScheduledChatAttended";

    public static final String ACTIVITY_LOCATION_CHAT_ATTENDED = "LocationChatAttended";

    public static String ACTIVITY_PRIVATE_CHAT_ATTENDED = "PrivateChatAttended";

    //Prize Activity Messages
    public static final String PRIZE_ACTIVITY_NOT_AVAILABLE = "prize activity not available with same configuration";

    public static final String SAME_PRIZE_ACTIVITY = "prize activity configuration not changed";

    public static final String DUPLICATE_PRIZE_ACTIVITY = "prize activity already exists";

    // reporting activitites
    public static final String ACTIVITY_HOTSPOT = "Hotspot";

    public static final String ACTIVITY_NAVIGATION = "Navigation";

    public static final String ACTIVITY_LOGIN = "Login";

    public static final String ACTIVITY_LOGOUT = "Logout";

    public static final Long DEFAULT_EVENT_SESSION_ID = 1L;

    //reporting parameters
    public static final String SPONSOR_COLUMNS_GROUP_NAME = "sponsorOrContentVisited";

    public static final String PRIZE_NAMES = "availablePrizes";

    public static final String REPORT_CODE_ATTENDEES = "ATTENDEES_REPORT";

    public static final String PRIZE_CENTER_REPORT_CODE = "PRIZE_CENTER_REPORT";

    public static final String SPONSOR_ATTENDEES_REPORT_CODE = "SPONSOR_ATTENDEES_REPORT";

    // ts_views
    public static final String TS_VIEW_TYPE_LOCATION = "location";

    //XSLT PARAMETERS
    public static final String XSLT_PARAM_SELECTED_COLUMNS = "selected-columns";

    public static final String XSLT_PARAM_TRADESHOW_NAME = "tradeshow-name";

    public static final String XSLT_PARAM_USER_NAME = "user-name";

    public static final String XSLT_PARAM_EMAIL_BODY_FORMAT = "email-body-format";

    public static String XSLT_PARAM_REQUESTING_USERNAME = "requesting-username";

    public static String XSLT_PARAM_OTHER_USERNAME = "other-username";

    public static String XSLT_PARAM_TIMEZONE = "timezone";

    public static String XSLT_PARAM_REQUESTED_ITEMS = "requested-items";

    public static String XSLT_PARAM_DATE_FORMAT = "date-format";

    public static String XSLT_PARAM_SHORTFORM_TIMEZONE = "shortform-timezone";

    public static String XSLT_PARAM_COMPLETE_LOGS = "are-complete-logs";

    public static String XSLT_PARAM_MESSAGE_COUNT = "message-count";

    public static String XSLT_PARAM_CURRENT_TRADESHOW_ID = "current-tradeshow-id";

    public static String XSLT_PARAM_CURRENT_USER_ID = "current-user-id";

    public static String XSLT_PARAM_TRADESHOW_CODE = "tradeshow-code";

    public static String XSLT_PARAM_IS_VBC = "is-vbc";

    public static String XSLT_PARAM_TRADESHOW_DESCRIPTION = "tradeshow-desc";

    public static String XSLT_PARAM_STANDALONE_PAGE = "standalone-page";

    public static String XSLT_PARAM_TS_LIVE_START = "live-start-date";

    public static String XSLT_PARAM_TS_LIVE_END = "live-end-date";

    public static String XSLT_PARAM_TS_ARCHIVE_START = "archive-start-date";

    public static String XSLT_PARAM_TS_ARCHIVE_END = "archive-end-date";

    public static String XSLT_PARAM_LOCALE = "locale";

    // Admin Profile related Constants
    public static final String CONTACT_PROFILE_NAME = "Contact - No Login";

    public static final Long CONTACT_PROFILE_ID = new Long(16);

    public static final String REG_REQUIRED = "regRequired";

    public static final String REG_START_DATE_PARAM = "regStartDate";

    public static final String REG_END_DATE_PARAM = "regEndDate";

    public static final String LOCATION_VIEW_START_DATE_PARAM = "locationViewStartDate";

    public static final String LOCATION_VIEW_END_DATE_PARAM = "locationViewEndDate";

    public static final String START_DATE_PARAM = "showViewStartDate";

    public static final String END_DATE_PARAM = "showViewEndDate";

    public static final String INCLUDE_PROFILE_FIELDS_PARAM = "showprofilefields";

    public static final String LOCATION_PARAM = "location";

    public static final String DISPLAY_ELEMENT_PASSWORD_FIELD_NAME = "password";

    public static final String DISPLAY_ELEMENT_PARTNERREF_FIELD_NAME = "partnerref";

    public static final String DISPLAY_ELEMENT_PARTNERREF_FIELD_TITLE = "Partnerref";

    //content hit logging related constants
    public static final String CONTENT_HIT_LOCATION_LOBBY = "lobby";

    public static final String CONTENT_HIT_ACTION_CLICK = "click";

    public static final String CONTENT_HIT_CONTENT_NAME_RESOURCE = "resource";

    public static final String VSHOW_APP_CONTEXT = "/vshow/";

    public static final String HTTP_PROTOCOL = "http://";

    public static final String HTTPS_PROTOCOL = "https://";

    public static final String RICH_TEXT_CODE = ".richtext.";

    public static final String VSHOW_MANAGER_CODE = "manager";

    public static final String VS_MANAGER_CODE = "mgr";

    public static final String BOOTH_MANAGER_CODE = "bm";

    public static final String VBC_MANAGER_CODE = "vbcm";

    public static final String VSHOW_SPONSOR_REPORT = "sponsorreport";

    public static final String VSHOW_SPONSOR_ADMIN = "sponsor";

    public static final String REPORT_CODE_PARAM = "reportCode";

    public static final String REGISTRATION_URL_PART = "registration";

    public static final String LOBBY_URL_PART = "lobby";

    public static final String SHOW_MODE_PARAM = "mode";

    public static final String SHOW_MODE_REGISTER = "register";

    public static final String SHOW_MODE_LOBBY = "lobby";

    public static final String SHOW_MODE_SHOW = "show"; // awesome name han !!  - JJ

    public static final String REG_PAGE_ID_PARAM = "regPageId";

    public static final String WEB_FOLDER = "web";

    public static final String API_CONTEXT = "api";

    public static final String API_KEY = "apiKey";

    public static final String JUST_REGISTERED_USER = "justRegisteredUser";

    // user info property constants
    public static final String USER_PROPERTY_NOT_VISIBLE_TO_ATTENDEES = "vts.notVisibleToTsAttendees";

    public static final String USER_PROPERTY_NOT_VISIBLE_TO_STAFF = "vts.notVisibleToTsStaff";

    public static final String USER_PROPERTY_OPT_OUT_OF_PRIZES = "vts.optOutOfPrizes";

    public static final String USER_PROPERTY_HIDE_PROFILE_INFO = "vts.hideProfileInfo";

    public static String RESOURCE_CATEGORY_ROOT = "ROOT";

    // Replacement tokens
    public static final String SHOW_CODE_TOKEN = "#SHOW_CODE#";

    public static final String VSHOW_DOMAIN_TOKEN = "#VSHOW_DOMAIN#";

    public static final String EVENT_DOMAIN_TOKEN = "#EVENT_DOMAIN#";

    public static final String STANDARD_REG_PAGE_CODE = "#STANDARD#";

    public static final String DEFAULT_PARTNERREF_CODE = "-";

    public static final String VSHOW_CHAT_PAGE_CODE = "vshow.chat";

    public static final String VSHOW_GENERAL_PAGE_CODE = "vshow.general";

    public static final String HOTSPOT_ID = "hotspotId";

    public static final String ACTION_ID = "actionId";

    public static final String MAX_IN_CLAUSE_SIZE = "query.max.inclause";

    public static final String AUTH_KEY_PARAM = "key";

    public static final String BRIEFCASE_DOWNLOAD_COMMAND = "downloadbc";

    public static final String PURGE_DATE = "purgeDate";

    public static final String SPONSOR_CATEGORY_ID = "sponsorCategoryId";

    public static final String DEFAULT_AVATAR_URL = "/thisDoesNotExist.jpg";

    public static final String DELETED_AVATAR_URL = "D";

    public static final String DELETED_RESUME_URL = "D";

    public static final String REALTIME_MESSAGE = "msg";

    public static final String VIEW_BOOTH = "booth";

    public static final String VIEW_REG_PAGE = "registration";

    public static final String VIEW_LOBBY_PAGE = "regLobby";

    public static final String REFERER_CHECK_COOKIE = "re";

    public static final String LOCALE_PREFERENCE_COOKIE = "locale";

    public static final String LOCATION_COOKIE = "location";

    public static final String VSHOW_TEST_COOKIE_NAME = "vshow-test-cookie";

    public static final String VSHOW_TYS_STATUS_COOKIE = "vshow-tys-status";

    public static final String TYS_COOKIE_VALUE_SUCCESS = "success";

    public static final String I18N_DEFAULT_LONG_DATE = "EEE, MM/dd/yyyy h:mm a";

    public static final String MODE = "mode";

    //email constants
    public static final String EMAIL_SUBJECT_BOOTH_MANAGER = "Virtual Show Booth Manager: Password Request";

    public static final String EMAIL_REGARDS_BOOTH_MANAGER = "Virtual Show Booth Manager Support";

    public static final String EMAIL_SUBJECT_VBC = "Virtual Briefing Center Manager: Password Request";

    public static final String EMAIL_REGARDS_VBC = "Virtual Briefing Center Manager Support";

    //VBC - Booth Manager titles
    public static final String VBC_TITLE = "Virtual Briefing Center Manager";

    public static final String BOOTH_MANAGER_TITLE = "Virtual Show Booth Manager";

    // request attributes
    public static final String TRADESHOW_REQ_ATTRIBUTE = "tradeshow";

    //registration fields
    public static final String REG_FIELD_BROWSER = "browser";

    public static final String REG_FIELD_OS = "operatingSystem";

    public static final String REG_FIELD_REMOTE_IP = "eventUserIp";

    //email notifications
    public static final String NOTIFICATION_ID = "notificationId";

    public static final String REG_PAGE_ID = "regPageId";

    public static final String LOBBY_ID = "lobbyId";

    //confirmation email notification    
    public static final String DEST_SHOW_ID = "destShowId";

    public static final String DEST_SPONSOR_ID = "destSponsorId";

    public static final String IN_USE_ONLY_FILTER = "inUseOnly";

    public static final int TEN_MB = 10 * 1024 * 1024;

    public static final Long VTS_MASTER_CLIENT_ID = 900L;

    public static final String CATEGORY = "cat";

    public static final String VHSOW_VIEW_COOKIE = "vshow-view";

    public static final String VIEW_PARAMAETER = "v";

    public static final String JDBC_FETCH_SIZE = "vshow.jdbc.fetchsize";

    public static final String CRUNCH_TASK_TYPE = "taskType";

    public static final String AUTO_GENERATE_PASSWORD = "##Auto_Generate_Password##";

    public static final String SEAMLESS_REG_PARAM_PREFIX = "setting_"; //This is the prefix that all parameters have to start with to distinguish them as Seamless registration parameters. VSHOW-3084

    public static final String PREVIEW_MODE = "previewmode";
    
    public static final String TRADESHOW_RESOURCES = "tradeshowResources";
    
    public static final String TRADESHOW_DEFAULT_VIEW_BACKGROUND = "tsDefaultBackground";

}
