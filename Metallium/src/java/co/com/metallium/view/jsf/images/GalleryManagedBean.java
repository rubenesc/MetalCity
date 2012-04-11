/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.images;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.state.CommentState;
import co.com.metallium.core.service.dto.ImageDTO;
import co.com.metallium.core.entity.ImageGallery;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.exception.GalleryException;
import co.com.metallium.core.service.dto.search.ImageGallerySearchDTO;
import co.com.metallium.core.util.ApplicationParameters;
import co.com.metallium.core.util.MetUtilHelper;
import co.com.metallium.view.util.PaginationHelper;
import com.metallium.utils.utils.UtilHelper;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.event.PhaseId;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * 20101122
 * @author Ruben
 */
@ManagedBean(name = "galleryManagedBean")
@javax.faces.bean.ViewScoped
public class GalleryManagedBean extends ImageGalleryBaseManagedBean implements Serializable {

    private Integer galleryId; //It indates which gallery I must display
    private boolean galleryExists; //Tells me if the gallery Im searching for can be displayed. It could exist, but maybe I cant display it, becuase Its banned or something.
    //=========start Image Pagination parameters
    private DataModel itemsAlbumCover = null; //These are the images items
    private DataModel items = null; //These are the images items
    private PaginationHelper pagination;
    private int countItems = 0;
    private boolean didItCountItemsAlready = false;
    //=========end Image Pagination parameters

    /** Creates a new instance of GalleryManagedBean */
    public GalleryManagedBean() {
    }

    /**
     * This set Profile Id is very Important, its the point of entry of the page.
     * it tells us which wall we are going to use.
     *
     * @param profileId
     */
    public void setGalleryId(Integer galleryId) {
        this.galleryId = galleryId;
    }

    public Integer getGalleryId() {
        return galleryId;
    }

    /***
     * This method is called by Pretty Faces.
     * It returns a navigation command to indicate a new page to take the user
     * if the information could not be loaded correctly
     *
     * @return String Navigation Page.
     */
    public String loadUserGallery() {

        String answer = Navegation.PRETTY_HOME; //I assume the data is not going to be loaded correctly thus I'll send the requqest back to the home location.

        if (galleryId != null) {

            refreshUserBaseCacheVariables();

            this.findGallery(galleryId);

            if (galleryExists) {
                User userOnwerOfGallery = this.getImageGallery().getUser();
                Integer userId = userOnwerOfGallery.getId();
                this.setUser(userOnwerOfGallery);
                this.setUserNick(userOnwerOfGallery.getNick());

                //I get the user relative path in the filesystem --> users/user00/00/00/01/
                String userFilePath = MetUtilHelper.getRelativeUserPath(userId);

                //I get the gallery location in the filesystem example --> users/user00/00/00/01/ + galleries/gallery20101201190626M6g3
                String galleryLocation = userFilePath + this.getImageGallery().getDirectory();

                ImageGallerySearchDTO criteria = new ImageGallerySearchDTO(galleryId, userId, galleryLocation, CommentState.ACTIVE);
                this.setSearchCriteria(criteria); //Profile owner of the WALL we are goint to interact with.
                this.recreateItemsModel();
                answer = Navegation.PRETTY_OK;
            }
        }

        return answer;

    }

    public String xxx() {
        String answer = Navegation.PRETTY_HOME; //I assume the data is not going to be loaded correctly thus I'll send the requqest back to the home location.

        return answer;
    }

    private void findGallery(int id) {

        ImageGallery galleryToFind = null;
        try {
            galleryToFind = this.getImageGalleryService().findImageGalleryById(id);
            boolean canIsee = this.canIseeThisGallery(galleryToFind.getState(), galleryToFind.getUser().getId());
            this.setGalleryExists(canIsee);
            this.setImageGallery(galleryToFind);
        } catch (Exception ex) {
            this.manageException(ex);
        }

    }

    public boolean isGalleryExists() {

        if (this.getGalleryId() == null) { //&& this.getUser() == null) {
            if (this.isJsfMessageListEmpty()) {
                //If the msg list is not empty is because I already put the corresponding
                //msg I need to show, so this method could get called various times and I dont want to
                //show the msg repeatedly
                this.addFacesMsgFromProperties("profile_info_no_result_exception");
            }
            this.setGalleryExists(false);
        }
        return this.galleryExists;
    }

    public void setGalleryExists(boolean galleryExists) {
        this.galleryExists = galleryExists;
    }

