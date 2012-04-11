/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.framework.exception;

/**
 *
 * @author Ruben
 */
public class ParameterRequiredException extends RuntimeException {

    public ParameterRequiredException() {
    }

    public ParameterRequiredException(String mensaje) {
        super(mensaje);
    }

    public ParameterRequiredException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

}
