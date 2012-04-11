/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.filter;

import com.metallium.utils.framework.utilities.LogHelper;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * 20101217
 * @author Ruben
 */
public class JsfPhaseListener implements PhaseListener {

    public void afterPhase(PhaseEvent event) {
    }

    public void beforePhase(PhaseEvent event) {
        if (event.getPhaseId() == PhaseId.RESTORE_VIEW) {
            if (event.getFacesContext().getExternalContext().getSessionMap().isEmpty()) {
                //navigate to the page you need with no rendering or ViewExpiredException problems
                //just dont forget to add any object to the SessionMap
                LogHelper.makeLog("JsfPhaseListener.beforePhase, and getSessionMap is empty");
            }
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
