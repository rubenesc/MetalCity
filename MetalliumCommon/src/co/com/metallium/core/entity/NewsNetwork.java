/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;

/**
 * 20110124
 * @author Ruben
 */
@Entity
@Cache(type=CacheType.SOFT, size=64000, expiry=36000000, coordinationType=CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS)
@Table(name = "met_news_network")
@NamedQueries({
    @NamedQuery(name = "NewsNetwork.findAll", query = "SELECT n FROM NewsNetwork n"),
    @NamedQuery(name = "NewsNetwork.findByIdNews", query = "SELECT n FROM NewsNetwork n WHERE n.newsNetworkPK.idNews = :idNews"),
    @NamedQuery(name = "NewsNetwork.findByIdNetwork", query = "SELECT n FROM NewsNetwork n WHERE n.newsNetworkPK.idNetwork = :idNetwork"),
    @NamedQuery(name = "NewsNetwork.findNews", query = "SELECT n FROM NewsNetwork nn Inner Join nn.news n WHERE nn.newsNetworkPK.idNetwork = :idNetwork AND n.state = :newsState order by n.date desc")
})



public class NewsNetwork implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NewsNetworkPK newsNetworkPK;
    @JoinColumn(name = "id_news", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch=FetchType.EAGER)
    private News news;
    @JoinColumn(name = "id_network", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch=FetchType.LAZY)
    private Network network;

    public NewsNetwork() {
    }

    public NewsNetwork(NewsNetworkPK newsNetworkPK) {
        this.newsNetworkPK = newsNetworkPK;
    }

    public NewsNetwork(int idNews, int idNetwork) {
        this.newsNetworkPK = new NewsNetworkPK(idNews, idNetwork);
    }

    public NewsNetworkPK getNewsNetworkPK() {
        return newsNetworkPK;
    }

    public void setNewsNetworkPK(NewsNetworkPK newsNetworkPK) {
        this.newsNetworkPK = newsNetworkPK;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (newsNetworkPK != null ? newsNetworkPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NewsNetwork)) {
            return false;
        }
        NewsNetwork other = (NewsNetwork) object;
        if ((this.newsNetworkPK == null && other.newsNetworkPK != null) || (this.newsNetworkPK != null && !this.newsNetworkPK.equals(other.newsNetworkPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.NewsNetwork[newsNetworkPK=" + newsNetworkPK + "]";
    }

}
