package client;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

import physics.PlayerState;

public class Client {
	public DatagramSocket s;
	public DatagramPacket p;
	public InetAddress i;
	public int port;
	private byte[] key;
	
	Client(InetAddress ip, int port) throws IOException {
		i = ip;
		this.port = port;
		s = new DatagramSocket(port);
		key = new byte[16];
		p = new DatagramPacket(key, key.length);
		
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
		ArrayList<Byte> flexBuf = new ArrayList<Byte>();
		for(byte b : key)
			flexBuf.add(b);
		for(byte b : p.getBytes())
			flexBuf.add(b);
				
		byte[] buf = new byte[flexBuf.size()];
		for(int i = 0; i < flexBuf.size(); i++)
			buf[i] = flexBuf.get(i);
		this.p.setData(buf, 0, buf.length);
		s.send(this.p);
	}

	public PlayerState[] getServerState(byte[] data) {
		ArrayList<PlayerState> states = new ArrayList<PlayerState>();
		
		for(int i = 0; i < data.length/PlayerState.byteSize; i += PlayerState.byteSize) {
			byte[] single = new byte[PlayerState.byteSize];
			for(int x = 0; x < single.length; x++)
				single[x] = data[i+x];
		}
		
		PlayerState[] statesArr = new PlayerState[states.size()];
		for(int i = 0; i < statesArr.length; i++)
			statesArr[i] = states.get(i);
		return statesArr;
	}
	
	public DatagramPacket getEvent() throws IOException {
		s.receive(p);
		
		return p;
	}
	
	public Object call(String className, String methodName, Object[] args) {
		try {
			Class<?> c = Class.forName(className);
			Class<?>[] pars = new Class[args.length];
			for(int i = 0; i < args.length; i++)
				pars[i] = args[i].getClass();
			Method m = c.getMethod(methodName, pars);
			Object ret = m.invoke(c, args);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
