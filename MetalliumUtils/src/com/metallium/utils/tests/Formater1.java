/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.tests;

import java.util.logging.LogRecord;

import java.util.logging.*;
import java.io.*;

/**
 *
 * @author Ruben
 */


public class Formater1 extends Formatter {

    public String format(LogRecord rec) {
        StringBuilder buf = new StringBuilder(1000);
        buf.append(new java.util.Date());
        buf.append(" ,,|,, ");
        buf.append(rec.getLevel());
        buf.append(' ');
        buf.append(formatMessage(rec));
        buf.append('\n');
        return buf.toString();
    }

}
