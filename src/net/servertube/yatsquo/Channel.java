/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
 *
 * Channel.java - 2012-08-29
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

import java.util.HashMap;
import net.servertube.yatsquo.Data.Codec;

/**
 * <strong>The Channel class provides all necessary tools to manage the channels
 * in your server.</strong>
 *
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public class Channel {

  private Integer ID;
  private Server server;
  private String name;
  private String name_phonetic;
  private String topic;
  private String description;
  private String password;
  private Integer parentID;
  private Integer order;
  private Codec codec;
  private Integer codec_quality;
  private Integer codec_latency_factor;
  private Boolean codec_unencrypted;
  private Integer maxclients;
  private Integer maxfamilyclients;
  private Boolean maxfamilyclients_unlimited;
  private Boolean maxfamilyclients_inherited;
  private Boolean maxclients_unlimited;
  private Boolean isDefault;
  private Boolean isPasswordProtected;
  private Boolean isPermanent;
  private Boolean isSemiPermanent;
  private Boolean isTemporary;
  private Integer needed_talk_power;
  private Integer icon_id;

  /**
   * When initializing a new Channel object with this constructor the library
   * <br />will fetch all necessary data from the query port.<br />
   * <strong>Requires the given Channel ID to be existent</strong>
   *
   * @param ID The channel ID to get the infor for
   * @param server A server Object the channel will be assigned to
   */
  public Channel(Integer ID, Server server) throws QueryException {
    this.ID = ID;
    this.server = server;
    this.fillChannelInfo();
    this.registerChannel();
  }

  /**
   * String version of the data-fetch-constructor (see above)
   *
   * @param ID The channel ID to get the infor for
   * @param server A server Object the channel will be assigned to
   */
  public Channel(String ID, Server server) throws QueryException {
    this(Integer.parseInt(ID), server);
  }

  /**
   * This constructor creates a new Channel object with all the available
   * optional parameters.<br />
   * Only the server and name parameters are vital, the rest can be set to
   * null!<br /><br />
   * After creating the Channel object call the createAndRegister() function if
   * you want<br />
   * the channel to be created in your server! All missing data will be filled
   * in automatically.
   *
   * @param server A server Object the channel will be assigned to
   * @param name The channels name
   * @param name_phonetic The channels phonetic name
   * @param topic The channels topic
   * @param description The channels description
   * @param password The channels password
   * @param parentID The channels parent channel ID
   * @param order The channels order #
   * @param codec The channels codec (<a href="Enums/Codec.html">Codec</a>)
   * @param codec_quality The codec quality
   * @param codec_latency_factor The codec latency factor
   * @param codec_unencrypted Is the codec encrypted?
   * @param maxclients max clients in channel
   * @param maxfamilyclients max family clients in channel
   * @param maxfamilyclients_unlimited allow unlimited family clients?
   * @param maxfamilyclients_inherited family client limit inherited?
   * @param maxclients_unlimited allow unlimited clients?
   * @param isDefault set as default channel?
   * @param isPermanent is the channel permanent?
   * @param isSemiPermanent is the channel semi permanent?
   * @param isTemporary is the channel temporary?
   * @param needed_talk_power required talk power
   * @param icon_id the icon id
   * @throws QueryException
   */
  public Channel(Server server, String name, String name_phonetic, String topic, String description, String password, Integer parentID,
          Integer order, Codec codec, Integer codec_quality, Integer codec_latency_factor, Boolean codec_unencrypted, Integer maxclients,
          Integer maxfamilyclients, Boolean maxfamilyclients_unlimited, Boolean maxfamilyclients_inherited, Boolean maxclients_unlimited,
          Boolean isDefault, Boolean isPermanent, Boolean isSemiPermanent, Boolean isTemporary,
          Integer needed_talk_power, Integer icon_id) throws QueryException {
    if (server == null || name == null) {
      throw new QueryException("Cannot create a channel without server or name");
    }
    this.server = server;
    this.name = name;
    this.name_phonetic = name_phonetic;
    this.topic = topic;
    this.description = description;
    this.password = password;
    this.parentID = parentID;
    this.order = order;
    this.codec = codec;
    this.codec_quality = codec_quality;
    this.codec_latency_factor = codec_latency_factor;
    this.codec_unencrypted = codec_unencrypted;
    this.maxclients = maxclients;
    this.maxfamilyclients = maxfamilyclients;
    this.maxfamilyclients_unlimited = maxfamilyclients_unlimited;
    this.maxfamilyclients_inherited = maxfamilyclients_inherited;
    this.maxclients_unlimited = maxclients_unlimited;
    this.isDefault = isDefault;
    this.isPermanent = isPermanent;
    this.isSemiPermanent = isSemiPermanent;
    this.isTemporary = isTemporary;
    this.needed_talk_power = needed_talk_power;
    this.icon_id = icon_id;
  }

  /**
   * After initializing a new Channel object call this function to actually
   * <br />create the channel in the server it is assigned to.<br />
   * <strong>Will only function when the channel has no ID set!<br />
   * That means when the channel is not already registered!</strong>
   *
   * @return boolean success
   * @throws QueryException
   */
  public boolean createAndRegister() throws QueryException {
    if (this.ID != null) {
      throw new QueryException("Cannot create a channel already registered!");
    }
    HashMap<String, Object> paramInfo = new HashMap<String, Object>();
    paramInfo.put("channel_name", name);
    if (name_phonetic != null) {
      paramInfo.put("channel_name_phonetic", name_phonetic);
    }
    if (topic != null) {
      paramInfo.put("channel_topic", topic);
    }
    if (description != null) {
      paramInfo.put("channel_description", description);
    }
    if (password != null) {
      paramInfo.put("channel_password", password);
    }
    if (parentID != null) {
      paramInfo.put("cpid", parentID);
    }
    if (order != null) {
      paramInfo.put("channel_order", order);
    }
    if (codec != null) {
      paramInfo.put("channel_codec", codec.getID());
    }
    if (codec_quality != null) {
      paramInfo.put("channel_codec_quality", codec_quality);
    }
    if (codec_unencrypted != null) {
      paramInfo.put("channel_codec_is_unencrypted", codec_unencrypted);
    }
    if (maxfamilyclients != null) {
      paramInfo.put("channel_maxfamilyclients", maxfamilyclients);
    }
    if (isPermanent != null) {
      paramInfo.put("channel_flag_permanent", isPermanent);
    }
    if (isSemiPermanent != null) {
      paramInfo.put("channel_flag_semi_permanent", isSemiPermanent);
    }
    if (isTemporary != null) {
      paramInfo.put("channel_flag_temporary", isTemporary);
    }
    if (isDefault != null) {
      paramInfo.put("channel_flag_default", isDefault);
    }
    if (maxclients_unlimited != null) {
      paramInfo.put("channel_flag_maxclients_unlimited", maxclients_unlimited);
    }
    if (maxfamilyclients_unlimited != null) {
      paramInfo.put("channel_flag_maxfamilyclients_unlimited", maxfamilyclients_unlimited);
    }
    if (maxfamilyclients_inherited != null) {
      paramInfo.put("channel_flag_maxfamilyclients_inherited", maxfamilyclients_inherited);
    }
    if (needed_talk_power != null) {
      paramInfo.put("channel_needed_talk_power", needed_talk_power);
    }
    if (icon_id != null) {
      paramInfo.put("channel_icon_id", icon_id);
    }
    QueryResponse qr = server.executeCommand(new QueryCommand("channelcreate", paramInfo));
    if (qr.hasError()) {
      throw new QueryException("Error while creating channel", qr.getErrorResponse());
    }
    this.setID(Integer.valueOf(qr.getDataResponse().get(0).get("cid")));
    this.fillChannelInfo();
    this.registerChannel();
    return true;
  }

  /**
   * registers the channel to the server object
   */
  private void registerChannel() {
    server.registerChannel(this);
  }

  /**
   * Deletes the Channel from the server and destroy the object
   *
   * @param forceDeletion force deletion even if there are clients in the
   * channel
   * @return boolean success
   * @throws QueryException
   */
  public boolean delete(Boolean forceDeletion) throws QueryException {
    QueryResponse qr = server.executeCommand(new QueryCommand("channeldelete").param("cid", this.getID()).param("force", forceDeletion));
    if (!qr.hasError()) {
      server.unregisterChannel(this);
      return true;
    }
    throw new QueryException("Error while deleting channel", qr.getErrorResponse());
  }

  /**
   * requests all information available about the channel
   *
   * @throws QueryException
   */
  private void fillChannelInfo() throws QueryException {
    QueryResponse qr = server.executeCommand(new QueryCommand("channelinfo").param("cid", this.getID()));
    if (qr.hasError()) {
      throw new QueryException("Error retrieving channel info", qr.getErrorResponse());
    }

    HashMap<String, String> info = qr.getDataResponse().get(0);

    this.name = info.get("channel_name");
    this.name_phonetic = info.get("channel_name_phonetic");
    this.codec = Codec.getByID(Integer.valueOf(info.get("channel_codec")));
    this.codec_latency_factor = Integer.valueOf(info.get("channel_codec_latency_factor"));
    this.codec_quality = Integer.valueOf(info.get("channel_codec_quality"));
    this.description = info.get("channel_description");
    this.isDefault = Boolean.parseBoolean("channel_flag_default");
    this.isPasswordProtected = Boolean.parseBoolean("channel_flag_password");
    this.isPermanent = Boolean.parseBoolean("channel_flag_permanent");
    this.isSemiPermanent = Boolean.parseBoolean("channel_flag_semi_permanent");
    this.maxclients = Integer.valueOf(info.get("channel_maxclients"));
    this.maxclients_unlimited = Boolean.parseBoolean(info.get("channel_flag_maxclients_unlimited"));
    this.maxfamilyclients = Integer.valueOf(info.get("channel_maxfamilyclients"));
    this.maxfamilyclients_inherited = Boolean.parseBoolean(info.get("channel_flag_maxfamilyclients_inherited"));
    this.order = Integer.valueOf(info.get("channel_order"));
    this.parentID = Integer.valueOf(info.get("pid"));
    this.topic = info.get("channel_topic");
  }

  /**
   * Get the value of ID
   *
   * @return the value of ID
   */
  public Integer getID() {
    return ID;
  }

  /**
   * Set the value of ID
   *
   * @param ID new value of ID
   */
  private void setID(Integer ID) {
    this.ID = ID;
  }

  /**
   * Get the used Codec
   *
   * @return
   */
  public Codec getCodec() {
    return codec;
  }

  /**
   * Set the Codec to use
   *
   * @param codec
   * @return boolean success
   * @throws QueryException
   */
  public boolean setCodec(Codec codec) throws QueryException {
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_codec=" + this.codec.getID())) {
      this.codec = codec;
      return true;
    }
    return false;
  }

  /**
   * Get the codec latency factor
   *
   * @return int
   */
  public int getCodec_latency_factor() {
    return codec_latency_factor;
  }

  /**
   * Set the codec latency factor
   *
   * @param codec_latency_factor
   * @return boolean success
   * @throws QueryException
   */
  public boolean setCodec_latency_factor(int codec_latency_factor) throws QueryException {
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_codec_latency_factor=" + codec_latency_factor)) {
      this.codec_latency_factor = codec_latency_factor;
      return true;
    }
    return false;
  }

  /**
   * Get the codec quality
   *
   * @return int
   */
  public int getCodec_quality() {
    return codec_quality;
  }

  /**
   * Set the codec quality
   *
   * @param codec_quality
   * @return boolean success
   * @throws QueryException
   */
  public boolean setCodec_quality(int codec_quality) throws QueryException {
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_codec_quality=" + codec_quality)) {
      this.codec_quality = codec_quality;
      return true;
    }
    return false;
  }

  /**
   * Get the channel description
   *
   * @return String
   */
  public String getDescription() {
    return description;
  }

  /**
   * Set the channel description
   *
   * @param description
   * @return boolean success
   * @throws QueryException
   */
  public boolean setDescription(String description) throws QueryException {
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_description=" + description)) {
      this.description = description;
      return true;
    }
    return false;
  }

  /**
   * Check if the channel is the default channel
   *
   * @return boolean default
   */
  public boolean isIsDefaultChannel() {
    return isDefault;
  }

  /**
   * Set the channel to be the default channel<br />
   * If the channel already is the default channel no changes are made
   *
   * @param setDefault set whether or not the channel is the default channel
   * @return boolean success
   * @throws QueryException
   */
  public boolean setIsDefaultChannel(boolean setDefault) throws QueryException {
    if (this.isDefault == setDefault) {
      return true;
    }
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_flag_default=" + (setDefault ? "1" : "0"))) {
      this.isDefault = setDefault;
      return true;
    }
    return false;
  }

  /**
   * Check if the channel is permanent
   *
   * @return boolean permanent
   */
  public boolean isIsPermanent() {
    return isPermanent;
  }

  /**
   * Set the channel to be permanent
   *
   * @param setPermanent <b>true</b>: permanent; <b>false</b>: not permanent
   * @return boolean success
   * @throws QueryException
   */
  public boolean setIsPermanent(boolean setPermanent) throws QueryException {
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_flag_permanent=" + (setPermanent ? "1" : "0"))) {
      this.isPermanent = setPermanent;
      return true;
    }
    return false;
  }

  /**
   * Check if the channel is semi permanent
   *
   * @return boolean semi permanent
   */
  public boolean isIsSemiPermanent() {
    return isSemiPermanent;
  }

  /**
   * Set the channel to be semi permanent
   *
   * @param setSemiPermanent <b>true</b>: semi permanent; <b>false</b>: not semi
   * permanent
   * @return boolean success
   * @throws QueryException
   */
  public boolean setIsSemiPermanent(boolean setSemiPermanent) throws QueryException {
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_flag_semi_permanent=" + (setSemiPermanent ? "1" : "0"))) {
      this.isSemiPermanent = setSemiPermanent;
      return true;
    }
    return false;
  }

  /**
   * Get the max client count of the channel
   *
   * @return int maxclients
   */
  public int getMaxclients() {
    return maxclients;
  }

  /**
   * Set the max client count for the channel
   *
   * @param maxclients new max client count
   * @return boolean success
   * @throws QueryException
   */
  public boolean setMaxclients(int maxclients) throws QueryException {
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_maxclients=" + maxclients)) {
      this.maxclients = maxclients;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public boolean isMaxclients_unlimited() {
    return maxclients_unlimited;
  }

  /**
   *
   * @param maxclients_unlimited
   * @return
   * @throws QueryException
   */
  public boolean setMaxclients_unlimited(boolean maxclients_unlimited) throws QueryException {
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_flag_maxclients_unlimited=" + (maxclients_unlimited ? "1" : "0"))) {
      this.maxclients_unlimited = maxclients_unlimited;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public int getMaxfamilityclients() {
    return maxfamilyclients;
  }

  /**
   *
   * @param maxfamilyclients
   * @return
   * @throws QueryException
   */
  public boolean setMaxfamilityclients(int maxfamilyclients) throws QueryException {
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_maxfamilyclients=" + maxfamilyclients)) {
      this.maxfamilyclients = maxfamilyclients;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public boolean isMaxfamilyclients_inherited() {
    return maxfamilyclients_inherited;
  }

  /**
   *
   * @param maxfamilyclients_inherited
   * @return
   * @throws QueryException
   */
  public boolean setMaxfamilyclients_inherited(boolean maxfamilyclients_inherited) throws QueryException {
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_flag_maxfamilyclients_inherited=" + (maxfamilyclients_inherited ? "1" : "0"))) {
      this.maxfamilyclients_inherited = maxfamilyclients_inherited;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public boolean isMaxfamilyclients_unlimited() {
    return maxfamilyclients_unlimited;
  }

  /**
   *
   * @param maxfamilyclients_unlimited
   * @return
   * @throws QueryException
   */
  public boolean setMaxfamilyclients_unlimited(boolean maxfamilyclients_unlimited) throws QueryException {
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_flag_maxfamilyclients_unlimited=" + (maxfamilyclients_unlimited ? "1" : "0"))) {
      this.maxfamilyclients_unlimited = maxfamilyclients_unlimited;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   *
   * @param name
   * @return
   * @throws QueryException
   */
  public boolean setName(String name) throws QueryException {
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_name=" + name)) {
      this.name = name;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public String getName_phonetic() {
    return name_phonetic;
  }

  /**
   *
   * @param name_phonetic
   * @return
   * @throws QueryException
   */
  public boolean setName_phonetic(String name_phonetic) throws QueryException {
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_name_phonetic=" + name_phonetic)) {
      this.name_phonetic = name_phonetic;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public int getOrder() {
    return order;
  }

  /**
   *
   * @param order
   * @return
   * @throws QueryException
   */
  public boolean setOrder(int order) throws QueryException {
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_order=" + order)) {
      this.order = order;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public int getParentID() {
    return parentID;
  }

  /**
   *
   * @param parentID
   * @return
   * @throws QueryException
   */
  public boolean setParentID(int parentID) throws QueryException {
    if (this.server.executeCommandCheckResponse("channelmove cid=" + this.ID + " cpid=" + parentID)) {
      this.parentID = parentID;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public String getTopic() {
    return topic;
  }

  /**
   *
   * @param topic
   * @return
   * @throws QueryException
   */
  public boolean setTopic(String topic) throws QueryException {
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_topic=" + topic)) {
      this.topic = topic;
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
    if (this.server.executeCommandCheckResponse("channeledit cid=" + this.ID + " channel_password=" + password)) {
      this.password = password;
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public boolean isPasswordProtected() {
    return isPasswordProtected;
  }
}
