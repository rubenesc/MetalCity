/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.youtube;

import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.TimeWatch;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.data.geo.impl.GeoRssWhere;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaPlayer;
import com.google.gdata.data.media.mediarss.MediaRestriction;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaContent;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeMediaRating;
import com.google.gdata.data.youtube.YtPublicationState;
import com.google.gdata.data.youtube.YtStatistics;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 20111128
 * 
 * taken from http://www.javacodegeeks.com/2010/05/getting-started-with-youtube-java-api.html
 * 
 * @author Ruben
 */
public class YouTubeClient {

    private static final String YOUTUBE_URL = "http://gdata.youtube.com/feeds/api/videos/";
    private static final String YOUTUBE_EMBEDDED_URL = "http://www.youtube.com/v/";
    private static final String clientID = "MetalCity";
    private static final boolean isDebug = false;
    private int timeout = 3000;
    private YouTubeService service;
    //YouTube Video
    private static final Pattern youTubePattern1 = Pattern.compile("^[^v]+v=(.{11}).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern youTubePattern2 = Pattern.compile(".*youtu.be/(.{11}).*", Pattern.CASE_INSENSITIVE);

    public YouTubeClient() {
        service = new YouTubeService(clientID);
        service.setConnectTimeout(timeout);
    }

    /**
     * Given a YouTube Url it gives me the Video Id
     * Example:
     *
     * id: cKZDdG9FTKY, link: https://www.youtube.com/watch?v=cKZDdG9FTKY&feature=channel
     * id: yZ-K7nCVnBI, link: http://www.youtube.com/watch?v=yZ-K7nCVnBI&playnext_from=TL&videos=osPknwzXEas&feature=sub
     * id: NRHVzbJVx8I, link: http://www.youtube.com/ytscreeningroom?v=NRHVzbJVx8I
     * id: C7ZrU2fwKME, link: http://www.youtube.com/watch?v=C7ZrU2fwKME&feature=grec_index
     * id: HM9BCnqTNlw, link: http://www.youtube.com/watch?v=HM9BCnqTNlw&feature=grec_index
     * id: HM9BCnqTNlw, link: youtu.be/HM9BCnqTNlw
     * 
     * @return video id
     *  returns null if no Id could be found
     */
    public static String getYouTubeId(String youTubeUrl) {

        if (youTubeUrl != null) {

            Matcher m = youTubePattern1.matcher(youTubeUrl); //for patterns that have www.youtube.com/watch?v=cLnTjHywE54

            if (m.matches()) {
                return m.group(1);
            } else {
                //So Im going to try another pattern.
                m = youTubePattern2.matcher(youTubeUrl); //for patterns that have youtu.be/cLnTjHywE54
                if (m.matches()) {
                    return m.group(1);
                }
            }
        }

        return null;

    }

    /**
     * Retrieves the info of a YouTube Video 
     * 
     * example: videoId = Bqvcmud3LFQ 
     *          videoId = tI_sv5uswoY 
     * 
     * @param videoId
     * @return
     * @throws Exception 
     */
    public YouTubeVideoDTO retrieveVideo(String videoId) throws Exception {

        VideoEntry videoEntry = service.getEntry(new URL(YOUTUBE_URL.concat(videoId)), VideoEntry.class);
        YouTubeVideoDTO videoDTO = convertVideo(videoEntry);
        if (isDebug) {
            printVideoEntry(videoEntry, true);
        }
        return videoDTO;
    }

    public List<YouTubeVideoDTO> retrieveVideos(String textQuery) throws Exception {
        return retrieveVideos(textQuery, 50, true, timeout);

    }

    public List<YouTubeVideoDTO> retrieveVideos(String textQuery, int maxResults, boolean filter, int timeout) throws Exception {
        TimeWatch watch = TimeWatch.start();

        service.setConnectTimeout(timeout); // millis
        YouTubeQuery query = new YouTubeQuery(new URL(YOUTUBE_URL));
          query.setRestrictLocation(isDebug);
        query.setOrderBy(YouTubeQuery.OrderBy.RELEVANCE);
        query.setFullTextQuery(textQuery);
        query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);
        query.setMaxResults(maxResults);
        query.setFormats(5); //This format is not available for a video that is not embeddable. Developers commonly add &format=5 to their queries to restrict results to videos that can be embedded on their sites.

        VideoFeed videoFeed = service.query(query, VideoFeed.class);
        List<VideoEntry> videos = videoFeed.getEntries();

        List<YouTubeVideoDTO> answer = convertVideos(videos);
        LogHelper.makeLog("YouTubeClient.retrieveVideos time: " + watch.timeDes());

        return answer;

    }

    private List<YouTubeVideoDTO> convertVideos(List<VideoEntry> videos) {

        List<YouTubeVideoDTO> youtubeVideosList = new LinkedList<YouTubeVideoDTO>();
        int cont = 0;
        for (VideoEntry videoEntry : videos) {
            YouTubeVideoDTO ytv = convertVideo(videoEntry);
            ytv.setRowIndex(cont++);
            youtubeVideosList.add(ytv);
        }

        return youtubeVideosList;
    }

