/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.utils.htmlscriptvalidator;

import java.util.regex.Pattern;

/**
 * 20100201
 * @author Ruben Escudero
 */
public class TagHtmlDTO {

    private Pattern[] htmlTagRegex;
    private String accion;
    private String nombre; //attribute name, just for information

    private String replaceString;

    public TagHtmlDTO(String tag1, String accion, String nombre) {
        htmlTagRegex = new Pattern[]{
                    Pattern.compile(tag1, Pattern.CASE_INSENSITIVE)};
        this.accion = accion;
        this.nombre = nombre;
    }

    public TagHtmlDTO(String tag1, String tag2, String accion, String nombre) {
        htmlTagRegex = new Pattern[]{
                    Pattern.compile(tag1, Pattern.CASE_INSENSITIVE), Pattern.compile(tag2, Pattern.CASE_INSENSITIVE)};
        this.accion = accion;
        this.nombre = nombre;
    }

    public TagHtmlDTO(String tag1, String tag2, String accion, String replaceString, String nombre) {
        htmlTagRegex = new Pattern[]{
                    Pattern.compile(tag1,Pattern.CASE_INSENSITIVE), Pattern.compile(tag2,Pattern.CASE_INSENSITIVE)};

        this.accion = accion;
        this.replaceString = replaceString;
        this.nombre = nombre;
    }


    public Pattern[] getHtmlTagRegex() {
        return htmlTagRegex;
    }

    public void setHtmlTagRegex(Pattern[] htmlTagRegex) {
        this.htmlTagRegex = htmlTagRegex;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getReplaceString() {
        return replaceString;
    }

    public void setReplaceString(String replaceString) {
        this.replaceString = replaceString;
    }

}