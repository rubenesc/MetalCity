/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.images;

import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.state.GalleryState;
import co.com.metallium.core.entity.ImageGallery;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.service.ImageGalleryService;
import co.com.metallium.core.service.dto.search.ImageGallerySearchDTO;
import co.com.metallium.core.util.ApplicationParameters;
import co.com.metallium.core.util.MetUtilHelper;
import co.com.metallium.view.jsf.user.UserBaseManagedBean;
import com.metallium.utils.dto.PairDTO;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import java.io.Serializable;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;

/**
 * 20101124
 * @author Ruben
 */
@ManagedBean(name = "imageGalleryBase")
@NoneScoped
public class ImageGalleryBaseManagedBean extends UserBaseManagedBean implements Serializable {

    private String userNick; //It indicates which gallery I must display
    private Integer userId; //It indicates which gallery I must display
    private boolean amItheOwnerOfTheGallery = false;
    private ImageGallery imageGallery = new ImageGallery();
    private ImageGallerySearchDTO searchCriteria = new ImageGallerySearchDTO();
    @EJB
    private ImageGalleryService imageGalleryService;

    /** Creates a new instance of ImageGalleryBaseManagedBean */
    public ImageGalleryBaseManagedBean() {
    }


    public void findUser(String nick) {

        User userToFind = null;
        try {
            refreshCacheVariables();
            userToFind = this.getUserService().findUserByNick(nick);
            setUserId(userToFind.getId());
        } catch (Exception ex) {
            manageException(ex);
        }
        this.setUser(userToFind);
    }

//    public void findUser(Integer id) {
//
//        User userToFind = null;
//        try {
//            refreshCacheVariables();
//            userToFind = this.getUserService().findUserById(id);
//            setUserId(userToFind.getId());
//        } catch (Exception ex) {
//            manageException(ex);
//        }
//        this.setUser(userToFind);
//    }

    private void refreshCacheVariables() {
        this.refreshUserBaseCacheVariables();
    }

    /**
     * I validate the input to create the Gallery
     *
     */
    public void validateCreateImageGallery() {

        if (UtilHelper.isStringEmpty(this.getImageGallery().getTitle())) {
            this.addFacesMsgFromProperties("image_gallery_requierd_title");
        }

    }

    public boolean isThisAnImageGallery() {
        boolean answer = false;

        try {
            if (MetUtilHelper.isDirectoyAGallery(this.getImageGallery().getDirectory())) {
                answer = true;
            }
        } catch (Exception e) {
            LogHelper.makeLog(e);
        }


        return answer;
    }

    /**
     *
     * I suppose an image gallery can be seen by
     * a user if
     * 1. The Gallery is active
     * 2. The user is the owner and it
     * 3. An administrator of high rank.
     *
     */
    public boolean canIseeThisGallery(String state, Integer userId) {
        boolean answer = false;

        //If its NOT!!!! Private then ... you can see it.
        answer = !GalleryState.isPrivate(state);

        if (!answer) {
            answer = this.isAmITheOwner(userId);
        }

        return answer;
    }

    public String goToGalleries() {
        return Navegation.goToGalleries;
    }

    public String goToGallery() {
        return Navegation.goToGallery;
    }

    public String goToCreateGallery() {
//        return Navegation.goToCreateGallery;
        return Navegation.goToCreateGalleryRedirect;
    }

    public String goToUploadImagesToGallery() {
        return Navegation.goToUploadImagesToGallery;
    }

    public ImageGallery getImageGallery() {
        return imageGallery;
    }

    public void setImageGallery(ImageGallery imageGallery) {
        this.imageGallery = imageGallery;
    }

    public boolean isAmItheOwnerOfTheGallery() {
        return amItheOwnerOfTheGallery;
    }

    public void setAmItheOwnerOfTheGallery(boolean amItheOwnerOfTheGallery) {
        this.amItheOwnerOfTheGallery = amItheOwnerOfTheGallery;
    }

    public ImageGalleryService getImageGalleryService() {
        return imageGalleryService;
    }

    public void setImageGalleryService(ImageGalleryService imageGalleryService) {
        this.imageGalleryService = imageGalleryService;
    }

    public ImageGallerySearchDTO getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(ImageGallerySearchDTO searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public ArrayList<PairDTO> getSelectItemVisibility() {
        return ApplicationParameters.getSelectItemVisibility();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

}
