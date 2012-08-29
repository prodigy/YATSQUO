package net.servertube.QueryInterface;

import java.util.HashMap;

/**
 *
 * @author Sebastian "prodigy" G.
 */
public class QueryException extends Throwable {

  /**
   * 
   */
  public final int ID;
  /**
   * 
   */
  public final String msg;
  /**
   * 
   */
  public final String extra_msg;
  /**
   * 
   */
  public final int failed_permid;

  /**
   * 
   */
  public QueryException() {
    super();
    ID = 0;
    msg = "";
    extra_msg = "";
    failed_permid = 0;
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
    this.ID = ID;
    this.msg = msg;
    this.extra_msg = extra_msg;
    this.failed_permid = failed_permid;
  }

  /**
   * 
   * @param message
   */
  public QueryException(String message) {
    super(message);
    ID = 0;
    msg = "";
    extra_msg = "";
    failed_permid = 0;
  }

  /**
   * 
   * @param message
   * @param data
   */
  public QueryException(String message, HashMap<String, String> data) {
    super(message);
    ID = Integer.valueOf(data.get("id"));
    msg = data.get("msg");
    if (data.get("extra_msg") != null) {
      extra_msg = data.get("extra_msg");
    } else {
      extra_msg = "";
    }
    if (data.get("failed_permid") != null) {
      failed_permid = Integer.valueOf(data.get("failed_permid"));
    } else {
      failed_permid = 0;
    }
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
    this.ID = ID;
    msg = "";
    this.extra_msg = extra_msg;
    this.failed_permid = failed_permid;
  }

  /**
   * 
   * @param message
   * @param cause
   */
  public QueryException(String message, Throwable cause) {
    super(message, cause);
    ID = 0;
    msg = "";
    extra_msg = "";
    failed_permid = 0;
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
    this.ID = ID;
    msg = "";
    this.extra_msg = extra_msg;
    this.failed_permid = failed_permid;
  }

  /**
   * 
   * @param cause
   */
  public QueryException(Throwable cause) {
    super(cause);
    ID = 0;
    msg = "";
    extra_msg = "";
    failed_permid = 0;
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
    this.ID = ID;
    msg = "";
    this.extra_msg = extra_msg;
    this.failed_permid = failed_permid;
  }

  @Override
  public String getMessage() {
    return super.getMessage() + " | id: " + ID +" | msg: " + msg + (extra_msg!=null&&!extra_msg.isEmpty()?" | extra_msg: " + extra_msg:"") + (failed_permid!=0?" | failed_permid: " + failed_permid:"");
  }
}
