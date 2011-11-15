package server;

import java.io.IOException;

/*
 * All this thing does is wait for new clients.
 * Then ServerThread takes over for each one.
 * 
 * TODO: handle events, collate clients, provide global map, etc.
 */

public class Server {
	Network n;
	
	Server(int port) throws IOException {
		n = new Network(port);
		System.out.println("Server Successfully Started.");
	}
	
	public static void main(String[] args) throws IOException {
		Server s = new Server(9001);
	}
}
