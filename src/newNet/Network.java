package newNet;

import java.util.ArrayList;

import physics.Message;
import physics.Team;
import physics.actors.*;
import com.esotericsoftware.kryo.Kryo;

/* put things common between the two networking classes here maybe? */
public class Network {
	public ArrayList<Player> players = new ArrayList<Player>();
	
	public Network() {
	}

	public static void register(Kryo k) {
		k.register(Team.class);
		k.register(Player.class);
		k.register(Message.class);
		k.register(Snapshot.class);
		k.register(PlayerState.class);
	}
}
