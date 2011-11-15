package server;

import java.io.IOException;

/* This thing creates the Network server. Then handles all
 * of the events it passes here by polling Network.
 * 
 * TODO: handle events, collate clients, provide global map, etc.
 */

public class Server {
	Network n;
	
	Server(int port) throws IOException {
		n = new Network(port);
	}
	
	public void run() throws IOException {
		n.getEvent();
	}
	
	public static void main(String[] args) throws IOException {
		Server s = new Server(9001);
		
		s.run();
	}
}
