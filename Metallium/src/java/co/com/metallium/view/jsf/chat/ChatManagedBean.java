/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.chat;

import co.com.metallium.core.entity.User;
import co.com.metallium.core.service.UserService;
import co.com.metallium.view.jsf.BaseManagedBean;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
//import org.icefaces.application.PushRenderer;

/**
 * 20110104
 * @author Ruben
 */
@ManagedBean(name = "chatManagedBean")
@javax.faces.bean.ViewScoped
public class ChatManagedBean extends BaseManagedBean {

    private Integer toUserId;
    private User toUser;
    private User fromUser;
    private boolean initialized = false;
    @EJB
    private UserService userService;
    private static final String PUSH_GROUP = "colorPage";

    /** Creates a new instance of ChatManagedBean */
    public ChatManagedBean() {
    }

    public void setToUserId(Integer toUserId) {
        if (!initialized) {
        //    PushRenderer.addCurrentSession(PUSH_GROUP);
            this.toUserId = toUserId;
            initialize();
            initialized = true;
        }
    }

    private void initialize() {

        try {
            fromUser = this.getAuthenticaedUser();
            toUser = this.userService.findUserById(toUserId);
        } catch (Exception e) {
            manageException(e);
        }
//        PushRenderer.render(PUSH_GROUP);

    }

    //========getters and setters ============================================//
    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public Integer getToUserId() {
        return toUserId;
    }
}
