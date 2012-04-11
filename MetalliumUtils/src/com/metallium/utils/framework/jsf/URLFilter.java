/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.framework.jsf;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
public class URLFilter {
    public final static String[] MUST_NOT_ENDS_WITH = {".css",".gif",".png",".jpg",".js",".css.jsf",".gif.jsf",".png.jsf",".jpg.jsf",".js.jsf"};

    public boolean ignore(String uri){
        if(uri == null || uri.isEmpty()){
            return true;
        }
        if(mustNotEndsWith(uri)){
            return true;
        }
        return false;
    }

    boolean mustNotEndsWith(String uri) {
        for (String ending : MUST_NOT_ENDS_WITH) {
            if(uri.endsWith(ending)){
                return true;
                }
        }
        return false;
    }
}