/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.constants.state;

/**
 * 20101128
 * @author Ruben
 */
public class GalleryState {

    public static final String PUBLIC = "PUBLIC";
    public static final String WITH_LINK = "WITH_LINK";
    public static final String PRIVATE = "PRIVATE";


    public static boolean isPublic(String state){
        boolean answer = false;

        if (GalleryState.PUBLIC.equalsIgnoreCase(state)){
            answer = true;
        }
       return answer;
    }

    public static boolean isWithLink(String state){
        boolean answer = false;

        if (GalleryState.WITH_LINK.equalsIgnoreCase(state)){
            answer = true;
        }
       return answer;
    }

    public static boolean isPrivate(String state){
        boolean answer = false;

        if (GalleryState.PRIVATE.equalsIgnoreCase(state)){
            answer = true;
        }
       return answer;
    }



}
