/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.user;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.state.BanTime;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.entity.UserBan;
import co.com.metallium.core.exception.AuthenticationException;
import co.com.metallium.core.exception.UserException;
import co.com.metallium.core.service.JpaCacheService;
import co.com.metallium.core.service.dto.NotificationDTO;
import co.com.metallium.core.util.MetUtilHelper;
import co.com.metallium.view.util.JsfUtil;
import com.metallium.utils.framework.jsf.FacesUtils;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ruben
 */
@ManagedBean(name = "loginUserManagedBean")
@ViewScoped
public class LoginUserManagedBean extends UserBaseManagedBean implements Serializable {

    public static final String LAST_URL_REDIRECT_KEY = "LAST_URL_REDIRECT_KEY";
    private boolean autoLogin = true;
    private String forgotPassword;
    //notification variables
    private Boolean autenticated = null;
    private Boolean newMessage = null;
    private Boolean newNotification = null;
    //Im putty this meanwhile, but this isn't the best place to put this.
    private String faviconUrl = MetConfiguration.FAVICON_URL;
    @EJB
    public JpaCacheService jpaCacheService;

    /** Creates a new instance of LoginUserManagedBean */
    public LoginUserManagedBean() {
        refreshCacheVariables();
    }

    public String logoutUser() {
        refreshCacheVariables();
        String answer = Navegation.goToIndex;
        Integer userId = this.getAuthenticaedUser().getId();
        this.logOut();
        this.removeAutoLogin(userId);
        JsfUtil.invalidateSession();
        refreshCacheVariables();
        return answer;
    }

    public void loginUser() {

        refreshCacheVariables();
        String answer = Navegation.goToLogin; //I stay in the same page and show an error message

        validateLoginUserInput();

        if (JsfUtil.getMessageListSize() == 0) {
            //Since there are no validation messages then I can procced to authenticate the user.
            try {
                this.setUserLogInfo();
                User authenticatedUser = this.getUserService().authenticateUser(this.getUser());

                this.logIn(authenticatedUser);


                if (autoLogin) {
                    String uuid = UUID.randomUUID().toString();
                    userService.rememberSave(uuid, authenticatedUser.getId());
                    Cookie userCookie = new Cookie("metalcity.cookie", uuid);
                    userCookie.setMaxAge(60*60*24*7); // One week
                    ((HttpServletResponse) FacesUtils.getExternalContext().getResponse()).addCookie(userCookie);
                } else {
                    removeAutoLogin(authenticatedUser.getId());
                }

            } catch (Exception ex) {
                //Login failed
                if (ex instanceof AuthenticationException) {
                    boolean result = this.getUserService().blockUserForFailedLoginAttempts(this.getUser().getEmail());
                    if (result) {
                        banUserForInvalidAuthentication(ex);
                    }
                }

                this.logOut();
                this.manageException(ex);
            }
        }
        //  return answer;
    }

    private void removeAutoLogin(Integer userId) {
        userService.rememberDelete(userId);
        Cookie userCookie = new Cookie("metalcity.cookie", null);
        userCookie.setMaxAge(0);
        ((HttpServletResponse) FacesUtils.getExternalContext().getResponse()).addCookie(userCookie);
    }

    private void banUserForInvalidAuthentication(Exception ex) {
        try {
            UserBan userBan = new UserBan();
            userBan.setBannedUserId(((AuthenticationException) ex).getUserId());
            userBan.setAdminId(null); //No Admin Id necesarry
            userBan.setBanExpirationDate(MetUtilHelper.determineBanExpirationDate(BanTime._5_MIN_));
            userBan.setDate(new Date());
            userBan.setWhy("banned_info_invalid_authentication");
            this.getUserService().banUser(userBan);
        } catch (UserException ex1) {
            LogHelper.makeLog(ex1);
        }
    }

    private void validateLoginUserInput() {
        this.validateUserEmail(this.getUser().getEmail());
        this.validateUserPassword();
    }

    public String forgotPassword() {
        String answer = Navegation.stayInSamePage; //I stay in the same page and show an error message

        try {
            this.validateForgotPasswordInput();

            if (isJsfMessageListEmpty()) {
                this.getUserService().rememberPassword(this.forgotPassword);
                answer = Navegation.goToIndex;
                this.addFacesMsgFromProperties("forgot_password_info_email_sent", this.forgotPassword);
            }

        } catch (Exception ex) {
            this.manageException(ex);
        }

        return answer;
    }

    private void validateForgotPasswordInput() {
        String auxEmailValidate = this.validateEmail(this.forgotPassword);

        if (!UtilHelper.isStringEmpty(auxEmailValidate)) {
            this.addFacesMsg(auxEmailValidate);
        }
    }

    public boolean isDoIHaveNewMessages() {

        if (autenticated == null) {
            autenticated = isAuthenticated();
        }

        if (autenticated) {
            if (newMessage == null) {
                initializeCacheVariables();
            }
        }

        return newMessage;

    }

    public boolean isDoIHaveNewNofitications() {

        if (autenticated == null) {
            autenticated = isAuthenticated();
        }

        if (autenticated) {
            if (newNotification == null) {
                initializeCacheVariables();
            }
        }

        return newNotification;

    }

    private void initializeCacheVariables() {
        NotificationDTO notifications = userService.readUserNotifications(getAuthenticaedUser().getId());
        setCacheVariable(notifications);
        this.getAuthenticaedUser().setNotifications(newNotification); //I just read it.
        this.getAuthenticaedUser().setNewMessage(newMessage); //I just read it.

    }

    private void refreshCacheVariables() {
        autenticated = null;
        newMessage = null;
        newNotification = null;
    }

    private void setCacheVariable(NotificationDTO notifications) {
        if (notifications != null) {
            newMessage = notifications.getNewMessage();
            newNotification = notifications.getNotifications();
        } else {
            newMessage = false;
            newNotification = false;
        }
    }

    public void evictAll() {
        this.addFacesMsg("Evict All!!!");
        jpaCacheService.evictAll();
    }

    //==========================Getters and Setters=========================================//
    public String getForgotPassword() {
        return forgotPassword;
    }

    public void setForgotPassword(String forgotPassword) {
        this.forgotPassword = forgotPassword;
    }

    public String getFaviconUrl() {
        return faviconUrl;
    }
}
