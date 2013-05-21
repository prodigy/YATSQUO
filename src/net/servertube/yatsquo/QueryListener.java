/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
 *
 * QueryListener.java - 2012-08-29
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

import net.servertube.yatsquo.Data.EventType;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.servertube.yatsquo.Data.ErrorCodes;

/**
 * Class needed in order to receive events raised by the TS3 Server
 *
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public abstract class QueryListener implements QueryEventExecutioner {

  private List<EventType> events = null;
  private QueryInterface qi = null;
  private BufferedReader in = null;
  private Timer eventTimer = null;
  private TimerTask eventTimerTask = null;
  private Integer serverID = null;
  private QueryConnection qCon = null;

  /**
   * creates a new query listener, which connects to given ip and port<br />
   * logs in with given user and password, and selects given server id
   *
   * @param ip
   * @param user
   * @param port
   * @param pass
   * @param serverID
   * @throws QueryException
   */
  public QueryListener(String ip, Integer port, String user, String pass, Integer serverID) throws QueryException {
    this.events = new ArrayList<EventType>();
    this.qCon = new QueryConnection(ip, port, user, pass);
    this.in = this.qCon.getInStream();
    this.qCon.selectVirtualserver(serverID);
  }

  public QueryListener(QueryInterface queryInterface, Integer serverID) throws QueryException {
    this(queryInterface.qCon.getIp(), queryInterface.qCon.getPort(),
            queryInterface.qCon.getUser(), queryInterface.qCon.getPass(),
            serverID);
  }

  /**
   * disables this event listener
   *
   * @param clearRegisteredEvents
   */
  public void disableEventListener(boolean clearRegisteredEvents) {
    if (eventTimer != null) {
      eventTimer.cancel();
      eventTimer.purge();
    }
    eventTimer = null;
    if (eventTimerTask != null) {
      eventTimerTask.cancel();
    }
    eventTimerTask = null;
    if (clearRegisteredEvents == true) {
      events.clear();
    }
  }

  /**
   * enables this event listener
   */
  public void enableEventListener() {
    if (eventTimer != null || eventTimerTask != null) {
      this.disableEventListener(false);
    }
    eventTimer = new Timer(true);
    if (eventTimerTask == null) {
      eventTimerTask = new eventTimerTask();
      eventTimer.schedule(eventTimerTask, 200, 200);
    }
  }

  /**
   *
   * @param line
   * @return
   * @throws QueryException
   */
  private boolean handleEvent(String line) throws QueryException {
    if (line.startsWith("notify")) {
      StringTokenizer tkn = new StringTokenizer(line, " ");
      if (tkn.countTokens() > 1) {
        String eventType = tkn.nextToken();
        String subType = tkn.nextToken();
        EventType event = EventType.getTypeBySubType(subType);
        if (event == null) {
          event = EventType.getTypeByEventID(eventType);
        }
        if (event == null) {
          throw new QueryException(ErrorCodes.EVENT_UNKNOWN, eventType.toString());
        }
        boolean isHandled = false;
        if (event != null) {
          for (EventType _event : events) {
            if (_event.getEvent().equals(event.getEvent())) {
              isHandled = true;
              break;
            }
          }
        }
        if (isHandled) {
          executeEvent(event, QueryTools._parseInput(line.substring(line.indexOf(" ") + 1)));
        }
      }
      return true;
    }
    return false;
  }

  /**
   * registers an event to listen for (servernotifyregister)
   *
   * @param type
   * @param params
   * @return success
   * @throws QueryException
   */
  public final boolean registerEvent(EventType type, HashMap<String, Object> params) throws QueryException {
    if (!events.contains(type)) {
      List<String> paramList = type.getParameters();
      HashMap<String, Object> paramsFinal = new HashMap<String, Object>();
      for (String param : paramList) {
        if (param.equals("event")) {
          paramsFinal.put(param, type.getNotifyType());
          continue;
        }
        if (params != null) {
          if (params.containsKey(param)) {
            paramsFinal.put(param, params.get(param));
          }
        }
      }
      QueryResponse qr = this.qCon.executeCommand(new QueryCommand("servernotifyregister", paramsFinal));
      if (!qr.hasError()) {
        events.add(type);
        return true;
      }
    }
    return false;
  }

  /**
   * unregisters an event
   *
   * @param type
   * @return success
   */
  protected final boolean unregisterEvent(EventType type) {
    if (events.contains(type)) {
      events.remove(type);
      return true;
    }
    return false;
  }

  /**
   *
   * @throws QueryException
   */
  private synchronized void eventCheck() throws QueryException {
    try {
      if (this.in.ready()) {
        String inLine = in.readLine();
        if (!inLine.isEmpty()) {
          CommunicationLog.log(inLine, false);
          this.handleEvent(inLine);
        }
      }
    } catch (IOException ex) {
      throw new QueryException(ErrorCodes.CONNECTION_SOCKET_READ_ERROR);
    } catch (Exception ex) {
      throw new QueryException(ErrorCodes.CONNECTION_SOCKET_READ_ERROR);
    }
    try {
      this.wait(100);
    } catch (InterruptedException ex) {
    }
  }

  /**
   *
   */
  private class eventTimerTask extends TimerTask {

    /**
     *
     */
    @Override
    public void run() {
      try {
        eventCheck();
      } catch (QueryException ex) {
        Logger.getLogger(QueryListener.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
}
