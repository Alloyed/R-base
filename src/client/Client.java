package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import physics.PlayerState;

public class Client {
	public DatagramSocket s;
	public DatagramPacket p;
	public InetAddress i;
	public int port;
	private byte[] key;
	private ArrayList<Byte> flexBuf;
	
	Client(InetAddress ip, int port) throws IOException {
		i = ip;
		this.port = port;
		s = new DatagramSocket();
		key = new byte[16];
		p = new DatagramPacket(key, key.length);
		flexBuf = new ArrayList<Byte>();
		
		requestKey(ip);
	}
	
	public void requestKey(InetAddress ip) throws IOException {
		s.send(new DatagramPacket(new byte[] {70}, 1, i, port));
		s.setSoTimeout(5000);
		System.out.println(s.getSoTimeout());
		s.receive(p);
		key = p.getData();
		System.out.println("Server response: "+getKey());
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
	public Object callback(String object, String method, Object[] args, boolean addToPacket) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<?> c = Class.forName(object, true, null);
		Class[] partypes = new Class[args.length];
		for(int i = 0; i < args.length; i++)
			partypes[i] = args[i].getClass();
		Method m = c.getMethod(method, partypes);
		Object o = m.invoke(c, args);
		
		if(addToPacket)
			for(byte b : m.getName().getBytes())
				flexBuf.add(b);
		
		return o;
	}

	public static String[] getServers() {
		ArrayList<String> s = new ArrayList<String>();
		
		Scanner sc;
		try {
			sc = new Scanner(new URL("http://idontknow/remote/serverlist.txt").openStream());
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
