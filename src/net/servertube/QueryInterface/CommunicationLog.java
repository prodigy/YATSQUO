/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
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
package net.servertube.QueryInterface;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian "prodigy" G.
 */
public class CommunicationLog {
  private static PrintStream cLogOut;
  private static SimpleDateFormat dateFormat;

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
    } catch (FileNotFoundException ex) {
      Logger.getLogger(CommunicationLog.class.getName()).log(Level.SEVERE, null, ex);
    } catch (UnsupportedEncodingException ex) {
      Logger.getLogger(CommunicationLog.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }

  /**
   *
   * @param msg
   * @param outbound
   */
  public static void log(String msg, boolean outbound) {
    if(cLogOut == null) {
      CommunicationLog.openLog("comm.log");
    }
    if(dateFormat == null) {
      CommunicationLog.setupDateFormat(null);
    }
    Date now = Calendar.getInstance().getTime();
    cLogOut.println(dateFormat.format(now) + " :: " + (outbound?"> ":"< ") + msg);
    //System.out.println(dateFormat.format(now) + " :: " + (outbound?"> ":"< ") + msg);
  }
}
