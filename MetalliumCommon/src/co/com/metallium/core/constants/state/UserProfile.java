/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.constants.state;

/**
 *
 * @author Ruben
 */
public class UserProfile {

    public final static Integer USER = 1;
    public final static Integer ADMINISTRATOR_1 = 100;
    public final static Integer ADMINISTRATOR_2 = 200;
    public final static Integer ADMINISTRATOR_3 = 300;
    public final static Integer ROOT = 1000;


    public static boolean isUser(Integer id){
        boolean answer = false;
        if (id.compareTo(UserProfile.USER) >= 0 ){
            answer = true;
        }
       return answer;
    }

    public static boolean isAdministrator1(Integer id){
        boolean answer = false;
        if (id.compareTo(UserProfile.ADMINISTRATOR_1) >= 0 ){
            answer = true;
        }
       return answer;
    }

    public static boolean isAdministrator2(Integer id){
        boolean answer = false;
        if (id.compareTo(UserProfile.ADMINISTRATOR_2) >= 0 ){
            answer = true;
        }
       return answer;
    }

    public static boolean isAdministrator3(Integer id){
        boolean answer = false;
        if (id.compareTo(UserProfile.ADMINISTRATOR_3) >= 0 ){
            answer = true;
        }
       return answer;
    }


    public static boolean isRoot(Integer id){
        boolean answer = false;
        if (id.compareTo(UserProfile.ROOT) == 0 ){
            answer = true;
        }
       return answer;
    }

}
