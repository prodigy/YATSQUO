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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 *
 * @author Sebastian "prodigy" G.
 */
public class QueryInterface {

  private BufferedReader in = null;
  private PrintStream out = null;
  private Socket socket = null;
  private boolean bConnected = false;
  /**
   *
   */
  protected int iServerID = -1;
  /**
   *
   */
  protected int iChannelID = -1;
  /**
   *
   */
  protected int iClientID = -1;
  private List<Server> servers = new ArrayList<Server>();
  //private List qList = new ArrayList<QueryListener>();
  /**
   *
   */
  protected QueryListener queryListener;

  /**
   *
   */
  public QueryInterface() {
  }

  /**
   *
   * @return
   */
  public boolean isConnected() {
    //return bConnected;
    return this.socket.isConnected();
  }

  /**
   *
   * @return
   */
  public BufferedReader getInStream() {
    return in;
  }

  /**
   *
   * @param ql
   */
  public void registerQueryListener(QueryListener ql) {
    //qList.add(ql);
    queryListener = ql;
  }

  /**
   *
   * @return
   */
  public QueryListener getQueryListener() {
    return queryListener;
  }

  /**
   *
   * @param ip
   * @param port
   * @return
   * @throws QueryException
   */
  public boolean connect(String ip, int port) throws QueryException {
    if (socket != null) {
      throw new QueryException("Connection still open!");
    }
    try {
      socket = new Socket(ip, port);
    } catch (Exception ex) {
      socket = null;
      throw new QueryException("Unable to open socket: " + ex.getMessage());
    }

    if (socket.isConnected()) {
      try {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        out = new PrintStream(socket.getOutputStream(), true, "UTF-8");

        socket.setSoTimeout(5000);

        String ident = in.readLine();
        CommunicationLog.log(ident, false);
        if (ident.equals("TS3")) {
          socket.setSoTimeout(500);
          try {
            while (true) {
              CommunicationLog.log(in.readLine(), false);
            }
          } catch (Exception ex) {
          }
          bConnected = true;
        } else {
          closeConnection();
          throw new QueryException("No TS3 query port detected!");
        }
      } catch (Exception ex) {
        socket = null;
        throw new QueryException("Error while connecting: " + ex.getMessage());
      }
    } else {
      try {
        socket.close();
      } catch (Exception ex) {
      }
      socket = null;
      throw new QueryException("Error connecting to Query Port (unknown)!");
    }
    return false;
  }

  /**
   *
   * @return
   * @throws QueryException
   */
  public boolean closeConnection() throws QueryException {
    if (queryListener != null) {
      queryListener.disableEventListener(true);
    }
    bConnected = false;
    this.executeCommand("quit");
    try {
      socket.close();
    } catch (IOException ex) {
      System.err.println("Unknown error while closing connection");
    } finally {
      socket = null;
    }
    return true;
  }

  /**
   *
   * @param command
   * @return
   * @throws QueryException
   */
  public QueryResponse executeCommand(String command) throws QueryException {
    if(!this.isConnected()) {
      throw new QueryException("Command failed: No active connection!");
    }
    CommunicationLog.log(command, true);
    this.out.println(command);
    return this.readLine();
  }

  /**
   *
   * @param command
   * @return
   * @throws QueryException
   */
  public QueryResponse executeCommand(QueryCommand command) throws QueryException {
    if(!this.isConnected()) {
      throw new QueryException("Command failed: No active connection!");
    }
    CommunicationLog.log(command.toString(), true);
    this.out.println(command.toString());
    return this.readLine();
  }

  private QueryResponse readLine() throws QueryException {
    if (!this.isConnected()) {
      throw new QueryException("Cannot read from input stream: Not connected!");
    }

    if (queryListener != null) {
      queryListener.setEventListenerStatus(false);
    }
    String data = "";
    String tmp = "";
    while (true) {
      try {
        tmp = this.in.readLine();
        CommunicationLog.log(tmp, false);
      } catch (SocketTimeoutException ex) {
        throw new QueryException("Socket timed out while reading from input stream:\n" + ex.toString());
      } catch (SocketException ex) {
        throw new QueryException("Socket timed out while reading from input stream:\n" + ex.toString());
      } catch (Exception ex) {
        throw new QueryException("Error reading line from input Stream:\n" + ex.toString());
      }

      if (tmp == null) {
        this.closeConnection();
        throw new QueryException("NULL result from input stream; Connection lost?");
      }

      if (tmp.startsWith("error")) {
        break;
      }

      if (!tmp.isEmpty()) {
        boolean handleResult = false;
        if (this.queryListener != null) {
          handleResult = this.queryListener.handleEvent(tmp);
        }
        if (!handleResult) {
          if (!data.isEmpty()) {
            data += System.getProperty("line.seperator", "\n");
          }
          data += tmp;
        }
      }
    }
    if (queryListener != null) {
      queryListener.setEventListenerStatus(true);
    }

    List<HashMap<String, String>> inLHM = new ArrayList<HashMap<String, String>>();
    inLHM = QueryTools.parseInput(tmp);
    if (inLHM == null) {
      this.closeConnection();
      throw new QueryException("parsing returned NULL object; Connection lost?");
    }
    /*if(!inLHM.get(0).get("id").equals("0")) {
      throw new QueryException("Error while executing command!", inLHM.get(0));
    }*/
    List<HashMap<String, String>> dataList = QueryTools.parseInput(data);
    if (dataList != null) {
      inLHM.addAll(dataList);
    }
    return new QueryResponse(inLHM);
  }

  /**
   *
   * @param user
   * @param pass
   * @return
   * @throws QueryException
   */
  public boolean login(String user, String pass) throws QueryException {
    QueryResponse qr = this.executeCommand(new QueryCommand("login").noValParam(user, pass));
    /*List<HashMap<String, String>> response = this.executeCommand("login " + user + " " + pass);
    if (!isResponseOK(response)) {
      return false;
    }*/
    if(qr.hasError()) {
      return false;
    }

    return true;
  }

  /**
   *
   * @return
   * @throws QueryException
   */
  public HashMap<String, String> whoAmI() throws QueryException {
    QueryResponse qr = this.executeCommand(new QueryCommand("whoami"));
    if(qr.hasError()) {
      return null;
    }
    return qr.getDataResponse().get(0);
  }

  /**
   *
   * @param id
   * @return
   * @throws QueryException
   */
  public boolean selectVirtualserver(int id) throws QueryException {
    return selectVirtualserver(String.valueOf(id));
  }

  /**
   *
   * @param id
   * @return
   * @throws QueryException
   */
  public boolean selectVirtualserver(String id) throws QueryException {
    QueryResponse qr = this.executeCommand(new QueryCommand("use").noValParam(id.toString()));
    if (qr.hasError()) {
      return false;
    }
    return true;
  }

  /**
   *
   * @return
   * @throws QueryException
   */
  public boolean fillServerList() throws QueryException {
    QueryResponse qr = this.executeCommand(new QueryCommand("serverlist"));
    if (qr.hasError()) {
      return false;
    }

    for (HashMap<String, String> server : qr.getDataResponse()) {
      this.servers.add(new Server(server.get("virtualserver_id"), this));
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
    QueryResponse qr = this.executeCommand(new QueryCommand("serverlist"));
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
