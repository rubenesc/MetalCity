/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.util;

import co.com.metallium.core.constants.BirthDayDisplayConst;
import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.SessionConst;
import co.com.metallium.core.constants.state.BanTime;
import co.com.metallium.core.constants.state.GalleryState;
import co.com.metallium.core.constants.state.NewsState;
import co.com.metallium.core.constants.state.UserState;
import co.com.metallium.core.entity.Country;
import co.com.metallium.core.entity.ForumCategory;
import co.com.metallium.core.entity.Network;
import co.com.metallium.core.entity.Profile;
import co.com.metallium.core.entity.ReservedNames;
import co.com.metallium.core.entity.iplocation.LocationDTO;
import co.com.metallium.core.service.IpLocationService;
import com.metallium.utils.dto.PairDTO;
import com.metallium.utils.framework.jsf.FacesUtils;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.faces.model.SelectItem;

/**
 * 20101102
 * @author Ruben
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN) //http://www.adam-bien.com/roller/abien/entry/singleton_the_perfect_cache_facade
public class ApplicationParameters {

    @EJB
    private IpLocationService ipLocationService;
    //Ips
    private static HashMap<String, String> unauthorizedIPs = new HashMap<String, String>(); //
    //Profile
    private static HashMap<String, String> countryMap = new HashMap<String, String>(); //If someone has CO it will return Colombia.
    private static ArrayList<SelectItem> selectItemCountry = new ArrayList<SelectItem>(); //To show anytime someone need the country list in a web page
    private static HashMap<String, String> profileMap = new HashMap<String, String>(); //Gives me the name of a certain profile
    private static ArrayList<SelectItem> selectItemProfile = new ArrayList<SelectItem>(); //To show anytime someone need the country list in a web page
    private static HashMap<Integer, Network> networkMap = new HashMap<Integer, Network>();
    private static ArrayList<SelectItem> selectItemNetwork = new ArrayList<SelectItem>();
    private static HashMap<String, String> sexMap = new HashMap<String, String>();
    private static ArrayList<PairDTO> selectItemSex = new ArrayList<PairDTO>();
    private static HashMap<Integer, String> birthdayDisplayMap = new HashMap<Integer, String>();
    private static ArrayList<PairDTO> selectItemBirthdayDisplay = new ArrayList<PairDTO>();
    //Image Gallery
    private static HashMap<String, String> visibilityMap = new HashMap<String, String>();
    private static ArrayList<PairDTO> selectItemVisibility = new ArrayList<PairDTO>();
    
    //Forum
    private static List<ForumCategory> forumCategoryList = new ArrayList<ForumCategory>();
    private static HashMap<Short, ForumCategory> forumCategoryMap = new HashMap<Short, ForumCategory>();
    private static ArrayList<PairDTO> selectItemForumCategory = new ArrayList<PairDTO>();
    
    //States for News and Users
    private static HashMap<String, String> newsStateMap = new HashMap<String, String>();
    private static ArrayList<SelectItem> selectItemNewsStateFull = new ArrayList<SelectItem>();
    private static ArrayList<SelectItem> selectItemNewsStateLimited = new ArrayList<SelectItem>();
    private static HashMap<String, String> userStateMap = new HashMap<String, String>();
    private static ArrayList<SelectItem> selectItemUserState = new ArrayList<SelectItem>();
    private static ArrayList<PairDTO> selectItemMonths = new ArrayList<PairDTO>();
    private static ArrayList<SelectItem> selectItemYears = new ArrayList<SelectItem>();
    private static ArrayList<SelectItem> selectItemDays = new ArrayList<SelectItem>();
    //User ban time
    private static HashMap<String, String> banTimeMap = new HashMap<String, String>();
    private static ArrayList<PairDTO> selectItemBanTime = new ArrayList<PairDTO>();
    //Locale
    private static HashMap<String, String> localeMap = new HashMap<String, String>();
    private static ArrayList<PairDTO> selectItemLocale = new ArrayList<PairDTO>();
    //Reserved usernames
    private static HashMap<String, String> reservedNamesMap = new HashMap<String, String>();

    //=================Resource Bundle=========================================//
    //==============Unauthorized Ips==================================================//
    public static boolean isIpUnauthorized(String ip) {
        boolean answer = false;

        try {
            if (ip != null && ip.trim().length() > 0) {
                if (unauthorizedIPs != null && !unauthorizedIPs.isEmpty()) {
                    answer = unauthorizedIPs.containsKey(ip);
                }
            }
        } catch (Exception e) {
            LogHelper.makeLog(e);
        }

        return answer;
    }

    public static synchronized void initializeUnauthorizedIPsList(HashMap<String, String> ipsAutorizadas) {
//        for (Iterator<Country> it = ipsAutorizadas.iterator(); it.hasNext();) {
//            Country country = it.next();
//            countryMap.put(country.getIso(), country.getPrintableName());
//            selectItemCountry.add(new SelectItem(country.getIso(), country.getPrintableName()));
//        }
    }

    //==============Country==================================================//
    public static String getCountry(String iso) {

        if (iso == null) {
            return null;
        }

        try {
            return countryMap.get(iso);
        } catch (Exception ex) {
            LogHelper.makeLog(ex.getMessage(), ex);
        }

        return null;
    }

    public static ArrayList<SelectItem> getSelectItemCountry() {
        return selectItemCountry;
    }

    public static synchronized void initializeCountryList(List<Country> countryList) {
        for (Iterator<Country> it = countryList.iterator(); it.hasNext();) {
            Country country = it.next();
            countryMap.put(country.getIso(), country.getPrintableName());
            selectItemCountry.add(new SelectItem(country.getIso(), country.getPrintableName()));
        }
    }

    //==============Profile==================================================//
    public static String getProfile(Integer id) {

        if (id == null) {
            return null;
        }

        try {
            return profileMap.get(id.toString());
        } catch (Exception ex) {
            LogHelper.makeLog(ex.getMessage(), ex);
        }

        return null;
    }

    public static ArrayList<SelectItem> getSelectItemProfile() {
        return selectItemProfile;
    }

    public static synchronized void initializeProfileList(List<Profile> list) {
        for (Iterator<Profile> it = list.iterator(); it.hasNext();) {
            Profile entity = it.next();
            profileMap.put(entity.getId().toString(), entity.getAlias());
            selectItemProfile.add(new SelectItem(entity.getId().toString(), entity.getAlias()));
        }
    }

    //==============Sex==================================================//
    public String getSex(String id) {
        if (id == null) {
            return null;
        }

        try {
            return getResourceBundle().getString(sexMap.get(id));
        } catch (Exception ex) {
            LogHelper.makeLog(ex.getMessage(), ex);
        }

        return null;
    }

    public static ArrayList<PairDTO> getSelectItemSex() {
        return selectItemSex;
    }

    public static synchronized void initializeSexList() {

        selectItemSex = new ArrayList<PairDTO>();
        selectItemSex.add(new PairDTO("", "commun_select_sex"));

        selectItemSex.add(new PairDTO("M", "commun_male"));
        sexMap.put("M", "commun_male");

        selectItemSex.add(new PairDTO("F", "commun_female"));
        sexMap.put("F", "commun_female");

    }

    //==============Birthday Display=================================================//
    public String getBirthdayDisplay(Integer id) {
        if (id == null) {
            return null;
        }

        try {
            return getResourceBundle().getString(birthdayDisplayMap.get(id));
        } catch (Exception ex) {
            LogHelper.makeLog(ex.getMessage(), ex);
        }

        return null;
    }

    public static ArrayList<PairDTO> getSelectItemBirthdayDisplay() {
        return selectItemBirthdayDisplay;
    }

    public static synchronized void initializeBirthdayDisplayList() {

        selectItemBirthdayDisplay = new ArrayList<PairDTO>();

        selectItemBirthdayDisplay.add(new PairDTO(BirthDayDisplayConst.DISPLAY_COMPLETE, "common_info_birthday_displpay_complete"));
        birthdayDisplayMap.put(BirthDayDisplayConst.DISPLAY_COMPLETE, "common_info_birthday_displpay_complete");

        selectItemBirthdayDisplay.add(new PairDTO(BirthDayDisplayConst.DISPLAY_MONTH_DAY, "common_info_birthday_displpay_month_day"));
        birthdayDisplayMap.put(BirthDayDisplayConst.DISPLAY_MONTH_DAY, "common_info_birthday_displpay_month_day");

        selectItemBirthdayDisplay.add(new PairDTO(BirthDayDisplayConst.DISPLAY_NONE, "common_info_birthday_displpay_none"));
        birthdayDisplayMap.put(BirthDayDisplayConst.DISPLAY_NONE, "common_info_birthday_displpay_none");

    }

    //==============Locale==================================================//
    public String getLocale(String id) {

        if (id == null) {
            return null;
        }

        try {
            return getResourceBundle().getString(localeMap.get(id));
        } catch (Exception ex) {
            LogHelper.makeLog(ex.getMessage(), ex);
        }

        return null;
    }

    public static ArrayList<PairDTO> getSelectItemLocale() {
        return selectItemLocale;
    }

    public static synchronized void initializeLocaleList() {

        selectItemLocale = new ArrayList<PairDTO>();
        selectItemLocale.add(new PairDTO("", "commun_select"));

        selectItemLocale.add(new PairDTO("en", "locale_english"));
        localeMap.put("en", "locale_english");

        selectItemLocale.add(new PairDTO("es", "locale_spanish"));
        localeMap.put("es", "locale_spanish");

    }

    //==============Gallery Visibility==============================================//
    public String getVisibility(String id) {

        if (id == null) {
            return null;
        }

        try {
            return getResourceBundle().getString(visibilityMap.get(id));
        } catch (Exception ex) {
            LogHelper.makeLog(ex.getMessage(), ex);
        }

        return null;
    }

    public static ArrayList<PairDTO> getSelectItemVisibility() {
        return selectItemVisibility;
    }

    public static synchronized void initializeVisibilityList() {

        selectItemVisibility = new ArrayList<PairDTO>();

        selectItemVisibility.add(new PairDTO(GalleryState.PUBLIC, "image_gallery_info_visibility_public"));
        visibilityMap.put(GalleryState.PUBLIC, "image_gallery_info_visibility_public");

        selectItemVisibility.add(new PairDTO(GalleryState.WITH_LINK, "image_gallery_info_visibility_with_link"));
        visibilityMap.put(GalleryState.WITH_LINK, "image_gallery_info_visibility_with_link");

        selectItemVisibility.add(new PairDTO(GalleryState.PRIVATE, "image_gallery_info_visibility_with_private"));
        visibilityMap.put(GalleryState.PRIVATE, "image_gallery_info_visibility_with_private");

    }

    //==============User Ban Time==============================================//
    public String getBanTime(String id) {

        if (id == null) {
            return null;
        }

        try {
            return getResourceBundle().getString(banTimeMap.get(id));
        } catch (Exception ex) {
            LogHelper.makeLog(ex.getMessage(), ex);
        }

        return null;
    }

    public static ArrayList<PairDTO> getSelectItemBanTime() {
        return selectItemBanTime;
    }

    public static synchronized void initializeBanTimeList() {

        selectItemBanTime = new ArrayList<PairDTO>();

        selectItemBanTime.add(new PairDTO(BanTime._5_MIN_, "ban_5_m"));
        banTimeMap.put(BanTime._5_MIN_, "ban_5_m");

        selectItemBanTime.add(new PairDTO(BanTime._30_MIN_, "ban_30_m"));
        banTimeMap.put(BanTime._30_MIN_, "ban_30_m");

        selectItemBanTime.add(new PairDTO(BanTime._1_HOUR_, "ban_1_h"));
        banTimeMap.put(BanTime._1_HOUR_, "ban_1_h");

        selectItemBanTime.add(new PairDTO(BanTime._1_DAY_, "ban_1_d"));
        banTimeMap.put(BanTime._1_DAY_, "ban_1_d");

        selectItemBanTime.add(new PairDTO(BanTime._INDEFINITELY_, "ban_indefinitely"));
        banTimeMap.put(BanTime._INDEFINITELY_, "ban_indefinitely");

    }

//==============Resource Bundle Messages==================================================//
    /**
     * Permite obtener un mensaje especifico del cache distribuido de parametros
     * @param constantesParametro clave(Key) con el cual se quiere obtener el parametro
     * @param formato que al que se va ha ajustar el mensaje que se va a buscar
     * @return el valor que se buscaba o null si este no fue encontrado u ocurrio un EHCACHE ERROR en la busqueda
     */
    public String getResourceBundleMessage(String msg, Object... parameters) {
        String answer = null;
        try {
            String message = getResourceBundle().getString(msg);
            answer = String.format(message, parameters);
        } catch (Exception ex) {
            LogHelper.makeLog(ex.getMessage(), ex);
        }
        return answer;
    }

    public String getResourceBundleMessage(String msg) {
        String answer = null;
        try {
            answer = getResourceBundle().getString(msg);
        } catch (Exception ex) {
            LogHelper.makeLog(ex.getMessage(), ex);
        }
        return answer;

    }

    /**
     *
     * So the difference is that this method takes 'message' and 'field'
     * both from the resource bundle, and with these two creates a new string.
     *
     * I created it for cases like:
     *
     * validation_input_too_long=The %1$s, is too long.
     * forum_field_content=Content
     *
     *
     * @param message = 'validation_input_too_long'
     * @param field = 'forum_field_content'
     * @return = 'The Content, is too long.'
     */
    public String getResourceBundleValidationMessage(String message, String field) {
        String answer = null;
        try {
            message = getResourceBundle().getString(message);
            field = getResourceBundle().getString(field);
            answer = String.format(message, field);
        } catch (Exception ex) {
            LogHelper.makeLog(ex.getMessage(), ex);
        }
        return answer;
    }

    public ResourceBundle getResourceBundle() {

        String baseName = co.com.metallium.core.constants.MetConfiguration.LOCALE_FILE_NAME;
        ResourceBundle resourceBundle = null;
        if (resourceBundle == null) {
            try {
                resourceBundle = ResourceBundle.getBundle(baseName, new Locale(getLocale()));
            } catch (Exception ex) {
                LogHelper.makeLog("Initializing ResourceBundle: " + baseName + " failed, --> " + ex.getMessage(), ex);
            }
        }

        return resourceBundle;
    }

    public String getLocale() {

        LocationDTO userLocation = null;
        //First I get the Locale from the users session, to see if I have already assigned him one.
        try {
            userLocation = (LocationDTO) FacesUtils.getManagedBean(SessionConst.LOCALE);
        } catch (Exception e) {
            //I dont care
        }

        if (userLocation == null || UtilHelper.isStringEmpty(userLocation.getLocale())) {

            //Ok the user doesn't have a locale Set to him yet, so Im going to lookup his
            //location based on his address and assing him a locle.

            boolean saveInSession = true; //This variable is only used to fix an ocurrence that happens during initialiation of the application.

            try {

                String ip = FacesUtils.getRemoteAddress();
                userLocation = ipLocationService.findLocale(ip);

            } catch (Exception e) {
                userLocation = new LocationDTO(MetConfiguration.DEFAULT_LOCALE_LANGUAGE, MetConfiguration.DEFAULT_NETWORK);

                if (e instanceof NullPointerException) {
                    //This can happen during initialization of the application, beacuse
                    //there is no remoteAdress, because no real user is invoking this method, so I just
                    //ignore it.
                    saveInSession = false;
                } else {
                    //Someting bad happened and I couldn't retieve the users Location, so I assing him a
                    //default location.
                    LogHelper.makeLog(e);
                }
            }

            try {
                if (saveInSession) {
                    //Finally I assign the UsersLocation to the users Session
                    FacesUtils.setManagedBeanInSession(SessionConst.LOCALE, userLocation);
                }
            } catch (Exception e) {
                LogHelper.makeLog(e.getMessage());
            }
        }

        return userLocation.getLocale();
    }

    //==============User State==================================================//
    public static String getUserState(String id) {

        if (id == null) {
            return null;
        }

        try {
            return userStateMap.get(id);
        } catch (Exception ex) {
            LogHelper.makeLog(ex.getMessage(), ex);
        }

        return null;
    }

    public static ArrayList<SelectItem> getSelectItemUserState() {
        return selectItemUserState;
    }

    public synchronized void initializeUserStateList() {
        ResourceBundle resourceBundle = getResourceBundle();

        selectItemUserState = new ArrayList<SelectItem>();
        selectItemUserState.add(new SelectItem("", resourceBundle.getString("commun_select")));

        selectItemUserState.add(new SelectItem(NewsState.ACTIVE, resourceBundle.getString("common_state_active")));
        userStateMap.put(UserState.ACTIVE, resourceBundle.getString("common_state_active"));

        selectItemUserState.add(new SelectItem(NewsState.INACTIVE, resourceBundle.getString("common_state_inactive")));
        userStateMap.put(UserState.INACTIVE, resourceBundle.getString("common_state_inactive"));

        selectItemUserState.add(new SelectItem(NewsState.DELETED, resourceBundle.getString("common_state_deleted")));
        userStateMap.put(UserState.DELETED, resourceBundle.getString("common_state_deleted"));

        selectItemUserState.add(new SelectItem(NewsState.PENDING, resourceBundle.getString("common_state_pending")));
        userStateMap.put(UserState.PENDING, resourceBundle.getString("common_state_pending"));

        selectItemUserState.add(new SelectItem(NewsState.PENDING, resourceBundle.getString("common_state_banned")));
        userStateMap.put(UserState.BANNED, resourceBundle.getString("common_state_banned"));

    }

    //==============News State==================================================//
    public static String getNewsState(String id) {

        if (id == null) {
            return null;
        }

        try {
            return newsStateMap.get(id);
        } catch (Exception ex) {
            LogHelper.makeLog(ex.getMessage(), ex);
        }

        return null;
    }

    public static ArrayList<SelectItem> getSelectItemNewsStateFull() {
        return selectItemNewsStateFull;
    }

    public static ArrayList<SelectItem> getSelectItemNewsStateLimited() {
        return selectItemNewsStateLimited;
    }

    public synchronized void initializeNewsStateList() {
        ResourceBundle resourceBundle = getResourceBundle();

        selectItemNewsStateFull = new ArrayList<SelectItem>();
        selectItemNewsStateLimited = new ArrayList<SelectItem>();
        selectItemNewsStateFull.add(new SelectItem("", resourceBundle.getString("commun_select")));
        selectItemNewsStateLimited.add(new SelectItem("", resourceBundle.getString("commun_select")));

        selectItemNewsStateFull.add(new SelectItem(NewsState.ACTIVE, resourceBundle.getString("common_state_active")));
        selectItemNewsStateLimited.add(new SelectItem(NewsState.ACTIVE, resourceBundle.getString("common_state_active")));
        newsStateMap.put(NewsState.ACTIVE, resourceBundle.getString("common_state_active"));

        selectItemNewsStateFull.add(new SelectItem(NewsState.INACTIVE, resourceBundle.getString("common_state_inactive")));
        newsStateMap.put(NewsState.INACTIVE, resourceBundle.getString("common_state_inactive"));

        selectItemNewsStateFull.add(new SelectItem(NewsState.DELETED, resourceBundle.getString("common_state_deleted")));
        newsStateMap.put(NewsState.DELETED, resourceBundle.getString("common_state_deleted"));

        selectItemNewsStateFull.add(new SelectItem(NewsState.PENDING, resourceBundle.getString("common_state_pending")));
        selectItemNewsStateLimited.add(new SelectItem(NewsState.PENDING, resourceBundle.getString("common_state_pending")));
        newsStateMap.put(NewsState.PENDING, resourceBundle.getString("common_state_pending"));

    }

    //==============Date Select Items==================================================//
    public static ArrayList<SelectItem> getSelectItemDays() {
        return selectItemDays;
    }

    public static ArrayList<PairDTO> getSelectItemMonths() {
        return selectItemMonths;
    }

    public static ArrayList<SelectItem> getSelectItemYears() {
        return selectItemYears;
    }

    public static synchronized void initializedDateList() {

        selectItemMonths = new ArrayList<PairDTO>();
        selectItemDays = new ArrayList<SelectItem>();
        selectItemYears = new ArrayList<SelectItem>();

        selectItemMonths.add(new PairDTO("", "common_info_month"));

        int month = 0;
        //Months have to start form zero. Because for Gregorian Calendar Jan = 0 and Feb = 1;
        for (int i = 1; i <= 12; i++) {
            month = i - 1;
            selectItemMonths.add(new PairDTO("" + month, ("common_months_" + i)));
        }

        for (int i = 1; i <= 31; i++) {
            selectItemDays.add(new SelectItem("" + i, "" + i));
        }


        int actualYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = actualYear; i >= 1900; i--) {
            selectItemYears.add(new SelectItem("" + i, "" + i));
        }

    }

