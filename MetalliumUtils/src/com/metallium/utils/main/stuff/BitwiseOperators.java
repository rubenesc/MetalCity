/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.main.stuff;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ruben
 */
public class BitwiseOperators {

    public static void main(String[] args) {


        BitwiseOperators x = new BitwiseOperators();
        x.executeTest();
    }

    private void textToBinary(String text) {
        try {
            byte[] infoBin = text.getBytes("UTF-8");
            for (byte b : infoBin) {
                System.out.println("c:" + (char) b + "-> "
                        + Integer.toBinaryString(b));
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BitwiseOperators.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String binaryString(Integer text) {
        byte b = text.byteValue();
        return Integer.toBinaryString(b);
    }

    private void executeTest() {

        int a = 11; //1 0 1 1
        int b = 12; //1 1 0 0

        System.out.println("---> Bitwise Operators <---");

        if (true) {
            System.out.println("");
            for (Integer i = 0; i <= 20; i++) {
                byte byt = i.byteValue();
                System.out.println("#" + i + " = " + Integer.toBinaryString(byt));
            }

        }


        System.out.println("a: " + a);
        System.out.println("b: " + b);
        System.out.println("binary(" + a + "): " + binaryString(a));
        System.out.println("binary(" + b + "): " + binaryString(b));
        System.out.println("");

        System.out.println("a & b : " + (a & b));
        System.out.println("a | b : " + (a | b));
        System.out.println("a ^ b : " + (a ^ b));
        System.out.println("~a : " + (~a));
        System.out.println("a << b : " + (a << b));
        System.out.println("a >> b : " + (a >> b));
        System.out.println("a >>> b : " + (a >>> b));

        System.out.println("");
        System.out.println("is810(820) --> " + is0810("0820".getBytes()));
        System.out.println("is810(810) --> " + is0810("0810".getBytes()));



    }

    private boolean is0810(byte[] datos) {
        if (datos.length >= 4) {
            for (int i = 0; i < datos.length; i++) {
                System.out.println(i + ": " + datos[i]);
            }
            return datos[0] == 48 && datos[1] == 56 && datos[2] == 49 && datos[3] == 48;
        }
        return false;
    }
}
