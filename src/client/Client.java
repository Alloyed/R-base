package client;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

import physics.PlayerState;

public class Client {
	public DatagramSocket s;
	public DatagramPacket p;
	public InetAddress i;
	public int port;
	private byte[] key;
	
	Client(InetAddress ip, int port) throws IOException {
		i = ip;
		this.port = port;
		s = new DatagramSocket();
		key = new byte[256];
		p = new DatagramPacket(key, key.length);
		
		requestKey(ip);
	}
	
	public void requestKey(InetAddress ip) throws IOException {
		s.send(new DatagramPacket(new byte[] {70}, 1, i, port));
		s.setSoTimeout(5000);
		System.out.println(s.getSoTimeout());
		s.receive(p);
		key = p.getData();
		System.out.println();
	}
	
	public String getKey() {
		String s = "";
		for(byte k : key) {
			s += ""+k;
		}
		return s;
	}
	
	public void sendEvent(PlayerState p) throws IOException {
		byte[] buf = p.getBytes();
		s.send(new DatagramPacket(buf, buf.length));
	}

	/* Stupid char-to-string doesn't-work-in-Java
	 * failure-of-everything */
	public void sendEvent(char c) throws IOException {
		byte[] buf = Character.toString(c).getBytes();
		s.send(new DatagramPacket(buf, buf.length));
	}
}
