/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.user;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.NotificationConst;
import co.com.metallium.core.constants.UserCommon;
import co.com.metallium.core.constants.state.CommentState;
import co.com.metallium.core.constants.state.UserProfile;
import co.com.metallium.core.constants.state.UserState;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.entity.UserWallComments;
import co.com.metallium.core.entity.iplocation.LocationDTO;
import co.com.metallium.core.exception.CommentException;
import co.com.metallium.core.exception.FriendException;
import co.com.metallium.core.util.MetUtilHelper;
import co.com.metallium.core.util.RegexUtil;
import co.com.metallium.view.jsf.BaseManagedBean;
import co.com.metallium.view.util.JsfUtil;
import com.metallium.utils.dto.DateHelperDTO;
import com.metallium.utils.dto.PairDTO;
import com.metallium.utils.framework.jsf.FacesUtils;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

/**
 *
 * @author Ruben
 */
@ManagedBean
//@javax.faces.bean.SessionScoped
@javax.faces.bean.SessionScoped
public class UserBaseManagedBean extends BaseManagedBean implements Serializable {

    private Locale locale;
    private User user = new User();
    private DateHelperDTO dateHelper = new DateHelperDTO();
    private User authenticatedUser = null;
    private String network = MetConfiguration.DEFAULT_NETWORK.toString();
    private boolean newMssage = false;
    //These are my CACHE VALUES
    private Boolean canIeditProfile = null;
    private Boolean canIbanUser = null;
    private Boolean canIunbanUser = null;
    private Boolean amIadministrator = null;

    /** Creates a new instance of UserBaseManagedBean */
    public UserBaseManagedBean() {
    }

    //==============Start Authentication Logic =======================================//
    public void logOut() {
        this.setAuthenticated(false);
        this.setAuthenticaedUser(null);
        this.setUserLocale(MetConfiguration.DEFAULT_LOCALE_LANGUAGE);
        this.setNetwork(MetConfiguration.DEFAULT_NETWORK.toString());
        this.locale = null; //So the Locale can be refreshed
    }

    public void logIn(User user) {
        this.setAuthenticaedUser(user);
        this.setAuthenticated(true);
        this.setUserLocale(user.getLocale());
        this.locale = null; //So the Locale can be refreshed
    }
    //==============End Authentication Logic =======================================//

    public String validateUserEmail(String email) {
        String auxEmailValidate = this.validateEmail(email);
        if (auxEmailValidate.length() > 0) {
            this.addFacesMsg(auxEmailValidate);
        }
        return auxEmailValidate;
    }

    public void validateUserPassword() {
        String auxValidatePasswordMsg = this.validatePassword(this.getUser().getPassword());
        if (auxValidatePasswordMsg.length() > 0) {
            JsfUtil.addSuccessMessage(auxValidatePasswordMsg);
        }
    }

    public String validatePassword(String password) {
        String answer = "";

        if (!UtilHelper.isStringEmpty(password)) {

            if (password.length() < 6) {
                answer = applicationParameters.getResourceBundleMessage("register_field_password_info_length_short");
            } else {
                if (password.length() > 50) {
                    answer = applicationParameters.getResourceBundleMessage("register_field_password_info_length_long");
                } else {
                    if (!password.matches(MetUtilHelper.PASSWORD_REGEX)) {
                        answer = applicationParameters.getResourceBundleMessage("register_field_password_info_valid");
                    }
                }
            }
        } else {
            answer = applicationParameters.getResourceBundleMessage("register_field_password_required");
        }

        return answer;
    }

    public String validateEmail(String email) {

        String answer = "";
        if (UtilHelper.isStringEmpty(email)) {
            answer = applicationParameters.getResourceBundleMessage("register_field_email_required");
        } else {
            if (!RegexUtil.isEmailValid(email)) {
                answer = applicationParameters.getResourceBundleMessage("register_field_email_info_invalid");
            }
        }

        return answer;
    }

    public ArrayList<PairDTO> getSelectItemLocale() {
        return applicationParameters.getSelectItemLocale();
    }

    public ArrayList<SelectItem> getSelectItemCountry() {
        return applicationParameters.getSelectItemCountry();
    }

