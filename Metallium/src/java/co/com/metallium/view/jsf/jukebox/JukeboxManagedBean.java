/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.jukebox;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.state.PlaylistVisibilityEnum;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.entity.jukebox.JukeMedia;
import co.com.metallium.core.entity.jukebox.JukePlaylist;
import co.com.metallium.core.entity.jukebox.JukePlaylistMedia;
import co.com.metallium.core.service.JukeboxService;
import co.com.metallium.view.jsf.user.UserBaseManagedBean;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.TimeWatch;
import com.metallium.utils.youtube.YouTubeClient;
import com.metallium.utils.youtube.YouTubeVideoDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.DragDropEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

/**
 * 20120103
 * @author Ruben
 */
@ManagedBean(name = "jukebox")
@ViewScoped
public class JukeboxManagedBean extends UserBaseManagedBean implements Serializable {

    private String searchQuery;
    private DataModel playlistModel;
    private String firstVideoId;
    private String searchVideoIds;
    private PlaylistOrderConverter playlistOrderConverter = null;
    //Data
    private String currentPlaylistTitle;
    private Integer currentPlaylistId;
    private Integer currentTrack;
    private YouTubeVideoDTO selectedMedia;
    private YouTubeVideoDTO youtubeVideo;
    private List<YouTubeVideoDTO> videoList;
    private List<YouTubeVideoDTO> droppedList = new ArrayList<YouTubeVideoDTO>();
    private HashMap<String, YouTubeVideoDTO> videoMap = new HashMap<String, YouTubeVideoDTO>();
    private List<JukePlaylist> playlists;
    @EJB
    public JukeboxService jukeboxService;
    //
    private boolean restartPlaylist = false; //When I change to a new playlist I want that after current song that is playing, I want the playlist to start from the first track again.
    private boolean amIonSearchMode = true;
    private JukePlaylist editPlaylist = new JukePlaylist(); //this is for creating and editing a new play list
    private String dialogEditPlaylistSubmitButtonText = null;
    private String playlistOperation;
    private JukeboxHelper jukeHelper = new JukeboxHelper();
    private Integer userId;
    //This is to see the playlist of certain profile
    private String playlistNick;
    private String playlistId;
    private String trackId;
    //These are my CACHE VALUES
    private Boolean canIsaveMedia = null;
    private String playlistImage = MetConfiguration.WEB_IMAGE_SERVLET_PATH + "users/common/playlist.jpg";

    /** Creates a new instance of JukeboxManagedBean */
    public JukeboxManagedBean() {
    }

    /***
     * This method is called by Pretty Faces. When the search.xhtml page is invoked
     *
     * @return String Navigation Page.
     */
    public String initialize() {
        String answer = Navegation.PRETTY_OK;
        try {
            refreshJukeCacheVariables();

            if (StringUtils.isNotBlank(this.playlistNick)) {

                Integer userId = this.userService.findActiveUserIdByNick(playlistNick);
                if (userId != null) {
                    initializePlaylist(userId);
                } else {
//                    this.addFacesMsgFromProperties("news_field_title");
                    this.addFacesMsg("Sorry we where unable to retrieve that playlist.");
                }

            } else {
                if (this.isAuthenticated()) {
                    userId = getAuthenticaedUser().getId();
                    initializePlaylist(userId);
                } else {
                    //What do I do with someone who is not Authenticated and wants to
                    //use the Jukebox?

                }
            }
        } catch (Exception ex) {
            LogHelper.makeLog(ex);
        }

        
        if (this.playlists == null){
            this.playlists = new ArrayList<JukePlaylist>();
        }
        

        return answer;
    }

    private void initializePlaylist(Integer userId) {
        this.playlists = this.jukeboxService.getPlaylistsByUser(userId);
    }

