package physics;

import org.jbox2d.common.Vec2;

public class PlayerState implements State {
	public boolean upPressed, downPressed, leftPressed, rightPressed, ROTATE_FORCE;
	public Vec2 aim;
	public long lastTime = System.currentTimeMillis();
	public long current = lastTime;
	public static int byteSize = 28;

	public PlayerState() {
		aim = new Vec2(0, 0);
	}
	
	/* Byte #		Contents
	 * 		0	-	upPressed
	 * 		1	-	downPressed
	 * 		2	-	leftPressed
	 * 		3	-	rightPressed
	 * 		4	-	ROTATE_FORCE
	 * 	  5-8	-	Cursor's X distance
	 * 	 9-12	-	Cursor's Y distance
	 * 	13-20	-	current timestamp
	 *  21-28	-	last timestamp
	 */
	public byte[] getBytes() {
		byte[] b = new byte[29];
		b[0] = (byte) (upPressed?1:0);
		b[1] = (byte) (downPressed?1:0);
		b[2] = (byte) (leftPressed?1:0);
		b[3] = (byte) (rightPressed?1:0);
		b[4] = (byte) (ROTATE_FORCE?1:0);

		int val = Float.floatToIntBits(aim.x);
		for(int i = 3; i > 0; i--) {//5-8 - the cursor x
			b[5+i] = (byte) val;
			val >>= 8;
		}

		val = Float.floatToIntBits(aim.y);
		for(int i = 3; i > 0; i--) {//9-12 - the cursor y
			b[9+i] = (byte) val;
			val >>= 8;
		}

		long current = System.currentTimeMillis();
		for(int i = 7; i > 0; i--) {//13-20 - the current time
			b[13+i] = (byte) current;
			current >>= 8;
		}

		lastTime = current;
		for(int i = 7; i > 0; i--) {//21-28 - the last time
			b[13+i] = (byte) lastTime;
			lastTime >>= 8;
		}

		return b;
	}

	public Comparable getParam(String name, byte[] b) {
		if(name.equals("up")) {
			return b[0] != 0;
		} else if(name.equals("down")) {
			return b[1] != 0;
		} else if(name.equals("left")) {
			return b[2] != 0;
		} else if(name.equals("right")) {
			return b[3] != 0;
		} else if(name.equals("ROTATE_FORCE")) {
			return b[4] != 0;
		} else if(name.equals("aim.x")) {
			return Float.intBitsToFloat((b[8] << 24) | (b[7] << 16) | (b[6] << 8) | (b[5]));
		} else if(name.equals("aim.y")) {
			return Float.intBitsToFloat((b[12] << 24) | (b[11] << 16) | (b[10] << 8) | (b[9]));
		} else if(name.equals("current")) {
			return (b[20] << 56) | (b[19] << 48) | (b[18] << 40) | (b[17] << 32) | (b[16] << 24) | (b[15] << 16) | (b[14] << 8) | (b[13]);
		} else if(name.equals("last")) {
			return (b[28] << 56) | (b[27] << 48) | (b[26] << 40) | (b[25] << 32) | (b[24] << 24) | (b[23] << 16) | (b[22] << 8) | (b[21]);
		}

		//If parameter doesn't exist, return null
		return null;
	}

	public PlayerState(byte[] data) {
		upPressed = data[0] != 0;
		downPressed = data[1] != 0;
		leftPressed  = data[2] != 0;
		rightPressed = data[3] != 0;
		ROTATE_FORCE = data[4] != 0;
		aim.set(Float.intBitsToFloat(
					(data[8] << 24) |
					(data[7] << 16) |
					(data[6] << 8) |
					(data[5])),
			Float.intBitsToFloat(
					(data[12] << 24) |
					(data[11] << 16) |
					(data[10] << 8) |
					(data[9])));
		current = (data[20] << 56) |
				(data[19] << 48) |
				(data[18] << 40) |
				(data[17] << 32) |
				(data[16] << 24) |
				(data[15] << 16) |
				(data[14] << 8) |
				(data[13]);
		lastTime = (data[28] << 56) |
				(data[27] << 48) |
				(data[26] << 40) |
				(data[25] << 32) |
				(data[24] << 24) |
				(data[23] << 16) |
				(data[22] << 8) |
				(data[21]);
	}
}
