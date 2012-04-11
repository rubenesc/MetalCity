/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 * 20110526
 * @author Ruben
 */
public class ForumException extends BaseException {

    public ForumException() {
    }

    public ForumException(String mensaje) {
        super(mensaje);
    }

    public ForumException(Throwable exxx) {
        super(exxx);
    }

    public ForumException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public ForumException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa, displayMessage);
    }

}
