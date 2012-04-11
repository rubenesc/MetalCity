/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.framework.utilities;

//import co.com.itac.framework.excepciones.InvalidDateException;
//import co.com.itac.framework.excepciones.InvalidParameterException;
//import co.com.itac.framework.excepciones.ParameterRequiredException;
import com.metallium.utils.framework.exception.InvalidDateException;
import com.metallium.utils.framework.exception.InvalidParameterException;
import com.metallium.utils.framework.exception.ParameterRequiredException;
//import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Ruben
 */
public class DateHelper {

    public DateHelper() {
    }

    /**
     * Metodo que recibe una fecha y la retorna con los milisegundos en cero
     * @param fechaAModificar
     * @return la fecha que ingreso por parametros con los milisegundos en Cero
     */
    public static Date setMilisEnCero(Date fechaAModificar) {

        Calendar calendario = Calendar.getInstance();

        calendario.setTime(fechaAModificar);
        calendario.set(Calendar.MILLISECOND, 0);

        return (Date) calendario.getTime();

    }

    /**
     * Coloca la hora de una fecha en 0
     * @param fechaAModificar
     */
    public static java.util.Date setHoraEnCero(java.util.Date date) {
        Calendar fechaAModificar = Calendar.getInstance();
        fechaAModificar.setTime(date);
        fechaAModificar.set(Calendar.MILLISECOND, 0);
        fechaAModificar.set(Calendar.HOUR_OF_DAY, 0);
        fechaAModificar.set(Calendar.SECOND, 0);
        fechaAModificar.set(Calendar.MINUTE, 0);
        return fechaAModificar.getTime();
    }

    public static java.util.Date setHoraEn235959(java.util.Date date) {
        Calendar fechaAModificar = Calendar.getInstance();
        fechaAModificar.setTime(date);
        fechaAModificar.set(Calendar.MILLISECOND, 999);
        fechaAModificar.set(Calendar.HOUR_OF_DAY, 23);
        fechaAModificar.set(Calendar.SECOND, 59);
        fechaAModificar.set(Calendar.MINUTE, 59);
        return fechaAModificar.getTime();
    }

    public static boolean isDate(String date) {
        return isDate(date, "dd/MM/yyyy");
    }

    public static boolean isDate(String date, String format) {
        if (format == null) {
            format = "dd/MM/yyyy";
        }
        try {
            buildDate(date, format);
            return true;
        } catch (InvalidDateException e) {
            return false;
        }
    }

    public static Date armarFechaSQL(String fecha)
            throws InvalidDateException {
        return armarFechaSQL(fecha, "dd/MM/yyyy");
    }

    public static Date armarFechaSQL(String fecha, String formato)
            throws InvalidDateException {
        Calendar cTemp = buildDate(fecha, formato);
        java.util.Date dTemp = cTemp.getTime();
        Date respuesta = new Date(dTemp.getTime());
        return respuesta;
    }

    public static Date armarFechaSQL(Calendar fecha)
            throws InvalidDateException {
        if (fecha == null) {
            throw new InvalidDateException((new StringBuilder()).append("La fecha '").append(fecha).append("' no es v\uFFFDlida").toString());
        } else {
            java.util.Date dTemp = fecha.getTime();
            Date respuesta = new Date(dTemp.getTime());
            dTemp = null;
            return respuesta;
        }
    }

    public static Calendar armarFecha(String fecha)
            throws InvalidDateException {
        return buildDate(fecha, "dd/MM/yyyy");
    }

    public static Calendar buildDate(String fecha, String formato)
            throws InvalidDateException {
        java.util.Date dTemp = armarFechaDate(fecha, formato);
        Calendar cTemp = Calendar.getInstance();
        cTemp.setTime(dTemp);
        dTemp = null;
        return cTemp;
    }

    public static java.util.Date armarFechaDate(String fecha)
            throws InvalidDateException {
        return armarFechaDate(fecha, "dd/MM/yyyy");
    }

