/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.events;

import co.com.metallium.core.constants.CommentType;
import co.com.metallium.core.entity.Event;
import co.com.metallium.core.service.dto.search.EventSearchDTO;
import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.state.NewsState;
import co.com.metallium.core.service.EventService;
import co.com.metallium.core.service.SearchService;
import co.com.metallium.view.jsf.components.CommentsComp;
import co.com.metallium.view.util.PaginationHelper;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.NoResultException;

/**
 * 20110501
 * @author Ruben
 */
@ManagedBean(name = "event")
@ViewScoped
public class EventManagedBean extends EventBaseManagedBean implements Serializable {

    private String eventAlias = null;
    private Integer eventId = null;
    private boolean eventExists = false;
    private Event currentEvent;
    //Pagination Variables
    private DataModel itemsEvent = null;
    private PaginationHelper paginationEvent;
    private EventSearchDTO searchDTO = new EventSearchDTO();
    //These two parameters are to control the number of times the count command compareStates executed with each call.
    //so it optimizes it to call it just one time insted of 10 or more times.
    private int countEvent = 0;
    private boolean didItCountEventAlready = false;
    private List<Event> searchResult = null;
    @EJB
    private SearchService searchService;
    @ManagedProperty(value = "#{commentsComp}")
    private CommentsComp commentComp;
    //These are my CACHE VALUES
    private Boolean dislayEventMenu = null;

    public EventManagedBean() {
        initializeEventNetwork();
    }

    /**
     * This method is the Point of Entry for Pretty Faces.
     * @param eventId
     */
    public String getEventAlias() {
        return eventAlias;
    }

    public void setEventAlias(String eventAlias) {
        this.eventAlias = eventAlias;
    }

    /**
     * This method is the Point of Entry for the page EVENT.xhtml
     * @param eventId
     */
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getEventId() {
        return eventId;
    }

    /***
     * This method is called by Pretty Faces.
     * It returns a navigation command to indicate a new page to take the user
     * if the information could not be loaded correctly
     *
     * @return String Navigation Page.
     */
    public String loadEventByAlias() {

        String answer = Navegation.PRETTY_EVENT_LIST; //I assume the data is not going to be loaded correctly thus I'll send the requqest back to the home location.

        Event eventToFind = null;
        try {

            refreshCacheVariables();//If I atempt to call this page to display the events then I must recalculate my variables.

            eventToFind = this.getEventService().findEventByAlias(this.eventAlias);

            this.setEventId(eventToFind.getId());
            this.setEventExists(true);

            //Ok finaly after I looked up the event and I know it exists then I initialize the comments component.
            this.commentComp.initialize(CommentType.EVENT, eventId);

            answer = Navegation.PRETTY_OK; //The event was loaded correctly so I allow the user to go this news page.

        } catch (Exception ex) {
            if (ex.getCause() instanceof NoResultException) {
                this.addFacesMsgFromProperties("search_info_no_results");
                this.setEventExists(false);
            } else {
                this.manageException(ex);
                this.setEventExists(false);
            }
        }
        this.setCurrentEvent(eventToFind);

        return answer;
    }

    //=========================Start News Pagination Logic=====================//
    public void valueChangeListenerNetwork(ValueChangeEvent vce) {
        if (!vce.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
            vce.setPhaseId(PhaseId.INVOKE_APPLICATION);
            vce.queue();
            return;
        }

        try {
            if (vce.getNewValue() != null) {
                this.searchDTO.setNetwork((Integer) vce.getNewValue());
            } else {
                this.searchDTO.setNetwork(null);
            }
        } catch (Exception e) {
            LogHelper.makeLog(e);
            this.searchDTO.setNetwork(null);
        }

        this.recreateEventModel();
        paginationEvent = null;

    }

