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
@Table(name = "met_user_favorite")
@NamedQueries({
    @NamedQuery(name = "UserFavorite.findAll", query = "SELECT m FROM UserFavorite m"),
    @NamedQuery(name = "UserFavorite.findById", query = "SELECT m FROM UserFavorite m WHERE m.id = :id"),
    @NamedQuery(name = "UserFavorite.findByBand", query = "SELECT m FROM UserFavorite m WHERE m.band = :band"),
    @NamedQuery(name = "UserFavorite.findByAlbum", query = "SELECT m FROM UserFavorite m WHERE m.album = :album"),
    @NamedQuery(name = "UserFavorite.findBySong", query = "SELECT m FROM UserFavorite m WHERE m.song = :song"),
    @NamedQuery(name = "UserFavorite.findByMovie", query = "SELECT m FROM UserFavorite m WHERE m.movie = :movie"),
    @NamedQuery(name = "UserFavorite.findByAuthor", query = "SELECT m FROM UserFavorite m WHERE m.author = :author"),
    @NamedQuery(name = "UserFavorite.findByGod", query = "SELECT m FROM UserFavorite m WHERE m.god = :god"),
    @NamedQuery(name = "UserFavorite.findByGame", query = "SELECT m FROM UserFavorite m WHERE m.game = :game"),
    @NamedQuery(name = "UserFavorite.findByDrink", query = "SELECT m FROM UserFavorite m WHERE m.drink = :drink")})
public class UserFavorite implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "band")
    private String band;
    @Column(name = "album")
    private String album;
    @Column(name = "song")
    private String song;
    @Column(name = "movie")
    private String movie;
    @Column(name = "author")
    private String author;
    @Column(name = "book")
    private String book;
    @Column(name = "god")
    private String god;
    @Column(name = "game")
    private String game;
    @Column(name = "drink")
    private String drink;
    @Basic(optional = false)
    @Column(name = "display")
    private boolean display = false;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch=FetchType.LAZY)
    private User user;

    public UserFavorite() {
    }

    public UserFavorite(Integer id) {
        this.id = id;
    }

    public UserFavorite(Integer id, boolean display) {
        this.id = id;
        this.display = display;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getGod() {
        return god;
    }

    public void setGod(String god) {
        this.god = god;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
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
        if (!(object instanceof UserFavorite)) {
            return false;
        }
        UserFavorite other = (UserFavorite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.entity.UserFavorite[id=" + id + "]";
    }

}
