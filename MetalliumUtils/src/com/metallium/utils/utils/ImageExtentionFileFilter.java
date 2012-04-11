/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.utils;

import com.metallium.utils.regex.ImageValidator;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.*;

/**
 * 20111207
 * @author Ruben
 */
public class ImageExtentionFileFilter implements FilenameFilter {

    private ImageExtentionFileFilter() {
    }

    //Singleton Lazy Loader.
    public static ImageExtentionFileFilter getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {

        private static final ImageExtentionFileFilter instance = new ImageExtentionFileFilter();
    }

    public boolean accept(File dir, String name) {
        return ImageValidator.validate(name);
    }
}
