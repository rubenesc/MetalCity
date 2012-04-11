/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.NetworkConst;
import co.com.metallium.core.entity.iplocation.LocationDTO;
import co.com.metallium.core.exception.IpLocationException;
import com.metallium.utils.dto.PairDTO;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import java.util.HashMap;
import java.util.Locale;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * 20110208
 * @author Ruben
 */
@Stateless
public class IpLocationService {

    @PersistenceContext(unitName = "MetalliumStatsPU")
    private EntityManager em;
    private HashMap<String, LocationDTO> countryLocaleHashMap = new HashMap<String, LocationDTO>();

    public IpLocationService() {
        initializeLocaleMap();
    }

    /**
     * In this Method I enter an IP and it returns the Location of the User
     * Plus the Locale which he is assigned to him, if he doesn't have one
     * then the default locale will be chosen
     *
     * @param ip of the user
     * @return All the Location data.
     */
    public LocationDTO findLocale(String ip) {

        LocationDTO answer = null;
        LocationDTO location = null;
        try {
            answer = findLocation(ip);

            if (answer != null) {
                location = countryLocaleHashMap.get(answer.getCountryCode());
                if (location != null) {
                    answer.setLocale(location.getLocale());
                    answer.setNetwork(location.getNetwork());
                    LogHelper.makeLog("IpLocationService we found a locale: " + answer.getLocale() + ", ip: " + ip + ", country: " + answer.getCountryCode());
                } else {
                    answer = new LocationDTO(MetConfiguration.DEFAULT_LOCALE_LANGUAGE, MetConfiguration.DEFAULT_NETWORK); //This shouldn't happen
                    LogHelper.makeLog("IpLocationService we could not find a locale: " + answer.getLocale() + ", ip: " + ip + ", country: " + answer.getCountryCode());
                }
            } else {
                answer = new LocationDTO(MetConfiguration.DEFAULT_LOCALE_LANGUAGE, MetConfiguration.DEFAULT_NETWORK); //This shouldn't happen
                LogHelper.makeLog("IpLocationService we could not find a locale: " + answer.getLocale() + ", ip: " + ip + ", country: " + answer.getCountryCode());
            }

        } catch (IpLocationException ex) {
            LogHelper.makeLog(ex);
        }


        return answer;

    }

    private LocationDTO findLocation(String ip) throws IpLocationException {


        LocationDTO answer = new LocationDTO();
        String query = "SELECT l.id, l.country_code, l.region_code, l.city, l.zipcode FROM ip_group_city i, locations l "
                + "where i.ip_start <= INET_ATON( ?1 ) and l.id = i.location order by i.ip_start desc limit 1";

        try {

            Query searchQuery = (Query) em.createNativeQuery(query);
            searchQuery.setParameter(1, ip);

            Object searchResult = searchQuery.getSingleResult();

            Object[] ab = (Object[]) searchResult;
            answer.setId((Integer) ab[0]);
            answer.setCountryCode((String) ab[1]);
            answer.setRegionCode((String) ab[2]);
            answer.setCity((String) ab[3]);
            answer.setZipcode((String) ab[4]);


        } catch (Exception ex) {
            throw new IpLocationException(ex);
        }

        return answer;

    }

    private void initializeLocaleMap() {

        String es = "es";
        String en = "en";
        //South America
        countryLocaleHashMap.put("AR", new LocationDTO(es, NetworkConst.COLOMBIA));
        countryLocaleHashMap.put("BO", new LocationDTO(es, NetworkConst.COLOMBIA));
        countryLocaleHashMap.put("CL", new LocationDTO(es, NetworkConst.COLOMBIA));
        countryLocaleHashMap.put("CO", new LocationDTO(es, NetworkConst.COLOMBIA));
        countryLocaleHashMap.put("EC", new LocationDTO(es, NetworkConst.COLOMBIA));
        countryLocaleHashMap.put("PE", new LocationDTO(es, NetworkConst.COLOMBIA));
        countryLocaleHashMap.put("PY", new LocationDTO(es, NetworkConst.COLOMBIA));
        countryLocaleHashMap.put("UY", new LocationDTO(es, NetworkConst.COLOMBIA));
        countryLocaleHashMap.put("VE", new LocationDTO(es, NetworkConst.COLOMBIA));

        //Central America
        countryLocaleHashMap.put("CU", new LocationDTO(es, NetworkConst.COLOMBIA)); //Holy fucking shit! welcome Fidel!!!
        countryLocaleHashMap.put("CR", new LocationDTO(es, NetworkConst.COLOMBIA));
        countryLocaleHashMap.put("DO", new LocationDTO(es, NetworkConst.COLOMBIA));
        countryLocaleHashMap.put("GT", new LocationDTO(es, NetworkConst.COLOMBIA));
        countryLocaleHashMap.put("HN", new LocationDTO(es, NetworkConst.COLOMBIA));
        countryLocaleHashMap.put("PA", new LocationDTO(es, NetworkConst.COLOMBIA));
        countryLocaleHashMap.put("PR", new LocationDTO(es, NetworkConst.COLOMBIA));
        countryLocaleHashMap.put("MX", new LocationDTO(es, NetworkConst.COLOMBIA));
        countryLocaleHashMap.put("NI", new LocationDTO(es, NetworkConst.COLOMBIA));
        countryLocaleHashMap.put("SV", new LocationDTO(es, NetworkConst.COLOMBIA));

        //Europe
        countryLocaleHashMap.put(es, new LocationDTO(es, NetworkConst.COLOMBIA));

        //North America
//        countryLocaleHashMap.put("CA", new LocationDTO(en, NetworkConst.USA));
//        countryLocaleHashMap.put("US", new LocationDTO(en, NetworkConst.USA));


    }
}
