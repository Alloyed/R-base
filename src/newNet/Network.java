package newNet;

import physics.Stage;

/*put things common between the two networking classes here maybe?*/
public class Network extends Thread {
	public Object call(Stage obj, String method, Object[] args) {
		RPC r = new RPC(obj,method,args);
		//TODO: store the RPC for later.
		return r.call(obj);
	}
}
