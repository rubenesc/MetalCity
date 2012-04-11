/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 *
 * If for some reason I fail to update the number of friends
 * in the users table when I add or delete a friend, then I throw this.
 *
 * 20110111
 * @author Ruben
 */
public class FriendUpdateNumberException extends BaseException {

    public FriendUpdateNumberException() {
    }

    public FriendUpdateNumberException(String mensaje) {
        super(mensaje);
    }

    public FriendUpdateNumberException(Throwable exxx) {
        super(exxx);
    }

    public FriendUpdateNumberException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public FriendUpdateNumberException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa);
    }
}

