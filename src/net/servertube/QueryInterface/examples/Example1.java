package net.servertube.QueryInterface.examples;

import net.servertube.QueryInterface.Channel;
import net.servertube.QueryInterface.QueryException;
import net.servertube.QueryInterface.QueryInterface;
import net.servertube.QueryInterface.Server;

/**
 * QueryInterface - Example1<br />
 * <br />
 * Establishing a connection and priting out every Server with<br />
 * its Channels and Subchannels (not-sorted).
 * @author Sebastian "prodigy" G.
 */
public class Example1 {

  private static String user = "serveradmin";
  private static String passwd = "PASSWORD";
  private static String ip = "127.0.0.1";
  private static int port = 10011;

  /**
   * @see Example1
   * @param args
   * @throws QueryException
   */
  public static void main(String[] args) throws QueryException {
    QueryInterface qi = new QueryInterface();
    // connect to the given ip : port
    qi.connect(ip, port);
    // login using given user and passwd
    qi.login(user, passwd);
    // populate the internal server list
    qi.fillServerList();
    // iterate through all servers available
    for (Server s : qi.getServers()) {
      // print out the ID and name of the server
      System.out.println("Server (" + s.getID() + "): '" + s.getName() + "'");
      for (Channel c : s.getChannels()) {
        // print out ID and name of the channels belonging to Server s
        System.out.println(" - Channel (" + c.getID() + "): " + c.getName());
      }
    }
  }
}
