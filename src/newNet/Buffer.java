package newNet;

import java.util.ArrayList;

public class Buffer {
	ArrayList<Byte> bytes;
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
	
	public void add(Object obj) {
		
	}
	
	public Object next() {
		return null;
	}
	
	public boolean hasNext() {
		return !bytes.isEmpty();
	}
}
