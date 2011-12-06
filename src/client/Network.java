package client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import network.PlayerState;

import physics.Console;
import physics.Stage;
import physics.actors.Actor;
@SuppressWarnings("unused")
public class Network extends Thread {
	public static final float frame = 1/20f;
	public DatagramSocket s;
	public DatagramPacket p;
	public InetAddress i;
	public int port;
	private byte[] key;
	private ArrayList<Byte> flexBuf;
	private StatusListener status; //Returns status of connection to client
	private Stage stage; //known server state. May or may not be used
	private List<DatagramPacket> packets; //Stack of recieved packets
	
	public Network(String ip, int port, Stage stage, StatusListener l) {
		status = l;
		this.port = port;
		this.stage = new Stage(stage); //TODO
		try {
			i = InetAddress.getByName(ip);
			s = new DatagramSocket(); 
		} catch (Exception e) {
			e.printStackTrace(Console.dbg);
			status.setStatus(false);
			return;
		}
		packets = Collections.synchronizedList(new ArrayList<DatagramPacket>());
		key = new byte[16];
		p = new DatagramPacket(key, key.length);
		flexBuf = new ArrayList<Byte>();
	}
	
	public void run() {
		try {
			requestKey(i);
			status.setStatus(true);
			while (true) {
				DatagramPacket pck = new DatagramPacket(new byte[256],256);
				s.receive(pck);
				packets.add(pck);
			}
		} catch (IOException e) {
			e.printStackTrace(Console.dbg);
			close();
			return;
		}
	}
	
	public void close() {
		s.close();
		status.setStatus(false);
	}
	
	/* We are doing a poll instead of just using the packets in run to avoid sync issues
	 * Polling will always be done by the main thread.
	 */
	public void poll() {
		for (DatagramPacket pck : packets) {
			//TODO: Do something with the packet
		}
		packets.clear();
	}
	
	public void requestKey(InetAddress ip) throws IOException {
		s.send(new DatagramPacket(new byte[] {70}, 1, i, port));
		s.setSoTimeout(5000);
		Console.dbg.println("timeout: " + s.getSoTimeout());
		s.receive(p);
		key = p.getData();
		Console.dbg.println("Server response: " + getKey());
	}
	
	public String getKey() {
		String s = "";
		for(byte k : key) {
			s += ""+k;
		}
		return s;
	}
	
	public void sendEvent(PlayerState p) throws IOException {
		for(byte b : key)
			flexBuf.add(b);
		for(byte b : p.getBytes())
			flexBuf.add(b);
				
		byte[] buf = new byte[flexBuf.size()];
		for(int i = 0; i < flexBuf.size(); i++)
			buf[i] = flexBuf.get(i);
		this.p.setData(buf);
		s.send(this.p);
		flexBuf.clear();
	}
	
	@SuppressWarnings("rawtypes")
	public <E> Object callback(E obj, String method, Object[] args, boolean addToPacket) throws Exception {
		Class[] partypes = new Class[args.length];
		for(int i = 0; i < args.length; i++)
			partypes[i] = args[i].getClass();
		Method m = obj.getClass().getDeclaredMethod(method, partypes);
		Object o = m.invoke(obj, args);
		if(addToPacket)
			if(obj.getClass().getSuperclass() == Actor.class) {
				byte id = 0;
				flexBuf.add(obj.getClass().getDeclaredField("id").getByte(id));
			}
		
			for(byte b : m.getName().getBytes())
				flexBuf.add(b);
		
		return o;
	}

	public static String[] getServers() {
		ArrayList<String> s = new ArrayList<String>();
		
		Scanner sc;
		try {
			sc = new Scanner(new URL("http://shsprog.com/remote/serverlist.txt").openStream());
			while(sc.hasNextLine())
				s.add(sc.nextLine());
		
			String[] servs = new String[s.size()];
			for(int i = 0; i < s.size(); i++)
				servs[i] = s.get(i);
		
			return servs;
		} catch (FileNotFoundException e) {
			return null;
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}
}
