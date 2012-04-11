/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.youtube;

import java.util.List;

public class YouTubeVideoDTO {


    private Integer id;
    private String mediaId;
    private String title;
    private String description;
    private String webPlayerUrl;
    private String keywords;
    private Long viewCount;
    private Integer duration;
    private String thumbnail1;
    private String thumbnail2;
    
    private List<String> thumbnails;
    private List<YouTubeMedia> medias;
    private String embeddedWebPlayerUrl;
    
    
    private boolean restricted;
    private String restrictedCountries;
    
    //Parameters for view display
    private Integer rowIndex;
    private boolean selected;

    public YouTubeVideoDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
    
    public List<String> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<String> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public List<YouTubeMedia> getMedias() {
        return medias;
    }

    public void setMedias(List<YouTubeMedia> medias) {
        this.medias = medias;
    }

    public String getWebPlayerUrl() {
        return webPlayerUrl;
    }

    public void setWebPlayerUrl(String webPlayerUrl) {
        this.webPlayerUrl = webPlayerUrl;
    }

    public String getEmbeddedWebPlayerUrl() {
        return embeddedWebPlayerUrl;
    }

    public void setEmbeddedWebPlayerUrl(String embeddedWebPlayerUrl) {
        this.embeddedWebPlayerUrl = embeddedWebPlayerUrl;
    }

    public String retrieveHttpLocation() {
        if (medias == null || medias.isEmpty()) {
            return null;
        }
        for (YouTubeMedia media : medias) {
            String location = media.getLocation();
            if (location.startsWith("http")) {
                return location;
            }
        }
        return null;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    public String getRestrictedCountries() {
        return restrictedCountries;
    }

    public void setRestrictedCountries(String restrictedCountries) {
        this.restrictedCountries = restrictedCountries;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public String getThumbnail1() {
        return thumbnail1;
    }

    public void setThumbnail1(String thumbnail1) {
        this.thumbnail1 = thumbnail1;
    }

    public String getThumbnail2() {
        return thumbnail2;
    }

    public void setThumbnail2(String thumbnail2) {
        this.thumbnail2 = thumbnail2;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "YouTubeVideoDTO{" + "id=" + id + ", title=" + title + ", description=" + description + ", restricted=" + restricted + ", restrictedCountries=" + restrictedCountries + ", rowIndex=" + rowIndex + ", selected=" + selected + '}';
    }
    
    
    
}