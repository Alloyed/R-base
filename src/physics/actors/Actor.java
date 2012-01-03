package physics.actors;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;

import physics.Stage;
import physics.Team;

/*The main representation of an in-game object, right now*/
public class Actor {
	//The physical representation of the object.
	public Body b;
	//The stage it belongs to
	public Stage s;
	//Unique ID
	public int id;
	//The player the Actor belongs to
	//and that players team
	public Team team;
	public int owner;
	//dimensions of the box. 
	//I can't find an easy way to get this out of a fixture
	public Vec2 size;
	//Important things, right now, get their labels drawn.
	public boolean isImportant = false;
	//What gets drawn next to the box to identify it, 
	//if the box's labeled important
	public String label="box";
	//The name of the sprite used to represent it
	public String baseImage="box";
	public String[] modifiers;
	//Has the actor been picked up by another?
	public boolean isHeld;
	//Health, damage, etc.
	public float dmg = 1; //multiplier
	public float wear;
	public float maxWear = 100;
	public float wearFrac;
	//This is for lerping, clients only.
	public static float alpha;
	public Vec2 oldPos;
	public float oldAng;
	public boolean hit = false;
	
	BodyDef d;
	FixtureDef fd;
	public boolean toStore = false;
	
	/*Construction methods*/
	public Actor() {
		modifiers = new String[5];
		wear = maxWear;
	}
	
	/**
	 * Creates an object definition to be placed later.
	 * @param size
	 * @param pos
	 * @param ang
	 * @param vel
	 * @param velAng
	 */
	public void create(Vec2 size, Vec2 pos, float ang, Vec2 vel, float velAng) {
		this.size = size;
		d = new BodyDef();
		fd = new FixtureDef();
		d.type = BodyType.DYNAMIC;
		d.position = pos;
		oldPos = new Vec2(pos);
		d.angle = ang;
		oldAng = ang;
		d.linearVelocity = vel;
		d.angularVelocity = velAng;
		fd.density = 1.6f;
		fd.friction = .3f;
		
		makeBody();
	}
	
	public void create(Vec2 size, Vec2 pos) {
		create(size, pos, 0, new Vec2(0, 0), 0);
	}
	
	/**
	 * Places the Actor on the stage.
	 * Use create to change where it will placed etc.
	 * TODO: make actors invincible for the first few frames of their life
	 * @param st
	 */
	public void place(Stage st) {
		s = st;
		b = s.w.createBody(d);
		float friction = .5f;
		if (fd != null) {
			friction = fd.friction;
			b.createFixture(fd);
		}
		b.setUserData(this);
		b.setLinearDamping(friction*9.8f);
		b.setAngularDamping(friction*9.8f);
		if (!s.actors.containsKey(id))
			s.actors.put(id, this);
		s.activeActors.add(this);
	}
	
	/**
	 * Flags the the Actor for removal off the stage.
	 * Use this if you might want to use the actor later
	 */
	public void store() {
		toStore = true;
	}
	
	/**
	 * Creates a basic definition of the Actor.
	 * Override this to change shape, friction, etc.
	 */
	public void makeBody() {
		PolygonShape p = new PolygonShape();
		p.setAsBox(size.x/2, size.y/2);
		fd.shape = p;
	}
	
	/*"Action" methods*/
	
	/**
	 * Actors are moving, but useless boxes by default.
	 * Change this to give them their own free will
	 */
	public void force() {}
	
	/**
	 * What happens when two actors collide?
	 * By default, they make a noise.
	 * @param contact
	 * @param other
	 */
	public void beginContact(Contact contact, Actor other) {
		if (!(contact.getFixtureA().m_isSensor || contact.getFixtureB().m_isSensor)) {
			hit = true;
		}
	}
	
	/**
	 * What happens when two actors pull away from each other?
	 * Nothing.
	 * @param c
	 * @param other
	 */
	public void endContact(Contact c, Actor other) {}
	
	/**
	 * Called before the actors get normals applied to them.
	 * Use for whatever.
	 * @param c
	 * @param m
	 * @param other
	 */
	public void preSolve(Contact c, Manifold m, Actor other) {}
	
	/**
	 * Lets us access the normals from the collision.
	 * We do damage calculation here.
	 * @param c
	 * @param imp
	 * @param other
	 */
	public void postSolve(Contact c, ContactImpulse imp, Actor other) {
		float force = 0;
		for (float f : imp.normalImpulses)
			force += f;
		force /= 50;
		force *= force;
		force *= 50;
		hurt(force * other.dmg);
	}
	
	/**
	 * Hurt the box
	 * @param force
	 */
	public void hurt(float force) {
		wear -= force;
		wearFrac = wear / maxWear;
		modifiers[4] = wearFrac < .25f ? "-dmg3" :
						wearFrac < .5f ? "-dmg2" :
						wearFrac < .75f ? "-dmg1" : "";
	}
	
	/**
	 * What happens when the box wears out?
	 */
	public void destroy() {
		if (s == null)
			return;
		if (size.length() > .4)
		for (int i = -1; i < 2; i += 2) {
			for (int j = -1; j < 2; j += 2) {
				Actor a = new Actor();
				Vec2 pos = new Vec2(size.x * i / 4f,size.y * j / 4f);
				a.create(new Vec2(size.x / 2,size.y / 2), b.getWorldPoint(pos), b.getAngle(),
						b.getLinearVelocity(), b.getAngularVelocity());
				a.place(s);
			}
		}
	}
	
	/*Getters and Setters*/
	public String getImage() {
		String s = baseImage;
		for (String m : modifiers)
			if (m != null)
				s += m;
		return s;
	}
	
	public void setTeam(Team t) {
		team = t;
		modifiers[3] = Team.get(t);
	}	
}
