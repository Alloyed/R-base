package server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;

/*
 * All this thing does is wait for new clients.
 * Then ServerThread takes over for each one.
 * 
 * TODO: handle events, collate clients, provide global map, etc.
 */

public class Server {
	Network n;
	static Server s;
	
	Server(int port) throws IOException {
		n = new Network(port);
		System.out.println("Server Successfully Started.");
		n.run();
	}
	
	public static void giveEvent(DatagramPacket p) {
		if(p != null) {
			return;
		} else {
			//TODO: process events into current state.
		}
	}
	
	public Object callback(String object, String method, Object[] args) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<?> c = Class.forName(object, true, null);
		Class[] partypes = new Class[args.length];
		for(int i = 0; i < args.length; i++)
			partypes[i] = args[i].getClass();
		Method m = c.getMethod(method, partypes);
		Object o = m.invoke(c, args);
		return o;
	}
	
	public static void main(String[] args) throws IOException {
		s = new Server(9001);
	}
}
