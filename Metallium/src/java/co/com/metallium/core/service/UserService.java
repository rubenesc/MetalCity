/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import co.com.metallium.core.constants.BirthDayDisplayConst;
import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.state.UserFriendState;
import co.com.metallium.core.constants.state.UserProfile;
import co.com.metallium.core.constants.state.UserState;
import co.com.metallium.core.entity.Country;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.entity.UserBan;
import co.com.metallium.core.entity.UserFavorite;
import co.com.metallium.core.entity.UserFriend;
import co.com.metallium.core.entity.UserFriendPK;
import co.com.metallium.core.entity.UserOpinion;
import co.com.metallium.core.entity.UserSession;
import co.com.metallium.core.exception.AuthenticationException;
import co.com.metallium.core.exception.EmailException;
import co.com.metallium.core.exception.FriendException;
import co.com.metallium.core.exception.FriendUpdateNumberException;
import co.com.metallium.core.exception.UserException;
import co.com.metallium.core.exception.UserIsBannedException;
import co.com.metallium.core.log.interceptor.InterceptorUserAuthentication;
import co.com.metallium.core.log.service.InterceptorService;
import co.com.metallium.core.service.dto.ChangePasswordDTO;
import co.com.metallium.core.service.dto.NotificationDTO;
import co.com.metallium.core.service.dto.search.UserFriendsSearchDTO;
import co.com.metallium.core.util.ApplicationParameters;
import co.com.metallium.core.util.MetUtilHelper;
import co.com.metallium.core.util.RegexUtil;
import com.metallium.utils.cipher.EmailCipher;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.framework.utilities.RandomHelper;
import com.metallium.utils.utils.CryptoUtils;
import com.metallium.utils.utils.FormatHelper;
import com.metallium.utils.utils.TimeWatch;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import com.metallium.utils.utils.UtilHelper;
import co.org.apache.commons.lang.RandomStringUtilsExt;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Ruben
 */
@Stateless
public class UserService implements Serializable {

    @PersistenceContext(unitName = "MetalliumPU")
    private EntityManager em;
    @EJB
    CommonService commonService;
    @EJB
    GeneralService generalService;
    @EJB
    UserHelperService userHelperService;
    @EJB
    NotificationService notificationService;
    @EJB
    InterceptorService InterceptorService;
    @EJB
    private ApplicationParameters applicationParameters;
    //info stuff
    String className = UserService.class.getName();

    public UserService() {
    }

    public void editWhatsOnYourMind(User user) throws UserException {
        User u = this.findUserById(user.getId());
        u.setWhatsOnYourMind(user.getWhatsOnYourMind());
        this.edit(u);
    }

    public void setDefaultProfilePicture(Integer id) throws UserException {
        User userToEdit = this.findUserById(id);
        try {
            userToEdit.setAvatar(MetConfiguration.DEFAULT_USER_PROFILE_AVATAR_PIC);
            userToEdit.setAvatarProfile(MetConfiguration.DEFAULT_USER_PROFILE_PIC);
            this.edit(userToEdit);
        } catch (Exception e) {
            throw new UserException(e.getMessage(), e);
        }
    }

    public void editProfilePicture(User user) throws UserException {
        User userToEdit = this.findUserById(user.getId());
        try {
            userToEdit.setAvatar(user.getAvatar());
            userToEdit.setAvatarProfile(user.getAvatarProfile());
            this.edit(userToEdit);
        } catch (Exception e) {
            throw new UserException(e.getMessage(), e);
        }

    }

    public User editUserProfilePersonalInfo(User user) throws UserException {
        User userToEdit = this.findUserById(user.getId());

        try {
            userToEdit.setNetwork(user.getNetwork());
            userToEdit.setName(user.getName());
            userToEdit.setBirthday(user.getBirthday());

            if (user.getBirthdayDispay() != null) {
                userToEdit.setBirthdayDispay(user.getBirthdayDispay());
            }

            userToEdit.setCity(user.getCity());
            userToEdit.setSex(user.getSex());
            userToEdit.setAvatar(user.getAvatar());
            userToEdit.setWhatsOnYourMind(user.getWhatsOnYourMind());

            if (StringUtils.isEmpty(user.getCountry().getIso())) {
                userToEdit.setCountry(new Country(MetConfiguration.DEFAULT_COUNTRY));
            } else {
                Country co = commonService.findCountryByIso(user.getCountry().getIso());
                userToEdit.setCountry(co);
            }

            userToEdit.setLastModified(new Date());
            this.edit(userToEdit);
        } catch (Exception e) {
            throw new UserException(e.getMessage(), e);
        }

        return userToEdit;

    }

    public UserFavorite editUserProfileFavoriteInfo(UserFavorite uf) throws UserException {
        User user = this.findUserById(uf.getId());
        boolean isCreate = false;
        UserFavorite ufToEdit = user.getUserFavorite();

        try {

            if (ufToEdit == null) {
                isCreate = true;
                ufToEdit = new UserFavorite();
            }

            /* TODO make this a dynamic copy and not a manual */
            ufToEdit.setId(uf.getId());
            ufToEdit.setBand(uf.getBand());
            ufToEdit.setAlbum(uf.getAlbum());
            ufToEdit.setSong(uf.getSong());
            ufToEdit.setMovie(uf.getMovie());
            ufToEdit.setAuthor(uf.getAuthor());
            ufToEdit.setBook(uf.getBook());
            ufToEdit.setGod(uf.getGod());
            ufToEdit.setGame(uf.getGame());
            ufToEdit.setDrink(uf.getDrink());
            ufToEdit.setDisplay(uf.isDisplay());

            user.setLastModified(new Date());
            if (isCreate) {
                em.persist(ufToEdit);
            } else {
                em.merge(ufToEdit);
            }

            em.flush();
        } catch (Exception e) {
            throw new UserException(e.getMessage(), e);
        }

        return ufToEdit;

    }

