/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 * 20101103
 * @author Ruben
 */
public class InitializationException extends BaseException {

    public InitializationException() {
    }

    public InitializationException(String mensaje) {
        super(mensaje);
    }

    public InitializationException(Throwable exxx) {
        super(exxx);
    }
    
    public InitializationException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public InitializationException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa, displayMessage);
    }

}

