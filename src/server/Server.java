package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/*
 * All this thing does is wait for new clients.
 * Then ServerThread takes over for each one.
 * 
 * TODO: handle events, collate clients, provide global map, etc.
 */

public class Server {
	public static void main(String[] args) {

	ServerSocket s;
	ArrayList<ServerThread> clients;
	
	Server(int port) throws IOException {
		s = new ServerSocket(port);
		clients = new ArrayList<ServerThread>();
	}
	
	void loop() throws IOException {
		while(true) {
			clients.add(new ServerThread(s.accept()) {{ run(); }});
		}
	}
	
	public static void main(String[] args) throws IOException {
		Server s = new Server(9001);
		
		s.loop();
	}
}
