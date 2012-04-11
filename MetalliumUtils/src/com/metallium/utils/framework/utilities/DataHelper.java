/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.framework.utilities;

import com.metallium.utils.framework.exception.InvalidParameterException;
import com.metallium.utils.framework.exception.ParameterRequiredException;
import com.metallium.utils.utils.htmlscriptvalidator.HtmlSanitizer;
import java.io.*;
import java.util.*;
import javax.faces.model.SelectItem;

/**
 *
 * @author Ruben
 */
public class DataHelper {

    public DataHelper() {
    }

    /**
     * Return the first 500 Characters of an HTML document followed by a ...
     * This is just used to get the preview of the input String
     *
     * @param message
     * @return
     */
    public static String getNewsPreviewString(String message) {
        return obtainPreviewString(message, 500);
    }

    /**
     * Same as above.
     * 
     * @param message
     * @return 
     */
    public static String getMessagePreviewString(String message) {
        return obtainPreviewString(message, 40);
    }

    public static String getMediaTitleString(String message) {
        return obtainPreviewString(message, 80);
    }

    public static String getMediaDescriptionString(String message) {
        return obtainPreviewString(message, 295);
    }

    public static String getNonNullString(String s) {
        if (s == null) {
            return "";
        } else {
            return s;
        }
    }

    /**
     * Just get text preview of a String, no HTML elements.
     * 
     * @param message
     * @param length
     * @return 
     */
    private static String obtainPreviewString(String message, int length) {
        String answer = HtmlSanitizer.deleteAllHtmlTags(message);

        if (answer.length() > length) {
            answer = answer.substring(0, length - 1).concat(" ... ");
        }
        return answer;
    }

    public static double convertirANumero(String cadena)
            throws NumberFormatException {
        try {
            if (cadena == null || cadena.indexOf(' ') != -1) {
                throw new NumberFormatException((new StringBuilder()).append("N\uFFFDmero inv\uFFFDlido-> '").append(cadena).append("'").toString());
            } else {
                return (new Double(cadena)).doubleValue();
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException((new StringBuilder()).append("N\uFFFDmero inv\uFFFDlido-> '").append(cadena).append("'").toString());
        }
    }

    public static String truncateString(String value, int length) {
        String anwser = null;
        if (length < 0) {
            anwser = "";
        } else if (value != null) {
            if (value.length() > length) {
                anwser = value.substring(0, length);
            } else {
                anwser = value;
            }
        }
        return anwser;
    }

    public static String truncarCadenaDesdeLaDerecha(String cadena, int longitud) {
        String respuesta = null;
        if (longitud < 0) {
            respuesta = "";
        } else if (cadena != null) {
            if (cadena.length() > longitud) {
                respuesta = cadena.substring(cadena.length() - longitud, cadena.length());
            } else {
                respuesta = cadena;
            }
        }
        return respuesta;
    }

    public static String validarCadenaHTML(String cadena) {
        String sTemp = reemplazarCadena(cadena, "\r\n", "<BR/>");
        return reemplazarCadena(sTemp, "\n", "<BR/>");
    }

    public static String validarCadenaSQL(String cadena) {
        return reemplazarCadena(cadena, "'", "''");
    }

    public static String reemplazarCadenaIgnoreCase(String cadena, String fuente, String reemplazo) {
        return reemplazarCadena(cadena, fuente, reemplazo, -1);
    }

    public static String reemplazarCadenaIgnoreCase(String cadena, String fuente, String reemplazo, int numeroDeVeces) {
        String respuesta = null;
        int indice = -1;
        if (cadena == null || fuente == null || reemplazo == null || (indice = cadena.toLowerCase().indexOf(fuente)) == -1) {
            respuesta = cadena;
        } else {
            StringBuffer sb = new StringBuffer(cadena);
            for (int numeroActual = 0; indice != -1 && (numeroActual++ < numeroDeVeces || numeroDeVeces == -1); indice = sb.toString().toLowerCase().indexOf(fuente, indice + reemplazo.length())) {
                sb.delete(indice, indice + fuente.length());
                sb.insert(indice, reemplazo);
            }

            respuesta = sb.toString();
            sb = null;
        }
        return respuesta;
    }

    public static String reemplazarCadena(String cadena, String fuente, String reemplazo) {
        return reemplazarCadena(cadena, fuente, reemplazo, -1);
    }

    public static String reemplazarCadena(String cadena, String fuente, String reemplazo, int numeroDeVeces) {
        String respuesta = null;
        int indice = -1;
        if (cadena == null || fuente == null || reemplazo == null || (indice = cadena.indexOf(fuente)) == -1) {
            respuesta = cadena;
        } else {
            StringBuffer sb = new StringBuffer(cadena);
            for (int numeroActual = 0; indice != -1 && (numeroActual++ < numeroDeVeces || numeroDeVeces == -1); indice = sb.toString().indexOf(fuente, indice + reemplazo.length())) {
                sb.delete(indice, indice + fuente.length());
                sb.insert(indice, reemplazo);
            }

            respuesta = sb.toString();
            sb = null;
        }
        return respuesta;
    }

    public static String obtenerNombreArchivo(String path) {
        String respuesta = null;
        if (path != null) {
            for (StringTokenizer st = new StringTokenizer(path, "/"); st.hasMoreTokens();) {
                respuesta = st.nextToken();
            }

            Object obj = null;
        }
        return respuesta;
    }

    public static String obtenerNombreCarpeta(String path) {
        String respuesta = null;
        if (path != null) {
            int index = path.lastIndexOf(File.separatorChar);
            if (index != -1) {
                respuesta = path.substring(0, index + 1);
            }
        }
        return respuesta;
    }

    public static String obtenerRutaWeb(String path) {
        String respuesta = null;
        if (path != null) {
            int index = path.lastIndexOf("/");
            if (index != -1) {
                respuesta = path.substring(0, index + 1);
            }
        }
        return respuesta;
    }

    public static String eliminarValorNulo(String valor, String valorPorDefecto) {
        String respuesta = null;
        if (valor == null) {
            respuesta = valorPorDefecto;
        } else {
            respuesta = valor;
        }
        return respuesta;
    }

    public static String eliminarValorVacio(String valor, String valorPorDefecto) {
        String respuesta = null;
        if (valor == null || valor.equals("")) {
            respuesta = valorPorDefecto;
        } else {
            respuesta = valor;
        }
        return respuesta;
    }

    public static int convertirANumero(boolean b) {
        return !b ? 0 : 1;
    }

    public static boolean convertirABoolean(String s) {


        boolean respuesta;
        if (s == null) {
            respuesta = false;
        } else {
            if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("true")) {
                respuesta = true;
            } else {
                respuesta = false;
            }
        }

        return respuesta;
    }

