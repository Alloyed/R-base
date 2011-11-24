package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

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
		
		changeAvail(true);
	}
	
	public boolean changeAvail(boolean avail) {
		try {
			master = new URL("http://idontknow/wp-content/uploads/servers.php?ip="+ip+"&avail="+(avail?"true":"false"));
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
