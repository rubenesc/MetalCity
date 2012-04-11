/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.view.util.validators;

import co.com.metallium.view.jsf.BaseManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Ruben
 */
public class CorreoElectronicoValidator extends BaseManagedBean implements Validator {

    public static final String CORREO_ELECTRONICO_REGEX = "[a-zA-Z0-9\\.\\_\\-]+@[a-zA-Z0-9\\_\\-]+\\.[a-zA-Z0-9\\.\\_\\-]+";

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String s = String.valueOf(value);
        if (!s.matches(CORREO_ELECTRONICO_REGEX)) {
//            throw new ValidatorException(new FacesMessage(ConstantesBean.CORREO_ELECTRONICO_VALIDATOR));
        }
        if(s.length() > 50){
  //          throw new ValidatorException(new FacesMessage(ConstantesBean.LONGITUD_CADENA_VALIDATOR));
        }
    }
}