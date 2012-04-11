/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.NotificationConst;
import co.com.metallium.core.constants.SessionConst;
import co.com.metallium.core.constants.state.UserProfile;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.entity.iplocation.LocationDTO;
import co.com.metallium.core.exception.BaseException;
import co.com.metallium.core.service.UserService;
import co.com.metallium.core.service.dto.search.UserFriendsSearchDTO;
import co.com.metallium.core.util.ApplicationParameters;
import co.com.metallium.view.jsf.filter.SecurityFilter;
import co.com.metallium.view.util.GeneralConverter;
import co.com.metallium.view.util.JsfUtil;
import com.metallium.utils.framework.jsf.FacesUtils;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Ruben
 */
@ManagedBean(name = "baseBean")
@NoneScoped
public class BaseManagedBean implements Serializable {

    @EJB
    public ApplicationParameters applicationParameters;
    private GeneralConverter converter = null;
    private boolean initialized = false;
    @EJB
    public UserService userService;

    /** Creates a new instance of BaseManagedBean */
    public BaseManagedBean() {
    }

    /**
     * Returns the Converter class that has all the Converters.
     * Now as you can see I am over ridding the method getApplicationParameters()
     * and what Im doing is that I am injecting an EJB in a Pojo (The Converter is a POJO),
     * because, in a Converter you can't inject an EJB, so I had to do this trick.
     *
     * If not I would have had to change everything, so this saved my day.
     *
     * @return
     */
    public GeneralConverter getConverter() {

        if (converter == null) {
            converter = new GeneralConverter() {

                @Override
                public ApplicationParameters getApplicationParameters() {
                    return applicationParameters;
                }
            };

        }

        return converter;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    /**
     * I validate if there are any error messages in the List.
     * I usually use this to see if a validation condition has occurred so
     * that I can stop the normal cause of an event.
     *
     * Example: if it is not empty then it could have a lets say a "user didn't type the password"
     * message in the list. So I know that I cant go on until this validation is passed.
     * 
     */
    public boolean isJsfMessageListEmpty() {
        boolean answer = false;

        if (JsfUtil.getMessageListSize() == 0) {
            answer = true;
        }
        return answer;
    }

    public void addFacesMsgFromProperties(String msg) {
        JsfUtil.addSuccessMessage(applicationParameters.getResourceBundleMessage(msg));
    }

    public void addFacesMsgFromProperties(String msg, Object... parameters) {
        JsfUtil.addSuccessMessage(applicationParameters.getResourceBundleMessage(msg, parameters));
    }

    public void addFacesMsgFromValidationProperties(String msg, String field) {
        JsfUtil.addSuccessMessage(applicationParameters.getResourceBundleValidationMessage(msg, field));
    }

    public void addFacesMsg(String msg) {
        JsfUtil.addSuccessMessage(msg);
    }

    public void manageException(Exception ex, String displayMessage) {

        if (ex instanceof BaseException) {
            BaseException bx = ((BaseException) ex);
            bx.setDisplayMessage(displayMessage);
            this.manageException(bx);
        } else {
            manageException(ex);
        }
    }

    public void manageException(Exception ex) {

        if (ex instanceof BaseException) {
            this.manageBaseException((BaseException) ex);
        } else {
            manageGenericException(ex);
        }
    }

    /**
     * Its in charged of handling an exception in the web tier. If verifies if it has
     * a display message to show or if it must show a generic exception and finally
     * of printing out the log of the exception. 
     *
     * @param ex
     */
    private void manageBaseException(BaseException ex) {

        if (ex.getDisplayMessage() != null && !ex.getDisplayMessage().trim().equalsIgnoreCase("")) {
            addFacesMsg(ex.getDisplayMessage());

            //Ok now, if it usually is a BaseException, with a Display Message, then it means
            //I just throw the exception to display that display message, not to print the whole Stack Trace
            //of the exception. So I will check if there is a Cause of this exception otherwise I wont print nothing.
            //Ohh and also I dont print NoResultExceptions
            if (ex.getCause() != null
                    && !"class javax.persistence.NoResultException".equals(ex.getCause().getClass().toString())) {
                LogHelper.makeLog(ex.getMessage(), ex);
            }

        } else {
            manageGenericException(ex);
        }

    }

    private void manageGenericException(Exception e) {
        if (e != null) {
            LogHelper.makeLog(e.getMessage(), e);
        }
        JsfUtil.addSuccessMessage(applicationParameters.getResourceBundleMessage("commun_info_generic_error"));
    }

    public String goToIndex() {
        return Navegation.goToIndex;
    }

    public String goToRegisterUser() {
        return Navegation.goToRegisterUser;
    }

    public String goToProfile() {
        return Navegation.goToProfile;
    }

    public void invalidarSesion() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
    }

    /**
     * Compares the given user Id against the user authenticated ID
     *
     * true = if its the owner
     * false = if its not the owner
     *
     */
    public boolean isAmITheOwner(Integer userId) {

        if (!this.isAuthenticated()) {
            return false;
        }

        try {
            User authenticatedUser = this.getAuthenticaedUser();
            //This has to be bullet proof

            //Here I Compare the User who's profile I'm on, and the user authenticated.
            //because I can see Jon's profile but I am Ruben, so that Means that only when Jon compareStates authenticated he can edit his profile.
            boolean amIinMyProfilePage = UtilHelper.areIntegersEqual(userId, authenticatedUser.getId());
            if (amIinMyProfilePage) {
                return true;
            }
        } catch (Exception ex) {
            LogHelper.makeLog("Error in isCanIeditProfile: " + ex.getMessage() + ", so this means he can't edit it", ex);
        }

        return false;
    }

