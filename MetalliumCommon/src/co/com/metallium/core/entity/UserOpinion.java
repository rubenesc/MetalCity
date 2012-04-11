/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;

/**
 *
 * @author Ruben
 */
@Entity
@Cache(type=CacheType.SOFT, size=64000, expiry=36000000, coordinationType=CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS)
@Table(name = "met_user_opinion")
@NamedQueries({
    @NamedQuery(name = "UserOpinion.findAll", query = "SELECT u FROM UserOpinion u"),
    @NamedQuery(name = "UserOpinion.findById", query = "SELECT u FROM UserOpinion u WHERE u.id = :id"),
    @NamedQuery(name = "UserOpinion.findByYourself", query = "SELECT u FROM UserOpinion u WHERE u.yourself = :yourself"),
    @NamedQuery(name = "UserOpinion.findByReligion", query = "SELECT u FROM UserOpinion u WHERE u.religion = :religion"),
    @NamedQuery(name = "UserOpinion.findByMusic", query = "SELECT u FROM UserOpinion u WHERE u.music = :music")})
public class UserOpinion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "yourself")
    private String yourself;
    @Column(name = "religion")
    private String religion;
    @Column(name = "music")
    private String music;
    @Basic(optional = false)
    @Column(name = "display")
    private boolean display = false;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch=FetchType.LAZY)
    private User user;

    public UserOpinion() {
    }

    public UserOpinion(Integer id) {
        this.id = id;
    }

    public UserOpinion(Integer id, boolean display) {
        this.id = id;
        this.display = display;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getYourself() {
        return yourself;
    }

    public void setYourself(String yourself) {
        this.yourself = yourself;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
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
        if (!(object instanceof UserOpinion)) {
            return false;
        }
        UserOpinion other = (UserOpinion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.entity.UserOpinion[id=" + id + "]";
    }

}
