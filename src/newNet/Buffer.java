package newNet;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;

import physics.actors.Actor;

public class Buffer {
	ArrayList<Byte> bytes;
	String encode = "UTF-8";
	
	public Buffer(byte[] b) {
		bytes = new ArrayList<Byte>();
		for (byte bt: b) {
			bytes.add(bt);
		}
	}
	
	public byte[] get() {
		byte[] b = new byte[bytes.size()];
		for (int i = 0; i < bytes.size(); ++i) {
			b[i] = bytes.get(i);
		}
		return b;
	}
	
	public byte leadChar(String s) {
		try {
			byte[] b = s.getBytes(encode);
			return b[0];
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return (Byte) null;
	}
	
	public void add(int i) {
		bytes.add((byte) (i >> 24));
		bytes.add((byte) (i >> 16));
		bytes.add((byte) (i >> 8));
		bytes.add((byte) i);
	}
	
	public <E> void add(E e) {
		if(e instanceof Integer) {
			bytes.add(leadChar("i"));
			add((Integer) e);
		} else if(e instanceof Float) {
			bytes.add(leadChar("f"));
			add(Float.floatToIntBits((Integer) e));
		} else if(e instanceof String) {
			bytes.add(leadChar("s"));
			try {
				byte[] str = ((String) e).getBytes(encode);
				for(byte b : str)
					bytes.add(b);
			} catch (UnsupportedEncodingException e1) { }
		} else if(e instanceof Vec2) {
			bytes.add(leadChar("v"));
			add(Float.floatToIntBits(((Vec2) e).x));
			add(Float.floatToIntBits(((Vec2) e).y));
		} else if(e instanceof Actor) {
			bytes.add(leadChar("a"));
			Actor a = (Actor) e;
			add(a.b.getWorldCenter());
			add(a.b.getLinearVelocity());
			add(a.b.getAngle());
			add(a.b.getAngularVelocity());
		} else if(e instanceof RPC) {
			RPC r = (RPC) e;
			add((Integer) r.id);
			add((Integer) r.stamp);
			add(r.method);
		}
	}
	
	public Object next() {
		return null;
	}
	
	public boolean hasNext() {
		return !bytes.isEmpty();
	}
}
