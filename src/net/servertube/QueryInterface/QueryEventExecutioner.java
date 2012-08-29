/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.servertube.QueryInterface;

import net.servertube.QueryInterface.Data.EventType;
import java.util.HashMap;

/**
 *
 * @author Basti
 */
public interface QueryEventExecutioner {
  
  /**
   * Executes the event raised giving the event type and data
   * @param type
   * @param data
   */
    public void executeEvent(EventType type, HashMap<String, String> data);
}
