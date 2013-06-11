/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
 *
 * EventType.java - 2012-08-29
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
package net.servertube.yatsquo.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public enum EventType {

  /**
   * Returned if a client enters the view
   */
  CLIENT_ENTERVIEW("notifycliententerview", null),
  /**
   * Returned if a client leaves the view
   */
  CLIENT_LEFTVIEW("notifyclientleftview", null),
  /**
   * Returned when a notifytextmessage event occurs.
   */
  EVENT_TEXTMESSAGE("notifytextmessage", null),
  /**
   * Returned when a notifyclientmoved event occurs.
   */
  EVENT_CLIENT_MOVED("notifyclientmoved", null),
  /**
   * Used to register the notifycliententerview event, possible paramter is
   * event.
   */
  REGISTER_ENTER_VIEW("notifycliententerview", new HashMap<String, String>() {
    {
      put("event", "server");
    }
  }),
  /**
   * Used to register the notifycliententerview event, possible paramter is
   * event.
   */
  REGISTER_LEFT_VIEW("notifyclientleftview", new HashMap<String, String>() {
    {
      put("event", "server");
    }
  }),
  /**
   * Used to register notifyclientmoved events, possible parameters are event
   * and id.
   */
  REGISTER_CLIENT_MOVED("notifyclientmoved", new HashMap<String, String>() {
    {
      put("event", "channel");
      put("id", null);
    }
  }),
  /**
   * Used to register text message events (server wide)
   */
  REGISTER_TEXT_SERVER("notifytextmessage", new HashMap<String, String>() {
    {
      put("event", "textserver");
    }
  }),
  /**
   * Used to register text message events (channel wide)
   */
  REGISTER_TEXT_CHANNEL("notifytextmessage", new HashMap<String, String>() {
    {
      put("event", "textchannel");
    }
  }),
  /**
   * Used to register text message events (private)
   */
  REGISTER_TEXT_PRIVATE("notifytextmessage", new HashMap<String, String>() {
    {
      put("event", "textprivate");
    }
  });
  /**
   * The event name the query expects
   */
  private final String event;
  /**
   * Additional parameters which need to be defined
   */
  private final HashMap<String, String> parameters;

  private EventType(String event, HashMap<String, String> params) {
    this.event = event;
    this.parameters = params;
  }

  /**
   *
   * @return
   */
  public String getEvent() {
    return event;
  }

  /**
   *
   * @return
   */
  public HashMap<String, String> getParameters() {
    return parameters;
  }

  /**
   *
   * @param event
   * @return
   */
  public static EventType getTypeByEventID(String event) {
    if (event != null) {
      for (EventType ev : EventType.values()) {
        if (ev.event.equals(event)) {
          return ev;
        }
      }
    }
    return null;
  }

  /**
   *
   * @param event
   * @return
   */
  public static EventType getTypeByParameter(String event, String key, String value) {
    if (key != null || value != null) {
      for (EventType ev : EventType.values()) {
        if (ev.event.equals(event)
                && ev.parameters != null
                && ev.parameters.containsKey(key)
                && ev.parameters.get(key).equals(value)) {
          return ev;
        }
      }
    }
    return null;
  }
}
