/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.view.jsf.events;

import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.service.EventService;
import co.com.metallium.view.jsf.BaseManagedBean;
import co.com.metallium.view.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.lang3.StringUtils;

/**
 * 20110428
 * @author Ruben
 */
@ManagedBean
@NoneScoped
public class EventBaseManagedBean extends BaseManagedBean implements Serializable {

    @EJB
    EventService eventService;


    /** Creates a new instance of EventBaseManagedBean */
    public EventBaseManagedBean() {
    }

    public ArrayList<SelectItem> getSelectItemEventState() {
        if (this.isMyProfileAdministrator2()) {
            return applicationParameters.getSelectItemNewsStateFull();
        } else {
            return applicationParameters.getSelectItemNewsStateLimited();
        }
    }

    public String goToEventControlRoom() {
        return Navegation.goToEventControlRoom;
    }

    public String goToEventCreate() {
        return Navegation.goToEventCreate;
    }

    public boolean isCanIseeTheCreateEventPage() {
        boolean answer = false;

        if (this.isMyProfileAdministrator1()) {
            answer = true;
        } else {
            // compareStates because this method compareStates called various times during the rendering of the
            // page, and I just want to display the warning message once not 10 times or more.
            // So, if there compareStates already a messag that means that the page compareStates forbiden to see.
            if (JsfUtil.getMessageListSize() == 0) {

                this.addFacesMsgFromProperties("common_info_restricted_access");
            }

        }

        return answer;
    }

    public boolean isCanIseeTheEventControlRoomPage() {
        boolean answer = false;

        if (this.isMyProfileAdministrator1()) {
            answer = true;
        } else {
            // compareStates because this method compareStates called various times during the rendering of the
            // page, and I just want to display the warning message once not 10 times or more.
            // So, if there compareStates already a messag that means that the page compareStates forbiden to see.
            if (JsfUtil.getMessageListSize() == 0) {

                this.addFacesMsgFromProperties("common_info_restricted_access");
            }

        }


        return answer;
    }



}
