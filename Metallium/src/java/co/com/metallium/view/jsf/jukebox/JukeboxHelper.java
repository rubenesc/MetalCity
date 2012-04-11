/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.jukebox;

import co.com.metallium.core.entity.jukebox.JukePlaylist;
import com.metallium.utils.youtube.YouTubeVideoDTO;
import java.io.Serializable;

/**
 * 20120129
 * @author Ruben
 */
public class JukeboxHelper implements Serializable {

    private Integer mediaId;
    private YouTubeVideoDTO media;
    
    private Integer playlistId1;
    private JukePlaylist playlist1;
    
    private Integer playlistId2;
    private JukePlaylist playlist2;

    public JukeboxHelper() {
    }

    //================Getters and Setters =====================================//

    public YouTubeVideoDTO getMedia() {
        return media;
    }

    public void setMedia(YouTubeVideoDTO media) {
        this.media = media;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public JukePlaylist getPlaylist1() {
        return playlist1;
    }

    public void setPlaylist1(JukePlaylist playlist1) {
        this.playlist1 = playlist1;
    }

    public JukePlaylist getPlaylist2() {
        return playlist2;
    }

    public void setPlaylist2(JukePlaylist playlist2) {
        this.playlist2 = playlist2;
    }

    public Integer getPlaylistId1() {
        return playlistId1;
    }

    public void setPlaylistId1(Integer playlistId1) {
        this.playlistId1 = playlistId1;
    }

    public Integer getPlaylistId2() {
        return playlistId2;
    }

    public void setPlaylistId2(Integer playlistId2) {
        this.playlistId2 = playlistId2;
    }

    void cleanUp() {
        this.mediaId = null;
        this.media = null;
        this.playlistId1 = null;
        this.playlist1 = null;
        this.playlistId2 = null;
        this.playlist2 = null;
    }
    
    
    
}
