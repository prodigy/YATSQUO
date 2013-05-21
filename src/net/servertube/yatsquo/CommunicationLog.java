/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
 *
 * CommunicationLog.java - 2012-08-29
 *
 * YATSQUO is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * YATSQUO is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with YATSQUO; If not, see
 * <http://www.gnu.org/licenses/>.
 */
package net.servertube.yatsquo;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public class CommunicationLog {
  private static PrintStream cLogOut;
  private static SimpleDateFormat dateFormat;
  /**
   * An internal Throwable used to determine the source stack.
   */
  private static Throwable source = null;
  /**
   * A list of the current source stack.
   */
  private static ArrayList<HashMap<String, String>> sourceStack = null;

  /**
   *
   * @param format
   */
  public static void setupDateFormat(String format) {
    if(format == null) {
      format = "yyyy-MM-dd hh:mm:ss";
    }
    dateFormat = new SimpleDateFormat(format);
  }

  /**
   *
   * @param file
   * @return
   */
  public static boolean openLog(String file) {
    try {
      cLogOut = new PrintStream(file, "UTF-8");
      return true;
    } catch (FileNotFoundException | UnsupportedEncodingException ex) {
      Logger.getLogger(CommunicationLog.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }

  /**
   *
   * @param msg
   * @param outbound
   */
  public static synchronized void log(String msg, boolean outbound) {
    if(cLogOut == null) {
      CommunicationLog.openLog("comm.log");
    }
    if(dateFormat == null) {
      CommunicationLog.setupDateFormat(null);
    }
    Date now = Calendar.getInstance().getTime();
    CommunicationLog.fillSourceStack();
    cLogOut.println(dateFormat.format(now) + " :: " + sourceStack.get(0).get("class") + " " + (outbound?"> ":"< ") + msg);
  }

  /**
   * Fills the sourceStack with the relevant information.<br /> This data can be
   * displayed when the message is displayed.
   */
  private static void fillSourceStack() {
    source = new Throwable();
    source.fillInStackTrace();
    ArrayList<HashMap<String, String>> stack = new ArrayList<HashMap<String, String>>();
    int x = 0;
    for (StackTraceElement elem : source.getStackTrace()) {
      if (elem.getClassName().equals(source.getStackTrace()[0].getClassName())) {
        continue; // skip the logging class in the stack trace
      }
      ++x;
      HashMap<String, String> data = new HashMap<String, String>();
      data.put("class", elem.getClassName());
      data.put("method", elem.getMethodName());
      data.put("file", elem.getFileName());
      data.put("line", String.valueOf(elem.getLineNumber()));
      stack.add(data);
    }
    /*
     * The same block again if we are still at x = 0
     * This block is only executed if the exception originates from the Logger class!
     */
    if (x == 0) {
      for (StackTraceElement elem : source.getStackTrace()) {
        ++x;
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("class", elem.getClassName());
        data.put("method", elem.getMethodName());
        data.put("file", elem.getFileName());
        data.put("line", String.valueOf(elem.getLineNumber()));
        stack.add(data);
      }
    }
    sourceStack = stack;
  }
}
