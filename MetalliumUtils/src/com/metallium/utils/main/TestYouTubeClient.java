/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.main;

import com.metallium.utils.youtube.YouTubeClient;
import com.metallium.utils.youtube.YouTubeMedia;
import com.metallium.utils.youtube.YouTubeVideoDTO;
import java.util.List;

public class TestYouTubeClient {

    public static void main(String[] args) throws Exception {
//        testRetrieveVideo();
        testRetrieveVideos();


    }

    private static void testRetrieveVideos() throws Exception {
        String textQuery = "Iron Maiden";
        int maxResults = 10;
        boolean filter = true;
        int timeout = 2000;

        YouTubeClient ym = new YouTubeClient();

        List<YouTubeVideoDTO> videos = ym.retrieveVideos(textQuery, maxResults, filter, timeout);
        for (YouTubeVideoDTO youtubeVideo : videos) {
            printVideoInfo(youtubeVideo);
        }
    }

    private static void testRetrieveVideo() throws Exception {
        String textQuery = "Iron Maiden - Virus";
        int maxResults = 10;
        boolean filter = true;
        int timeout = 2000;

        YouTubeClient ym = new YouTubeClient();

        if (true) {
            String clientId = "b3QL6QaKwZI";
            clientId = "tI_sv5uswoY";  //Pink Floyd
            YouTubeVideoDTO youtubeVideo = ym.retrieveVideo(clientId);
            printVideoInfo(youtubeVideo);

        } else {
            List<YouTubeVideoDTO> videos = ym.retrieveVideos(textQuery, maxResults, filter, timeout);
            for (YouTubeVideoDTO youtubeVideo : videos) {
                printVideoInfo(youtubeVideo);
            }
        }
    }

    private static void printVideoInfo(YouTubeVideoDTO youtubeVideo) {
        System.out.println("");
        System.out.println("Id: " + youtubeVideo.getId());
        System.out.println("Title: " + youtubeVideo.getTitle());
        System.out.println("Description: " + youtubeVideo.getDescription());
        System.out.println("Keywords: " + youtubeVideo.getKeywords());
        System.out.println("View Count: " + youtubeVideo.getViewCount());

        System.out.println(youtubeVideo.getWebPlayerUrl());
        System.out.println("Thumbnails");
        for (String thumbnail : youtubeVideo.getThumbnails()) {
            System.out.println("\t" + thumbnail);
        }

        System.out.println("Media");
        for (YouTubeMedia media : youtubeVideo.getMedias()) {
            System.out.println("\t" + media.getLocation() + " - " + media.getType() + " - " + media.getDuration());
        }


        System.out.println(youtubeVideo.getEmbeddedWebPlayerUrl());
        System.out.println("************************************");
    }
}