/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 * 20110208
 * @author Ruben
 */
public class IpLocationException extends BaseException {

    public IpLocationException() {
    }

    public IpLocationException(String mensaje) {
        super(mensaje);
    }

    public IpLocationException(Throwable exxx) {
        super(exxx);
    }

    public IpLocationException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public IpLocationException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa, displayMessage);
    }


}
