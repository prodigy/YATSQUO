/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
 *
 * Client.java - 2012-08-29
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
import net.servertube.yatsquo.Data.TextMessageTargetMode;

/**
 *
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public class Client extends TS3Object {

  private Server server;
  private Channel channel;
  private Integer database_id;
  private String uuid;
  private String nickname;
  private String nickname_phonetic;
  private String version;
  private String platform;
  private String default_channel;
  private Boolean away;
  private String away_message;
  private String meta_data;
  private Boolean is_recording;
  private Integer idle_time;
  private Integer channel_group_id;
  private ArrayList<Integer> servergroups;
  private String login_name;
  private Long created;
  private Long lastconnected;
  private Integer totalConnections;
  private Integer type;
  private Integer talk_power;
  private Boolean is_talker;
  private Boolean input_muted;
  private Boolean output_muted;
  private Boolean outputonly_muted;
  private Boolean input_hardware;
  private Boolean output_hardware;
  private Integer month_bytes_uploaded;
  private Integer month_bytes_downloaded;
  private Integer total_bytes_uploaded;
  private Integer total_bytes_downloaded;
  private Boolean is_priority_speaker;
  private Boolean is_channel_commander;
  private Integer icon_id;
  private String base64HashClientUID;
  private Long filetransfer_bandwidth_sent;
  private Long filetransfer_bandwidth_received;
  private Long packets_sent_total;
  private Long bytes_sent_total;
  private Long packets_received_total;
  private Long bytes_received_total;
  private Long bandwidth_sent_last_second_total;
  private Long bandwidth_sent_last_minute_total;
  private Long bandwidth_received_last_second_total;
  private Long bandwidth_received_last_minute_total;
  private Long connected_time;
  private String client_ip;

  /**
   * creates a new client and requests all available information through query
   *
   * @param ID
   * @param server
   */
  public Client(Integer ID, Server server) throws QueryException {
    this.server = server;
    this.ID = ID;
    this.fillClientInfo();
    this.registerClient();
  }

  /**
   * creates a new client and requests all available information through query
   *
   * @param ID
   * @param server
   */
  public Client(String ID, Server server) throws QueryException {
    this(Integer.parseInt(ID), server);
  }

  /**
   * requests all information available about the client from query
   *
   * @throws QueryException
   */
  private void fillClientInfo() throws QueryException {
    QueryResponse qr = server.executeCommand(new QueryCommand("clientinfo").param("clid", this.ID));
    if (qr.hasError()) {
      throw new QueryException("Error retrieving Client info: ", qr.getErrorResponse());
    }

    HashMap<String, String> info = qr.getDataResponse().get(0);

    this.channel = server.getChannelByID(Integer.valueOf(info.get("cid")));
    this.channel.registerClient(this);
    this.database_id = Integer.valueOf(info.get("client_database_id"));
    this.uuid = info.get("client_unique_identifier");
    this.nickname = info.get("client_nickname");
    this.nickname_phonetic = info.get("client_nickname_phonetic");
    this.version = info.get("client_version");
    this.platform = info.get("client_platform");
    this.default_channel = info.get("client_default_channel");
    this.away = Boolean.parseBoolean(info.get("client_away"));
    this.away_message = info.get("client_away_message");
    this.meta_data = info.get("client_meta_data");
    this.is_recording = Boolean.parseBoolean(info.get("client_is_recording"));
    this.idle_time = Integer.valueOf(info.get("client_idle_time"));
    this.channel_group_id = Integer.valueOf(info.get("client_channel_group_id"));
    this.servergroups = QueryTools.buildList(info.get("client_servergroups"), Integer.class);
    this.login_name = info.get("client_login_name");
    this.created = Long.valueOf(info.get("client_created"));
    this.lastconnected = Long.valueOf(info.get("client_lastconnected"));
    this.totalConnections = Integer.valueOf(info.get("client_totalconnections"));
    this.type = Integer.valueOf(info.get("client_type"));
    this.talk_power = Integer.valueOf(info.get("client_talk_power"));
    this.is_talker = Boolean.parseBoolean(info.get("client_is_talker"));
    this.input_muted = Boolean.parseBoolean(info.get("client_input_muted"));
    this.output_muted = Boolean.parseBoolean(info.get("client_output_muted"));
    this.outputonly_muted = Boolean.parseBoolean(info.get("client_outputonly_muted"));
    this.input_hardware = Boolean.parseBoolean(info.get("client_input_hardware"));
    this.output_hardware = Boolean.parseBoolean(info.get("client_output_hardware"));
    this.month_bytes_uploaded = Integer.valueOf(info.get("client_month_bytes_uploaded"));
    this.month_bytes_downloaded = Integer.valueOf(info.get("client_month_bytes_downloaded"));
    this.total_bytes_uploaded = Integer.valueOf(info.get("client_total_bytes_uploaded"));
    this.total_bytes_downloaded = Integer.valueOf(info.get("client_total_bytes_downloaded"));
    this.is_priority_speaker = Boolean.parseBoolean(info.get("client_is_priority_speaker"));
    this.is_channel_commander = Boolean.parseBoolean(info.get("client_is_channel_commander"));
    this.icon_id = Integer.valueOf(info.get("client_icon_id"));
    this.base64HashClientUID = info.get("client_base64HashClientUID");
    this.filetransfer_bandwidth_sent = Long.valueOf(info.get("connection_filetransfer_bandwidth_sent"));
    this.filetransfer_bandwidth_received = Long.valueOf(info.get("connection_filetransfer_bandwidth_received"));
    this.packets_sent_total = Long.valueOf(info.get("connection_packets_sent_total"));
    this.bytes_sent_total = Long.valueOf(info.get("connection_bytes_sent_total"));
    this.packets_received_total = Long.valueOf(info.get("connection_packets_received_total"));
    this.bytes_received_total = Long.valueOf(info.get("connection_bytes_received_total"));
    this.bandwidth_sent_last_second_total = Long.valueOf(info.get("connection_bandwidth_sent_last_second_total"));
    this.bandwidth_sent_last_minute_total = Long.valueOf(info.get("connection_bandwidth_sent_last_minute_total"));
    this.bandwidth_received_last_second_total = Long.valueOf(info.get("connection_bandwidth_received_last_second_total"));
    this.bandwidth_received_last_minute_total = Long.valueOf(info.get("connection_bandwidth_received_last_minute_total"));
    this.connected_time = Long.valueOf(info.get("connection_connected_time"));
    this.client_ip = info.get("connection_client_ip");
  }

  /**
   * registers the client to the server
   */
  private void registerClient() {
    server.registerClient(this);
  }

  public boolean sendMessage(String message) throws QueryException {
    return this.server.queryInterface.qCon.sendTextMessage(this, message);
  }

  public Server getServer() {
    return server;
  }

  public Channel getChannel() {
    return channel;
  }

  /**
   * returns the client ID
   *
   * @return
   */
  public Integer getClientID() {
    return ID;
  }

  public Integer getDatabaseID() {
    return database_id;
  }

  public String getUUID() {
    return uuid;
  }

  public String getNickname() {
    return nickname;
  }

  public String getNickname_phonetic() {
    return nickname_phonetic;
  }

  public String getVersion() {
    return version;
  }

  public String getPlatform() {
    return platform;
  }

  public String getDefaultChannel() {
    return default_channel;
  }

  public Boolean getAway() {
    return away;
  }

  public String getAwayMessage() {
    return away_message;
  }

  public String getMetaData() {
    return meta_data;
  }

  public Boolean getIsRecording() {
    return is_recording;
  }

  public Integer getIdleTime() {
    return idle_time;
  }

  public Integer getChannelGroupID() {
    return channel_group_id;
  }

  public ArrayList<Integer> getServergroups() {
    return servergroups;
  }

  public String getLoginName() {
    return login_name;
  }

  public Long getCreated() {
    return created;
  }

  public Long getLastconnected() {
    return lastconnected;
  }

  public Integer getTotalConnections() {
    return totalConnections;
  }

  public Integer getType() {
    return type;
  }

  public Integer getTalkPower() {
    return talk_power;
  }

  public Boolean getIsTalker() {
    return is_talker;
  }

  public Boolean getInputMuted() {
    return input_muted;
  }

  public Boolean getOutputMuted() {
    return output_muted;
  }

  public Boolean getOutputonlyMuted() {
    return outputonly_muted;
  }

  public Boolean getInputHardware() {
    return input_hardware;
  }

  public Boolean getOutputHardware() {
    return output_hardware;
  }

  public Integer getMonthBytesUploaded() {
    return month_bytes_uploaded;
  }

  public Integer getMonthBytesDownloaded() {
    return month_bytes_downloaded;
  }

  public Integer getTotalBytesUploaded() {
    return total_bytes_uploaded;
  }

  public Integer getTotalBytesDownloaded() {
    return total_bytes_downloaded;
  }

  public Boolean getIsPrioritySpeaker() {
    return is_priority_speaker;
  }

  public Boolean getIsChannelCommander() {
    return is_channel_commander;
  }

  public Integer getIconID() {
    return icon_id;
  }

  public String getBase64HashClientUID() {
    return base64HashClientUID;
  }

  public Long getFiletransferBandwidthSent() {
    return filetransfer_bandwidth_sent;
  }

  public Long getFiletransferBandwidthReceived() {
    return filetransfer_bandwidth_received;
  }

  public Long getPacketsSentTotal() {
    return packets_sent_total;
  }

  public Long getBytesSentTotal() {
    return bytes_sent_total;
  }

  public Long getPacketsReceivedTotal() {
    return packets_received_total;
  }

  public Long getBytesReceivedTotal() {
    return bytes_received_total;
  }

  public Long getBandwidthSentLastSecondTotal() {
    return bandwidth_sent_last_second_total;
  }

  public Long getBandwidthSentLastMinuteTotal() {
    return bandwidth_sent_last_minute_total;
  }

  public Long getBandwidthReceivedLastSecondTotal() {
    return bandwidth_received_last_second_total;
  }

  public Long getBandwidthReceivedLastMinuteTotal() {
    return bandwidth_received_last_minute_total;
  }

  public Long getConnectedTime() {
    return connected_time;
  }

  public String getClientIP() {
    return client_ip;
  }
}
