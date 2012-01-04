package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.URL;
import java.net.URLConnection;

import newNet.Player;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import physics.*;

/*
 * All this thing does is wait for new clients.
 * Then ServerThread takes over for each one.
 * 
 * TODO: handle events, collate clients, provide global map, etc.
 */

public class Main {
	Stage main; //we might need extra states to emulate the client's state
	Network net;
	public Main() {
		net = new Network();
		try {
			net.start(net.TCP, net.UDP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//Main game loop. Recall to restart game.
	public void game() {
		main = new Stage();
//		long seed = System.currentTimeMillis();
		try {
			//
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Console.out.println("Game started.");
		while (true) {
			//Send state to clients here.
			try { Thread.sleep((long) Stage.frame * 1000); } 
			catch (Exception e) {}
			main.step(Stage.frame);
			if (!main.won()) {
				Console.out.println("Game finished, restarting.");
				//TODO: empty state across all clients
				break;
			}
		}
	}
	

	public static void main(String[] args) throws IOException {
		Main s = new Main();
		while (true) {
			s.game();
		}
	}
}
