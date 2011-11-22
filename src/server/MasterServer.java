package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class MasterServer {
	public ArrayList<Host> servers;
	public DatagramSocket s;
	
	MasterServer(int port) throws IOException {
		servers = new ArrayList<Host>();
		s = new DatagramSocket();
	}
	
	public void addServer(InetAddress ip, int port) {
		servers.add(new Host(ip, port));
	}
	
	public void run() throws IOException {
		byte[] buf = new byte[256];
		DatagramPacket p = new DatagramPacket(buf, buf.length);
		s.receive(p);
		addServer(p.getAddress(), p.getPort());
		p.setData(new byte[] {0});
		s.send(p);
	}
	
	public ArrayList<Host> getServers() {
		return servers;
	}
	
	public static void main(String[] args) throws IOException {
		MasterServer m = new MasterServer(9001);
		m.run();
	}

}
