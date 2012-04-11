/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.util.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Ruben Escudero
 */
public class BooleanConverter implements Converter {

    public BooleanConverter() {
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return null;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (value instanceof Boolean) {
            Boolean bandera = (Boolean) value;

            if (bandera) {
                return "Si";
            } else {
                return "No";
            }

        }

        return "";
    }
}
