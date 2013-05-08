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
package net.servertube.yatsquo.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Basti
 */
public enum EventType {

  /**
   *
   */
  CLIENT_ENTERVIEW("notifycliententerview", null, null),
  /**
   *
   */
  CLIENT_LEFTVIEW("notifyclientleftview", null, null),
  /**
   *
   */
  EVENT_SERVER("notifycliententerview", "server", new ArrayList<String>() {

{
add("event");
}
}),
  /**
   *
   */
  CLIENT_MOVED("notifyclientmoved", "channel", new ArrayList<String>() {

{
add("event");
add("id");
}
}),
  /**
   *
   */
  EVENT_TEXTMESSAGE("notifytextmessage", null, null),
  /**
   *
   */
  TEXT_SERVER("notifytextmessage", "textserver", new ArrayList<String>() {

{
add("event");
}
}),
  /**
   *
   */
  TEXT_CHANNEL("notifytextmessage", "textchannel", new ArrayList<String>() {

{
add("event");
}
}),
  /**
   *
   */
  TEXT_PRIVATE("notifytextmessage", "textprivate", new ArrayList<String>() {

{
add("event");
}
});
  private final String event;
  private final String notify_type;
  private final List<String> parameters;

  private EventType(String event, String notify_type, List<String> params) {
    this.event = event;
    this.notify_type = notify_type;
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
  public String getNotifyType() {
    return notify_type;
  }

  /**
   *
   * @return
   */
  public List<String> getParameters() {
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
  public static EventType getTypeBySubType(String subType) {
    if (subType != null) {
      for (EventType ev : EventType.values()) {
        if (ev.notify_type == null) {
          continue;
        }
        if (ev.notify_type.equals(subType)) {
          return ev;
        }
      }
    }
    return null;
  }
}