    public ArrayList<SelectItem> getSelectItemNetwork() {
        return applicationParameters.getSelectItemNetwork();
    }
    
    public ArrayList<PairDTO> getSelectItemSex() {
        return applicationParameters.getSelectItemSex();
    }

    public ArrayList<PairDTO> getSelectItemBirthdayDisplay() {
        return applicationParameters.getSelectItemBirthdayDisplay();
    }

    public ArrayList<PairDTO> getSelectItemBanTime() {
        return applicationParameters.getSelectItemBanTime();
    }

    public ArrayList<SelectItem> getSelectItemDays() {
        return applicationParameters.getSelectItemDays();
    }

    public ArrayList<PairDTO> getSelectItemMonths() {
        return applicationParameters.getSelectItemMonths();
    }

    public ArrayList<SelectItem> getSelectItemYears() {
        return applicationParameters.getSelectItemYears();
    }

    /**
     * It includes in the user his Session ID and his remote address
     * and such info in order to log his where he's doing his stuff.
     * 
     */
    public void setUserLogInfo() {
        this.getUser().setSessionId(FacesUtils.getSessionId());
        this.getUser().setIpAddress(FacesUtils.getRemoteAddress());

        LocationDTO userLocationDTO = obtainCurrentUserLocation();
        if (userLocationDTO != null) {
            this.getUser().setLocationId(userLocationDTO.getId());
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DateHelperDTO getDateHelper() {
        return dateHelper;
    }

    public void setDateHelper(DateHelperDTO dateHelper) {
        this.dateHelper = dateHelper;
    }

    //==============Form Input validations===================================//
    /**
     * It compareStates only supposed to tell me If I can Edit or NOT a profile.
     * It should not add any warring message or anything, other methods will
     * deal with that.
     *
     * I can edit a profile only if it compareStates my profile
     *
     * @return boolean true = I can edit, false = I cant edit.
     */
    public boolean isCanIeditProfile() {

        if (this.canIeditProfile == null) {

            this.canIeditProfile = false; //I assume you cant edit my profile unless you prove otherwise.

            if (this.isAuthenticated()) {
                try {
                    this.canIeditProfile = this.isAmITheOwner(this.getUser().getId());
                } catch (Exception ex) {
                    LogHelper.makeLog(ex);
                }
            }
        }
        return this.canIeditProfile;
    }

    public boolean isAmIadministrator() {

        if (this.amIadministrator == null) {
            this.amIadministrator = this.isMyProfileAdministrator1(); //I can ban a User if I am an Administrator 1 and up.
        }
        return this.amIadministrator;
    }

    /**
     * Wrapper Method that handles my CACHE Variable
     * THis one is the one called from the web.
     */
    public boolean isCanIbanUser() {
        if (this.canIbanUser == null) {
            try {
                this.canIbanUser = isCanIbanOrUnBanUserLogic(true);
            } catch (Exception e) {
                //I dont fucking care !!!
                this.canIbanUser = false;
            }
        }
        return this.canIbanUser;
    }

    /**
     * Wrapper Method that handles my CACHE Variable
     * THis one is the one called from the web.
     */
    public boolean isCanIunbanUser() {
        if (this.canIunbanUser == null) {
            try {
                this.canIunbanUser = isCanIbanOrUnBanUserLogic(false);
            } catch (Exception e) {
                //I dont fucking care !!!
                this.canIunbanUser = false;
            }
        }
        return this.canIunbanUser;
    }

    private boolean isCanIbanOrUnBanUserLogic(boolean isActionBan) {
        boolean answer = false;


        if (isActionBan) {
            if (UserState.isBanned(this.getUser().getState())) {
                //I cant ban a banned user who is Banned
                return false;
            }
        } else {
            //So Im going to unban someone.
            if (!UserState.isBanned(this.getUser().getState())) {
                //I cant unban a user who is NOT banned
                return false;
            }
        }


        answer = this.isMyProfileAdministrator1(); //I can ban a User if I am an Administrator 1 and up.

        if (answer) {
            //Yes Im an administrator 2 or Up, but now I have to check if I am superior than the fucker I want to ban.
            //I just cant let some asshole come up to me, and kick me out of my house. Fuck that.
            try {
                answer = this.isMyProfileSuperior(this.getUser().getProfile());
            } catch (Exception ex) {
                answer = false; //Well If I dont know if Im superior then I cant ban no one.
                LogHelper.makeLog(ex);
            }
        }
        return answer;
    }

    //==============Start User Wall Comments===================================//
    /**
     * Only three persons can delete a User wall Comments.
     * The person owner of the wall
     * The person who wrote the comment.
     * An Administrator level 2 or greater.
     *
     * @param auxComment
     * @return
     * @throws CommentException
     */
    public boolean canIdeleteUserWallCommentLogic(UserWallComments auxComment) throws CommentException {

        if (!this.isAuthenticated()) {
            throw new CommentException("", null, applicationParameters.getResourceBundleMessage("common_info_user_must_be_authenticated"));
        }

        boolean answer = false;

        if (!CommentState.isDeleted(auxComment.getState())) {

            Integer myId = this.getAuthenticaedUser().getId();
            Integer userWallId = auxComment.getUserWallId();

            if (UtilHelper.areIntegersEqual(myId, userWallId)) {
                //YES I am the Owner of the wall. So I can delete it
                answer = true;
            } else {

                Integer userCommentId = auxComment.getUser().getId();
                if (UtilHelper.areIntegersEqual(myId, userCommentId)) {
                    //YES I am the author of the comment.
                    answer = true;
                } else {
                    if (UserProfile.isAdministrator1(authenticatedUser.getProfile())) {
                        //YES Im not the author of the comment, But I have the right to delete it. jajaja I just have the power
                        answer = true;
                    }
                }
            }
        }
        return answer;
    }
    //==============End User Wall Comments===================================//
    //==============Start Friends Logic===================================//

    /**
     * 3 Steps:
     * #1 Am I authenticated
     * #2 Am I on my profile
     * #3 What action am I doing
     *
     *
     * @param idUserOwnerOfTheProfile
     * @param accion
     * @return
     * @throws FriendException
     */
    public boolean canI_x_ToFriend(Integer idUserOwnerOfTheProfile, String accion) throws FriendException {

        boolean answer = false;

        //#1
        if (DataHelper.isNull(authenticatedUser) && !this.isAuthenticated()) {

            if (UserCommon.FRIEND_VIEW_FRIEND_LIST.equalsIgnoreCase(accion) &&
                    DataHelper.isNotNull(idUserOwnerOfTheProfile)) {
                //Ok I do allow to preview friends if you are not logged in, and
                //you are looking at a profile. At least in this method I allow him.
                return true;
            }

            //I cant be a friend of someone if I am not logged in.
            return false;
        } else {
            this.authenticatedUser = this.getAuthenticaedUser();
        }

        //#2
        int myId = this.getAuthenticaedUser().getId();
        boolean amIOnMyOwnProfile = UtilHelper.areIntegersEqual(myId, idUserOwnerOfTheProfile);

        if (amIOnMyOwnProfile) {
            //If I am on my profile, this means I can only do a certain numer of actions.
            //I can only write on my wall.
            if (UserCommon.FRIEND_WRITE_ON_WALL.equalsIgnoreCase(accion)
                    || UserCommon.FRIEND_VIEW_FRIEND_LIST.equalsIgnoreCase(accion)) {

                return true;

            } else {
                //I cant make any action on my profile. Just write on my wall and see my friends.
                return false;
            }
        }

        //#3
        if (UserCommon.FRIEND_ADD.equalsIgnoreCase(accion)) {
            //I can add a friend to my list if I am not on MY PROFILE (It is already validated)
            //and if he is not my friend.
            if (!isUserMyFriend(myId, idUserOwnerOfTheProfile)) {
                //If user is not my friend then I can Add Him as a friend
                answer = true;
            }
        } else if (UserCommon.FRIEND_DELETE.equalsIgnoreCase(accion)) {
            //I can delete a friend to my list if I am not on MY PROFILE (It is already validated)
            //and if he Is my friend.
            if (isUserMyFriend(myId, idUserOwnerOfTheProfile)) {
                //If user IS my friend then I delete him from my friends list.
                answer = true;
            }
        } else if (UserCommon.FRIEND_WRITE_ON_WALL.equalsIgnoreCase(accion)) {

            if (isUserMyFriend(myId, idUserOwnerOfTheProfile)) {
                //If user IS my friend then I can write on his wall.
                answer = true;
            }
        } else if (UserCommon.FRIEND_VIEW_FRIEND_LIST.equalsIgnoreCase(accion)) {

            if (DataHelper.isNotNull(idUserOwnerOfTheProfile)) {
                //This means that if they put an Id I can search for there friends. If no Id is specified then
                //I wont bother showing the friend list.
                return true;

            }

        }

        return answer;

    }

    private boolean isUserMyFriend(Integer myId, Integer idUserThatWantsToBeMyFriend) {
        boolean answer = false;

        //We take the friends that are in the users current session. Because we dont want to go to the db every time I want to find
        //the users friends. Only when I need to.
        List<User> myFriends = this.authenticatedUser.getUserFriendsCollection();

        //So I check to see if the user has friends.
        if (myFriends == null) {
            //So he has nothing set on his "Session friend list". So I go to the DB and check again, to see if he really has no friends.
            myFriends = refreshUsersFriends(myId);
        }

        if (myFriends.contains(new User(idUserThatWantsToBeMyFriend))) {
            //The user is already my friend.
            answer = true;
        }

        return answer;
    }


    public void refreshNotifications(int myId) {

        this.refreshUsersFriends(myId);

    }

    //==============End Friends Logic===================================//
    public Locale getLocale() {
        if (locale == null) {
            try {
                locale = new Locale(applicationParameters.getLocale());
            } catch (Exception e) {
                locale = MetConfiguration.DEFAULT_LOCALE;
                LogHelper.makeLog("Error retrieving locale, setting default locale. Error --> " + e.getMessage());
            }
        }
        return locale;
    }

    public String getNetwork() {

        if (UtilHelper.isStringEmpty(network)) {
            network = MetConfiguration.DEFAULT_NETWORK.toString();
        }
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public void refreshUserBaseCacheVariables() {
        this.canIeditProfile = null;
        this.canIbanUser = null;
        this.canIunbanUser = null;
        this.amIadministrator = null;
    }

    public void checkForNotificacions(ActionEvent event) {

        if (!MetConfiguration.APPLICATION_SCOPE_ACTIVE) {
            LogHelper.makeLog("Application Scope is inactive ...");
            return; //we are not using the applicacion scope
        }

        if (isAuthenticated()) {
            if (authenticatedUser == null) {
                authenticatedUser = this.getAuthenticaedUser();
            }

            System.out.println("... checking for Notificacions ... " + authenticatedUser.getId());

            String key = "__".concat(authenticatedUser.getId().toString()).concat("__");

            Object value = FacesUtils.getValueFromApplicationScope(key);
            if (value != null) {
                if (((String) value).equalsIgnoreCase("newMessage")) {
                    System.out.println("wwwe got a new Messageee!!!!!");
                    System.out.println("wwwe got a new Messageee!!!!!");
                    System.out.println("wwwe got a new Messageee!!!!!");
                    System.out.println("wwwe got a new Messageee!!!!!");
                    this.setNewMssage(true);
                    this.addFacesMsg("You have a new Message!!!");
                    FacesUtils.removeValueFromApplicationScope(key);
                }
            }

        } else {
            System.out.println("... checking for Notificacions ... ");
        }
    }

    public boolean isNewMssage() {
//        return newMssage;

        if (newMssage) {
            newMssage = false;
            return true;
        } else {
            return false;
        }

    }

    public void setNewMssage(boolean newMssage) {
        this.newMssage = newMssage;
    }

    public void notifyUserToRefreshFriendList(Integer userId) {

        System.out.println("... notification sent to user id: " + this.getAuthenticaedUser().getId() + " to refresh friends");

        FacesUtils.setValueInApplicationScope("__" + userId + "__", NotificationConst.REFRESH_FRIENDS);
    }
}
