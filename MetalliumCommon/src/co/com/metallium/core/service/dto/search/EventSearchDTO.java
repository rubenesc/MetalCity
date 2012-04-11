/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.service.dto.search;

import co.com.metallium.core.entity.Event;
import com.metallium.utils.search.SearchBaseDTO;
import java.util.Date;

/**
 * 20110428
 * @author Ruben
 */
public class EventSearchDTO extends SearchBaseDTO {

    private Date date1;
    private Date date2;
    private String state;
    private Integer userIdCreated;
    private Integer eventId;
    private Integer network;
    private String search; //Search for some key words, like the title or something in the body

    public EventSearchDTO() {
        this.setTheClass(Event.class);
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

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
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

    public Integer getUserIdCreated() {
        return userIdCreated;
    }

    public void setUserIdCreated(Integer userIdCreated) {
        this.userIdCreated = userIdCreated;
    }

}
