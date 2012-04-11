/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.constants.state;

/**
 * 20110126
 * @author Ruben
 */
public class BanTime {

    public static final String _5_MIN_ = "_5_MIN_";
    public static final String _30_MIN_ = "_30_MIN_";
    public static final String _1_HOUR_ = "_1_HOUR_";
    public static final String _1_DAY_ = "_1_DAY_";
    public static final String _INDEFINITELY_ = "_INDEFINITELY_";
    
    public static boolean isBan5Minutes(String banTime){
       return BaseState.compareStates(BanTime._5_MIN_, banTime);
    }

    public static boolean isBan30Minutes(String banTime){
       return BaseState.compareStates(BanTime._30_MIN_, banTime);
    }

    public static boolean isBan1Hour(String banTime){
       return BaseState.compareStates(BanTime._1_HOUR_, banTime);
    }

    public static boolean isBan1Day(String banTime){
       return BaseState.compareStates(BanTime._1_DAY_, banTime);
    }

    public static boolean isBanIndefinitely(String banTime){
       return BaseState.compareStates(BanTime._INDEFINITELY_, banTime);
    }

}
