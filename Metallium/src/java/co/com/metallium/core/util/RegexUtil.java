/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.util;

import co.com.metallium.core.constants.UserCommon;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * http://regexpal.com/
 * @author Ruben
 */
public class RegexUtil {

    private static final String COMMON_WEB_FILE_EXT_REGEX = ".*((\\.png)|(\\.jpg)|(\\.gif)|(\\.css)|(\\.js))";
    private static final String NO_FILTER_REGEX = COMMON_WEB_FILE_EXT_REGEX;
    public static final Pattern noWebFilterPattern = Pattern.compile(NO_FILTER_REGEX, Pattern.CASE_INSENSITIVE);
    public static final String PASSWORD_REGEX = "[a-zA-Z0-9\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\-\\+\\{\\}\\.\\_\\=]+";
    private static final Pattern emailPattern = Pattern.compile("^[_A-Za-z0-9-']+([.+_A-Za-z0-9-']+)*@[A-Za-z0-9-_]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,4})$", Pattern.CASE_INSENSITIVE); //This also accepts the following characters + - _
    //Use 4 to 50 characters and start with a letter. You may use letters, numbers, underscores, and one dot (.).  http://stackoverflow.com/questions/168956/yahoo-username-regex
    public static final Pattern userNamePattern = Pattern.compile("(^[A-Za-z](?=[A-Za-z0-9 _.]{1,49}$)[a-zA-Z0-9 _]*.?[a-zA-Z0-9_]*$)", Pattern.CASE_INSENSITIVE);
    //Example: ruben.escudero, only letters, numbers and dots. no spaces, or anything else. Its very simple
    public static final Pattern userNickPattern = Pattern.compile("(^[a-z0-9](?=[a-z0-9.]{1,60}$)[a-z0-9]*.?[a-z0-9]*$)", Pattern.CASE_INSENSITIVE);
    // "/metallium/index.jsf";   Simple but works for me
    public static final Pattern indexPagePattern = Pattern.compile(".*?[/]index[.].*?", Pattern.CASE_INSENSITIVE);
    public static final Pattern profilePagePattern = Pattern.compile(".*?[/]profile[.].*?", Pattern.CASE_INSENSITIVE);
    public static final Pattern editProfilePagePattern = Pattern.compile(".*?[/]editProfile[.].*?", Pattern.CASE_INSENSITIVE);
    // /profile/
    private static final Pattern profileDirectoryPattern = Pattern.compile("[\\\\/]*(" + UserCommon.FILE_SYSTEM_USER_PROFILE + ")[\\\\/]*", Pattern.CASE_INSENSITIVE);
    // galleries///gallery20101130124416F0E9";
    private static final Pattern galleryDirectoryPattern = Pattern.compile("[\\\\/]*(" + UserCommon.FILE_SYSTEM_USER_GALLERIES + ")[\\\\/]+(" + UserCommon.FILE_SYSTEM_USER_GALLERY + ")", Pattern.CASE_INSENSITIVE);
    // <p> sdfdsfdsfs </p>
    private static final Pattern firstHtmlParagraph = Pattern.compile("<p>(.*?)</p>", Pattern.CASE_INSENSITIVE);
    public static final Pattern urlPartsPattern = Pattern.compile(".*?([^.]+\\.[^.]+)"); //http://stackoverflow.com/questions/863297/regular-expression-to-retrieve-domain-tld
    public static final Pattern numberPattern = Pattern.compile("[0-9]*");

    public static void main(String args[]) {
        String x = "<P>Primer Parrago vamos a publicar una noticia sobre algo que         " + "                                      haya pasado en los ultimos dias.sdfsfddsfdssfsfdsdfsf.<br />segundo Renglon<br />tercer Renglon.</p> " + " <p>Segundo Parrafo y comienzasdfsdfsssssssssssssssssssssssssssssssssssssssss</p> " + " <p>&nbsp;</p> " + " <p>Hoy quiero decir que</p> ";
        LogHelper.makeLog("Entrada: " + x);
        x = getFirtHtmlParagraph(x);
        LogHelper.makeLog("Salida: " + x);
    }