    /**
     * Compares the given user Id against the user authenticated ID
     *
     * true = if the given Profile is of user who has an INFERIOR profile than mine
     * false = if the given Profile is of a user who as an EQUAL or SUPERIOR profile than mine
     *
     */
    public boolean isMyProfileSuperior(Integer userProfile) {

        if (!this.isAuthenticated()) {
            return false;
        }

        try {
            Integer myProfile = this.getAuthenticaedUser().getProfile();
            //I compare my profile against another users profile.
            return UtilHelper.isIntegerSuperior(myProfile, userProfile);

        } catch (Exception ex) {
            LogHelper.makeLog(ex);
        }

        return false; //Im nobody jajajaja
    }

    public User getAuthenticaedUser() {
        User authenticatedUser = null;
        try {
            authenticatedUser = (User) FacesUtils.getManagedBean(SecurityFilter.AUTHENTICATED_USER);
        } catch (NullPointerException e) {
            //This is normal to happen when you are not logged in
        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }
        return authenticatedUser;
    }

    public void setAuthenticaedUser(User authenticaedUser) {
        FacesUtils.setManagedBeanInSession(SecurityFilter.AUTHENTICATED_USER, authenticaedUser);
    }

    public boolean isAuthenticated() {
        boolean authenticated = false;
        try {
            authenticated = (Boolean) FacesUtils.getManagedBean(SecurityFilter.AUTHENTICATED);

            if (authenticated) {
                checkForNotifications();
            }

        } catch (NullPointerException e) {
            //This is normal to happen when you are not logged in
        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        FacesUtils.setManagedBeanInSession(SecurityFilter.AUTHENTICATED, authenticated);
    }

    public void setUserLocale(String locale) {

        LocationDTO userLocation = obtainCurrentUserLocation();
        if (userLocation == null) {
            userLocation = new LocationDTO();
        }
        userLocation.setLocale(locale);

        FacesUtils.setManagedBeanInSession(SessionConst.LOCALE, userLocation);
    }

    public LocationDTO obtainCurrentUserLocation() {

        LocationDTO userLocation = null;
        try {
            userLocation = (LocationDTO) FacesUtils.getManagedBean(SessionConst.LOCALE);
        } catch (Exception e) {
            //I dont care
        }
        return userLocation;
    }

    /**
     * These are the rules to determine the users network
     * 
     * 1. Only if you are an administrator can you actually determine your network.
     *    So if you are an administrator I will return the network you have configured.
     * 
     * 2. If you are not an administrator, then I will return the network that I 
     *    assigned to you when I looked up your IP address (IpLocationService).
     * 
     * 3. Finally if it all went wrong I will return the default network defined in 
     *    the system. This shouldn't be the case.
     * 
     * @return
     */
    public Integer obtainCurrentUserNetwork() {

        if (isAuthenticated() && isMyProfileAdministrator1()) {
            return getAuthenticaedUser().getNetwork();
        } else {
            LocationDTO userLocation = obtainCurrentUserLocation();
            if (userLocation != null) {
                return userLocation.getNetwork();
            }
        }

        return MetConfiguration.DEFAULT_NETWORK;
    }

    public boolean isMyProfileAdministrator1() {
        boolean answer = false;
        try {

            User user = this.getAuthenticaedUser();
            if (user != null && UserProfile.isAdministrator1(user.getProfile())) {
                answer = true;
            }
        } catch (NullPointerException e) {
            //This is normal to happen when you are not logged in
        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }
        return answer;

    }

    public boolean isMyProfileAdministrator2() {
        boolean answer = false;
        try {

            User authenticatedUser = this.getAuthenticaedUser();
            if (authenticatedUser != null && UserProfile.isAdministrator2(authenticatedUser.getProfile())) {
                answer = true;
            }
        } catch (NullPointerException e) {
            //This is normal to happen when you are not logged in
        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }
        return answer;

    }

    public boolean isMyProfileAdministrator666() {
        boolean answer = false;
        try {

            User user = this.getAuthenticaedUser();
            if (user != null && UserProfile.isRoot(user.getProfile())) {
                answer = true;
            }
        } catch (NullPointerException e) {
            //This is normal to happen when you are not logged in
        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }
        return answer;

    }

    /**
     * Helper Method to find a specific Managed bean so I can initialize it with some parameters.
     * @param <T>
     * @param beanName
     * @param beanClass
     * @return
     */
    public static <T> T findManagedBean(String beanName, Class<T> beanClass) {
        FacesContext context = FacesContext.getCurrentInstance();
        return beanClass.cast(context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", beanClass));
    }

    private void checkForNotifications() {
        try {
            Object valueFromApplicationScope = FacesUtils.getValueFromApplicationScope("__".concat(this.getAuthenticaedUser().getId().toString()).concat("__"));

            if (valueFromApplicationScope != null && (valueFromApplicationScope instanceof String)) {

                String action = (String) valueFromApplicationScope;
                if (NotificationConst.REFRESH_FRIENDS.equalsIgnoreCase(action)) {
                    System.out.println("... notification consumed for user id: " + this.getAuthenticaedUser().getId() + "user refreshed");
                    refreshUsersFriends(this.getAuthenticaedUser().getId());
                    //Since I already completed the action I wanted then I remove the action.
                    FacesUtils.removeValueFromApplicationScope("__" + this.getAuthenticaedUser().getId() + "__");
                }
            }
        } catch (Exception e) {
            LogHelper.makeLog(e);
        }
    }

    public List<User> refreshUsersFriends(int myId) {
        List<User> friends = this.userService.findUsersFriends(new UserFriendsSearchDTO(myId));
        this.getAuthenticaedUser().setUserFriendsCollection(friends);
        return friends;
    }

    //================ Setters and Getters ===================================//
    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
