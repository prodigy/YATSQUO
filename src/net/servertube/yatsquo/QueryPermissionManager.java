/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
 *
 * QueryPermissionManager.java - 2012-08-29
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

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public class QueryPermissionManager {

  private HashMap<Integer, String> permList;
  private QueryInterface queryInterface;

  public QueryPermissionManager(QueryInterface queryInterface) {
    this.queryInterface = queryInterface;
    this.permList = new HashMap<Integer, String>();
  }

  public void addPermission(int id, String name) {
    permList.put(id, name);
  }

  public String getPermByID(int id) {
    return permList.get(id);
  }

  public Integer getPermByName(String name) {
    for(Entry<Integer, String> entry : permList.entrySet()) {
      if(entry.getValue().equals(name)) {
        return entry.getKey();
      }
    }
    return null;
  }
}