    public static java.util.Date armarFechaDate(String fecha, String formato)
            throws InvalidDateException {
        if (fecha == null || formato == null) {
            throw new InvalidDateException((new StringBuilder()).append("La fecha '").append(fecha).append("' no es v\uFFFDlida").toString());
        }
        SimpleDateFormat sdfTemp = new SimpleDateFormat(formato, localizacion);
        sdfTemp.setLenient(false);
        java.util.Date dTemp = null;
        try {
            dTemp = sdfTemp.parse(fecha);
        } catch (ParseException e) {
            throw new InvalidDateException((new StringBuilder()).append("La fecha '").append(fecha).append("' no es v\uFFFDlida con el formato: ").append(formato).toString());
        }
        if (dTemp == null) {
            throw new InvalidDateException((new StringBuilder()).append("La fecha ").append(fecha).append(" no es v\uFFFDlida con el formato: ").append(formato).toString());
        } else {
            sdfTemp = null;
            return dTemp;
        }
    }

    public static String buildDateToString() {
        return buildDateToString(Calendar.getInstance(), "dd/MM/yyyy");
    }

    public static String buildDateToString(Date fechaSQL, String formato) {
        String respuesta = null;
        if (fechaSQL != null) {
            if (formato == null) {
                formato = "dd/MM/yyyy";
            }
            java.util.Date fTemp = new java.util.Date(fechaSQL.getTime());
            respuesta = buildDateToString(fTemp, formato);
        }
        return respuesta;
    }

    public static String buildDateToString(Date fechaSQL) {
        return buildDateToString(fechaSQL, "dd/MM/yyyy");
    }

    public static String buildDateToString(java.util.Date fecha) {
        return buildDateToString(fecha, "dd/MM/yyyy");
    }

