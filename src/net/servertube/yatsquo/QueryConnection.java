/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
 *
 * QueryConnection.java - 2013-05-17
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
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.servertube.yatsquo.Data.ErrorCodes;

/**
 *
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public class QueryConnection {

  private Socket socket = null;
  private String ip = null;
  private Integer port = null;
  private String user = null;
  private String pass = null;
  private BufferedReader in = null;
  private PrintStream out = null;
  private boolean bConnected = false;
  private int connectionErrors = 0;
  private Integer serverID = null;

  /**
   * initializes a new query connection to the given ip and port and<br />
   * tries to login using given username and password
   *
   * @param ip
   * @param port
   * @param user
   * @param pass
   * @throws QueryException
   */
  public QueryConnection(String ip, int port, String user, String pass) throws QueryException {
    this.ip = ip;
    this.port = port;
    this.user = user;
    this.pass = pass;
    initialize();
  }

  /**
   * initializes the connection (safe call in case constructor is overriden)
   *
   * @throws QueryException
   */
  private void initialize() throws QueryException {
    connect(ip, port);
    if ((user != null && !user.isEmpty()) || (pass != null && !pass.isEmpty())) {
      login(user, pass);
    }
  }

  /**
   *
   * @return
   */
  public boolean isConnected() {
    if (socket != null) {
      return socket.isConnected();
    } else {
      return false;
    }
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
   * @param ip
   * @param port
   * @return
   * @throws QueryException
   */
  protected boolean connect(String ip, int port) throws QueryException {
    if (socket != null) {
      throw new QueryException(ErrorCodes.CONNECTION_NOT_CONNECTED);
    }
    try {
      socket = new Socket(ip, port);
    } catch (Exception ex) {
      socket = null;
      throw new QueryException(ErrorCodes.CONNECTION_UNABLE_OPEN_SOCKET, ex.getMessage());
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
          return true;
        } else {
          closeConnection();
          throw new QueryException(ErrorCodes.CONNECTION_TS3_NOT_PRESENT);
        }
      } catch (Exception ex) {
        socket = null;
        throw new QueryException(ErrorCodes.CONNECTION_ERROR_CONNECTING, ex.getMessage());
      }
    } else {
      try {
        socket.close();
      } catch (Exception ex) {
      } finally {
        socket = null;
      }
    }
    return false;
  }

  /**
   *
   * @return @throws QueryException
   */
  public boolean closeConnection() throws QueryException {
    bConnected = false;
    this.executeCommand("quit");
    try {
      socket.close();
    } catch (IOException ex) {
      throw new QueryException(ErrorCodes.CONNECTION_CLOSING_FAILED, ex.getMessage());
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
  private QueryResponse readLine(String command) throws QueryException {
    if (!socket.isConnected()) {
      throw new QueryException(ErrorCodes.CONNECTION_NOT_CONNECTED);
    }

    String data = "";
    String tmp = "";
    while (true) {
      try {
        tmp = this.in.readLine();
        CommunicationLog.log(tmp, false);
      } catch (SocketTimeoutException | SocketException ex) {
        ++this.connectionErrors;
        if (this.connectionErrors > 3) {
          throw new QueryException(ErrorCodes.CONNECTION_SOCKET_READ_ERROR, ex.toString());
        }
        Logger.getLogger(QueryInterface.class.getName()).log(Level.WARNING, "Lost connection to TS3, attempting to reconnect");
        this.closeConnection();
        this.connect(ip, port);
        this.login(user, pass);
        this.selectVirtualserver(serverID);
        return this.executeCommand(command);
      } catch (Exception ex) {
        throw new QueryException(ErrorCodes.CONNECTION_SOCKET_READ_ERROR, ex.toString());
      }

      if (tmp == null) {
        this.closeConnection();
        throw new QueryException(ErrorCodes.CONNECTION_NULL_RESPONSE);
      }

      if (tmp.startsWith("error")) {
        break;
      }

      if (!tmp.isEmpty()) {
        boolean handleResult = false;
        if (!handleResult) {
          if (!data.isEmpty()) {
            data += System.getProperty("line.seperator", "\n");
          }
          data += tmp;
        }
      }
    }

    List<HashMap<String, String>> inLHM;
    inLHM = QueryTools.parseInput(tmp);
    if (inLHM == null) {
      this.closeConnection();
      throw new QueryException(ErrorCodes.PARSER_NULL_RESULT);
    }
    List<HashMap<String, String>> dataList = QueryTools.parseInput(data);
    if (dataList != null) {
      inLHM.addAll(dataList);
    }
    this.connectionErrors = 0;
    return new QueryResponse(inLHM);
  }

  /**
   *
   * @param user
   * @param pass
   * @return
   * @throws QueryException
   */
  protected boolean login(String user, String pass) throws QueryException {
    QueryResponse qr = this.executeCommand(new QueryCommand("login").noValParam(user, pass));
    if (qr.hasError()) {
      return false;
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
    if (!socket.isConnected()) {
      throw new QueryException(ErrorCodes.CONNECTION_NOT_CONNECTED);
    }
    CommunicationLog.log(command, true);
    this.out.println(command);
    return this.readLine(command);
  }

  /**
   *
   * @param command
   * @return
   * @throws QueryException
   */
  public QueryResponse executeCommand(QueryCommand command) throws QueryException {
    if (!socket.isConnected()) {
      throw new QueryException(ErrorCodes.CONNECTION_NOT_CONNECTED);
    }
    CommunicationLog.log(command.toString(), true);
    this.out.println(command.toString());
    return this.readLine(command.toString());
  }

  /**
   *
   * @param id
   * @return
   * @throws QueryException
   */
  public boolean selectVirtualserver(Integer id) throws QueryException {
    QueryResponse qr = this.executeCommand(new QueryCommand("use").noValParam(id.toString()));
    if (qr.hasError()) {
      return false;
    }
    this.serverID = id;
    return true;
  }

  /**
   *
   * @param id
   * @return
   * @throws QueryException
   */
  public boolean selectVirtualserver(String id) throws QueryException {
    return selectVirtualserver(Integer.valueOf(id));
  }

  protected String getIp() {
    return ip;
  }

  protected Integer getPort() {
    return port;
  }

  protected String getUser() {
    return user;
  }

  protected String getPass() {
    return pass;
  }
}
