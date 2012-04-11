/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.framework.utilities;

import com.metallium.utils.framework.configuration.ConfigurationConstants;
import com.metallium.utils.framework.exception.InterfaceBaseException;
import javax.servlet.GenericServlet;

/**
 *
 * @author Ruben
 */
public class LogHelper {

    public LogHelper() {
    }

    public static void makeLog(String message)
    {
        makeLog(message, null, null);
    }

    public static void makeLog(Exception exception)
    {
        makeLog(exception.getMessage(), exception, null);
    }

    public static void makeLog(String message, Exception exception)
    {
        makeLog(message, exception, null);
    }

    public static void makeLog(String message, Exception exception, GenericServlet servlet)
    {
        String fecha = DateHelper.buildDateToString(DateHelper.obtenerFechaActualDate(), "dd/MM/yyyy H:m:s");
        String msgToPrint = (new StringBuilder()).append("\r\nCybercore Framework LOG: ").append(fecha).append("\r\n").append(message).toString();
        if(ConfigurationConstants.MODO_DEPURACION)
        {
            if(exception != null)
                msgToPrint = (new StringBuilder()).append(msgToPrint).append("\r\nError:").append(buildLogMessageStringFromException(exception)).toString();
            logguear(msgToPrint, exception, servlet);
        }
    }

    private static void logguear(String message, Throwable exception, GenericServlet servlet)
    {
        switch(1)
        {
        }
        if(servlet != null)
        {
            if(ConfigurationConstants.IMPRIMIR_STACK_TRACE && exception != null)
                servlet.log((new StringBuilder()).append(message).append("\r\n****STACK TRACE****\r\n").toString(), exception);
            else
                servlet.log(message);
        } else
        if(ConfigurationConstants.IMPRIMIR_STACK_TRACE && exception != null)
        {
            message = (new StringBuilder()).append(message).append("\r\n****STACK TRACE****\r\n").toString();
            printInSystemOut(message, exception);
        } else
        {
            printInSystemOut(message, null);
        }
    }

    private static void printInSystemOut(String message, Throwable exception)
    {
        System.out.println(message);
        if(exception != null)
            exception.printStackTrace(System.out);
    }

    public static String buildLogMessageStringFromException(Exception exception)
    {
        String anwser = "";
        if(exception != null)
        {
            String claseName = exception.getClass().getName();
            anwser = (new StringBuilder()).append(anwser).append("(").append(claseName).append(") --> ").toString();
            anwser = (new StringBuilder()).append(anwser).append(" [").append(exception.getMessage()).append("] ").toString();
            Exception exceptionCopy = exception;
            do
            {
                if(!(exceptionCopy instanceof InterfaceBaseException))
                    break;
                InterfaceBaseException beTemp = (InterfaceBaseException)exceptionCopy;
                if(beTemp.getExcepcion() == null)
                    break;
                Exception eTemp = beTemp.getExcepcion();
                anwser = (new StringBuilder()).append(anwser).append(" [").append(eTemp.getMessage()).append("] ").toString();
                exceptionCopy = beTemp.getExcepcion();
            } while(true);
        }
        return anwser;
    }


}
