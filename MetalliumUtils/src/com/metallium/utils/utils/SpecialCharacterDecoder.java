/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.utils;

import java.util.StringTokenizer;

public class SpecialCharacterDecoder {

    public static void main(String args[]) {
        String x = "Gonz\u0026aacute;lez";
         x = "Itagüi";
        System.out.println("--> " + x);
        x = entityToChar(x);
        System.out.println("--> " + x);
        x = htmlToChar(x);
        System.out.println("--> " + x);
        x = entityToHtml(x);
        System.out.println("--> " + x);
    }
    public final static String htmlEntities[] = {"&euro;", "", "&lsquor;",
        "&fnof;", "&ldquor;", "&hellip;", "&dagger;", "&Dagger;", "&#710;",
        "&permil;", "&Scaron;", "&lsaquo;", "&OElig;", "", "&#381;", "",
        "", "&lsquo;", "&rsquo;", "&ldquo;", "&rdquo;", "&bull;",
        "&ndash;", "&mdash;", "&tilde;", "&trade;", "&scaron;", "&rsaquo;",
        "&oelig;", "", "&#382;", "&Yuml;", "&nbsp;", "&iexcl;", "&cent;",
        "&pound;", "&curren;", "&yen;", "&brvbar;", "&sect;", "&uml;",
        "&copy;", "&ordf;", "&laquo;", "&not;", "&shy;", "&reg;", "&macr;",
        "&deg;", "&plusmn;", "&sup2;", "&sup3;", "&acute;", "&micro;",
        "&para;", "&middot;", "&cedil;", "&sup1;", "&ordm;", "&raquo;",
        "&frac14;", "&frac12;", "&frac34;", "&iquest;", "&Agrave;",
        "&Aacute;", "&Acirc;", "&Atilde;", "&Auml;", "&Aring;", "&AElig;",
        "&Ccedil;", "&Egrave;", "&Eacute;", "&Ecirc;", "&Euml;",
        "&Igrave;", "&Iacute;", "&Icirc;", "&Iuml;", "&ETH;", "&Ntilde;",
        "&Ograve;", "&Oacute;", "&Ocirc;", "&Otilde;", "&Ouml;", "&times;",
        "&Oslash;", "&Ugrave;", "&Uacute;", "&Ucirc;", "&Uuml;",
        "&Yacute;", "&THORN;", "&szlig;", "&agrave;", "&aacute;",
        "&acirc;", "&atilde;", "&auml;", "&aring;", "&aelig;", "&ccedil;",
        "&egrave;", "&eacute;", "&ecirc;", "&euml;", "&igrave;",
        "&iacute;", "&icirc;", "&iuml;", "&eth;", "&ntilde;", "&ograve;",
        "&oacute;", "&ocirc;", "&otilde;", "&ouml;", "&divide;",
        "&oslash;", "&ugrave;", "&uacute;", "&ucirc;", "&uuml;",
        "&yacute;", "&thorn;", "&yuml;"};

