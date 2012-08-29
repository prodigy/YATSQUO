/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.servertube.QueryInterface.Data;

/**
 *
 * @author Basti
 */
public enum ClientListMode {

  CLIENT_LIST("clientlist"),
  DB_CLIENT_LIST("clientdblist");
  
  private String command;

  private ClientListMode(String command) {
    this.command = command;
  }

  public String getCommand() {
    return command;
  }
}
