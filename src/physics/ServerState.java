package physics;

import java.util.ArrayList;

public class ServerState implements State {
	public ArrayList<PlayerState> clients;
	
	public ServerState() {
		clients = new ArrayList<PlayerState>();
	}

	@Override
	public byte[] getBytes() {
		ArrayList<Byte> buf = new ArrayList<Byte>();
		
		for(PlayerState p : clients)
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
			return new PlayerState(data);
		} catch (Exception e) {
			//Handle problem
		}
		
		return null;
	}

	public void addParam(byte[] b) {
		clients.add(new PlayerState(b));
	}
}
