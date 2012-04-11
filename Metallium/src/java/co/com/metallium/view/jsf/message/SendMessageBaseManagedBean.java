/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.message;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.entity.Message;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.service.MessageService;
import co.com.metallium.view.jsf.BaseManagedBean;
import com.metallium.utils.framework.jsf.FacesUtils;
import com.metallium.utils.utils.UtilHelper;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Ruben
 */
@ManagedBean
@javax.faces.bean.ViewScoped
public class SendMessageBaseManagedBean extends BaseManagedBean {

    private Message sendMessage = new Message();
    @EJB
    private MessageService messageService;

    /** Creates a new instance of SendMessageBaseManagedBean */
    public SendMessageBaseManagedBean() {
    }

    public String cancelSendMessage() {
        this.cleanMessage();
        return Navegation.goToMessageBox;
    }

    public String sendMessage() {
        String answer = Navegation.stayInSamePage;

        this.validateSendMessageInput();

        if (isJsfMessageListEmpty()) {
            try {
                this.sendMessage.setFromUser(new User(this.getAuthenticaedUser().getId()));
                this.messageService.sendMessage(sendMessage); //I send the message ...
                this.messageNotifyToUserOfNewMessage(sendMessage.getToUser().getId());
                
                answer = Navegation.goToMessageBox;
                // answer = "messagebox".concat("?faces-redirect=true&amp;includeViewParams=true");
                this.addFacesMsgFromProperties("message_info_message_sent");
                cleanMessage();
            } catch (Exception e) {
                manageException(e);
            }
        }

        return answer;

    }

    public void cleanMessage() {
        if (this.sendMessage != null) {
            this.sendMessage.setTitle("");
            this.sendMessage.setMessage("");
        }
    }

    private void validateSendMessageInput() {

        if (this.getSendMessage() == null) {
            this.addFacesMsgFromProperties("message_info_validation_write_something");
            return;

        }
        
        if (UtilHelper.isStringEmpty(this.getSendMessage().getMessage())) {
            this.addFacesMsgFromProperties("message_info_validation_write_something");
            return;
        }        

        if (this.getSendMessage().getToUser() == null
                || this.getSendMessage().getToUser().getId() == null) {
            this.addFacesMsgFromProperties("message_info_validation_recipient");
            return;
        }

    }

    //================== Getters and Setters ==================================//
    public Message getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(Message sendMessage) {
        this.sendMessage = sendMessage;
    }

    private void messageNotifyToUserOfNewMessage(Integer idUser) {
        if (MetConfiguration.APPLICATION_SCOPE_ACTIVE) {
            FacesUtils.setValueInApplicationScope("__".concat("" + idUser).concat("__"), "newMessage");
            System.out.println("We just set a value in applicacion scope:  " + "__".concat("" + idUser).concat("__") + " : newMessage");
            System.out.println("this is what we set in " + "__".concat("" + idUser).concat("__") + " --> " + FacesUtils.getValueFromApplicationScope("__".concat("" + idUser).concat("__")));
        }
    }
}
