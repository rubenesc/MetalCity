/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.filter;

import co.com.metallium.core.entity.User;
import co.com.metallium.view.jsf.user.LoginUserManagedBean;
import com.metallium.utils.framework.utilities.LogHelper;
import javax.el.ELContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 *
 * I just trimmed it and adapted it to my needs.
 * 20120214
 * @author Ruben
 * 
 */
public class AutoLoginPhaseListener implements PhaseListener {

    public void afterPhase(PhaseEvent event) {
        checkAutoLogin(event);
    }

    public void beforePhase(PhaseEvent event) {
//        checkAutoLogin(event);
    }

    private void checkAutoLogin(PhaseEvent event) {
        Boolean isAuth = (Boolean) event.getFacesContext().getExternalContext().getSessionMap().get("__PHASE_AUTH__");

        if (isAuth != null && isAuth) {

            try {
                ELContext elContext = FacesContext.getCurrentInstance().getELContext();
                LoginUserManagedBean neededBean = (LoginUserManagedBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "loginUserManagedBean");

                User user = (User) event.getFacesContext().getExternalContext().getSessionMap().get("__AUTHENTICATED_USER__");
                if (user != null) {
                    neededBean.logIn(user);
                }

                event.getFacesContext().getExternalContext().getSessionMap().put("__PHASE_AUTH__", null);
                LogHelper.makeLog("Phase --> " + this.getPhaseId());
            } catch (Exception e) {
                LogHelper.makeLog(e);
            }

        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
//        return PhaseId.RENDER_RESPONSE;
//        return PhaseId.ANY_PHASE;
    }
}
