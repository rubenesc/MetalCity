/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import co.com.metallium.core.constants.state.NewsState;
import co.com.metallium.core.constants.state.UserProfile;
import co.com.metallium.core.entity.Event;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.exception.EventException;
import co.com.metallium.core.util.ApplicationParameters;
import co.com.metallium.core.util.MetUtilHelper;
import com.metallium.utils.dto.PairDTO;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import com.metallium.utils.utils.htmlscriptvalidator.HtmlSanitizer;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * 20110430
 * @author Ruben
 */
@Stateless
public class EventService extends BaseService {

    @PersistenceContext(unitName = "MetalliumPU")
    private EntityManager em;
    @EJB
    private UserService userService;
    @EJB
    ImageGalleryService imageGalleryService;
    @EJB
    private GeneralService generalService;

    public EventService() {
    }

    public void edit(Event entity) {
        em.merge(entity);
    }

    /**
     * Creates a new Event.
     *
     * @param event = contains the event to create with the information to persist.
     * @param uploadImage = if TRUE then it means we uploaded a new image so we have to process it (copy from temp dir to the new event dir)
     *                      if FALSE then it means we copied an image from another EVENT so we don't have to process it.
     * @return
     * @throws EventException
     */
    public Event createEvent(Event event, boolean uploadImage) throws EventException {

        User userEditing = null;
        try {
            userEditing = this.userService.findActiveUserById(event.getUserCreated());
        } catch (Exception e) {
            throw new EventException(e.getMessage(), e);
        }

        try {
            Integer profile = userEditing.getProfile();

            if (UserProfile.isAdministrator1(profile)) {

                event.setContent(HtmlSanitizer.admin(event.getContent())); //Clean HTML input.
                validateNewsEventLength(event.getContent());

                if (uploadImage) {
                    //It means that I uploaded an image so I have to process it.
                    //If it doesnt enter here then it means that I copied the image from another event so that image location already comes in the Event entity.
                    PairDTO pairDTO = imageGalleryService.processEventUploadedImages(event.getCoverImage());
                    event.setCoverImage(pairDTO.getOne());
                    event.setCoverImageThumbnail(pairDTO.getTwo());
                }

                String firstParagraph = DataHelper.getNewsPreviewString(event.getContent());
                event.setPreview(firstParagraph);
                event.setState(NewsState.PENDING);
                event.setDate(new Date());
                event.setNumberComments(0);

                String titleAlias = this.generalService.generateAliasForTitle(event.getTitle(), Event.namedQueryCountAlias, true, null);
                event.setAlias(titleAlias);
                em.persist(event);
            } else {
                throw new EventException("User with id: " + userEditing.getId() + ", and Profile: " + userEditing.getProfile() + ", can't Create an Event!!!", null, applicationParameters.getResourceBundleMessage("common_info_fuck_you"));
            }

        } catch (EventException e) {
            throw e;
        } catch (Exception e) {
            throw new EventException(e.getMessage(), e);
        }

        return event;
    }

