/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 20101119
 * @author Ruben Escudero
 */
public class FormatHelper {

    //Number Helper
    private static final NumberFormat numeroConSoloDosDecimales = new DecimalFormat("####################.##");
    private static final NumberFormat numeroSinDecimales = new DecimalFormat("####################");
    public static final String LINE_SEPARATOR = "\r\n";
    private static Pattern patronNombreTablaFacturacion = Pattern.compile("[A-Za-z][A-Za-z0-9_]*");
    public  SimpleDateFormat format_yyMMddHHmmssSSS = new SimpleDateFormat("yyMMddHHmmssSSS"); //I use this to create an Id for the user once I register him
    private  SimpleDateFormat formatyyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    private  SimpleDateFormat formatyyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
    private  SimpleDateFormat formatoMMddHHmmss = new SimpleDateFormat("MMddHHmmss");
    private  SimpleDateFormat formatoHHmmss = new SimpleDateFormat("HHmmss");
    private  SimpleDateFormat formatyyyyMMdd_HH_mm_ss = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    private  SimpleDateFormat formatyyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private  SimpleDateFormat format_dd_MM_yyyy_hh_mm_aaa = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
    private  SimpleDateFormat formatyyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");      //fecha de la base de datos, para insertar en un stored procedure
    private  SimpleDateFormat formatohmm_aa = new SimpleDateFormat("h:mm aa");
    //IsoHelper
    private  SimpleDateFormat formatoMMddhhmmss = new SimpleDateFormat("MMddhhmmss");
    private  SimpleDateFormat formatoMMdd = new SimpleDateFormat("MMdd");
    private  SimpleDateFormat formatohhmmss = new SimpleDateFormat("hhmmss");
    public  SimpleDateFormat format_yDDD = new SimpleDateFormat("yDDD"); //La fecha Juliana para el P37

    private  SimpleDateFormat formato_yyyyDDDhhmmss = new SimpleDateFormat("yyyyDDDhhmmss"); //P37 + P12
    private  SimpleDateFormat formato_MMddyyyy = new SimpleDateFormat("MMddyyyy");



    //LDAPHelper
    // Se crea un formateador de acuerdo al standard x.208
    private static SimpleDateFormat formatx208 = new SimpleDateFormat("yyyyMMddHHmmss'Z'");

    //---String a Date----------------------
    public Date parse_yyyyMMdd(String string) throws ParseException {
        return formatyyyyMMdd.parse(string);
    }

    public static Date parse_x208(String fechaTexto) throws ParseException {
        return formatx208.parse(fechaTexto);
    }

    //Esto es para la FECHA_HORA_TX de la tabla Pago
    public Date formato_yyyyDDDhhmmss(String fecha) throws ParseException {
        return formato_yyyyDDDhhmmss.parse(fecha);
    }

    //Esto es para la FECHA_COMPENSA_FRT de la tabla Pago
    public Date formato_MMddyyyy(String fecha) throws ParseException {
        return formato_MMddyyyy.parse(fecha);
    }

    //---Date a String----------------------
    public String format_yyyyMMdd(Date fecha) {
        return formatyyyyMMdd.format(fecha);
    }

    public String format_MMddHHmmss(Date fecha) {
        return formatoMMddHHmmss.format(fecha);
    }

    public String format_yyyyMMddHHmmss(Date fecha) {
        return formatyyyyMMddHHmmss.format(fecha);
    }

    public String format_yyyyMMdd_HH_mm_ss(Date fecha) {
        return formatyyyyMMdd_HH_mm_ss.format(fecha);
    }

    public String format_yyyy_MM_dd_HH_mm_ss(Date fecha) {
        return formatyyyy_MM_dd_HH_mm_ss.format(fecha);
    }

    public static String format_x208(Date fecha) {
        return formatx208.format(fecha);
    }

    public String format_MMdd(Date fecha) {
        return formatoMMdd.format(fecha);
    }

    public String format_HHmmss(Date fecha) {
        return formatoHHmmss.format(fecha);
    }

    public String format_yDDD(Date fecha) {
        return format_yDDD.format(fecha).substring(1);
    }

    public String format_dd_MM_yyyy_hh_mm_aaa(Date fecha) {
        return format_dd_MM_yyyy_hh_mm_aaa.format(fecha);
    }

    public String format_MMddhhmmss(Date fecha) {
        return formatoMMddhhmmss.format(fecha);
    }

    public String format_hhmmss(Date fecha) {
        return formatohhmmss.format(fecha);
    }

    public String formatyyyy_MM_dd(Date fecha) {
        return formatyyyy_MM_dd.format(fecha);
    }

    /**
     * verifica si el sistema operativo es windows.
     * @return
     */
    public String generarHora(Date fecha){
        return fecha != null ? formatohmm_aa.format(fecha) : "--:--";
    }


    public static String numeroConSoloDosDecimales(Object numericValue) {
        return numeroConSoloDosDecimales.format(numericValue);
    }

    public static String numeroSinDecimales(Object numericValue) {
        return numeroSinDecimales.format(numericValue);
    }

    public static boolean concuerdaNombreTablaFacturacion(String nombreTablaFacturacion) {
        Matcher matcher = patronNombreTablaFacturacion.matcher(nombreTablaFacturacion);
        return matcher.matches();
    }
}