    /**
     * Returns anything text within the first Paragraph TAG, of an HTML
     * document.
     *
     * @param mensaje
     * @return
     */
    public static String getFirtHtmlParagraph(String mensaje) {
        String respuesta = mensaje;

        Matcher matcher = firstHtmlParagraph.matcher(mensaje);
        if (matcher.find()) {
            respuesta = matcher.group(0);

        }


        return respuesta;
    }

    public static boolean isDirectoyAProfile(String mensaje) {
        boolean anwser = false;

        Matcher matcher = profileDirectoryPattern.matcher(mensaje);
        if (matcher.find()) {
            anwser = true; // Its a profile !!!
        }

        return anwser;
    }

    public static boolean isDirectoyAGallery(String mensaje) {
        boolean anwser = false;

        Matcher matcher = galleryDirectoryPattern.matcher(mensaje);
        if (matcher.find()) {
            anwser = true; // Its a gallery!!!
        }

        return anwser;
    }

    /**
     * Removes all the back slashes from a sentence and replaces them for one slash.
     *
     *
     * This method gets something like this --> User00\\00\00\01\profile
     * and returns something like this --> User00/00/00/01/profile
     *
     * The idea is because a back slash "\" cant be shown in a URL to display an image.
     *
     */
    public static String removeBackSlashesFromFilePath(String mensaje) {
        return mensaje.replaceAll("\\\\+", "/");
    }



    /**
     * Removes all the double slashes from a sentence and replaces them for one slash.
     *
     *
     * This method gets something like this --> /User00//00////00//01/profile///////
     * and returns something like this --> /User00/00/00/01/profile/
     *
     * The idea is because a double slashes "//" fuck up my URL to display an image.
     *
     */
    public static String removeDoubleSlashesFromFilePath(String mensaje) {
        return mensaje.replaceAll("/+", "/");
    }



    public static boolean isEmailValid(String email) {
        boolean answer = false;

        if (email != null && email.length() >= 3 && email.length() <= 50) {
            if (emailPattern.matcher(email).matches()) {
                answer = true;
            }
        }

        return answer;
    }

    public static boolean isUserNameValid(String name) {

        if (!UtilHelper.isStringEmpty(name)) {
            if (userNamePattern.matcher(name).matches()) {
                return true; //Valid
            }
        }

        return false; //Invalid
    }

    public static boolean isUserNickValid(String name) {

        if (!UtilHelper.isStringEmpty(name)) {
            if (userNickPattern.matcher(name).matches()) {
                return true; //Valid
            }
        }

        return false; //Invalid
    }

    public static boolean isValidNumber(String number) {

        if (!UtilHelper.isStringEmpty(number)) {
            if (numberPattern.matcher(number).matches()) {
                return true; //Valid
            }
        }

        return false; //Invalid
    }

    /***
     *
     * http://www.devdaily.com/blog/post/java/remove-non-alphanumeric-characters-java-string
     *
     */
    public static String removeNonAlphanumericCharacters(String s) {

        if (UtilHelper.isStringNotEmpty(s)) {
            //Here I apply 3 regular extpressions.
            //First I get a string with only alpha numeric characters
            //Second I make sure there is only once space among words
            //Finaly I replace any space with -
            return s.trim().replaceAll("[^a-zA-Z0-9]", " ").replaceAll("\\s{2,}", " ").replaceAll(" ", "-").toLowerCase();
        }

        return "";//If it is empty or null then I just return an emptry string.
    }

    /***
     *      http://stackoverflow.com/questions/863297/regular-expression-to-retrieve-domain-tld
     *
     *  https://foo.com/bar        -->       foo.com
     *  http://www.foo.com#bar"    -->   www.foo.com
     *  http://bar.foo.com         -->   bar.foo.com
     *
     * @return
     */
    public static String getDomainUrl(String url) {
        String answer = null;
        try {
            URI uri = new URI(url);
            //eg: uri.getHost() will return "www.foo.com"
            Matcher m = urlPartsPattern.matcher(uri.getHost());
            if (m.matches()) {
                //Now this is configured however you want it. I want the domain and sub domain so for: http://bar.foo.com
                //answer = m.group(1);  //with 1 it returns -->    foo.com
                answer = m.group(0);    //with 0 it returns --> bar.foo.com
            } else {
                answer = "";
            }
        } catch (Exception ex) {
            //Something went terribly wrong
        }

        return answer;
    }

}
