/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.framework.exception;

/**
 *
 * @author Ruben
 */
public class InvalidParameterException extends RuntimeException {

    public InvalidParameterException() {
    }

    public InvalidParameterException(String mensaje) {
        super(mensaje);
    }

    public InvalidParameterException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

}
