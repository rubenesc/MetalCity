/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.utils.htmlscriptvalidator;

import com.metallium.utils.utils.UtilHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 20100201
 * I did this one for the Fucking Visor Project.
 * @author Ruben Escudero
 */
public class ExternalResourceBlocker {

    // regular expression Patterns (HTML TAGS) container
    private List<TagHtmlDTO> htmlTagRegex = initialize();
    private static TagHtmlDTO deleteAllHtmlRegEx = new TagHtmlDTO("<.*?>", "</.*?>", "deleteAllTags", "Any Html Tag");

    private ExternalResourceBlocker() {
    }

    //Singleton Lazy Loader.
    public static ExternalResourceBlocker getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {

        private static final ExternalResourceBlocker instance = new ExternalResourceBlocker();
    }

    public List<TagHtmlDTO> initialize() {
        List<TagHtmlDTO> tagList = new ArrayList<TagHtmlDTO>();

        //Before I used to get an image and replace it with a No Resource Gif. No I just replace is with a "".
        //<img src='/visor/images/no_resources.gif' />
        //listaTags.add(new TagHtmlDTO("<[ ]*(img)[ ]+.*?>", "</[ ]*(img)[ ]*>", "replaceWithBlockImage", ParametrosGenerales.getParametro(Constantes.MAIL_BLOCK_IMAGE), "Tag de Imagen"));
    //    tagList.add(new TagHtmlDTO("<[ ]*(img)[ ]+.*?>", "</[ ]*(img)[ ]*>", "replaceWithBlockImage", "", "Tag de Imagen"));
        tagList.add(new TagHtmlDTO("<[ ]*(script)[ ]+.*?>", "</[ ]*(script)[ ]*>", "deleteTagsAndAnythingWithinThem", "Tag de Javascript"));
//        tagList.add(new TagHtmlDTO("<[ ]*(a)[ ]+.*?>", "</[ ]*(a)[ ]*>", "deleteTags", "Tag de cualquier link"));
//        tagList.add(new TagHtmlDTO("<[ ]*(embed)[ ]+.*?>", "</[ ]*(embed)[ ]*>", "deleteTags", "Tag de un Embeded object como Flash"));

        return tagList;
    }

    public boolean tieneTagsHtmlInvalidos(String mensaje) {
        boolean respuesta = false;

        for (Iterator<TagHtmlDTO> it = htmlTagRegex.iterator(); it.hasNext();) {
            TagHtmlDTO tagHtml = it.next();
            Pattern[] htmlRegexPattern = tagHtml.getHtmlTagRegex();
            for (int i = 0; i < htmlRegexPattern.length; i++) {
                Matcher matcher = htmlRegexPattern[i].matcher(mensaje);
                while (matcher.find()) {
                    //Se encontro un tag html de la lista de tags prohbidos.
                    respuesta = true;
                    break;
                }
            }
        }

        return respuesta;
    }

    public String eliminarTagsHtmlInvalidos(String mensaje) {

        String respuesta = mensaje;

        for (Iterator<TagHtmlDTO> it = htmlTagRegex.iterator(); it.hasNext();) {
            TagHtmlDTO tagHtml = it.next();
            Pattern[] htmlRegexPattern = tagHtml.getHtmlTagRegex();

            String accion = tagHtml.getAccion();

            if (accion == null || accion.trim().equals("") || accion.trim().equalsIgnoreCase("deleteTags")) {
                respuesta = deleteTags(htmlRegexPattern, respuesta);
            } else if (accion.trim().equalsIgnoreCase("replaceWithBlockImage")) {
                respuesta = replaceWithBlockImage(htmlRegexPattern, tagHtml.getReplaceString(), respuesta);
            } else if (accion.trim().equalsIgnoreCase("deleteTagsAndAnythingWithinThem")) {
                respuesta = deleteTagsAndAnythingWithinThem(htmlRegexPattern, respuesta);
            }

        }

        return respuesta;
    }

