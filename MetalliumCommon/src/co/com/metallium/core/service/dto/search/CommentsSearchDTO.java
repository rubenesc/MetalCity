/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service.dto.search;

import com.metallium.utils.search.SearchBaseDTO;
import java.util.Date;

/**
 *
 * @author Ruben Escudero
 */
public class CommentsSearchDTO extends SearchBaseDTO {

    private Date date1;
    private Date date2;
    private String state;
    private Integer userId;
    private Integer entityId; //THis could be "newsId" || "eventId" || what ever
    private String imageId;
    private String type;
    private Integer commentId;

    public CommentsSearchDTO() {
    }

    public CommentsSearchDTO(Integer entityId, String state, String type, Class aClass) {
        this.entityId = entityId;
        this.state = state;
        this.type = type;
        this.setTheClass(aClass); //THis could be "NewsComments" || "EventComments" || what ever type of comments
    }

    public CommentsSearchDTO(Integer entityId, String imageId, String state, String type, Class aClass) {
        this.entityId = entityId;
        this.imageId = imageId;
        this.state = state;
        this.type = type;
        this.setTheClass(aClass); //THis could be "NewsComments" || "EventComments" || what ever type of comments
    }


    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
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

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

}
