/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.service.dto.search;

import co.com.metallium.core.constants.state.UserFriendState;
import com.metallium.utils.search.SearchBaseDTO;

/**
 * 20110110
 * @author Ruben
 */
public class UserFriendsSearchDTO extends SearchBaseDTO {

    private Integer userId;
    private String userNick;
    private Short state = UserFriendState.FRIENDSHIP_APPROVED;

    public UserFriendsSearchDTO() {
    }

    public UserFriendsSearchDTO(Class theClass) {
        this.setTheClass(theClass);
    }

    public UserFriendsSearchDTO(Integer userId) {
        this.userId = userId;
    }

    public UserFriendsSearchDTO(String userNick) {
        this.userNick = userNick;
    }

    public UserFriendsSearchDTO(Integer userId, Short state) {
        this.userId = userId;
        this.state = state;
    }

    public UserFriendsSearchDTO(String userNick, Short state) {
        this.userNick = userNick;
        this.state = state;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
