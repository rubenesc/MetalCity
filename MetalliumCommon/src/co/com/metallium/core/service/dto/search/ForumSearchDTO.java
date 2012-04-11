/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.service.dto.search;

import co.com.metallium.core.constants.state.ForumCategoryStateEnum;
import co.com.metallium.core.entity.Forum;
import com.metallium.utils.search.SearchBaseDTO;
import java.util.Date;

/**
 * 20110525
 * @author Ruben
 */
public class ForumSearchDTO extends SearchBaseDTO {

    private Date date1;
    private Date date2;
    private String state;
    private Integer id;
    private Integer userId;
    private Integer network;
    private String search; //Search for some key words, like the title or something in the body
    private Short category;

    public ForumSearchDTO() {
        this.setTheClass(Forum.class);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getNetwork() {
        return network;
    }

    public void setNetwork(Integer network) {
        this.network = network;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
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

    public Short getCategory() {
        return category;
    }

    public void setCategory(Short category) {
        this.category = category;
    }
    
}
