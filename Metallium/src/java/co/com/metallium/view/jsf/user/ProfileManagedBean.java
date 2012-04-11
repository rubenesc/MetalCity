/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.user;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.UserCommon;
import co.com.metallium.core.constants.state.CommentState;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.entity.UserBan;
import co.com.metallium.core.entity.UserFriend;
import co.com.metallium.core.entity.UserWallComments;
import co.com.metallium.core.exception.CommentException;
import co.com.metallium.core.service.UserWallCommentsService;
import co.com.metallium.core.service.dto.search.UserWallCommentsSearchDTO;
import co.com.metallium.core.service.dto.search.UserSearchDTO;
import co.com.metallium.core.util.MetUtilHelper;
import co.com.metallium.view.jsf.components.FriendSearchComp;
import co.com.metallium.view.jsf.components.wall.UserWallComp;
import co.com.metallium.view.util.PaginationHelper;
import com.metallium.utils.framework.jsf.FacesUtils;
import com.metallium.utils.framework.utilities.LogHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * 20101101
 * @author Ruben
 */
@ManagedBean(name = "profileManagedBean")
@javax.faces.bean.ViewScoped
public class ProfileManagedBean extends ProfileExtManagedBean implements Serializable {

    private String profileNick = null;
    private Integer profileId = null; //It indicates which profile I must display
    private boolean profileExists; //Tells me if the profile Im searching for can be displayed. It could exist, but maybe I cant display it, becuase Its banned or something.
    private boolean showFriends = true; //Tells me if Im gonna show the friend list in the profile. By default its turned off.
    //====Start Pagination Comments=====================================//
    private String comment = "";
    private DataModel itemsComments = null; //These are user wall comments.
    private PaginationHelper pagination;
    @EJB
    private UserWallCommentsService service;
    private UserWallCommentsSearchDTO searchCriteria = new UserWallCommentsSearchDTO();
    private int countComments = 0;
    private boolean didItCountCommentsAlready = false;
    //====End Pagination Comments=============================================//
    //====Start Pagination Users Friends=====================================//
    private DataModel items2 = null;
    private PaginationHelper pagination2;
    private UserSearchDTO userSearchCriteria = null;
    private int countItems2 = 0;
    private boolean didItCountItemsAlready2 = false;
    //====End Pagination Users Friends=====================================================//
    //If im an administrator and I want to ban someone, then I use this object
    private UserBan userBan = new UserBan();
    //These are my CACHE VALUES
    private Boolean canIsearchUsersFriends = null;
    private Boolean canIWriteOnUserWall = null;
    private Boolean canIAddToFriends = null;
    private Boolean canIDeleteFromFriends = null;
    private Boolean doIHaveNotifications = null;
    @ManagedProperty(value = "#{friendSearchComp}")
    private FriendSearchComp friendSearchComp;

    @ManagedProperty(value = "#{userWallComp}")
    private UserWallComp userWallComp;

    /** Creates a new instance of ProfileManagedBean */
    public ProfileManagedBean() {
        LogHelper.makeLog("... ProfileManagedBean() ...");
    }


    /***
     * This method is called by Pretty Faces.
     * It returns a navigation command redirect the user to a new page
     * in case the information could not be loaded correctly. Otherwise it loads
     * the data requested and presents it in the page.
     *
     * @return String Navigation Page.
     */
    public String loadProfileByNick() {

        String answer = Navegation.PRETTY_HOME; //I assume the data is not going to be loaded correctly thus I'll send the requqest back to the home location.
        User userToFind = null;
        try {

            refreshCacheVariables();//If I atempt to find a new user, then I must calculate my variables.

            //First I verify that the nick entered is a valid set of characters (I dont want to look for strange garbage)
            if (MetUtilHelper.isUserNickValid(profileNick)) {
                userToFind = this.getUserService().findUserWithFriends(profileNick);
                initializeUser(userToFind);
                answer = Navegation.PRETTY_OK; //The user was loaded correctly so I allow him to go this profile page.
            }

        } catch (Exception ex) {
            //Well if the profile does not exist, then I dont store the profile Id.
            this.setProfileExists(false);
            manageException(ex);
        }

        return answer;
    }


