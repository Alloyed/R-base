package physics.actors;

import java.util.LinkedList;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.DistanceJointDef;

import physics.Console;
import physics.Stage;
import physics.Team;

/**
 * One of the robot players
 * 
 * @author kyle
 *
 */
public class Robot extends Actor {
	final int speed=200;
	public PlayerState state;
	public Actor treads;
	public Actor held;
	public LinkedList<Actor> inventory;

	public Robot() {
		super();
		treads = new Treads();
		maxWear = 100;
		wear    = maxWear;
		isImportant = true;
		label = "robot";
		baseImage = "playerTop";
		inventory = new LinkedList<Actor>();
		for (int i=0; i<5; ++i) {
			Bullet b = new Bullet();
			b.create(new Vec2(.3f, .3f), new Vec2(0, 0));
			inventory.add(b);
		}
		
		state = new PlayerState();
	}
	
	@Override
	public void create(Vec2 size, Vec2 pos, float ang, Vec2 vel, float velAng) {
		super.create(size, pos, ang, vel, velAng);
		treads.create(size, pos, ang, vel, velAng);
	}
	
	@Override
	public void place(Stage st) {
		treads.place(st);
		super.place(st);
		
		DistanceJointDef j = new DistanceJointDef();
		j.initialize(b, treads.b, b.getWorldCenter(), treads.b.getWorldCenter());
		st.w.createJoint(j);
		if (team == Team.BLUE)
			st.bluebots++;
		else if (team == Team.ORANGE)
			st.orangebots++;
	}
	
	/**
	 * return the point dist away in the direction we're facing.
	 * @param dist
	 * @return
	 */
	public Vec2 getLocalPointAhead(float dist) {
		float ang = (float)Math.atan2(state.aim.y, state.aim.x);
		return new Vec2(dist*(float)Math.cos(ang),dist*(float)Math.sin(ang));
	}
	
	/**
	 * return the point dist away from us in the direction we're facing.
	 * TODO: see if b.getWorldVector has the same effect.
	 * @param dist
	 * @return
	 */
	public Vec2 getPointAhead(float dist) {
		return b.getWorldCenter().add(getLocalPointAhead(dist));
	}

	/**
	 * Get the point where the robot would pick things up and hold them, 
	 * if it had hands.
	 */
	public Vec2 getPointAhead() {
		return getPointAhead(1);
	}
	
	/**
	 * Pickup the object in front of us.
	 */
	public void hold() {
		AABB area = new AABB();
		Vec2 center = getPointAhead();
		area.lowerBound.set(center.sub(new Vec2(.2f,.2f)));
		area.upperBound.set(center.add(new Vec2(.2f,.2f)));
		QueryCallback q = new QueryCallback() {
			public boolean reportFixture(Fixture f) {
				if (held != null)
					return false;
				Object data = f.getBody().getUserData();
				if (data instanceof Actor) {
					Actor a = (Actor)data;
					if (!a.isHeld &&
							a.id != id &&
							a.b.getType() == BodyType.DYNAMIC &&
							a.b.getMass() < 5) {
						a.isHeld = true;
						held = a;
						return false;
					}
				}
				return true;
			}
		};
		s.w.queryAABB(q, area);
	}
	
	/**
	 * Drop what we're holding.
	 */
	public void release() {
		held.isHeld = false;
		held = null;
	}
	
	/**
	 * Drops anything we're holding, 
	 * and if we weren't holding anything, picks something up
	 */
	public void toggleHold() {
		if (held == null)
			hold();
		else
			release();
	}
		
	/**
	 * Fire something. 
	 * It can either be something we're holding or an object in our inventory.
	 */
	public void fire() {
		Actor a;
		if (held != null) {
			a = held;
			release();
			a.b.setBullet(true);
			a.b.applyLinearImpulse(getLocalPointAhead(100), a.b.getWorldCenter());
		} else if (!inventory.isEmpty()) {
			a = inventory.pollFirst();
			a.create(a.size, getPointAhead(a.size.length()+.1f), 
					0, getLocalPointAhead(100), 0);
			a.d.bullet = true;
			a.place(s);
		} else {
			return;
		}
	}
	
	/**
	 * Calculates the movement of our robot
	 */
	@Override
	public void force() {
		state.force(this, speed);
		
		Vec2 vel = b.getLinearVelocity();
		
		treads.b.setTransform(treads.b.getWorldCenter(), (float)Math.atan2(vel.y, vel.x));
		if (held != null)
			held.b.setTransform(getPointAhead(held.size.length()), held.b.getAngle());
	}
	
	/**
	 * Is there anything nearby we can pick up?
	 */
	@Override
	public void beginContact(Contact c, Actor other) {
		super.beginContact(c, other);
		if (other.size.length() < 1 && other.b.getLinearVelocity().length() < 4)
			take(other);
	}
	
	/**
	 * Puts an actor into our inventory.
	 * @param a
	 */
	public void take(Actor a) {
		//FIXME: a instanceof Map is a pretty bad stopgap
		if (a != null && !(a instanceof Map) && s.activeActors.contains(a)) { //Is it a pickup-able thing?
			inventory.add(a);
			a.wear = a.maxWear;
			a.store();
			s.activeActors.remove(a);
		}
	}
	
	/**
	 * Makes the spaghetti fall out our pockets.
	 * TODO: handle respawning, etc.
	 */
	@Override
	public void destroy() {
		Console.chat.println("\\" + label + " has died.");
		if (team == Team.BLUE)
			s.bluebots--;
		else if (team == Team.ORANGE)
			s.orangebots--;
		if (held != null)
			release();
		s.delete(treads);
		
		Actor shell = new Actor();
		shell.create(size, b.getWorldCenter());
		shell.place(s);
		shell.b.setTransform(b.getWorldCenter(), b.getAngle());
		shell.b.applyLinearImpulse(b.getLinearVelocity(), 
				shell.b.getWorldCenter());
		shell.baseImage = "playerTop";
		shell.modifiers = modifiers;
		for(Actor a : inventory) {
			a.create(a.size, b.getWorldCenter(),  
							b.getAngle(), 
							b.getLinearVelocity(), 
							b.getAngularVelocity());
			a.place(s);
		}
	}
}
