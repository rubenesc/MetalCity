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
@Table(name = "met_log_user_authentication")
@NamedQueries({
    @NamedQuery(name = "LogUserAuthentication.findAll", query = "SELECT l FROM LogUserAuthentication l"),
    @NamedQuery(name = "LogUserAuthentication.findById", query = "SELECT l FROM LogUserAuthentication l WHERE l.id = :id"),
    @NamedQuery(name = "LogUserAuthentication.findByEmail", query = "SELECT l FROM LogUserAuthentication l WHERE l.email = :email"),
    @NamedQuery(name = "LogUserAuthentication.findBySessionId", query = "SELECT l FROM LogUserAuthentication l WHERE l.sessionId = :sessionId"),
    @NamedQuery(name = "LogUserAuthentication.findByIp", query = "SELECT l FROM LogUserAuthentication l WHERE l.ip = :ip"),
    @NamedQuery(name = "LogUserAuthentication.findByDate", query = "SELECT l FROM LogUserAuthentication l WHERE l.date = :date"),
    @NamedQuery(name = "LogUserAuthentication.countUserFailedLoginAttempts", query = "Select COUNT(DISTINCT l.id) From LogUserAuthentication l Where l.email = :email and l.date BETWEEN :date1 AND :date2 and l.result = :result "),
    @NamedQuery(name = "LogUserAuthentication.findByResult", query = "SELECT l FROM LogUserAuthentication l WHERE l.result = :result")})
public class LogUserAuthentication implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
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
    @Basic(optional = false)
    @Column(name = "result")
    private Integer result;
    @Column(name = "error")
    private String error;
    @Column(name = "location")
    private Integer location;

    public LogUserAuthentication() {
    }

    public LogUserAuthentication(Integer id) {
        this.id = id;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
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
        if (!(object instanceof LogUserAuthentication)) {
            return false;
        }
        LogUserAuthentication other = (LogUserAuthentication) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.LogUserAuthentication[id=" + id + "]";
    }

}