    public UserOpinion editUserProfileOpinionInfo(UserOpinion uo) throws UserException {
        User user = this.findUserById(uo.getId());
        boolean isCreate = false;
        UserOpinion uoToEdit = user.getUserOpinion();

        try {

            if (uoToEdit == null) {
                isCreate = true;
                uoToEdit = new UserOpinion();
            }

            /* TODO make this a dynamic copy and not a manual */
            uoToEdit.setId(uo.getId());
            uoToEdit.setYourself(uo.getYourself());
            uoToEdit.setReligion(uo.getReligion());
            uoToEdit.setMusic(uo.getMusic());
            uoToEdit.setDisplay(uo.isDisplay());

            user.setLastModified(new Date());
            if (isCreate) {
                em.persist(uoToEdit);
            } else {
                em.merge(uoToEdit);
            }
            em.flush();
        } catch (Exception e) {
            throw new UserException(e.getMessage(), e);
        }

        return uoToEdit;

    }

    private void arePasswordsEqual(String userToAutenticatePassword, String userPassword) throws Exception, AuthenticationException {
        String inputHash = CryptoUtils.byteArrayToHexString(CryptoUtils.computeHash(userToAutenticatePassword));
        if (!userPassword.equals(inputHash)) {
            String msg = applicationParameters.getResourceBundleMessage("login_info_password_incorrect");
            throw new AuthenticationException(msg, null, msg);
        }
    }

    private void edit(User user) {
        user.setLastModified(new Date());
        em.merge(user);
        em.flush();
    }

