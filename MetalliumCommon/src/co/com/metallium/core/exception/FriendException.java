/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 * 20110109
 * @author Ruben
 */
public class FriendException extends BaseException {

    public FriendException() {
    }

    public FriendException(String mensaje) {
        super(mensaje);
    }

    public FriendException(Throwable exxx) {
        super(exxx);
    }

    public FriendException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public FriendException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa);
    }
}
