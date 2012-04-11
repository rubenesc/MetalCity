/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.view.util.validators;

import java.text.MessageFormat;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Ruben
 */
public class PasswordValidator implements Validator {

    private int minSize = 6;
    private int maxSize = 20;

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String msg = null;
        String s = String.valueOf(value);
        if( s.length() < minSize || s.length() > maxSize ){
        //    msg = MessageFormat.format(ConstantesBean.TAMANHO_PASSWORD_VALIDATOR, minSize, maxSize);
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

}