/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.images;

import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.entity.ImageGallery;
import co.com.metallium.core.entity.User;
import co.com.metallium.view.jsf.user.ProfileManagedBean;
import com.metallium.utils.framework.utilities.LogHelper;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * 20101129
 * @author Ruben
 */
@ManagedBean(name = "createImageGallery")
@ViewScoped
public class CreateImageGalleryManagedBean extends ImageGalleryBaseManagedBean implements Serializable {

    /** Creates a new instance of CreateImageGalleryManagedBean */
    public CreateImageGalleryManagedBean() {
    }

    /**
     * This method is the POINT of entry of this managed bean.
     * Because I have to initialize the user upon which I am going to create
     * the image gallery.
     *
     * I get the User by POST (not GET) for security reasons.
     *
     * After looking at many ways I think this is the best and easiest way.
     *
     */
    @PostConstruct
    public void initializeCreateImageGallery() {
        LogHelper.makeLog("Create Image Gallery PostConstruct");
        //       ProfileManagedBean profileManagedBean = this.findManagedBean("profileManagedBean", ProfileManagedBean.class);
        User user = this.getAuthenticaedUser();
        this.setUser(user);
        this.getImageGallery().setUser(user);
    }

    public String cancelCreateImageGallery() {
        return Navegation.goToGalleriesRedirect;
    }

    public String createImageGallery() {
        String answer = Navegation.stayInSamePage; //I stay in the same page and show an error message


        if (isCanIeditProfile()) {
            //I can edit the profile
            this.validateCreateImageGallery();
            if (isJsfMessageListEmpty()) {
                try {

                    ImageGallery createdImageGallery = this.getImageGalleryService().createImageGallery(this.getImageGallery());
                    this.setImageGallery(createdImageGallery);
                    //Once I persist the users new data, I set him, so when I go the
                    //the next page I can see his new data refreshed.

                    //The Next page is the galleries page, and Im resending the page in a request mode.
                    answer = Navegation.goToGalleriesRedirect;
                } catch (Exception ex) {
                    this.manageException(ex);
                }

            }
        } else {
            this.addFacesMsgFromProperties("common_info_restricted_access");
        }

        return answer;
    }
    //=============Getters and Setters========================================//
}
