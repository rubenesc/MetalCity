/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.view.util;

import co.com.metallium.core.constants.Navegation;
import org.apache.commons.lang3.StringUtils;

/**
 * 20110928
 * @author Ruben
 */
public class JsfViewReturnPage {

    public static final String VIEW_INDEX = "in";
    public static final String VIEW_EVENT_CONTROL_ROOM = "ecr";
    public static final String VIEW_NEWS_CONTROL_ROOM = "ncr";

    public static String getPage(String view) {

        if (!StringUtils.isEmpty(view)) {
            if (VIEW_INDEX.equalsIgnoreCase(view)) {
                return Navegation.goToIndex;
            } else if (VIEW_EVENT_CONTROL_ROOM.equalsIgnoreCase(view)) {
                return Navegation.goToEventControlRoom;
            } else if (VIEW_NEWS_CONTROL_ROOM.equalsIgnoreCase(view)) {
                return Navegation.goToNewsControlRoom;
            }
        }
       return Navegation.goToIndex;
    }



}
