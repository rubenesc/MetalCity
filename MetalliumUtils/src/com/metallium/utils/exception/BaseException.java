/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.exception;

import java.io.Serializable;

/**
 *
 * @author Ruben
 */
public class BaseException extends Exception implements Serializable {

    private String displayMessage;


    public BaseException() {
    }

    public BaseException(String mensaje) {
        super(mensaje);
    }

    public BaseException(Throwable exxx) {
        super(exxx);
    }

    public BaseException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public BaseException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa);
        this.setDisplayMessage(displayMessage);
    }


    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }
}