    public final static String entities[] = {"F6", "E4", "FC", "D6", "C4",
        "DC", "DF", "3F", "5C", "2C", "3A", "3B", "23", "2B", "7E", "21",
        "22", "A7", "24", "25", "26", "28", "29", "3D", "3C", "3E", "7B",
        "5B", "5D", "7D", "2F", "E2", "EA", "EE", "F4", "FB", "C2", "CA",
        "CE", "D4", "DB", "E1", "E9", "ED", "F3", "FA", "C1", "C9", "CD",
        "D3", "DA", "E0", "E8", "EC", "F2", "F9", "C1", "C9", "CD", "D3",
        "DA", "B0", "B3", "B2", "80", "7C", "5E", "60", "B4", "27", "20",
        "40", "98", "2A"};
    public final static String charsHtml[] = {"ö", "ä", "ü", "Ö", "Ä", "Ü",
        "ß", "?", "\\", ",", ":", ";", "#", "+", "&tilde;", "!", "\"",
        "&sect;", "$", "%", "&amp;", "(", ")", "=", "&lt;", "&gt;", "{",
        "[", "]", "}", "/", "&acirc;", "&ecirc;", "&icirc;", "&ocirc;",
        "&ucirc;", "&Acirc;", "&Ecirc;", "&Icirc;", "&Ocirc;", "&Ucirc;",
        "&aacute;", "&eacute;", "&iacute;", "&oacute;", "&uacute;",
        "&Aacute;", "&Eacute;", "&Iacute;", "&Oacute;", "&Uacute;",
        "&agrave;", "&egrave;", "&igrave;", "&ograve;", "&Ugrave;",
        "&Agrave;", "&Egrave;", "&Igrave;", "&Ograve;", "&Ugrave;",
        "&deg;", "&sup3;", "&sup2;", "&euro;", "|", "&circ;", "`",
        "&acute;", "'", " ", "@", "~", "*", "&uuml;", "&Uuml;"};
    public final static String chars[] = {"ö", "ä", "ü", "Ö", "Ä", "Ü", "ß",
        "?", "\\", ",", ":", ";", "#", "+", "~", "!", "\"", "§", "$", "%",
        "&", "(", ")", "=", "<", ">", "{", "[", "]", "}", "/", "â", "ê",
        "î", "ô", "û", "Â", "Ê", "Î", "Ô", "Û", "á", "é", "í", "ó", "ú",
        "Á", "É", "Í", "Ó", "Ú", "à", "è", "ì", "ò", "ù", "Á", "É", "Í",
        "Ó", "Ú", "°", "³", "²", "€", "|", "^", "`", "´", "'", " ", "@",
        "~", "*", "ü", "Ü"};

    public static String entityToChar(String raw) {
        return (entityTo(raw, chars));
    }

    public static String entityToHtml(String raw) {
        return (charToHtml(entityTo(raw, chars)));
    }

    public static String htmlToChar(String raw) {
        return convert(raw, charsHtml, chars);
    }

    public static String charToHtml(String raw) {
        if (raw == null) {
            return null;
        }
        char[] chars = raw.toCharArray();
        StringBuilder encoded = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '<') {
                encoded.append("&lt;");
            } else if (c == '>') {
                encoded.append("&gt;");
            } else if (c < 128) {
                encoded.append(c);
            } else if (c < 256) {
                encoded.append(htmlEntities[c - 128]);
            } else {
                encoded.append("&#");
                encoded.append((int) c);
                encoded.append(";");
            }
        }
        return encoded.toString();
    }

    public static String entityTo(String raw, String[] tc) {
        StringBuilder sb = new StringBuilder();
        boolean entity = false;
        raw = raw.replace('+', ' ');
        String tokens = tc == charsHtml ? "%<>" : "%";
        for (StringTokenizer st = new StringTokenizer(raw, tokens, true); st.hasMoreTokens();) {
            String token = st.nextToken();
            if (entity) {
                boolean replaced = false;
                for (int i = 0; i < entities.length; i++) {
                    if (token.startsWith(entities[i])) {
                        sb.append(tc[i]);
                        sb.append(token.substring(2));
                        replaced = true;
                        break;
                    }
                }
                if (!replaced) {
                    sb.append(token);
                }

                entity = false;
            } else if (token.equals("%")) {
                entity = true;
                continue;
            } else if (token.equals("<")) {
                sb.append("&lt;");
            } else if (token.equals(">")) {
                sb.append("&gt;");
            } else {
                sb.append(token);
            }
        }
        return (sb.toString());
    }

    public static String convert(String raw, String[] from, String[] to) {
        String result = raw;
        for (int i = 0; i < from.length; i++) {
            int idx = result.indexOf(from[i]);
            if (idx < 0) {
                continue;
            }
            StringBuilder sb = new StringBuilder();
            while (idx > -1) {
                sb.append(result.substring(0, idx));
                sb.append(to[i]);
                result = result.substring(idx + from[i].length());
                idx = result.indexOf(from[i]);
            }
            sb.append(result);
            result = sb.toString();
        }
        return result;
    }
}
