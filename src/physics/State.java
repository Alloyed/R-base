package physics;

public interface State {
	public byte[] getBytes();
	public Object getParam(String param, byte[] b);
}
