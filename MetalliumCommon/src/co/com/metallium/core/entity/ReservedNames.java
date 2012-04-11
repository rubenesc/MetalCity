/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Ruben
 */
@Entity
@Table(name = "met_reserved_names")
@NamedQueries({
    @NamedQuery(name = "ReservedNames.findAll", query = "SELECT r FROM ReservedNames r"),
    @NamedQuery(name = "ReservedNames.findById", query = "SELECT r FROM ReservedNames r WHERE r.id = :id"),
    @NamedQuery(name = "ReservedNames.findByNick", query = "SELECT r FROM ReservedNames r WHERE r.nick = :nick"),
    @NamedQuery(name = "ReservedNames.findByName", query = "SELECT r FROM ReservedNames r WHERE r.name = :name")})
public class ReservedNames implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nick")
    private String nick;
    @Column(name = "name")
    private String name;

    public ReservedNames() {
    }

    public ReservedNames(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof ReservedNames)) {
            return false;
        }
        ReservedNames other = (ReservedNames) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.ReservedNames[id=" + id + "]";
    }

}
