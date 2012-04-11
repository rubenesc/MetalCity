/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.entity.jukebox;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;

/**
 *
 * @author Ruben
 */
@Entity
@Cache(type=CacheType.SOFT, size=64000, expiry=36000000, coordinationType=CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS)
@Table(name = "met_juke_media")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JukeMedia.findAll", query = "SELECT j FROM JukeMedia j"),
    @NamedQuery(name = "JukeMedia.findById", query = "SELECT j FROM JukeMedia j WHERE j.id = :id"),
    @NamedQuery(name = "JukeMedia.findByMediaId", query = "SELECT j FROM JukeMedia j WHERE j.mediaId = :mediaId"),
    @NamedQuery(name = "JukeMedia.findByTitle", query = "SELECT j FROM JukeMedia j WHERE j.title = :title"),
    @NamedQuery(name = "JukeMedia.findByUrl", query = "SELECT j FROM JukeMedia j WHERE j.url = :url"),
    @NamedQuery(name = "JukeMedia.findByKeywords", query = "SELECT j FROM JukeMedia j WHERE j.keywords = :keywords"),
    @NamedQuery(name = "JukeMedia.findByThumbnail1", query = "SELECT j FROM JukeMedia j WHERE j.thumbnail1 = :thumbnail1"),
    @NamedQuery(name = "JukeMedia.findByThumbnail2", query = "SELECT j FROM JukeMedia j WHERE j.thumbnail2 = :thumbnail2"),
    @NamedQuery(name = "JukeMedia.findByDuration", query = "SELECT j FROM JukeMedia j WHERE j.duration = :duration"),
    @NamedQuery(name = "JukeMedia.findByCreated", query = "SELECT j FROM JukeMedia j WHERE j.created = :created"),
    @NamedQuery(name = "JukeMedia.findByLastModified", query = "SELECT j FROM JukeMedia j WHERE j.lastModified = :lastModified")})
public class JukeMedia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "media_id")
    private String mediaId;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Lob
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "url")
    private String url;
    @Column(name = "keywords")
    private String keywords;
    @Column(name = "thumbnail1")
    private String thumbnail1;
    @Column(name = "thumbnail2")
    private String thumbnail2;
    @Column(name = "duration")
    private Integer duration;
    @Basic(optional = false)
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Basic(optional = false)
    @Column(name = "last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jukeMedia")
//    private Collection<JukePlaylistMedia> jukePlaylistMediaCollection;

    public JukeMedia() {
    }

    public JukeMedia(Integer id) {
        this.id = id;
    }

    public JukeMedia(Integer id, String mediaId, String title, String url, Date created, Date lastModified) {
        this.id = id;
        this.mediaId = mediaId;
        this.title = title;
        this.url = url;
        this.created = created;
        this.lastModified = lastModified;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getThumbnail1() {
        return thumbnail1;
    }

    public void setThumbnail1(String thumbnail1) {
        this.thumbnail1 = thumbnail1;
    }

    public String getThumbnail2() {
        return thumbnail2;
    }

    public void setThumbnail2(String thumbnail2) {
        this.thumbnail2 = thumbnail2;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

//    @XmlTransient
//    public Collection<JukePlaylistMedia> getJukePlaylistMediaCollection() {
//        return jukePlaylistMediaCollection;
//    }
//
//    public void setJukePlaylistMediaCollection(Collection<JukePlaylistMedia> jukePlaylistMediaCollection) {
//        this.jukePlaylistMediaCollection = jukePlaylistMediaCollection;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JukeMedia)) {
            return false;
        }
        JukeMedia other = (JukeMedia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.temp.juke.JukeMedia[ id=" + id + " ]";
    }
    
}
