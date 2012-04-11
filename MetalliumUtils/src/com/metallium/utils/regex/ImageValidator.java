/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.regex;

import java.util.regex.Pattern;

/**
 * http://www.mkyong.com/regular-expressions/how-to-validate-image-file-extension-with-regular-expression/
 *
 * 20110127
 * @author Ruben
 */
public class ImageValidator {

    public static final Pattern pattern = Pattern.compile("((.+{1,10})(\\.(?i)(jpg|png|gif))$)", Pattern.CASE_INSENSITIVE); //It doesn't accept a file with white spaces.
//    public static final Pattern pattern = Pattern.compile("([^\\s]+(\\.(?i)(jpg|png|gif))$)", Pattern.CASE_INSENSITIVE); //It doesn't accept a file with white spaces.
    
    
    
//    public static final Pattern pattern = Pattern.compile("([\\w\\s-_.]+(\\.(jpg|png|gif))$)", Pattern.CASE_INSENSITIVE);
    
    public ImageValidator() {
    }

    /**
     * Validate image with regular expression
     * @param image image for validation
     * @return true valid image, false invalid image
     */
    public static boolean validate(final String image) {
        return pattern.matcher(image).matches();
    }
}
