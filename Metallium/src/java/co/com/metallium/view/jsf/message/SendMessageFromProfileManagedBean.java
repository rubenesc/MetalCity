/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.message;

import co.com.metallium.core.entity.Message;
import co.com.metallium.view.jsf.user.ProfileManagedBean;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import org.primefaces.event.CloseEvent;

/**
 * 20101230
 * @author Ruben
 */
@ManagedBean(name = "sendMessageFromProfileManagedBean")
@javax.faces.bean.ViewScoped
public class SendMessageFromProfileManagedBean extends SendMessageBaseManagedBean {

    private Integer toId;

    /** Creates a new instance of SendMessageFromProfileManagedBean */
    public SendMessageFromProfileManagedBean() {
    }

    @PostConstruct
    public void initializeSendMessage() {
        this.setSendMessage(new Message());
        initializeUserToFromProfile();
    }

    /**
     * Since we are supposed to be sending a message from a users profile (we are in the profile and
     * we click send message from there) then we get from that profile the user information from the
     * current session.
     * 
     *
     */
    private void initializeUserToFromProfile() {
        try {
            ProfileManagedBean profile = this.findManagedBean("profileManagedBean", ProfileManagedBean.class);

            if (DataHelper.isNotNull(profile) && profile.isProfileExists()) {
                this.getSendMessage().setToUser(profile.getUser());
                this.toId = profile.getUser().getId();
            }
        } catch (Exception e) {
            LogHelper.makeLog(e);
        }
    }

    //==========================Send a Message=========================================//
    /**
     * I can send a message if ... I am authenticated and I am not the owner of
     * the profile because it makes no sense to send a message to myself.
     *
     *
     */
    public boolean isCanISendMessage() {
        boolean answer = false;

        if (this.isAuthenticated()) {
            if (!this.isAmITheOwner(this.toId)) {
                //So this means that this profile is not the profile
                //of the authenticated user, so I can surely send the message.
                answer = true;
            }
        }

        return answer;
    }

    public void sendMessageFromProfile() {

        if (this.getSendMessage() != null
                && (this.getSendMessage().getToUser() == null || this.getSendMessage().getToUser().getId() == null)) {
            initializeUserToFromProfile();
        }

        if (!this.isCanISendMessage()) {
            this.addFacesMsgFromProperties("message_info_message_not_sent");
            this.cleanMessage();
            return;
        }

        sendMessage(); //Ok everything is validated, now we really send the message.
    }

    public void handleClose(CloseEvent event) {
        this.cleanMessage();
    }
}
