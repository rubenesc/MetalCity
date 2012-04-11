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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Ruben
 */
@Entity
@Table(name = "met_log_user_delete")
@NamedQueries({
    @NamedQuery(name = "LogUserDelete.findAll", query = "SELECT l FROM LogUserDelete l"),
    @NamedQuery(name = "LogUserDelete.findById", query = "SELECT l FROM LogUserDelete l WHERE l.id = :id"),
    @NamedQuery(name = "LogUserDelete.findByUserId", query = "SELECT l FROM LogUserDelete l WHERE l.userId = :userId"),
    @NamedQuery(name = "LogUserDelete.findByEmail", query = "SELECT l FROM LogUserDelete l WHERE l.email = :email"),
    @NamedQuery(name = "LogUserDelete.findBySessionId", query = "SELECT l FROM LogUserDelete l WHERE l.sessionId = :sessionId"),
    @NamedQuery(name = "LogUserDelete.findByIp", query = "SELECT l FROM LogUserDelete l WHERE l.ip = :ip"),
    @NamedQuery(name = "LogUserDelete.findByDate", query = "SELECT l FROM LogUserDelete l WHERE l.date = :date"),
    @NamedQuery(name = "LogUserDelete.findByWhy", query = "SELECT l FROM LogUserDelete l WHERE l.why = :why")})
public class LogUserDelete implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "session_id")
    private String sessionId;
    @Basic(optional = false)
    @Column(name = "ip")
    private String ip;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "why")
    private String why;

    public LogUserDelete() {
    }

    public LogUserDelete(Integer id) {
        this.id = id;
    }

    public LogUserDelete(Integer id, int userId, String email, String sessionId, String ip, Date date) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.sessionId = sessionId;
        this.ip = ip;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getWhy() {
        return why;
    }

    public void setWhy(String why) {
        this.why = why;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LogUserDelete)) {
            return false;
        }
        LogUserDelete other = (LogUserDelete) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.LogUserDelete[id=" + id + "]";
    }

}