    public Event editEvent(Event event, boolean editCoverImage) throws EventException {

        Event eventToEdit = this.findEventById(event.getId());

        User userEditing = null;
        try {
            userEditing = this.userService.findActiveUserById(event.getUserApproved());
        } catch (Exception e) {
            throw new EventException(e.getMessage(), e);
        }

        try {

            Integer profile = userEditing.getProfile();

            if (UserProfile.isAdministrator1(profile)) {

                event.setContent(HtmlSanitizer.admin(event.getContent())); //Clean HTML input.
                validateNewsEventLength(event.getContent());

                if (editCoverImage) {
                    PairDTO pairDTO = imageGalleryService.processOverwriteUploadedImage(event.getCoverImage(), eventToEdit.getCoverImage());
                    eventToEdit.setCoverImage(pairDTO.getOne());
                    eventToEdit.setCoverImageThumbnail(pairDTO.getTwo());
                } else {
                    //This means that either the image wasn't edited or that we are going to copy
                    //the image from another event.
                    eventToEdit.setCoverImage(event.getCoverImage());
                    eventToEdit.setCoverImageThumbnail(event.getCoverImageThumbnail());
                }

                eventToEdit.setNetwork(event.getNetwork());
                eventToEdit.setState(event.getState());
                String firstParagraph = DataHelper.getNewsPreviewString(event.getContent());
                String firstParagraphWithNoImages = HtmlSanitizer.admin(firstParagraph);
                eventToEdit.setPreview(firstParagraphWithNoImages);
                eventToEdit.setMediaUrl(event.getMediaUrl());

                if (UtilHelper.areStringsNotEqual(eventToEdit.getTitle(), event.getTitle().trim())) {
                    //Only if the Title was modified do I set the new title and generate
                    //a new alias.
                    eventToEdit.setTitle(event.getTitle().trim());

                    String titleAlias = this.generalService.generateAliasForTitle(event.getTitle(), Event.namedQueryCountAlias, false, eventToEdit.getId());
                    eventToEdit.setAlias(titleAlias);
                }

                eventToEdit.setEventDate(event.getEventDate());
                eventToEdit.setLocation(event.getLocation());
                eventToEdit.setContent(event.getContent());
                eventToEdit.setUserApproved(userEditing.getId());
                em.merge(eventToEdit);
                em.flush();

            } else {
                throw new EventException("User with id: " + userEditing.getId() + ", and Profile: " + userEditing.getProfile() + ", cant Edit an Event!!!", null, applicationParameters.getResourceBundleMessage("common_info_fuck_you"));
            }

        } catch (EventException e) {
            throw e;
        } catch (Exception e) {
            throw new EventException(e.getMessage(), e);
        }

        return event;
    }

    public Event findEventByAlias(String alias) throws EventException {
        Event event = null;

        try {
            event = (Event) em.createNamedQuery("Event.findByAlias").setParameter("alias", alias).getSingleResult();
        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg = "The event with alias: " + alias + ", could not be found in the system.";
                throw new EventException(msg, ex, applicationParameters.getResourceBundleMessage("search_info_no_results"));
            } else {
                throw new EventException(ex.getMessage(), ex);
            }
        }
        return event;
    }

    public Event findEventById(int id) throws EventException {
        Event event = null;

        try {
            event = (Event) em.createNamedQuery("Event.findById").setParameter("id", id).getSingleResult();
        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg = "The event with id: " + id + ", could not be found in the system.";
                throw new EventException(msg, ex, applicationParameters.getResourceBundleMessage("search_info_no_results"));
            } else {
                throw new EventException(ex.getMessage(), ex);
            }
        }
        return event;
    }

    public void deleteEvent(Event entity) throws EventException {

        try {

            if (NewsState.isActive(entity.getState()) || NewsState.isPending(entity.getState())) {
                entity.setState(NewsState.DELETED);
                em.merge(entity);
            } else {
                LogHelper.makeLog("... Cant delete event because it's state its: " + entity.getState());
            }

        } catch (Exception e) {
            throw new EventException(e.getMessage(), e);
        }

    }

    public Event activateEvent(Event entity) throws EventException {


        Event eventToActivate = this.findEventById(entity.getId());
        if (eventToActivate.getNetwork() == null) {
            String msg = applicationParameters.getResourceBundleMessage("event_control_info_network_required");
            throw new EventException(msg, null, msg);
        }

        try {

            if (NewsState.isDeleted(entity.getState())
                    || NewsState.isInactive(entity.getState())
                    || NewsState.isPending(entity.getState())) {
                eventToActivate.setState(NewsState.ACTIVE);
                em.merge(eventToActivate);
            } else {
                LogHelper.makeLog("... Cant activate event because it's state is: " + entity.getState());
            }

        } catch (Exception e) {
            throw new EventException(e.getMessage(), e);
        }
        return eventToActivate;

    }

    public void updateNumberComments(Integer id, int numberOfComments) throws EventException {
        Event entityToUpdate = this.findEventById(id);
        entityToUpdate.setNumberComments(numberOfComments);
        this.edit(entityToUpdate);
    }
}
