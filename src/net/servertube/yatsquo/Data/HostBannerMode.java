/*
 * Copyright (C) 2013 Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>.
 *
 * HostBannerMode.java - 2013-05-21
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
 * Reflects the possible host banner modes
 * @author Sebastian "prodigy" Grunow <sebastian.gr at servertube.net>
 */
public enum HostBannerMode {

  /**
   * do not adjust
   */
  NOADJUST(0),
  /**
   * adjust but ignore aspect ratio (like TeamSpeak 2)
   */
  IGNOREASPECT(1),
  /**
   * adjust and keep aspect ratio
   */
  KEEPASPECT(2);
  private final int id;

  private HostBannerMode(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return String.valueOf(id);
  }
}
