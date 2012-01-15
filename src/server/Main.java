package server;

import java.io.IOException;

import org.jbox2d.common.Vec2;

import newNet.Player;
import physics.AddDef;
import physics.Stage;
import physics.Stagelike;
import physics.actors.Actor;
import physics.actors.PlayerState;
import physics.map.IMap;
import physics.map.Map;

import com.esotericsoftware.kryonet.rmi.ObjectSpace;

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
		main = new Stage();
		main.space = net.space;
		net.stage = main;
		net.mesg("Waiting for at least 1 players");
		while (net.players.size() < 1) { try {Thread.sleep(100);} catch (Exception e) {} }
		net.mesg("Players found, Game started.");
		AddDef map = new AddDef(Map.class, new Vec2(0, 0), new Vec2(0, 0));
		map.id = main.getNewId();
		for (Player p : net.players) {
			p.currentMode = 2;
			p.from.sendTCP(map);
		}
		for (Player p : net.players) {
			IMap a = ObjectSpace.getRemoteObject(p.from, map.id, IMap.class);
			a.startGame(12345l);
		}
		main.addDef(map);
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
			
			//Wait to get all the players input
			while (!net.waitingfor.isEmpty()) {}
			//Send the input back out
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
	}
}