    /**
     * This set Profile Nick is very Important, its the point of entry of the page.
     * it tells us which wall we are going to use.
     *
     * @param profileId
     */
    @Override
    public void setProfileNick(String profileNick) {
        this.profileNick = profileNick;
    }

    @Override
    public String getProfileNick() {
        return profileNick;
    }

    @Override
    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    @Override
    public Integer getProfileId() {
        return profileId;
    }

    /**
     * Since I found an active user to display, then in this method I
     * initialize all the variables to display the users information correctly
     */
    private void initializeUser(User userToFind) {
        this.setProfileExists(true);
        this.setProfileId(userToFind.getId());
        this.setUser(userToFind);
        this.userWallComp.initialize(userToFind.getId());
        this.friendSearchComp.initialize(userToFind.getId());

        //Now I initialize the wall information
        this.setSearchCriteria(new UserWallCommentsSearchDTO(userToFind.getId(), CommentState.ACTIVE)); //Profile owner of the WALL we are goint to interact with.
        this.setUserSearchCriteria(new UserSearchDTO(userToFind.getId())); //Id of the profile so we can search for the users friends.
        this.recreateCommentsModel();
        pagination = null; //THis is so, that the every time I find a new profile, the comments pagination start from the begining.
    }

    public boolean isProfileExists() {

        if (this.getProfileId() == null && this.getUser() == null) {
            if (this.isJsfMessageListEmpty()) {
                //If the msg list is not empty is because I already put the corresponding
                //msg I need to show, so this method could get called various times and I dont want to
                //show the msg repeatedly
                this.addFacesMsgFromProperties("profile_info_no_result_exception");
            }
            this.setProfileExists(false);
        } else {
            this.setProfileExists(true);
        }
        return this.profileExists;
    }

    public void setProfileExists(boolean profileExists) {
        this.profileExists = profileExists;
    }

    private void refreshCacheVariables() {
        this.canIsearchUsersFriends = null;
        this.canIWriteOnUserWall = null;
        this.canIAddToFriends = null;
        this.canIDeleteFromFriends = null;
        this.doIHaveNotifications = null;
        this.refreshUserBaseCacheVariables();
    }

    //==========================Start Pagination Comments=========================================//
    public void initializeComments(Integer profileId) {
        this.setProfileId(profileId);
        pagination = null;
        recreateCommentsModel();

    }

