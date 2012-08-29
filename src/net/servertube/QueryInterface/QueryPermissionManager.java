package net.servertube.QueryInterface;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Sebastian "prodigy" G.
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
