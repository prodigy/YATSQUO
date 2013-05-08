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
package net.servertube.yatsquo.examples;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import net.servertube.yatsquo.Data.EventType;
import net.servertube.yatsquo.QueryException;
import net.servertube.yatsquo.QueryInterface;
import net.servertube.yatsquo.QueryListener;
import net.servertube.yatsquo.Server;

/**
 * QueryInterface - Example2<br />
 * <br />
 * Establish a connection and hook up an event caller<br />
 * The program quits when an event is received.
 * @author Sebastian "prodigy" G.
 */
public class Example2 {

  private static String user = "serveradmin";
  private static String passwd = "PASSWORD";
  private static String ip = "127.0.0.1";
  private static int port = 10011;

  private QueryInterface qi;
  protected Boolean running;

  /**
   * @see Example2
   * @param args
   * @throws QueryException
   */
  public static void main(String[] args) throws QueryException {
    Example2 ex2 = new Example2();
    ex2.showMe();
  }

  public Example2() {
    qi = new QueryInterface();
    running = true;
  }

  public void showMe() throws QueryException {
    // connect to the given ip : port
    qi.connect(ip, port);
    // register the QueryListener class defined below
    qi.registerQueryListener(new Listener(qi));
    // login using given user and passwd
    qi.login(user, passwd);
    // populate the internal server list
    qi.fillServerList();
    // get the first server in the list
    Server s = qi.getServers().get(0);
    // register all available events
    s.registerEvent(EventType.TEXT_SERVER, null);
    s.registerEvent(EventType.TEXT_CHANNEL, null);
    s.registerEvent(EventType.TEXT_PRIVATE, null);
    s.registerEvent(EventType.EVENT_SERVER, null);
    s.registerEvent(EventType.CLIENT_MOVED, null);
    // enable the event listener
    qi.getQueryListener().enableEventListener();
    // get the current server name
    String serverName = s.getName();
    String newName;
    while (running) {
      /*
       * While we are waiting for an event we change the server name in a loop
       * that breaks when we receive an event.
       * This demonstrates that the seperate thread of the QueryListener does
       * even work under heavy load of other commands. A situation like this
       * is very uncommon in a live environment. In a test on a local host I
       * got about 14 commands per second in such a loop. (With each name change
       * and the EVENT_SERVER registered a notifyserveredited event is passed
       * which is NOT catched by the library, yet; This increases traffic
       * and reduces possible commands per second)
       *
       * The library still passes commands and receives the correct replies while
       * the system waits for events on another thread. All events are received.
       */
      newName = new BigInteger(130, new SecureRandom()).toString(32).substring(0, 8);
      s.setName(newName);
    }
    // reset the server name to what we got before
    s.setName(serverName);
  }

  protected class Listener extends QueryListener {

    public Listener(QueryInterface qi) {
      super(qi);
    }

    /*
     * Override the abstract function executeEvent with our own testing function<br />
     * This function simply displays all data received with the event and disables<br />
     * the name-changing-loop in our main class.<br />
     */
    @Override
    public void executeEvent(EventType type, HashMap<String, String> data) {
      System.out.println("** EVENT RAISED **");
      System.out.println(" - Type: " + type.toString());
      System.out.println(" - Data {");
      for (String key : data.keySet()) {
        System.out.println("    " + key + " = " + data.get(key));
      }
      System.out.println("   }");
      running = false;
    }
  }
}
