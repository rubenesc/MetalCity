package co.com.metallium.view.jsf.events;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.state.NewsState;
import co.com.metallium.core.entity.Event;
import co.com.metallium.core.service.SearchService;
import co.com.metallium.core.service.dto.search.EventSearchDTO;
import co.com.metallium.view.util.PaginationHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * 20110428
 * @author Ruben
 */
@ManagedBean(name = "eventControlRoom")
@ViewScoped
public class EventControlRoomManagedBean extends EventBaseManagedBean implements Serializable {

    private Event currentEvent;
    private int selectedItemIndex;
    private DataModel items = null;
    private EventSearchDTO criteria = new EventSearchDTO();
    private PaginationHelper pagination;
    private int count = 0;
    private boolean didItCountAlready = false;
    @EJB
    private SearchService searchService;

    /** Creates a new instance of EventControlRoomManagedBean */
    public EventControlRoomManagedBean() {
        //    criteria.setState(NewsState.PENDING); //If uncommented the "Event Control Room" page comes with Events in Pending state.
        // In the event control room I want to display the events in desending order. This way I override the default EventSearch order by
        criteria.setOrderBy(Event.entityOrderByQueryFragmentDesc);
    }

    public void valueChangeListenerNetwork(ValueChangeEvent vce) {
        if (!vce.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
            vce.setPhaseId(PhaseId.INVOKE_APPLICATION);
            vce.queue();
            return;
        }

        try {
            if (vce.getNewValue() != null) {
                this.criteria.setNetwork((Integer) vce.getNewValue());
            } else {
                this.criteria.setNetwork(null);
            }
        } catch (Exception e) {
            LogHelper.makeLog(e);
            this.criteria.setNetwork(null);
        }

        searchForEvents();

    }

    public void valueChangeListenerState(ValueChangeEvent vce) {

        if (!vce.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
            vce.setPhaseId(PhaseId.INVOKE_APPLICATION);
            vce.queue();
            return;
        }

        try {
            if (vce.getNewValue() != null) {
                this.criteria.setState(vce.getNewValue().toString());
            } else {
                this.criteria.setState("");
            }
        } catch (Exception e) {
            LogHelper.makeLog(e);
            this.criteria.setState("");
        }

        searchForEvents();
    }

    private void searchForEvents() {
        this.recreateNewsModel();
        pagination = null;
    }

    public Event getSelected() {
        if (currentEvent == null) {
            currentEvent = new Event();
            selectedItemIndex = -1;
        }
        return currentEvent;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_NEWS_CONTROL_ROOM) {

                @Override
                public int getItemsCount() {

                    if (!didItCountAlready) {
                        count = searchService.countQuery(criteria);
                        didItCountAlready = true;
                    }

                    return count;
                }

                @Override
                public DataModel createPageDataModel() {
                    int[] range = new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()};
                    criteria.setRange(range);
                    List<Event> searchResult = searchService.findQueryByRange(criteria);
                    return new ListDataModel(searchResult);
                }
            };
        }
        return pagination;
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    public String next() {
        getPagination().nextPage();
        recreateNewsModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    public String previous() {
        getPagination().previousPage();
        recreateNewsModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    private void recreateNewsModel() {
        items = null;
        didItCountAlready = false;
    }

    public EventSearchDTO getCriteria() {
        return criteria;
    }

    public void setCriteria(EventSearchDTO criteria) {
        this.criteria = criteria;
    }

    //======= Event Actions - Start These methods should be called from the eventActions1.xhtml =======
    public String activateEvent() {
        String answer = Navegation.stayInSamePage;

        try {
            currentEvent = (Event) getItems().getRowData();
            selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
            this.eventService.activateEvent(currentEvent);
            this.recreateNewsModel();
            this.addFacesMsgFromProperties("event_control_info_event_activated");
        } catch (Exception ex) {
            this.manageException(ex);
        }

        return answer;
    }

    public String deleteEvent() {
        String answer = Navegation.stayInSamePage;

        try {
            currentEvent = (Event) getItems().getRowData();
            selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
            this.eventService.deleteEvent(currentEvent);
            this.recreateNewsModel();
            this.addFacesMsgFromProperties("event_control_info_event_deleted");
        } catch (Exception ex) {
            this.manageException(ex);
        }

        return answer;
    }
    //======= Event Actions - End ========================================================
}
