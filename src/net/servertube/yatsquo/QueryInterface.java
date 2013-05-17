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
package net.servertube.yatsquo;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 *
 * @author Sebastian "prodigy" G.
 */
public class QueryInterface {

  /**
   *
   */
  private String ip = null;
  /**
   *
   */
  private Integer port = null;
  /**
   *
   */
  private String user = null;
  /**
   *
   */
  private String pass = null;
  /**
   *
   */
  public QueryConnection qCon = null;
  /**
   *
   */
  protected int serverID = -1;
  /**
   *
   */
  protected int channelID = -1;
  /**
   *
   */
  protected int clientID = -1;
  /**
   *
   */
  private List<Server> servers = new ArrayList<Server>();
  /**
   *
   */
  protected List<QueryListener> listeners;

  /**
   *
   */
  public QueryInterface() {
  }

  /**
   *
   * @param ip
   * @param port
   * @param user
   * @param passwd
   */
  public QueryInterface(String ip, int port, String user, String passwd) throws QueryException {
    initialize(ip, port, user, passwd);
  }

  /**
   *
   * @param ip
   * @param port
   * @param user
   * @param passwd
   * @throws QueryException
   */
  private void initialize(String ip, int port, String user, String passwd) throws QueryException {
    this.qCon = new QueryConnection(ip, port, user, passwd);
    this.fillServerList();
  }

  /**
   *
   * @param ql
   */
  public void registerQueryListener(QueryListener ql) {
    listeners.add(ql);
  }

  public void unregisterQueryListener(QueryListener ql) {
    listeners.remove(ql);
  }

  /**
   *
   * @return
   */
  public List<QueryListener> getQueryListeners() {
    return listeners;
  }

  /**
   *
   * @return @throws QueryException
   */
  public HashMap<String, String> whoAmI() throws QueryException {
    QueryResponse qr = this.qCon.executeCommand(new QueryCommand("whoami"));
    if (qr.hasError()) {
      return null;
    }
    return qr.getDataResponse().get(0);
  }

  /**
   *
   * @return @throws QueryException
   */
  protected boolean fillServerList() throws QueryException {
    QueryResponse qr = this.qCon.executeCommand(new QueryCommand("serverlist"));
    if (qr.hasError()) {
      return false;
    }

    for (HashMap<String, String> server : qr.getDataResponse()) {
      //this.servers.add(new Server(server.get("virtualserver_id"), this));
      Server s = new Server(server.get("virtualserver_id"), this);
    }

    return true;
  }

  /**
   *
   * @param id
   * @return
   * @throws QueryException
   */
  public Server getServerByID(Integer id) throws QueryException {
    for (Server s : servers) {
      if (s.getID() == id) {
        System.out.println("got server from internal list");
        return s;
      }
    }
    QueryResponse qr = this.qCon.executeCommand(new QueryCommand("serverlist"));
    if (!qr.hasError()) {
      for (HashMap<String, String> server : qr.getDataResponse()) {
        if (server.get("virtualserver_id").equals(id.toString())) {
          Server s = new Server(server.get("virtualserver_id"), this);
          servers.add(s);
          System.out.println("fetched data from query port");
          return s;
        }
      }
    }
    return null;
  }

  /**
   *
   * @return
   */
  public List<Server> getServers() {
    return servers;
  }

  /**
   *
   * @param server
   */
  protected void registerServer(Server server) {
    this.servers.add(server);
  }
}
