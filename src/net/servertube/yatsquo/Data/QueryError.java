/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
 *
 * QueryError.java - 2013-05-21
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
package net.servertube.yatsquo.Data;

/**
 *
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public class QueryError {

  public final int id;
  public final String msg;
  public final String extra_msg;
  public final int failed_permid;

  public QueryError(int id, String msg, String extra_msg, int failed_permid) {
    this.id = id;
    this.msg = msg;
    this.extra_msg = extra_msg;
    this.failed_permid = failed_permid;
  }

  public int getId() {
    return id;
  }

  public String getMsg() {
    return msg;
  }

  public String getExtra_msg() {
    return extra_msg;
  }

  public int getFailed_permid() {
    return failed_permid;
  }

  @Override
  public String toString() {
    return "QueryError (" + this.id + ") : " + this.msg +
            ((this.extra_msg!=null && !this.extra_msg.isEmpty())?
            " [ " + this.extra_msg + " ] ":"") + ((failed_permid!=0)?
            " (failed_permid: " + this.failed_permid + ")":"");
  }
}
