package physics;

public interface State {
	public byte[] getBytes();
	public String parseBytes(byte[] packet);
}
