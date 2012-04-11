/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.user;

import javax.faces.bean.ViewScoped;
import co.com.metallium.core.constants.UserCommon;
import co.com.metallium.core.entity.UserFavorite;
import co.com.metallium.core.entity.UserOpinion;
import co.com.metallium.core.util.ApplicationParameters;
import java.util.ArrayList;
import javax.faces.model.SelectItem;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.UserWallPostTypeEnum;
import co.com.metallium.core.constants.state.UserProfile;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.entity.UserWallPost;
import co.com.metallium.core.util.MetUtilHelper;
import co.com.metallium.view.jsf.components.wall.UserWallComp;
import com.metallium.utils.dto.DateHelperDTO;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 * 20101222
 * @author Ruben
 */
@ManagedBean(name = "editProfileManagedBean")
@ViewScoped
public class EditProfileManagedBean extends ProfileExtManagedBean implements Serializable {

    private String edit; // Tells me which part of the profile am I going to edit.
    private boolean profileExists; //Tells me if the profile Im searching for can be displayed. It could exist, but maybe I cant display it, becuase Its banned or something.
    @ManagedProperty(value = "#{userWallComp}")
    private UserWallComp userWallComp;

    /** Creates a new instance of EditProfileManagedBean */
    public EditProfileManagedBean() {
    }

    /***
     * Point of entry to the page Edit Profile.
     * This method is called by Pretty Faces.
     * It returns a navigation command redirect the user to a new page
     * in case the information could not be loaded correctly. Otherwise it loads
     * the data requested and presents it in the page.
     *
     * @return String Navigation Page.
     */
    public String loadDataProfile() {

        String answer = Navegation.PRETTY_OK; //if the data is loaded correctly then I let him pass to the requested page.

        try {
            //I am going to edit the profile of the user that is Authenticated, I dont care
            //what nick the passed me in the request.
            Integer profileId = this.getAuthenticaedUser().getId();
            this.findProfile(profileId);
            this.initializeDateHelper(this.getUser().getBirthday());
        } catch (Exception e) {
            answer = Navegation.PRETTY_HOME; //there was a problem so Im redirecting to user to another page.
            manageException(e);
        }

        return answer;
    }