    /**
     *  This method is called when a search request is called.
     * 
     */
    public void performSearch() {
        TimeWatch watch = TimeWatch.start();

        YouTubeClient client = new YouTubeClient();

        this.validateSearchQuery();

        try {

            if (StringUtils.isNotBlank(this.getSearchQuery())) {
                StringBuilder sb = new StringBuilder();
                boolean isFirst = true;

                videoList = client.retrieveVideos(this.getSearchQuery());

                this.currentPlaylistId = null;

                amIonSearchMode = true;
                playlistModel = new ListDataModel(videoList);
                currentTrack = 0;

                videoMap.clear();

                for (Iterator<YouTubeVideoDTO> it = videoList.iterator(); it.hasNext();) {

                    YouTubeVideoDTO dto = it.next();

                    videoMap.put(dto.getMediaId(), dto);

                    if (isFirst) {
                        this.setFirstVideoId(dto.getMediaId());
                        this.setYoutubeVideo(dto);
                        isFirst = false;
                    } else {
                        sb.append(dto.getMediaId()).append(",");
                    }
                }

                this.setSearchVideoIds(sb.toString());

            } else {
                this.setFirstVideoId(null);
                this.setSearchVideoIds(null);
            }

        } catch (Exception ex) {
            manageException(ex);
        }

        refreshJukeCacheVariables();
        LogHelper.makeLog("JukeboxManagedBean.performSearch time: " + watch.timeDes());

    }

    public void reorderPlaylist() {

        RequestContext context = RequestContext.getCurrentInstance(); //Primefaces component to return variables back from the server to the clientside.
        boolean result = false; //It indicates if the Playlist was created or not.

        System.out.println("");
        System.out.println("reordered playlist: ");
        for (Iterator<JukePlaylist> it = this.playlists.iterator(); it.hasNext();) {
            JukePlaylist p = it.next();
            System.out.println("--> " + p.getTitle());
        }
        result = true;
        context.addCallbackParam("result", result);

    }

    public void editPlaylist() {

        RequestContext context = RequestContext.getCurrentInstance(); //Primefaces component to return variables back from the server to the clientside.
        boolean result = false; //It indicates if the Playlist was created or not.

        if (this.isAuthenticated()) {

            validateEditPlaylistInput(); //I Validate that all the fields are filled in.

            if (isJsfMessageListEmpty()) {
                try {

                    if ("createPlaylist".equalsIgnoreCase(this.playlistOperation)) {
                        this.jukeboxService.createPlaylist(this.getEditPlaylist(), userId);
                        this.addFacesMsgFromProperties("juke_playlist_create_success", this.getEditPlaylist().getTitle());
                    } else if ("editPlaylist".equalsIgnoreCase(this.playlistOperation)) {
                        this.jukeboxService.editPlaylist(this.getEditPlaylist(), userId);
                        this.addFacesMsgFromProperties("juke_playlist_edit_success", this.getEditPlaylist().getTitle());
                    } else if ("deletePlaylist".equalsIgnoreCase(this.playlistOperation)) {
                        this.jukeboxService.deletePlaylist(this.getEditPlaylist(), userId);

                        if (this.currentPlaylistId != null && (this.currentPlaylistId.compareTo(this.getEditPlaylist().getId()) == 0)) {
                            this.currentPlaylistId = null;
                        }

                        this.addFacesMsgFromProperties("juke_playlist_delete_success", this.getEditPlaylist().getTitle());
                    }

                    result = true;



                    //Now I just refresh the stuff
                    this.editPlaylist = new JukePlaylist();
                    this.playlists = this.jukeboxService.getPlaylistsByUser(userId);
                    obtainPlaylistTitle();


                } catch (Exception ex) {
                    this.manageException(ex);
                }
            }
        }

        context.addCallbackParam("result", result);
    }

    private void validateEditPlaylistInput() {

        if ("deletePlaylist".equalsIgnoreCase(this.playlistOperation)) {
//            if (this.editPlaylist.getTitle() == null) {
//                this.addFacesMsgFromProperties("news_field_title");
//                return;
//            }
        } else {
            if (StringUtils.isEmpty(this.editPlaylist.getTitle())) {
                this.addFacesMsgFromProperties("news_field_title");
                return;
            } else {
                if (DataHelper.isTooLong(editPlaylist.getTitle(), 38)) {
                    this.addFacesMsgFromValidationProperties("validation_input_too_long", "forum_field_title");
                    return;
                }
            }
        }
    }

    public void saveMedia() {
        if (currentTrack != null && !videoList.isEmpty() && currentTrack >= 0) {
            YouTubeVideoDTO youTubeVideoDTO = videoList.get(currentTrack);
            this.saveTrack(youTubeVideoDTO);
        }
    }

