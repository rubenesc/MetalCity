/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.util;

import com.metallium.utils.dto.RequestInfoDTO;
import com.ocpsoft.pretty.time.PrettyTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * The difference between this class and the 'ApplicationParameters' 
 * is that is is a POJO and will have any relation with the Data Base.
 * 
 * 20110609 
 * @author Ruben
 */
public class StaticApplicationParameters {

    //This map is going to contain all the instances of the PrettyTime each for a different Locale
    //the idea is to create only one instance per locale and just once for the entire life cycle of the application.
    public static HashMap<String, PrettyTime> prettyTimeMap = new HashMap<String, PrettyTime>();
    public static HashMap<String, RequestInfoDTO> clientIpMap = new HashMap<String, RequestInfoDTO>();
    private static Integer activeWebSessions = 0;

    public StaticApplicationParameters() {
    }

    //---------start Active Web Sessions-------------------------------------------//
    public static Integer getActiveWebSessions() {
        return activeWebSessions;
    }

    public static void createWebSession() {
        StaticApplicationParameters.activeWebSessions = StaticApplicationParameters.activeWebSessions + 1;
    }

    public static void destroyWebSession() {
        StaticApplicationParameters.activeWebSessions = StaticApplicationParameters.activeWebSessions - 1;
    }

    //---------end Active Web Sessions-------------------------------------------//
    public static HashMap<String, RequestInfoDTO> getClientIpMap() {
        return clientIpMap;
    }

    public static List<RequestInfoDTO> getClientIpList() {
        List<RequestInfoDTO> list = new ArrayList<RequestInfoDTO>(clientIpMap.values());


        Collections.sort(list, new Comparator(){

            public int compare(Object o1, Object o2) {
                RequestInfoDTO p1 = (RequestInfoDTO) o1;
                RequestInfoDTO p2 = (RequestInfoDTO) o2;
               return p2.getCount().compareTo(p1.getCount()) ;
            }

        });


        return list;
    }

    public static void clientIpAdd(String ip, Map<String, String> headers) {
        RequestInfoDTO pairDTO = clientIpMap.get(ip);

        if (pairDTO == null) {
            
            String userAgent = headers.get("user-agent");
            String referer = headers.get("referer");
            String xForwardedFor = headers.get("x-forwarded-for");
            String xForwardedHost = headers.get("x-forwarded-host");
            String xForwardedServer = headers.get("x-forwarded-server");

            clientIpMap.put(ip, new RequestInfoDTO(1, ip, referer, userAgent, xForwardedFor, xForwardedHost, xForwardedServer, headers));
        } else {

            pairDTO.setCount(pairDTO.getCount() + 1);

            clientIpMap.put(ip, pairDTO);
        }

    }
}
