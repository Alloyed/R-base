package physics;

public interface State {
	public byte[] getBytes();
	public void parseBytes(byte[] packet, int head);
}
