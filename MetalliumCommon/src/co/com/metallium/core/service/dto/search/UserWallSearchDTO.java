/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.service.dto.search;

import co.com.metallium.core.entity.UserWallPost;

/**
 * 20110810
 * @author Ruben
 */
public class UserWallSearchDTO extends UserSearchDTO {


    public UserWallSearchDTO(Integer userId) {
        this.setUserId(userId);
        this.setTheClass(UserWallPost.class);
    }

}
