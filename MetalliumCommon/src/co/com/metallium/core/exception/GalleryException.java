/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 *
 * @author Ruben
 */
public class GalleryException extends BaseException {

    public GalleryException() {
    }

    public GalleryException(String mensaje) {
        super(mensaje);
    }

    public GalleryException(Throwable exxx) {
        super(exxx);
    }

    public GalleryException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public GalleryException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa, displayMessage);
    }

}
