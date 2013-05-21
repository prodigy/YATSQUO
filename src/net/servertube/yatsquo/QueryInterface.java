/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
 *
 * QueryInterface.java - 2012-08-29
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
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public class QueryInterface {

  private String ip = null;
  private Integer port = null;
  private String user = null;
  private String pass = null;
  private List<Server> servers = new ArrayList<Server>();
  /**
   * currently selected server ID
   */
  protected int serverID = -1;
  /**
   * channel the query client is in
   */
  protected int channelID = -1;
  /**
   * the query clients ID
   */
  protected int clientID = -1;
  /**
   * registered query listeners
   */
  protected List<QueryListener> listeners;
  /**
   * The query connection
   */
  public QueryConnection qCon = null;

  /**
   * Creates a new QueryInterface object with given parameters.
   *
   * @param ip IP of the query port
   * @param port Port of the query port
   * @param user username to login with
   * @param passwd password to login with
   */
  public QueryInterface(String ip, int port, String user, String passwd) throws QueryException {
    initialize(ip, port, user, passwd);
  }

  /**
   * Initializes the QueryInterface object, moved out of constructor in case<br />
   * constructor is overriden
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
   * registers a new query listener.
   *
   * @param ql
   */
  public void registerQueryListener(QueryListener ql) {
    listeners.add(ql);
  }

  /**
   * unregisters a registered query listener
   *
   * @param ql
   */
  public void unregisterQueryListener(QueryListener ql) {
    listeners.remove(ql);
  }

  /**
   * gets a list of all registered query listeners
   *
   * @return
   */
  public List<QueryListener> getQueryListeners() {
    return listeners;
  }

  /**
   * returns a query response with who i am
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
   * fills in the server list
   *
   * @return @throws QueryException
   */
  protected boolean fillServerList() throws QueryException {
    QueryResponse qr = this.qCon.executeCommand(new QueryCommand("serverlist"));
    if (qr.hasError()) {
      return false;
    }

    for (HashMap<String, String> server : qr.getDataResponse()) {
      Server s = new Server(server.get("virtualserver_id"), this);
    }

    return true;
  }

  /**
   * returns a server object by given id
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
   * returns a server object by given port
   *
   * @param port
   * @return
   * @throws QueryException
   */
  public Server getServerByPort(int port) throws QueryException {
    for (Server s : servers) {
      if (s.getPort() == port) {
        System.out.println("got server from internal list");
        return s;
      }
    }
    QueryResponse qr = this.qCon.executeCommand(new QueryCommand("serveridgetbyport").param("virtualserver_port", port));
    if (!qr.hasError()) {
      for (HashMap<String, String> server : qr.getDataResponse()) {
        if (server.get("server_id") != null) {
          Server s = new Server(server.get("server_id"), this);
          System.out.println("fetched data from query port");
          return s;
        }
      }
    }
    return null;
  }

  /**
   * gets a list of all registered servers
   *
   * @return
   */
  public List<Server> getServers() {
    return servers;
  }

  /**
   * registers a server to the list
   *
   * @param server
   */
  protected void registerServer(Server server) {
    this.servers.add(server);
  }
}
