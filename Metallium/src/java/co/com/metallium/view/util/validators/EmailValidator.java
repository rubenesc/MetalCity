
package co.com.metallium.view.util.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Ruben
 */
public class EmailValidator implements Validator {

    private static final String EMAIL_REGEX = "[a-zA-Z0-9\\.\\_\\-]+@[a-zA-Z0-9\\_\\-]+\\.[a-zA-Z0-9\\.\\_\\-]+";
    private static final String EMAIL_MENSAJE = "El Correo Electrónico no es válido. Ejemplo: nombre@dominio.com";
    private static final String LONGITUD_MENSAJE = "El Campo tiene demasiados caracteres.";

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String s = String.valueOf(value);
        if (!s.matches(EMAIL_REGEX)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, EMAIL_MENSAJE, EMAIL_MENSAJE));
        }
        if(s.length() > 50){
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, LONGITUD_MENSAJE, LONGITUD_MENSAJE));
        }
    }
}
