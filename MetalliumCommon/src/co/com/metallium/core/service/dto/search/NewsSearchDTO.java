/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service.dto.search;

import co.com.metallium.core.entity.News;
import com.metallium.utils.search.SearchBaseDTO;
import java.util.Date;

/**
 * 20101111
 * @author Ruben
 */
public class NewsSearchDTO extends SearchBaseDTO {

    private Date date1;
    private Date date2;
    private String state;
    private Integer userIdPublisher;
    private Integer newsId;
    private Integer network;

    public NewsSearchDTO() {
        this.setTheClass(News.class);
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

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getUserIdPublisher() {
        return userIdPublisher;
    }

    public void setUserIdPublisher(Integer userIdPublisher) {
        this.userIdPublisher = userIdPublisher;
    }

    public Integer getNetwork() {
        return network;
    }

    public void setNetwork(Integer network) {
        this.network = network;
    }
}
