package client;

import java.util.Random;

import physics.Console;
import physics.actors.Actor;
import physics.actors.Snapshot;

import newNet.Message;
import newNet.Net;
import newNet.Player;

import client.ui.Loop;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class CNet extends newNet.Net {
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
			if (o instanceof Snapshot) {
				Snapshot s = (Snapshot) o;
				Console.dbg.println(s.actor + " " + l.stage.get(s.actor) + " " + s.pos);
				s.develop((Actor)l.stage.get(s.actor));
			} else if (o instanceof Player) {
				Player p = (Player) o;
				if (!addPlayer(p) && us.name.equals(p.name)) {
					us = p;
				}
			} else if (o instanceof Message) {
				Message m = (Message) o;
				messages.add(m);
				m.print();
			}
		}
	}
	
	Loop l;
	public Client cl;
	public Player us;
	public CNet(Loop l) {
		this.l = l;
		cl = new Client();
		Net.register(cl.getKryo());
		us = new Player();
		
		us.name = "faggot"+ new Random().nextInt(100);
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