    private YouTubeVideoDTO convertVideo(VideoEntry videoEntry) {
        YouTubeVideoDTO dto = new YouTubeVideoDTO();

        dto.setMediaId(videoEntry.getId());
        if (videoEntry.getTitle() != null) {
            dto.setTitle(videoEntry.getTitle().getPlainText());
        }

        if (videoEntry.getSummary() != null) {
            dto.setDescription(videoEntry.getSummary().getPlainText());
        }

        YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();

        dto.setMediaId(mediaGroup.getVideoId());
        dto.setDescription(mediaGroup.getDescription().getPlainTextContent());


        MediaKeywords keywords = mediaGroup.getKeywords();
        StringBuilder sb = new StringBuilder();
        for (String keyword : keywords.getKeywords()) {
            sb.append(keyword).append(", ");
        }
        dto.setKeywords(sb.toString());

        YtStatistics stats = videoEntry.getStatistics();
        if (stats != null) {
            dto.setViewCount(stats.getViewCount());
        }

        String webPlayerUrl = mediaGroup.getPlayer().getUrl();
        dto.setWebPlayerUrl(webPlayerUrl);
        String query = "?v=";
        int index = webPlayerUrl.indexOf(query);
        String embeddedWebPlayerUrl = webPlayerUrl.substring(index + query.length());
        embeddedWebPlayerUrl = YOUTUBE_EMBEDDED_URL + embeddedWebPlayerUrl;
        dto.setEmbeddedWebPlayerUrl(embeddedWebPlayerUrl);
        List<String> thumbnails = new LinkedList<String>();
        for (MediaThumbnail mediaThumbnail : mediaGroup.getThumbnails()) {
            thumbnails.add(mediaThumbnail.getUrl());
        }

        if (!thumbnails.isEmpty()) {
            dto.setThumbnail1(thumbnails.get(0));
            dto.setThumbnail2(thumbnails.get(1));
        }

        dto.setThumbnails(thumbnails);
        List<YouTubeMedia> medias = new LinkedList<YouTubeMedia>();
        for (YouTubeMediaContent mediaContent : mediaGroup.getYouTubeContents()) {
            medias.add(new YouTubeMedia(mediaContent.getUrl(), mediaContent.getType(), mediaContent.getDuration()));
        }
        dto.setMedias(medias);



        return dto;
    }

    public static void printVideoEntry(VideoEntry videoEntry, boolean detailed) {
        System.out.println("Title: " + videoEntry.getTitle().getPlainText());

        if (videoEntry.isDraft()) {
            System.out.println("Video is not live");
            YtPublicationState pubState = videoEntry.getPublicationState();
            if (pubState.getState() == YtPublicationState.State.PROCESSING) {
                System.out.println("Video is still being processed.");
            } else if (pubState.getState() == YtPublicationState.State.REJECTED) {
                System.out.print("Video has been rejected because: ");
                System.out.println(pubState.getDescription());
                System.out.print("For help visit: ");
                System.out.println(pubState.getHelpUrl());
            } else if (pubState.getState() == YtPublicationState.State.FAILED) {
                System.out.print("Video failed uploading because: ");
                System.out.println(pubState.getDescription());
                System.out.print("For help visit: ");
                System.out.println(pubState.getHelpUrl());
            }
        }

        if (videoEntry.getEditLink() != null) {
            System.out.println("Video is editable by current user.");
        }

        if (detailed) {

            YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();

            System.out.println("Uploaded by: " + mediaGroup.getUploader());

            System.out.println("Video ID: " + mediaGroup.getVideoId());
            System.out.println("Description: "
                    + mediaGroup.getDescription().getPlainTextContent());

            MediaPlayer mediaPlayer = mediaGroup.getPlayer();
            System.out.println("Web Player URL: " + mediaPlayer.getUrl());
            MediaKeywords keywords = mediaGroup.getKeywords();
            System.out.print("Keywords: ");
            for (String keyword : keywords.getKeywords()) {
                System.out.print(keyword + ",");
            }

            GeoRssWhere location = videoEntry.getGeoCoordinates();
            if (location != null) {
                System.out.println("Latitude: " + location.getLatitude());
                System.out.println("Longitude: " + location.getLongitude());
            }

            Rating rating = videoEntry.getRating();
            if (rating != null) {
                System.out.println("Average rating: " + rating.getAverage());
            }

            YtStatistics stats = videoEntry.getStatistics();
            if (stats != null) {
                System.out.println("View count: " + stats.getViewCount());
            }
            System.out.println();

            System.out.println("\tThumbnails:");
            for (MediaThumbnail mediaThumbnail : mediaGroup.getThumbnails()) {
                System.out.println("\t\tThumbnail URL: " + mediaThumbnail.getUrl());
                System.out.println("\t\tThumbnail Time Index: "
                        + mediaThumbnail.getTime());
                System.out.println();
            }

            System.out.println("\tMedia:");
            for (YouTubeMediaContent mediaContent : mediaGroup.getYouTubeContents()) {
                System.out.println("\t\tMedia Location: " + mediaContent.getUrl());
                System.out.println("\t\tMedia Type: " + mediaContent.getType());
                System.out.println("\t\tDuration: " + mediaContent.getDuration());
                System.out.println();
            }

            for (YouTubeMediaRating mediaRating : mediaGroup.getYouTubeRatings()) {
                System.out.println("Video restricted in the following countries: "
                        + mediaRating.getCountries().toString());
            }

            System.out.println("Restrictions");
            for (MediaRestriction m : mediaGroup.getRestrictions()) {
                System.out.println("relationShip: " + m.getRelationship().toString());
                System.out.println("type: " + m.getType().toString());
                System.out.println("content: " + m.getContent());
            }


        }
    }
}
