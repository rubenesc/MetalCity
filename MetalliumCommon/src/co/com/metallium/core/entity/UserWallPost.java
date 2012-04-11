/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.entity;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.UserWallPostTypeEnum;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
 * 20110814
 * @author Ruben
 */
@Entity
@Cache(type = CacheType.SOFT, size = 64000, expiry = 36000000, coordinationType = CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS)
@Table(name = "met_user_wall_post")
@NamedQueries({
    @NamedQuery(name = "UserWallPost.findAll", query = "SELECT u FROM UserWallPost u"),
    @NamedQuery(name = "UserWallPost.findById", query = "SELECT u FROM UserWallPost u WHERE u.id = :id"),
    @NamedQuery(name = "UserWallPost.findByPostIdAndUserId", query = "SELECT u FROM UserWallPost u WHERE u.id = :id and u.userId = :userId"),
    @NamedQuery(name = "UserWallPost.findByUserId", query = "SELECT u FROM UserWallPost u WHERE u.userId = :userId order by u.date desc ")})
public class UserWallPost implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "url")
    private String url;
    @Column(name = "media_id")
    private String mediaId;
    @Column(name = "image")
    private String image;
    @Column(name = "image_thumbnail")
    private String imageThumbnail;
    @Lob
    @Column(name = "content")
    private String content;
    @Column(name = "media_title")
    private String mediaTitle;
    @Column(name = "media_description")
    private String mediaDescription;
    @Column(name = "media_thumbnail_1")
    private String mediaThumbnail1;
    @Column(name = "media_thumbnail_2")
    private String mediaThumbnail2;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private UserWallPostTypeEnum type;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;
    //Search properties.
    @Transient
    public static String entitySearchQueryFragment = "SELECT u FROM UserWallPost u ";
    @Transient
    public static String entityCountQueryFragment = "Select COUNT(DISTINCT u.id) FROM UserWallPost u ";
    @Transient
    public static String entityOrderByQueryFragment = " order by u.date desc";
    @Transient
    private boolean playMedia = false;

    public UserWallPost() {
        type = UserWallPostTypeEnum.VIDEO; //Default option when Im going to create a Wall Post
    }

    public UserWallPost(Integer id) {
        this.id = id;
    }

    public UserWallPost(Integer id, String content, Date date, Integer userId) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageLocation() {
        try {
            return MetConfiguration.WEB_IMAGE_SERVLET_PATH.concat(this.getImage());
        } catch (Exception e) {
            return "";
        }
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    public String getImageThumbnailLocation() {
        try {
            return MetConfiguration.WEB_IMAGE_SERVLET_PATH.concat(this.getImageThumbnail());
        } catch (Exception e) {
            return "";
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UserWallPostTypeEnum getType() {
        return type;
    }

    public void setType(UserWallPostTypeEnum type) {
        this.type = type;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getMediaDescription() {
        return mediaDescription;
    }

    public void setMediaDescription(String mediaDescription) {
        this.mediaDescription = mediaDescription;
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

    public String getMediaTitle() {
        return mediaTitle;
    }

    public void setMediaTitle(String mediaTitle) {
        this.mediaTitle = mediaTitle;
    }

    public boolean isPlayMedia() {
        return playMedia;
    }

    public void setPlayMedia(boolean playMedia) {
        this.playMedia = playMedia;
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
        if (!(object instanceof UserWallPost)) {
            return false;
        }
        UserWallPost other = (UserWallPost) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.UserWallPost[id=" + id + "]";
    }
}
