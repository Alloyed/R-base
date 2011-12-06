package network;

import org.jbox2d.common.Vec2;

import physics.actors.Actor;

/* this is TODO */
public class ActorState implements State {
	final Class<? extends Actor> type;
	int id;
	Vec2 pos, vel;
	float angPos, angVel;
	
	public ActorState(Actor a) {
		type = a.getClass();
		id = a.id;
	}
	
	//update state
	public void set(Actor a) {
		pos    = a.b.getWorldCenter();
		angPos = a.b.getAngle();
		vel    = a.b.getLinearVelocity();
		angVel = a.b.getAngularVelocity();
	}
	
	public void set(byte[] b) {
		;
	}
	
	/* Byte order
	 * 	0-3		- ID
	 * 	4-7		- pos.x
	 *  8-11	- pos.y
	 *  12-15	- vel.x
	 *  16-19	- vel.y
	 *  20-23	- angPos
	 *  24-27	- angVel
	 */
	@Override
	public byte[] getBytes() {
		byte[] b = new byte[28];

		b[3] = (byte) id;
		b[2] = (byte) (id >> 8);
		b[1] = (byte) (id >> 16);
		b[0] = (byte) (id >> 24);
		
		int val = Float.floatToIntBits(pos.x);
		for(int i = 3; i > 0; i++) {
			b[4+i] = (byte) val;
			val >>= 8;
		}
		
		val = Float.floatToIntBits(pos.y);
		for(int i = 3; i > 0; i++) {
			b[8+i] = (byte) val;
			val >>= 8;
		}
		
		val = Float.floatToIntBits(vel.x);
		for(int i = 3; i > 0; i++) {
			b[12+i] = (byte) val;
			val >>= 8;
		}
		
		val = Float.floatToIntBits(vel.y);
		for(int i = 3; i > 0; i++) {
			b[16+i] = (byte) val;
			val >>= 8;
		}
		
		val = Float.floatToIntBits(angPos);
		for(int i = 3; i > 0; i++) {
			b[20+i] = (byte) val;
			val >>= 8;
		}
		
		val = Float.floatToIntBits(angVel);
		for(int i = 3; i > 0; i++) {
			b[23+i] = (byte) val;
			val >>= 8;
		}
		
		return null;
	}

	@Override
	public Object getParam(String param, byte[] b) {
		// TODO Auto-generated method stub
		return null;
	}

}
