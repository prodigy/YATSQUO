package net.servertube.QueryInterface;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Sebastian "prodigy" G.
 */

public class Client {
  
  private Server server;
  private Channel channel;
  private Integer client_id;
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

  public Client(Integer ID, Server server) {
    this.server = server;
    this.client_id = ID;
  }
  
  private void fillClientInfo() throws QueryException {
    QueryResponse qr = server.executeCommand(new QueryCommand("clientinfo").param("clid", this.client_id));
    if(qr.hasError()) {
      throw new QueryException("Error retrieving Client info: ", qr.getErrorResponse());
    }
    
    HashMap<String, String> info = qr.getDataResponse().get(0);
    
    this.channel = server.getChannelByID(Integer.valueOf(info.get("cid")));
    this.database_id = Integer.valueOf(info.get("client_database_id"));
    this.uuid = info.get("client_unique_identifier");
    this.nickname = info.get("client_nickname");
    this.nickname_phonetic = info.get("client_nickname_phonetic");    this.version = info.get("client_version");
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
    this.totalConnections = Integer.valueOf(info.get("client_totalConnections"));
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
    this.filetransfer_bandwidth_sent = Long.valueOf(info.get("client_filetransfer_bandwidth_sent"));
    this.filetransfer_bandwidth_received = Long.valueOf(info.get("client_filetransfer_bandwidth_received"));
    this.packets_sent_total = Long.valueOf(info.get("client_packets_sent_total"));
    this.bytes_sent_total = Long.valueOf(info.get("client_bytes_sent_total"));
    this.packets_received_total = Long.valueOf(info.get("client_packets_received_total"));
    this.bytes_received_total = Long.valueOf(info.get("client_bytes_received_total"));
    this.bandwidth_sent_last_second_total = Long.valueOf(info.get("client_bandwidth_sent_last_second_total"));
    this.bandwidth_sent_last_minute_total = Long.valueOf(info.get("client_bandwidth_sent_last_minute_total"));
    this.bandwidth_received_last_second_total = Long.valueOf(info.get("client_bandwidth_received_last_second_total"));
    this.bandwidth_received_last_minute_total = Long.valueOf(info.get("client_bandwidth_received_last_minute_total"));
    this.connected_time = Long.valueOf(info.get("client_connected_time"));
    this.client_ip = info.get("client_client_ip");
  }

}