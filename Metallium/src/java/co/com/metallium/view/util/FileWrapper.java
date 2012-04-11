/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.view.util;

import java.io.File;
/**
 *
 * @author Ruben
 */
public class FileWrapper {

    private File file;
    private String name;
    private String size;

    public FileWrapper(File file){
        this.file = file;
        this.name = file.getName();

        long byteLenght = file.length();

        if( byteLenght/(1024*1024) > 0 ){
            this.size = byteLenght/(1024*1024) + " M Bytes";
        } else if( byteLenght/(1024) > 0 ){
            this.size = byteLenght/(1024) + " K Bytes";
        } else {
            this.size = byteLenght + " Bytes";
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


}
