package physics;


import org.jbox2d.common.Vec2;

public class PlayerState implements State {
	public boolean upPressed, downPressed, leftPressed, rightPressed, ROTATE_FORCE;
	public Vec2 aim;
	public long lastTime = 0;
	public PlayerState() {
		aim = new Vec2(0,0);
	}
	/* Byte #		Contents
	 * 		0	-	upPressed
	 * 		1	-	downPressed
	 * 		2	-	leftPressed
	 * 		3	-	rightPressed
	 * 		4	-	ROTATE_FORCE
	 * 	  5-8	-	Cursor's X distance
	 * 	 9-12	-	Cursor's Y distance
	 * 	13-26	-	time since last packet
	 */
	public byte[] getBytes() {
		byte[] b = new byte[21];
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
		
		int nextTime = (int) (System.currentTimeMillis()-lastTime);
		for(int i = 3; i > 0; i--) {//13-16 - the time dif
			b[13+i] = (byte) nextTime;
			nextTime >>= 8;
		}
		lastTime = nextTime;
		
		return b;
	}
	
	public String parseBytes(byte[] packet) {
		String s = "";
		for(int i = 16; i < packet.length; i++)
			if(i == 6 || i == 7)
				s += (float) packet[i] + " ";
			else
				s += packet[i]+" ";
		return s;
	}
}
