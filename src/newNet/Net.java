package newNet;

import java.util.ArrayList;
import java.util.LinkedList;

import org.jbox2d.common.Vec2;

import physics.Team;
import physics.actors.*;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/* put things common between the two networking classes here maybe? */
public class Net {
	EndPoint end;
	public LinkedList<Player> waitingfor = new LinkedList<Player>(); //temp
	public final int UDP = 9002, TCP = 9001;
	public ArrayList<Player> players = new ArrayList<Player>();
	public ArrayList<Message> messages = new ArrayList<Message>();
	
	public static void register(Kryo k) {
		k.register(Vec2.class);
		k.register(Team.class);
		k.register(Player.class);
		k.register(Message.class);
		k.register(Snapshot.class);
		k.register(PlayerState.class);
	}
	
	public Player getPlayer(int id) {
		for (Player i : players) {
			if (i.id == id) {
				return i;
			}
		}
		return null;
	}
	
	public boolean addPlayer(Player p) {
		for (Player i : players) {
			if (p.equals(i)) {
				i.set(p);
				return true;
			}
		}
		players.add(p);
		return false;
	}

}