    public static boolean convertirABoolean(int i) {
        return i != 0;
    }

    public static String truncarYColocarCaracteresALaIzquierda(char caracter, int longitud, String cadena, int longitudTruncado) {
        cadena = truncateString(cadena, longitudTruncado);
        return colocarCaracteresALaIzquierda(caracter, longitud, cadena);
    }

    public static String eliminarCaracteresInicioFinCadena(String cadena, char caracter, boolean izquierdaADerecha) {
        StringBuilder sb = new StringBuilder(cadena);
        if (!izquierdaADerecha) {
            sb = sb.reverse();
        }
        for (; sb.length() > 0 && sb.charAt(0) == caracter; sb.deleteCharAt(0));
        if (!izquierdaADerecha) {
            sb = sb.reverse();
        }
        return sb.toString();
    }

    public static String colocarCaracteresALaIzquierda(char caracter, int longitud, String cadena) {
        String respuesta = "";
        if (cadena != null) {
            if (cadena.length() == longitud) {
                respuesta = cadena;
            } else {
                for (respuesta = cadena; respuesta.length() < longitud; respuesta = (new StringBuilder()).append(caracter).append(respuesta).toString());
            }
        } else {
            respuesta = null;
        }
        return respuesta;
    }

    public static String colocarCaracteresALaDerecha(char caracter, int longitud, String cadena) {
        String respuesta = "";
        if (cadena != null) {
            if (cadena.length() == longitud) {
                respuesta = cadena;
            } else {
                for (respuesta = cadena; respuesta.length() < longitud; respuesta = (new StringBuilder()).append(respuesta).append(caracter).toString());
            }
        } else {
            respuesta = null;
        }
        return respuesta;
    }

    public static final String obtenerNombreTipoDato(int tipoDato) {
        switch (tipoDato) {
            case 3: // '\003'
                return "Num\uFFFDrico entero (INTEGER)";

            case 6: // '\006'
                return "Num\uFFFDrico decimal (DOUBLE)";

            case 2: // '\002'
                return "Num\uFFFDrico entero (SHORT)";

            case 1: // '\001'
                return "Num\uFFFDrico entero (BYTE)";

            case 5: // '\005'
                return "Num\uFFFDrico decimal (FLOAT)";

            case 4: // '\004'
                return "Num\uFFFDrico entero (LONG)";

            case 7: // '\007'
                return "Texto (STRING)";

            case 8: // '\b'
                return "Fecha (UTIL_DATE)";
        }
        return "DESCONOCIDO";
    }

