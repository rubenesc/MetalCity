/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 * 20110514
 * @author Ruben
 */
public class EmailException extends BaseException {

    public EmailException() {
    }

    public EmailException(String mensaje) {
        super(mensaje);
    }

    public EmailException(Throwable exxx) {
        super(exxx);
    }

    public EmailException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public EmailException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa);
    }
}
