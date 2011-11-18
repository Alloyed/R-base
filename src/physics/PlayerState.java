package physics;


import org.jbox2d.common.Vec2;

public class PlayerState implements State {
	public boolean upPressed, downPressed, leftPressed, rightPressed, ROTATE_FORCE;
	public Vec2 aim;
	
	public byte[] toBytes(int value) {
		return new byte[] {
			(byte)((value >> 24) & 0xff),
			(byte)((value >> 16) & 0xff),
			(byte)((value >> 8) & 0xff),
			(byte)((value >> 0) & 0xff)
		};
	}
	
	/* Byte #		Contents
	 * 		0	-	upPressed
	 * 		1	-	downPressed
	 * 		2	-	leftPressed
	 * 		3	-	rightPressed
	 * 		4	-	ROTATE_FORCE
	 * 	  5-8	-	Cursor's X distance
	 * 	 9-12	-	Cursor's Y distance
	 * 	13-20	-	timeStamp Data
	 */
	public byte[] getBytes() {
		byte[] b = new byte[21];
		b[0] = (byte) (upPressed?1:0);
		b[1] = (byte) (downPressed?1:0);
		b[2] = (byte) (leftPressed?1:0);
		b[3] = (byte) (rightPressed?1:0);
		b[4] = (byte) (ROTATE_FORCE?1:0);
		byte[] bytes = toBytes(Float.floatToRawIntBits(aim.x));
		for(int i = 0; i < bytes.length; i++)
				b[5+i] = bytes[i];
		
		bytes = toBytes(Float.floatToRawIntBits(aim.y));
		for(int i = 0; i < bytes.length; i++)
				b[9+i] = bytes[i];
		
		bytes = new org.jscience.net.ntp.TimeStamp().getData();
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
