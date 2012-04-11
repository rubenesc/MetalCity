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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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
@Table(name = "met_news")
@NamedQueries({
    @NamedQuery(name = "News.findAll", query = "SELECT n FROM News n order by n.date desc"),
    @NamedQuery(name = "News.findById", query = "SELECT n FROM News n WHERE n.id = :id"),
    @NamedQuery(name = "News.findByAlias", query = "SELECT n FROM News n WHERE n.alias = :alias"),
    @NamedQuery(name = "News.findByTitle", query = "SELECT n FROM News n WHERE n.title = :title"),
    @NamedQuery(name = "News.findByDate", query = "SELECT n FROM News n WHERE n.date = :date"),
    @NamedQuery(name = "News.findByCoverImage", query = "SELECT n FROM News n WHERE n.coverImage = :coverImage"),
    @NamedQuery(name = "News.findNewsByState", query = "Select n From News n Where n.state = :state order by n.date desc"),
    @NamedQuery(name = "News.countNewsByState", query = "Select COUNT(DISTINCT n.id) From News n Where n.state = :state "),
    @NamedQuery(name = "News.findByState", query = "SELECT n FROM News n WHERE n.state = :state"),

    //These two count queries names must no be changed. If they do, then verify that both end with the 'Create' and 'Edit' word respectively
    //and as update the News.namedQueryCountAlias constant
    @NamedQuery(name = "News.countNewsByAliasCreate", query = "Select COUNT(DISTINCT n.id) From News n Where n.alias = :alias"),
    @NamedQuery(name = "News.countNewsByAliasEdit", query = "Select COUNT(DISTINCT n.id) From News n Where n.alias = :alias and n.id <> :id ")


})
public class News extends Ext implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "alias")
    private String alias;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @Lob
    @Column(name = "content")
    private String content;
    @Basic(optional = false)
    @Lob
    @Column(name = "preview")
    private String preview;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "cover_image")
    private String coverImage;
    @Column(name = "cover_image_thumbnail")
    private String coverImageThumbnail;
    @Column(name = "media_url")
    private String mediaUrl;
    @Column(name = "source_url")
    private String sourceUrl;
    @Basic(optional = false)
    @Column(name = "state")
    private String state;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User user;
    @Column(name = "number_comments")
    private Integer numberComments;

    @JoinTable(name = "met_news_network", joinColumns = {
        @JoinColumn(name = "id_news", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "id_network", referencedColumnName = "id")})
    @ManyToMany(fetch=FetchType.LAZY)
    private Collection<Network> networkCollection;



    //Search properties.
    @Transient
    public static String entitySearchQueryFragment = "SELECT u FROM News u ";
    @Transient
    public static String entityCountQueryFragment = "Select COUNT(DISTINCT u.id) FROM News u ";
    @Transient
    public static String entityOrderByQueryFragment =  " order by u.date desc";

    @Transient
    public static String entityAddOnQueryFragment1 = ", NewsNetwork nn ";

    //extra. this query is used in a method called 'generateAliasForTitle'
    @Transient
    public static String namedQueryCountAlias = "News.countNewsByAlias";

    public News() {
    }

    public News(Integer id) {
        this.id = id;
    }

    public News(Integer id, String title, String content, Date date, String state) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getCoverImageLocation() {
        try {
            return MetConfiguration.WEB_IMAGE_SERVLET_PATH.concat(this.getCoverImage());
        } catch (Exception e) {
            return "";
        }
    }

    public String getCoverImageThumbnail() {
        return coverImageThumbnail;
    }

    public void setCoverImageThumbnail(String coverImageThumbnail) {
        this.coverImageThumbnail = coverImageThumbnail;
    }

    public String getCoverImageThumbnailLocation() {
        try {
            return MetConfiguration.WEB_IMAGE_SERVLET_PATH.concat(this.getCoverImageThumbnail());
        } catch (Exception e) {
            return "";
        }
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getNumberComments() {
        return numberComments;
    }

    public void setNumberComments(Integer numberComments) {
        this.numberComments = numberComments;
    }

    public Collection<Network> getNetworkCollection() {
        return networkCollection;
    }

    public void setNetworkCollection(Collection<Network> networkCollection) {
        this.networkCollection = networkCollection;
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
        if (!(object instanceof News)) {
            return false;
        }
        News other = (News) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.entity.News[id=" + id + "]";
    }

}
