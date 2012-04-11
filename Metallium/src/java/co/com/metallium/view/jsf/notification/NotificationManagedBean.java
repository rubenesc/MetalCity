/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.notification;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.state.UserFriendState;
import co.com.metallium.core.entity.Notification;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.service.MessageService;
import co.com.metallium.core.service.NotificationService;
import co.com.metallium.core.service.UserService;
import co.com.metallium.core.service.dto.search.NotificationSearchDTO;
import co.com.metallium.core.service.dto.search.UserFriendsSearchDTO;
import co.com.metallium.view.jsf.user.UserBaseManagedBean;
import co.com.metallium.view.util.PaginationHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * 20110112
 * @author Ruben
 */
@ManagedBean(name = "notificationManagedBean")
@ViewScoped
public class NotificationManagedBean extends UserBaseManagedBean implements Serializable {

    @EJB
    private NotificationService notificationService;
    @EJB
    private UserService userService;
    @EJB
    private MessageService messageService;
    private User user;
    //====Start Pagination Notifications=====================================//
    private DataModel items = null; //These are user wall comments.
    private PaginationHelper pagination;
    private NotificationSearchDTO searchCriteria = null;
    private int countItems = 0;
    private boolean didItCountItemsAlready = false;
    //====End Pagination Notifications=====================================================//
    //====Start Pagination Users Friends=====================================//
    private DataModel items2 = null;
    private PaginationHelper pagination2;
    private UserFriendsSearchDTO searchCriteria2 = null;
    private int countItems2 = 0;
    private boolean didItCountItemsAlready2 = false;
    //====End Pagination Users Friends=====================================================//

    /** Creates a new instance of NotificationManagedBean */
    public NotificationManagedBean() {
    }

    @PostConstruct
    public void initialize() {
        if (this.isAuthenticated()) {
            this.user = this.getAuthenticaedUser();

            //Notificacion Variable Initialization
            searchCriteria = new NotificationSearchDTO();
            searchCriteria.setIdUserTo(this.user.getId());
            pagination = null;
            recreateItemsModel();

            //Friend Confirmation Variable Initialization
            searchCriteria2 = new UserFriendsSearchDTO(user.getId(), UserFriendState.FRIENDSHIP_PENDING_APPROVAL);
            pagination2 = null;
            recreateItemsModel2();
            verifyNotifications();
        }
    }

    private void verifyNotifications() {

        //I know what Box Im in based on the SearchCriteria  (thats what is going to finally search in the DB)s
        if (this.user != null) {

            boolean doIHaveNotifications = this.user.getNotifications();

            if (doIHaveNotifications) {
                LogHelper.makeLog("User: " + this.user.getId() + ", has just read a New Notificacion");
                this.messageService.messageNotificationRead(this.user.getId());
                this.getAuthenticaedUser().setNotifications(false); //I just read it.
            }
        }
    }

    //=========================Start Friend Request Logic============================================//
    public void confirmFriendRequest() {

        if (isAuthenticated()) {
            try {

                User user1 = (User) this.getItems2().getRowData();
                int idUser1 = user1.getId();                          //Id user who sends the friend request
                int idUser2ThisIsMe = this.getAuthenticaedUser().getId();     //Id user who confirms the friend request
                this.getUserService().confirmFriendRequest(idUser1, idUser2ThisIsMe);
                refreshUsersFriends(idUser2ThisIsMe); //If I added a new Friend I refresh the list in my user session.
                notifyUserToRefreshFriendList(idUser1); //I also notify the other user to refresh his friend list.
            } catch (Exception e) {
                manageException(e);
            }
        }

        this.recreateItemsModel2();
    }

    public void deleteFriendRequest() {

        if (isAuthenticated()) {
            try {

                User user1 = (User) this.getItems2().getRowData();

                int idUser1 = this.getAuthenticaedUser().getId();     //Id user who wants to delete someone
                int idUser2 = user1.getId();                          //Id user who gets deleted
                this.getUserService().deleteFriend(idUser1, idUser2);
            } catch (Exception e) {
                manageException(e);
            }
        }

        this.recreateItemsModel2();

    }

    //=========================End Friend Request Logic============================================//
    //=========================In Box============================================//
    public PaginationHelper getPagination() {
        if (user == null || user.getId() == null) {
            return null;
        }


        if (pagination == null) {
            pagination = new PaginationHelper(MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_USER_NOTIFICATIONS) {

                @Override
                public int getItemsCount() {

                    if (!didItCountItemsAlready) {
                        countItems = notificationService.countQuery(getSearchCriteria());
                        didItCountItemsAlready = true;
                    }
                    return countItems;
                }

                @Override
                public DataModel createPageDataModel() {

                    int[] range = new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()};
                    getSearchCriteria().setRange(range);
                    List<Notification> searchResult = notificationService.findQueryByRange(getSearchCriteria());
                    return new ListDataModel(searchResult);
                }
            };
        }
        return pagination;
    }

    public DataModel getItems() {
        if (items == null) {
            if (user != null && user.getId() != null) {
                items = getPagination().createPageDataModel();
            }
        }
        return items;
    }

    public String next() {
        getPagination().nextPage();
        recreateItemsModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    public String previous() {
        getPagination().previousPage();
        recreateItemsModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    public void recreateItemsModel() {
        items = null;
        didItCountItemsAlready = false;
    }

    public NotificationSearchDTO getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(NotificationSearchDTO searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    //========================================================================//
    public PaginationHelper getPagination2() {
        if (user == null || user.getId() == null) {
            return null;
        }

        if (pagination2 == null) {
            pagination2 = new PaginationHelper(MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_GENERAL) {

                @Override
                public int getItemsCount() {

                    if (!didItCountItemsAlready2) {
                        try {
                            countItems2 = userService.countFriendsByState(getSearchCriteria2());
                        } catch (Exception e) {
                            //I cant do shit
                            countItems2 = 0;
                            LogHelper.makeLog(e);
                        }
                        didItCountItemsAlready2 = true;
                    }
                    return countItems2;
                }

                @Override
                public DataModel createPageDataModel() {

                    int[] range = new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()};
                    getSearchCriteria().setRange(range);
                    List<User> searchResult = userService.findUsersFriends(getSearchCriteria2());
                    return new ListDataModel(searchResult);
                }
            };
        }
        return pagination2;
    }

    public DataModel getItems2() {
        if (items2 == null) {
            if (user != null && user.getId() != null) {
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

    public UserFriendsSearchDTO getSearchCriteria2() {
        return searchCriteria2;
    }

    public void setSearchCriteria2(UserFriendsSearchDTO searchCriteria2) {
        this.searchCriteria2 = searchCriteria2;
    }
    //==========================Comments=========================================//
}
