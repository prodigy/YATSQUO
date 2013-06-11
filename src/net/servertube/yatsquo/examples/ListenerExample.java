/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
 *
 * ListenerExample.java - 2013-00-21
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
import net.servertube.yatsquo.Data.EventType;
import net.servertube.yatsquo.Data.TextMessageTargetMode;
import net.servertube.yatsquo.QueryException;
import net.servertube.yatsquo.QueryInterface;
import net.servertube.yatsquo.QueryListener;

/**
 *
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public class ListenerExample {

  public static void main(String[] args) {
    try {
      System.out.println("Connecting to " + args[0] + ":" + args[1] + "...");
      System.out.println("");
      System.out.println("Creating queryInterface...");
      QueryInterface qi = new QueryInterface(args[0], Integer.valueOf(args[1]), args[2], args[3]);
      System.out.println("Creating Query Listener...");
      qi.qCon.sendTextMessage(qi.getServers().get(0), "Hello!");
      QueryListener ql = new QueryListener(qi, qi.getServers().get(0).getID()) {
        @Override
        public void executeEvent(EventType type, HashMap<String, String> data) {
          System.out.println("executeEvent(" + type.name() + ")");
          System.out.println("data:");
          for (String key : data.keySet()) {
            System.out.println("  " + key + " = " + data.get(key));
          }
        }
      };
      ql.registerEvent(EventType.REGISTER_TEXT_SERVER, null);
      ql.registerEvent(EventType.REGISTER_TEXT_CHANNEL, null);
      ql.registerEvent(EventType.REGISTER_TEXT_PRIVATE, null);
      ql.registerEvent(EventType.REGISTER_ENTER_VIEW, null);
      ql.registerEvent(EventType.REGISTER_LEFT_VIEW, null);
      ql.registerEvent(EventType.REGISTER_CLIENT_MOVED, null);
      ql.enableEventListener();
      while(true) {
        //nop
      }
    } catch (QueryException | Exception ex) {
      Logger.getLogger(ListenerExample.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
