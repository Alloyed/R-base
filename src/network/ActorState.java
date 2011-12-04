package network;

import org.jbox2d.common.Vec2;

import physics.actors.Actor;

/* this is TODO */
public class ActorState implements State {
	final Class<? extends Actor> type;
	final int id;
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

}
