/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 * 20110101
 * @author Ruben
 */
public class CommentCalmDownException extends BaseException {

    public CommentCalmDownException() {
    }

    public CommentCalmDownException(String mensaje) {
        super(mensaje);
    }

    public CommentCalmDownException(Throwable exxx) {
        super(exxx);
    }

    public CommentCalmDownException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public CommentCalmDownException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa);
    }
}


