/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.exception;

/**
 * 20120114
 * @author Ruben
 */
public class JukeboxException extends BaseException {

    public JukeboxException() {
    }

    public JukeboxException(String mensaje) {
        super(mensaje);
    }

    public JukeboxException(Throwable exxx) {
        super(exxx);
    }

    public JukeboxException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public JukeboxException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa, displayMessage);
    }

}