    public void saveMediaMenu() {
        this.saveTrack(this.selectedMedia);
    }

    private void saveTrack(YouTubeVideoDTO youTubeVideoDTO) {
        try {
            if (youTubeVideoDTO != null && this.isCanIsaveMedia()) {
                User currentUser = this.getAuthenticaedUser();
                if (currentUser != null) {
                    boolean mediaAdded = jukeboxService.addMediaToPlaylist(this.userId, youTubeVideoDTO);
                    if (mediaAdded) {
                        this.addFacesMsgFromProperties("juke_media_save_success", youTubeVideoDTO.getTitle());
                    }
                }
            }
        } catch (Exception ex) {
            this.manageException(ex);
        }
    }

    public void preparePlaylistToDelete(JukePlaylist playlist) {
        this.editPlaylist = playlist;
        playlistOperation = "deletePlaylist";
        this.editPlaylist();
    }

    public void preparePlaylistToCreate() {
        playlistOperation = "createPlaylist";
        this.dialogEditPlaylistSubmitButtonText = applicationParameters.getResourceBundleMessage("common_button_create");
    }

    public void preparePlaylistToEdit(JukePlaylist playlist) {

        this.editPlaylist = new JukePlaylist();
        this.editPlaylist.setId(playlist.getId());
        this.editPlaylist.setTitle(playlist.getTitle());
        this.editPlaylist.setVisibility(playlist.getVisibility());

        this.dialogEditPlaylistSubmitButtonText = applicationParameters.getResourceBundleMessage("commun_button_save_changes");

        playlistOperation = "editPlaylist";

    }

    public void onMediaDrop(DragDropEvent ddEvent) {
        YouTubeVideoDTO media = ((YouTubeVideoDTO) ddEvent.getData());

        droppedList.add(media);
        //  videoList.remove(car);  
    }

    public void onDrop() {
        System.out.println("--onDrop---><-- ");
        System.out.println("--onDrop---><-- ");
        System.out.println("--onDrop---><-- ");
    }

    public void onDrop(DragDropEvent event) {
        Object player = (Object) event.getData();
        System.out.println("--onDrop---> " + player);
        System.out.println("--onDrop---> " + player);
        System.out.println("--onDrop---> " + player);
        System.out.println("--onDrop---> " + player);
    }

    public void loadPlaylist(Integer playlistId) {

        this.refreshJukeCacheVariables();

        try {
            List<JukePlaylistMedia> mediaList = jukeboxService.findMediaList(playlistId);


            if (this.videoList != null) {
                this.videoList.clear();
            } else {
                this.videoList = new ArrayList<YouTubeVideoDTO>();
            }

            if (!mediaList.isEmpty()) {

                amIonSearchMode = false;
                boolean isFirst = true;

                restartPlaylistNextSong();
                this.searchQuery = "";
                int cont = 0;
                YouTubeVideoDTO dto = null;

                videoMap.clear();

                for (Iterator<JukePlaylistMedia> it = mediaList.iterator(); it.hasNext();) {
                    JukePlaylistMedia jukePlaylistMedia = it.next();
                    JukeMedia video = jukePlaylistMedia.getJukeMedia();

                    if (video != null) {

                        dto = new YouTubeVideoDTO();
                        dto.setDescription(video.getDescription());
                        dto.setDuration(video.getDuration());
                        dto.setKeywords(video.getKeywords());
                        dto.setMediaId(video.getMediaId());
                        dto.setId(video.getId());
                        dto.setThumbnail1(video.getThumbnail1());
                        dto.setThumbnail2(video.getThumbnail2());
                        dto.setTitle(video.getTitle());
                        dto.setWebPlayerUrl(video.getUrl());
                        dto.setRowIndex(cont++);

                        videoMap.put(dto.getMediaId(), dto);

                        if (isFirst) {
                            this.setFirstVideoId(dto.getMediaId());
                            this.setYoutubeVideo(dto);
                            isFirst = false;
                            this.currentTrack = 0;
                        }

                        videoList.add(dto);
                    }
                }


                playlistModel = new ListDataModel(videoList);
            }
        } catch (Exception ex) {
            this.manageException(ex);
        }

        this.currentPlaylistId = playlistId;
        this.obtainPlaylistTitle();

        refreshJukeCacheVariables();
    }

