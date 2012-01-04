package physics.actors;

import org.jbox2d.common.Vec2;
/**
 * The state an object was in at a point in time.
 * 
 * @author kyle
 *
 */
public class Snapshot {
	
	long time;
	int actor;
	public Vec2 pos = new Vec2(0, 0); 
	public float ang;
	/*
	public Vec2 vel;
	public float velAng;
	*/
	
	public Snapshot(Actor a) {
		actor = a.id;
		if (a.s != null) { //Has it been placed yet?
			time = a.s.time;
			pos.set(a.b.getWorldCenter());
			ang = a.b.getAngle();
		}
	}
	
	/**
	 * Same as the constructor, but modifies the snapshot in-place
	 * @param a
	 */
	public void snap(Actor a) {
		actor = a.id;
		time = a.s.time;
		pos.set(a.b.getWorldCenter());
		ang = a.b.getAngle();
	}
	
	/**
	 * puts all the information back into the actor minus the time
	 * @param the actor this is a snapshot of
	 */
	public void develop(Actor a) {
		a.b.setTransform(pos, ang);
	}
}
