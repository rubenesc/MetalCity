/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Cache(type=CacheType.SOFT, size=64000, expiry=36000000, coordinationType=CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS)
@Table(name = "met_user_friend")
@NamedQueries({
    @NamedQuery(name = "UserFriend.findAll", query = "SELECT u FROM UserFriend u"),
    @NamedQuery(name = "UserFriend.findByIdUser1", query = "SELECT u FROM UserFriend u WHERE u.userFriendPK.idUser1 = :idUser1"),
    @NamedQuery(name = "UserFriend.findByIdUser2", query = "SELECT u FROM UserFriend u WHERE u.userFriendPK.idUser2 = :idUser2"),
    @NamedQuery(name = "UserFriend.findByState", query = "SELECT u FROM UserFriend u WHERE u.state = :state"),
    @NamedQuery(name = "UserFriend.findByDate", query = "SELECT u FROM UserFriend u WHERE u.date = :date"),
    @NamedQuery(name = "UserFriend.deleteFriend", query = "DELETE FROM UserFriend u WHERE u.userFriendPK.idUser1 = :idUser1 and u.userFriendPK.idUser2 = :idUser2 "),
    @NamedQuery(name = "UserFriend.updateFriendState", query = "Update UserFriend AS u set u.state = :state WHERE u.userFriendPK.idUser1 = :idUser1 and u.userFriendPK.idUser2 = :idUser2  ")
})
        
public class UserFriend implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UserFriendPK userFriendPK;
    @Basic(optional = false)
    @Column(name = "state")
    private short state;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "id_user_2", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch=FetchType.EAGER)
    private User user2;
    @JoinColumn(name = "id_user_1", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch=FetchType.LAZY)
    private User user1;

    public UserFriend() {
    }

    public UserFriend(UserFriendPK userFriendPK) {
        this.userFriendPK = userFriendPK;
    }

    public UserFriend(UserFriendPK userFriendPK, short state, Date date) {
        this.userFriendPK = userFriendPK;
        this.state = state;
        this.date = date;
    }

    public UserFriend(int idUser1, int idUser2) {
        this.userFriendPK = new UserFriendPK(idUser1, idUser2);
    }

    public UserFriendPK getUserFriendPK() {
        return userFriendPK;
    }

    public void setUserFriendPK(UserFriendPK userFriendPK) {
        this.userFriendPK = userFriendPK;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userFriendPK != null ? userFriendPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserFriend)) {
            return false;
        }
        UserFriend other = (UserFriend) object;
        if ((this.userFriendPK == null && other.userFriendPK != null) || (this.userFriendPK != null && !this.userFriendPK.equals(other.userFriendPK))) {
            return false;
        }
        return true;

    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.UserFriend[userFriendPK=" + userFriendPK + "]";
    }

}
