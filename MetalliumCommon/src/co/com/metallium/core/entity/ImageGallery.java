/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.entity;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.service.dto.ImageDTO;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;

/**
 *
 * @author Ruben
 */
@Entity
@Cache(type=CacheType.SOFT, size=64000, expiry=36000000, coordinationType=CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS)
@Table(name = "met_image_gallery")
@NamedQueries({
    @NamedQuery(name = "ImageGallery.findAll", query = "SELECT i FROM ImageGallery i"),
    @NamedQuery(name = "ImageGallery.findById", query = "SELECT i FROM ImageGallery i WHERE i.id = :id"),
    @NamedQuery(name = "ImageGallery.findByState", query = "SELECT i FROM ImageGallery i WHERE i.state = :state"),
    @NamedQuery(name = "ImageGallery.findGalleriesWhereUserIsOwner", query = "Select i From ImageGallery i Where i.user.id = :userId order by i.date desc"),
    @NamedQuery(name = "ImageGallery.countGalleriesWhereUserIsOwner", query = "Select COUNT(DISTINCT i.id) From ImageGallery i Where i.user.id = :userId"),
    @NamedQuery(name = "ImageGallery.findGalleriesWhereUserIsAnonymous", query = "Select i From ImageGallery i Where i.user.id = :userId and i.state = :state order by i.date desc"),
    @NamedQuery(name = "ImageGallery.countGalleriesWhereUserIsAnonymous", query = "Select COUNT(DISTINCT i.id) From ImageGallery i Where i.user.id = :userId and  i.state = :state"),
    @NamedQuery(name = "ImageGallery.findByDate", query = "SELECT i FROM ImageGallery i WHERE i.date = :date")})
public class ImageGallery extends ImageDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @Column(name = "state")
    private String state;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @Column(name = "directory")
    private String directory;
    @Column(name = "cover")
    private String cover;
    @Column(name = "description")
    private String description;
    @Column(name = "number_pics")
    private Short numberPics;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch=FetchType.LAZY)
    private User user;
    
    public ImageGallery() {
    }

    public ImageGallery(Integer id) {
        this.id = id;
    }

    public ImageGallery(Integer id, String title, String state, Date date) {
        this.id = id;
        this.title = title;
        this.state = state;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getCover() {
        return cover;
    }

    public String getCoverImage(){
        return MetConfiguration.WEB_IMAGE_SERVLET_PATH + this.getCover();
    }

     public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Short getNumberPics() {
        return numberPics;
    }

    public void setNumberPics(Short numberPics) {
        this.numberPics = numberPics;
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
        if (!(object instanceof ImageGallery)) {
            return false;
        }
        ImageGallery other = (ImageGallery) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.ImageGallery[id=" + id + "]";
    }

}
