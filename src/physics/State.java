package physics;

public interface State {
	public byte[] getBytes();
	public <E> E getParam(String name, byte[] b);
}
