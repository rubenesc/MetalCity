/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.entity;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.service.dto.Ext;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;

/**
 *
 * @author Ruben
 */
@Entity
@Cache(type = CacheType.SOFT, size = 64000, expiry = 36000000, coordinationType = CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS)
@Table(name = "met_user")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findUserActiveById", query = "SELECT u FROM User u WHERE u.id = :id and u.state = :state"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name"),
    @NamedQuery(name = "User.findByNick", query = "SELECT u FROM User u WHERE u.nick = :nick "),
    @NamedQuery(name = "User.findActiveUserIdByNick", query = "SELECT u.id FROM User u WHERE u.nick = :nick and  u.state = :state "),
    @NamedQuery(name = "User.countUsersByNick", query = "SELECT COUNT(u.id) FROM User u WHERE u.nick = :nick"),
    @NamedQuery(name = "User.findByBirthday", query = "SELECT u FROM User u WHERE u.birthday = :birthday"),
    @NamedQuery(name = "User.findByCountry", query = "SELECT u FROM User u WHERE u.country = :country"),
    @NamedQuery(name = "User.findByCity", query = "SELECT u FROM User u WHERE u.city = :city"),
    @NamedQuery(name = "User.findByRegistrationDate", query = "SELECT u FROM User u WHERE u.registrationDate = :registrationDate"),
    @NamedQuery(name = "User.findByState", query = "SELECT u FROM User u WHERE u.state = :state"),
    @NamedQuery(name = "User.findByProfile", query = "SELECT u FROM User u WHERE u.profile = :profile"),
    @NamedQuery(name = "User.findBySex", query = "SELECT u FROM User u WHERE u.sex = :sex"),
    @NamedQuery(name = "User.findByAvatar", query = "SELECT u FROM User u WHERE u.avatar = :avatar"),
    @NamedQuery(name = "User.findFriendsByState", query = "SELECT u FROM User u, UserFriend uf WHERE uf.user2.id = u.id and uf.user1.id = :id and uf.state = :state"),
    @NamedQuery(name = "User.findFriendsByNick", query = "SELECT u FROM User u, UserFriend uf WHERE uf.user2.nick = u.nick and uf.user1.nick = :nick and uf.state = :state"),
    @NamedQuery(name = "User.countFriendsByState", query = "Select COUNT(DISTINCT uf.user2.id) From UserFriend uf Where uf.user1.id = :id and  uf.state = :state"),
    @NamedQuery(name = "User.doesUserHaveNotifications", query = "Select new co.com.metallium.core.service.dto.NotificationDTO( u.notifications, u.newMessage) From User u Where u.id = :id "),
    @NamedQuery(name = "User.updateUserNotifications", query = "Update User AS u set u.notifications = :notifications WHERE u.id = :id"),
    @NamedQuery(name = "User.updateUserNewMessage", query = "Update User AS u set u.newMessage = :newMessage WHERE u.id = :id"),
    @NamedQuery(name = "User.updateUserState", query = "Update User AS u set u.state = :state WHERE u.id = :id"),
    @NamedQuery(name = "User.updateUserBanState", query = "Update User AS u set u.state = :state, u.currentBanId = :currentBanId WHERE u.id = :id")
})
public class User extends Ext implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "nick")
    private String nick;
    @Column(name = "birthday")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthday;
    @Column(name = "birthday_display")
    private Integer birthdayDispay;
    @Column(name = "city")
    private String city;
    @Basic(optional = false)
    @Column(name = "registration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;
    @Basic(optional = false)
    @Column(name = "last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @Basic(optional = false)
    @Column(name = "state")
    private String state;
    @Column(name = "sex")
    private String sex;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "avatar_profile")
    private String avatarProfile;
    @Column(name = "network")
    private Integer network;
    @Column(name = "locale")
    private String locale;
    @Column(name = "whats_on_your_mind")
    private String whatsOnYourMind;
    @Column(name = "number_of_friends")
    private Integer numberOfFriends;
    @Basic(optional = false)
    @Column(name = "notifications")
    private Boolean notifications;
    @Basic(optional = false)
    @Column(name = "new_message")
    private Boolean newMessage;
    @Column(name = "current_ban_id")
    private Integer currentBanId;
//    @JoinColumn(name = "profile", referencedColumnName = "id")
//    @ManyToOne(optional = false, fetch=FetchType.EAGER)
//    private Profile profile;
    @Basic(optional = false)
    @Column(name = "profile")
    private Integer profile;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<News> newsCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private UserFavorite userFavorite;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private UserOpinion userOpinion;
    @JoinColumn(name = "country", referencedColumnName = "iso")
    @ManyToOne(fetch = FetchType.LAZY)
    private Country country;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<ImageGallery> imageGalleryCollection;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user1", fetch=FetchType.LAZY)
//    private Collection<UserFriend> UserFriendsCollection;
    //Right now I want to handle this manualy.
    @Transient
    private List<User> userFriendsCollection;
    @Transient
    String sessionId;
    @Transient
    String ipAddress; //(IP) address of the client or last proxy that sent the request.
    @Transient
    Integer locationId;
    //Search properties.
    @Transient
    public static String entitySearchQueryFragment = "SELECT u FROM User u";
    @Transient
    public static String entityCountQueryFragment = "Select COUNT(DISTINCT u.id) FROM User u ";
    @Transient
    public static String entityOrderByQueryFragment = " order by u.id DESC";
    @Transient
    public static String entityAddOnQueryFragment1 = ", UserFriend uf ";

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(String nick) {
        this.nick = nick;
    }

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(Integer id, String email, String name, String password, Date registrationDate, String state) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.registrationDate = registrationDate;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        if (email != null){
            return email.toLowerCase(); //email should always be in lower case
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getBirthdayDispay() {
        return birthdayDispay;
    }

    public void setBirthdayDispay(Integer birthdayDispay) {
        this.birthdayDispay = birthdayDispay;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
    
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getProfile() {
        return profile;
    }

    public void setProfile(Integer profile) {
        this.profile = profile;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        //1. context (in this case "metallium")
        //2. (word "image" because this is the name of the Servlettttt)
        //3. image file system relative path based on the user id
        //4. name of the image

//        this.setAvatarMedLocation(MetConfiguration.WEB_IMAGE_SERVLET_PATH.concat(MetUtilHelper.getNewFileNameKeepExtention(avatar, UserCommon.PROFILE_PIC_MED)));
//        this.setAvatarSmallLocation(MetConfiguration.WEB_IMAGE_SERVLET_PATH.concat(MetUtilHelper.getNewFileNameKeepExtention(avatar, UserCommon.PROFILE_PIC_SMALL)));
    }

    public String getAvatarLocation() {
        try {
            return MetConfiguration.WEB_IMAGE_SERVLET_PATH.concat(this.getAvatar());
        } catch (Exception e) {
            return MetConfiguration.WEB_IMAGE_SERVLET_PATH.concat(MetConfiguration.DEFAULT_USER_PROFILE_AVATAR_PIC);
        }
    }

    public String getAvatarProfile() {
        return avatarProfile;
    }

    public void setAvatarProfile(String avatarProfile) {
        this.avatarProfile = avatarProfile;
    }

    public String getAvatarProfileLocation() {
        try {
            return MetConfiguration.WEB_IMAGE_SERVLET_PATH.concat(this.getAvatarProfile());
        } catch (Exception e) {
            return "";
        }
    }

    public String getWhatsOnYourMind() {
        return whatsOnYourMind;
    }

    public void setWhatsOnYourMind(String whatsOnYourMind) {
        this.whatsOnYourMind = whatsOnYourMind;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Integer getNetwork() {
        return network;
    }

    public void setNetwork(Integer network) {
        this.network = network;
    }

    public Integer getCurrentBanId() {
        return currentBanId;
    }

    public void setCurrentBanId(Integer currentBanId) {
        this.currentBanId = currentBanId;
    }

    public Collection<News> getNewsCollection() {
        return newsCollection;
    }

    public void setNewsCollection(Collection<News> newsCollection) {
        this.newsCollection = newsCollection;
    }

    public UserFavorite getUserFavorite() {
        return userFavorite;
    }

    public void setUserFavorite(UserFavorite userFavorite) {
        this.userFavorite = userFavorite;
    }

    public UserOpinion getUserOpinion() {
        return userOpinion;
    }

    public void setUserOpinion(UserOpinion userOpinion) {
        this.userOpinion = userOpinion;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Integer getNumberOfFriends() {
        return numberOfFriends;
    }

    public void setNumberOfFriends(Integer numberOfFriends) {
        this.numberOfFriends = numberOfFriends;
    }

    public Boolean getNotifications() {
        return notifications;
    }

    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }

    public Boolean getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(Boolean newMessage) {
        this.newMessage = newMessage;
    }

    public Collection<ImageGallery> getImageGalleryCollection() {
        return imageGalleryCollection;
    }

    public void setImageGalleryCollection(Collection<ImageGallery> imageGalleryCollection) {
        this.imageGalleryCollection = imageGalleryCollection;
    }

    public List<User> getUserFriendsCollection() {
        return userFriendsCollection;
    }

    public void setUserFriendsCollection(List<User> userFriendsCollection) {
        this.userFriendsCollection = userFriendsCollection;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        //If I send an object, I have to make sure that it is a user Object.
        if (!(object instanceof User)) {
            return false;
        }
        //If its a user Object I validate that the Ids are equal
        User other = (User) object;


        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            if ((this.nick == null && other.nick != null) || (this.nick != null && !this.nick.equalsIgnoreCase(other.nick))) {

                return false;

            }
        }

        return true;

    }

    @Override
    public String toString() {
        return "co.com.metallium.entity.User[id=" + id + "]";
    }

}