    public void nextTrack() {
        currentTrack = this.videoList.indexOf(this.getYoutubeVideo()) + 1;
        validateIfRestartIsRequired();
        processNextTrack();
    }

    public void previousTrack() {
        currentTrack = this.videoList.indexOf(this.getYoutubeVideo()) - 1;
        validateIfRestartIsRequired();
        processNextTrack();
    }

    public void playMedia() {
        int indexOfMedia = this.videoList.indexOf(this.selectedMedia);
        this.playTrack(indexOfMedia);
    }

    public void playTrack(YouTubeVideoDTO trackToPlay) {
        int indexOfMedia = this.videoList.indexOf(trackToPlay);
        playTrack(indexOfMedia);
    }

    private void playTrack(Integer trackToPlay) {

        currentTrack = trackToPlay;
        processNextTrack();
    }

    public boolean validateIfMediaIsSelected(String mediaId) {

        if (StringUtils.isNotBlank(mediaId) && this.getYoutubeVideo() != null
                && mediaId.equals(this.getYoutubeVideo().getMediaId())) {
            return true;
        }

        return false;
    }

    private void processNextTrack() {
        if (currentTrack >= videoList.size()) {
            currentTrack = 0;
        }

        if (currentTrack < 0) {
            currentTrack = videoList.size() - 1;
        }

        if (!videoList.isEmpty()) {
            YouTubeVideoDTO youTubeVideoDTO = videoList.get(currentTrack);
            youTubeVideoDTO.setSelected(true);
            this.setFirstVideoId(youTubeVideoDTO.getMediaId());
            this.setYoutubeVideo(youTubeVideoDTO);

            RequestContext context = RequestContext.getCurrentInstance(); //Primefaces component to return variables back from the server to the clientside.
            context.addCallbackParam("mediaId", this.getFirstVideoId());
            context.addCallbackParam("result", true);
        }
    }

    public void deleteMedia() {

        YouTubeVideoDTO media = this.selectedMedia;

        if (media != null) {

            int indexOfMedia = videoList.indexOf(media);

            if (indexOfMedia >= 0) {
                videoList.remove(indexOfMedia);

                if (this.currentPlaylistId != null) {
                    try {
                        this.jukeboxService.removeMediaFromPlaylist(currentPlaylistId, media.getId());
                    } catch (Exception e) {
                        manageException(e);
                    }
                }
            }
        }
    }

    public void moveMediaToPlaylist1(YouTubeVideoDTO media) {
        jukeHelper.setMedia(media);
    }

    public void moveMediaToPlaylist1() {
        System.out.println("--> moveMediaToPlaylist1");
        System.out.println("--> moveMediaToPlaylist1");
        System.out.println("--> moveMediaToPlaylist1");
        System.out.println("--> moveMediaToPlaylist1");
    }

    public void moveMediaToPlaylist2(JukePlaylist playlist) {

        try {

            if (userId != null) {

                YouTubeVideoDTO auxSelectedMedia = this.jukeHelper.getMedia();

                if (auxSelectedMedia != null) {
                    this.jukeboxService.moveMedia(userId, auxSelectedMedia, currentPlaylistId, playlist.getId());

                    if (currentPlaylistId != null) {
                        this.videoList.remove(this.jukeHelper.getMedia());
                    }
                    this.addFacesMsgFromProperties("juke_media_add_media_to_playlist", auxSelectedMedia.getTitle(), playlist.getTitle());

                    //Now I just make sure I clean up the data since I already used it.
                    this.jukeHelper.cleanUp();
                }
            }

        } catch (Exception e) {
            manageException(e);
        }

    }

    public void moveMediaToPlaylist3(JukePlaylist playlist) {

        this.jukeHelper.setMedia(getYoutubeVideo());
        this.moveMediaToPlaylist2(playlist);
    }

    //================Helper Logic =====================================//
    private void obtainPlaylistTitle() {

        int indexOf = this.playlists.indexOf(new JukePlaylist(this.currentPlaylistId));
        if (indexOf >= 0) {
            this.currentPlaylistTitle = this.playlists.get(indexOf).getTitle();
        } else {
            this.currentPlaylistTitle = "List";
        }

    }

