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

/**
 *
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public enum Codec {

  /**
   *
   */
  SPEEX_NARROWBAND(0, "Speex Narrowband (8 kHz)"),
  /**
   *
   */
  SPEEX_WIDEBAND(1, "Speex Wideband (16 kHz)"),
  /**
   *
   */
  SPEEX_ULTRA_WIDEBAND(2, "Speex Ultra-Wideband (32 kHz)"),
  /**
   *
   */
  SPEEX_CELT_MONO(3, "CELT Mono (48 kHz)"),
  /**
   * ID is guessed
   */
  OPUS_VOICE(4, "Opus Voice"),
  /**
   * ID is guessed
   */
  OPUS_MUSIC(5, "Opus Music");

  private final int id;
  private final String name;

  private Codec(int id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   *
   * @return
   */
  public int getID() {
    return this.id;
  }

  /**
   *
   * @return
   */
  public String getName() {
    return this.name;
  }

  /**
   *
   * @param id
   * @return
   */
  public static Codec getByID(int id) {
    for(Codec c : Codec.values()) {
      if(c.getID() == id) {
        return c;
      }
    }
    return null;
  }
}