    public PaginationHelper getPagination() {
        if (profileId == null) {
            return null;
        }


        if (pagination == null) {
            pagination = new PaginationHelper(MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_USER_WALL_COMMENTS) {

                @Override
                public int getItemsCount() {

                    if (!didItCountCommentsAlready) {
                        countComments = service.countCommentsByUserWall(getSearchCriteria());
                        didItCountCommentsAlready = true;
                    }
                    return countComments;
                }

                @Override
                public DataModel createPageDataModel() {

                    int[] range = new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()};
                    List<UserWallComments> commentsFound = service.findUserWallCommentsByRange(range, getSearchCriteria());
                    return new ListDataModel(commentsFound);
                }
            };
        }
        return pagination;
    }

    public DataModel getItems() {
        if (itemsComments == null) {
            if (profileId != null) {
                itemsComments = getPagination().createPageDataModel();
            }
        }
        return itemsComments;
    }

    public String next() {
        getPagination().nextPage();
        recreateCommentsModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    public String previous() {
        getPagination().previousPage();
        recreateCommentsModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    private void recreateCommentsModel() {
        itemsComments = null;
        didItCountCommentsAlready = false;  //PLEASE REMEMBER that this is the only place where I can set this variable to TRUE. Otherwise it will result in multiple invocations to the count query.
    }

    public UserWallCommentsSearchDTO getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(UserWallCommentsSearchDTO searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    //==========================End Pagination Comments=========================================//
    //==========================Start Comments Logic=========================================//
    public String publishComment() {
        String answer = Navegation.stayInSamePage;

        this.validateComment();

        if (isJsfMessageListEmpty()) {
            try {
                User authenticatedUser = this.getAuthenticaedUser();
                UserWallComments newComment = new UserWallComments();
                newComment.setComment(this.getComment());
                newComment.setUserWallId(this.getProfileId()); //The user owner of the wall
                newComment.setUser(new User(authenticatedUser.getId())); //The user who wrote on the Wall
                this.service.create(newComment);
                this.didItCountCommentsAlready = false;
                this.recreateCommentsModel();
                getPagination();
                this.setComment(""); //Once I publish the comment I clean it up on the web page.
                this.addFacesMsgFromProperties("comment_added");
            } catch (Exception ex) {
                this.manageException(ex);
            }
        }

        return answer;
    }

    private void validateComment() {

        try {

            if (this.getProfileId() == null) {
                throw new Exception("... Error ... Users Wall Profile Id is NULL when trying to publish a comment.");
            } else {
                if (this.getComment() == null
                        || this.getComment().trim().length() < 1) {
                    addFacesMsgFromProperties("news_info_comment_required");
                }
            }
        } catch (Exception ex) {
            this.manageException(ex);
        }
    }

    public boolean isCanIdeleteComment() {
        boolean answer = false;

        try {
            UserWallComments auxComment = (UserWallComments) getItems().getRowData();
            answer = canIdeleteUserWallCommentLogic(auxComment);
        } catch (Exception e) {
            //I dont fucking care !!!
        }

        return answer;
    }

    public String deleteComment() {
        String answer = Navegation.stayInSamePage;

        try {
            UserWallComments auxComment = (UserWallComments) getItems().getRowData();
            //I verify again that it is a leagal operation
            if (canIdeleteUserWallCommentLogic(auxComment)) {
                this.service.deleteUserWallComment(auxComment);
                this.recreateCommentsModel();
                this.addFacesMsgFromProperties("comment_deleted");
            } else {
                throw new CommentException("", null, applicationParameters.getResourceBundleMessage("common_info_restricted_operation"));
            }
        } catch (Exception ex) {
            this.manageException(ex);
        }

        return answer;
    }

    //==========================End Comments Logic=========================================//
    //==========================Start General Logic=========================================//
    public boolean isCanISendMessage() {
        boolean answer = false;

        try {
            //I have to validate if the user is authenticated first.
            if (this.isAuthenticated()) {
                if (!this.isAmITheOwner(this.getUser().getId())) {
                    //So this means that this profile is not the profile
                    //of the authenticated user, so I can surely send the message.
                    answer = true;
                }
            }
        } catch (Exception e) {
            //I dont care
        }

        return answer;
    }

    private boolean doIHaveNotifications() {

        //if the variable is already set, then I look no more. I set it further on this method.
        if (doIHaveNotifications != null) {
            return doIHaveNotifications;
        }

        //if the profile doesnt exist, then I have no notifications.
        if (!this.profileExists) {
            doIHaveNotifications = false;
            return doIHaveNotifications;
        }

        //if I am not the owner of this profile, then notifications are not my business.
        if (!this.isAmITheOwner(this.getUser().getId())) {
            doIHaveNotifications = false;
            return doIHaveNotifications;
        }

        //If I get to this point, then I am the owner of the profile in question,
        //so I check if I have notifications or not.
        if (this.getUser().getNotifications()) {
            doIHaveNotifications = true;
//            this.addFacesMsg("You have a new Notification!!!");

            if (doIHaveNotifications) {
                //And since I do have a Notification, which I have just read, then I can
                //turn if off now. This means I indicate that I already read the message.
                int userId = this.getUser().getId();
                getUserService().userNotificationRead(userId);
                this.messageSetMessageRead(userId);
                this.refreshNotifications(userId);
            }

            return doIHaveNotifications;
        }


        //I should Never ever get to this point. But just in case the apocalypse happens, I make sure I dont have notifications.
        doIHaveNotifications = false;
        return doIHaveNotifications;

    }

    //==========================End General Logic=========================================//
    //==========================Start Friends Logic=========================================//
    public boolean isCanIWriteOnUserWall() {
        if (this.canIWriteOnUserWall == null) {
            try {
                this.canIWriteOnUserWall = canI_x_ToFriend(this.getUser().getId(), UserCommon.FRIEND_WRITE_ON_WALL);
            } catch (Exception e) {
                //I dont fucking care !!!
                this.canIWriteOnUserWall = false;
            }
        }
        return this.canIWriteOnUserWall;
    }

    public boolean isCanIsearchUsersFriends() {
        if (this.canIsearchUsersFriends == null) {
            try {
                canIsearchUsersFriends = canI_x_ToFriend(this.getProfileId(), UserCommon.FRIEND_VIEW_FRIEND_LIST);
            } catch (Exception e) {
                //I dont fucking care !!!

                //I return false, but never the less I dont mark the property as false,
                //because I want at least another time to see if I can get an answer an no not an exception.
                return false;
            }
        }
        return canIsearchUsersFriends;
    }

    public boolean isCanIAddToFriends() {

        if (this.canIAddToFriends == null) {
            try {
                canIAddToFriends = canI_x_ToFriend(this.getUser().getId(), UserCommon.FRIEND_ADD);
            } catch (Exception e) {
                //I dont fucking care !!!
                this.canIAddToFriends = false;
            }
        }
        return this.canIAddToFriends;
    }

    public boolean isCanIDeleteFromFriends() {

        if (this.canIDeleteFromFriends == null) {
            try {
                canIDeleteFromFriends = canI_x_ToFriend(this.getUser().getId(), UserCommon.FRIEND_DELETE);
            } catch (Exception e) {
                //I dont fucking care !!!
                this.canIDeleteFromFriends = false;
            }
        }
        return this.canIDeleteFromFriends;

    }

    public String getAddFriendConfirmMessage() {
        return applicationParameters.getResourceBundleMessage("profile_info_add_friend_confirmation", this.getUser().getName());
    }

    public String getDeleteFriendConfirmMessage() {
        return applicationParameters.getResourceBundleMessage("profile_info_delete_friend_confirmation", this.getUser().getName());
    }

    public void sendFriendRequest() {

        if (isAuthenticated()) {
            try {

                int idUser1 = this.getAuthenticaedUser().getId();     //Id user who sends the friend request
                int idUser2 = profileId;                              //Id user who confirms the friend request

                this.getUserService().sendFriendRequest(idUser1, idUser2);
                this.addFacesMsgFromProperties("profile_info_add_friend_request_sent");

            } catch (Exception e) {
                manageException(e);
            }
        }
    }

    public void deleteUserFromFriends() {

        if (isAuthenticated()) {
            try {
                int idUser1ThisIsMe = this.getAuthenticaedUser().getId();     //Id user who wants to delete someone
                int idUser2 = profileId;                              //Id user who gets deleted

                this.getUserService().deleteFriend(idUser1ThisIsMe, idUser2);
                this.addFacesMsgFromProperties("profile_info_delete_friend_successful");
                refreshUsersFriends(idUser1ThisIsMe); //If I delete a Friend I refresh the list in my user session, so he wont appear anymore.
                notifyUserToRefreshFriendList(idUser2); //I also notify the other user to refresh his friend list.
            } catch (Exception e) {
                manageException(e);
            }
        }
    }

    public int getFriendsSize() {
        int size = getFriends().size();
        return size;
    }

    public List<UserFriend> getFriends() {

        List<UserFriend> answer = null;
        try {
            answer = new ArrayList(this.getUser().getUserFriendsCollection());
        } catch (Exception e) {
            answer = new ArrayList<UserFriend>();
        }

        return answer;
    }

    /**
     * This is an action that turns on or off the Friend List of a profile.
     * Its supposed to be called from the friendsInfo.xhtml  page.
     * 
     */
    public void showUsersFriendList() {
        if (this.showFriends) {
            this.showFriends = false;
        } else {
            this.showFriends = true;
        }
    }

    //==========================End Friends Logic=========================================//
    //==========================Start Admin Logic=========================================//
    public void banUser() {

        if (isCanIbanUser()) {
            try {
                userBan.setBannedUserId(this.getUser().getId());
                userBan.setAdminId(this.getAuthenticaedUser().getId());
                userBan.setBanExpirationDate(MetUtilHelper.determineBanExpirationDate(userBan.getBanTime()));
                userBan.setDate(new Date());

                this.getUserService().banUser(userBan);
                this.loadProfileByNick(); //So now I just refesh the users stuff.

                this.addFacesMsgFromProperties("common_done");

            } catch (Exception e) {
                manageException(e);
            }
        }
    }

    public void unbanUser() {

        if (isCanIunbanUser()) {
            try {

                this.getUserService().unbanUser(this.getUser().getId());
                this.loadProfileByNick(); //So now I just refesh the users stuff.
                this.addFacesMsgFromProperties("common_done");

            } catch (Exception e) {
                manageException(e);
            }
        }
    }

    //==========================End Admin Logic=========================================//
    //====Start Pagination Users Friends=====================================//
    public PaginationHelper getPagination2() {
        if (profileId == null) {
            return null;
        }

        if (pagination2 == null) {
            pagination2 = new PaginationHelper(MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_GENERAL) {

                @Override
                public int getItemsCount() {

                    if (!didItCountItemsAlready2) {
                        //countItems2 = searchService.countQuery(getSearchCriteria());
                        didItCountItemsAlready2 = true;
                    }
                    return countItems2;
                }

                @Override
                public DataModel createPageDataModel() {

                    int[] range = new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()};
                    getSearchCriteria().setRange(range);
                    List<User> searchResult = null; //searchService.findQueryByRange(getSearchCriteria());
                    return new ListDataModel(searchResult);
                }
            };
        }
        return pagination2;
    }

    public DataModel getItems2() {
        if (items2 == null) {
            if (profileId != null) {
                didItCountItemsAlready2 = false;
                items2 = getPagination2().createPageDataModel();
            }
        }
        return items2;
    }

    public String next2() {
        getPagination2().nextPage();
        recreateItemsModel2();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    public String previous2() {
        getPagination2().previousPage();
        recreateItemsModel2();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    public void recreateItemsModel2() {
        items2 = null;
        didItCountItemsAlready2 = false;
    }

    public UserSearchDTO getUserSearchCriteria() {
        return userSearchCriteria;
    }

    public void setUserSearchCriteria(UserSearchDTO userSearchCriteria) {
        this.userSearchCriteria = userSearchCriteria;
    }

    //====End Pagination Users Friends=====================================//
    //==========================Getters and Setters=========================================//
    public UserBan getUserBan() {
        return userBan;
    }

    public void setUserBan(UserBan userBan) {
        this.userBan = userBan;
    }

    public boolean isShowFriends() {
        return showFriends;
    }

    public void setShowFriends(boolean showFriends) {
        this.showFriends = showFriends;
    }

    private void messageSetMessageRead(int userId) {
        if (MetConfiguration.APPLICATION_SCOPE_ACTIVE) {
            FacesUtils.removeValueFromApplicationScope("__".concat("" + userId).concat("__"));
        }
    }

    public FriendSearchComp getFriendSearchComp() {
        return friendSearchComp;
    }

    public void setFriendSearchComp(FriendSearchComp friendSearchComp) {
        this.friendSearchComp = friendSearchComp;
    }

    public UserWallComp getUserWallComp() {
        return userWallComp;
    }

    public void setUserWallComp(UserWallComp userWallComp) {
        this.userWallComp = userWallComp;
    }

}
