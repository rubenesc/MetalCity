/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.message;

import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.entity.Message;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.service.UserService;
import co.com.metallium.core.service.dto.search.UserFriendsSearchDTO;
import com.metallium.utils.utils.UtilHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import org.primefaces.event.SelectEvent;

/**
 * 20101230
 * @author Ruben
 */
@ManagedBean(name = "sendMessageFromBoxManagedBean")
@javax.faces.bean.ViewScoped
public class SendMessageFromBoxManagedBean extends SendMessageBaseManagedBean {

    @EJB
    private UserService userService;
    //-----------New Message
    List<User> usersFriends = null;
    List<String> names = null;
    private String toUserName;

    /** Creates a new instance of SendMessageFromBoxManagedBean */
    public SendMessageFromBoxManagedBean() {
    }

    @PostConstruct
    public void initializeSendMessage() {
        this.setSendMessage(new Message());
        try {

            Integer myId = this.getAuthenticaedUser().getId();
            usersFriends = userService.findUsersFriends(new UserFriendsSearchDTO(myId));

            names = new ArrayList<String>();
            for (Iterator<User> it = usersFriends.iterator(); it.hasNext();) {
                User user = it.next();
                names.add(user.getName());
            }

        } catch (Exception ex) {
            usersFriends = new ArrayList<User>();
            names = new ArrayList<String>();
            manageException(ex);
        }

    }

    public String sendMessageFromMessageBox() {

        return sendMessage();
    }

    private Message obtainMessageFromMessageBean() {
        MessageManagedBean message = this.findManagedBean("messageManagedBean", MessageManagedBean.class);
        return message.getMessage();
    }

    /**
     *
     * @return
     */
    private MessageBoxManagedBean obtainMessageBoxManagedBean() {
        MessageBoxManagedBean managedBean = this.findManagedBean("messageBoxManagedBean", MessageBoxManagedBean.class);
        return managedBean;
    }

    public String replyMessage() {

        String answer = Navegation.stayInSamePage;

        try {
            Message message = obtainMessageFromMessageBean();
            User toUser = obtainUserToReplyMessage(message);
            this.getSendMessage().setToUser(toUser);
            this.getSendMessage().setTitle(message.getTitle());

            answer = this.sendMessage();
        } catch (Exception e) {
            manageException(e);
        }

        return answer;

    }

    /**
     * Ok. If you are going to reply a Message What you usually do is
     * that you send it back to the User that sent the message to you which
     * is the "FROM User" that is in the message. You send it back. Easy.
     *
     * But, if you go the the Sent box. and you see the messages you Sent,
     * and you sent a message to User X, if you replay that message, the from
     * User is yourself, but we would like to send it to User X not to ourself.
     *
     * So. The To User works oppositely when you replay a message that is in the
     * InBox then on in the SentBox. So what I do in this method is just
     * Calculate the toUser depending on the box im in.
     *
     * @param message = message that Im going to reply.
     * @return User = User to which the message is going to be sent.
     */
    private User obtainUserToReplyMessage(Message message) {

        boolean isMessageFromSENTbox = false;

        if (obtainMessageBoxManagedBean().isCanISeeSent()) {
            //I am in the SentBox.
            isMessageFromSENTbox = true;
        }


        User toUser = null;
        if (isMessageFromSENTbox) {
            toUser = message.getToUser();
        } else {
            toUser = message.getFromUser();
        }

        return toUser;


    }

    //============================New Message From Box===============================//
    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public List<String> complete(String query) {
        List<String> suggestions = new ArrayList<String>();

        for (String name : names) {
            if (UtilHelper.startsWithIgnoreCase(name, query)) {
                suggestions.add(name);
            }
        }

        return suggestions;
    }

    public void handleSelect(SelectEvent event) {
        String nameToCheck = (String) event.getObject();

        User newUser = null;
        for (User p : usersFriends) {
            if (p.getName().equalsIgnoreCase(nameToCheck)) {
                newUser = p;
                break;
            }
        }

        if (newUser == null) {
            this.setToUserName("");
        }
        this.getSendMessage().setToUser(newUser);

    }
}
