package newNet;

import physics.Team;

import com.esotericsoftware.kryonet.Connection;

/**
 * A player from the perspective of the networking code.
 * 
 * @author kyle
 *
 */

public class Player {
	public String name;
	public transient Connection from; //Clients don't need this
	public int id;
	public Team team;
	public int currentMode, wantedMode; //use the statebasedgame ids here
	public int respawnCounter;
	
	@Override
	public boolean equals(Object p2) {
		return p2 instanceof Player && (id == ((Player)p2).id);
	}
	
	public void set(Player p2) {
		name = p2.name;
		id = p2.id;
		team = p2.team;
		currentMode = p2.currentMode;
		wantedMode = p2.wantedMode;
		respawnCounter = p2.respawnCounter;
	}
	
	//Like set, but for the server
	public void update(Player p2) {
		name = p2.name;
		team = p2.team;
		currentMode = p2.currentMode;
	}
	
	@Override
	public String toString() {
		return name + ", id " + id;
	}
}
