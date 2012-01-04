package client;

import java.io.IOException;

import physics.Console;

import newNet.Message;
import newNet.Network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class Net {
	class HEYLISTEN extends Listener {
		@Override
		public void connected(Connection connection) {
			Console.dbg.println("WHAT!");
		}
		
		@Override
		public void disconnected(Connection connection) {
			
		}
		
		@Override
		public void received(Connection connection, Object o) {
			
		}
	}
	
	Client cl;
	public Net() {
		cl = new Client();
		Network.register(cl.getKryo());
		cl.addListener(new HEYLISTEN());
		//cl.start();
	}
	
	String host; int tcpPort; int udpPort;
	public void connect(String h, int t, int u) {
			host = h; tcpPort = t; udpPort = u;
			System.out.println("CONNETICAL!");
			new Thread("CON"){
				public void run() {
					try {
						cl.connect(5000, host, tcpPort, udpPort);
					} catch (Exception e) {
						System.out.println(this.toString() + " " + cl.getUpdateThread().toString());
						e.printStackTrace();
					}
				}
			}.start();
		
	}
	
	public void update() {
		try {
			cl.update(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void say(String s) {
		Message m = new Message(s);
		cl.sendTCP(m);
		m.print();
	}
}
