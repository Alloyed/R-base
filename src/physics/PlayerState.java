package physics;

import org.jbox2d.common.Vec2;

public class PlayerState implements State{
	public boolean upPressed, downPressed, leftPressed, rightPressed, ROTATE_FORCE;
	public Vec2 aim;
	
	public PlayerState() {
		aim = new Vec2(0,0);
	}
	
	public byte[] getBytes() {
		return null;
	}
	
	public void parseBytes(byte[] packet, int head) {
		;
	}
}