    /**
     * Searches for Events
     *
     * @return
     */
    public PaginationHelper getPaginationEvent() {
        if (paginationEvent == null) {
            paginationEvent = new PaginationHelper(MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_EVENTS) {

                @Override
                public int getItemsCount() {

                    if (!didItCountEventAlready) {
                        searchDTO.setState(NewsState.ACTIVE);
                        searchDTO.setDate1(new Date());

                        //count the number of active events to display in the index page
                        countEvent = searchService.countQuery(searchDTO);
                        didItCountEventAlready = true;
                    }

                    return countEvent;
                }

                @Override
                public DataModel createPageDataModel() {
                    int[] range = new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()};
                    searchDTO.setRange(range);
                    searchDTO.setState(NewsState.ACTIVE);
                    searchDTO.setDate1(new Date());

                    //find the active events to display in the index page
                    searchResult = searchService.findQueryByRange(searchDTO);
                    return new ListDataModel(searchResult);
                }
            };
        }
        return paginationEvent;
    }

    public DataModel getItemsEvent() {
        if (itemsEvent == null) {
            itemsEvent = getPaginationEvent().createPageDataModel();
        }
        return itemsEvent;
    }

    public String nextEvents() {
        getPaginationEvent().nextPage();
        recreateEventModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    public String previousEvents() {
        getPaginationEvent().previousPage();
        recreateEventModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    //=========================End Event Pagination Logic=====================//
    private void recreateEventModel() {
        itemsEvent = null;
        didItCountEventAlready = false;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public EventSearchDTO getSearchDTO() {
        return searchDTO;
    }

    public void setSearchDTO(EventSearchDTO searchDTO) {
        this.searchDTO = searchDTO;
    }

    private EventService getEventService() {
        return eventService;
    }

    public boolean isEventExists() {
        if (this.getEventId() == null) {
            if (this.isJsfMessageListEmpty()) {
                //If the msg list compareStates not empty compareStates because I already put the corresponding
                //msg I need to show, so this method could get called various times and I dont want to
                //show the msg repeatedly
                this.addFacesMsgFromProperties("news_info_no_result_exception");
            }
            this.setEventExists(false);

        }

        return this.eventExists;
    }

    public void setEventExists(boolean newsExists) {
        this.eventExists = newsExists;
    }

    /**
     * This method sets a Network depending of the user who is browsing the events section.
     * The idea is that if a user is logged in the system and decides to go to the NEWS section
     * then he can see first the NEWS regarding his network (Is the network is has configured in his profile)
     *
     * If the user is not logged in then the default network will be selected.
     */
    private void initializeEventNetwork() {
        if (DataHelper.isNull(searchDTO.getNetwork())) {
            //If no Network is set we have to see which network applies for this user

            this.searchDTO.setNetwork(obtainCurrentUserNetwork());

        }
    }

    public CommentsComp getCommentComp() {
        return commentComp;
    }

    public void setCommentComp(CommentsComp commentComp) {
        this.commentComp = commentComp;
    }

    //======= Event Actions - Start These methods should be called from the eventActions2.xhtml =======
    public String activateEvent() {
        String answer = Navegation.stayInSamePage;

        try {
            currentEvent = this.eventService.activateEvent(currentEvent);
            this.addFacesMsgFromProperties("event_control_info_event_activated");
        } catch (Exception ex) {
            this.manageException(ex);
        }

        return answer;
    }

    public String deleteEvent() {
        String answer = Navegation.stayInSamePage;

        try {
            this.eventService.deleteEvent(currentEvent);
            this.addFacesMsgFromProperties("event_control_info_event_deleted");
        } catch (Exception ex) {
            this.manageException(ex);
        }

        return answer;
    }
    //======= Event Actions - End ========================================================

    //deprecated.
    public boolean isDislayEventMenu() {
        if (this.dislayEventMenu == null) {

            if (isAuthenticated() && isMyProfileAdministrator1()) {
                this.dislayEventMenu = true; //I can see everythong
            } else {

                if (this.getPaginationEvent().getItemsCount() > 0) {
                    this.dislayEventMenu = true;
                } else {
                    //If I am not an Administrator, and there are no events, then I dont display the event menu
                    this.dislayEventMenu = false;
                }
            }
        }


        return this.dislayEventMenu;

    }

    private void refreshCacheVariables() {
        this.dislayEventMenu = null;
    }
}
