/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import co.com.metallium.core.constants.state.BaseState;
import co.com.metallium.core.constants.state.PlaylistVisibilityEnum;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.entity.jukebox.JukeMedia;
import co.com.metallium.core.entity.jukebox.JukePlaylist;
import co.com.metallium.core.entity.jukebox.JukePlaylistMedia;
import co.com.metallium.core.entity.jukebox.JukePlaylistMediaPK;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import co.com.metallium.core.exception.ForumException;
import co.com.metallium.core.exception.JukeboxException;
import co.com.metallium.core.exception.ValidationException;
import co.com.metallium.core.util.ApplicationParameters;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.TimeWatch;
import com.metallium.utils.utils.UtilHelper;
import com.metallium.utils.utils.htmlscriptvalidator.HtmlSanitizer;
import com.metallium.utils.youtube.YouTubeVideoDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * 20120114
 * @author Ruben
 */
@Stateless
public class JukeboxService extends BaseService {

    @PersistenceContext(unitName = "MetalliumPU")
    private EntityManager em;
    @EJB
    private GeneralService generalService;

    public JukeboxService() {
    }

    //==========public methods==============================================//
    /**
     * @param user
     * @param video
     * @throws Exception 
     */
    public boolean addMediaToPlaylist(Integer userId, YouTubeVideoDTO video) throws Exception {

        boolean mediaAdded = false;

        JukePlaylist playList = getUsersDefaultPlayList(userId);

        if (playList == null) {
            playList = createDefaultPlayList(userId);
        }

        JukeMedia media = findJukeMediaByMediaId(video.getMediaId());
        if (media == null) {
            media = createMediaYoutubeVideo(video);
        }

        //First I verify if the Song has been added to the playlist all ready
        if (countPlaylistMedia(playList.getId(), media.getId()) == 0) {

            int mediaOrder = countPlayListElements(playList.getId());

            JukePlaylistMediaPK pk = new JukePlaylistMediaPK(playList.getId(), media.getId());
            JukePlaylistMedia plm = new JukePlaylistMedia(pk);
            plm.setMediaOrder(mediaOrder + 1);
            plm.setDate(new Date());

            em.persist(plm);
            //For some reason if I dont have the, flush and the refresh, 
            //the transaccion isn't visiable in for the 'em' although the info is 
            //stored successfully in the DB. It appear after an evict all.
            em.flush();
            em.refresh(plm);
            mediaAdded = true;
        }
        return mediaAdded;

    }

    /**
     * 
     * @param playlistId
     * @return 
     */
    public JukePlaylist findPlaylist(Integer playlistId) {

        Query q = null;
        q = em.createNamedQuery("JukePlaylist.findById");
        q.setParameter("id", playlistId);
        q.setParameter("state", BaseState.ACTIVE);
        return (JukePlaylist) q.getSingleResult();

    }

    public JukePlaylist getUsersDefaultPlayList(Integer userId) {

        List<JukePlaylist> userPlaylists = null;

        try {

            Query q = null;
            q = em.createNamedQuery("JukePlaylist.findByUser");
            q.setParameter("id", userId);   //Id of the user of whom we are going to find the friends

            userPlaylists = q.getResultList();

        } catch (Exception e) {
            userPlaylists = new ArrayList<JukePlaylist>();
            LogHelper.makeLog(e);
        }

        if (!userPlaylists.isEmpty()) {
            return userPlaylists.get(0);
        }

        return null;
    }

    public List<JukePlaylist> getPlaylistsByUser(Integer userId) {

        List<JukePlaylist> userPlaylists = null;

        try {

            Query q = null;
            q = em.createNamedQuery("JukePlaylist.findByUser");
            q.setParameter("id", userId);   //Id of the user of whom we are going to find the friends
            q.setParameter("state", BaseState.ACTIVE);

            userPlaylists = q.getResultList();

        } catch (Exception e) {
            userPlaylists = new ArrayList<JukePlaylist>();
            LogHelper.makeLog(e);
        }


        return userPlaylists;
    }

    public JukeMedia findJukeMediaByMediaId(String mediaId) throws Exception {
        JukeMedia entity = null;

        try {
            entity = (JukeMedia) em.createNamedQuery("JukeMedia.findByMediaId").setParameter("mediaId", mediaId).getSingleResult();
        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                //This is fine.
            } else {
                throw ex;
            }
        }

