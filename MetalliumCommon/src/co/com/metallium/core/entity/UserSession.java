/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;

/**
 *
 * @author Ruben
 */
@Entity
@Cache(type = CacheType.SOFT, size = 64000, expiry = 36000000, coordinationType = CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS)
@Table(name = "met_user_session")
@NamedQueries({
    @NamedQuery(name = "UserSession.findAll", query = "SELECT u FROM UserSession u"),
    @NamedQuery(name = "UserSession.findByUserId", query = "SELECT u FROM UserSession u WHERE u.userId = :userId"),
    @NamedQuery(name = "UserSession.deleteSession", query = "DELETE FROM UserSession u WHERE u.userId = :userId "),
    @NamedQuery(name = "UserSession.findByUuid", query = "SELECT u FROM UserSession u WHERE u.uuid = :uuid"),
    @NamedQuery(name = "UserSession.findByDate", query = "SELECT u FROM UserSession u WHERE u.date = :date")})
public class UserSession implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    @Id
    @Basic(optional = false)
    @Column(name = "uuid")
    private String uuid;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public UserSession() {
    }

    public UserSession(String uuid) {
        this.uuid = uuid;
    }

    public UserSession(String uuid, int userId, Date date) {
        this.uuid = uuid;
        this.userId = userId;
        this.date = date;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uuid != null ? uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserSession)) {
            return false;
        }
        UserSession other = (UserSession) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.UserSession[ uuid=" + uuid + " ]";
    }
    
}
