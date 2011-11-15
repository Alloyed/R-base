package client;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class Client {
	DatagramSocket s;
	InetAddress i;
	int port;
	
	Client(InetAddress ip, int port) throws IOException {
		i = ip;
		this.port = port;
		
		s = new DatagramSocket(port, i);
	}
	
	public boolean pollServer() {
		return false;
	}
	
	public void sendEvent(String e) throws IOException {
		byte[] buf = e.getBytes();
		s.send(new DatagramPacket(buf, buf.length));
	}

	/* Stupid char-to-string doesn't-work-in-Java
	 * failure-of-everything */
	public void sendEvent(char c) throws IOException {
		byte[] buf = Character.toString(c).getBytes();
		s.send(new DatagramPacket(buf, buf.length));
	}
}
