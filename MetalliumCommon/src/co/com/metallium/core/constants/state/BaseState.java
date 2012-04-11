/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.constants.state;

/**
 * 20101121
 * @author Ruben
 */
public class BaseState {

    public static final String ACTIVE = "ACTIVE"; 
    public static final String INACTIVE = "INACTIVE"; //Because user compareStates not active or deleted
    public static final String DELETED = "DELETED"; //User Deleted his message


    public static boolean isActive(String state){
       return compareStates(BaseState.ACTIVE, state);
    }

    public static boolean isInactive(String state){
       return compareStates(BaseState.INACTIVE, state);
    }

    public static boolean isDeleted(String state){
       return compareStates(BaseState.DELETED, state);
    }

    public static boolean compareStates(String state1, String state2){
        boolean answer = false;
        if (state1.equalsIgnoreCase(state2)){
            answer = true;
        }

       return answer;
    }


}
