/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 * 20110814
 * @author Ruben
 */
public class UserWallException extends BaseException {

    public UserWallException() {
    }

    public UserWallException(String mensaje) {
        super(mensaje);
    }

    public UserWallException(Throwable exxx) {
        super(exxx);
    }

    public UserWallException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public UserWallException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa, displayMessage);
    }

}
