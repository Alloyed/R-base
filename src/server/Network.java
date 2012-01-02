package server;

import java.io.IOException;

import com.esotericsoftware.kryonet.*;


public class Network extends newNet.Network {
	Main server;
	Server s;
	
	Network(Main mn, int port) throws IOException {
		s = new Server();
		s.bind(9001, 9002);
		register(s.getKryo());
	}
	
	public void start() {
		
	}
}