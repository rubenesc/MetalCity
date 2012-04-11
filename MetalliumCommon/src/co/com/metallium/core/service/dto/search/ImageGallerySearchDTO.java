/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service.dto.search;

import com.metallium.utils.search.SearchBaseDTO;
import java.util.Date;

/**
 * 20101124
 * @author Ruben
 */
public class ImageGallerySearchDTO extends SearchBaseDTO {

    private Date date1;
    private Date date2;
    private String state;
    private Integer userId;
    private Integer galleryId;
    private String galleryDirectory;
    private String userPath;

    public ImageGallerySearchDTO() {
    }

    public ImageGallerySearchDTO(Integer galleryId, Integer userId, String galleryDirectory, String state) {
        this.galleryId = galleryId;
        this.userId = userId;
        this.galleryDirectory = galleryDirectory;
        this.state = state;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public Integer getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(Integer galleryId) {
        this.galleryId = galleryId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getGalleryDirectory() {
        return galleryDirectory;
    }

    public void setGalleryDirectory(String galleryDirectory) {
        this.galleryDirectory = galleryDirectory;
    }

    public String getUserPath() {
        return userPath;
    }

    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }

}