    public String loadDataWallPost() {

        String answer = Navegation.PRETTY_OK; //if the data is loaded correctly then I let him pass to the requested page.

        try {
            //I am going to edit the profile of the user that is Authenticated, I dont care
            //what nick the passed me in the request.
            Integer profileId = this.getAuthenticaedUser().getId();
            Integer postId = this.userWallComp.getUserWallPost().getId();
            UserWallPost wallPost = this.userWallComp.findPost(postId, profileId);
            if (wallPost != null) {
                this.userWallComp.getInfoRender().initialize(wallPost.getType());
            }

            this.setUser(new User(profileId));
            this.setProfileExists(true); //This just indicates that I found what I was looking for
        } catch (Exception e) {
            this.setProfileExists(false); //This indicates that I didnot find what I was looking for.
            answer = Navegation.PRETTY_HOME; //there was a problem so Im redirecting to user to another page.
            manageException(e);
        }


        return answer;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public String getEdit() {
        return edit;
    } 
 
    private void findProfile(Integer id) {

        User userToFind = null;
        try {
            userToFind = this.getUserService().findUserById(id);
            this.setProfileExists(true);
            this.setProfileId(userToFind.getId());
        } catch (Exception ex) {
            this.setProfileExists(false);
            manageException(ex);
        }

        //WHat I do here is see if the objects of UserFavoriteInfo and UserOpinionInfo
        //exist or not. When a user is created the first time, this info does not exist
        //so it si null and we must asure that if its null, then it becomes initialized.
        if (userToFind.getUserFavorite() == null) {
            userToFind.setUserFavorite(new UserFavorite());
        }

        if (userToFind.getUserOpinion() == null) {
            userToFind.setUserOpinion(new UserOpinion());
        }

        this.setUser(userToFind);

    }

    public boolean isProfileExists() {

        if (this.getProfileId() == null && this.getUser() == null) {
            if (this.isJsfMessageListEmpty()) {
                //If the msg list is not empty is because I already put the corresponding
                //msg I need to show, so this method could get called various times and I dont want to
                //show the msg repeatedly
                this.addFacesMsgFromProperties("profile_info_no_result_exception");
            }
            this.setProfileExists(false);
        } else {
            this.setProfileExists(true);
        }
        return this.profileExists;
    }

    public void setProfileExists(boolean profileExists) {
        this.profileExists = profileExists;
    }

    //==========================Edit Profile=========================================//
    public boolean isCanIeditProfilePictureInfo() {
        return isCanIEdit(UserCommon.EDIT_PROFILE_PICTURE);
    }

    public boolean isCanIeditPersonalInfo() {
        return isCanIEdit(UserCommon.EDIT_PERSONAL);
    }

    public boolean isCanIeditFavoriteInfo() {
        return isCanIEdit(UserCommon.EDIT_FAVORITE);
    }

    public boolean isCanIeditOpinionInfo() {
        return isCanIEdit(UserCommon.EDIT_OPINION);
    }

    public boolean isCanIaddWallPost() {
        return isCanIEdit(UserCommon.EDIT_WALL_POST_ADD);
    }

    public boolean isCanIeditWallPost() {
        return isCanIEdit(UserCommon.EDIT_WALL_POST_EDIT);
    }

    private boolean isCanIEdit(String editOption) {
        boolean answer = false;
        if (edit != null && edit.equalsIgnoreCase(editOption)) {
            answer = true;
        }
        return answer;
    }

    public String saveChangesUserProfilePersonalInfo() {
        String answer = Navegation.stayInSamePage; //I stay in the same page and show an error message

        if (isCanIeditProfile()) {
            //I can edit the profile
            try {
                validateInputEditPersonal();
                if (isJsfMessageListEmpty()) {
                    User userToEdit = this.getUser();
                    userToEdit = this.getUserService().editUserProfilePersonalInfo(userToEdit);
                    //Once I persist the users new data, I set him, so when I go the
                    //the next page I can see his new data refreshed.
                    //   this.setUser(userToEdit);
                    verifyIfUserChangedHisName(userToEdit.getName());
                    answer = Navegation.goToProfileRedirect;
                }
            } catch (Exception ex) {
                this.manageException(ex);
            }
        } else {
            this.addFacesMsgFromProperties("common_info_restricted_access");
        }

        return answer;
    }

    public String saveChangesUserProfileFavoriteInfo() {
        String answer = Navegation.stayInSamePage; //I stay in the same page and show an error message


        if (isCanIeditProfile()) {
            //I can edit the profile
            try {
                User userToEdit = this.getUser();
                UserFavorite uf = userToEdit.getUserFavorite();
                uf.setId(userToEdit.getId()); //Just to be sure that its the User im editing
                uf = this.getUserService().editUserProfileFavoriteInfo(uf);
                //Once I persist the users new data, I set him, so when I go the
                //the next page I can see his new data refreshed.
                userToEdit.setUserFavorite(uf);
                this.setUser(userToEdit);

                answer = Navegation.goToProfileRedirect;
            } catch (Exception ex) {
                this.manageException(ex);
            }
        } else {
            this.addFacesMsgFromProperties("common_info_restricted_access");
        }

        return answer;
    }

    public String saveChangesUserProfileOpinionInfo() {
        String answer = Navegation.stayInSamePage; //I stay in the same page and show an error message

        if (isCanIeditProfile()) {
            //I can edit the profile
            if (isJsfMessageListEmpty()) {
                try {
                    User userToEdit = this.getUser();
                    UserOpinion uo = userToEdit.getUserOpinion();
                    uo.setId(userToEdit.getId()); //Just to be sure that its the User im editing
                    uo = this.getUserService().editUserProfileOpinionInfo(uo);
                    //Once I persist the users new data, I set him, so when I go the
                    //the next page I can see his new data refreshed.
                    userToEdit.setUserOpinion(uo);
                    this.setUser(userToEdit);
                    answer = Navegation.goToProfileRedirect;
                } catch (Exception ex) {
                    this.manageException(ex);
                }
            }
        } else {
            this.addFacesMsgFromProperties("common_info_restricted_access");
        }

        return answer;
    }

    public String cancelEditProfile() {
        return Navegation.goToProfileRedirect;
    }

    public String editWhatsOnYourMind() {
        String answer = Navegation.stayInSamePage;

        if (isCanIeditProfile()) {
            //I can edit the profile
            try {
                this.getUserService().editWhatsOnYourMind(this.getUser());
                answer = Navegation.goToProfileRedirect;
            } catch (Exception ex) {
                this.manageException(ex);
            }
        } else {
            this.addFacesMsgFromProperties("common_info_restricted_access");
        }

        return answer;
    }

    private void validateInputEditPersonal() {

        Integer authenticatedUserProfile = this.getAuthenticaedUser().getProfile();

        if (UserProfile.isUser(authenticatedUserProfile)) {

            if (!MetUtilHelper.isUserNameValid(this.getUser().getName())) {
                if (UtilHelper.isStringEmpty(this.getUser().getName())) {
                    this.addFacesMsgFromProperties("register_field_name_required");
                } else {
                    this.addFacesMsgFromProperties("register_field_name_invalid");
                }
                return;
            }

            try {
                this.getUser().setBirthday(MetUtilHelper.getInstance().createDateFromString(this.getDateHelper()));
            } catch (Exception e) {
                LogHelper.makeLog("... Error converting time: " + e.getMessage());
                this.getUser().setBirthday(null);
            }

            if (this.getUser().getBirthday() == null) {
                this.addFacesMsgFromProperties("register_field_birthday_required");
            }

            if (UtilHelper.isStringEmpty(this.getUser().getSex())) {
                this.addFacesMsgFromProperties("register_field_sex_required");
            }

//            if (UtilHelper.isStringEmpty(this.getUser().getCountry().getIso())) {
//                this.addFacesMsgFromProperties("edit_profile_field_country_required");
//            }

            if (this.getUser().getNetwork() == null) {
                this.addFacesMsgFromProperties("profile_info_network_required");
            }
        }

        if (UserProfile.isAdministrator2(authenticatedUserProfile)) {
            if (UtilHelper.isStringEmpty(this.getUser().getState())) {
                this.addFacesMsgFromProperties("edit_profile_field_state_required");
            }
        }

        if (UserProfile.isRoot(authenticatedUserProfile)) {
            if (this.getUser().getProfile() == null) {
                this.addFacesMsgFromProperties("edit_profile_field_profile_required");
            }
        }
    }

    public ArrayList<SelectItem> getSelectItemProfile() {
        return ApplicationParameters.getSelectItemProfile();
    }

    public ArrayList<SelectItem> getSelectItemUserState() {
        return ApplicationParameters.getSelectItemUserState();
    }

    private void initializeDateHelper(Date date) {

        if (date != null) {
            try {

                Calendar c = Calendar.getInstance();
                c.setTime(date);

                String year = "" + c.get(Calendar.YEAR);
                String month = "" + c.get(Calendar.MONTH);
                String day = "" + c.get(Calendar.DAY_OF_MONTH);
                DateHelperDTO dto = new DateHelperDTO(year, month, day);
                this.setDateHelper(dto);

            } catch (Exception e) {
                LogHelper.makeLog(e.getMessage(), e);
            }

        }

    }

    /**
     * Verifies if the User Changed his Name, if so, he updates this name in the
     * Authenticated User Object, because this doesn't get updated until the user logs in
     * again, so I just refresh it here.
     *
     * @param name which was persisted in the DB when the changes where made.
     * This name is compared with the authenticated users Name, to see if it
     * has changed or not.
     */
    private void verifyIfUserChangedHisName(String name) {

        try {
            if (!UtilHelper.areStringsEqual(name, this.getAuthenticaedUser().getName())) {
                this.getAuthenticaedUser().setName(name);
            }
        } catch (Exception e) {
            LogHelper.makeLog(e);
        }


    }

    //==========================Getters and Setters=========================================//
    public UserWallComp getUserWallComp() {
        return userWallComp;
    }

    public void setUserWallComp(UserWallComp userWallComp) {
        this.userWallComp = userWallComp;
    }
}
