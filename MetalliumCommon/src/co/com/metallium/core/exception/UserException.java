/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 *
 * @author Ruben
 */
public class UserException extends BaseException {

    public UserException() {
    }

    public UserException(String mensaje) {
        super(mensaje);
    }

    public UserException(Throwable exxx) {
        super(exxx);
    }

    public UserException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public UserException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa, displayMessage);
    }

}
