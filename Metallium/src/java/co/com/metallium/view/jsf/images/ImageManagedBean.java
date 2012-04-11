/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.images;

import co.com.metallium.view.jsf.components.CommentsComp;
import co.com.metallium.core.constants.CommentType;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.state.CommentState;
import co.com.metallium.core.entity.ImageGallery;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.service.dto.ImageDTO;
import co.com.metallium.core.service.dto.search.ImageGallerySearchDTO;
import co.com.metallium.core.util.MetUtilHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * 20101101
 * @author Ruben
 */
@ManagedBean(name = "imageManagedBean")
@javax.faces.bean.ViewScoped
public class ImageManagedBean extends ImageGalleryBaseManagedBean implements Serializable {

    private Integer galleryId; //It indates which gallery I must display
    private boolean galleryExists; //Tells me if the gallery Im searching for can be displayed. It could exist, but maybe I cant display it, becuase Its banned or something.
    private boolean galleryEmpty = true;
    private String imageName;
    private String imageLocation;
    //This is only used in image.xhtml and it is only to keep track of the
    //current image index of the gallery Im showing.
    private int currentImageIndex;
    //This is an auxiliar list that I use only in the image.xhtml page.
    //What it does is that it keeps a list of the names of all the images in the Gallery
    //so that I can find the next or previous image in a gallery when Im browsing image per image.
    private List<ImageDTO> galleryImages = new ArrayList<ImageDTO>();
    @ManagedProperty(value = "#{commentsComp}")
    private CommentsComp commentComp;

    public ImageManagedBean() {
    }

