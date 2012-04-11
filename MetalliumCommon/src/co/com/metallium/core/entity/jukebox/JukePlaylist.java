/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.entity.jukebox;

import co.com.metallium.core.constants.state.PlaylistVisibilityEnum;
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
@Table(name = "met_juke_playlist")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JukePlaylist.findAll", query = "SELECT j FROM JukePlaylist j"),
    @NamedQuery(name = "JukePlaylist.findById", query = "SELECT j FROM JukePlaylist j WHERE j.id = :id and j.state = :state"),
    @NamedQuery(name = "JukePlaylist.findByUser", query = "SELECT j FROM JukePlaylist j WHERE j.user = :id and j.state = :state order by j.title"),
    @NamedQuery(name = "JukePlaylist.findByTitle", query = "SELECT j FROM JukePlaylist j WHERE j.title = :title"),
    @NamedQuery(name = "JukePlaylist.findByVisibility", query = "SELECT j FROM JukePlaylist j WHERE j.visibility = :visibility"),
    @NamedQuery(name = "JukePlaylist.findByCreated", query = "SELECT j FROM JukePlaylist j WHERE j.created = :created"),
    @NamedQuery(name = "JukePlaylist.countPlaylistTitles", query = "Select COUNT(DISTINCT j.id) FROM JukePlaylist j Where j.user = :userId and j.title = :title and j.state = :state"),
    @NamedQuery(name = "JukePlaylist.findByLastModified", query = "SELECT j FROM JukePlaylist j WHERE j.lastModified = :lastModified")})
public class JukePlaylist implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    private PlaylistVisibilityEnum visibility;
    @Basic(optional = false)
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Basic(optional = false)
    @Column(name = "last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    @ManyToOne(optional = false)
//    private User user;

    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer user;
    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jukePlaylist")
//    private Collection<JukePlaylistMedia> jukePlaylistMediaCollection;
    @Basic(optional = false)
    @Column(name = "state")
    private String state;
    
    public JukePlaylist() {
    }

    public JukePlaylist(Integer id) {
        this.id = id;
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

    public PlaylistVisibilityEnum getVisibility() {
        return visibility;
    }

    public void setVisibility(PlaylistVisibilityEnum visibility) {
        this.visibility = visibility;
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

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    

//
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
        if (!(object instanceof JukePlaylist)) {
            return false;
        }
        JukePlaylist other = (JukePlaylist) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.temp.juke.JukePlaylist[ id=" + id + " ]";
    }
    
}
