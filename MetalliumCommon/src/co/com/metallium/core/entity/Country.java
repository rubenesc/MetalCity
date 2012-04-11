/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Ruben
 */
@Entity
@Table(name = "met_country")
@NamedQueries({
    @NamedQuery(name = "Country.findAll", query = "SELECT c FROM Country c order by c.printableName asc"),
    @NamedQuery(name = "Country.findByIso", query = "SELECT c FROM Country c WHERE c.iso = :iso"),
    @NamedQuery(name = "Country.findByName", query = "SELECT c FROM Country c WHERE c.name = :name"),
    @NamedQuery(name = "Country.findByPrintableName", query = "SELECT c FROM Country c WHERE c.printableName = :printableName"),
    @NamedQuery(name = "Country.findByIso3", query = "SELECT c FROM Country c WHERE c.iso3 = :iso3"),
    @NamedQuery(name = "Country.findByNumcode", query = "SELECT c FROM Country c WHERE c.numcode = :numcode")})
public class Country implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "iso")
    private String iso;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "printable_name")
    private String printableName;
    @Column(name = "iso3")
    private String iso3;
    @Column(name = "numcode")
    private Short numcode;

    public Country() {
    }

    public Country(String iso) {
        this.iso = iso;
    }

    public Country(String iso, String name, String printableName) {
        this.iso = iso;
        this.name = name;
        this.printableName = printableName;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrintableName() {
        return printableName;
    }

    public void setPrintableName(String printableName) {
        this.printableName = printableName;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public Short getNumcode() {
        return numcode;
    }

    public void setNumcode(Short numcode) {
        this.numcode = numcode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iso != null ? iso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Country)) {
            return false;
        }
        Country other = (Country) object;
        if ((this.iso == null && other.iso != null) || (this.iso != null && !this.iso.equals(other.iso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.Country[iso=" + iso + "]";
    }

}
