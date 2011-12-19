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
	//Unique ID, doesn't work right now
	public int id;
	//The team the Actor belongs to
	public Team team;
	//dimensions of the box. 
	//I can't find an easy way to get this out of a fixture
	public Vec2 size;
	//Important things, right now, get their labels drawn.
	public boolean isImportant = false;
	//What gets drawn next to the box to identify it, 
	//if the box's labeled important
	public String label="Box";
	//The filename of the sprite used to represent it
	public String baseImage="box";
	public String[] modifiers;
	//Has the actor been picked up by another?
	public boolean isHeld;
	//Health, damage, etc.
	public float wear;
	public float maxWear = 100;
	public float wearFrac;
	//This is for lerping, clients only.
	public static float alpha;
	public Vec2 oldPos;
	public float oldAng;
	
	BodyDef d;
	FixtureDef fd;
	public boolean toStore = false;
	
	/*Construction methods*/
	public Actor(int newid) {
		modifiers = new String[5];
		wear = maxWear;
		id = newid;
	}
	
	public Actor() {
		this(Stage.getNewId());
	}

	public void create(Vec2 size) {
		this.size = size;
		d = new BodyDef();
		fd = new FixtureDef();
		d.type = BodyType.DYNAMIC;
		fd.density = 1.6f;
		fd.friction = .3f;
		
		makeBody();
	}
	
	public void create(float size) {
		create(new Vec2(size,size));
	}
	
	//Places the Actor on the stage, in a given position, etc.
	//TODO: make actors invincible for the first few frames of their life
	public void place(Stage st, Vec2 pos, float ang, Vec2 vel, float velAng) {
		s = st;
		d.position = new Vec2(pos);
		oldPos = new Vec2(pos);
		oldAng = ang;
		d.angle = ang;
		d.linearVelocity.set(vel);
		d.angularVelocity = velAng;
		b = s.w.createBody(d);
		float friction = .5f;
		if (fd != null) {
			friction = fd.friction;
			b.createFixture(fd);
		}
		b.setUserData(this);
		b.setLinearDamping(friction*9.8f);
		b.setAngularDamping(friction*9.8f);
		s.actors.put(id, this);
		s.activeActors.add(this);
	}
	
	public void place (Stage st, Vec2 pos) {
		toStore = false;
		place(st, pos, 0, new Vec2(0, 0), 0);
	}
	
	//Takes the Actor off the stage
	public void store() {
		toStore = true;
	}
	
	//Override this to do what you want to the defs before they are used
	public void makeBody() {
		PolygonShape p = new PolygonShape();
		p.setAsBox(size.x/2, size.y/2);
		fd.shape = p;
	}
	
	/*"Action" methods*/
	
	//Actors are moving, but useless boxes by default.
	//Change this to give them their own free will
	public void force() {}
	
	public void beginContact(Contact arg0, Actor other) {}

	public void endContact(Contact arg0, Actor other) {}
	
	/*What happens if this collides with another box? */
	public void preSolve(Contact arg0, Manifold arg1, Actor other) {}
	
	/*Damage calculation*/
	public void postSolve(Contact c, ContactImpulse imp, Actor other) {
		float force = 0;
		for (float f : imp.normalImpulses)
			force += f;
		force /= 50;
		force *= force;
		force *= 50;
		hurt(force);
	}
	
	public void hurt(float force) {
		wear -= force;
		wearFrac = wear / maxWear;
		modifiers[4] = wearFrac < .25f ? "-dmg3" :
						wearFrac < .5f ? "-dmg2" :
						wearFrac < .75f ? "-dmg1" : "";
	}
	
	//What happens when the box wears out?
	public void destroy() {
		if (s == null)
			return;
		if (size.length() > .4)
		for (int i = -1; i < 2; i += 2) {
			for (int j = -1; j < 2; j += 2) {
				Actor a = new Actor();
				a.create(new Vec2(size.x / 2,size.y / 2));
				Vec2 pos = new Vec2(size.x * i / 4f,size.y * j / 4f);
				a.place(s, b.getWorldPoint(pos), b.getAngle(),
						b.getLinearVelocity(), b.getAngularVelocity());
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