    public User findUserByEmail(String email) throws UserException {
        User user = null;

        try {
            user = (User) em.createNamedQuery("User.findByEmail").setParameter("email", email.trim().toLowerCase()).getSingleResult();
        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg = applicationParameters.getResourceBundleMessage("profile_info_email_not_registered", email);
                throw new UserException(msg, ex, msg);
            } else {
                throw new UserException(ex.getMessage(), ex);
            }
        }
        return user;
    }

    /**
     * This is the Method I use when I go to a users Profile. So in this method I get all of
     * the users stuff the present in his profile.
     *
     * @param id
     * @return
     * @throws UserException
     */
    public User findUserWithFriends(Integer id) throws UserException {
        TimeWatch watch = TimeWatch.start();
        User user = this.findUserById(id);
        user = loadUsersFriends(user);
        LogHelper.makeLog("--> ".concat(className).concat(".findUserWithFriends(Integer id) time: ") + watch.time());
        return user;

    }

    public User findUserWithFriends(String nick) throws UserException {
        TimeWatch watch = TimeWatch.start();
        User user = this.findUserByNick(nick);
        user = loadUsersFriends(user);
        LogHelper.makeLog("--> ".concat(className).concat(".findUserWithFriends(String nick) time: ") + watch.time());
        return user;

    }

    private User loadUsersFriends(User user) throws UserException {
        try {
            verifyIfUserIsActive(user);
        } catch (Exception e) {
            //I dont care.
        }

        user.setUserFriendsCollection(this.findUsersFriends(new UserFriendsSearchDTO(user.getId())));
        return user;

    }

    public User findUserByNick(String nick) throws UserException {
        User user = null;

        try {
            user = (User) em.createNamedQuery("User.findByNick").setParameter("nick", nick).getSingleResult();
        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg1 = "The user with nick: " + nick + ", could not be found in the system.";
                String msg2 = applicationParameters.getResourceBundleMessage("profile_info_no_result_exception");
                throw new UserException(msg1, null, msg2); //Well If its a NoResultException then fuck the exception, it just goes null.
            } else {
                throw new UserException(ex.getMessage(), ex);
            }
        }

        return user;
    }
    
    
    public Integer findActiveUserIdByNick(String nick) throws UserException {
        Integer user = null;

        try {
            user = (Integer) em.createNamedQuery("User.findActiveUserIdByNick")
                    .setParameter("nick", nick)
                    .setParameter("state", UserState.ACTIVE).getSingleResult();
        } catch (Exception ex) {
        }

        return user;
    }
    
    
    

    public User findActiveUserById(int id) throws UserException {
        User user = null;

        try {

            user = (User) em.createNamedQuery("User.findUserActiveById").setParameter("id", id).setParameter("state", UserState.ACTIVE).getSingleResult();

        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg1 = "The user with id: " + id + " and state ACTIVE, could not be found in the system.";
                String msg2 = applicationParameters.getResourceBundleMessage("profile_info_no_result_exception");
                throw new UserException(msg1, null, msg2); //Well If its a NoResultException then fuck the exception, it just goes null.
            } else {
                throw new UserException(ex.getMessage(), ex);
            }
        }

        return user;
    }

    public User findUserById(int id) throws UserException {
        User user = null;

        try {
            user = (User) em.createNamedQuery("User.findById").setParameter("id", id).getSingleResult();
        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg1 = "The user with id: " + id + ", could not be found in the system.";
                String msg2 = applicationParameters.getResourceBundleMessage("profile_info_no_result_exception");
                throw new UserException(msg1, null, msg2); //Well If its a NoResultException then fuck the exception, it just goes null.
            } else {
                throw new UserException(ex.getMessage(), ex);
            }
        }

        return user;
    }

    public User createUser(User user, boolean sendMail) throws UserException, EmailException {

        validateIfEmailIsRegistered(user.getEmail());

        try {
            //I set the user with his default values
            setCreateUserDefaultValues(user);

            //I asign the user his new encripted password
            String hash = CryptoUtils.byteArrayToHexString(CryptoUtils.computeHash(user.getPassword()));
            user.setPassword(hash);

            user.setEmail(user.getEmail().toLowerCase());
            if (UtilHelper.isStringNotEmpty(user.getNick())) {
                user.setNick(user.getNick().toLowerCase());
            }

            //I persist the user
            em.persist(user);
            em.flush();
            em.refresh(user);

            //I get his newly created Id
            Integer userId = user.getId();

            //And now I create the Favorite and Opinion Data
            user.setUserFavorite(new UserFavorite(userId, false));
            user.setUserOpinion(new UserOpinion(userId, false));
            user.setNick(generateUserId(userId));
            em.merge(user);
            em.flush();

        } catch (Exception ex) {
            throw new UserException(ex.getMessage(), ex);
        }

        if (sendMail) {
            this.sendActivateAccountEmail(user);
        }

        return user;
    }

    /**
     * This method tells me if a user name is taken or not
     * @param userName
     * @return true = username available
     *         false = username not available
     */
    private boolean validateIfUserNameIsAvailable(String userName) {
        boolean answer = false;

        if (!ApplicationParameters.isNameReserved(userName)) {
            try {
                int count = ((Number) em.createNamedQuery("User.countUsersByNick").
                        setParameter("nick", userName).getSingleResult()).intValue();
                if (count == 0) {
                    answer = true; //The userName is not taken
                }

            } catch (Exception e) {
                LogHelper.makeLog(e);
            }
        }


        return answer;
    }

    public void validateIfEmailIsRegistered(String email) throws UserException {
        User registerdUser = null;
        try {
            registerdUser = this.findUserByEmail(email);
        } catch (UserException ex) {
            if (ex.getCause() instanceof NoResultException) {
                //this is ok
            } else {
                throw ex;
            }
        }
        if (registerdUser != null) {
            String displayMessage = applicationParameters.getResourceBundleMessage("register_error_email_exists", registerdUser.getEmail());
            throw new UserException(displayMessage, null, displayMessage);
        }
    }

    private void setCreateUserDefaultValues(User user) {
        user.setState(UserState.PENDING); //His state is pending, because he has to verify his email.
        user.setProfile(UserProfile.USER);
        user.setAvatar(MetConfiguration.DEFAULT_USER_PROFILE_PIC);
        user.setAvatarProfile(MetConfiguration.DEFAULT_USER_PROFILE_AVATAR_PIC);
        user.setNumberOfFriends(0); //No friends ... poor thing.
        user.setNotifications(false); //Tells if the user has a new notification he should be aware of (exaple: friend request).
        user.setNewMessage(false); //Tells if the user has a new message in his message inbox.
        user.setBirthdayDispay(BirthDayDisplayConst.DISPLAY_COMPLETE);

        if (UtilHelper.isStringEmpty(user.getCity())) {
            user.setCity(MetConfiguration.DEFAULT_CITY);
        }

        if (user.getCountry() == null) {
            user.setCountry(new Country(MetConfiguration.DEFAULT_COUNTRY));
        }

        if (user.getNetwork() == null) {
            user.setNetwork(MetConfiguration.DEFAULT_NETWORK);
        }

        if (UtilHelper.isStringEmpty(user.getLocale())) {
            user.setLocale(MetConfiguration.DEFAULT_LOCALE_LANGUAGE);
        }

        Date currentDate = new Date();
        user.setRegistrationDate(currentDate);
        user.setLastModified(currentDate);
    }

    @Interceptors(InterceptorUserAuthentication.class)
    public User authenticateUser(User userToAuthenticate) throws UserException, Exception {
        User user = this.findUserByEmail(userToAuthenticate.getEmail().trim());

        String userToAutenticatePassword = userToAuthenticate.getPassword();
        String userPassword = user.getPassword();

        verifyIfUserIsActive(user);

        if (!MetConfiguration.ARE_WE_IN_DEVELOPMENT) {
            try {
                arePasswordsEqual(userToAutenticatePassword, userPassword);
            } catch (AuthenticationException ex) {
                ex.setUserId(user.getId());
                throw ex;
            }
        }

        //Now I set the users friends. Its important to know who the users friends are
        //because with this info, we can distinguish who is a friend and who is not, for
        //the Add or Delete friend, to send a message you know who u can send it too, and so on.
        List<User> friends = this.findUsersFriends(new UserFriendsSearchDTO(user.getId()));
        user.setUserFriendsCollection(friends);


        return user;
    }
    
    
    public User authenticateUser(Integer userId) throws UserException, Exception {
        User user = this.findUserById(userId);
        
        verifyIfUserIsActive(user);

        //Now I set the users friends. Its important to know who the users friends are
        //because with this info, we can distinguish who is a friend and who is not, for
        //the Add or Delete friend, to send a message you know who u can send it too, and so on.
        List<User> friends = this.findUsersFriends(new UserFriendsSearchDTO(user.getId()));
        user.setUserFriendsCollection(friends);


        return user;
    }
    
    
    
    
    

    /***
     * Verifies if the user has an active state to log in or do something.
     * It also verifies if the user has a banned state and deals with it.
     * If he was banned but payed his time the user Entity is updated to is normal
     * state.
     *
     * @param user
     *
     * @throws UserException = The User is not Active at all.
     */
    private void verifyIfUserIsActive(User user) throws UserException, UserIsBannedException {
        if (!UserState.isActive(user.getState())) {
            //Holy Shit ... The User is not Active ... here we gooo....
            if (UserState.isBanned(user.getState())) {

                //Holy mother fucker this guy is banned ....
                //We have to verify if he is still banned or if his time is up.
                this.verifyIfUserBanIsOn(user.getId(), user.getCurrentBanId());

                //If I pass the verifyIfUserBanIsOn method then that means that the
                //user servered his ban time.
                em.refresh(user); //I just simply refresh the users Data, because his bann is over!!!

            } else {
                String msg = applicationParameters.getResourceBundleMessage("login_info_user_not_active", MetConfiguration.EMAIL_SUPPORT);
                throw new UserException(msg, null, msg);
            }
        }
    }

    //====================Start Password Logic ================================//
    public void rememberPassword(String email) throws Exception {

        User user = this.findUserByEmail(email);

        if (!UserState.isActive(user.getState())) {
            throw new UserException("The user is not active");
        }

        changeUserPasswordAsynchronous(user);
    }

    @Asynchronous
    private void changeUserPasswordAsynchronous(User user) throws Exception {

        String newPassword = RandomHelper.generateRandomPassword();
        this.changeTheFuckingPassword(user, newPassword);
        sendRememberPasswordEmail(user, newPassword);

    }

    public void changeUserPassword(User user, ChangePasswordDTO dto) throws Exception {

        User userToEdit = this.findUserById(user.getId());

        String userToAutenticatePassword = dto.getCurrentPassword();
        String userPassword = userToEdit.getPassword();

        try {
            this.arePasswordsEqual(userToAutenticatePassword, userPassword);
        } catch (AuthenticationException e) {
            String msg = applicationParameters.getResourceBundleMessage("change_password_info_current_password_incorrect");
            throw new AuthenticationException(msg, null, msg);
        }

        this.changeTheFuckingPassword(user, dto.getNewPassword());
    }

    private void changeTheFuckingPassword(User user, String newPassword) throws UserException {
        try {
            String hash = CryptoUtils.byteArrayToHexString(CryptoUtils.computeHash(newPassword));
            user.setPassword(hash);
            this.edit(user);
        } catch (Exception ex) {
            throw new UserException(ex.getMessage(), ex);
        }
    }

    private void sendRememberPasswordEmail(User user, String newPassword) throws EmailException {
        //Now that I changed the password, I'll send the user an email.
        Object[] arguments = {user.getName(), newPassword};
        generalService.sendEmail(user.getEmail(), arguments, "email_forgot_password_body", "email_forgot_password_subject");

    }

    private void sendActivateAccountEmail(User user) throws EmailException {
        String key = null;
        try {
            //Now that I changed the password, I'll send the user an email.
            key = EmailCipher.encode(user.getEmail());
        } catch (Exception ex) {
            LogHelper.makeLog("EmailCipher.encode Exception !!! -->  " + ex.getMessage(), ex);
        }
        String activationUrl = MetUtilHelper.buildActivationUrl(key);
        Object[] arguments = {user.getName(), activationUrl};
        generalService.sendEmail(user.getEmail(), arguments, "email_registration_verification_body", "email_registration_verification_subject");
    }

    //====================End Password Logic ================================//
    public void changeUserEmail(User user, String changeEmail) throws UserException {

        validateIfEmailIsRegistered(changeEmail);

        User userToEdit = this.findUserById(user.getId());

        try {
            userToEdit.setEmail(changeEmail.toLowerCase());
        } catch (Exception ex) {
            throw new UserException(ex.getMessage(), ex);
        }

    }

    /**
     * this method is in charge of performing the validations
     * and persisting the new user name in the data base.
     *
     * @param user to whom Im going to change its user name "nick"
     * @param userName is the new user name to persist
     * @throws UserException
     */
    public void changeUserName(User user, String userName) throws UserException {

        String errorMsg = validateUserName(userName);

        //if errorMsg is null or empty, then it means that the username is valid 100%
        if (StringUtils.isEmpty(errorMsg)) {
            User userToEdit = this.findUserById(user.getId());

            try {
                userToEdit.setNick(userName.toLowerCase());
            } catch (Exception ex) {
                throw new UserException(ex.getMessage(), ex);
            }
        } else {
            throw new UserException(errorMsg, null, errorMsg);
        }
    }

    /**
     * This method is in charge of performing multiple validations to the given
     * user name and in case a validation fails it should return a precise error
     * message to display to the client side.
     *
     * If the username is valid then an empty string will be returned.
     *
     * @param userName
     * @return
     */
    public String validateUserName(String userName) {

        //is it too short?
        if (userName.length() < 4) {
            return applicationParameters.getResourceBundleMessage("edit_username_info_to_short");
        }

        //is it too long?
        if (userName.length() > 40) {
            return applicationParameters.getResourceBundleMessage("edit_username_info_to_long");
        }

        //does it contain valid characters??
        if (!RegexUtil.isUserNickValid(userName)) {
            return applicationParameters.getResourceBundleMessage("edit_username_info_invalid");
        }

        //is it available
        if (!validateIfUserNameIsAvailable(userName)) {
            return applicationParameters.getResourceBundleMessage("edit_username_info_username_not_availabe");
        }

        return "";
    }

    public void deleteProfile(User user, String passwordToAuthenticate, String whyAreYouLeaving) throws UserException, Exception {
        User userToEdit = this.findUserById(user.getId());

        String userPassword = userToEdit.getPassword();

        try {
            this.arePasswordsEqual(passwordToAuthenticate, userPassword);
        } catch (AuthenticationException e) {
            String msg = applicationParameters.getResourceBundleMessage("delete_profile_info_password_incorrect");
            throw new AuthenticationException(msg, null, msg);
        }

        try {
            userToEdit.setState(UserState.DELETED);
        } catch (Exception e) {
            throw new UserException(e.getMessage(), e);
        }

        //Finally I log that we deleted the User.
        this.InterceptorService.logUserDelete(user, whyAreYouLeaving);

    }

    public void changeUserLocale(Integer userId, String locale) throws UserException {
        User userToEdit = this.findUserById(userId);

        try {
            userToEdit.setLocale(locale);
            this.edit(userToEdit);
        } catch (Exception ex) {
            throw new UserException(ex.getMessage(), ex);
        }

    }

    //====================Start Friend Logic ================================//
    /**
     * This method is the first part of the process to be friends with someone.
     * User 1 wants to be friends with User 2. So I create the friendship link in the
     * DB, but only as a pending. They are not real friends until User 2 confirms
     * the friendship.
     * 
     * Then, we fire the process to send a notification to the User 2, so he can decide
     * if he wants to be friends with User 1 or just cancel the fucking thing.
     *
     * And finally I send a Notification to User 2 so he can be aware of this event.
     *
     * @param idUser1 = User who is sending the friend request
     * @param idUser2 = User who is receiving the friend request
     * @throws FriendException
     * 
     */
    public void sendFriendRequest(int idUser1, int idUser2) throws FriendException {


        try {
            UserFriendPK pk1 = new UserFriendPK(idUser1, idUser2);
            UserFriend friend1 = new UserFriend(pk1);
            friend1.setDate(new Date());
            friend1.setState(UserFriendState.FRIENDSHIP_PENDING);

            UserFriendPK pk2 = new UserFriendPK(idUser2, idUser1);
            UserFriend friend2 = new UserFriend(pk2);
            friend2.setDate(new Date());
            friend2.setState(UserFriendState.FRIENDSHIP_PENDING_APPROVAL);

            em.persist(friend1);
            em.persist(friend2);
            em.flush();
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                //I am asuming that this is because a request existis and is pending.
                //so the the primary key already exists in the DB. I dont care, I'll
                //just tell him sure, everything is alright. Which means, I just ignore it.
            } else {
                throw new FriendException(e);
            }
        }


        //I notify the "idUser2" that a friend accepted his friend request. He has a new Friend !!!! wow
        this.notificationService.userNotificationNew(idUser2);

    }

    /**
     * Confirm Friend Request. So now User 2 got the friend request notification
     * from User 1 and decided to be friends with him and now here we are, in this
     * method. So what I do is that I change the state for their friendship link
     * that I created in the method "sendFriendRequest", to the state APPROVED,
     * so they can be considered friends.
     *
     * And finally I send the notification to user 1 that his friend confirmation
     * was approved.
     *
     * @param idUser1
     * @param idUser2
     * @throws FriendException
     */
    public void confirmFriendRequest(int idUser1, int idUser2) throws FriendException {

        try {

            Query q1 = null;
            q1 = em.createNamedQuery("UserFriend.updateFriendState");
            q1.setParameter("state", UserFriendState.FRIENDSHIP_APPROVED);
            q1.setParameter("idUser1", idUser1);
            q1.setParameter("idUser2", idUser2);

            if (q1.executeUpdate() == 0) {
                String errorMsg = "Error ... confirmFriendRequest failed, executeUpdate updated 0 entities for user idUser1: " + idUser1 + ", idUser2: " + idUser2;
                throw new FriendException(errorMsg);
            }

            Query q2 = null;
            q2 = em.createNamedQuery("UserFriend.updateFriendState");
            q2.setParameter("state", UserFriendState.FRIENDSHIP_APPROVED);
            q2.setParameter("idUser1", idUser2);
            q2.setParameter("idUser2", idUser1);

            if (q2.executeUpdate() == 0) {
                String errorMsg = "Error ... confirmFriendRequest failed, executeUpdate updated 0 entities for user idUser1: " + idUser2 + ", idUser2: " + idUser1;
                throw new FriendException(errorMsg);
            }

            em.flush();

        } catch (FriendException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FriendException(ex);
        }


        //Ok now that User 1 and User 2 are friends, I have to update their friend count.
        try {
            this.updateNumberOfFriends(idUser1, idUser2);
        } catch (Exception e) {
            //I dont care about this.
            LogHelper.makeLog(e);
        }


        //I notify the "idUser1" that a friend accepted his friend request. He has a new Friend !!!! wow

        //TODO ...
        //Yeah but I have to persist somehting in the Notification thing so that he can actually know that
        //his friend acepted the request
        //this.notificationService.userNotificationNew(idUser1);
    }

    /**
     *
     * Well ... someone doesn't like the other, so they use this method to delete the friend.
     * Simple as the friendship deleted, no hard feelings.
     *
     * @param idUser1
     * @param idUser2
     * @return
     * @throws FriendException
     * @throws FriendUpdateNumberException
     */
    public int deleteFriend(int idUser1, int idUser2) throws FriendException, FriendUpdateNumberException {

        try {

            Query q1 = null;
            q1 = em.createNamedQuery("UserFriend.deleteFriend");
            q1.setParameter("idUser1", idUser1);
            q1.setParameter("idUser2", idUser2);
            q1.executeUpdate();

            Query q2 = null;
            q2 = em.createNamedQuery("UserFriend.deleteFriend");
            q2.setParameter("idUser1", idUser2);
            q2.setParameter("idUser2", idUser1);
            q2.executeUpdate();

            em.flush();
        } catch (Exception e) {
            throw new FriendException(e);
        }

        //I notify the "idUser2" that there is a notification, so he should refresh his state.
        //this is actually if he is online so he can refresh his friend list. This is unseen to the user himself all
        //happens underneath, so he is unaware that someone deleted him.
        this.notificationService.userNotificationNew(idUser2);

        return this.updateNumberOfFriends(idUser1, idUser2);

    }

    private int updateNumberOfFriends(int idUser1, int idUser2) throws FriendUpdateNumberException {

        this.updateNumberOfFriendsLater(idUser2); // I update Asynchronously the second users friend count
        int userNewNumberOfFriends = this.updateNumberOfFriendsNow(idUser1); //I update now the first users friend count so I can send him now the number of friends he has.
        return userNewNumberOfFriends;
    }

    @Asynchronous
    private void updateNumberOfFriendsLater(Integer userId) {
        try {
            this.updateNumberOfFriendsNow(userId);
        } catch (Exception e) {
            LogHelper.makeLog("Fuck!!! updateNumberOfFriendsLater: " + e.getMessage(), e);
        }
    }

    private int updateNumberOfFriendsNow(Integer userId) throws FriendUpdateNumberException {
        int numberOfFriends = 0;
        try {

            UserFriendsSearchDTO criteria = new UserFriendsSearchDTO(userId);
            numberOfFriends = this.countFriendsByState(criteria);

            User user = findUserById(userId);
            user.setNumberOfFriends(numberOfFriends);
        } catch (Exception e) {
            throw new FriendUpdateNumberException(e);
        }

        return numberOfFriends;

    }

    /*
     *
     * This finds the friends a user has.
     *
     * @Param UserFriendsSearchDTO = it has just the "user id" to search for
     * return int = number of friends
     */
    public List<User> findUsersFriends(UserFriendsSearchDTO criteriaDTO) {
        TimeWatch watch = TimeWatch.start();

        List<User> answer = null;

        try {

            Query q = null;
            q = em.createNamedQuery("User.findFriendsByState");
            q.setParameter("id", criteriaDTO.getUserId());   //Id of the user of whom we are going to find the friends
            q.setParameter("state", criteriaDTO.getState()); //This is supposed to be Friendship Approved State

            answer = q.getResultList();

        } catch (Exception e) {
            answer = new ArrayList<User>();
            LogHelper.makeLog(e);
        }

        LogHelper.makeLog("-->QUERY UserService.findUsersFriends(UserFriendsSearchDTO criteriaDTO) time: " + watch.time());
        return answer;

    }

    public List<User> findUsersFriends(String nick) {
        TimeWatch watch = TimeWatch.start();
        List<User> answer = null;

        try {

            Query q = null;
            q = em.createNamedQuery("User.findFriendsByNick");
            q.setParameter("nick", nick);   //Id of the user of whom we are going to find the friends
            q.setParameter("state", UserFriendState.FRIENDSHIP_APPROVED); //This is supposed to be Friendship Approved State

            answer = q.getResultList();

        } catch (Exception e) {
            answer = new ArrayList<User>();
            LogHelper.makeLog(e);
        }

        LogHelper.makeLog("-->QUERY UserService.findUsersFriends(String nick) time: " + watch.time());
        return answer;

    }

    /*
     *
     * This counts the number of friends a user has
     *
     * @Param UserFriendsSearchDTO = it has just the "user id" to search for
     * return int = number of friends
     */
    public int countFriendsByState(UserFriendsSearchDTO criteriaDTO) throws FriendException {
        int answer = 0;
        try {

            answer = ((Number) em.createNamedQuery("User.countFriendsByState").
                    setParameter("id", criteriaDTO.getUserId()).setParameter("state", criteriaDTO.getState()).getSingleResult()).intValue();

        } catch (Exception e) {
            throw new FriendException(e);
        }

        return answer;
    }

    /**
     * This method is just a Wrapper from the method in the Notification Service.
     * I use it here, because I make this invocation from the ProfileMangedBean
     * and I prefer to add a wrapper method than to Inject the NotificationService
     * in the ProfileManagedBean just for the sake of invoking this method. Just not
     * worth it.
     *
     * This just turns off the notification sign for the given user.
     *
     * @param idUser
     */
    public void userNotificationRead(int idUser) {
        this.notificationService.userNotificationRead(idUser);
    }

    /**
     * Method that indicates if the user has notifications or not.
     * It just goes and reads the boolean parameter in the user table.
     *
     * This method for now is called upon every new page the user
     * navigates in the page to check if he has new notifications to view.
     *
     * @param idUser
     * @return NotificationDTO which contains the certain notifications a
     * user has. Remember, true = the user has notifications
     *                     false = the user does not have notifications.
     *
     */
    public NotificationDTO readUserNotifications(int idUser) {
        NotificationDTO answer = null;

        try {
            answer = (NotificationDTO) em.createNamedQuery("User.doesUserHaveNotifications").setParameter("id", idUser).getSingleResult();
        } catch (Exception e) {
            LogHelper.makeLog(e);
        }


        return answer;
    }

    //====================End Friend Logic ================================//
    //====================Start Ban functions =============================//
    /**
     * To Ban a user I have to do two things. One I have to create a UserBan
     * object, second I associate the UserBan object to the Users profile
     * and thats it.
     *
     * @param userBan
     * @throws UserException
     */
    public void banUser(UserBan userBan) throws UserException {

        try {

            em.persist(userBan);
            em.flush();

            Query q = em.createNamedQuery("User.updateUserBanState");
            q.setParameter("state", UserState.BANNED);
            q.setParameter("currentBanId", userBan.getId());
            q.setParameter("id", userBan.getBannedUserId());

            if (q.executeUpdate() == 0) {
                String errorMsg = "Error ... banUser failed, executeUpdate updated 0 entities for user id: " + userBan.getBannedUserId();
                throw new UserException(errorMsg);
            }

        } catch (Exception e) {
            throw new UserException(e);
        }

    }

    /**
     *
     * @param userToUnbanId
     * @throws UserException
     */
    /**
     * Method that unbans a user. I just need the Users ID so I can
     * change his state to Active and I set his current ban Id to NULL, which
     * means that he has no active ban pending.
     *
     * @param userToUnbanId
     * @throws UserException = means that the user was not unbanned successfully
     */
    public void unbanUser(Integer userToUnbanId) throws UserException {


        try {

            Query q = em.createNamedQuery("User.updateUserBanState");
            q.setParameter("state", UserState.ACTIVE);
            q.setParameter("currentBanId", null);
            q.setParameter("id", userToUnbanId);

            if (q.executeUpdate() == 0) {
                String errorMsg = "Error ... unbanUser failed, executeUpdate updated 0 entities for user id: " + userToUnbanId;
                throw new UserException(errorMsg);
            }

        } catch (Exception e) {
            throw new UserException(e);
        }

    }

    /**
     * It verifies the cases if the User is Banned. Has his ban expired or not?
     * If his ban has expired then I just update his user state so that his ban is lifted.
     *
     * If his ban has not expired then I show the user a message to telling him when to come
     * back.
     *
     * @param userId = the banned user id
     * @param currentBanId = where the ban info is.
     * @throws UserException = This means that the User is Banned. It also comes with the corresponding
     * info message so that it can be displayed to the User.
     */
    private void verifyIfUserBanIsOn(Integer userId, Integer currentBanId) throws UserException, UserIsBannedException {

        boolean isBanStillOn = true;

        //Check 1
        if (DataHelper.isNull(currentBanId)) {
            //This shouldn't happen its an inconsistency, because, we are saying
            //that the user is banned but he has no Ban Id in his profile, so Im, just
            //totally unbanning him and thats it.
            isBanStillOn = false;

        } else {

            //Check 2
            UserBan userBan = this.em.find(UserBan.class, currentBanId);
            if (userBan == null) {
                isBanStillOn = false;
            } else if (!UtilHelper.areIntegersEqual(userId, userBan.getBannedUserId())) {
                //Just to make sure things are straight and their are no mis understandings, that a ban is put
                //on the wrong user. THis should never happen.
                isBanStillOn = false;
            }

            Date banExpirationDate = userBan.getBanExpirationDate();
            if (isBanStillOn) {
                //Check 3
                boolean isTimeUP = UtilHelper.isTimeUp(banExpirationDate);

                if (!isTimeUP) {
                    //After all the Checks he didnt make, which means the User is still Banned.
                    //Im banning him.
                    String msg = this.buildUserBanMessage(banExpirationDate, userBan.getWhy());
                    throw new UserIsBannedException(msg, null, msg);
                }

            }
        }

        //Finally the guy passed all the Checks and he is free to go. So I unban him
        this.unbanUser(userId);

    }

    /**
     * This method, receives the user ban expiration date, so I calculate the number
     * of minutes the user has left before his ban expires, so I can put them
     * in the ban info message, so the user can know when he can be accepted back in.
     *
     * If the on the other hand the ban expiration date is NULL, this means the user
     * has a permanent ban, so I just tell him to come back in 666 minutes.
     * Which he will receive every time he tries to log in.
     *
     * @param banExpirationDate = Date the the user's ban expires.
     * @return ban info message.
     *
     */
    private String buildUserBanMessage(Date banExpirationDate, String banReason) {
        String answer = null;

        Long differenceInMinutes = UtilHelper.dateDifferenceInMinutes(banExpirationDate);

        if (differenceInMinutes == null) {
            //THis means that the user has a permanent Ban.
            String a1000YearsJajaja = applicationParameters.getResourceBundleMessage("ban_indefinitely"); // jajajajaajaja So I tell him to go fuck himself jajaja
            answer = applicationParameters.getResourceBundleMessage("banned_info_user_login_message", a1000YearsJajaja);

        } else {
            String banInMinutes = applicationParameters.getResourceBundleMessage("ban_x_minutes", differenceInMinutes);

            if (StringUtils.isNotEmpty(banReason)) {
                answer = banReason + applicationParameters.getResourceBundleMessage("ban_comeback_in", banInMinutes);
            } else {
                answer = applicationParameters.getResourceBundleMessage("banned_info_user_login_message", banInMinutes);
            }
        }




        return answer;
    }

    /**
     * Method that given a user email activates his account. It basically changes
     * a users state from PENDING to ACTIVE.
     * 
     * @param email of the user who's account is pending and we are going to activate
     * @return true = account activated
     *         false = account not activated
     *
     */
    public boolean activateUserAccount(String email) {

        boolean answer = false;
        try {
            User userToActivate = findUserByEmail(email);

            //Only if the users state is pending can I activate him.
            if (UserState.isPending(userToActivate.getState())) {

                try {
                    //I create the user File system user folders
                    this.generalService.createUserFileSystem(userToActivate.getId());
                    userToActivate.setState(UserState.ACTIVE); //I change his state to active.
                    this.edit(userToActivate);
                    answer = true; //I only set this if the user was activated correctly

                } catch (IOException ex) {
                    LogHelper.makeLog(ex);
                }

            }

        } catch (UserException ex) {
            LogHelper.makeLog("Activate User Account failed: " + ex.getMessage());
        }

        return answer;

    }

    /**
     * Generates a 17 character  long numeric string
     *
     * Example: 79110911232015057
     *
     * 79 -->random
     * 110911 --> yyMMdd
     * 232015057 --> hhMMssSSS
     *
     */
    private String generateUserId() {
        Calendar cal = Calendar.getInstance();
        FormatHelper f = new FormatHelper();
        return RandomStringUtilsExt.randomNumeric(2).concat(f.format_yyMMddHHmmssSSS.format(cal.getTime()));
    }

    /**
     * Generates an id for the user based on the users ID and the current date.
     *
     * Example: 128579
     *
     * 1285 --> yDDD
     * 79 --> id
     *
     */
    private String generateUserId(Integer id) {
        Calendar cal = Calendar.getInstance();
        FormatHelper f = new FormatHelper();
        return f.format_yDDD.format(cal.getTime()).concat(id.toString()); //yDDD
    }
    //====================End Ban functions =============================//

    //=========== TEMP Functions========================================//
    public Integer getLastCreateUserId() throws UserException {


        Integer answer = null;
        String query = "SELECT id FROM met_user order by id desc LIMIT 1 ";

        try {

            Query searchQuery = (Query) em.createNativeQuery(query);
            Object searchResult = searchQuery.getSingleResult();

            answer = (Integer) searchResult;
        } catch (Exception ex) {
            throw new UserException(ex);
        }

        return answer;

    }

    public List<Integer> findUserIdByRange(Integer uId1, Integer uId2) throws UserException {


        List<Integer> answer = new ArrayList();
        String query = "SELECT id FROM met_user where id >= ?1 and id < ?2 ";



        try {

            Query searchQuery = (Query) em.createNativeQuery(query);
            searchQuery.setParameter(1, uId1);
            searchQuery.setParameter(2, uId2);

            List<Integer> searchResult = searchQuery.getResultList();

            answer = searchResult;
//            Object[] ab = (Object[]) searchResult;
//
//
//            answer.setId((Integer) ab[0]);
//            answer.setCountryCode((String) ab[1]);
//            answer.setRegionCode((String) ab[2]);
//            answer.setCity((String) ab[3]);
//            answer.setZipcode((String) ab[4]);



        } catch (Exception ex) {
            throw new UserException(ex);
        }

        return answer;

    }

    public void createFriends(int idUser1, int idUser2) throws FriendException {


        try {
            UserFriendPK pk1 = new UserFriendPK(idUser1, idUser2);
            UserFriend friend1 = new UserFriend(pk1);
            friend1.setDate(new Date());
            friend1.setState(UserFriendState.FRIENDSHIP_APPROVED);

            UserFriendPK pk2 = new UserFriendPK(idUser2, idUser1);
            UserFriend friend2 = new UserFriend(pk2);
            friend2.setDate(new Date());
            friend2.setState(UserFriendState.FRIENDSHIP_APPROVED);

            em.persist(friend1);
            em.persist(friend2);

        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                //I am asuming that this is because a request existis and is pending.
                //so the the primary key already exists in the DB. I dont care, I'll
                //just tell him sure, everything is alright. Which means, I just ignore it.
            } else {
                throw new FriendException(e);
            }
        }

        try {
            this.updateNumberOfFriends(idUser1, idUser2);

        } catch (Exception e) {
            //i dont care
        }



    }

    public boolean blockUserForFailedLoginAttempts(String email) {

        boolean answer = false;
        try {

            Query q = null;

            Date date2 = new Date();
            Date date1 = addMinute(date2, -5);
            q = em.createNamedQuery("LogUserAuthentication.countUserFailedLoginAttempts");
            q.setParameter("email", email);
            q.setParameter("date1", date1);
            q.setParameter("date2", date2);
            q.setParameter("result", 0);

            int count = ((Number) q.getSingleResult()).intValue();

            if (count >= 3) {
                answer = true; //the alias is not taken
            }

        } catch (Exception e) {
            //I cant to anything.
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }

    private Date addHour(Date date1, int hour) {

        GregorianCalendar date = new GregorianCalendar();
        date.setTime(date1);
        date.add(Calendar.HOUR, hour);

        return date.getTime();
    }

    private Date addMinute(Date date1, int minute) {

        GregorianCalendar date = new GregorianCalendar();
        date.setTime(date1);
        date.add(Calendar.MINUTE, minute);

        return date.getTime();
    }

    //===================================remember me===========================//
    public void rememberSave(String uuid, Integer userId) {
        UserSession us = new UserSession(uuid, userId, new Date());
        em.persist(us);
    }

    public void rememberDelete(Integer userId) {
        Query q1 = em.createNamedQuery("UserSession.deleteSession");
        q1.setParameter("userId", userId);
        q1.executeUpdate();
    }

    public Integer rememberFind(String uuid) {
        Integer userId = null;
        try {

            UserSession us = (UserSession) em.createNamedQuery("UserSession.findByUuid").
                    setParameter("uuid", uuid).getSingleResult();
            userId = us.getUserId();

        } catch (Exception ex) {

            if (ex instanceof NonUniqueResultException) {
            }

        }
        return userId;
    }
}
