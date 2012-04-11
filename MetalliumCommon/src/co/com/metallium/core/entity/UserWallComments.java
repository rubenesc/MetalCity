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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "met_user_wall_comments")
@NamedQueries({
    @NamedQuery(name = "UserWallComments.findAll", query = "SELECT u FROM UserWallComments u"),
    @NamedQuery(name = "UserWallComments.findById", query = "SELECT u FROM UserWallComments u WHERE u.id = :id"),
    @NamedQuery(name = "UserWallComments.findByDate", query = "SELECT u FROM UserWallComments u WHERE u.date = :date"),
    @NamedQuery(name = "UserWallComments.findCommentsByUserWallAndState", query = "Select u From UserWallComments u Where u.userWallId = :userWallId and u.state = :state order by u.date desc"),
    @NamedQuery(name = "UserWallComments.countCommentsByUserWallAndState", query = "Select COUNT(DISTINCT u.id) From UserWallComments u Where u.userWallId = :userWallId and u.state = :state "),
    @NamedQuery(name = "UserWallComments.findByState", query = "SELECT u FROM UserWallComments u WHERE u.state = :state")})
public class UserWallComments implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @Lob
    @Column(name = "comment")
    private String comment;
    @Basic(optional = false)
    @Column(name = "state")
    private String state;

//    @JoinColumn(name = "user_wall_id", referencedColumnName = "id")
//    @ManyToOne(optional = false, fetch=FetchType.LAZY)
//    private User userWall;
    @Basic(optional = false)
    @Column(name = "user_wall_id")
    private Integer userWallId;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch=FetchType.EAGER) //Its eager because when you get a comment it usually to show it on a wall, and you always need info from the user.
    private User user;

    public UserWallComments() {
    }

    public UserWallComments(Integer id) {
        this.id = id;
    }

    public UserWallComments(Integer id, Date date, String comment, String state) {
        this.id = id;
        this.date = date;
        this.comment = comment;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getUserWallId() {
        return userWallId;
    }

    public void setUserWallId(Integer userWallId) {
        this.userWallId = userWallId;
    }

//    public User getUserWall() {
//        return userWall;
//    }
//
//    public void setUserWall(User userWall) {
//        this.userWall = userWall;
//    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        if (!(object instanceof UserWallComments)) {
            return false;
        }
        UserWallComments other = (UserWallComments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.UserWallComments[id=" + id + "]";
    }

}
