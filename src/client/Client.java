package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
	DatagramSocket s;
	InetAddress dest;
	int port;
	
	Client(String ip, int port) throws IOException {
		dest = InetAddress.getByName(ip);
		this.port = port;

		s = new DatagramSocket(port, InetAddress.getByName(ip));
	}
	
	public boolean sendEvent(String e) throws IOException {
		byte[] buf = e.getBytes();
		s.send(new DatagramPacket(buf, buf.length, dest, port));
		
		return false;
	}
	
	public void getState() {
		
	}
}