    public static boolean isNull(Object data) {
        return data == null;
    }

    public static boolean isNotNull(Object data) {
        return !(data == null);
    }

    public static boolean esVacio(Object dato) {
        boolean retorno = true;
        if (dato != null) {
            retorno = dato.equals("");
        }
        return retorno;
    }

    public static boolean esNumerico(Object dato) {
        boolean retorno = false;
        if (dato != null) {
            retorno = dato instanceof Number;
            if (!retorno) {
                retorno = dato instanceof String;
                if (retorno) {
                    try {
                        convertirANumero((String) dato);
                        retorno = true;
                    } catch (NumberFormatException e) {
                        retorno = false;
                    }
                }
            }
        }
        return retorno;
    }

    public static boolean esTipoNumerico(int tipoDato) {
        return tipoDato == 3 || tipoDato == 4 || tipoDato == 6 || tipoDato == 2 || tipoDato == 1 || tipoDato == 5;
    }

    public static boolean esTipoFecha(int tipoDato) {
        return tipoDato == 8;
    }

    public static String convertirNombreJavaAFormatoUsuario(String nombre) {
        validarStringNulo("nombre", nombre);
        char cTemp = nombre.charAt(0);
        StringBuffer respuesta = new StringBuffer(nombre.length());
        respuesta.append(Character.toUpperCase(cTemp));
        int longitud = nombre.length();
        for (int i = 1; i < longitud; i++) {
            cTemp = nombre.charAt(i);
            if (Character.isUpperCase(cTemp)) {
                respuesta.append(" de ");
            }
            respuesta.append(cTemp);
        }

        return respuesta.toString();
    }

    public static String convertirNombreTablaBDAFormatoJava(String nombre) {
        validarStringNulo("nombre", nombre);
        String respuesta = nombre.toLowerCase();
        int index = respuesta.indexOf('_');
        char cTemp = ' ';
        for (; index != -1; index = respuesta.indexOf('_')) {
            cTemp = respuesta.charAt(index + 1);
            respuesta = reemplazarCadena(respuesta, (new StringBuilder()).append("_").append(cTemp).toString(), (new StringBuilder()).append("").append(Character.toUpperCase(cTemp)).toString(), 1);
        }

        return respuesta;
    }

    public static String convertirNombreJavaAFormatoTabla(String nombre) {
        validarStringNulo("nombre", nombre);
        StringBuffer respuesta = new StringBuffer(nombre.toUpperCase());
        int insertados = 0;
        for (int i = 0; i < nombre.length(); i++) {
            if (Character.isUpperCase(nombre.charAt(i))) {
                respuesta.insert(i + insertados, "_");
                insertados++;
            }
        }

        return respuesta.toString();
    }

    public static void validarStringNulo(String nombre, String valor)
            throws ParameterRequiredException {
        validarObjetoNulo(nombre, valor);
    }

    public static void validarObjetoNulo(String nombre, Object valor)
            throws ParameterRequiredException {
        if (valor == null) {
            throw new ParameterRequiredException((new StringBuilder()).append("Par\uFFFDmetro requerido ").append(nombre).append(" no suministrado").toString());
        } else {
            return;
        }
    }

    public static void validarStringVacio(String nombre, String valor)
            throws InvalidParameterException {
        validarStringNulo(nombre, valor);
        if (valor.trim().equals("")) {
            throw new InvalidParameterException((new StringBuilder()).append("Par\uFFFDmetro requerido ").append(nombre).append(" no puede ser vac\uFFFDo").toString());
        } else {
            return;
        }
    }

    public static void validarCollectionVacio(String nombre, Collection coleccion)
            throws InvalidParameterException {
        validarObjetoNulo(nombre, coleccion);
        if (coleccion.size() <= 0) {
            throw new InvalidParameterException((new StringBuilder()).append("Par\uFFFDmetro requerido ").append(nombre).append(" no puede ser vac\uFFFDo").toString());
        } else {
            return;
        }
    }

