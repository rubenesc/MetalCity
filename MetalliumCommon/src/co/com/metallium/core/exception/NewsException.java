/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 *
 * @author Ruben
 */
public class NewsException extends BaseException {

    public NewsException() {
    }

    public NewsException(String mensaje) {
        super(mensaje);
    }

    public NewsException(Throwable exxx) {
        super(exxx);
    }

    public NewsException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public NewsException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa, displayMessage);
    }

}
