package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

/* TODO: handle events, respond to the client. */

public class Network extends Thread {
	DatagramSocket socket;
	DatagramPacket p;
	ArrayList<byte[]> keys;
	byte[] buf = new byte[256];
	
	Network(int port) throws IOException {
		socket = new DatagramSocket(port);
		p = new DatagramPacket(buf, buf.length);
		keys = new ArrayList<byte[]>();
	}
	
	public void run() {
		while(true) {
			try {
				Server.giveEvent(getEvent());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public DatagramPacket getEvent() throws IOException {
		socket.receive(p);
		if(p.getData() == "new".getBytes()) {
			int key = (int) (Math.random()* 9854764);
			byte[] nextKey = (""+key).getBytes();
			keys.add(nextKey);
			socket.send(new DatagramPacket(nextKey, nextKey.length, p.getAddress(), p.getPort()));
			System.out.println("New client at: "+p.getAddress());
			return null;
		}
		return p;
	}
	
	void handleEvent(String s) {
		// TODO: this stuff
	}
}
