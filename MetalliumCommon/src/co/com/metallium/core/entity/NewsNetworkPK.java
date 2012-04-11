/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 20110124
 * @author Ruben
 */
@Embeddable
public class NewsNetworkPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_news")
    private int idNews;
    @Basic(optional = false)
    @Column(name = "id_network")
    private int idNetwork;

    public NewsNetworkPK() {
    }

    public NewsNetworkPK(int idNews, int idNetwork) {
        this.idNews = idNews;
        this.idNetwork = idNetwork;
    }

    public int getIdNews() {
        return idNews;
    }

    public void setIdNews(int idNews) {
        this.idNews = idNews;
    }

    public int getIdNetwork() {
        return idNetwork;
    }

    public void setIdNetwork(int idNetwork) {
        this.idNetwork = idNetwork;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idNews;
        hash += (int) idNetwork;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NewsNetworkPK)) {
            return false;
        }
        NewsNetworkPK other = (NewsNetworkPK) object;
        if (this.idNews != other.idNews) {
            return false;
        }
        if (this.idNetwork != other.idNetwork) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.NewsNetworkPK[idNews=" + idNews + ", idNetwork=" + idNetwork + "]";
    }

}
