/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.service.dto.search;

import com.metallium.utils.search.SearchBaseDTO;

/**
 * 20110101
 * @author Ruben
 */
public class UserSearchDTO extends SearchBaseDTO {

    private Integer userId;

    public UserSearchDTO() {
    }

    public UserSearchDTO(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
