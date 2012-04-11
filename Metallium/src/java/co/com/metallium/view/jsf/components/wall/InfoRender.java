/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.components.wall;

import co.com.metallium.core.constants.UserWallPostTypeEnum;

/**
 * 20110823
 * @author Ruben
 */
public class InfoRender {


    private Boolean text = false;
    private Boolean link = false;
    private Boolean image = false;
    private Boolean video = false;

    public InfoRender() {
        initialize(UserWallPostTypeEnum.VIDEO); 
    }

    public void initialize(UserWallPostTypeEnum type) {

        setTypeFalse();
        if (UserWallPostTypeEnum.TEXT.equals(type)) {
            text = true;
        } else if (UserWallPostTypeEnum.LINK.equals(type)) {
            link = true;

        } else if (UserWallPostTypeEnum.IMAGE.equals(type)) {
            image = true;

        } else if (UserWallPostTypeEnum.VIDEO.equals(type)) {
            video = true;
        }
    }

    private void setTypeFalse(){
    text = false;
    link = false;
    image = false;
    video = false;
    }


    public Boolean getImage() {
        return image;
    }

    public void setImage(Boolean image) {
        this.image = image;
    }

    public Boolean getLink() {
        return link;
    }

    public void setLink(Boolean link) {
        this.link = link;
    }

    public Boolean getText() {
        return text;
    }

    public void setText(Boolean text) {
        this.text = text;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

}
