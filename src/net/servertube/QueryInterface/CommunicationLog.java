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
