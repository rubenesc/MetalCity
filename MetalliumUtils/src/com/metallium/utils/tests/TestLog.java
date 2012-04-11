/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.tests;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TestLog {

    public static Logger logger;

    static {
        try {
            boolean append = true;
            FileHandler fh = new FileHandler("c:\\TestLog.log", append);
            //fh.setFormatter(new XMLFormatter());
            fh.setFormatter(new Formater1());
            logger = Logger.getLogger("TestLog");
            logger.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        logger.severe("my severe message");
        logger.warning("my warning message");
        logger.info("my info message");
    }
}
