/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.entity;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.service.dto.Ext;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
 * 20110428
 * @author Ruben
 */
@Entity
@Cache(type=CacheType.SOFT, size=64000, expiry=36000000, coordinationType=CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS)
@Table(name = "met_event")
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
    @NamedQuery(name = "Event.findByAlias", query = "SELECT e FROM Event e WHERE e.alias = :alias"),
    @NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id"),
    @NamedQuery(name = "Event.findByTitle", query = "SELECT e FROM Event e WHERE e.title = :title"),
    @NamedQuery(name = "Event.findByCity", query = "SELECT e FROM Event e WHERE e.city = :city"),
    @NamedQuery(name = "Event.findByEventDate", query = "SELECT e FROM Event e WHERE e.eventDate = :eventDate"),
    @NamedQuery(name = "Event.findByDate", query = "SELECT e FROM Event e WHERE e.date = :date"),
    @NamedQuery(name = "Event.findByCoverImage", query = "SELECT e FROM Event e WHERE e.coverImage = :coverImage"),
    @NamedQuery(name = "Event.findByCoverImageThumbnail", query = "SELECT e FROM Event e WHERE e.coverImageThumbnail = :coverImageThumbnail"),
    @NamedQuery(name = "Event.findByState", query = "SELECT e FROM Event e WHERE e.state = :state"),
    @NamedQuery(name = "Event.findByNumberComments", query = "SELECT e FROM Event e WHERE e.numberComments = :numberComments"),

    //These two count queries names must no be changed. If they do, then verify that both end with the 'Create' and 'Edit' word respectively
    //and as update the Event.namedQueryCountAlias constant
    @NamedQuery(name = "Event.countEventByAliasCreate", query = "Select COUNT(DISTINCT e.id) From Event e Where e.alias = :alias"),
    @NamedQuery(name = "Event.countEventByAliasEdit", query = "Select COUNT(DISTINCT e.id) From Event e Where e.alias = :alias and e.id <> :id ")


})
public class Event extends Ext implements Serializable {
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
    @Lob
    @Column(name = "preview")
    private String preview;
    @Basic(optional = false)
    @Column(name = "city")
    private String city;
    @Basic(optional = false)
    @Column(name = "location")
    private String location;
    @Basic(optional = false)
    @Column(name = "event_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "cover_image")
    private String coverImage;
    @Column(name = "cover_image_thumbnail")
    private String coverImageThumbnail;
    @Basic(optional = false)
    @Column(name = "state")
    private String state;
    @Basic(optional = false)
    @Column(name = "number_comments")
    private int numberComments;
    @Column(name = "media_url")
    private String mediaUrl;


//    @JoinColumn(name = "user_id_created", referencedColumnName = "id")
//    @ManyToOne(optional = false)
//    private User userCreated;
    @Basic(optional = false)
    @Column(name = "user_id_created")
    private Integer userCreated;

//    @JoinColumn(name = "user_id_approved", referencedColumnName = "id")
//    @ManyToOne
//    private User userApproved;
    @Basic(optional = false)
    @Column(name = "user_id_approved")
    private Integer userApproved;

//    @JoinColumn(name = "id_network", referencedColumnName = "id")
//    @ManyToOne(optional = false)
//    private Network network;
    @Basic(optional = false)
    @Column(name = "id_network")
    private Integer network;



    //Search properties.
    @Transient
    public static String entitySearchQueryFragment = "SELECT e FROM Event e ";
    @Transient
    public static String entityCountQueryFragment = "Select COUNT(DISTINCT e.id) FROM Event e ";
    @Transient
    public static String entityOrderByQueryFragment =  " order by e.eventDate asc"; //This is for the index page, they have to be ordered AScending by date.

    @Transient
    public static String entityOrderByQueryFragmentDesc =  " order by e.eventDate desc"; //I will use this in the event control page, to display the events descending.

    //extra. this query is used in a method called 'generateAliasForTitle'
    @Transient
    public static String namedQueryCountAlias = "Event.countEventByAlias";


    public Event() {
    }

    public Event(Integer id) {
        this.id = id;
    }

    public Event(Integer id, String title, String content, String city, Date eventDate, Date date, String state, int numberComments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.city = city;
        this.eventDate = eventDate;
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

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
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

    public String getCoverImageLocation() {
        try {
            return MetConfiguration.WEB_IMAGE_SERVLET_PATH.concat(this.getCoverImage());
        } catch (Exception e) {
            return "";
        }
    }

    public String getCoverImageThumbnail() {
        return coverImageThumbnail;
    }

    public void setCoverImageThumbnail(String coverImageThumbnail) {
        this.coverImageThumbnail = coverImageThumbnail;
    }

    public String getCoverImageThumbnailLocation() {
        try {
            return MetConfiguration.WEB_IMAGE_SERVLET_PATH.concat(this.getCoverImageThumbnail());
        } catch (Exception e) {
            return "";
        }
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

    public Integer getNetwork() {
        return network;
    }

    public void setNetwork(Integer network) {
        this.network = network;
    }

    public Integer getUserApproved() {
        return userApproved;
    }

    public void setUserApproved(Integer userApproved) {
        this.userApproved = userApproved;
    }

    public Integer getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(Integer userCreated) {
        this.userCreated = userCreated;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
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
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.Event[id=" + id + "]";
    }

}
