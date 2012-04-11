/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.constants.state;

import co.com.metallium.core.constants.state.BaseState;

/**
 *
 * @author Ruben
 */
public class NewsState extends BaseState {

    public static final String PENDING = "PENDING";

    public static boolean isPending(String state){
       return compareStates(NewsState.PENDING, state);
    }


}
