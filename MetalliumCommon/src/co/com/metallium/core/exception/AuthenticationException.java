/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.exception;

/**
 *
 * @author Ruben
 */
public class AuthenticationException extends BaseException {

    private Integer userId; //Id of the User that failed the authentication.
    
    public AuthenticationException() {
    }

    public AuthenticationException(String mensaje) {
        super(mensaje);
    }

    public AuthenticationException(Throwable exxx) {
        super(exxx);
    }

    public AuthenticationException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public AuthenticationException(String mensaje, Throwable causa, String displayMessage) {
        super(mensaje, causa, displayMessage);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
}

