/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.service.dto.search;

import com.metallium.utils.search.SearchBaseDTO;

/**
 * 20110112
 * @author Ruben
 */
public class NotificationSearchDTO extends SearchBaseDTO {

    private Integer idUserTo;
    private Integer idUserFrom;
    private Short type;
    private Boolean read;


    public NotificationSearchDTO() {
    }

    public Integer getIdUserFrom() {
        return idUserFrom;
    }

    public void setIdUserFrom(Integer idUserFrom) {
        this.idUserFrom = idUserFrom;
    }

    public Integer getIdUserTo() {
        return idUserTo;
    }

    public void setIdUserTo(Integer idUserTo) {
        this.idUserTo = idUserTo;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

}
