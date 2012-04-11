/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.message;

import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.entity.Message;
import co.com.metallium.core.service.MessageService;
import co.com.metallium.core.util.MetUtilHelper;
import co.com.metallium.view.jsf.BaseManagedBean;
import com.metallium.utils.utils.UtilHelper;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

/**
 * 20100103
 * @author Ruben
 */
@ManagedBean(name = "messageManagedBean")
@javax.faces.bean.ViewScoped
public class MessageManagedBean extends BaseManagedBean {

    @EJB
    private MessageService messageService;
    private Integer messageId;
    private Message message;
    private boolean messageExists = false;
    private boolean canIreplay = false; //I can replay a message, only if I didn't send it. It makes no sense replaying a message to myself.

    /** Creates a new instance of MessageManagedBean */
    public MessageManagedBean() {
    }

    /**
     * Point of entry
     *
     */
    public String loadMessageById() {

        String answer = Navegation.PRETTY_MESSAGE_BOX;

        if (this.getMessageId() != null) {

            if (this.isAuthenticated()) {

                //I verify that if I already found the messeage, then not to look it up again
                if (!this.messageExists) {
                    Message messageToFind = null;
                    try {
                        messageToFind = this.messageService.findMessageById(this.getMessageId());

                        //I found the message now I have to check if it belongs to the user
                        //that is authenticated. THIS Means I can see it only if it was SENT to me or if I sent it.
                        if (canISeeMessage(messageToFind)) {

                            this.setMessageExists(true);
                            this.setMessage(messageToFind);
                            verifyIfIcanReplayMessage(messageToFind.getFromUser().getId()); //Just checks if I display the replay button or not.
                            
                            answer = Navegation.PRETTY_OK; //Perfect the user can see the message
                        }
                    } catch (Exception ex) {
                        this.setMessageExists(false);
                        manageException(ex);
                    }
                }
            } else {
                answer = Navegation.PRETTY_HOME;
            }
        }

        return answer;
    }

    
    /**
     * Helper method that tells me if I am going to display the 'Replay message' button or not.
     * I don't display this button if I sent the message. Because it doesn't make sense to replay it
     * to myself.
     * 
     * 
     * @param messageToFind 
     */
    private void verifyIfIcanReplayMessage(Integer fromUserId) {
        
        boolean didIsendTheMessage = isAmITheOwner(fromUserId); //I compare the id of the user who sent the message with mine, (the authenticated user) and see if the are the same.
        if (didIsendTheMessage){
            this.setCanIreplay(false);
        } else {
            this.setCanIreplay(true);
        }
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    private boolean canISeeMessage(Message message) {
        boolean answer = false;

        int myId = this.getAuthenticaedUser().getId();

        if (UtilHelper.areIntegersEqual(myId, message.getToUser().getId())
                || UtilHelper.areIntegersEqual(myId, message.getFromUser().getId())) {
            //Yes I can see the message because either I sent it or it was sent to me.
            answer = true;
        }


        //Ok Now I know I can see the message Because either I sent it or I recived it.
        if (answer) {
            //Ok now I validte if I am the one who received the message.
            if (UtilHelper.areIntegersEqual(myId, message.getToUser().getId())) {
                //Ok sin Im the receiver, I check if I already READ the message or not.
                if (!MetUtilHelper.isMessageRead(message.getMessageRead())) {
                    //Since the message is NOT read, I will mark it as read.
                    this.messageService.readMessageById(message);
                }
            }
        }



        return answer;
    }

    public String deleteMessage() {
        String answer = Navegation.stayInSamePage;

        try {


            this.messageService.deleteMessage(this.getMessageId(), true);

            //The Next page is the galleries page, and Im resending the page in a request mode.

//            answer = Navegation.goToMessageBox;
            answer = "messagebox".concat("?faces-redirect=true&amp;includeViewParams=true");

        } catch (Exception e) {
            manageException(e);
        }

        return answer;
    }

    public String goBackToBox() {
        String answer = Navegation.stayInSamePage;

        try {
            answer = "messagebox".concat("?faces-redirect=true&amp;includeViewParams=true");
        } catch (Exception e) {
            manageException(e);
        }

        return answer;
    }

    //==================Setters and Getters============================//
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public boolean isMessageExists() {
        return messageExists;
    }

    public void setMessageExists(boolean messageExists) {
        this.messageExists = messageExists;
    }

    public boolean isCanIreplay() {
        return canIreplay;
    }

    public void setCanIreplay(boolean canIreplay) {
        this.canIreplay = canIreplay;
    }
    
}