    /***
     * This method is Point of Entry of this Managed Bean
     * and it is invoked by Pretty Faces.
     *
     * It returns a navigation command to indicate a new page to take the user
     * if the information could not be loaded correctly
     *
     * --> The galleryId is part of the request params of image.xhtml
     *
     * --> The 'imageName' is also part of the request params of image.xhtml
     * so it should only be called when the page is invoked the first time
     * to checkout an image gallery, then its pure ajax, soooo
     * if I have to find the current Image index number to display if
     * not a Big Fat Zero is going to appear and thats not good jajaja.
     *
     *
     * @return String Navigation Page.
     */
    public String loadManagedBean() {
        String answer = Navegation.PRETTY_HOME; //I assume the data is not going to be loaded correctly thus I'll send the request back to the home location.

        if (getGalleryId() != null && !StringUtils.isEmpty(this.getImageName())) {
            try {

                //First I check if the gallery actually exits and if it can be seen.
                this.findGallery(galleryId);

                if (galleryExists) {

                    //The Gallery is accessible, so now I initialize the galleries elements, like
                    //where is are the galleries image location directory, and the galleries coments,
                    //and the number of images that the gallery has.

                    User userOnwerOfGallery = this.getImageGallery().getUser();
                    Integer userId = userOnwerOfGallery.getId();
                    this.setUser(userOnwerOfGallery);

                    //I get the user relative path in the filesystem --> users/user00/00/00/01/
                    String userFilePath = MetUtilHelper.getRelativeUserPath(userId);

                    //I get the gallery location in the filesystem example --> users/user00/00/00/01/ + galleries/gallery20101201190626M6g3
                    String galleryLocation = userFilePath.concat(this.getImageGallery().getDirectory());

                    ImageGallerySearchDTO criteria = new ImageGallerySearchDTO(galleryId, userId, galleryLocation, CommentState.ACTIVE);
                    this.setSearchCriteria(criteria); //Profile owner of the WALL we are goint to interact with.

                    this.findTheCurrentImageIndexNumber();
                    //Initialize Comments Component
                    this.commentComp.initialize(CommentType.IMAGE, this.getGalleryId(), this.getImageName());

                    answer = Navegation.PRETTY_OK;
                }

            } catch (Exception e) {
                manageException(e);
            }
        }

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

    public void deleteImage() {

        //Usually if its the first time I click on next image for a gallery I have to initialize the GalleryImages List.
        if ((this.getGalleryImages() == null || this.getGalleryImages().isEmpty())) {
            //This search method doesn't have a file range because it searches for all of the images in the gallery.
            List<ImageDTO> commentsFound = getImageGalleryService().findImageFilesByUserGallery(getSearchCriteria());
            setGalleryImages(commentsFound);
        }

        try {
            int imageIndex = this.getCurrentImageIndex() - 1;

            if (imageIndex >= 0) {
                ImageDTO image = this.getGalleryImages().get(imageIndex);

                Integer galleryId = this.getImageGallery().getId();
                short numberOfFiles = this.getImageGalleryService().deleteImageFromGallery(galleryId, image.getImageName());

                this.getImageGallery().setNumberPics(numberOfFiles);
                //Ok I just deleted the Image !!!

                this.getGalleryImages().remove(imageIndex);
                this.getCommentComp().removeComments(this.getGalleryId(), this.getImageName()); //If I remove the Image, then I also remove all its wall messages.

//                addFacesMsgFromProperties("image_gallery_info_image_deleted");
                this.setCurrentImageIndex(this.getCurrentImageIndex() - 1);
                this.findNextImage();

            }

        } catch (Exception ex) {
            this.manageException(ex);
        }

        verifyIfGalleryIsEmpty();

    }

    /**
     * This method should only be invoked in the image.xhtml.
     * Its in charge of finding the next image in the list of images.
     *
     *
     */
    public void findNextImage() {
        this.findNextImage(true);
        this.commentComp.setImageId(getImageName());
        this.commentComp.recreateCommentsModelImage();
        this.commentComp.getItemsComments();
    }

    public void findPreviousImage() {
        this.findNextImage(false);
        this.commentComp.setImageId(getImageName());
        this.commentComp.recreateCommentsModelImage();
        this.commentComp.getItemsComments();
    }

    /*
     * it finds the next or previous image in the gallery depending of
     * the boolean parameter.
     * True = finds next image
     * False = finds previous image
     *
     */
    private void findNextImage(boolean isNext) {
        long startTime = System.currentTimeMillis();
        long endTime;

        //Usually if its the first time I click on next image for a gallery I have to initialize the GalleryImages List.
        if ((this.getGalleryImages() == null || this.getGalleryImages().isEmpty())) {
            //This search method doesn't have a file range because it searches for all of the images in the gallery.
            List<ImageDTO> commentsFound = getImageGalleryService().findImageFilesByUserGallery(getSearchCriteria());
            setGalleryImages(commentsFound);
        }

        if (getGalleryImages().size() >= 1) {

            int imageIndex = this.getCurrentImageIndex() - 1;

            if (isNext) {
                //Then it searches next Image
                if (imageIndex >= this.getGalleryImages().size() - 1) {
                    imageIndex = 0; //well if it reached the end it should start again :P
                } else {
                    imageIndex++;
                }
            } else {
                //Then it searches previous Image
                if (imageIndex <= 0) {
                    imageIndex = this.getGalleryImages().size() - 1; //well we are in the start so we shall go to the last image :P
                } else {
                    imageIndex--;
                }
            }

            ImageDTO nextImage = this.getGalleryImages().get(imageIndex);
            this.setImageName(nextImage.getImageName());

            //Now that I got everything figured out I call the method
            //that sets up the image to show in the gallery.
            this.getImageLocation();
        }

        verifyIfGalleryIsEmpty();

        endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        LogHelper.makeLog("Time to find image: " + duration);
    }

    public List<ImageDTO> getGalleryImages() {
        return galleryImages;
    }

    public void setGalleryImages(List<ImageDTO> galleryImages) {
        this.galleryImages = galleryImages;
    }

    public boolean isGalleryEmpty() {
        return galleryEmpty;
    }

    public void setGalleryEmpty(boolean galleryEmpty) {
        this.galleryEmpty = galleryEmpty;
    }

    private void verifyIfGalleryIsEmpty() {
        if (getGalleryImages() != null && getGalleryImages().size() > 0) {
            this.setGalleryEmpty(false);
        } else {
            this.setGalleryEmpty(true);
        }
    }

    /**
     * This method is supposed to be used only by the image.xhtml page,
     * to get the servlet image location of the image to display, so I perform
     * the corresponding transformations to return the correct path so that
     * the image can be displayed.
     *
     * Remeber that this has to go something like
     * context + image servlet + user path + image directory + image name
     *
     */
    public String getImageLocation() {
        try {
            Integer userId = this.getImageGallery().getUser().getId();
            String galleryDirectory = this.getImageGallery().getDirectory();
            String imageName = this.getImageName();
            imageLocation = MetUtilHelper.getImageServletRelativePathToImage(userId, galleryDirectory, imageName);
        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }

        this.findTheCurrentImageIndexNumber();

        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    private void findTheCurrentImageIndexNumber() {

        //Usually if its the first time I click on next image for a gallery I have to initialize the GalleryImages List.
        if ((this.getGalleryImages() == null || this.getGalleryImages().isEmpty())) {
            //This search method doesn't have a file range because it searches for all of the images in the gallery.
            List<ImageDTO> images = getImageGalleryService().findImageFilesByUserGallery(getSearchCriteria());
            setGalleryImages(images);
        }

        int imageIndex = this.getGalleryImages().indexOf(new ImageDTO(this.getImageName()));
        this.setCurrentImageIndex(imageIndex + 1); //I increment it because the user doesnt want to see 0 insted 1, its for visual efects.
        verifyIfGalleryIsEmpty();

    }

    //==========================GETTERS & SETTERS============================//
    public CommentsComp getCommentComp() {
        return commentComp;
    }

    public void setCommentComp(CommentsComp commentComp) {
        this.commentComp = commentComp;
    }

    public void setGalleryId(Integer galleryId) {
        this.galleryId = galleryId;
    }

    public Integer getGalleryId() {
        return galleryId;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }

    public int getCurrentImageIndex() {
        return currentImageIndex;
    }

    public void setCurrentImageIndex(int currentImageIndex) {
        this.currentImageIndex = currentImageIndex;
    }
}
