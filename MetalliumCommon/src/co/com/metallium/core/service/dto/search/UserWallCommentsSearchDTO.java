/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.service.dto.search;

import com.metallium.utils.search.SearchBaseDTO;
import java.util.Date;

/**
 *
 * @author Ruben
 */
public class UserWallCommentsSearchDTO extends SearchBaseDTO {

    private Date date1;
    private Date date2;
    private String state;
    private Integer userId;
    private Integer userWallId; //User who owns the wall on which "userID" is writing on.
    private Integer commentId;

    public UserWallCommentsSearchDTO() {
    }

    public UserWallCommentsSearchDTO(Integer userWallId, String state) {
        this.userWallId = userWallId;
        this.state = state;
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

    public Integer getUserWallId() {
        return userWallId;
    }

    public void setUserWallId(Integer userWallId) {
        this.userWallId = userWallId;
    }

}
