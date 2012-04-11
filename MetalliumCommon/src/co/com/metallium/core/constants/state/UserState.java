/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.constants.state;

/**
 *
 * @author Ruben
 */
public class UserState extends BaseState{

    public static final String PENDING = "PENDING";
    public static final String BANNED = "BANNED";


    public static boolean isBanned(String state){
       return compareStates(UserState.BANNED, state);
    }

    public static boolean isPending(String state){
       return compareStates(UserState.PENDING, state);
    }


}
