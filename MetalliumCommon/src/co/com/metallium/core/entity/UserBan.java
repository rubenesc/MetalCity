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
import javax.persistence.Transient;

/**
 * 20110126
 * @author Ruben
 */
@Entity
@Table(name = "met_user_ban")
@NamedQueries({
    @NamedQuery(name = "UserBan.findAll", query = "SELECT u FROM UserBan u"),
    @NamedQuery(name = "UserBan.findById", query = "SELECT u FROM UserBan u WHERE u.id = :id"),
    @NamedQuery(name = "UserBan.findByBannedUserId", query = "SELECT u FROM UserBan u WHERE u.bannedUserId = :bannedUserId"),
    @NamedQuery(name = "UserBan.findByAdminId", query = "SELECT u FROM UserBan u WHERE u.adminId = :adminId"),
    @NamedQuery(name = "UserBan.findByDate", query = "SELECT u FROM UserBan u WHERE u.date = :date"),
    @NamedQuery(name = "UserBan.findByWhy", query = "SELECT u FROM UserBan u WHERE u.why = :why"),
    @NamedQuery(name = "UserBan.findByBanExpirationDate", query = "SELECT u FROM UserBan u WHERE u.banExpirationDate = :banExpirationDate")})
public class UserBan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "banned_user_id")
    private int bannedUserId;
    @Basic(optional = false)
    @Column(name = "admin_id")
    private Integer adminId;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "why")
    private String why;
    @Basic(optional = false)
    @Column(name = "ban_expiration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date banExpirationDate;
    @Transient
    private String banTime; //Keeps the ban time contant selected from the ban page dialog. With this I can calculate how much time is the ban gonna last.

    public UserBan() {
    }

    public UserBan(Integer id) {
        this.id = id;
    }

    public UserBan(Integer id, int bannedUserId, int adminId, Date date, Date banExpirationDate) {
        this.id = id;
        this.bannedUserId = bannedUserId;
        this.adminId = adminId;
        this.date = date;
        this.banExpirationDate = banExpirationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getBannedUserId() {
        return bannedUserId;
    }

    public void setBannedUserId(int bannedUserId) {
        this.bannedUserId = bannedUserId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
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

    public Date getBanExpirationDate() {
        return banExpirationDate;
    }

    public void setBanExpirationDate(Date banExpirationDate) {
        this.banExpirationDate = banExpirationDate;
    }

    public String getBanTime() {
        return banTime;
    }

    public void setBanTime(String banTime) {
        this.banTime = banTime;
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
        if (!(object instanceof UserBan)) {
            return false;
        }
        UserBan other = (UserBan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.UserBan[id=" + id + "]";
    }
}
