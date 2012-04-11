/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.entity.jukebox;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "met_juke_playlist_media")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JukePlaylistMedia.findAll", query = "SELECT j FROM JukePlaylistMedia j"),
    @NamedQuery(name = "JukePlaylistMedia.findByIdPlaylist", query = "SELECT j FROM JukePlaylistMedia j WHERE j.jukePlaylistMediaPK.idPlaylist = :idPlaylist order by j.mediaOrder "),
    @NamedQuery(name = "JukePlaylistMedia.findByIdMedia", query = "SELECT j FROM JukePlaylistMedia j WHERE j.jukePlaylistMediaPK.idMedia = :idMedia"),
    @NamedQuery(name = "JukePlaylistMedia.removeMedia", query = "DELETE FROM JukePlaylistMedia j WHERE j.jukePlaylistMediaPK.idPlaylist = :idPlaylist and j.jukePlaylistMediaPK.idMedia = :idMedia "),
    @NamedQuery(name = "JukePlaylistMedia.countPlayListMedia", query = "Select COUNT(DISTINCT j.jukePlaylistMediaPK.idMedia) FROM JukePlaylistMedia j Where j.jukePlaylistMediaPK.idPlaylist = :idPlaylist and j.jukePlaylistMediaPK.idMedia = :idMedia "),
    @NamedQuery(name = "JukePlaylistMedia.countPlayListElements", query = "Select COUNT(DISTINCT j.jukePlaylistMediaPK.idMedia) FROM JukePlaylistMedia j Where j.jukePlaylistMediaPK.idPlaylist = :idPlaylist")})

public class JukePlaylistMedia implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected JukePlaylistMediaPK jukePlaylistMediaPK;
    @Column(name = "media_order")
    private Integer mediaOrder;

//    @JoinColumn(name = "id_playlist", referencedColumnName = "id", insertable = false, updatable = false)
//    @ManyToOne(optional = false)
//    private JukePlaylist jukePlaylist;
    
    @JoinColumn(name = "id_media", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch=FetchType.EAGER)
    private JukeMedia jukeMedia;
    
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    

    public JukePlaylistMedia() {
    }

    public JukePlaylistMedia(JukePlaylistMediaPK jukePlaylistMediaPK) {
        this.jukePlaylistMediaPK = jukePlaylistMediaPK;
    }

    public JukePlaylistMedia(int idPlaylist, int idMedia) {
        this.jukePlaylistMediaPK = new JukePlaylistMediaPK(idPlaylist, idMedia);
    }

    public JukePlaylistMediaPK getJukePlaylistMediaPK() {
        return jukePlaylistMediaPK;
    }

    public void setJukePlaylistMediaPK(JukePlaylistMediaPK jukePlaylistMediaPK) {
        this.jukePlaylistMediaPK = jukePlaylistMediaPK;
    }

    public Integer getMediaOrder() {
        return mediaOrder;
    }

    public void setMediaOrder(Integer mediaOrder) {
        this.mediaOrder = mediaOrder;
    }

//    public JukePlaylist getJukePlaylist() {
//        return jukePlaylist;
//    }
//
//    public void setJukePlaylist(JukePlaylist jukePlaylist) {
//        this.jukePlaylist = jukePlaylist;
//    }

    public JukeMedia getJukeMedia() {
        return jukeMedia;
    }

    public void setJukeMedia(JukeMedia jukeMedia) {
        this.jukeMedia = jukeMedia;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jukePlaylistMediaPK != null ? jukePlaylistMediaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JukePlaylistMedia)) {
            return false;
        }
        JukePlaylistMedia other = (JukePlaylistMedia) object;
        if ((this.jukePlaylistMediaPK == null && other.jukePlaylistMediaPK != null) || (this.jukePlaylistMediaPK != null && !this.jukePlaylistMediaPK.equals(other.jukePlaylistMediaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.temp.juke.JukePlaylistMedia[ jukePlaylistMediaPK=" + jukePlaylistMediaPK + " ]";
    }
    
}
