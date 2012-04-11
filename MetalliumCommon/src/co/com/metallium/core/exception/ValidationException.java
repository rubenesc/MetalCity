/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 * 20111115
 * @author Ruben
 */
public class ValidationException extends BaseException {

    public ValidationException() {
    }

    public ValidationException(String mensaje) {
        super(mensaje);
    }

    public ValidationException(Throwable exxx) {
        super(exxx);
    }

    public ValidationException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public ValidationException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa, displayMessage);
    }

}
