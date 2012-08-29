/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.servertube.QueryInterface.Data;

/**
 *
 * @author Basti
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
  SPEEX_CELT_MONO(3, "CELT Mono (48 kHz)");
  
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
