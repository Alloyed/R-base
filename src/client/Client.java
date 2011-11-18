package client;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

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
		key = new byte[16];
		p = new DatagramPacket(key, key.length);
		
		requestKey(ip);
	}
	
	public void requestKey(InetAddress ip) throws IOException {
		s.send(new DatagramPacket(new byte[] {70}, 1, i, port));
		s.setSoTimeout(5000);
		System.out.println(s.getSoTimeout());
		s.receive(p);
		key = p.getData();
		System.out.println("Server response: "+getKey());
	}
	
	public String getKey() {
		String s = "";
		for(byte k : key) {
			s += ""+k;
		}
		return s;
	}
	
	public void sendEvent(PlayerState p) throws IOException {
		ArrayList<Byte> flexBuf = new ArrayList<Byte>();
		
		for(byte b : key)
			flexBuf.add(b);
		for(byte b : p.getBytes())
			flexBuf.add(b);
				
		byte[] buf = new byte[flexBuf.size()];
		for(int i = 0; i < flexBuf.size(); i++)
			buf[i] = flexBuf.get(i);
		
		s.send(new DatagramPacket(buf, buf.length));
	}
}
