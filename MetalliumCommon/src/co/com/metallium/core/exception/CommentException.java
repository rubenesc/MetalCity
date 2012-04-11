/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 * 20101123
 * @author Ruben
 */
public class CommentException extends BaseException {

    public CommentException() {
    }

    public CommentException(String mensaje) {
        super(mensaje);
    }

    public CommentException(Throwable exxx) {
        super(exxx);
    }

    public CommentException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public CommentException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa, displayMessage);
    }

}