    //==========================Start Pagination=========================================//
    public PaginationHelper getPagination() {
        if (galleryId == null) {
            return null;
        }


        if (pagination == null) {
            pagination = new PaginationHelper(MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_IMAGE_GALLERY_PICS) {

                @Override
                public int getItemsCount() {

                    if (!didItCountItemsAlready) {
                        countItems = getImageGalleryService().countImageFilesByUserGallery(getSearchCriteria());
                        didItCountItemsAlready = true;
                    }
                    return countItems;
                }

                @Override
                public DataModel createPageDataModel() {

                    int[] range = new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()};
                    List<ImageDTO> list = getImageGalleryService().findImageFilesByUserGallery(range, getSearchCriteria());
                    return new ListDataModel(list);
                }
            };
        }
        return pagination;
    }

    public DataModel getItems() {
        if (items == null) {
            if (galleryId != null) {
                items = getPagination().createPageDataModel();
            }
        }
        return items;
    }

    public DataModel getItemsAlbumCover() {

        if (itemsAlbumCover == null) {
            if (galleryId != null) {
                List<ImageDTO> list = getImageGalleryService().findImageFilesByUserGallery(getSearchCriteria());
                itemsAlbumCover = new ListDataModel(list);
            }
        }
        return itemsAlbumCover;
    }

    public String next() {
        getPagination().nextPage();
        recreateItemsModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    public String previous() {
        getPagination().previousPage();
        recreateItemsModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    private void recreateItemsModel() {
        items = null;
        didItCountItemsAlready = false;
    }

    //==========================End Pagination=========================================//
    //==========================Start Image Upload ====================================//
    public String editImageGalleryBasicInfo() {
        String answer = Navegation.stayInSamePage; //I stay in the same page and show an error message

        //I can edit the profile - which means can I edid this Image Gallery
        if (isAmITheOwner(this.getImageGallery().getUser().getId())) {

            this.validateCreateImageGallery();

            if (isJsfMessageListEmpty()) {
                try {

                    verifyGalleryDirectoryAccuracy();

                    ImageGallery entity = this.getImageGalleryService().editImageGalleryBasicInfo(this.getImageGallery());
                    this.setImageGallery(entity);
                    //Once I persist the users new data, I set him, so when I go the
                    //the next page I can see his new data refreshed.

                    this.addFacesMsgFromProperties("common_info_changes_saved");
                } catch (Exception ex) {
                    this.manageException(ex);
                }
            }
        } else {
            this.addFacesMsgFromProperties("common_info_restricted_access");
        }

        return answer;
    }

    public void handleFileUpload(FileUploadEvent event) {

        if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
            event.queue();
            return;
        }

        if (isCanIeditProfile()) {
            //I can edit the profile

            try {

                UploadedFile uploadedFile = event.getFile();
                // Just to demonstrate what information you can get from the uploaded file.

                String contentType = uploadedFile.getContentType();
                String fileName = uploadedFile.getFileName(); // "image"
                long fileSize = uploadedFile.getSize();

                Integer userId = this.getUser().getId();
                Integer uploadGalleryId = this.getImageGallery().getId();
                String galleryDir = this.getImageGallery().getDirectory();

                short numberOfFiles = this.getImageGalleryService().uploadImage(userId, uploadGalleryId,
                        fileName, contentType, fileSize, galleryDir, uploadedFile.getInputstream());

                this.getImageGallery().setNumberPics(numberOfFiles);//I update the number of pics so I can show it on the web page.

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
    //==========================End Image Upload ====================================//

    public String chooseAlbumCoverImageGallery() {
        String answer = Navegation.stayInSamePage;

        try {
            ImageDTO image = (ImageDTO) getItemsAlbumCover().getRowData();
            String coverImage = this.getImageGalleryService().setGalleryCoverImage(this.getImageGallery().getId(), image.getImageName());
            this.getImageGallery().setCover(coverImage);
            this.addFacesMsgFromProperties("common_info_changes_saved");
        } catch (Exception ex) {
            this.manageException(ex);
        }

        return answer;
    }

    public String deleteImageGallery() {
        String answer = Navegation.stayInSamePage;


        //I verify well that I can delete the corresponding gallery.
        if (isAmITheOwner(this.getImageGallery().getUser().getId())) {
            //So I can delete the Gallery because Im the owner or because I have a profile which can.

            try {
                verifyGalleryDirectoryAccuracy();
                this.getImageGalleryService().deleteImageGallery(this.getImageGallery());
                String galleryTitle = this.getImageGallery().getTitle();
                this.setImageGallery(null);
                String displayMessage = applicationParameters.getResourceBundleMessage("image_gallery_info_deleted", galleryTitle);
                addFacesMsg(displayMessage);

                //The Next page is the galleries page, and Im resending the page in a request mode.
                answer = "galleries".concat("?faces-redirect=true&amp;includeViewParams=true");
                answer = Navegation.goToGalleriesRedirect;
            } catch (Exception ex) {
                this.manageException(ex);
            }

        } else {
            this.addFacesMsgFromProperties("common_info_restricted_access");
        }

        return answer;
    }

    /**
     * This is only to check that the Gallery Im deleting is a gallery.
     * What I mean is that the directory of the gallery is in something like
     *  galleries/gallery
     * and not something else like
     *  profile
     * because I cant modify the profile directory.
     *
     */
    private void verifyGalleryDirectoryAccuracy() throws GalleryException {

        try {
            String directory = this.getImageGallery().getDirectory();
            if (!MetUtilHelper.isDirectoyAGallery(directory)) {
                String msg = applicationParameters.getResourceBundleMessage("image_gallery_info_can_not_be_modified", this.getImageGallery().getTitle());
                throw new GalleryException(msg, null, msg);
            }
        } catch (GalleryException e) {
            throw e;
        } catch (Exception e) {
            throw new GalleryException(e.getMessage(), e);
        }

    }
}
