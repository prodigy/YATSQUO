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

import net.servertube.yatsquo.Data.EventType;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class needed in order to receive events raised by the TS3 Server
 *
 * @author Sebastian Grunow
 * @since 0.1a
 */
public abstract class QueryListener {

  private List<EventType> events = new ArrayList<EventType>();
  private QueryInterface qi = null;
  private BufferedReader in = null;
  private Timer eventTimer = null;
  private TimerTask eventTimerTask = null;
  private Integer serverID = null;
  private QueryConnection qCon = null;

  /**
   *
   * @param qi
   */
  public QueryListener(String ip, Integer port, String user, String pass, Integer serverID) throws QueryException {
    qCon = new QueryConnection(ip, port, user, pass);
    qCon.selectVirtualserver(serverID);
  }

  /**
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
   *
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
   * Executes the event raised giving the event type and data<br />
   * Override this function and create the QueryInterface<br />
   * with your own QueryListener as parameter.
   *
   * @param type
   * @param data
   */
  public abstract void executeEvent(EventType type, HashMap<String, String> data);

  /**
   *
   * @param line
   * @return
   */
  public boolean handleEvent(String line) {
    if (line.startsWith("notify")) {
      System.out.println("received notify: " + line);
      StringTokenizer tkn = new StringTokenizer(line, " ");
      if (tkn.countTokens() > 1) {
        String eventType = tkn.nextToken();
        String subType = tkn.nextToken();
        System.out.println("Event type received: " + eventType + "; subType: " + subType);
        EventType event = EventType.getTypeBySubType(subType);
        if (event == null) {
          event = EventType.getTypeByEventID(eventType);
        }
        if (event == null) {
          System.err.println("Unknwon event type: " + eventType);
        } else {
          System.out.println("Event type assigned: " + event.toString());
        }
        System.out.print("Events registered: ");
        for (EventType et : events) {
          System.out.print(et.toString() + ", ");
        }
        System.out.println();
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
        } else {
          System.out.println("Event not passed to executeEvent()! Event: " + eventType);
        }
      }
      return true;
    }
    return false;
  }

  /**
   *
   * @param type
   * @param params
   * @return
   * @throws QueryException
   */
  protected final boolean registerEvent(EventType type, HashMap<String, Object> params) throws QueryException {
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
   *
   * @param type
   * @return
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
   */
  public synchronized void eventCheck() {
    try {
      if (this.in.ready()) {
        String inLine = in.readLine();
        if (!inLine.isEmpty()) {
          CommunicationLog.log(inLine, false);
          this.handleEvent(inLine);
        }
      }
    } catch (IOException ex) {
      System.err.println("QueryListener: Error reading line from input stream");
      ex.printStackTrace();
    } catch (Exception ex) {
      System.err.println("QueryListener: Error while reading line from input stream");
      ex.printStackTrace();
    }
    try {
      this.wait(100);
    } catch (InterruptedException ex) {
    }
  }

  private class eventTimerTask extends TimerTask {

    @Override
    public void run() {
      eventCheck();
    }
  }
}
