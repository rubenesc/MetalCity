/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.constants;

/**
 *
 * @author Ruben
 */
public class MessageConst { 

    public static final int unread = 0;
    public static final int read = 1;

    public static final int delete_no = 0;
    public static final int delete_yes = 1;

    public static String inbox = "inbox";
    public static String sent = "sent";
    public static String newmessage = "newmessage";

    public static boolean isCanI(String box, String option) {
        boolean answer = false;
        if (box != null && box.equalsIgnoreCase(option)) {
            answer = true;
        }
        return answer;
    }

}
