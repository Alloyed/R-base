package server;

import java.io.IOException;
import java.net.DatagramPacket;

/*
 * All this thing does is wait for new clients.
 * Then ServerThread takes over for each one.
 * 
 * TODO: handle events, collate clients, provide global map, etc.
 */

public class Server {
	Network n;
	static Server s;
	
	Server(int port) throws IOException {
		n = new Network(port);
		System.out.println("Server Successfully Started.");
		n.run();
	}
	
	public static void giveEvent(DatagramPacket p) {
		if(!p.equals(null)) {
			//TODO: process events
			return;
		}
	}
	
	public static void main(String[] args) throws IOException {
		s = new Server(9001);
	}
}