    //================Render Logic =====================================//
    public boolean isCanIsaveMedia() {

        if (this.canIsaveMedia == null) {

            this.canIsaveMedia = false; //I assume you cant edit my profile unless you prove otherwise.

            if (this.isAuthenticated()) {

                if (true || amIonSearchMode) {
                    this.canIsaveMedia = true;
                }

            }
        }

        return this.canIsaveMedia;
    }

    //================Getters and Setters =====================================//
    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getFirstVideoId() {
        return firstVideoId;
    }

    public void setFirstVideoId(String firstVideoId) {
        this.firstVideoId = firstVideoId;
    }

    public String getSearchVideoIds() {
        return searchVideoIds;
    }

    public void setSearchVideoIds(String searchVideoIds) {
        this.searchVideoIds = searchVideoIds;
    }

    public DataModel getPlaylistModel() {
        return playlistModel;
    }

    public void setPlaylistModel(DataModel playlistModel) {
        this.playlistModel = playlistModel;
    }

    public YouTubeVideoDTO getYoutubeVideo() {
        return youtubeVideo;
    }

    public void setYoutubeVideo(YouTubeVideoDTO youtubeVideo) {
        this.youtubeVideo = youtubeVideo;
    }

    public List<JukePlaylist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<JukePlaylist> playlists) {
        this.playlists = playlists;
    }

    public List<YouTubeVideoDTO> getVideoList() {
        return videoList;
    }

    public YouTubeVideoDTO getSelectedMedia() {
        return selectedMedia;
    }

    public void setSelectedMedia(YouTubeVideoDTO selectedMedia) {
        this.selectedMedia = selectedMedia;
    }

    public String getPlaylistImage() {
        return playlistImage;
    }

    private void validateSearchQuery() {
        if (this.getSearchQuery().contains("bieber")) {
            this.setSearchQuery("Who the fuck is Justin Bieber? Ozzy Osbourne");
        }
    }

    public JukePlaylist getEditPlaylist() {
        return editPlaylist;
    }

    public void setEditPlaylist(JukePlaylist editPlaylist) {
        this.editPlaylist = editPlaylist;
    }

    public String getCurrentPlaylistTitle() {
        return currentPlaylistTitle;
    }

    public void setCurrentPlaylistTitle(String currentPlaylistTitle) {
        this.currentPlaylistTitle = currentPlaylistTitle;
    }

    public String getPlaylistOperation() {
        return playlistOperation;
    }

    public void setPlaylistOperation(String playlistOperation) {
        this.playlistOperation = playlistOperation;
    }

    public String getDialogEditPlaylistSubmitButtonText() {
        return dialogEditPlaylistSubmitButtonText;
    }

    public void setDialogEditPlaylistSubmitButtonText(String dialogEditPlaylistSubmitButtonText) {
        this.dialogEditPlaylistSubmitButtonText = dialogEditPlaylistSubmitButtonText;
    }

    public List<YouTubeVideoDTO> getDroppedList() {
        return droppedList;
    }

    public void setDroppedList(List<YouTubeVideoDTO> droppedList) {
        this.droppedList = droppedList;
    }

    public String getPlaylistNick() {
        return playlistNick;
    }

    public void setPlaylistNick(String playlistNick) {
        this.playlistNick = playlistNick;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    private void restartPlaylistNextSong() {

        if (currentTrack != null) {
//            restartPlaylist = true;
            this.currentTrack = -10;
        }
    }

    private void validateIfRestartIsRequired() {

        if (restartPlaylist) {
            this.currentTrack = 0;
            restartPlaylist = false;
        }
    }

    public void refreshJukeCacheVariables() {
        this.canIsaveMedia = null;
    }

    public PlaylistOrderConverter getPlaylistOrderConverter() {

        if (playlistOrderConverter == null) {
            playlistOrderConverter = new PlaylistOrderConverter() {

                @Override
                public List<JukePlaylist> getPlaylists() {
                    return playlists;
                }
            };

        }

        return playlistOrderConverter;
    }
}
