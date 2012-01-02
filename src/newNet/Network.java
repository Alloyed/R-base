package newNet;

import physics.Message;
import physics.actors.PlayerState;

import com.esotericsoftware.kryo.Kryo;

/* put things common between the two networking classes here maybe? */
public class Network {
	
	public Network() {
	}
	
	public static void register(Kryo k) {
		k.register(PlayerState.class);
		k.register(Message.class);
	}
}
