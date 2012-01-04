package newNet;

import java.util.ArrayList;

import physics.Team;
import physics.actors.*;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/* put things common between the two networking classes here maybe? */
public class Network {
	EndPoint end;
	public ArrayList<Player> players = new ArrayList<Player>();
	
	public static void register(Kryo k) {
		k.register(Team.class);
		k.register(Player.class);
		k.register(Message.class);
		k.register(Snapshot.class);
		k.register(PlayerState.class);
	}
	

}
