/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.user;

import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.entity.Country;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.entity.iplocation.LocationDTO;
import co.com.metallium.core.util.ApplicationParameters;
import co.com.metallium.core.util.MetUtilHelper;
import co.com.metallium.view.util.JsfUtil;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.TimeWatch;
import com.metallium.utils.utils.UtilHelper;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Ruben
 */
@ManagedBean(name = "registerUserManagedBean")
@RequestScoped
public class RegisterUserManagedBean extends UserBaseManagedBean implements Serializable {

    private String name = "";
    private String nickAvailableMsg = "";
    private String emailReenter = "";
    private String email1Msg = "";
    private String email2Msg = "";
    private String captchaAnwser = "";

    public RegisterUserManagedBean() {
    }

    @PostConstruct
    private void initialize() {
        this.getUser().setBirthday(new Date());
    }

    public String registerUser() {
        String answer = Navegation.stayInSamePage; //I stay in the same page and show an error message

        validateRegisterUserInput(); //I Validate that all the fields are filled in


        if (isJsfMessageListEmpty()) {
            try {

                assignUserLocationValues();
                this.getUserService().createUser(this.getUser(), true); //I go ahead and register the user.
                answer = Navegation.goToRegistrationVerification;

            } catch (Exception ex) {
                this.manageException(ex);
            }
        }

        return answer;

    }

    private void validateRegisterUserInput() {


        if (!MetUtilHelper.isUserNameValid(this.getName())) {
            if (UtilHelper.isStringEmpty(this.getName())) {
                this.addFacesMsgFromProperties("register_field_name_required");
            } else {
                this.addFacesMsgFromProperties("register_field_name_invalid");
            }
            return;
        } else {
            this.getUser().setName(this.getName());
        }


        this.validateUserEmailRegister(this.getUser().getEmail());


        if (UtilHelper.isStringEmpty(this.getUser().getSex())) {
            this.addFacesMsgFromProperties("register_field_sex_required");
            return;
        }

        this.validateUserPassword();
        if (!isJsfMessageListEmpty()) {
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
            return;
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailReenter() {
        return emailReenter;
    }

    public void setEmailReenter(String emailReenter) {
        this.emailReenter = emailReenter;
    }

    public String getEmail1Msg() {
        return email1Msg;
    }

    public void setEmail1Msg(String email1Msg) {
        this.email1Msg = email1Msg;
    }

    public String getEmail2Msg() {
        return email2Msg;
    }

    public void setEmail2Msg(String email2Msg) {
        this.email2Msg = email2Msg;
    }

    public String getCaptchaAnwser() {
        return captchaAnwser;
    }

    public void setCaptchaAnwser(String captchaAnwser) {
        this.captchaAnwser = captchaAnwser;
    }

    /**
     *
     * This is the method that has the logic that validates the nick.
     *
     * @param nickToCheck
     * @return
     */
    private String validateEmail(String email1, String email2) {
        String answer = validateEmail(email1);

        //If the first email is ok. Then I validate it againt the second email
        if (answer.length() == 0) {
            if (!UtilHelper.areStringsEqual(email1.trim(), email2.trim())) {
                answer = applicationParameters.getResourceBundleMessage("register_field_email_info_dont_match");

            }
        }

        return answer;
    }

    private void validateUserEmailRegister(String email1) {

        String emailValidate = this.validateUserEmail(email1);
        if (UtilHelper.isStringEmpty(emailValidate)) {
            //email 1 is valid, so now I compare it with email 2
            emailValidate = this.validateEmail(email1, this.getEmailReenter());
            if (emailValidate.length() > 0) {
                JsfUtil.addSuccessMessage(emailValidate);
            }
        }
    }

    /**
     * What Im doing here is setting the User his Country, Locale and Network based on the Location
     * obtained from his IP.
     * 
     * If not then I will set the default values.
     *
     * I also do a Check to see that no country codes like
     * RD = Reserved
     * A1 = Anonymous Proxy
     * A2 = Satellite Provider
     *
     * Fall in this section.
     *
     */
    private void assignUserLocationValues() {
        try {

            //First I get the location of the user based on his IP address
            LocationDTO userLocationDTO = this.obtainCurrentUserLocation();


            if (userLocationDTO != null) {
                //Now I check if that country exists in my countries table, based on the countries code.
                String country = ApplicationParameters.getCountry(userLocationDTO.getCountryCode());
                if (!UtilHelper.isStringEmpty(country)) {
                    //The country does have a displayble name so it is a valid country code.
                    this.getUser().setCountry(new Country(userLocationDTO.getCountryCode()));
                }

                //Now I set the Locale
                this.getUser().setLocale(userLocationDTO.getLocale());
                //Now I set the Network
                this.getUser().setNetwork(userLocationDTO.getNetwork());


            }

        } catch (Exception e) {
            LogHelper.makeLog(e);
        }


    }
}
