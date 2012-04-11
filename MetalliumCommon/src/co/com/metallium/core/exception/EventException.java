/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 * 20110430
 * @author Ruben
 */
public class EventException extends BaseException {

    public EventException() {
    }

    public EventException(String mensaje) {
        super(mensaje);
    }

    public EventException(Throwable exxx) {
        super(exxx);
    }

    public EventException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public EventException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa, displayMessage);
    }

}
