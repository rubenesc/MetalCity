/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.user;

import co.com.metallium.core.entity.User;
import com.metallium.utils.framework.utilities.LogHelper;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

/**
 * 20110101
 * @author Ruben
 */
@ManagedBean
@javax.faces.bean.RequestScoped
public class BannedManagedBean extends UserBaseManagedBean {

    /** Creates a new instance of BannedManagedBean */
    public BannedManagedBean() {
    }

    @PostConstruct
    public void xxx() {

        if (this.isAuthenticated()) {
            User authenticated = this.getAuthenticaedUser();
            LogHelper.makeLog("The user with id: " + authenticated.getId() + ", is banned for bad behavior!!!");
            this.setUser(authenticated);

        } else {
            LogHelper.makeLog("Some annonymous user, is banned for bad behavior!!!");
            this.setUser(new User());
        }

        this.setUserLogInfo();

        LogHelper.makeLog("The users ip address is: " + this.getUser().getIpAddress() + ", session id: " + this.getUser().getSessionId());

        logOut();
    }

    /**
     * this is really nothing, just a method to initialize the bean
     * @return
     */
    public String getDoNothing(){
        return "";
    }


}
