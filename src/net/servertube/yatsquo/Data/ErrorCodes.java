/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
 *
 * ErrorCodes.java - 2013-05-21
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

/**
 *
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public enum ErrorCodes {

  ERRORCODE_NO_ERROR(0, "No error"),
  ERRORCODE_CUSTOM_ERROR(8, "Custom error; See message"),
  ERRORCODE_TS3_ERROR(9, "TS3 error; See queryError"),
  CONNECTION_UNABLE_OPEN_SOCKET(101, "Unable to open socket!"),
  CONNECTION_NOT_CLOSED(102, "Connection is still open!"),
  CONNECTION_TS3_NOT_PRESENT(103, "Connection opened, but no TS3 Server detected."),
  CONNECTION_ERROR_CONNECTING(104, "Error connecting to query port."),
  CONNECTION_NOT_CONNECTED(105, "Connection was closed unexpectedly."),
  CONNECTION_SOCKET_READ_ERROR(106, "Error while reading from input stream"),
  CONNECTION_NULL_RESPONSE(107, "NULL response from input stream; Connection lost?"),
  CONNECTION_CLOSING_FAILED(108, "Error closing connection"),
  PARSER_NULL_RESULT(201, "Parser returned NULL object; Unknown error, should not occur"),
  SERVER_MISSING_PARAMETER(301, "queryInterface and name cannot be null"),
  SERVER_CREATE_ERROR(302, "An error occured while creating the server"),
  SERVER_DELETE_ERROR(303, "An error occured while deleting the server"),
  SERVER_GET_INFO_FAILED(304, "Retrieving the serverinfo failed"),
  SERVER_ALREADY_RUNNING(305, "Starting failed: already running"),
  SERVER_ALREADY_STOPPED(306, "Stopping failed: already stopped"),
  EVENT_UNKNOWN(401, "Unknown event type"),
  ;

  private final Integer Id;
  private final String Message;

  private ErrorCodes(Integer Id, String Message) {
    this.Id = Id;
    this.Message = Message;
  }

  public Integer getId() {
    return Id;
  }

  public String getMessage() {
    return Message;
  }

  @Override
  public String toString() {
    return "ErrorCode (" + this.Id + ") : " + this.Message;
  }
}
