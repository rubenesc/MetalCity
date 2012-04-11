/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.user;

import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.UserCommon;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.service.dto.ChangePasswordDTO;
import co.com.metallium.core.util.MetUtilHelper;
import javax.faces.bean.ViewScoped;
import com.metallium.utils.utils.UtilHelper;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import org.apache.commons.lang3.StringUtils;

/**
 * 20101220
 * @author Ruben
 */
@ManagedBean(name = "userConfigurationManagedBean")
@ViewScoped
public class UserConfigurationManagedBean extends UserBaseManagedBean implements Serializable {

    private Integer profileId; //It indates which profile I must display
    private boolean profileExists; //Tells me if the profile Im searching for can be displayed. It could exist, but maybe I cant display it, becuase Its banned or something.
    private String edit; //This is edit option Im on.
    //Configuration
    private String currentLocale;
    //ChangePassword
    private ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
    private String currentPasswordValitation = "";
    private String newPasswordValitation = "";
    private String confirmNewPasswordValitation = "";
    //ChangeEmail
    private String changeEmail = "";
    //DeleteProfile
    private String whyAreYouLeaving = "";
    private String password = "";
    //Edit Username
    private String webDomainName = MetUtilHelper.getWebDomainName();
    private String userName;
    private String userNameValitation = "";
    private boolean isUseraNameValid;

    /** Creates a new instance of UserConfigurationManagedBean */
    public UserConfigurationManagedBean() {
    }

