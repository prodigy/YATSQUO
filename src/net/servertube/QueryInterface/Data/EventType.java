/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.servertube.QueryInterface.Data;

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
