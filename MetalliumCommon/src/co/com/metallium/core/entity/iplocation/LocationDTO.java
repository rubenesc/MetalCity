/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.entity.iplocation;

import java.io.Serializable;

/**
 * It was an Entity but I converted it in a POJO
 * because Im only using it in a Native Query, but Im leaving it here mean while.
 * @author Ruben
 */
public class LocationDTO implements Serializable {

    //LocationDTO table
    private Integer id;
    private String countryCode;
    private String regionCode;
    private String city;
    private String zipcode;

    //My Stuff
    private String locale;
    private Integer network;

    public LocationDTO() {
    }

    public LocationDTO(String locale, Integer network) {
        this.locale = locale;
        this.network = network;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Integer getNetwork() {
        return network;
    }

    public void setNetwork(Integer network) {
        this.network = network;
    }

}
