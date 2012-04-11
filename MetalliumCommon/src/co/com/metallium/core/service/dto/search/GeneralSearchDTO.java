/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.service.dto.search;

import co.com.metallium.core.entity.User;
import com.metallium.utils.search.SearchBaseDTO;
import java.util.List;

/**
 * 20110107
 * @author Ruben
 */
public class GeneralSearchDTO extends SearchBaseDTO {

    private String query;
    private String searchEntity;

    //User
    private Integer friendsOfUserId;

    //User
    private String name;
    private String email;
    private String sex;
    private String country;
    private String city;
    private List<String> state;
    

    public GeneralSearchDTO() {
        this.setTheClass(User.class);
    }

    public GeneralSearchDTO(String query, String searchEntity) {
        this.setTheClass(User.class);
        
        this.query = query;
        this.searchEntity = searchEntity;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSearchEntity() {
        return searchEntity;
    }

    public void setSearchEntity(String searchEntity) {
        this.searchEntity = searchEntity;
    }

    public Integer getFriendsOfUserId() {
        return friendsOfUserId;
    }

    public void setFriendsOfUserId(Integer friendsOfUserId) {
        this.friendsOfUserId = friendsOfUserId;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public List<String> getState() {
        return state;
    }

    public void setState(List<String> state) {
        this.state = state;
    }

}
