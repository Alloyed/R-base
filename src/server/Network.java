package server;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import newNet.Message;
import newNet.Player;
import physics.Console;
import physics.actors.PlayerState;

import com.esotericsoftware.kryonet.*;


public class Network extends newNet.Network {
	Server serv;
	String serverName; //you know, [CTF] Unlimited respawns No Scrubs Allowed awesumservers.net [CTF] 
	URL master;
	URLConnection c;
	class HEYLISTEN extends Listener {
		@Override
		public void connected(Connection connection) {
			Console.dbg.println("WHAT! " + connection.toString());
		}
		
		@Override
		public void disconnected(Connection connection) {
			
		}
		
		@Override
		public void received(Connection connection, Object o) {
			if (o instanceof Player) {
				Player p = (Player) o;
				//If this player is already part of the playerlist, add the changes
				//Else, give the player an ID etc.
			} else if (o instanceof Message) {
				Message m = (Message) o;
			} else if (o instanceof PlayerState) {
				PlayerState ps = (PlayerState) o;
			}
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