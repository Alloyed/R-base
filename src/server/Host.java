package server;

import java.net.InetAddress;

class Host {
	public InetAddress ip;
	public int port;
	
	Host(InetAddress ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public String toString() {
		return ip.getHostAddress()+" "+port;
	}
}