        return entity;
    }

    public List<JukePlaylistMedia> findMediaList(Integer playlistId) {


        List<JukePlaylistMedia> mediaList = null;

        try {

            Query q = null;
            q = em.createNamedQuery("JukePlaylistMedia.findByIdPlaylist");
            q.setParameter("idPlaylist", playlistId);   //Id of the user of whom we are going to find the friends
            mediaList = q.getResultList();

        } catch (Exception e) {
            mediaList = new ArrayList<JukePlaylistMedia>();
            LogHelper.makeLog(e);
        }

        return mediaList;
    }

    //==========private methods==============================================//
    private JukePlaylist createDefaultPlayList(Integer userId) {
        JukePlaylist playlist = new JukePlaylist();
        playlist.setCreated(new Date());
        playlist.setLastModified(new Date());
        playlist.setTitle("Playlist");
        playlist.setVisibility(PlaylistVisibilityEnum.PUBLIC);
        playlist.setUser(userId);
        playlist.setState(BaseState.ACTIVE);
        em.persist(playlist);
        return playlist;
    }

    private JukeMedia createMediaYoutubeVideo(YouTubeVideoDTO video) {
        JukeMedia entity = new JukeMedia();

        entity.setCreated(new Date());
        entity.setDescription(video.getDescription());
        entity.setDuration(video.getDuration());
        entity.setKeywords(DataHelper.truncateString(video.getKeywords(), 99));
        entity.setLastModified(new Date());
        entity.setMediaId(video.getMediaId());
        entity.setThumbnail1(video.getThumbnail1());
        entity.setThumbnail2(video.getThumbnail2());
        entity.setTitle(video.getTitle());
        entity.setUrl(video.getWebPlayerUrl());
        em.persist(entity);
        em.flush();
        return entity;
    }

    /**
     * This can either be zero or one.
     * 
     * @param idPlaylist
     * @param idMedia
     * @return 
     */
    private Integer countPlaylistMedia(Integer idPlaylist, Integer idMedia) {
        Integer answer = 0;

        try {

            Query q = em.createNamedQuery("JukePlaylistMedia.countPlayListMedia");
            q.setParameter("idPlaylist", idPlaylist);
            q.setParameter("idMedia", idMedia);

            answer = ((Number) q.getSingleResult()).intValue();

        } catch (Exception e) {
            //I cant to anything.
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }

    private Integer countPlayListElements(Integer playlistId) {
        Integer answer = 0;
        try {

            Query q = em.createNamedQuery("JukePlaylistMedia.countPlayListElements");
            q.setParameter("idPlaylist", playlistId);

            answer = ((Number) q.getSingleResult()).intValue();

        } catch (Exception e) {
            //I cant to anything.
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }

    private Integer countPlaylistTitles(Integer userId, String title) {

        Query q = em.createNamedQuery("JukePlaylist.countPlaylistTitles");
        q.setParameter("userId", userId);
        q.setParameter("title", title);
        q.setParameter("state", BaseState.ACTIVE);

        return ((Number) q.getSingleResult()).intValue();
    }

    public void removeMediaFromPlaylist(Integer idPlaylist, Integer idMedia) {

        Query q1 = null;
        q1 = em.createNamedQuery("JukePlaylistMedia.removeMedia");
        q1.setParameter("idPlaylist", idPlaylist);
        q1.setParameter("idMedia", idMedia);
        q1.executeUpdate();
        em.flush();

    }

    public void createPlaylist(JukePlaylist playList, Integer userId) throws JukeboxException {

        if (countPlaylistTitles(userId, playList.getTitle()) > 0) {
            throw new JukeboxException(null, null, applicationParameters.getResourceBundleMessage("juke_playlist_create_change_title"));
        }

        playList.setCreated(new Date());
        playList.setLastModified(new Date());
        playList.setVisibility(PlaylistVisibilityEnum.PUBLIC);
        playList.setUser(userId);
        playList.setState(BaseState.ACTIVE);
        playList.setTitle(playList.getTitle().trim()); //I rather do the validation here at the end.

        em.persist(playList);
    }

    public void editPlaylist(JukePlaylist source, Integer userId) throws JukeboxException {

        JukePlaylist target = em.find(JukePlaylist.class, source.getId());

        String sourceTitle = source.getTitle().trim();
        if (target.getUser().compareTo(userId) == 0) {
            if (!target.getTitle().equalsIgnoreCase(sourceTitle)) {
                if (countPlaylistTitles(userId, sourceTitle) > 0) {
                    throw new JukeboxException(null, null, applicationParameters.getResourceBundleMessage("juke_playlist_create_change_title"));
                }
            }

            target.setTitle(sourceTitle);
            if (source.getVisibility() != null) {
                target.setVisibility(source.getVisibility());
            } else {
                target.setVisibility(PlaylistVisibilityEnum.PUBLIC);
            }
            target.setLastModified(new Date());

            em.merge(target);

        } else {
            throw new JukeboxException("You dont have permision to edit this playlist: " + target.getId() + ". Your userId is: " + userId);
        }

    }

    public void deletePlaylist(JukePlaylist source, Integer userId) throws JukeboxException {

        JukePlaylist target = em.find(JukePlaylist.class, source.getId());

        if (target.getUser().compareTo(userId) == 0) {

            target.setState(BaseState.DELETED);

            em.merge(target);
            em.flush();

        } else {
            throw new JukeboxException("You dont have permision to edit this playlist: " + target.getId() + ". Your userId is: " + userId);
        }

    }

    /**
     * 
     * @param userId
     * @param media
     * @param playlist1
     * @param playlist2
     * @throws Exception 
     */
    public void moveMedia(Integer userId, YouTubeVideoDTO media, Integer playlist1, Integer playlist2) throws Exception {
        TimeWatch watch = TimeWatch.start();
        
        //If a SourcePlaylist is specified, then its because Im going to move a Media
        //from one of the users playlist to another playlist. I need to know because 
        if (playlist1 != null) {
            JukePlaylist p1 = this.findPlaylist(playlist1);
            if (p1.getUser().compareTo(userId) != 0) {
                throw new Exception();
            }
        }

        //I make sure that the target playlist actully belongs to the user.
        JukePlaylist p2 = this.findPlaylist(playlist2);
        if (p2.getUser().compareTo(userId) != 0) {
            throw new Exception();
        }

        //Now I get the mediaId based on the caracteristics of the Media.
        Integer mediaId = obtainMediaId(media);


        //First I verify if the Song has been added to the playlist all ready
        if (countPlaylistMedia(playlist2, mediaId) == 0) {
            
            //Next I add the media to the users target playlist.
            addMediaToPlaylist(playlist2, mediaId);

            //Finally if the source playlist belongs to the user and it was Specified
            //then I remove the media from this playlist.
            if (playlist1 != null) {
                removeMediaFromPlaylist(playlist1, mediaId);
            }

            em.flush();
        }
        
        LogHelper.makeLog("JukeboxService.moveMedia time: " + watch.timeDes());

    }

    /**
     * This is the Id of the Media in the DB. This is JukeMedia.id
     * 
     * @param media
     * @return
     * @throws Exception 
     */
    private Integer obtainMediaId(YouTubeVideoDTO media) throws Exception {

        //First I verify if I need to create a Media or not.
        //I have to create a Media if the Media does not have an Id Assigned.

        Integer mediaId = media.getId();
        if (mediaId == null) {

            //Ok the media doesn't have an Id, this is the Id from the DB. 
            //So we have to check out if exits in the DB or not, based on the MediaId
            JukeMedia jukeMedia = findJukeMediaByMediaId(media.getMediaId());
            if (jukeMedia == null) {
                //No, this media doesn, exist in the DB lets create it.
                jukeMedia = createMediaYoutubeVideo(media);
            }
            mediaId = jukeMedia.getId();

        }

        return mediaId;
    }

    public void addMediaToPlaylist(Integer idPlaylist, Integer idMedia) {
        int mediaOrder = countPlayListElements(idPlaylist);
        JukePlaylistMediaPK pk = new JukePlaylistMediaPK(idPlaylist, idMedia);
        JukePlaylistMedia plm = new JukePlaylistMedia(pk);
        plm.setMediaOrder(mediaOrder + 1);
        plm.setDate(new Date());
        em.persist(plm);

        em.flush();
        em.refresh(plm);
    }
}
