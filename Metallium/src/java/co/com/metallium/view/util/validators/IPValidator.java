
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
public class IPValidator implements Validator {
    private static final String OCTET = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    private static final String IP_REGEX = OCTET+"(\\."+OCTET+"){3}";
    public static final String IP_MENSAJE = "La Dirección IP no es válida. Ejemplo: 192.168.100.1";

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String s = String.valueOf(value);
        if (!isIPValida(s)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, IP_MENSAJE, IP_MENSAJE));
        }
    }

    public static boolean isIPValida(String ip){
        return !(ip.length() > 15 || !ip.matches(IP_REGEX));
    }
}
