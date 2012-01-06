package client;

import physics.Console;
import physics.actors.Actor;
import physics.actors.Snapshot;

import newNet.Message;
import newNet.Network;
import newNet.Player;

import client.ui.Loop;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class Net extends newNet.Network {
	class HEYLISTEN extends Listener {
		@Override
		public void connected(Connection connection) {
			Console.dbg.println("WHAT!");
			connection.sendTCP(us);
		}
		
		@Override
		public void disconnected(Connection connection) {
			
		}
		
		@Override
		public void received(Connection connection, Object o) {
			Console.dbg.println("DASDADA");
			if (o instanceof Snapshot) {
				Snapshot s = (Snapshot) o;
				Console.dbg.println(s.actor + " " + l.stage.get(s.actor));
				s.develop((Actor)l.stage.get(s.actor));
			}
		}
	}
	
	Loop l;
	Client cl;
	Player us;
	public Net(Loop l) {
		this.l = l;
		cl = new Client();
		Network.register(cl.getKryo());
		us = new Player();
		
		us.name = "faggot";
		cl.addListener(new HEYLISTEN());
		cl.start();
	}
	
	String host; int tcpPort; int udpPort;
	public void connect(String h, int t, int u) {
			host = h; tcpPort = t; udpPort = u;
			new Thread("CON"){
				public void run() {
					try {
						cl.connect(5000, host, tcpPort, udpPort);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		
	}
	
	public void update() {
		/*
		try {
			//if (cl.isConnected())
				cl.update(0);
=======
		try {
			cl.update(0);
>>>>>>> eb0e9fbd738fbfe2555b133678eec47b3a198aae
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
<<<<<<< HEAD
		*/
	}
	
	public void say(String s) {
		Message m = new Message(s);
		cl.sendTCP(m);
		m.print();
	}
}
