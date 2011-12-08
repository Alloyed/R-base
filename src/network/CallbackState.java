package network;

public class CallbackState implements State {
	int id;
	long time;
	String method;
	Object[] args; //Can be primitives, or Vec2s, or classtypes, or Actors
	public CallbackState(int i, long frameNum, String m, Object[] a) {
		id = i;
		time = frameNum;
		method = m;
		args = a;
	}
	
	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getParam(String param, byte[] b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set(byte[] b) {
		// TODO Auto-generated method stub

	}

}