    /***
     * This method is Point of Entry of this Managed Bean
     * and it is invoked by Pretty Faces.
     *
     * It returns a navigation command to indicate a new page to take the user
     * if the information could not be loaded correctly
     *
     * @return String Navigation Page.
     */
    public String loadManagedBean() {
        String answer = Navegation.PRETTY_HOME; //I assume the data is not going to be loaded correctly thus I'll send the request back to the home location.
        if (!StringUtils.isEmpty(this.getEdit())) {
            try {
                this.profileId = this.getAuthenticaedUser().getId();
                this.findProfile(profileId);
                this.setUserName(this.getUser().getNick());
                answer = Navegation.PRETTY_OK;
            } catch (Exception e) {
                manageException(e);
            }
        }

        return answer;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public String getEdit() {
        return edit;
    }

    public void findProfile(Integer id) {
        User userToFind = null;
        try {
            userToFind = this.getUserService().findUserById(id);
        } catch (Exception ex) {
            manageException(ex);
        }
        this.setUser(userToFind);
    }

    public String changeUserPassword() {
        String answer = Navegation.stayInSamePage;

        validateChangePasswordInput();

        if (isJsfMessageListEmpty()) {
            try {
                this.getUserService().changeUserPassword(this.getUser(), changePasswordDTO);
                this.addFacesMsgFromProperties("change_password_info_change_successful");
            } catch (Exception ex) {
                this.manageException(ex);
            }
        }

        return answer;
    }

    public String changeUserEmail() {
        String answer = Navegation.stayInSamePage;

        validateChangeEmailInput();

        if (isJsfMessageListEmpty()) {
            try {
                this.getUserService().changeUserEmail(this.getUser(), this.changeEmail);
                this.getUser().setEmail(this.changeEmail); //this is to refresh the email shown in the web page.
                this.addFacesMsgFromProperties("change_email_info_change_successful");
            } catch (Exception ex) {
                this.manageException(ex);
            }
        }

        return answer;
    }

    public String changeUserLocale() {
        String answer = Navegation.stayInSamePage;

        validateChangeUserLocaleInput();

        try {
            if (isJsfMessageListEmpty()) {
                this.getUserService().changeUserLocale(this.getUser().getId(), this.getUser().getLocale());
                this.setUserLocale(this.getUser().getLocale());
                this.addFacesMsgFromProperties("common_info_save_success");
            }
        } catch (Exception e) {
            manageException(e);
        }

        return answer;
    }

    public String deleteProfile() {
        String answer = Navegation.stayInSamePage;

        validateDeleteProfileInput();

        if (isJsfMessageListEmpty()) {
            try {

                this.setUserLogInfo();
                this.getUserService().deleteProfile(this.getUser(), this.password, this.whyAreYouLeaving);
                answer = Navegation.goToIndex;
                this.logOut();
                this.addFacesMsgFromProperties("delete_profile_info_change_successful");
            } catch (Exception ex) {
                this.manageException(ex);
            }
        }

        return answer;
    }

    public void checkIfCurrentPasswordIsValid(ValueChangeEvent vce) {
        String currentPassword = vce.getNewValue().toString();
        String message = this.validatePassword(currentPassword);
        this.setCurrentPasswordValitation(message);
    }

    public void checkIfNewPasswordIsValid(ValueChangeEvent vce) {
        String newPassword = vce.getNewValue().toString();
        String message = this.validatePassword(newPassword);
        this.setNewPasswordValitation(message);

        String confirmNewPassword = this.changePasswordDTO.getConfirmNewPassword();
        if (UtilHelper.isStringEmpty(message) && !UtilHelper.isStringEmpty(confirmNewPassword)) {
            //It means that the confirmNewPassword is valid, so now I test it to see
            //if it is equal to newPassword.
            if (!newPassword.equalsIgnoreCase(confirmNewPassword)) {
                message = applicationParameters.getResourceBundleMessage("change_password_info_passwords_dont_match");
                this.setConfirmNewPasswordValitation(message);
            } else {
                this.setConfirmNewPasswordValitation("");
            }
        }
    }

    public void checkIfConfirmNewPasswordIsValid(ValueChangeEvent vce) {
        String confirmNewPassword = vce.getNewValue().toString();
        String message = this.validatePassword(confirmNewPassword);

        if (UtilHelper.isStringEmpty(message)) {
            //It means that the confirmNewPassword is valid, so now I test it to see
            //if it is equal to newPassword.
            if (!confirmNewPassword.equalsIgnoreCase(this.changePasswordDTO.getNewPassword())) {
                message = applicationParameters.getResourceBundleMessage("change_password_info_passwords_dont_match");
            }
        }
        this.setConfirmNewPasswordValitation(message);
    }

    //=============edit UserName logic=============================================//
    public String changeUserName() {
        String answer = Navegation.stayInSamePage;

        if (!StringUtils.isEmpty(this.getUserName())) {

            this.setUserName(getUserName().toLowerCase()); //I make sure Im working with the username in lower case

            if (isJsfMessageListEmpty()) {
                try {
                    //The changeUserName will valdate if the username is correct or not.
                    this.getUserService().changeUserName(this.getUser(), this.getUserName());
                    this.getUser().setNick(this.getUserName()); //this is to refresh the email shown in the web page.
                    this.getAuthenticaedUser().setNick(this.getUserName()); //This is very important, so that the authenticated your can now navigate with his new username
                    this.addFacesMsgFromProperties("edit_username_info_change_successful");
                } catch (Exception ex) {
                    this.manageException(ex);
                }
            }
        }
        return answer;
    }

    public void checkIfUserNameIsValid(ValueChangeEvent vce) {
        String newUserName = vce.getNewValue().toString();
        String validationMessage = this.getUserService().validateUserName(newUserName);
        this.setUserNameValitation(validationMessage);
    }

    //=============Is can I edit=============================================//
    public boolean isCanIeditProfileConfig() {
        return isCanIEdit(UserCommon.EDIT_PROFILE_CONFIG);
    }

    public boolean isCanIeditUsername() {
        return isCanIEdit(UserCommon.EDIT_USERNAME);
    }

    public boolean isCanIeditPassword() {
        return isCanIEdit(UserCommon.EDIT_PASSWORD);
    }

    public boolean isCanIeditEmail() {
        return isCanIEdit(UserCommon.EDIT_EMAIL);
    }

    public boolean isCanIeditDeleteProfile() {
        return isCanIEdit(UserCommon.EDIT_DELETE_PROFILE);
    }

    private boolean isCanIEdit(String editOption) {
        boolean answer = false;
        if (edit != null && edit.equalsIgnoreCase(editOption)) {
            answer = true;
        }
        return answer;
    }

    //=============Getters and Setters=======================================//
    public ChangePasswordDTO getChangePasswordDTO() {
        return changePasswordDTO;
    }

    public void setChangePasswordDTO(ChangePasswordDTO changePasswordDTO) {
        this.changePasswordDTO = changePasswordDTO;
    }

    public String getConfirmNewPasswordValitation() {
        return confirmNewPasswordValitation;
    }

    public void setConfirmNewPasswordValitation(String confirmNewPasswordValitation) {
        this.confirmNewPasswordValitation = confirmNewPasswordValitation;
    }

    public String getCurrentPasswordValitation() {
        return currentPasswordValitation;
    }

    public void setCurrentPasswordValitation(String currentPasswordValitation) {
        this.currentPasswordValitation = currentPasswordValitation;
    }

    public String getNewPasswordValitation() {
        return newPasswordValitation;
    }

    public void setNewPasswordValitation(String newPasswordValitation) {
        this.newPasswordValitation = newPasswordValitation;
    }

    public String getChangeEmail() {
        return changeEmail;
    }

    public void setChangeEmail(String changeEmail) {
        this.changeEmail = changeEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWhyAreYouLeaving() {
        return whyAreYouLeaving;
    }

    public void setWhyAreYouLeaving(String whyAreYouLeaving) {
        this.whyAreYouLeaving = whyAreYouLeaving;
    }

    public String getCurrentLocale() {
        return currentLocale;
    }

    public void setCurrentLocale(String currentLocale) {
        this.currentLocale = currentLocale;
    }

    public String getWebDomainName() {
        return webDomainName;
    }

    public void setWebDomainName(String webDomainName) {
        this.webDomainName = webDomainName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNameValitation() {
        return userNameValitation;
    }

    public void setUserNameValitation(String userNameValitation) {
        this.userNameValitation = userNameValitation;
    }

    //=============Validate Input=======================================//
    private void validateChangeUserLocaleInput() {

        if (UtilHelper.isStringEmpty(this.getUser().getLocale())) {
            this.addFacesMsg("Fuckkk please select something");
            return;

        }
    }

    private void validateDeleteProfileInput() {
        String validationMessage = this.validatePassword(this.password);
        if (!UtilHelper.isStringEmpty(validationMessage)) {
            this.addFacesMsg(validationMessage);
            return;
        }

    }

    private void validateChangeEmailInput() {
        String validationMessage = this.validateEmail(changeEmail);
        if (!UtilHelper.isStringEmpty(validationMessage)) {
            this.addFacesMsg(validationMessage);
            return;
        }
    }

    private void validateChangePasswordInput() {
        //I just decided to validate it one at a time.


        String validationMessage = this.validatePassword(this.getChangePasswordDTO().getCurrentPassword());
        if (!UtilHelper.isStringEmpty(validationMessage)) {
            this.addFacesMsg(validationMessage);
            return;
        }

        String newPassword = this.getChangePasswordDTO().getNewPassword();
        validationMessage = this.validatePassword(newPassword);
        if (!UtilHelper.isStringEmpty(validationMessage)) {
            this.addFacesMsg(validationMessage);
            return;
        }

        String confirmNewPassword = this.getChangePasswordDTO().getConfirmNewPassword();
        validationMessage = this.validatePassword(confirmNewPassword);
        if (!UtilHelper.isStringEmpty(validationMessage)) {
            this.addFacesMsg(validationMessage);
            return;
        }

        if (!newPassword.equalsIgnoreCase(confirmNewPassword)) {
            this.addFacesMsg(validationMessage);
            this.addFacesMsgFromProperties("change_password_info_passwords_dont_match");
            return;
        }
    }
}
