/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 * 20101231
 * @author Ruben
 */
public class MessageException extends BaseException {

    public MessageException() {
    }

    public MessageException(String mensaje) {
        super(mensaje);
    }

    public MessageException(Throwable exxx) {
        super(exxx);
    }

    public MessageException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public MessageException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa, displayMessage);
    }


}
