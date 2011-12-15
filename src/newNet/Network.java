package newNet;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import physics.Stage;

/* put things common between the two networking classes here maybe? */
public class Network {
	Connection c;
	List<RPC> rpcs;
	long lastTimeStamp;
	
	public Network() {
		rpcs = new ArrayList<RPC>();
	}
	
	public Object call(Stage obj, String method, Object[] args) {
		RPC r = new RPC(obj,method,args);
		rpcs.add(r);
		
		return r.call(obj);
	}
	
	public void poll() { c.poll(); }
	public void send() { c.send(); }
	
	public boolean connect(String ip, int port) throws UnknownHostException {
		c = new Connection(ip, port);
		c.start();
		
		return false;
	}
}