    public static String buildDateToString(java.util.Date fecha, String formato) {
        if (formato == null) {
            formato = "dd/MM/yyyy";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(formato, localizacion);
        dateFormat.setLenient(false);
        return dateFormat.format(fecha);
    }

    public static String armarFechaString(java.util.Date fecha, String formato, Locale localizacion) {
        if (formato == null) {
            formato = "dd/MM/yyyy";
        }
        if (localizacion == null) {
            localizacion = localizacion;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(formato, localizacion);
        dateFormat.setLenient(false);
        return dateFormat.format(fecha);
    }

    public static String buildDateToString(Calendar fecha, String formato) {
        String respuesta = null;
        if (fecha != null) {
            if (formato == null) {
                respuesta = buildDateToString(fecha.getTime(), "dd/MM/yyyy");
            } else {
                respuesta = buildDateToString(fecha.getTime(), formato);
            }
        }
        return respuesta;
    }

    public static String armarFechaString(Calendar fecha) {
        return buildDateToString(fecha, "dd/MM/yyyy");
    }

    public static int compararFechas(String fecha1, String fecha2)
            throws InvalidDateException, ParameterRequiredException {
        Calendar cFecha1 = armarFecha(fecha1);
        Calendar cFecha2 = armarFecha(fecha2);
        return compararFechas(cFecha1, cFecha2);
    }

    public static int compararFechas(java.util.Date fecha1, java.util.Date fecha2) {
        Calendar cFecha1 = armarFechaCalendar(fecha1);
        Calendar cFecha2 = armarFechaCalendar(fecha2);
        return compararFechas(cFecha1, cFecha2);
    }

    public static int compararFechas(Calendar fecha1, Calendar fecha2)
            throws ParameterRequiredException {
        if (fecha1 == null) {
            throw new ParameterRequiredException("Se requiere la fecha 1");
        }
        if (fecha2 == null) {
            throw new ParameterRequiredException("Se requiere la fecha 2");
        }
        try {
            if (fecha1.after(fecha2)) {
                return -1;
            }
        } catch (IllegalArgumentException e) {
            throw new ParameterRequiredException(e.toString());
        }
        if (fecha2.after(fecha1)) {
            return 1;
        }
        return 0;
    }

    public static Date obtenerFechaActualSQL() {
        try {
            Calendar c = Calendar.getInstance();
            return armarFechaSQL(c);
        } catch (Exception e) {
            return null;
        }
    }

    public static java.util.Date obtenerFechaActualDate() {
        try {
            Calendar c = Calendar.getInstance();
            return armarFecha(c);
        } catch (Exception e) {
            return null;
        }
    }

    public static java.util.Date armarFecha(Calendar fecha)
            throws InvalidDateException {
        if (fecha == null) {
            throw new InvalidDateException((new StringBuilder()).append("La fecha '").append(fecha).append("' no es v\uFFFDlida").toString());
        } else {
            return fecha.getTime();
        }
    }

    public static Date armarFechaCortaSQL(Calendar fecha)
            throws InvalidDateException {
        if (fecha == null) {
            throw new InvalidDateException((new StringBuilder()).append("La fecha '").append(fecha).append("' no es v\uFFFDlida").toString());
        } else {
            return armarFechaSQL(buildDateToString(fecha, "dd/MM/yyyy"));
        }
    }

    public static java.util.Date armarFechaCortaDate(Calendar fecha) {
        java.util.Date respuesta = null;
        if (fecha != null) {
            try {
                respuesta = armarFechaDate(buildDateToString(fecha, "dd/MM/yyyy"));
            } catch (InvalidDateException ex) {
            }
        }
        return respuesta;
    }

    public static Date obtenerFechaCortaActualSQL() {
        try {
            return armarFechaCortaSQL(Calendar.getInstance());
        } catch (InvalidDateException e) {
            return null;
        }
    }

    public static String obtenerFechaCortaActualString() {
        return armarFechaString(Calendar.getInstance());
    }

    public static java.util.Date obtenerFechaCortaActualDate() {
        return armarFechaCortaDate(Calendar.getInstance());
    }

    public static Calendar armarFechaCalendar(Date fecha) {
        Calendar respuesta = Calendar.getInstance();
        java.util.Date fechaDate = convertirAFechaUtil(fecha);
        respuesta.setTime(fechaDate);
        return respuesta;
    }

    public static Calendar armarFechaCalendar(java.util.Date fecha) {
        Calendar respuesta = null;
        if (fecha != null) {
            respuesta = Calendar.getInstance();
            respuesta.setTime(fecha);
        }
        return respuesta;
    }

    public static Date convertirAFechaSQL(java.util.Date fecha) {
        Date respuesta = null;
        if (fecha != null) {
            respuesta = new Date(fecha.getTime());
        }
        return respuesta;
    }

    public static java.util.Date convertirAFechaUtil(Date fecha) {
        java.util.Date respuesta = null;
        if (fecha != null) {
            respuesta = new java.util.Date(fecha.getTime());
        }
        return respuesta;
    }

    public static Calendar obtenerSiguienteDia(Calendar fechaInicial) {
        return obtenerSiguienteDiaHabil(fechaInicial, 7, null, true, true);
    }

    public static Calendar obtenerSiguienteDiaHabil(Calendar fechaInicial, int numeroDiasHabiles, Collection festivos, boolean sabadoEsHabil, boolean domingoEsHabil) {
        DataHelper.validarObjetoNulo("fechaInicial", fechaInicial);
        if (festivos == null) {
            festivos = new ArrayList();
        }
        Calendar copia = (Calendar) fechaInicial.clone();
        int mes = copia.get(2);
        int ano = copia.get(1);
        int dia = copia.get(5);
        copia.clear();
        copia.set(2, mes);
        copia.set(1, ano);
        copia.set(5, dia);
        Collection festivosValidos = new ArrayList();
        Iterator iTemp = festivos.iterator();
        do {
            if (!iTemp.hasNext()) {
                break;
            }
            Calendar cTemp = (Calendar) iTemp.next();
            if (cTemp.after(fechaInicial)) {
                Calendar copiaAGuardarDentroDelCollectionDeFestivosValidos = (Calendar) cTemp.clone();
                mes = copiaAGuardarDentroDelCollectionDeFestivosValidos.get(2);
                ano = copiaAGuardarDentroDelCollectionDeFestivosValidos.get(1);
                dia = copiaAGuardarDentroDelCollectionDeFestivosValidos.get(5);
                copiaAGuardarDentroDelCollectionDeFestivosValidos.clear();
                copiaAGuardarDentroDelCollectionDeFestivosValidos.set(2, mes);
                copiaAGuardarDentroDelCollectionDeFestivosValidos.set(1, ano);
                copiaAGuardarDentroDelCollectionDeFestivosValidos.set(5, dia);
                festivosValidos.add(copiaAGuardarDentroDelCollectionDeFestivosValidos);
            }
        } while (true);
        int numeroTotalDeDiasAIncrementar = 0;
        int limite = numeroDiasHabiles;
        for (int i = 1; i <= limite;) {
            copia.add(5, 1);
            if (festivosValidos.contains(copia)) {
                limite++;
            } else if (!sabadoEsHabil && copia.get(7) == 7) {
                limite++;
            } else if (!domingoEsHabil && copia.get(7) == 1) {
                limite++;
            }
            i++;
            numeroTotalDeDiasAIncrementar++;
        }

        Calendar respuesta = (Calendar) fechaInicial.clone();
        respuesta.add(5, numeroTotalDeDiasAIncrementar);
        return respuesta;
    }

    public static Calendar obtenerSiguienteMes(Calendar fechaInicial, int numeroMeses) {
        DataHelper.validarObjetoNulo("fechaInicial", fechaInicial);
        Calendar copia = (Calendar) fechaInicial.clone();
        int mes = copia.get(2);
        int ano = copia.get(1);
        int dia = copia.get(5);
        copia.clear();
        copia.set(2, mes);
        copia.set(1, ano);
        copia.set(5, dia);
        copia.set(2, mes + numeroMeses);
        Calendar respuesta = (Calendar) copia.clone();
        return respuesta;
    }

    public static Calendar obtenerAnteriorMes(Calendar fechaInicial, int numeroMeses) {
        DataHelper.validarObjetoNulo("fechaInicial", fechaInicial);
        Calendar copia = (Calendar) fechaInicial.clone();
        int mes = copia.get(2);
        int ano = copia.get(1);
        int dia = copia.get(5);
        copia.clear();
        copia.set(2, mes);
        copia.set(1, ano);
        copia.set(5, dia);
        copia.set(2, mes - numeroMeses);
        Calendar respuesta = (Calendar) copia.clone();
        return respuesta;
    }

    public static Integer obtenerDiferenciaNumeroDiasAHoy(java.util.Date fechaAnterior)
            throws InvalidDateException {
        return obtenerDiferenciaNumeroDias(fechaAnterior, obtenerFechaActualDate());
    }

    public static Integer obtenerDiferenciaNumeroDias(java.util.Date fechaAnterior, java.util.Date fechaPosterior)
            throws InvalidDateException {
        if (fechaAnterior == null) {
            throw new InvalidDateException((new StringBuilder()).append("La fecha '").append(fechaAnterior).append("' no es v\uFFFDlida").toString());
        }
        if (fechaPosterior == null) {
            throw new InvalidDateException((new StringBuilder()).append("La fecha '").append(fechaPosterior).append("' no es v\uFFFDlida").toString());
        }
        if (fechaAnterior.after(fechaPosterior)) {
            throw new InvalidDateException((new StringBuilder()).append("La fecha '").append(fechaAnterior).append("'  es posterior a la fecha '").append(fechaPosterior).append("'").toString());
        } else {
            long diferencia = fechaPosterior.getTime() - fechaAnterior.getTime();
            diferencia /= 0x5265c00L;
            return new Integer((int) diferencia);
        }
    }

    public static java.util.Date armarFechaDate(String fecha, String formatos[]) {
        java.util.Date respuesta = null;
        if (fecha != null) {
            if (formatos == null) {
                try {
                    respuesta = armarFechaDate(fecha);
                } catch (InvalidDateException e) {
                }
            } else {
                int longitud = formatos.length;
                for (int i = 0; i < longitud; i++) {
                    try {
                        if ((respuesta = armarFechaDate(fecha, formatos[i])) != null) {
                            break;
                        }
                    } catch (InvalidDateException e) {
                    }
                }

            }
        }
        return respuesta;
    }

    public static java.util.Date convertirAFechaUtil(Timestamp fecha) {
        java.util.Date respuesta = null;
        if (fecha != null) {
            respuesta = new java.util.Date(fecha.getTime());
        }
        return respuesta;
    }

    public static java.util.Date convertirAFechaSQL(Timestamp fecha) {
        Date respuesta = null;
        if (fecha != null) {
            respuesta = new Date(fecha.getTime());
        }
        return respuesta;
    }

    public static Calendar obtenerFechaMenor(Collection fechas) {
        Calendar respuesta = null;
        if (fechas != null) {
            Iterator iTemp = fechas.iterator();
            if (iTemp.hasNext()) {
                respuesta = (Calendar) iTemp.next();
                do {
                    if (!iTemp.hasNext()) {
                        break;
                    }
                    Calendar cTemp = (Calendar) iTemp.next();
                    if (cTemp.before(respuesta)) {
                        respuesta = cTemp;
                    }
                } while (true);
            }
        }
        return respuesta;
    }

    public static Calendar obtenerFechaMayor(Collection fechas) {
        Calendar respuesta = null;
        if (fechas != null) {
            Iterator iTemp = fechas.iterator();
            if (iTemp.hasNext()) {
                respuesta = (Calendar) iTemp.next();
            }
            do {
                if (!iTemp.hasNext()) {
                    break;
                }
                Calendar cTemp = (Calendar) iTemp.next();
                if (cTemp.after(respuesta)) {
                    respuesta = cTemp;
                }
            } while (true);
        }
        return respuesta;
    }

    public static String armarCondicionFechaParaConsultaSQL(String nombreCampoFecha, String operador, java.util.Date fecha)
            throws InvalidParameterException {
        return armarCondicionFechaParaConsultaSQL(nombreCampoFecha, operador, fecha, null);
    }

    public static String armarCondicionFechaParaConsultaSQL(String nombreCampoFecha, String operador, java.util.Date fecha, String operadorDeConexionParaAnteriorCondicion)
            throws InvalidParameterException {
        DataHelper.validarStringVacio("nombreCampoFecha", nombreCampoFecha);
        DataHelper.validarStringVacio("operador", operador);
        DataHelper.validarObjetoNulo("fecha", fecha);
        String fechaString = buildDateToString(fecha, "dd/MM/yyyy");
        if (operadorDeConexionParaAnteriorCondicion != null) {
            return (new StringBuilder()).append(operadorDeConexionParaAnteriorCondicion).append("( ").append(nombreCampoFecha).append(" ").append(operador).append(" CAST ( '").append(fechaString).append("' AS datetime ))").toString();
        } else {
            return (new StringBuilder()).append("( ").append(nombreCampoFecha).append(" ").append(operador).append(" CAST ( '").append(fechaString).append("' AS datetime ))").toString();
        }
    }

    public static Calendar obtenerProximoDiaDeSemana(Calendar fechaBase, int proximoDiaDeSemana)
            throws InvalidParameterException {
        Calendar respuesta = null;
        int numeroDiasDiferencia = 0;
        DataHelper.validarObjetoNulo("fechaBase", fechaBase);
        if (proximoDiaDeSemana != 2 && proximoDiaDeSemana != 3 && proximoDiaDeSemana != 4 && proximoDiaDeSemana != 5 && proximoDiaDeSemana != 6 && proximoDiaDeSemana != 7 && proximoDiaDeSemana != 1) {
            throw new InvalidParameterException((new StringBuilder()).append("D\uFFFDa de la semana suministrado inv\uFFFDlido:").append(proximoDiaDeSemana).toString());
        }
        numeroDiasDiferencia = proximoDiaDeSemana - fechaBase.get(7);
        if (numeroDiasDiferencia <= 0) {
            numeroDiasDiferencia += 7;
        }
        respuesta = (Calendar) fechaBase.clone();
        respuesta.add(5, numeroDiasDiferencia);
        return respuesta;
    }

    public static Calendar obtenerProximaOcurrenciaDiaDeSemana(Calendar fechaBase, int proximoDiaDeSemana, int numeroOcurrencia)
            throws InvalidParameterException {
        if (numeroOcurrencia < 1 || numeroOcurrencia > 4) {
            throw new InvalidParameterException((new StringBuilder()).append("N\uFFFDmero de ocurrencia suministrado inv\uFFFDlido:").append(numeroOcurrencia).toString());
        }
        Calendar respuesta = fechaBase;
        for (int i = 0; i < numeroOcurrencia; i++) {
            respuesta = obtenerProximoDiaDeSemana(respuesta, proximoDiaDeSemana);
        }

        return respuesta;
    }

    public static Timestamp convertirAFechaTimeStamp(Date fechaSQL) {
        Timestamp respuesta = null;
        if (fechaSQL != null) {
            respuesta = new Timestamp(fechaSQL.getTime());
        }
        return respuesta;
    }

    public static Timestamp convertirAFechaTimeStamp(java.util.Date fechaUtil) {
        Timestamp respuesta = null;
        if (fechaUtil != null) {
            respuesta = new Timestamp(fechaUtil.getTime());
        }
        return respuesta;
    }

    public static Calendar convertirAfechaCorta(Calendar fecha) {
        DataHelper.validarObjetoNulo("fecha", fecha);
        Calendar copia = (Calendar) fecha.clone();
        int mes = copia.get(2);
        int ano = copia.get(1);
        int dia = copia.get(5);
        copia.clear();
        copia.set(2, mes);
        copia.set(1, ano);
        copia.set(5, dia);
        return copia;
    }

    public static String armarFechaString(Object fecha)
            throws InvalidDateException {
        return armarFechaString(fecha, null);
    }

    public static String armarFechaString(Object fecha, String formato)
            throws InvalidDateException {
        String respuesta = null;
        if (fecha instanceof java.util.Date) {
            if (formato == null) {
                respuesta = buildDateToString((java.util.Date) fecha, "dd/MM/yyyy");
            } else {
                respuesta = buildDateToString((java.util.Date) fecha, formato);
            }
        } else if (fecha instanceof Date) {
            if (formato == null) {
                respuesta = buildDateToString((Date) fecha, "dd/MM/yyyy");
            } else {
                respuesta = buildDateToString((Date) fecha, formato);
            }
        } else if (fecha instanceof Calendar) {
            if (formato == null) {
                respuesta = buildDateToString((Calendar) fecha, "dd/MM/yyyy");
            } else {
                respuesta = buildDateToString((Calendar) fecha, formato);
            }
        } else {
            throw new InvalidDateException((new StringBuilder()).append("El objeto suministrado no es de tipo fecha:").append(fecha.getClass().getName()).toString());
        }
        return respuesta;
    }

    public static java.util.Date toDate(XMLGregorianCalendar fecha)
            throws InvalidDateException {
        return fecha.toGregorianCalendar().getTime();
    }

    public static java.util.Date sumarDias(java.util.Date fecha, int cantidadDias)
            throws InvalidDateException {
        return armarFecha(obtenerSiguienteDiaHabil(armarFechaCalendar(fecha), cantidadDias, null, true, true));
    }

    public static Calendar obtenerSiguienteYears(Calendar fechaInicial, int numeroYears) {
        DataHelper.validarObjetoNulo("fechaInicial", fechaInicial);
        Calendar copia = (Calendar) fechaInicial.clone();
        int mes = copia.get(2);
        int ano = copia.get(1);
        int dia = copia.get(5);
        copia.clear();
        copia.set(2, mes);
        copia.set(1, ano);
        copia.set(5, dia);
        copia.set(1, ano + numeroYears);
        Calendar respuesta = (Calendar) copia.clone();
        return respuesta;
    }

    public static Calendar obtenerAnteriorDia(Calendar fechaInicial, int numeroDias) {
        Calendar copia = (Calendar) fechaInicial.clone();
        int mes = copia.get(2);
        int ano = copia.get(1);
        int dia = copia.get(5);
        copia.clear();
        copia.set(2, mes);
        copia.set(1, ano);
        copia.set(5, dia);
        copia.set(5, dia - numeroDias);
        Calendar respuesta = (Calendar) copia.clone();
        return respuesta;
    }
    public static final String FORMATO_FECHA_POR_DEFECTO = "dd/MM/yyyy";
    private static final Locale localizacion = new Locale("ES", "CO");
    public static final String FORMATOS_DE_FECHA_COMUNES[] = {
        "dd/MM/yyyy", "MM/dd/yyyy", "yyyy/MM/dd", "yyyy/dd/MM", "dd-MM-yyyy", "MM-dd-yyyy", "yyyy-MM-dd", "yyyy-dd-MM", "dd MM yyyy", "MM dd yyyy",
        "yyyy MM dd", "yyyy dd MM", "dd\\MM\\yyyy", "MM\\dd\\yyyy", "yyyy\\MM\\dd", "yyyy\\dd\\MM"
    };
}
