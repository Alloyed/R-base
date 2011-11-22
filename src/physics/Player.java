package physics;

import java.util.LinkedList;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

/*A player in the world. TODO:Teams, a lot more*/
public class Player extends Actor {
	public PlayerState state;
	final int speed=75;
	public Actor held;
	public LinkedList<Actor> inventory;
	public final float maxWear = 500;
	final float HALF_PI = (float) (Math.PI/2f);
	public Player() {
		super();
		wear = 500;
		isImportant = true;
		label = "Robot";
		image = "player-blue.png";
		state = new PlayerState();
		inventory = new LinkedList<Actor>();
		for (int i=0; i<5; ++i) {
			inventory.add(new Bullet(.3f));
		}
	}
	
	public Vec2 getLocalPointAhead(float dist) {
		Vec2 dir = state.aim.sub(b.getWorldCenter());
		float ang = (float)Math.atan2(dir.y, dir.x);
		return new Vec2(dist*(float)Math.cos(ang),dist*(float)Math.sin(ang));
	}
	
	public Vec2 getPointAhead(float dist) {
		return b.getWorldCenter().add(getLocalPointAhead(dist));
	}

	/*Get the point where it would pick things up and hold them, if it had hands*/
	public Vec2 getPointAhead() {
		return getPointAhead(1);
	}
	
	/*Pickup another object in the world and hold it*/
	public void hold() {
		AABB area = new AABB();
		Vec2 center = getPointAhead();
		area.lowerBound.set(center.sub(new Vec2(.1f,.1f)));
		area.upperBound.set(center.sub(new Vec2(.1f,.1f)));
		QueryCallback q = new QueryCallback() {
			public boolean reportFixture(Fixture f) {
				if (held != null)
					return false;
				Object data = f.getBody().getUserData();
				if (data instanceof Actor) {
					Actor a = (Actor)data;
					if (!a.isHeld && a.id != id) {
						a.isHeld = true;
						held = a;
						return true;
					}
				}
				return false;
			}
		};
		
		s.w.queryAABB(q, area);
	}
	
	/*Drop the object being held*/
	public void release() {
		held.isHeld = false;
		held = null;
	}
	
	public void toggleHold() {
		if (held == null)
			hold();
		else
			release();
	}
	
	/*Take some scrap*/
	public void take(Actor a) {
		if (a != null && s.actors.contains(a)) { //Is it a pickup-able thing?
			inventory.add(a);
			a.wear = a.maxWear;
			a.store();
			s.actors.remove(a);
		}
	}
	
	/*Fire either the object being held or some scrap*/
	public void fire() {
		Actor a;
		if (held != null) {
			a = held;
			release();
		} else if (!inventory.isEmpty()) {
			a = inventory.pollFirst();
			a.place(s,getPointAhead()); //TODO: scale to size of object
		} else {
			return;
		}
		a.b.setBullet(true);
		a.b.applyLinearImpulse(getLocalPointAhead(100), b.getWorldCenter());
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
			held.b.setTransform(getPointAhead(), held.b.getAngle());
	}
	
	public void destroy() {
		Actor a = new Actor(sizeH);
		a.place(s,b.getWorldCenter());
		a.b.setTransform(b.getWorldCenter(), b.getAngle());
		a.b.applyLinearImpulse(b.getLinearVelocity(), a.b.getWorldCenter());
		a.image = "player-blue.png"; 
	}
}