    public static String dividirFraseMenu(String frase, int numeroDeCaracteresPorLinea) {
        String respuesta = null;
        if (frase == null) {
            respuesta = frase;
        } else if (numeroDeCaracteresPorLinea < 2 || frase.length() == 0) {
            respuesta = (new StringBuilder()).append("'").append(frase).append("'").toString();
        } else {
            StringTokenizer str = new StringTokenizer(frase, " ");
            StringBuffer linea = new StringBuffer(numeroDeCaracteresPorLinea + 1);
            StringBuffer cadenaMenu = new StringBuffer(numeroDeCaracteresPorLinea + 10);
            for (String subcadena = null; str.hasMoreTokens(); subcadena = null) {
                subcadena = str.nextToken();
                if (linea.toString().length() + subcadena.length() < numeroDeCaracteresPorLinea) {
                    linea.append((new StringBuilder()).append(" ").append(subcadena).toString());
                } else {
                    cadenaMenu.append((new StringBuilder()).append("'").append(linea.toString().trim()).append("' + ").toString());
                    linea = new StringBuffer(subcadena);
                }
            }

            if (linea.toString().length() > 0) {
                cadenaMenu.append((new StringBuilder()).append("'").append(linea.toString().trim()).append("'").toString());
            }
            respuesta = cadenaMenu.toString();
            str = null;
            linea = null;
            cadenaMenu = null;
            Object obj = null;
        }
        return respuesta;
    }

    public static String generarLlaveUnicaString() {
        RandomHelper a = new RandomHelper();
        return a.getUnformatedUUID();
    }

    public static String[] partirArregloPorToken(String str, String token) {
        StringTokenizer strtok = new StringTokenizer(str, token);
        String respuesta[] = new String[strtok.countTokens()];
        for (int i = 0; strtok.hasMoreElements(); i++) {
            respuesta[i] = (String) strtok.nextElement();
        }

        return respuesta;
    }

    public static boolean esTipoDeDatoValido(int tipoDeDato) {
        boolean retorno = false;
        if (tipoDeDato == 3 || tipoDeDato == 2 || tipoDeDato == 9 || tipoDeDato == 1 || tipoDeDato == 6 || tipoDeDato == 5 || tipoDeDato == 4 || tipoDeDato == 7 || tipoDeDato == 10 || tipoDeDato == 11 || tipoDeDato == 8) {
            retorno = true;
        }
        return retorno;
    }

    public static boolean esPrimitivaValida(int tipoDeDato, boolean stringEsPrimitiva, boolean javaUtilDateEsPrimitiva, boolean bigIntEsPrimitiva) {
        boolean esTipoValido = esTipoDeDatoValido(tipoDeDato);
        if (esTipoValido && (tipoDeDato == 8 && !javaUtilDateEsPrimitiva || tipoDeDato == 10 && !bigIntEsPrimitiva || tipoDeDato == 7 && !stringEsPrimitiva)) {
            esTipoValido = false;
        }
        return esTipoValido;
    }

    public static String[] adicionarFilaAArray(String arrayOriginal[]) {
        String arrayTmp[] = arrayOriginal;
        int nuevoLimite = arrayTmp.length + 1;
        arrayOriginal = new String[nuevoLimite];
        System.arraycopy(arrayTmp, 0, arrayOriginal, 0, arrayTmp.length);
        return arrayOriginal;
    }

    public static String convertirNumeroReciboOReferencia(Long numeroRecibo) {
        return convertirNumeroReciboOReferencia(numeroRecibo.toString());
    }

    public static String convertirNumeroReciboOReferencia(String numeroRecibo) {
        String recibo = numeroRecibo;
        if (numeroRecibo.length() == 11) {
            recibo = colocarCaracteresALaIzquierda('0', 12, numeroRecibo);
        } else if (numeroRecibo.length() == 13) {
            recibo = colocarCaracteresALaIzquierda('0', 14, numeroRecibo);
        }
        return recibo;
    }

    public static String obtenerString(byte datos[])
            throws UnsupportedEncodingException {
        return obtenerString(datos, "ISO-8859-1");
    }

    public static String obtenerString(byte datos[], String encoding)
            throws UnsupportedEncodingException {
        return new String(datos, encoding);
    }

    public static byte[] obtenerArregloByte(String datos)
            throws UnsupportedEncodingException {
        return obtenerArregloByte(datos, "ISO-8859-1");
    }

    public static byte[] obtenerArregloByte(String datos, String encoding)
            throws UnsupportedEncodingException {
        return datos.getBytes(encoding);
    }

