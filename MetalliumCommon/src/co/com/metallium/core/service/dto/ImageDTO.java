/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service.dto;

import co.com.metallium.core.constants.MetConfiguration;
import java.io.Serializable;
import javax.persistence.Transient;

/**
 * 20101122
 * @author Ruben
 */
public class ImageDTO implements Serializable {

    @Transient
    private String imageURL;
    @Transient
    private String thumbnailURL;
    @Transient
    private String imageName;

    public ImageDTO() {
    }

    public ImageDTO(String imageName) {
        this.imageName = imageName;
    }

    public ImageDTO(String imageURL, String imageName) {
        this.imageURL = imageURL;
        this.imageName = imageName;
    }

    public ImageDTO(String imageURL, String thumbnailURL, String imageName) {
        this.imageURL = imageURL;
        this.thumbnailURL = thumbnailURL;
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getImageLocation() {
        return MetConfiguration.WEB_IMAGE_SERVLET_PATH.concat(getImageURL());
    }

    public String getThumbnailLocation() {
        return MetConfiguration.WEB_IMAGE_SERVLET_PATH.concat(getThumbnailURL());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImageDTO other = (ImageDTO) obj;
        if ((this.imageName == null) ? (other.imageName != null) : !this.imageName.equals(other.imageName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    
}
