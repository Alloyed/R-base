package client;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

public class Client {
	public DatagramSocket s;
	public DatagramPacket p;
	public InetAddress i;
	public int port;
	private byte[] key, buf;
	
	Client(InetAddress ip, int port) throws IOException {
		i = ip;
		this.port = port;
		s = new DatagramSocket(port, i);
		
		buf = new byte[256];
		p = new DatagramPacket(buf, buf.length);
		
		requestKey(ip);
	}
	
	public void requestKey(InetAddress ip) throws IOException {
		s.send(new DatagramPacket("new".getBytes(), "new".getBytes().length, i, port));
		s.setSoTimeout(5000);
		p = new DatagramPacket(new byte[]{},0);
		s.receive(p);
		key = p.getData();
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
