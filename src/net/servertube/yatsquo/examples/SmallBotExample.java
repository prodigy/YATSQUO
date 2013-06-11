/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
 *
 * SmallBotExample.java - 2013-06-03
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

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.servertube.yatsquo.Client;
import net.servertube.yatsquo.Data.EventType;
import net.servertube.yatsquo.QueryException;
import net.servertube.yatsquo.QueryInterface;
import net.servertube.yatsquo.QueryListener;
import net.servertube.yatsquo.Server;

/**
 *
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public class SmallBotExample {

  public static void main(String[] args) {
    try {
      args = new String[4];
      args[0] = "127.0.0.1";
      args[1] = "10011";
      args[2] = "serveradmin";
      args[3] = "p816yD1c";
      System.out.println("Connecting to " + args[0] + ":" + args[1] + "...");
      System.out.println("");
      System.out.println("Creating queryInterface...");
      final QueryInterface qi = new QueryInterface(args[0], Integer.valueOf(args[1]), args[2], args[3]);
      System.out.println("Creating Query Listener...");
      QueryListener ql;
      ql = new QueryListener(qi, qi.getServers().get(0).getID(), "YATSQUO - Listener") {
        @Override
        public synchronized void executeEvent(EventType type, HashMap<String, String> data) {
          if (type.equals(EventType.CLIENT_ENTERVIEW)) {
            try {
              Server s = qi.getServerByID(Integer.parseInt(data.get("YATSQUO_server_id")));
              Client c = s.getClientByID(Integer.parseInt(data.get("clid")));
              c.sendMessage("Welcome to the server!");
            } catch (Exception | QueryException ex) {
              System.err.println("Error while processing event");
              Logger.getLogger(SmallBotExample.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
          if (type.equals(EventType.EVENT_TEXTMESSAGE)) {
            try {
              if (data.get("msg").startsWith("!")) {
                Server s = qi.getServerByID(Integer.parseInt(data.get("YATSQUO_server_id")));
                Client c = s.getClientByID(Integer.parseInt(data.get("invokerid")));
                switch (data.get("msg")) {
                  case "!quit":
                    this.qCon.sendTextMessage(c, "I will do as you ask.");
                    this.qCon.sendTextMessage(s, "Bye bye!");
                    System.exit(0);
                    break;
                  case "!uptime":
                    int uptime = s.getUptime();
                    Double days, hours, min, sec = null;
                    days = Math.floor(uptime / 86400);
                    uptime -= days * 86400;
                    hours = Math.floor(uptime / 3600);
                    uptime -= hours * 3600;
                    min = Math.floor(uptime / 60);
                    uptime -= min * 60;
                    sec = Math.floor(uptime);
                    this.qCon.sendTextMessage(c, "Server uptime is " + days.intValue() + " days, " + hours.intValue() + " hours, " + min.intValue() + " minutes, " + sec.intValue() + " seconds.");
                }
              }
            } catch (QueryException ex) {
            }
          }
        }
      };
      qi.registerQueryListener(ql);
      //qi.changeQueryClientName("YATSQUO - Master");
      ql.registerEvent(EventType.REGISTER_TEXT_SERVER, null);
      ql.registerEvent(EventType.REGISTER_TEXT_CHANNEL, null);
      ql.registerEvent(EventType.REGISTER_TEXT_PRIVATE, null);
      ql.registerEvent(EventType.REGISTER_ENTER_VIEW, null);
      ql.registerEvent(EventType.REGISTER_LEFT_VIEW, null);
      ql.registerEvent(EventType.REGISTER_CLIENT_MOVED, null);
      ql.enableEventListener();
      while (true) {
        //nop
      }
    } catch (QueryException | Exception ex) {
      Logger.getLogger(SmallBotExample.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
