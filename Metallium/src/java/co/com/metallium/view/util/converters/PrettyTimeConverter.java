/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.util.converters;

import co.com.metallium.core.constants.SessionConst;
import co.com.metallium.core.entity.iplocation.LocationDTO;
import co.com.metallium.core.util.StaticApplicationParameters;
import com.metallium.utils.framework.jsf.FacesUtils;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.TimeWatch;
import java.io.Serializable;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.ocpsoft.pretty.time.PrettyTime;
import java.util.Locale;
import javax.faces.convert.FacesConverter;

@FacesConverter("co.metallium.PrettyTimeConverter")
public class PrettyTimeConverter implements Converter, Serializable {

    private static final long serialVersionUID = 7690470362440868259L;

    public Object getAsObject(FacesContext context, UIComponent comp, String value) {
        return null;
        //   throw new ConverterException("Does not yet support converting String to Date");
    }

    public String getAsString(FacesContext context, UIComponent comp, Object value) {

        if (value instanceof Date) {
            try {

                return obtainPrettyTime().format((Date) value); //I get a pretty time instance

            } catch (Exception e) {
                LogHelper.makeLog(e);
            }

        }

        return "---"; //If an error ocurres Im returning this ... if you see this in the application then something bad happend.
    }

    private PrettyTime obtainPrettyTime() {
        //Now the idea of this is that Im only going to create one instance of a pretty time object per locale. 
        //If you dont do this, then you are going to create once instance for every converstion and that is an extremely ineficiente approach.

        PrettyTime prettyTime = null;

        try {
           prettyTime = StaticApplicationParameters.prettyTimeMap.get(((LocationDTO) FacesUtils.getManagedBean(SessionConst.LOCALE)).getLocale());
        } catch (Exception e) {
            LogHelper.makeLog(e);
            //I dont care
        }

        if (prettyTime == null) {
            String locale = ((LocationDTO) FacesUtils.getManagedBean(SessionConst.LOCALE)).getLocale();
            prettyTime = new PrettyTime(new Locale(locale));
            StaticApplicationParameters.prettyTimeMap.put(locale, prettyTime);
            LogHelper.makeLog("A new pretty time instance has been created for the following locale: " + locale);
        }

        return prettyTime;
    }
}
