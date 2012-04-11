/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.entity.jukebox;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Ruben
 */
@Embeddable
public class JukePlaylistMediaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_playlist")
    private int idPlaylist;
    @Basic(optional = false)
    @Column(name = "id_media")
    private int idMedia;

    public JukePlaylistMediaPK() {
    }

    public JukePlaylistMediaPK(int idPlaylist, int idMedia) {
        this.idPlaylist = idPlaylist;
        this.idMedia = idMedia;
    }

    public int getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public int getIdMedia() {
        return idMedia;
    }

    public void setIdMedia(int idMedia) {
        this.idMedia = idMedia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idPlaylist;
        hash += (int) idMedia;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JukePlaylistMediaPK)) {
            return false;
        }
        JukePlaylistMediaPK other = (JukePlaylistMediaPK) object;
        if (this.idPlaylist != other.idPlaylist) {
            return false;
        }
        if (this.idMedia != other.idMedia) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.temp.juke.JukePlaylistMediaPK[ idPlaylist=" + idPlaylist + ", idMedia=" + idMedia + " ]";
    }
    
}
