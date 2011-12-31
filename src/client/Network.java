package client;

import java.net.DatagramPacket;
import java.util.LinkedList;

import client.ui.Loop;

import physics.Stage;
import physics.actors.Actor;

import newNet.*;


/*
 * Use this to interact between the networking thread and the main one.
 */
public class Network {
	Loop c;
	Connection connection;
	LinkedList<RPC> calls;
	
	public Network(Loop c) {
		this.c = c;
		calls = new LinkedList<RPC>();
	}
	
	public void connect(String ip, String port) {
		try {
			connection = new Connection(ip, Integer.parseInt(port), new StatusListener() {
				public void setStatus(boolean connected) {
					//TODO: move all the game starting stuff here
//					c.gooey.getController("connect")
//					.setCaptionLabel( connected ? "disconnect" : "connect");
				}
			});
			connection.start();
		} catch (Exception e) {
			;
		}
	}
	
	public void poll() {
		if (connection != null) {
			for (DatagramPacket p : connection.packets) {
				Buffer b = new Buffer(p.getData());
			}
			connection.packets.clear();
		}
	}
	
	public void send() {
		//Send state.
	}

	public void call(Stage stage, String string, Object[] objects) {
		RPC r = new RPC(stage, string, objects);
		r.call(stage);
		calls.add(r);
	}
	
	public void call(Actor actor, String string, Object[] objects) {
		RPC r = new RPC(actor, string, objects);
		r.call(actor.s);
		calls.add(r);
	}
}

