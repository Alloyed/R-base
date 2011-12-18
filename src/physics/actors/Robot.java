package physics.actors;

import java.util.LinkedList;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import physics.Console;
import physics.Stage;
import physics.Team;

/*A player in the world. TODO:Teams, a lot more*/
public class Robot extends Actor {
	final int speed=60;
	public PlayerState state;
	public Actor treads;
	public Actor held;
	public LinkedList<Actor> inventory;
	
	final float HALF_PI = (float) (Math.PI/2f);
	public Robot(int id) {
		super(id);
		maxWear = 100;
		wear    = maxWear;
		isImportant = true;
		label = "Robot";
		baseImage = "playerTop";
		inventory = new LinkedList<Actor>();
		for (int i=0; i<5; ++i) {
			Bullet b = new Bullet();
			b.create(.3f);
			inventory.add(b);
		}
		
		state = new PlayerState();
	}
	
	public Robot() {
		this(Stage.getNewId());
	}
	
	@Override
	public void makeBody() {
		super.makeBody();
	}
	
	public void place(Stage st, Vec2 pos, float ang, Vec2 vel, float velAng) {
		treads = st.addActor(Treads.class, new Vec2(1,1), pos);
		super.place(st, pos, ang, vel, velAng);
		
		RevoluteJointDef j = new RevoluteJointDef();
		j.initialize(b, treads.b, b.getWorldCenter());
		st.w.createJoint(j);
		if (team == Team.BLUE)
			st.bluebots++;
		else if (team == Team.ORANGE)
			st.orangebots++;
	}
	
	public Vec2 getLocalPointAhead(float dist) {
		Vec2 dir = state.aim.sub(b.getWorldCenter());
		float ang = (float)Math.atan2(dir.y, dir.x);
		return new Vec2(dist*(float)Math.cos(ang),dist*(float)Math.sin(ang));
	}
	
	public Vec2 getPointAhead(float dist) {
		return b.getWorldCenter().add(getLocalPointAhead(dist));
	}

	/* Get the point where it would pick things up and hold them, 
	 * if it had hands
	 */
	public Vec2 getPointAhead() {
		return getPointAhead(1);
	}
	
	/*Pickup another object in the world and hold it*/
	public void hold() {
		AABB area = new AABB();
		Vec2 center = getPointAhead();
		area.lowerBound.set(center.sub(new Vec2(.2f,.2f)));
		area.upperBound.set(center.add(new Vec2(.2f,.2f)));
		QueryCallback q = new QueryCallback() {
			public boolean reportFixture(Fixture f) {
				if (held != null)
					return true;
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
		if (a != null && s.activeActors.contains(a)) { //Is it a pickup-able thing?
			inventory.add(a);
			a.wear = a.maxWear;
			a.store();
			s.activeActors.remove(a);
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
			a.place(s,getPointAhead(a.size.length()+.1f));
		} else {
			return;
		}
		a.b.setBullet(true);
		a.b.applyLinearImpulse(getLocalPointAhead(100).mul(a.b.getMass()), b.getWorldCenter());
	}
	
	public void force() {
		Vec2 dir = state.aim.sub(b.getWorldCenter());
		float ang = (float)Math.atan2(dir.y, dir.x);
		b.setTransform(b.getWorldCenter(), ang);

		Vec2 move = new Vec2(0, 0);
		if (state.upPressed)
			move.addLocal(0, -1);
		if (state.leftPressed)
			move.addLocal(-1, 0);
		if (state.rightPressed)
			move.addLocal(1, 0);
		if (state.downPressed)
			move.addLocal(0, 1);
		move.normalize();
		if (state.ROTATE_FORCE) {
			ang += HALF_PI;
			move.set(move.x * (float)Math.cos(ang)
					- move.y * (float)Math.sin(ang),
					move.x * (float)Math.sin(ang)
					+ move.y * (float)Math.cos(ang));
		}

		b.applyForce(move.mul(speed), b.getWorldCenter());
		Vec2 vel = b.getLinearVelocity();
		treads.b.setTransform(b.getWorldCenter(), (float)Math.atan2(vel.y, vel.x));
		treads.b.applyForce(move.mul(speed), b.getWorldCenter());
		if (held != null)
			held.b.setTransform(getPointAhead(held.size.length()), held.b.getAngle());
	}
	
	public void preSolve(Contact c, Manifold m, Actor other) {
		if (other.size.length() < 1 && other.b.getLinearVelocity().length() < 4)
			take(other);
	}
	
	public void destroy() {
		Console.out.println(label + " was killed!");
		if (team == Team.BLUE)
			s.bluebots--;
		else if (team == Team.ORANGE)
			s.orangebots--;
		if (held != null)
			release();
		s.delete(treads);
		
		Actor shell = new Actor();
		shell.create(size);
		shell.place(s,b.getWorldCenter());
		shell.b.setTransform(b.getWorldCenter(), b.getAngle());
		shell.b.applyLinearImpulse(b.getLinearVelocity(), shell.b.getWorldCenter());
		shell.baseImage = "playerTop";
		shell.modifiers = modifiers;
		for(Actor a : inventory) {
			a.place(s, b.getWorldCenter(),b.getAngle(), b.getLinearVelocity(), b.getAngularVelocity());
		}
	}

	public boolean isDead() {
		return wear < 1;
	}
}
