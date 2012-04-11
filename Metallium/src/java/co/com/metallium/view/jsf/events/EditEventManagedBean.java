/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.events;

import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.entity.Event;
import co.com.metallium.core.exception.EventException;
import co.com.metallium.core.exception.GalleryException;
import co.com.metallium.core.service.ImageGalleryService;
import co.com.metallium.view.util.JsfUtil;
import co.com.metallium.view.util.JsfViewReturnPage;
import com.metallium.utils.dto.PairDTO;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.utils.UtilHelper;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.PhaseId;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * 20110430
 * @author Ruben
 */
@ManagedBean(name = "editEvent")
@ViewScoped
public class EditEventManagedBean extends EventBaseManagedBean implements Serializable {

    private Integer id;
    private String view; //This is just an aux variable to store from what page the request is coming to edit the event. This way we know where to send the user when he saves or cancels a request.
    private Event event = new Event();
    private boolean eventExists = false; //Tells me if the event Im searching for can be displayed. It could exist, but maybe I cant display it, becuase Its pending or something.
    @EJB
    ImageGalleryService imageGalleryService;
    private boolean editCoverImage = false; //Tells me if the user editing the a "Event" object uploaded a new image or not. I need to know in order to also update the image in the DB when he updates the news.
    private Integer eventId; //This is an aux variable to select an existing event id from whom we want to have there same cover image.

    /** Creates a new instance of EditEventManagedBean */
    public EditEventManagedBean() {
    }

    /**
     * This set Id is very Important, its the point of entry of the page.
     * it tells us which news we are going to find to display and edit
     *
     * @param profileId
     */
    public void setId(Integer id) {
        //I set the profile who's Id I get in the request.
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    /**
     * This set Id, should come as a parameter to the page. It actually indicates
     * to what page should you redirect the user when the request is completed.
     * Example: in = index
     *          ecr = event control room
     *
     * @param view
     */
    public void setView(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }


    /***
     * This method is called by Pretty Faces.
     * It returns a navigation command to indicate a new page to take the user
     * if the information could not be loaded correctly
     *
     * @return String Navigation Page.
     */
    public String findEvent() {

        String answer = Navegation.PRETTY_EVENT_CONTROL_ROOM; //I assume the data is not going to be loaded correctly thus I'll send the requqest back to the home location.

        if (id != null) {

            if (!eventExists) { //I make this validation so it only enters the ONE time! Once it finds the news then it wont enter again.
                Event eventToFind = null;
                try {
                    eventToFind = eventService.findEventById(id);
                    this.setEventExists(true);

                    answer = Navegation.PRETTY_OK; //The news was loaded correctly so I allow the user to go this news page.

                } catch (Exception ex) {
                    this.setEventExists(false);
                    this.manageException(ex);
                }
                this.setEvent(eventToFind);

            }
        }

        return answer;
    }

    public String performSearchEvent() {

        if (this.eventId == null) {
            this.addFacesMsgFromProperties("search_info_validate_write_something");
        }

        if (isJsfMessageListEmpty()) {
            try {
                Event auxEvent = this.eventService.findEventById(this.eventId);
                this.getEvent().setCoverImage(auxEvent.getCoverImage());
                this.getEvent().setCoverImageThumbnail(auxEvent.getCoverImageThumbnail());
                this.editCoverImage = false;
            } catch (Exception ex) {
                this.manageException(ex);
            }
        }

        return null;
    }

    public void handleFileUpload(FileUploadEvent event) {

        if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
            event.queue();
            return;
        }

        if (true) {
            //I can edit the profile

            try {

                UploadedFile uploadedFile = event.getFile();
                // Just to demonstrate what information you can get from the uploaded file.

                String contentType = uploadedFile.getContentType();
                String fileName = uploadedFile.getFileName(); // "image"
                long fileSize = uploadedFile.getSize();

                PairDTO pairDTO = this.imageGalleryService.uploadImageEvent(
                        fileName, contentType, fileSize, uploadedFile.getInputstream());

                this.getEvent().setCoverImage(pairDTO.getOne());
                this.getEvent().setCoverImageThumbnail(pairDTO.getTwo());
                this.editCoverImage = true;

                this.addFacesMsgFromProperties("image_gallery_info_uploaded_successfully", fileName);
            } catch (GalleryException ex) {
                this.manageException(ex);
            } catch (Exception e) {
                String x = applicationParameters.getResourceBundleMessage("common_info_file_upload_failed");
                this.manageException(new GalleryException(e.getMessage(), e, x));
            }


        } else {
            this.addFacesMsgFromProperties("common_info_restricted_access");
        }
    }

    public String createEvent() {
        view = JsfViewReturnPage.VIEW_EVENT_CONTROL_ROOM; //Whenever I create an Event Im sending him back to the Control Room.
        return executeEventUpdate(true);
    }

    public String editEvent() {
        return executeEventUpdate(false);
    }

    public String cancel() {
        return JsfViewReturnPage.getPage(view);
    }

    private String executeEventUpdate(boolean isCreate) {

        String answer = JsfViewReturnPage.getPage(view);
        this.validateEventUpdate();

        if (JsfUtil.getMessageListSize() == 0) {
            try {

                if (this.isAuthenticated()) {

                    if (isCreate) {
                        this.event.setUserCreated(this.getAuthenticaedUser().getId());
                        eventService.createEvent(event, editCoverImage);
                        this.addFacesMsgFromProperties("event_control_info_event_created");
                    } else {
                        event.setUserApproved(this.getAuthenticaedUser().getId()); //This is admin who is modifing the event.
                        eventService.editEvent(event, editCoverImage);
                        this.addFacesMsgFromProperties("common_info_save_success");
                    }

                } else {
                    throw new EventException("", null, applicationParameters.getResourceBundleMessage("common_info_user_must_be_authenticated"));
                }

            } catch (Exception ex) {
                this.manageException(ex);
                answer = Navegation.stayInSamePage;
            }


        } else {
            answer = Navegation.stayInSamePage;

        }

        return answer;
    }

    private void validateEventUpdate() {

        if (UtilHelper.isStringEmpty(this.getEvent().getCoverImage())) {
            this.addFacesMsgFromProperties("news_info_cover_image_required");
            return;
        }

        if (DataHelper.isNull(this.getEvent().getNetwork())) {
            this.addFacesMsgFromProperties("event_info_network_choose");
            return;
        }

        if (UtilHelper.isStringEmpty(this.getEvent().getTitle())) {
            this.addFacesMsgFromProperties("news_field_title");
            return;
        }

        if (DataHelper.isNull(this.getEvent().getEventDate())) {
            this.addFacesMsgFromProperties("event_field_event_date");
            return;
        }

        if (UtilHelper.isStringEmpty(this.getEvent().getLocation())) {
            this.addFacesMsgFromProperties("event_field_event_location");
            return;
        }

        if (UtilHelper.isStringEmpty(this.getEvent().getContent())) {
            this.addFacesMsgFromProperties("news_field_content");
            return;
        }

    }

    //============Getters and Setters=======================================//
    public boolean isEditCoverImage() {
        return editCoverImage;
    }

    public void setEditCoverImage(boolean editCoverImage) {
        this.editCoverImage = editCoverImage;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean isEventExists() {
        return eventExists;
    }

    public void setEventExists(boolean eventExists) {
        this.eventExists = eventExists;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

}
