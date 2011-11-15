package physics;

import org.jbox2d.common.Vec2;

public class PlayerState implements State{
	public boolean upPressed, downPressed, leftPressed, rightPressed, ROTATE_FORCE;
	public Vec2 aim;
	public byte[] getBytes() {
		return null;
	}
	public void parseBytes(byte[] packet, int head) {
		//TODO: this stuff.
	}
}
