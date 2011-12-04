package network;

public interface State {
	public byte[] getBytes();
	public Object getParam(String param, byte[] b);
	public void set(byte[] b);
}
