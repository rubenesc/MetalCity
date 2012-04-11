/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 * 20110127
 * @author Ruben
 */
public class UserIsBannedException extends BaseException {

    public UserIsBannedException() {
    }

    public UserIsBannedException(String mensaje) {
        super(mensaje);
    }

    public UserIsBannedException(Throwable exxx) {
        super(exxx);
    }

    public UserIsBannedException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public UserIsBannedException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa, displayMessage);
    }

}
