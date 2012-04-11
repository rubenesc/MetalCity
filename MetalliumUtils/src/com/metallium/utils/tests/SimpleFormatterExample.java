/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.tests;


import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SimpleFormatterExample {

  private static Logger logger = Logger.getLogger("SimpleFormatterExample");

  private static void logMessages() {
    logger.info("Line One");
    logger.info("Line Two");
  }

  public static void main(String[] args) {
    logger.setUseParentHandlers(false);
    Handler conHdlr = new ConsoleHandler();
    conHdlr.setFormatter(new Formater1());
    logger.addHandler(conHdlr);
    logMessages();

  }
}