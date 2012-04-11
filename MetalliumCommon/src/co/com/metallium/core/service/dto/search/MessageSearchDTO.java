/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.service.dto.search;

import com.metallium.utils.search.SearchBaseDTO;
import java.util.Date;

/**
 * 20110102
 * @author Ruben
 */
public class MessageSearchDTO extends SearchBaseDTO {

    private Date date1;
    private Date date2;
    private Integer messageRead;
    private Integer toUser;
    private Integer fromUser;
    private String box;
    
    public MessageSearchDTO() {
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

    public Integer getFromUser() {
        return fromUser;
    }

    public void setFromUser(Integer fromUser) {
        this.fromUser = fromUser;
    }

    public Integer getMessageRead() {
        return messageRead;
    }

    public void setMessageRead(Integer messageRead) {
        this.messageRead = messageRead;
    }

    public Integer getToUser() {
        return toUser;
    }

    public void setToUser(Integer toUser) {
        this.toUser = toUser;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

}