    private String deleteTags(Pattern[] htmlRegexPattern, String respuesta) {
        for (int i = 0; i < htmlRegexPattern.length; i++) {
            Matcher matcher = htmlRegexPattern[i].matcher(respuesta);
            while (matcher.find()) {
                respuesta = matcher.replaceFirst("");
                matcher = htmlRegexPattern[i].matcher(respuesta);
            }
        }
        return respuesta;
    }

    private String deleteTagsAndAnythingWithinThem(Pattern[] htmlRegexPattern, String respuesta) {

        String tagInicio = null;
        String tagFin = null;
        if (htmlRegexPattern.length > 0) {
            //Primero remplazo todos los tags con la cadena que viene en el parametro "replaceString"
            int i = 0;
            Matcher matcher1 = htmlRegexPattern[i].matcher(respuesta);
            while (matcher1.find()) {
                tagInicio = matcher1.group(0);
            }

            //Ahora solo elimino todos los tags secundarios es decir los que cierran ej: </img> en caso de haber alguno.
            i = 1;
            Matcher matcher2 = htmlRegexPattern[i].matcher(respuesta);
            while (matcher2.find()) {
                tagFin = matcher2.group(0);
            }
        }

        if ((tagInicio != null && !tagInicio.trim().equals(""))
                && (tagFin != null && !tagFin.trim().equals(""))) {
            int inicioTagAbre = respuesta.indexOf(tagInicio);
            int finTagCierra = respuesta.indexOf(tagFin) + tagFin.length();
            String subStringToErrase = respuesta.substring(inicioTagAbre, finTagCierra);
            respuesta = respuesta.replace(subStringToErrase, ""); //Elimina todo lo que haya entre dichos tags.
        }

        return respuesta;
    }

    private String replaceWithBlockImage(Pattern[] htmlRegexPattern, String replaceString, String respuesta) {
        if (replaceString == null) {
            replaceString = "";
        }

        if (htmlRegexPattern.length > 0) {
            //Primero remplazo todos los tags con la cadena que viene en el parametro "replaceString"
            int i = 0;
            Matcher matcher1 = htmlRegexPattern[i].matcher(respuesta);
            respuesta = matcher1.replaceAll(replaceString);
            matcher1 = htmlRegexPattern[i].matcher(respuesta);

            //Ahora solo elimino todos los tags secundarios es decir los que cierran ej: </img> en caso de haber alguno.
            i = 1;
            Matcher matcher2 = htmlRegexPattern[i].matcher(respuesta);
            respuesta = matcher2.replaceAll("");
            matcher2 = htmlRegexPattern[i].matcher(respuesta);
        }

        return respuesta;
    }

    /**
     * Method that given an HTML format deletes all its tags.
     *
     *
     * @param respuesta
     * @return
     */
    public static String deleteAllHtmlTags(String respuesta) {
        if (UtilHelper.isStringEmpty(respuesta)) {
            return "";
        }
        String replaceString = "";
        Pattern[] htmlRegexPattern = deleteAllHtmlRegEx.getHtmlTagRegex();


        if (htmlRegexPattern.length > 0) {
            //Primero remplazo todos los tags con la cadena que viene en el parametro "replaceString"
            int i = 0;
            Matcher matcher1 = htmlRegexPattern[i].matcher(respuesta);
            respuesta = matcher1.replaceAll(replaceString);
            matcher1 = htmlRegexPattern[i].matcher(respuesta);

            //Ahora solo elimino todos los tags secundarios es decir los que cierran ej: </img> en caso de haber alguno.
            i = 1;
            Matcher matcher2 = htmlRegexPattern[i].matcher(respuesta);
            respuesta = matcher2.replaceAll("");
            matcher2 = htmlRegexPattern[i].matcher(respuesta);
        }

        return respuesta.trim();
    }

    public String validateStringFromInvalidTags(String string) {

        if (tieneTagsHtmlInvalidos(string)) {
            string = deleteAllHtmlTags(string);
        }

        return string;
    }
}
