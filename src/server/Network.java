package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/* TODO: handle events, respond to the client. */

public class Network {
	DatagramSocket socket;
	DatagramPacket p;
	ArrayList<DatagramSocket> clients;
	byte[] buf = new byte[256];
	
	Network(int port) throws IOException {
		socket = new DatagramSocket(port);
		p = new DatagramPacket(buf, buf.length);
		clients = new ArrayList<DatagramSocket>();
	}
	
	public DatagramSocket getClientByIP(InetAddress ip) throws IOException {
		for(DatagramSocket s : clients)
			if(s.getInetAddress() == ip)
				return socket;
		
		return null;
	}
	
	public DatagramPacket getEvent() throws IOException {
		socket.receive(p);
		return p;
	}
	
	void handleEvent(String s) {
		// TODO: this stuff
	}
}
