package physics;

import java.util.Arrays;

import org.jbox2d.common.Vec2;

public class PlayerState implements State {
	public boolean upPressed, downPressed, leftPressed, rightPressed, ROTATE_FORCE;
	public Vec2 aim;
	
	public byte[] getBytes() {
		byte[] b = new byte[5];
		b[0] = (byte) (upPressed?1:0);
		b[1] = (byte) (downPressed?1:0);
		b[2] = (byte) (leftPressed?1:0);
		b[3] = (byte) (rightPressed?1:0);
		b[4] = (byte) (ROTATE_FORCE?1:0);
		return b;
	}
	
	public String parseBytes(byte[] packet) {
		return Arrays.toString(packet);
	}
}
