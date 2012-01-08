package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.jbox2d.common.Vec2;

import newNet.Message;
import newNet.Player;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import physics.*;
import physics.actors.Actor;
import physics.actors.PlayerState;
import physics.actors.Snapshot;
import physics.map.Map;

/*
 * All this thing does is wait for new clients.
 * Then ServerThread takes over for each one.
 * 
 * TODO: handle events, collate clients, provide global map, etc.
 */

public class Main {
	Stage main; //we might need extra states to emulate the client's state
	SNet net;
	public Main() {
		net = new SNet();
		try {
			net.start(net.TCP, net.UDP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	int ind = 0;
	public void game() {
		net.mesg("Waiting for at least two players");
		while (net.players.size() < 2) {}
		net.mesg("Players found, Game started.");
		int time = 0;
		main = new Stage();
		net.stage = main;
		Map m = (Map) main.addActor(Map.class, new Vec2(0, 0), new Vec2(0, 0));
		m.startGame(1234l);
		while (true) {
			//Send state to clients here.
			/*
			if (time % 10 == 0) {
				
			if ( main.activeActors.size() > 0)
			for (int i = 0; i < 25; ++i) {
				ind = (ind+1)%main.activeActors.size();
				Snapshot s = main.activeActors.get(i).state;
				net.serv.sendToAllUDP(s);
			}
			}
			time++;
			*/
			for (Player p : net.players) {
				net.waitingfor.add(p);
			}
			
			try { Thread.sleep((long) Stage.frame * 1000); }
			catch (Exception e) {}
			
			while (!net.waitingfor.isEmpty()) {}
			for (Player p : net.players) {
				Actor a = main.actors.get(p.id);
				if (a != null) {
					net.serv.sendToAllExceptTCP(p.from.getID(), (PlayerState) a.state);
				}
			}
			
			main.step(Stage.frame);
			if (!main.won()) {
				net.mesg("Game finished, restarting.");
				//TODO: empty state across all clients
				break;
			}
		}
	}
	
	static Main s;
	public static void main(String[] args) throws IOException {
		s = new Main();
		new Thread ("GAME") {
			public void run() {
				while (true) {
					s.game();
				}
			}
		}.start();
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print(">");
			String str = sc.nextLine();
			if (str.contains("players"))
				System.out.println(s.net.players);
		}
	}
}
