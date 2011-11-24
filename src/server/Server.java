package server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

/*
 * All this thing does is wait for new clients.
 * Then ServerThread takes over for each one.
 * 
 * TODO: handle events, collate clients, provide global map, etc.
 */

public class Server {
	Network n;
	static Server s;
	URL master;
	URLConnection c;
	String ip;
	
	Server(int port) throws IOException {
		n = new Network(port);
		System.out.println("Server Successfully Started.");
		n.run();

		master = new URL("http://shsprog.com/servers.php");
		ip = InetAddress.getLocalHost().getHostAddress();
		c = master.openConnection();
		
		changeAvail(true);
	}
	
	public boolean changeAvail(boolean avail) {
		try {
			c.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(c.getOutputStream(), "UTF-8");
			String xml = ip+" "+(avail?"true":"false");
			out.write(xml);
			out.close();
			
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public static void giveEvent(DatagramPacket p) {
		if(p != null) {
			return;
		} else {
			//TODO: process events into current state.
		}
	}
	
	public static void main(String[] args) throws IOException {
		s = new Server(9001);
	}
}
