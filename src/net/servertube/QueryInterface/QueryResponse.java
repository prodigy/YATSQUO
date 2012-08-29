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
