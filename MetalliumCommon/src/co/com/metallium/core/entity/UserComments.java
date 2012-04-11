/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Cache(type=CacheType.SOFT, size=64000, expiry=36000000, coordinationType=CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS)
@Table(name = "met_user_comments")
@NamedQueries({
    @NamedQuery(name = "UserComments.findAll", query = "SELECT u FROM UserComments u"),
    @NamedQuery(name = "UserComments.findById", query = "SELECT u FROM UserComments u WHERE u.id = :id"),
    @NamedQuery(name = "UserComments.findByEntityId", query = "SELECT u FROM UserComments u WHERE u.entityId = :entityId"),
    @NamedQuery(name = "UserComments.findByDate", query = "SELECT u FROM UserComments u WHERE u.date = :date"),
    @NamedQuery(name = "UserComments.findByType", query = "SELECT u FROM UserComments u WHERE u.type = :type"),
    @NamedQuery(name = "UserComments.deleteComment", query = "DELETE FROM UserComments u WHERE u.id = :id"),
    @NamedQuery(name = "UserComments.deleteAllCommentsFromWall", query = "DELETE FROM UserComments u WHERE u.entityId = :entityId"),
    @NamedQuery(name = "UserComments.softDeleteAllCommentsFromWall", query = "UPDATE UserComments u SET u.state = :state WHERE u.entityId = :entityId"),
    @NamedQuery(name = "UserComments.deleteAllCommentsFromImageWall", query = "DELETE FROM UserComments u WHERE u.entityId = :entityId and u.imageId = :imageId"),
    @NamedQuery(name = "UserComments.findByState", query = "SELECT u FROM UserComments u WHERE u.state = :state")})

    public class UserComments implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "entity_id")
    private Integer entityId;

    @Basic(optional = false)
    @Column(name = "image_id")
    private String imageId;

    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Basic(optional = false)
    @Column(name = "type")
    private String type;

    @Basic(optional = false)
    @Lob
    @Column(name = "comment")
    private String comment;

    @Basic(optional = false)
    @Column(name = "state")
    private String state;

    @Column(name = "url")
    private String url;
    @Column(name = "media_id")
    private String mediaId;
    @Column(name = "media_title")
    private String mediaTitle;
    @Column(name = "media_description")
    private String mediaDescription;
    @Column(name = "media_thumbnail_1")
    private String mediaThumbnail1;
    @Column(name = "media_thumbnail_2")
    private String mediaThumbnail2;
    
    
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch=FetchType.EAGER) //Its eager because when you get a comment it usually to show it on a wall, and you always need info from the user.
    private User user;

    //Search properties.
    @Transient
    public static String entitySearchQueryFragment = "SELECT n FROM UserComments n ";
    @Transient
    public static String entityCountQueryFragment = "Select COUNT(DISTINCT n.id) FROM UserComments n ";
    @Transient
    public static String entityOrderByQueryFragment =  " order by n.date desc";

    @Transient
    private boolean playMedia = false;


    public UserComments() {
    }

    public UserComments(Integer id) {
        this.id = id;
    }

    public UserComments(Integer id, int entityId, Date date, String type, String comment, String state) {
        this.id = id;
        this.entityId = entityId;
        this.date = date;
        this.type = type;
        this.comment = comment;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMediaDescription() {
        return mediaDescription;
    }

    public void setMediaDescription(String mediaDescription) {
        this.mediaDescription = mediaDescription;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaThumbnail1() {
        return mediaThumbnail1;
    }

    public void setMediaThumbnail1(String mediaThumbnail1) {
        this.mediaThumbnail1 = mediaThumbnail1;
    }

    public String getMediaThumbnail2() {
        return mediaThumbnail2;
    }

    public void setMediaThumbnail2(String mediaThumbnail2) {
        this.mediaThumbnail2 = mediaThumbnail2;
    }

    public boolean isPlayMedia() {
        return playMedia;
    }

    public void setPlayMedia(boolean playMedia) {
        this.playMedia = playMedia;
    }
    
    public String getMediaTitle() {
        return mediaTitle;
    }

    public void setMediaTitle(String mediaTitle) {
        this.mediaTitle = mediaTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        if (!(object instanceof UserComments)) {
            return false;
        }
        UserComments other = (UserComments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.UserComments[id=" + id + "]";
    }

}
