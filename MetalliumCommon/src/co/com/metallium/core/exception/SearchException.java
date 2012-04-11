/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 *
 * @author Ruben
 */
public class SearchException extends BaseException {

    public SearchException() {
    }

    public SearchException(String mensaje) {
        super(mensaje);
    }

    public SearchException(Throwable exxx) {
        super(exxx);
    }

    public SearchException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public SearchException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa, displayMessage);
    }

}

