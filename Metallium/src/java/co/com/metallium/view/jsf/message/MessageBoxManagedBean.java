/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.message;

import co.com.metallium.core.constants.MessageConst;
import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.entity.Message;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.service.MessageService;
import co.com.metallium.core.service.dto.search.MessageSearchDTO;
import co.com.metallium.view.jsf.BaseManagedBean;
import co.com.metallium.view.util.PaginationHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * 20100102
 * @author Ruben
 */
@ManagedBean(name = "messageBoxManagedBean")
@javax.faces.bean.ViewScoped
public class MessageBoxManagedBean extends BaseManagedBean {

    private String box = MessageConst.inbox;
    @EJB
    private MessageService messageService;
    private User user;
    private DataModel items = null; //These are user wall comments.
    private PaginationHelper pagination;
    private MessageSearchDTO searchCriteria = null;
    private int countItems = 0;
    private boolean didItCountItemsAlready = false;
    private Message selectedItem;
    public MessageConst messageConst;

    /** Creates a new instance of MessageBoxManagedBean */
    public MessageBoxManagedBean() {
    }

    @PostConstruct
    public void initializeInbox() {
        if (this.isAuthenticated()) {
            this.user = this.getAuthenticaedUser();
            searchCriteria = new MessageSearchDTO();
            searchCriteria.setToUser(this.user.getId());
            searchCriteria.setBox(MessageConst.inbox);  //I always initialize it to the default inbox
            pagination = null;
            recreateItemsModel();
            verifyMessageNotifications();
        }
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        if (searchCriteria != null) {
            this.box = box;
            this.searchCriteria.setBox(box);
            verifyMessageNotifications();
        }
    }

    private Boolean isUserTheSenderOfTheMessage() {
        Boolean isSender = null;
        if (isCanISeeInbox()) {
            isSender = false;
        } else if (isCanISeeSent()) {
            isSender = true;
        }
        return isSender;
    }

    /**
     * I verify if I have any message notifications.
     * I know if the parameter notifications of the user is turned on.
     * If its turned on, then I have to go and call a method to turn if off.
     *
     */
    private void verifyMessageNotifications() {

        //I know what Box Im in based on the SearchCriteria  (thats what is going to finally search in the DB)s
        if (MessageConst.inbox.equalsIgnoreCase(searchCriteria.getBox())) {
            if (this.user != null) {

                boolean doIHaveNewMessage = this.user.getNewMessage();

                if (doIHaveNewMessage) {
                    LogHelper.makeLog("User: " + this.user.getId() + ", has just read a New Message Notificacion");
                    this.messageService.userNewMessageRead(this.user.getId());
                    this.getAuthenticaedUser().setNewMessage(false); //I just read it.
                }
            }
        }

    }

    //=========================In Box============================================//
    public PaginationHelper getPagination() {
        if (user == null || user.getId() == null) {
            return null;
        }


        if (pagination == null) {
            pagination = new PaginationHelper(MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_USER_WALL_COMMENTS) {

                @Override
                public int getItemsCount() {

                    if (!didItCountItemsAlready) {
                        countItems = messageService.countUserBoxMessages(getSearchCriteria());
                        didItCountItemsAlready = true;
                    }
                    return countItems;
                }

                @Override
                public DataModel createPageDataModel() {

                    int[] range = new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()};
                    getSearchCriteria().setRange(range);
                    List<Message> searchResult = messageService.findUserBoxMessagesByRange(getSearchCriteria());
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
        didItCountItemsAlready = false; //This is important!
    }

    public MessageSearchDTO getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(MessageSearchDTO searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    //========================================================================//
    public String doSomething() {
        try {
            selectedItem = (Message) getItems().getRowData();
        } catch (Exception e) {
            LogHelper.makeLog(e);
        }
        return "jaja";
    }

    /**
     * This should delete a list of messages
     * @return
     */
    public String deleteMessages() {
        String answer = Navegation.stayInSamePage;

        try {

            Boolean isSender = isUserTheSenderOfTheMessage();
            Message messageToDelete = null;

            Iterator<Message> it = this.getItems().iterator();
            while (it.hasNext()) {
                messageToDelete = it.next();
                if (messageToDelete.isDelete()) {
                    if (isSender != null) {
                        if (messageToDelete != null) {
                            this.messageService.deleteMessage(messageToDelete.getId(), isSender);
                        }
                    }
                }
            }

            //After I finish deleting the messages do I refresh the data in the page.
            //The Next page is the galleries page, and Im resending the page in a request mode.
            this.recreateItemsModel();
            answer = "messagebox".concat("?faces-redirect=true&amp;includeViewParams=true");


        } catch (Exception e) {
            manageException(e);
        }

        return answer;
    }

    public String deleteMessage() {
        String answer = Navegation.stayInSamePage;

        try {

            Message messageToDelete = this.selectedItem;

            Boolean isSender = isUserTheSenderOfTheMessage();

            if (isSender != null) {
                if (messageToDelete != null) {
                    this.messageService.deleteMessage(messageToDelete.getId(), isSender);
                    this.recreateItemsModel();
                    answer = "messagebox".concat("?faces-redirect=true&amp;includeViewParams=true");
                }
            } else {
                LogHelper.makeLog("No isSender == null to delete Message userId: " + this.searchCriteria.getToUser());
            }

        } catch (Exception e) {
            manageException(e);
        }

        return answer;
    }

    //==========================Util ========================================//
    public boolean isCanISeeInbox() {
        //I am the recipient
        return MessageConst.isCanI(box, MessageConst.inbox);
    }

    public boolean isCanISeeSent() {
        //I am the sender
        return MessageConst.isCanI(box, MessageConst.sent);
    }

    public boolean isCanISeeNewMessage() {
        //I am sending a new message
        return MessageConst.isCanI(box, MessageConst.newmessage);
    }
}
