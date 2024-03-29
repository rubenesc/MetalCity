/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.jukebox;


import co.com.metallium.core.entity.jukebox.JukePlaylist;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public abstract class PlaylistOrderConverter implements Converter {

    
    
    public abstract List<JukePlaylist> getPlaylists();
    
//    public static List<Player> playerDB;
//
//    static {
//        playerDB = new ArrayList<Player>();
//
//        playerDB.add(new Player("Messi", 10, "messi.jpg", "CF"));
//        playerDB.add(new Player("Bojan", 9, "bojan.jpg", "CF"));
//        playerDB.add(new Player("Iniesta", 8, "iniesta.jpg", "CM"));
//        playerDB.add(new Player("Villa", 7, "villa.jpg", "CF"));
//        playerDB.add(new Player("Xavi", 6, "xavi.jpg", "CM"));
//        playerDB.add(new Player("Puyol", 5, "puyol.jpg", "CB"));
//        playerDB.add(new Player("Afellay", 20, "afellay.jpg", "AMC"));
//        playerDB.add(new Player("Abidal", 22, "abidal.jpg", "LB"));
//        playerDB.add(new Player("Alves", 2, "alves.jpg", "RB"));
//        playerDB.add(new Player("Pique", 3, "pique.jpg", "CB"));
//        playerDB.add(new Player("Keita", 15, "keita.jpg", "DM"));
//        playerDB.add(new Player("Adriano", 21, "adriano.jpg", "LB"));
//        playerDB.add(new Player("Valdes", 1, "valdes.jpg", "GK"));
//    }

    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
                int id = Integer.parseInt(submittedValue);

                for (JukePlaylist p : getPlaylists()) {
                    if (p.getId() == id) {
                        return p;
                    }
                }
                
            } catch(NumberFormatException exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));
            }
        }

        return null;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(((JukePlaylist) value).getId());
        }
    }
}