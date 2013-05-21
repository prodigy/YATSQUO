/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
 *
 * QueryException.java - 2012-08-29
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

import java.util.ArrayList;
import java.util.HashMap;
import net.servertube.yatsquo.Data.ErrorCodes;
import net.servertube.yatsquo.Data.QueryError;

/**
 *
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public class QueryException extends Throwable {

  /**
   * YATSQUO ErrorCode holder
   */
  public final ErrorCodes errorCode;
  /**
   *
   */
  public final QueryError queryError;
  /**
   * A list of the current source stack.
   */
  private ArrayList<HashMap<String, String>> sourceStack = null;

  /**
   *
   */
  public QueryException() {
    super("ok");
    this.errorCode = ErrorCodes.ERRORCODE_NO_ERROR;
    this.queryError = new QueryError(0, "ok", null, 0);
    fillSourceStack();
  }

  /**
   *
   * @param errorCode
   */
  public QueryException(ErrorCodes errorCode) {
    super();
    this.errorCode = errorCode;
    this.queryError = null;
    fillSourceStack();
  }

  /**
   *
   * @param errorCode
   * @param message
   */
  public QueryException(ErrorCodes errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
    this.queryError = null;
    fillSourceStack();
  }

  /**
   *
   * @param ID
   * @param msg
   * @param extra_msg
   * @param failed_permid
   */
  public QueryException(int ID, String msg, String extra_msg, int failed_permid) {
    super(msg);
    this.errorCode = ErrorCodes.ERRORCODE_TS3_ERROR;
    this.queryError = new QueryError(ID, msg, extra_msg, failed_permid);
    fillSourceStack();
  }

  /**
   *
   * @param message
   */
  public QueryException(String message) {
    super(message);
    this.errorCode = ErrorCodes.ERRORCODE_CUSTOM_ERROR;
    this.queryError = null;
    fillSourceStack();
  }

  /**
   *
   * @param message
   * @param data
   */
  public QueryException(String message, HashMap<String, String> data) {
    super(message);
    this.errorCode = ErrorCodes.ERRORCODE_TS3_ERROR;
    int ID = Integer.valueOf(data.get("id"));
    String msg = data.get("msg");
    String extra_msg = "";
    if (data.get("extra_msg") != null) {
      extra_msg = data.get("extra_msg");
    }
    int failed_permid = 0;
    if (data.get("failed_permid") != null) {
      failed_permid = Integer.valueOf(data.get("failed_permid"));
    }
    this.queryError = new QueryError(ID, msg, extra_msg, failed_permid);
    fillSourceStack();
  }

  /**
   *
   * @param message
   * @param data
   */
  public QueryException(ErrorCodes errorCode, HashMap<String, String> data) {
    super();
    this.errorCode = errorCode;
    int ID = Integer.valueOf(data.get("id"));
    String msg = data.get("msg");
    String extra_msg = "";
    if (data.get("extra_msg") != null) {
      extra_msg = data.get("extra_msg");
    }
    int failed_permid = 0;
    if (data.get("failed_permid") != null) {
      failed_permid = Integer.valueOf(data.get("failed_permid"));
    }
    this.queryError = new QueryError(ID, msg, extra_msg, failed_permid);
    fillSourceStack();
  }

  /**
   *
   * @param message
   * @param ID
   * @param extra_msg
   * @param failed_permid
   */
  public QueryException(String message, int ID, String extra_msg, int failed_permid) {
    super(message);
    this.errorCode = ErrorCodes.ERRORCODE_CUSTOM_ERROR;
    this.queryError = new QueryError(ID, message, extra_msg, failed_permid);
    fillSourceStack();
  }

  /**
   *
   * @param message
   * @param cause
   */
  public QueryException(String message, Throwable cause) {
    super(message, cause);
    this.errorCode = ErrorCodes.ERRORCODE_CUSTOM_ERROR;
    this.queryError = null;
    fillSourceStack();
  }

  /**
   *
   * @param message
   * @param cause
   * @param ID
   * @param extra_msg
   * @param failed_permid
   */
  public QueryException(String message, Throwable cause, int ID, String extra_msg, int failed_permid) {
    super(message, cause);
    this.errorCode = ErrorCodes.ERRORCODE_CUSTOM_ERROR;
    this.queryError = new QueryError(ID, message, extra_msg, failed_permid);
    fillSourceStack();
  }

  /**
   *
   * @param cause
   */
  public QueryException(Throwable cause) {
    super(cause);
    this.errorCode = ErrorCodes.ERRORCODE_CUSTOM_ERROR;
    this.queryError = null;
    fillSourceStack();
  }

  /**
   *
   * @param cause
   * @param ID
   * @param extra_msg
   * @param failed_permid
   */
  public QueryException(Throwable cause, int ID, String extra_msg, int failed_permid) {
    super(cause);
    this.errorCode = ErrorCodes.ERRORCODE_CUSTOM_ERROR;
    this.queryError = new QueryError(ID, extra_msg, extra_msg, failed_permid);
    fillSourceStack();
  }

  /**
   * returns the exception as a string
   * @return
   */
  @Override
  public String getMessage() {
    String out = "QueryException AT " + sourceStack.get(0).get("class")
            + "." + sourceStack.get(0).get("method")
            + " @ " + sourceStack.get(0).get("file")
            + ":" + sourceStack.get(0).get("line") + "\n";
    if(errorCode != null) {
      out += this.errorCode.toString() + "\n";
    }
    if(queryError != null) {
      out += this.queryError.toString() + "\n";
    }
    if(super.getMessage() != null && !super.getMessage().isEmpty()) {
      out += "Message: " + super.getMessage();
    }
    return out;
  }

  /**
   * Fills the sourceStack with the relevant information.<br /> This data can be
   * displayed when the message is displayed.
   */
  private void fillSourceStack() {
    Throwable source = new Throwable();
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
