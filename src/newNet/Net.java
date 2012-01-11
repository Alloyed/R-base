package newNet;

import java.util.ArrayList;
import java.util.LinkedList;

import org.jbox2d.common.Vec2;

import physics.Stage;
import physics.Team;
import physics.actors.*;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

/**
 * put things common between the two networking classes here maybe? 
 */
public class Net {
	public EndPoint end;
	public Stage stage;
	
	//FIXME: Used for the current lockstep TCP method.
	public LinkedList<Player> waitingfor = new LinkedList<Player>(); 
	//The default ports, put here out of laziness
	public final int UDP = 9002, TCP = 9001;
	public ArrayList<Player> players = new ArrayList<Player>();
	public ArrayList<Message> messages = new ArrayList<Message>();
	/**
	 * Using this is nontrivial, so here's how it works:
	 * //assigns ourThing the ID of 42 to be called from the other side
	 * objectSpace.register(42, ourThing);
	 * //Lets connection access our objectSpace
	 * objectSpace.addConnection(connection); 
	 * //Gets the equivalent Thing from the connection's side, assuming they've added us
	 * theirThing = ObjectSpace.getRemoteObject(connection, 42, Thing.class);
	 * //Which can then be used like a normal object 
     * theirThing.do();
	 */
	public ObjectSpace space;
	//ids > 0 are for Actors
	final int netID = 0, stageID = -1; 
	
	public void register() {
		Kryo k = end.getKryo();
		k.register(Vec2.class);
		k.register(Team.class);
		k.register(Player.class);
		k.register(Message.class);
		k.register(Snapshot.class);
		k.register(PlayerState.class);
		ObjectSpace.registerClasses(k);
		
		space = new ObjectSpace();
		space.register(netID, this);
	}
	
	public Player getPlayer(int id) {
		for (Player p : players) {
			if (p.id == id) {
				return p;
			}
		}
		return null;
	}
	
	public boolean addPlayer(Player newp) {
		for (Player p : players) {
			if (newp.equals(p)) {
				p.set(newp);
				return true;
			}
		}
		players.add(newp);
		return false;
	}
	
	public void removePlayer(int id) {
		Player toRemove = null;
		for (Player p : players) {
			if (p.id == id) {
				toRemove = p;
			}
		}
		if (toRemove != null) {
			players.remove(toRemove);
			space.removeConnection(toRemove.from);
		}
	}
	
	public void setStage(Stage s) {
		stage = s;
		space.register(stageID, s);
	}

}
