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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.HashMap;

/**
 *
 * @author Sebastian "prodigy" G.
 */
public class QueryTools {

  /**
   *
   * @param str
   * @return
   */
  public static String encode(String str) {
    str = str.replace("\\", "\\\\");
    str = str.replace(" ", "\\s");
    str = str.replace("/", "\\/");
    str = str.replace("|", "\\p");
    str = str.replace("\b", "\\b");
    str = str.replace("\f", "\\f");
    str = str.replace("\n", "\\n");
    str = str.replace("\r", "\\r");
    str = str.replace("\t", "\\t");

    Character cBell = new Character((char) 7); // \a (not supported by Java)
    Character cTab = new Character((char) 11); // \v (not supported by Java)

    str = str.replace(cBell.toString(), "\\a");
    str = str.replace(cTab.toString(), "\\v");

    return str;
  }

  /**
   *
   * @param str
   * @return
   */
  public static String decode(String str) {
    str = str.replace("\\\\", "\\[CDATA]");
    str = str.replace("\\s", " ");
    str = str.replace("\\/", "/");
    str = str.replace("\\p", "|");
    str = str.replace("\\b", "\b");
    str = str.replace("\\f", "\f");
    str = str.replace("\\n", "\n");
    str = str.replace("\\r", "\r");
    str = str.replace("\\t", "\t");

    Character cBell = new Character((char) 7); // \a (not supported by Java)
    Character cTab = new Character((char) 11); // \v (not supported by Java)

    str = str.replace("\\a", cBell.toString());
    str = str.replace("\\v", cTab.toString());

    str = str.replace("\\[CDATA]", "\\");
    return str;
  }

  /**
   *
   * @param line
   * @return
   */
  public static List<HashMap<String, String>> parseInput(String line) {
    if (line == null || line.length() < 1) {
      return null;
    }
    StringTokenizer tkn = new StringTokenizer(line, "|", false);
    List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

    while (tkn.hasMoreTokens()) {
      data.add(_parseInput(tkn.nextToken()));
    }
    return data;
  }

  /**
   *
   * @param line
   * @return
   */
  public static HashMap<String, String> _parseInput(String line) {
    if (line == null || line.length() < 1) {
      return null;
    }
    StringTokenizer tkn = new StringTokenizer(line, " ", false);
    HashMap<String, String> data = new HashMap<String, String>();

    String s, t;
    int p = -1;

    while (tkn.hasMoreTokens()) {
      t = tkn.nextToken();
      p = t.indexOf("=");
      if (p > 0) {
        data.put(t.substring(0, p), decode(t.substring(p + 1)));
      } else {
        data.put(t, "");
      }
    }
    return data;
  }

  /**
   *
   * @param command
   * @param params
   * @return
   */
  public static String commandBuilder(String command, SortedMap<String, String> params) {
    for (String key : params.keySet()) {
      String value = params.get(key);
      if (value == null) {
        value = "";
      }
      command += " " + key + "=" + QueryTools.encode(value);
    }
    return command;
  }

  /**
   *
   * @param in
   * @return
   */
  public static Integer booleanAsInt(Boolean in) {
    if (in == null) {
      return null;
    }
    return (in ? 1 : 0);
  }

  /**
   *
   * @param in
   * @return
   */
  public static String booleanAsIntString(Boolean in) {
    if (in == null) {
      return null;
    }
    return (in ? "1" : "0");
  }

  public static <T> ArrayList<T> buildList(String items, Class<T> _class) {
    if (items == null || items.length() < 1) {
      return null;
    }
    StringTokenizer tkn = new StringTokenizer(items, ",", false);
    ArrayList<T> list = new ArrayList<T>();

    while (tkn.hasMoreTokens()) {
      String next = tkn.nextToken();
      if (_class.getSuperclass().getName().contains("Number")) {
        System.out.println("_class extending Number");
        if (_class.equals(Integer.class)) {
          list.add((T) Integer.valueOf(next));
        } else if (_class.equals(Long.class)) {
          list.add((T) Long.valueOf(next));
        } else if (_class.equals(Double.class)) {
          list.add((T) Double.valueOf(next));
        } else if (_class.equals(Float.class)) {
          list.add((T) Float.valueOf(next));
        }
      } else {
        list.add((T) next);
      }
    }
    return list;
  }
}
