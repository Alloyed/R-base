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
	String name;
	Connection from; //Clients ignore this
	int id;
	Team team;
	int currentMode, wantedMode; //use the statebasedgame ids here
	int respawnCounter;
	
	public Player() {
		
	}
	
	@Override
	public boolean equals(Object p2) {
		return p2 instanceof Player && (id == ((Player)p2).id);
	}
}
