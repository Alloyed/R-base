package server;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Random;

/* TODO: handle events, Limit one connection per IP. */

public class Network extends Thread {
	DatagramSocket socket;
	DatagramPacket p;
	Server server;
	ArrayList<byte[]> keys;
	Random keygen = new Random();
	byte[] buf = new byte[256],
		initBytes = new byte[] {70},
		endBytes = new byte[] {07},
		nextKey = new byte[16];
	
	Network(Server s, int port) throws IOException {
		server = s;
		socket = new DatagramSocket(port);
		socket.setSoTimeout(0);
		p = new DatagramPacket(buf, buf.length);
		keys = new ArrayList<byte[]>();
	}
	
	public void run() {
		while(true) {
			try {
				server.giveEvent(getEvent());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean matchKeys(byte[] b) {
		for(byte[] key : keys)
			for(int i = 0; i < key.length; i++)
				if(key[i] != b[i])
					return false;
		return true;
	}
	
	public DatagramPacket getEvent() throws IOException {
		socket.receive(p);
		if(p.getData()[0] == initBytes[0]) {
			keygen.nextBytes(nextKey);
			keys.add(nextKey);
			socket.send(new DatagramPacket(nextKey, nextKey.length, p.getAddress(), p.getPort()));
			System.out.println("New client at: "+p.getAddress());
			return null;
		} else if(p.getData() == null || !matchKeys(p.getData()))
			return null;
		return p;
	}
	
	/*NOTE: args cannot be primitives*/
	public Object callback(Object object, String method, Object[] args, boolean sendCall) {
		Class<?>[] partypes = new Class<?>[args.length];
		for(int i = 0; i < args.length; i++)
			partypes[i] = args[i].getClass();
		Object o = null;
		try {
			Method m =  object.getClass().getDeclaredMethod(method, partypes);
			o = m.invoke(object, args);
			if(sendCall) {
				p.setData(m.getName().getBytes());
					socket.send(p);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return o;
	}
}
