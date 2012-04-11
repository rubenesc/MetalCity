/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.entity.UserComments;
import co.com.metallium.core.entity.UserWallPost;
import co.com.metallium.core.exception.UserWallException;
import co.com.metallium.core.exception.ValidationException;
import co.com.metallium.core.util.ApplicationParameters;
import co.com.metallium.core.util.Assert;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.utils.htmlscriptvalidator.HtmlSanitizer;
import com.metallium.utils.youtube.YouTubeClient;
import com.metallium.utils.youtube.YouTubeVideoDTO;
import javax.ejb.EJB;

/**
 * 20111126
 * @author Ruben
 */
public class BaseService {

    private YouTubeClient youTubeManager = new YouTubeClient();
    @EJB
    public ApplicationParameters applicationParameters;

    public void validateNewsEventLength(String input) throws ValidationException {
        validateInputLength(input, MetConfiguration.NEWS_EVENT_MAX_LENGTH, "common_info_post_to_long");
    }

    public void validateWallPostLength(String input) throws ValidationException {
        validateInputLength(input, MetConfiguration.WALL_POST_MAX_LENGTH, "common_info_post_to_long");
    }

    public void validateCommentLength(String input) throws ValidationException {
        validateInputLength(input, MetConfiguration.COMMENT_MAX_LENGTH, "common_info_post_to_long");
    }

    private void validateInputLength(String input, Integer maxLength, String displayMessage) throws ValidationException {
        try {
            Assert.validStringLength(input, maxLength);
        } catch (ValidationException ex) {
            String message = applicationParameters.getResourceBundleMessage(displayMessage, maxLength.toString());
            ex.setDisplayMessage(message);
            throw ex;
        }
    }

    //================Youtube stuff==========================================//
    public String getYouTubeIdWrapper(String url) throws ValidationException {
        String videoId = YouTubeClient.getYouTubeId(url);
        if (videoId == null) {
            String msg = applicationParameters.getResourceBundleMessage("wall_post_info_url_youtube_video_invalid", url);
            throw new ValidationException(msg, null, msg);
        }
        return videoId;
    }

    /**
     * Aux Method to help me get the Info of a YouTube Video and pack that info in the entity to display 
     * to persist in the DB
     * 
     * @param videoId
     * @param entity
     * @throws Exception 
     */
    public void processMediaInfo(String videoId, UserWallPost entity) throws Exception {
        YouTubeVideoDTO dto = HtmlSanitizer.findYouTubeVideoInfo(videoId);
        entity.setMediaId(dto.getMediaId());
        entity.setMediaTitle(DataHelper.getMediaTitleString(dto.getTitle()));
        entity.setMediaDescription(DataHelper.getMediaDescriptionString(dto.getDescription()));
        entity.setMediaThumbnail1(dto.getThumbnail1());
        entity.setMediaThumbnail2(dto.getThumbnail2());
    }

    public void processMediaInfoComment(String videoId, UserComments entity) throws Exception {
        YouTubeVideoDTO dto = HtmlSanitizer.findYouTubeVideoInfo(videoId);
        entity.setMediaId(dto.getMediaId());
        entity.setMediaTitle(DataHelper.getMediaTitleString(dto.getTitle()));
        entity.setMediaDescription(DataHelper.getMediaDescriptionString(dto.getDescription()));
        entity.setMediaThumbnail1(dto.getThumbnail1());
        entity.setMediaThumbnail2(dto.getThumbnail2());
    }

    //===============Getters and Setters=====================================//
    public YouTubeClient getYouTubeManager() {
        return youTubeManager;
    }

    public void setYouTubeManager(YouTubeClient youTubeManager) {
        this.youTubeManager = youTubeManager;
    }
}
