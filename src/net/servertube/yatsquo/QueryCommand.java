/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
 *
 * QueryCommand.java - 2012-08-29
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public class QueryCommand {

  private String command;
  private HashMap<String, Object> parameters;
  private List<String> noValueParams;
  private List<String> options;

  /**
   * command without any params
   *
   * @param command
   */
  public QueryCommand(String command) {
    this.command = command;
    this.parameters = new HashMap<String, Object>();
    this.noValueParams = new ArrayList<String>();
    this.options = new ArrayList<String>();
  }

  /**
   * command with params (key/value pairs)
   *
   * @param command
   * @param parameters
   */
  public QueryCommand(String command, HashMap<String, Object> parameters) {
    this.command = command;
    this.parameters = parameters;
    this.noValueParams = new ArrayList<String>();
    this.options = new ArrayList<String>();
  }

  /**
   * command with params (key/value pairs) and options (only keys without
   * =value)
   *
   * @param command
   * @param parameters
   * @param options
   */
  public QueryCommand(String command, HashMap<String, Object> parameters, List<String> options) {
    this.command = command;
    this.parameters = parameters;
    this.noValueParams = new ArrayList<String>();
    this.options = options;
  }

  /**
   * adds a single parameter
   *
   * @param key
   * @param value
   * @return
   */
  public QueryCommand param(String key, Object value) {
    if (key == null) {
      return this;
    }
    parameters.put(key, value);
    return this;
  }

  /**
   * adds a parameter without value
   *
   * @param key
   * @return
   */
  public QueryCommand param(String key) {
    return this.param(key, null);
  }

  /**
   * adds multiple parameters from HashMap
   *
   * @param params
   * @return
   */
  public QueryCommand param(HashMap<String, Object> params) {
    if (params.isEmpty()) {
      return this;
    }
    parameters.putAll(params);
    return this;
  }

  /**
   * adds multiple params without value
   *
   * @param param
   * @return
   */
  public QueryCommand noValParam(String... param) {
    if (param == null) {
      return this;
    }
    Collections.addAll(this.noValueParams, param);
    return this;
  }

  /**
   * adds multiple options
   *
   * @param options
   * @return
   */
  public QueryCommand option(String... options) {
    if (options == null) {
      return this;
    }
    Collections.addAll(this.options, options);
    return this;
  }

  /**
   * returns the commands as string to send to query
   *
   * @return
   */
  @Override
  public String toString() {
    String cmdString = this.command;
    for (Entry<String, Object> param : this.parameters.entrySet()) {
      String key = param.getKey();
      Object value = param.getValue();

      if (value.getClass().isArray()) {
        for (int i = 0; i < Array.getLength(value); ++i) {
          String v = null;
          if (Array.get(value, i).getClass().equals(Boolean.class)) {
            v = QueryTools.booleanAsIntString((Boolean) Array.get(value, i));
          } else {
            v = String.valueOf(Array.get(value, i));
          }
          cmdString += (i == 0 ? " " : "|") + key + "=" + QueryTools.encode(v);
        }
      } else {
        String v = null;
        if (value.getClass().equals(Boolean.class)) {
          v = QueryTools.booleanAsIntString((Boolean) value);
        } else {
          v = String.valueOf(value);
        }
        cmdString += " " + key + "=" + QueryTools.encode(v);
      }
    }

    for (String novalParam : this.noValueParams) {
      cmdString += " " + novalParam;
    }

    for (String option : this.options) {
      cmdString += " -" + option;
    }

    return cmdString;
  }
}
