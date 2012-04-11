/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.main;

import java.io.*;
import java.util.Scanner;

public final class LeerEscribirArchivoDeTexto {

    private final String nombreArchivo;
    public static void main(String... aArgs) throws IOException {

        String fileName = "archivoPrueba.txt";

        LeerEscribirArchivoDeTexto test = new LeerEscribirArchivoDeTexto(fileName);
        test.escribirArchivo();
        test.leerArchivo();
    }

    /** Constructor. */
    LeerEscribirArchivoDeTexto(String aFileName) {
        nombreArchivo = aFileName;
    }

    /** Write fixed content to the given file. */
    void escribirArchivo() throws IOException {
        Writer out = new OutputStreamWriter(new FileOutputStream(nombreArchivo));
        try {
            out.write("Esto es una pruebaaaa de escrituraaaaaaa bla bla bla");
        } finally {
            out.close();
        }
    }

    /** Read the contents of the given file. */
    void leerArchivo() throws IOException {
        log("Reading from file.");
        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner = new Scanner(new FileInputStream(nombreArchivo));
        try {
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine() + NL);
            }
        } finally {
            scanner.close();
        }
        log("Text read in: " + text);
    }

    private void log(String aMessage) {
        System.out.println(aMessage);
    }
}
