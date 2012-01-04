package server;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import newNet.Message;
import newNet.Player;
import physics.Console;
import physics.Stage;
import physics.actors.PlayerState;

import com.esotericsoftware.kryonet.*;


public class Network extends newNet.Network {
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
		}
		
		@Override
		public void received(Connection connection, Object o) {
			if (o instanceof Player) {
				Console.dbg.println("Player recieved");
				parseIn(connection, (Player) o);
			} else if (o instanceof Message) {
				Message m = (Message) o;
				m.print();
			} else if (o instanceof PlayerState) {
				PlayerState ps = (PlayerState) o;
			}
		}
	}
	
	public void parseIn(Connection c, Player p) {
		boolean exists = false;
		for (Player i : players) {
			if (p.equals(i)) {
				exists = true;
				i.set(p);
				break;
			}
		}
		
		if (!exists) {
			p.from = c;
			p.id = Stage.getNewId();
			serv.sendToAllTCP(p);
			players.add(p);
		}
	}
	
	public void start(int tcpPort, int udpPort) throws IOException {
		serv = new Server();
		serv.bind(tcpPort, udpPort);
		Network.register(serv.getKryo());
		serv.addListener(new HEYLISTEN());
		serv.start();
		Console.dbg.println("Server Started");
	}
	
	public boolean changeAvail(boolean avail) {
		try {
			master = new URL("http://shsprog.com/wp-content/uploads/servers.php?ip="+serverName+"&avail="+(avail?"true":"false"));
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
}