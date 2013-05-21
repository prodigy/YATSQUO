/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
 *
 * Server.java - 2012-08-29
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.servertube.yatsquo.Data.ErrorCodes;

/**
 *
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public class Server {

  /**
   * This has to be an <strong>existing, already connected</strong>
   * queryInterface!<br />
   * Usually set in the constructor!
   */
  protected QueryInterface queryInterface;
  private List<Channel> channels = new ArrayList<Channel>();
  private List<Client> clients = new ArrayList<Client>();
  private Integer ID;
  private Integer port;
  private Boolean online;
  private Integer maxclients;
  private Integer reserved_slots;
  private String name;
  private Boolean autostart;
  private String password;
  private String hostmessage;
  private Integer hostmessage_mode;
  private String welcomemessage;
  private String machine_id;
  private String hostbanner_url;
  private String hostbanner_gfx_url;
  private Integer hostbanner_gfx_interval;
  private String hostbutton_url;
  private String hostbutton_gfx_url;
  private Integer createdAt;
  private String version;
  private Integer uptime;
  private String platform;

  /**
   *
   * @param ID
   * @param port
   * @param online
   * @param maxclients
   * @param name
   * @param autostart
   * @param queryInterface
   */
  public Server(String name, int port, int maxclients, boolean autostart, QueryInterface queryInterface) {
    this.port = port;
    this.maxclients = maxclients;
    this.name = name;
    this.autostart = autostart;
    this.queryInterface = queryInterface;
  }

  /**
   *
   * @param ID
   * @param queryInterface
   */
  public Server(int ID, QueryInterface queryInterface) throws QueryException {
    this.ID = ID;
    this.queryInterface = queryInterface;
    this.fillServerInfo();
    this.registerServer();
  }

  /**
   *
   * @param ID
   * @param queryInterface
   */
  public Server(String ID, QueryInterface queryInterface) throws QueryException {
    this(Integer.parseInt(ID), queryInterface);
  }

  /**
   *
   * @param queryInterface
   * @param name
   * @param port
   * @param maxclients
   * @param reserved_slots
   * @param autostart
   * @param password
   * @param hostmessage
   * @param hostmessage_mode
   * @param welcomemessage
   * @param machine_id
   * @param hostbanner_url
   * @param hostbanner_gfx_url
   * @param hostbanner_gfx_interval
   * @param hostbutton_url
   * @param hostbutton_gfx_url
   * @throws QueryException
   */
  public Server(QueryInterface queryInterface, String name, Integer port, Integer maxclients, Integer reserved_slots,
          Boolean autostart, String password, String hostmessage, Integer hostmessage_mode, String welcomemessage,
          String machine_id, String hostbanner_url, String hostbanner_gfx_url, Integer hostbanner_gfx_interval,
          String hostbutton_url, String hostbutton_gfx_url) throws QueryException {
    if (queryInterface == null || name == null) {
      throw new QueryException(ErrorCodes.SERVER_MISSING_PARAMETER);
    }
    this.port = port;
    this.maxclients = maxclients;
    this.reserved_slots = reserved_slots;
    this.name = name;
    this.autostart = autostart;
    this.queryInterface = queryInterface;
    this.password = password;
    this.hostmessage = hostmessage;
    this.hostmessage_mode = hostmessage_mode;
    this.welcomemessage = welcomemessage;
    this.machine_id = machine_id;
    this.hostbanner_url = hostbanner_url;
    this.hostbanner_gfx_url = hostbanner_gfx_url;
    this.hostbanner_gfx_interval = hostbanner_gfx_interval;
    this.hostbutton_url = hostbutton_url;
    this.hostbutton_gfx_url = hostbutton_gfx_url;
  }

  /**
   *
   * @return @throws QueryException
   */
  public boolean createAndRegister() throws QueryException {
    HashMap<String, Object> paramInfo = new HashMap<String, Object>();
    if (port != null) {
      paramInfo.put("virtualserver_port", port);
    }
    if (maxclients != null) {
      paramInfo.put("virtualserver_maxclients", maxclients);
    }
    if (reserved_slots != null) {
      paramInfo.put("virtualserver_reserved_slots", reserved_slots);
    }
    if (name != null) {
      paramInfo.put("virtualserver_name", name);
    }
    if (autostart != null) {
      paramInfo.put("virtualserver_autostart", autostart);
    }
    if (password != null) {
      paramInfo.put("virtualserver_password", password);
    }
    if (hostmessage != null) {
      paramInfo.put("virtualserver_hostmessage", hostmessage);
    }
    if (hostmessage_mode != null) {
      paramInfo.put("virtualserver_hostmessage_mode", hostmessage_mode);
    }
    if (welcomemessage != null) {
      paramInfo.put("virtualserver_welcomemessage", welcomemessage);
    }
    if (machine_id != null) {
      paramInfo.put("virtualserver_machine_id", machine_id);
    }
    if (hostbanner_url != null) {
      paramInfo.put("virtualserver_hostbanner_url", hostbanner_url);
    }
    if (hostbanner_gfx_url != null) {
      paramInfo.put("virtualserver_hostbanner_gfx_url", hostbanner_gfx_url);
    }
    if (hostbanner_gfx_interval != null) {
      paramInfo.put("virtualserver_hostbanner_gfx_interval", hostbanner_gfx_interval);
    }
    if (hostbutton_url != null) {
      paramInfo.put("virtualserver_hostbutton_url", hostbutton_url);
    }
    if (hostbutton_gfx_url != null) {
      paramInfo.put("virtualserver_hostbutton_gfx_url", hostbutton_gfx_url);
    }
    QueryResponse qr = queryInterface.qCon.executeCommand(new QueryCommand("servercreate", paramInfo));
    if (qr.hasError()) {
      throw new QueryException(ErrorCodes.SERVER_CREATE_ERROR, qr.getErrorResponse());
    }
    this.setID(Integer.valueOf(qr.getDataResponse().get(0).get("sid")));
    this.fillServerInfo();
    queryInterface.registerServer(this);
    return true;
  }

  private void registerServer() {
    queryInterface.registerServer(this);
  }

  /**
   *
   * @return @throws QueryException
   */
  public boolean delete() throws QueryException {
    QueryResponse qr = this.executeCommand(new QueryCommand("serverdelete").param("sid", this.getID()));
    if (!qr.hasError()) {
      return true;
    }
    throw new QueryException(ErrorCodes.SERVER_DELETE_ERROR, qr.getErrorResponse());
  }

  /**
   *
   * @param command
   * @return
   * @throws QueryException
   */
  protected QueryResponse executeCommand(String command) throws QueryException {
    queryInterface.qCon.selectVirtualserver(ID);
    return queryInterface.qCon.executeCommand(command);
  }

  /**
   *
   * @param command
   * @return
   * @throws QueryException
   */
  protected QueryResponse executeCommand(QueryCommand command) throws QueryException {
    queryInterface.qCon.selectVirtualserver(ID);
    return queryInterface.qCon.executeCommand(command);
  }

  /**
   *
   * @param command
   * @return
   * @throws QueryException
   */
  protected boolean executeCommandCheckResponse(String command) throws QueryException {
    if (!this.executeCommand(command).hasError()) {
      return true;
    }
    return false;
  }

  /**
   *
   * @param command
   * @return
   * @throws QueryException
   */
  protected boolean executeCommandCheckResponse(QueryCommand command) throws QueryException {
    if (!this.executeCommand(command).hasError()) {
      return true;
    }
    return false;
  }

  /**
   *
   * @param channel
   */
  protected void registerChannel(Channel channel) {
    this.channels.add(channel);
  }

  /**
   *
   * @param channel
   */
  protected void unregisterChannel(Channel channel) {
    this.channels.remove(channel);
    channel = null;
  }

  /**
   *
   * @param client
   */
  protected void registerClient(Client client) {
    for (Client c : this.clients) {
      if (c.getClientID() == client.getClientID()) {
        client = null;
        return;
      }
    }
    this.clients.add(client);
  }

  /**
   *
   * @param client
   */
  protected void unregisterClient(Client client) {
    this.clients.remove(client);
    client = null;
  }

  /**
   *
   * @throws QueryException
   */
  private void fillServerInfo() throws QueryException {
    QueryResponse qr = this.executeCommand(new QueryCommand("serverinfo"));
    if (qr.hasError()) {
      throw new QueryException(ErrorCodes.SERVER_GET_INFO_FAILED, "ID: " + this.ID);
    }

    HashMap<String, String> info = qr.getDataResponse().get(0);

    this.port = Integer.parseInt(info.get("virtualserver_port"));
    this.autostart = Boolean.parseBoolean(info.get("virtualserver_autostart"));
    this.createdAt = Integer.parseInt(info.get("virtualserver_created"));
    this.hostbanner_gfx_interval = Integer.parseInt(info.get("virtualserver_hostbanner_gfx_interval"));
    this.hostbanner_gfx_url = info.get("virtualserver_hostbanner_gfx_url");
    this.hostbanner_url = info.get("virtualserver_hostbanner_url");
    this.hostbutton_gfx_url = info.get("virtualserver_hostbutton_gfx_url");
    this.hostbutton_url = info.get("virtualserver_hostbutton_url");
    this.hostmessage = info.get("virtualserver_hostmessage");
    this.hostmessage_mode = Integer.parseInt(info.get("virtualserver_hostmessage_mode"));
    this.machine_id = info.get("virtualserver_machine_id");
    this.maxclients = Integer.parseInt(info.get("virtualserver_maxclients"));
    this.name = info.get("virtualserver_name");
    this.online = info.get("virtualserver_status").equals("online");
    this.platform = info.get("virtualserver_platform");
    this.reserved_slots = Integer.parseInt(info.get("virtualserver_reserved_slots"));
    this.uptime = Integer.parseInt(info.get("virtualserver_uptime"));
    this.version = info.get("virtualserver_version");
    this.welcomemessage = info.get("virtualserver_welcomemessage");

    this.getChannelInfo();
    this.getClientInfo();
  }

  /**
   *
   * @return @throws QueryException
   */
  private boolean getChannelInfo() throws QueryException {
    QueryResponse qr = this.executeCommand(new QueryCommand("channellist"));
    if (qr.hasError()) {
      return false;
    }

    for (HashMap<String, String> channel : qr.getDataResponse()) {
      /*
       * Removed because creating a new Channel object automatically registers
       * the channel to the Server channels list.
       * this.channels.add(new Channel(channel.get("cid"), this));
       */
      Channel c = new Channel(channel.get("cid"), this);
    }
    return true;
  }

  private boolean getClientInfo() throws QueryException {
    QueryResponse qr = this.executeCommand(new QueryCommand("clientlist"));
    if (qr.hasError()) {
      return false;
    }

    for (HashMap<String, String> client : qr.getDataResponse()) {
      Client c = new Client(client.get("clid"), this);
    }
    return true;
  }

  /**
   *
   * @param field
   * @param value
   * @return
   * @throws QueryException
   */
  protected boolean updateFieldValue(String field, String value) throws QueryException {
    return this.executeCommandCheckResponse("serveredit " + field + "=" + QueryTools.encode(value));
  }

  /**
   *
   * @param field
   * @param value
   * @return
   * @throws QueryException
   */
  protected boolean updateFieldValue(String field, int value) throws QueryException {
    return updateFieldValue(field, String.valueOf(value));
  }

  /**
   *
   * @param field
   * @param value
   * @return
   * @throws QueryException
   */
  protected boolean updateFieldValue(String field, boolean value) throws QueryException {
    return updateFieldValue(field, (value ? "1" : "0"));
  }

  /**
   * Get the value of ID
   *
   * @return the value of ID
   */
  public int getID() {
    return ID;
  }

  /**
   * Set the value of ID
   */
  private void setID(Integer ID) {
    this.ID = ID;
  }

  /**
   * Get the value of port
   *
   * @return the value of port
   */
  public int getPort() {
    return port;
  }

  /**
   * Set the value of port
   *
   * @param port new value of port
   * @return
   * @throws QueryException
   */
  public boolean setPort(int port) throws QueryException {
    if (this.updateFieldValue("virtualserver_port", port)) {
      this.port = port;
      return true;
    }
    return false;
  }

  /**
   * Get the value of online
   *
   * @return the value of online
   */
  public boolean isOnline() {
    return online;
  }

  /**
   * Set the value of online
   *
   * @param online new value of online
   * @throws QueryException
   */
  public void setOnline(boolean online) throws QueryException {
    if (online == true) {
      this.start();
    } else {
      this.stop();
    }
  }

  /**
   * Get the value of maxClients
   *
   * @return the value of maxClients
   */
  public int getMaxClients() {
    return maxclients;
  }

  /**
   * Set the value of maxClients
   *
   * @param maxClients new value of maxClients
   * @return
   * @throws QueryException
   */
  public boolean setMaxClients(int maxClients) throws QueryException {
    if (this.updateFieldValue("virtualserver_maxclients", maxClients)) {
      this.maxclients = maxClients;
      return true;
    }
    return false;
  }

  /**
   * Get the value of name
   *
   * @return the value of name
   */
  public String getName() {
    return name;
  }

  /**
   * Set the value of name
   *
   * @param name new value of name
   * @return
   * @throws QueryException
   */
  public boolean setName(String name) throws QueryException {
    if (this.updateFieldValue("virtualserver_name", name)) {
      this.name = name;
      return true;
    }
    return false;
  }

  /**
   * Get the value of autostart
   *
   * @return the value of autostart
   */
  public boolean isAutostart() {
    return autostart;
  }

  /**
   * Set the value of autostart
   *
   * @param autostart new value of autostart
   * @return
   * @throws QueryException
   */
  public boolean setAutostart(boolean autostart) throws QueryException {
    if (this.updateFieldValue("virtualserver_autostart", autostart)) {
      this.autostart = autostart;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public Integer getHostbanner_gfx_interval() {
    return hostbanner_gfx_interval;
  }

  /**
   *
   * @param hostbanner_gfx_interval
   * @return
   * @throws QueryException
   */
  public boolean setHostbanner_gfx_interval(int hostbanner_gfx_interval) throws QueryException {
    if (this.updateFieldValue("virtualserver_hostbanner_gfx_interval", hostbanner_gfx_interval)) {
      this.hostbanner_gfx_interval = hostbanner_gfx_interval;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public String getHostbanner_gfx_url() {
    return hostbanner_gfx_url;
  }

  /**
   *
   * @param hostbanner_gfx_url
   * @return
   * @throws QueryException
   */
  public boolean setHostbanner_gfx_url(String hostbanner_gfx_url) throws QueryException {
    if (this.updateFieldValue("virtualserver_hostbanner_gfx_url", hostbanner_gfx_url)) {
      this.hostbanner_gfx_url = hostbanner_gfx_url;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public String getHostbanner_url() {
    return hostbanner_url;
  }

  /**
   *
   * @param hostbanner_url
   * @return
   * @throws QueryException
   */
  public boolean setHostbanner_url(String hostbanner_url) throws QueryException {
    if (this.updateFieldValue("virtualserver_hostbanner_url", hostbanner_url)) {
      this.hostbanner_url = hostbanner_url;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public String getHostbutton_gfx_url() {
    return hostbutton_gfx_url;
  }

  /**
   *
   * @param hostbutton_gfx_url
   * @return
   * @throws QueryException
   */
  public boolean setHostbutton_gfx_url(String hostbutton_gfx_url) throws QueryException {
    if (this.updateFieldValue("virtualserver_hostbutton_gfx_url", hostbutton_gfx_url)) {
      this.hostbutton_gfx_url = hostbutton_gfx_url;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public String getHostbutton_url() {
    return hostbutton_url;
  }

  /**
   *
   * @param hostbutton_url
   * @return
   * @throws QueryException
   */
  public boolean setHostbutton_url(String hostbutton_url) throws QueryException {
    if (this.updateFieldValue("virtualserver_hostbutton_url", hostbutton_url)) {
      this.hostbutton_url = hostbutton_url;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public String getHostmessage() {
    return hostmessage;
  }

  /**
   *
   * @param hostmessage
   * @return
   * @throws QueryException
   */
  public boolean setHostmessage(String hostmessage) throws QueryException {
    if (this.updateFieldValue("virtualserver_hostmessage", hostmessage)) {
      this.hostmessage = hostmessage;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public Integer getHostmessage_mode() {
    return hostmessage_mode;
  }

  /**
   *
   * @param hostmessage_mode
   * @return
   * @throws QueryException
   */
  public boolean setHostmessage_mode(Integer hostmessage_mode) throws QueryException {
    if (this.updateFieldValue("virtualserver_hostmessage_mode", hostmessage_mode)) {
      this.hostmessage_mode = hostmessage_mode;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public String getMachine_id() {
    return machine_id;
  }

  /**
   *
   * @param machine_id
   * @return
   * @throws QueryException
   */
  public boolean setMachine_id(String machine_id) throws QueryException {
    if (this.updateFieldValue("virtualserver_machine_id", machine_id)) {
      this.machine_id = machine_id;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public Integer getReserved_slots() {
    return reserved_slots;
  }

  /**
   *
   * @param reserved_slots
   * @return
   * @throws QueryException
   */
  public boolean setReserved_slots(Integer reserved_slots) throws QueryException {
    if (this.updateFieldValue("virtualserver_reserved_slots", reserved_slots)) {
      this.reserved_slots = reserved_slots;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public String getWelcomemessage() {
    return welcomemessage;
  }

  /**
   *
   * @param welcomemessage
   * @return
   * @throws QueryException
   */
  public boolean setWelcomemessage(String welcomemessage) throws QueryException {
    if (this.updateFieldValue("virtualserver_welcomemessage", welcomemessage)) {
      this.welcomemessage = welcomemessage;
      return true;
    }
    return false;
  }

  /**
   *
   * @param password
   * @return
   * @throws QueryException
   */
  public boolean setPassword(String password) throws QueryException {
    if (this.updateFieldValue("virtualserver_password", password)) {
      this.password = password;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public Integer getCreatedAt() {
    return createdAt;
  }

  /**
   *
   * @return
   */
  public String getPlatform() {
    return platform;
  }

  /**
   *
   * @return
   */
  public Integer getUptime() {
    return uptime;
  }

  /**
   *
   * @return
   */
  public String getVersion() {
    return version;
  }

  /**
   *
   * @return @throws QueryException
   */
  public boolean start() throws QueryException {
    if (this.online == true) {
      throw new QueryException("Cannot start server already running!");
    }

    if (this.executeCommandCheckResponse("serverstart sid=" + ID)) {
      this.online = true;
      return true;
    }
    return false;
  }

  /**
   *
   * @return @throws QueryException
   */
  public boolean stop() throws QueryException {
    if (this.online == false) {
      throw new QueryException("Cannot stop server already stopped!");
    }

    if (this.executeCommandCheckResponse("serverstop sid=" + ID)) {
      this.online = false;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public List<Channel> getChannels() {
    return this.channels;
  }

  /**
   *
   * @param id
   * @return
   */
  public Channel getChannelByID(int id) {
    for (Channel channel : this.channels) {
      if (channel.getID() == id) {
        return channel;
      }
    }
    return null;
  }

  /**
   *
   * @return
   */
  public List<Client> getClients() {
    return this.clients;
  }

  /**
   *
   * @param id
   * @return
   */
  public Client getClientByID(int id) {
    for (Client client : this.clients) {
      if (client.getClientID() == id) {
        return client;
      }
    }
    return null;
  }

  /**
   *
   * @return @throws QueryException
   */
  public HashMap<String, String> getConnectionInfo() throws QueryException {
    QueryResponse qr = this.executeCommand(new QueryCommand("serverrequestconnectioninfo"));
    if (!qr.hasError()) {
      return qr.getDataResponse().get(0);
    }
    return null;
  }

  /**
   *
   * @param key
   * @param value
   * @return
   * @throws QueryException
   */
  public boolean serverEditManual(String key, String value) throws QueryException {
    boolean ret = this.executeCommandCheckResponse(new QueryCommand("serveredit").param(key, value));
    this.fillServerInfo();
    return ret;
  }

  /**
   *
   * @param parameters
   * @return
   * @throws QueryException
   */
  public boolean serverEditManual(HashMap<String, Object> parameters) throws QueryException {
    boolean ret = this.executeCommandCheckResponse(new QueryCommand("serveredit", parameters));
    this.fillServerInfo();
    return ret;
  }

  /*public DBClient getClientFromDBbyPattern(String pattern) {

   }*/
}