    public static String completarString(String caracter, String dato, char alineacion, int longitud)
            throws IOException {
        String completo = dato;
        int longitudActual = dato.length();
        if (longitudActual > longitud) {
            throw new IOException("El dato es mas grande que el campo");
        }
        if (caracter.length() != 1) {
            throw new IOException("Caracter de relleno no valido");
        }
        for (int i = longitudActual; i < longitud; i++) {
            switch (alineacion) {
                case 68: // 'D'
                    completo = (new StringBuilder()).append(caracter).append(completo).toString();
                    break;

                case 73: // 'I'
                    completo = (new StringBuilder()).append(completo).append(caracter).toString();
                    break;
            }
        }

        return completo;
    }

    public static boolean esObjetoSerializable(Object objeto) {
        boolean respuesta;
        ByteArrayOutputStream out;
        ObjectOutputStream oos;
        respuesta = true;
        out = null;
        oos = null;
        try {
            out = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(out);
            oos.writeObject(objeto);
        } catch (IOException ex) {
            respuesta = false;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e) {
            }

            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
            }
        }

        return respuesta;
    }

    public static List getListaSelectItemsFromMap(Map map) {
        List respuesta = new ArrayList();
        Set setMap = map.entrySet();
        SelectItem item;
        for (Iterator it = setMap.iterator(); it.hasNext(); respuesta.add(item)) {
            java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
            item = new SelectItem();
            item.setLabel(entry.getKey().toString());
            item.setValue(entry.getValue());
        }

        return respuesta;
    }

    public static List getListaSelectItemsFromMapNormal(Map mapa) {
        List lista;
        if (mapa != null && !mapa.isEmpty()) {
            lista = new ArrayList();
            Object key;
            for (Iterator i$ = mapa.keySet().iterator(); i$.hasNext(); lista.add(new SelectItem(key, mapa.get(key).toString()))) {
                key = i$.next();
            }

        } else {
            lista = null;
        }
        return lista;
    }

    public static void colocarElementoEnListaSelectItem(List lista, int pos, String label, Object value) {
        SelectItem item = new SelectItem();
        item.setLabel(label);
        item.setValue(value);
        lista.add(pos, item);
    }

    public static List getListaSelectItemsFromMap(Map map, boolean keyValue) {
        List respuesta = new ArrayList();
        Set setMap = map.entrySet();
        SelectItem item;
        for (Iterator it = setMap.iterator(); it.hasNext(); respuesta.add(item)) {
            java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
            item = new SelectItem();
            item.setLabel(keyValue ? entry.getValue().toString() : entry.getKey().toString());
            item.setValue(keyValue ? entry.getKey() : entry.getValue());
        }

        return respuesta;
    }

    public static String enmascararDato(String cadenaEnmascarar, char caracterMascara, int numeroCarateresMostrar, boolean mostrarCaracteresDerecha) {
        StringBuilder cadenaEnmascarada = new StringBuilder();
        if (numeroCarateresMostrar < 0 || numeroCarateresMostrar > cadenaEnmascarar.length()) {
            return cadenaEnmascarar;
        }
        int caracteresMascara = cadenaEnmascarar.length() - numeroCarateresMostrar;
        for (int i = 0; i < caracteresMascara; i++) {
            cadenaEnmascarada.append(caracterMascara);
        }

        String cadenaAModificar;
        if (mostrarCaracteresDerecha) {
            cadenaAModificar = (new StringBuilder()).append(cadenaEnmascarada.toString()).append(cadenaEnmascarar.substring(cadenaEnmascarar.length() - numeroCarateresMostrar)).toString();
        } else {
            cadenaAModificar = (new StringBuilder()).append(cadenaEnmascarar.substring(0, numeroCarateresMostrar)).append(cadenaEnmascarada.toString()).toString();
        }
        return cadenaAModificar;
    }

    public static boolean isTooLong(String value, int maxLength) {

        if (value != null && (value.length() > maxLength)) {
            return true;
        }

        return false;

    }
    public static final String ENCODING_ISO = "ISO-8859-1";
    public static final int BYTE = 1;
    public static final int SHORT = 2;
    public static final int INTEGER = 3;
    public static final int LONG = 4;
    public static final int FLOAT = 5;
    public static final int DOUBLE = 6;
    public static final int STRING = 7;
    public static final int UTIL_DATE = 8;
    public static final int BOOLEAN = 9;
    public static final int BIG_INT = 10;
    public static final int CHARACTER = 11;
}
