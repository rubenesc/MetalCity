/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.entity.stats;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Ruben
 */
@Entity
@Table(name = "metstat_memory_stat")
@NamedQueries({
    @NamedQuery(name = "MemoryStat.findStatsForReport", query = "SELECT m FROM MemoryStat m order by m.date desc"),
    @NamedQuery(name = "MemoryStat.findById", query = "SELECT m FROM MemoryStat m WHERE m.id = :id")
})
public class MemoryStat implements Serializable {
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
    @Column(name = "active_web_sessions")
    private int activeWebSessions;
    @Basic(optional = false)
    @Column(name = "percent_memory_used")
    private short percentMemoryUsed;
    @Basic(optional = false)
    @Column(name = "memory_used")
    private short memoryUsed;
    @Basic(optional = false)
    @Column(name = "memory_free")
    private short memoryFree;
    @Basic(optional = false)
    @Column(name = "memory_total")
    private short memoryTotal;
    @Basic(optional = false)
    @Column(name = "memory_max")
    private short memoryMax;

    public MemoryStat() {
    }

    public MemoryStat(Integer id) {
        this.id = id;
    }

    public MemoryStat(Integer id, Date date, int activeWebSessions, short percentMemoryUsed, short memoryUsed, short memoryFree, short memoryTotal, short memoryMax) {
        this.id = id;
        this.date = date;
        this.activeWebSessions = activeWebSessions;
        this.percentMemoryUsed = percentMemoryUsed;
        this.memoryUsed = memoryUsed;
        this.memoryFree = memoryFree;
        this.memoryTotal = memoryTotal;
        this.memoryMax = memoryMax;
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

    public int getActiveWebSessions() {
        return activeWebSessions;
    }

    public void setActiveWebSessions(int activeWebSessions) {
        this.activeWebSessions = activeWebSessions;
    }

    public short getPercentMemoryUsed() {
        return percentMemoryUsed;
    }

    public void setPercentMemoryUsed(short percentMemoryUsed) {
        this.percentMemoryUsed = percentMemoryUsed;
    }

    public short getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(short memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public short getMemoryFree() {
        return memoryFree;
    }

    public void setMemoryFree(short memoryFree) {
        this.memoryFree = memoryFree;
    }

    public short getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(short memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public short getMemoryMax() {
        return memoryMax;
    }

    public void setMemoryMax(short memoryMax) {
        this.memoryMax = memoryMax;
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
        if (!(object instanceof MemoryStat)) {
            return false;
        }
        MemoryStat other = (MemoryStat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.stats.MemoryStat[id=" + id + "]";
    }

}
