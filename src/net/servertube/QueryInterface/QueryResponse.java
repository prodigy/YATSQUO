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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;

/**
 *
 * @author Sebastian "prodigy" G.
 */
public class QueryResponse implements Iterable<HashMap<String, String>> {

  private final List<HashMap<String, String>> response;

  public QueryResponse(List<HashMap<String, String>> response) {
    this.response = Collections.unmodifiableList(response);
  }

  public List<HashMap<String, String>> getResponse() {
    return this.response;
  }

  /*public HashMap<String, String> getFirstResponse() {
    return this.response.get(0);
  }*/

  public HashMap<String, String> getErrorResponse() {
    return this.response.get(0);
  }

  public List<HashMap<String, String>> getDataResponse() {
    return this.response.subList(1, this.response.size());
  }

  /*public HashMap<String, String> getResponseByID(int id) {
    return this.response.get(id);
  }*/

  public boolean hasError() {
    return (this.response.get(0).get("id").equals("0"))?false:true;
  }

  @Override
  public Iterator<HashMap<String, String>> iterator() {
    return this.response.iterator();
  }

  @Override
  public String toString() {
    if (response == null) {
      return null;
    }

    String responseString = "QueryResponse {";
    for (Map<String, String> section : response) {
      responseString += "  {\n";
      for (Entry<String, String> entry : section.entrySet()) {
        responseString += "    - '" + entry.getKey() + "' = '" + entry.getValue() + "'\n";
      }
      responseString += "  }\n";
    }
    responseString += "}\n";
    return responseString;
  }

}
