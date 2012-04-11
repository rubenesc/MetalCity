/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.framework.exception;

/**
 *
 * @author Ruben
 */
public class InvalidDateException extends RuntimeException {

    public InvalidDateException() {
    }

    public InvalidDateException(String mensaje) {
        super(mensaje);
    }

    public InvalidDateException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

}
