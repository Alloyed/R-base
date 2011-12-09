package network;

import java.util.ArrayList;

import physics.Stage;
/* NOTE: I have no idea what going on here. This is never used, so I don't how it would be.
 * besides, the server really shouldn't be serializable, the stage should.
 *   --kyle
 */
public class ServerState {
	Stage own;
	public ArrayList<Stage> clients;
	
	public ServerState(Stage own) {
		this.own = own;
		clients = new ArrayList<Stage>();
	}

	public byte[] getBytes(int client) {
		ArrayList<Byte> buf = new ArrayList<Byte>();
		
		Stage s = clients.get(client);
		//find most important difference between own and s
		//Store so we can apply later
		byte[] b = new byte[buf.size()];
		for(int i = 0; i < buf.size(); i++)
			b[i] = buf.get(i);
		
		return b;
	}

	public Object getParam(String param, byte[] b) {
		try {
			int i = Integer.parseInt(param);
			byte[] data = new byte[PlayerState.byteSize];
			for(int k = i*PlayerState.byteSize; k < i*PlayerState.byteSize+PlayerState.byteSize; k++) {
				data[i] = b[k];
			}
			return null;//WAS: add a player?
		} catch (Exception e) {
			//Handle problem
		}
		
		return null;
	}

	public void addParam(byte[] b) {
		clients.add(null); //WAS: add a player?
	}

	public void set(byte[] b) {
		// TODO
		// find the client it's from
		// if the client is already ahead of it, discard
		// Else parse into client
		// make local copy of client, tick until at same frame as own
		// parse anything the packet changed into own
		
	}
}