//==============Network================================================//
    public static HashMap<Integer, Network> getNetworkMap() {
        return networkMap;
    }

    public static Network getNetwork(Integer id) {

        if (id == null) {
            return null;
        }

        try {
            return networkMap.get(id);
        } catch (Exception ex) {
            LogHelper.makeLog(ex.getMessage(), ex);
        }

        return null;
    }

    public static ArrayList<SelectItem> getSelectItemNetwork() {
        return selectItemNetwork;
    }

    public static synchronized void initializeNetworkList(List<Network> list) {
        for (Iterator<Network> it = list.iterator(); it.hasNext();) {
            Network entity = it.next();
            networkMap.put(entity.getId(), entity);
            selectItemNetwork.add(new SelectItem(entity.getId(), entity.getName()));
        }
    }

//==============Forum ================================================//
    
    public static ForumCategory getForumCategory(Short id) {

        if (id == null) {
            return null;
        }

        try {
            return forumCategoryMap.get(id);
        } catch (Exception ex) {
            LogHelper.makeLog(ex.getMessage(), ex);
        }

        return null;
    }

    public static ArrayList<PairDTO> getSelectItemForumCategory() {
        return selectItemForumCategory;
    }

    public static List<ForumCategory> getForumCategoryList() {
        return forumCategoryList;
    }
    
    public static synchronized void initializeForumCategoryList(List<ForumCategory> list) {
        for (Iterator<ForumCategory> it = list.iterator(); it.hasNext();) {
            ForumCategory entity = it.next();
            forumCategoryList.add(entity);
            forumCategoryMap.put(entity.getId(), entity);
//            selectItemForumCategory.add(new SelectItem(entity.getId(), entity.getName(), entity.getName(), false, true));
            selectItemForumCategory.add(new PairDTO(entity.getId().toString(), entity.getName()));
        }
    }
    
//==============Reserved Names==================================================//
    public static HashMap<String, String> getReservedNamesMap() {
        return reservedNamesMap;
    }

    public static boolean isNameReserved(String name) {
        return reservedNamesMap.containsKey(name);
    }

    public static synchronized void initializeReservedNamesList(List<ReservedNames> list) {
        for (Iterator<ReservedNames> it = list.iterator(); it.hasNext();) {
            ReservedNames entity = it.next();
            reservedNamesMap.put(entity.getNick(), entity.getName());
        }
    }
//==============Country==================================================//
//==============Country==================================================//
//==============Country==================================================//
//==============Country==================================================//
}
