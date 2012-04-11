/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.entity;

import co.com.metallium.core.constants.state.ForumCategoryStateEnum;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 20111213
 * @author Ruben
 */
@Entity
@Table(name = "met_forum_category")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ForumCategory.findAll", query = "SELECT f FROM ForumCategory f"),
    @NamedQuery(name = "ForumCategory.findById", query = "SELECT f FROM ForumCategory f WHERE f.id = :id"),
    @NamedQuery(name = "ForumCategory.findByName", query = "SELECT f FROM ForumCategory f WHERE f.name = :name"),
    @NamedQuery(name = "ForumCategory.findByState", query = "SELECT f FROM ForumCategory f WHERE f.state = :state")})
public class ForumCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    
//    @Basic(optional = false)
//    @Column(name = "state")
//    private Integer state;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state", nullable = false)
    private ForumCategoryStateEnum state;
    
    public ForumCategory() {
    }

    public ForumCategory(Short id) {
        this.id = id;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Integer getState() {
//        return state;
//    }
//
//    public void setState(Integer state) {
//        this.state = state;
//    }
    
    

    public ForumCategoryStateEnum getState() {
        return state;
    }

    public void setState(ForumCategoryStateEnum state) {
        this.state = state;
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
        if (!(object instanceof ForumCategory)) {
            return false;
        }
        ForumCategory other = (ForumCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.ForumCategory[ id=" + id + " ]";
    }
    
}
