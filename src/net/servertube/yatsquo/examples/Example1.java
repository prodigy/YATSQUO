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

import net.servertube.yatsquo.Channel;
import net.servertube.yatsquo.QueryException;
import net.servertube.yatsquo.QueryInterface;
import net.servertube.yatsquo.Server;

/**
 * QueryInterface - Example1<br />
 * <br />
 * Establishing a connection and priting out every Server with<br />
 * its Channels and Subchannels (not-sorted).
 * @author Sebastian "prodigy" G.
 */
public class Example1 {

  private static String user = "serveradmin";
  private static String passwd = "PASSWORD";
  private static String ip = "127.0.0.1";
  private static int port = 10011;

  /**
   * @see Example1
   * @param args
   * @throws QueryException
   */
  public static void main(String[] args) throws QueryException {
    QueryInterface qi = new QueryInterface();
    // connect to the given ip : port
    qi.connect(ip, port);
    // login using given user and passwd
    qi.login(user, passwd);
    // populate the internal server list
    qi.fillServerList();
    // iterate through all servers available
    for (Server s : qi.getServers()) {
      // print out the ID and name of the server
      System.out.println("Server (" + s.getID() + "): '" + s.getName() + "'");
      for (Channel c : s.getChannels()) {
        // print out ID and name of the channels belonging to Server s
        System.out.println(" - Channel (" + c.getID() + "): " + c.getName());
      }
    }
  }
}
