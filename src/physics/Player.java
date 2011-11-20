package physics;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.PrismaticJointDef;

public class Player extends Actor {
	public PlayerState state;
	final int speed=75;
	public Actor held;
	
	final float HALF_PI = (float) (Math.PI/2f);
	public Player(Stage s) {
		super(s);
		important = true;
		label = "Robot";
		image = "player-blue.png";
		state = new PlayerState();
	}
	
	public Vec2 getPointAhead() {
		Vec2 dir = state.aim.sub(b.getWorldCenter());
		float ang = (float)Math.atan2(dir.y, dir.x);
		return b.getWorldCenter().add(new Vec2((float)Math.cos(ang),(float)Math.sin(ang)));
	}
	
	public Vec2 getLocalPointAhead() {
		Vec2 dir = state.aim.sub(b.getWorldCenter());
		float ang = (float)Math.atan2(dir.y, dir.x);
		return new Vec2((float)Math.cos(ang),(float)Math.sin(ang));
	}
	
	//TODO: Join player and given actor
	public void pickup() {
		if (held != null)
			return;
		System.out.println("JOINT");
		AABB area = new AABB();
		Vec2 center = getPointAhead();
		area.lowerBound.set(center.sub(new Vec2(.1f,.1f)));
		area.upperBound.set(center.sub(new Vec2(.1f,.1f)));
		QueryCallback q = new QueryCallback() {
			public boolean reportFixture(Fixture f) {
				Object data = f.getBody().getUserData();
				if (data instanceof Actor && data != this) {
					held = (Actor)data;
					return true;
				}
				return false;
			}
		};
		
		s.w.queryAABB(q, area);
	}
	
	public void drop() {
		held = null;
	}
	public void toggleHold() {
		if (held == null)
			pickup();
		else
			drop();
	}
	public void fire() {
		Actor a;
		if (held == null) {
			a = new Actor(s, getPointAhead(),.1f);
		} else {
			a = held;
			drop();
		}
		a.b.setBullet(true);
		a.b.applyLinearImpulse(getLocalPointAhead().mul(100), b.getWorldCenter());
	}
	
	void force() {
		Vec2 dir = state.aim.sub(b.getWorldCenter());
		float ang = (float)Math.atan2(dir.y, dir.x);
		b.setTransform(b.getWorldCenter(), ang);

		Vec2 move = new Vec2(0, 0);
		if (state.upPressed)
			move.addLocal(0, -speed);
		if (state.leftPressed)
			move.addLocal(-speed, 0);
		if (state.rightPressed)
			move.addLocal(speed, 0);
		if (state.downPressed)
			move.addLocal(0, speed);
		if (state.ROTATE_FORCE) {
			ang += HALF_PI;
			move.set(move.x * (float)Math.cos(ang) - move.y * (float)Math.sin(ang),
					move.x * (float)Math.sin(ang) + move.y * (float)Math.cos(ang));
		}

		b.applyForce(move, b.getWorldCenter());
		if (held != null)
			held.b.setTransform(b.getWorldPoint(getLocalPointAhead().mul(1.2f)), held.b.getAngle());
	}
}
