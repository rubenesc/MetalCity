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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;

/**
 * 20110525
 * @author Ruben
 */
@Entity
@Cache(type=CacheType.SOFT, size=64000, expiry=36000000, coordinationType=CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS)
@Table(name = "met_forum")
@NamedQueries({
    @NamedQuery(name = "Forum.findAll", query = "SELECT f FROM Forum f"),
    @NamedQuery(name = "Forum.findByAlias", query = "SELECT f FROM Forum f WHERE f.alias = :alias and f.state = :state"),
    @NamedQuery(name = "Forum.findByIdAndState", query = "SELECT f FROM Forum f WHERE f.id = :id and f.state = :state"),
    @NamedQuery(name = "Forum.findById", query = "SELECT f FROM Forum f WHERE f.id = :id"),
    @NamedQuery(name = "Forum.findByTitle", query = "SELECT f FROM Forum f WHERE f.title = :title"),
    @NamedQuery(name = "Forum.findByDate", query = "SELECT f FROM Forum f WHERE f.date = :date"),
    @NamedQuery(name = "Forum.findByCoverImage", query = "SELECT f FROM Forum f WHERE f.coverImage = :coverImage"),
    @NamedQuery(name = "Forum.findByState", query = "SELECT f FROM Forum f WHERE f.state = :state"),
    @NamedQuery(name = "Forum.findByNumberComments", query = "SELECT f FROM Forum f WHERE f.numberComments = :numberComments"),
    @NamedQuery(name = "Forum.findByCoverImageThumbnail", query = "SELECT f FROM Forum f WHERE f.coverImageThumbnail = :coverImageThumbnail"),

    //These two count queries names must no be changed. If they do, then verify that both end with the 'Create' and 'Edit' word respectively
    //and as update the Event.namedQueryCountAlias constant
    @NamedQuery(name = "Forum.countForumByAliasCreate", query = "Select COUNT(DISTINCT f.id) From Forum f Where f.alias = :alias"),
    @NamedQuery(name = "Forum.countForumByAliasEdit", query = "Select COUNT(DISTINCT f.id) From Forum f Where F.alias = :alias and f.id <> :id ")


})
public class Forum implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "alias")
    private String alias;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @Lob
    @Column(name = "content")
    private String content;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "cover_image")
    private String coverImage;

    @Basic(optional = false)
    @Column(name = "state")
    private String state;

    @Basic(optional = false)
    @Column(name = "number_comments")
    private int numberComments;

//    @Basic(optional = false)
//    @Column(name = "number_views")
//    private int numberViews; //I dont think Im going to use this field.

    @Column(name = "cover_image_thumbnail")
    private String coverImageThumbnail;

    @JoinColumn(name = "category", referencedColumnName = "id")
    @ManyToOne
    private ForumCategory category;
    
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User user;

    @Basic(optional = false)
    @Column(name = "network")
    private Integer network;


    //Search properties.
    @Transient
    public static String entitySearchQueryFragment = "SELECT f FROM Forum f ";
    @Transient
    public static String entityCountQueryFragment = "Select COUNT(DISTINCT f.id) FROM Forum f ";
    @Transient
    public static String entityOrderByQueryFragment =  " order by f.date desc"; //This is for the index page, they have to be ordered AScending by date.


    //extra. this query is used in a method called 'generateAliasForTitle'
    @Transient
    public static String namedQueryCountAlias = "Forum.countForumByAlias";


    public Forum() {
    }

    public Forum(Integer id) {
        this.id = id;
    }

    public Forum(Integer id, String title, String content, Date date, String state, int numberComments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.state = state;
        this.numberComments = numberComments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getNumberComments() {
        return numberComments;
    }

    public void setNumberComments(int numberComments) {
        this.numberComments = numberComments;
    }

//    public int getNumberViews() {
//        return numberViews;
//    }
//
//    public void setNumberViews(int numberViews) {
//        this.numberViews = numberViews;
//    }

    public String getCoverImageThumbnail() {
        return coverImageThumbnail;
    }

    public void setCoverImageThumbnail(String coverImageThumbnail) {
        this.coverImageThumbnail = coverImageThumbnail;
    }

    public ForumCategory getCategory() {
        return category;
    }

    public void setCategory(ForumCategory category) {
        this.category = category;
    }

    public Integer getNetwork() {
        return network;
    }

    public void setNetwork(Integer network) {
        this.network = network;
    }

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
        if (!(object instanceof Forum)) {
            return false;
        }
        Forum other = (Forum) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.Forum[id=" + id + "]";
    }

}
