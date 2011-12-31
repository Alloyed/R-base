package client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


import physics.Console;
import physics.Stage;
import physics.actors.Actor;
import physics.actors.PlayerState;
/*A connection to a server. Is horribly broken as of the start of newNet.*/
@SuppressWarnings("unused")
public class Connection extends newNet.Connection {
	public static final float frame = 1/20f;
	public DatagramSocket s;
	public DatagramPacket p;
	public InetAddress i;
	public int port;
	private byte[] key;
	private ArrayList<Byte> flexBuf;
	public List<DatagramPacket> packets; //Stack of recieved packets
	private boolean start = false;
	StatusListener l;
	
	public Connection(String ip, int port, StatusListener l) throws Exception {
		super(ip, port);
		Console.out.println("Connecting to " + ip + ":" + port);
		i = InetAddress.getByName(ip);
		this.port = port;
		start = true;
		packets = Collections.synchronizedList(new ArrayList<DatagramPacket>());
		key = new byte[16];
		p = new DatagramPacket(key, key.length);
		flexBuf = new ArrayList<Byte>();
	}
	
	public void run() {
		try {
			Console.dbg.println("Starting network thread...");
			s = new DatagramSocket();
			requestKey(i);
			l.setStatus(true);
		} catch (Exception e) {
			e.printStackTrace(Console.dbg);
			l.setStatus(false);
		} 
		while (true) {
				DatagramPacket pck = new DatagramPacket(new byte[256],256);
				try {
					s.receive(pck);
					packets.add(pck);
				} catch(Exception e) {
					Console.out.println("Disconnected.");
					l.setStatus(false);
					return;
				}
		}
	}
	
	public void close() {
		if(s != null) {
			s.close();
			s = null;
		}
		l.setStatus(false);
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
