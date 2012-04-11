/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 *
 * @author Ruben
 */
public class CommonException extends BaseException {

    public CommonException() {
    }

    public CommonException(String mensaje) {
        super(mensaje);
    }

    public CommonException(Throwable exxx) {
        super(exxx);
    }

    public CommonException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public CommonException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa, displayMessage);
    }


}
