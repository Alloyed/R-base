package server;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

import newNet.Message;
import newNet.Player;
import physics.Console;
import physics.actors.Actor;
import physics.actors.PlayerState;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class SNet extends newNet.Net {
	Server serv;
	String serverName; //you know, [CTF] Unlimited respawns No Scrubs Allowed awesumservers.net [CTF]
	String motd; 
	URL master;
	URLConnection c;
	
	class HEYLISTEN extends Listener {
		@Override
		public void connected(Connection connection) {
			Console.dbg.println(connection.getID() + " connected! " + connection.toString());
		}
		
		@Override
		public void disconnected(Connection connection) {
			Console.dbg.println(connection.getID() + " disconnected! " + connection.toString());
			
			Player toRemove = null;
			for (Player p : players) {
				if (p.from.getID() == connection.getID()) {
					toRemove = p;
					break;
				}
			}
			if (toRemove != null) {
				players.remove(toRemove);
				
			}
		}
		
		@Override
		public void received(Connection connection, Object o) {
			if (o instanceof Player) {
				Console.dbg.println("Player recieved");
				parseIn(connection, (Player) o);
			} else if (o instanceof Message) {
				Message m = (Message) o;
				messages.add(m);
				serv.sendToAllExceptTCP(connection.getID(), m);
				m.print();
			} else if (o instanceof PlayerState && stage != null) {
				PlayerState ps = (PlayerState) o;
				Actor a = stage.actors.get(ps.actor);
				if (a != null)
					ps.develop(a);
				waitingfor.remove(getPlayer(ps.actor));
			}
		}
	}
	
	public void parseIn(Connection c, Player p) {
		if (!addPlayer(p)) {
			p.from = c;
			p.id = stage.getNewId();
			space.addConnection(c);
			for (Player i: players)
				if (i != p)
					c.sendTCP(i);
			for (Message m : messages)
				c.sendTCP(m);
			serv.sendToAllTCP(p);
			mesg("Player " + p + " has joined the game");
		}
	}
	
	public void start(int tcpPort, int udpPort) throws IOException {
		serv = new Server();
		end = serv;
		register();
		
		serv.bind(tcpPort, udpPort);
		serv.addListener(new HEYLISTEN());
		serv.start();
		
		waitingfor = new LinkedList<Player>();
		Console.dbg.println("Server Started");
	}
	
	public boolean changeAvail(boolean avail) {
		try {
			master = new URL("http://shsprog.com/wp-content/uploads/servers.php?ip="+serverName+"&avail="+(avail?"true":"false"));
			URLConnection MC = master.openConnection();
			MC.connect();
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	//Send a server message
	public void mesg(String body) {
		Message m = new Message("Server", body);
		serv.sendToAllTCP(m);
		messages.add(m);
		m.print();
	}
	
}