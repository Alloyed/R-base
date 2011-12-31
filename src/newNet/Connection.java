package newNet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import physics.Stage;
import physics.actors.Actor;

public class Connection extends Thread {
	DatagramSocket s;
	DatagramPacket send, receive;
	List<Buffer> received = new ArrayList<Buffer>();
	byte[] senData = new byte[256];
	byte[] getData = new byte[256];
	String ip; int port;
	public Stage stage;
	
	public Connection(String ip, int port) {
		stage = new Stage();
		try {
			s = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		receive = new DatagramPacket(getData, getData.length);
		this.ip = ip; this.port = port;
	}
	
	public void poll() {
		for(Buffer b : received) {
			while(b.hasNext()) {
				Object o = b.next();
				//TODO: assign to stage and stuff
				//FIXME: Put this/stage in network, so connectionless servers can use it too
				if(o instanceof Actor) {
					
				} else if(o instanceof RPC) {
					
				}
			}
		}
	}
	
	public void send() {
		
	}
	
	public void run() {
		try {
			send = new DatagramPacket(senData, senData.length, InetAddress.getByName(ip), port);
			send.setData(new byte[] { 12 });
			s.send(send);
			s.receive(send);
			if(send.getData()[0] == 12)
				while(true) {
					s.receive(receive);
					received.add(new Buffer(receive.getData()));
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class Client {
	public long stamp;
	public String id;
	
	public Client(long l, String id) {
		stamp = l;
		this.id = id;
	}
}
