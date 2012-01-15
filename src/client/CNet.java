package client;

import newNet.Message;
import newNet.Player;
import physics.AddDef;
import physics.Console;
import physics.actors.Actor;
import physics.actors.Snapshot;
import client.ui.Loop;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * The client class.
 *
 */
public class CNet extends newNet.Net {
	class HEYLISTEN extends Listener {
		@Override
		public void connected(Connection connection) {
			//WHY THE FUCK DID IT TAKE ME THIS LONG TO REALIZE THIS LINE WASN'T HERE FGJAODISAJFDFGAKFJSHJHKJFHSDKDFALSFDJKH
			space.addConnection(connection);
			//Send them our player, to be validated
			connection.sendTCP(us);
		}
		
		@Override
		public void disconnected(Connection connection) {
			//TODO: clean everything up
			space.removeConnection(connection);
		}
		
		@Override
		public void received(Connection connection, Object o) {
			if (o instanceof Snapshot) {
				Snapshot s = (Snapshot) o;
				Console.dbg.println(s.actor + " " + stage.get(s.actor) + " " + s.pos);
				s.develop((Actor)stage.get(s.actor));
			} else if (o instanceof Player) {
				Player p = (Player) o;
				//FIXME: this will break if two people are named the same thing
				if (!addPlayer(p) && us.name.equals(p.name)) {
					us = p;
				}
			} else if (o instanceof Message) {
				Message m = (Message) o;
				messages.add(m);
				m.print();
			} else if (o instanceof AddDef) {
				AddDef ad = (AddDef)o;
				stage.addDef(ad);
			}
		}
	}
	
	Loop l;
	public Client cl;
	public Player us;
	public CNet(Loop l) {
		this.l = l;
		cl = new Client();
		end = cl;
		register();
		setStage(l.stage);
		us = new Player();
		
		us.name = l.settings.USERNAME;
		cl.addListener(new HEYLISTEN());
		cl.start();
	}
	
	String host; int tcpPort; int udpPort;
	public void connect(String h, int t, int u) {
			host = h; tcpPort = t; udpPort = u;
			new Thread("CONNECT"){
				public void run() {
					try {
						cl.connect(5000, host, tcpPort, udpPort);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		
	}
	
	public void say(String s) {
		Message m = new Message(us.name, s);
		cl.sendTCP(m);
		m.print();
	}
}
