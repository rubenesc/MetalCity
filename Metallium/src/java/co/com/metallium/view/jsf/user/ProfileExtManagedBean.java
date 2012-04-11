/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.user;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.exception.BaseException;
import co.com.metallium.core.exception.GalleryException;
import co.com.metallium.core.exception.UserException;
import co.com.metallium.core.service.ImageGalleryService;
import com.metallium.utils.dto.PairDTO;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.event.PhaseId;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * 20101130
 *
 * Aux method that should be extended by Profile Manged Bean, so that it can put
 * its custom logic here.
 *
 * @author Ruben
 */
@ManagedBean
@NoneScoped
public class ProfileExtManagedBean extends UserBaseManagedBean implements Serializable {

    @EJB
    private ImageGalleryService imageGalleryService;
    private Integer profileId; //It indates which profile I must display
    private String profileNick; //It indates which profile I must display

    /** Creates a new instance of ProfileExtManagedBean */
    public ProfileExtManagedBean() {
    }

    /**
     *
     * @param profileId
     */
    public void setProfileId(Integer profileId) {
        //I set the profile who's Id I get in the request.
        this.profileId = profileId;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public String getProfileNick() {
        return profileNick;
    }

    public void setProfileNick(String profileNick) {
        this.profileNick = profileNick;
    }

    //==================Image Gallery=========================================//
    public void removeProfilePicture() {

        try {

            int userId = this.getUser().getId();

            this.imageGalleryService.deleteUserProfileImage(userId);

            User userToEdit = new User();
            userToEdit.setId(userId);
            userToEdit.setAvatar(MetConfiguration.DEFAULT_USER_PROFILE_AVATAR_PIC);
            userToEdit.setAvatarProfile(MetConfiguration.DEFAULT_USER_PROFILE_PIC);
            this.editProfilePicture(userToEdit);

            this.updateAuthenticatedUsersAvatas(userToEdit.getAvatar(), userToEdit.getAvatarProfile());

            addFacesMsgFromProperties("profile_info_remove_profile_image");
        } catch (Exception ex) {
            this.manageException(ex);
        }
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

                PairDTO pairDTO = this.imageGalleryService.uploadProfileImage(userId,
                        fileName, contentType, fileSize, uploadedFile.getInputstream());


                User userUpdate = new User();
                userUpdate.setId(userId);
                userUpdate.setAvatar(pairDTO.getOne());
                userUpdate.setAvatarProfile(pairDTO.getTwo());
                this.editProfilePicture(userUpdate);

                this.updateAuthenticatedUsersAvatas(userUpdate.getAvatar(), userUpdate.getAvatarProfile());

                this.addFacesMsgFromProperties("image_gallery_info_uploaded_successfully", fileName);
            } catch (BaseException ex) {
                this.manageException(ex);
            } catch (Exception ex) {
                String msg = applicationParameters.getResourceBundleMessage("common_info_file_upload_failed");
                manageException(new GalleryException(ex.getMessage(), ex, msg));
            }


        } else {
            this.addFacesMsgFromProperties("common_info_restricted_access");
        }
    }

    public void editProfilePicture(User user) throws UserException {
        this.getUserService().editProfilePicture(user);
        this.getUser().setAvatar(user.getAvatar());
        this.getUser().setAvatarProfile(user.getAvatarProfile());

    }

    private void updateAuthenticatedUsersAvatas(String avatar, String avatarProfile) {
        //Since I changed the avatar location for anotherone, I best refresh the data
        //of the authenticated user to. This makes sense more when I remove the image.
        //because, the images are not going to be in the users profile folder.
        this.getAuthenticaedUser().setAvatar(avatar);
        this.getAuthenticaedUser().setAvatarProfile(avatarProfile);

    }
    //==================Image Gallery=========================================//
    //==================Image Gallery=========================================//
    //==================Image Gallery=========================================//
    //==================Image Gallery=========================================//
    //==================Image Gallery=========================================//
}
