package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.URL;
import java.net.URLConnection;

import physics.*;

/*
 * All this thing does is wait for new clients.
 * Then ServerThread takes over for each one.
 * 
 * TODO: handle events, collate clients, provide global map, etc.
 */

public class Server {
	Network n;
	URL master;
	URLConnection c;
	String ip;
	Stage main; //we might need extra states to emulate the client's state
	
	Server(int port) throws IOException {
		n = new Network(this, port);
		Console.out.println("Server Successfully Started.");
		n.start();
		
		changeAvail(true);
	}

	//Main game loop. Recall to restart game.
	public void game() {
		main = new Stage();
		long seed = System.currentTimeMillis();
		try {
			n.callback(main, "startGame", new Object[] {seed}, true);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Console.out.println("Game started.");
		while (true) {
			//Send state to clients here.
			try { Thread.sleep((long) Stage.frame * 1000); } 
			catch (Exception e) {}
			main.step();
			if (main.won()) {
				Console.out.println("Game finished, restarting.");
				//TODO: empty state across all clients
				break;
			}
		}
	}
	
	public boolean changeAvail(boolean avail) {
		try {
			master = new URL("http://shsprog.com/wp-content/uploads/servers.php?ip="+ip+"&avail="+(avail?"true":"false"));
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public void giveEvent(DatagramPacket p) {
		if(p != null) {
			return;
		} else {
			//TODO: process events into current state.
		}
	}
	
	public static void main(String[] args) throws IOException {
		Server s = new Server(9001);
		while (true) {
			s.game();
		}
	}
}
