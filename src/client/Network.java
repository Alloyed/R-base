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
public class Network extends newNet.Network {
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
	private boolean start = false;
	
	public Network(Stage stage, StatusListener l) {
		status = l;
		this.stage = new Stage(stage); //TODO
		packets = Collections.synchronizedList(new ArrayList<DatagramPacket>());
		key = new byte[16];
		p = new DatagramPacket(key, key.length);
		flexBuf = new ArrayList<Byte>();
	}
	
	public void connect(String ip, int p) {
		try {
			Console.out.println("Connecting to " + ip + ":" + p);
			i = InetAddress.getByName(ip);
			port = p;
			start = true;
		} catch (Exception e) {
			e.printStackTrace(Console.dbg);
			status.setStatus(false);
			return;
		}

	}
	
	public void run() {
		try {
			while (true) {
				if (start){
					start = false;
					try {
						Console.out.println("ON IT");
						s = new DatagramSocket();
						requestKey(i);
						status.setStatus(true);
					} catch (Exception e) {
						e.printStackTrace(Console.dbg);
						status.setStatus(false);
					} 
				} else if (s != null) {
					DatagramPacket pck = new DatagramPacket(new byte[256],256);
					s.receive(pck);
					packets.add(pck);
				} else {
					//Wait until not null
				}
			}
		} catch (IOException e) {
			e.printStackTrace(Console.dbg);
			close();
			return;
		}
	}
	
	public void close() {
		if(s != null) {
			s.close();
			s = null;
		}
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
