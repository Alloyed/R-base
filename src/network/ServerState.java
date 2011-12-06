package network;

import java.util.ArrayList;
/* NOTE: I have no idea what going on here. This is never used, so I don't how it would be.
 * besides, the server really shouldn't be serializable, the stage should.
 *   --kyle
 */
public class ServerState implements State {
	public ArrayList<State> clients;
	
	public ServerState() {
		clients = new ArrayList<State>();
	}

	@Override
	public byte[] getBytes() {
		ArrayList<Byte> buf = new ArrayList<Byte>();
		
		for(State p : clients)
			for(byte b : p.getBytes())
				buf.add(b);
		
		byte[] b = new byte[buf.size()];
		for(int i = 0; i < buf.size(); i++)
			b[i] = buf.get(i);
		
		return b;
	}

	@Override
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

	@Override
	public void set(byte[] b) {
		// TODO Auto-generated method stub
		
	}
}
