package physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/*The main representation of an in-game object, right now*/
public class Actor {
	//The physical representation of the object.
	public Body b;
	//The stage it belongs to
	public Stage s;
	//Unique ID, doesn't work right now
	public int id; 
	//dimensions of the box. 
	//I can't find an easy way to get this out of a fixture
	public float sizeW, sizeH; 
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
	float wear;
	float maxWear = 130;
	public float wearFrac;
	
	BodyDef d;
	FixtureDef fd;
	
	//Makes an actor: TODO: make addActor() methods in Stage
	public Actor(Vec2 size) {
		this.sizeW = size.x;
		this.sizeH = size.y;
		modifiers = new String[5];
		modifiers[4] = ".png";
		wear = maxWear;
		d = new BodyDef();
		fd = new FixtureDef();
		d.type = BodyType.DYNAMIC;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(sizeW/2f, sizeH/2f);
		fd.shape = shape;
		fd.density = 1.6f;
		fd.friction = .3f;
		
		makeBody(d,fd);
		id = Stage.getNewId();
	}
	
	public Actor(float size) {
		this(new Vec2(size, size));
	}
	
	public Actor() {
		this(new Vec2(1,1));
	}
	
	//Places the Actor on the stage, in a given position
	public void place(Stage st, Vec2 pos, float ang, Vec2 vel, float velAng) {
		if (st == null) {
			System.out.println("No stage, you brok some ting.");
			return;
		}
		s = st;
		d.position.set(pos);
		d.angle = ang;
		d.linearVelocity.set(vel);
		d.angularVelocity = velAng;
		b = s.w.createBody(d);
		b.createFixture(fd);
		b.setUserData(this);
		s.actors.add(this);
	}
	
	public void place (Stage st, Vec2 pos) {
		place(st, pos, 0, new Vec2(0,0), 0);
	}
	
	//Takes the Actor off the stage
	public void store() {
		if (s != null) {
		s.actors.remove(this);
		s.w.destroyBody(b);
		s = null;
		}
	}
	
	//Override this to do what you want to the defs before they are used
	public void makeBody(BodyDef d, FixtureDef fd) {
	
	}
	
	//Actors are moving, but useless boxes by default.
	//Change this to give them their own free will
	void force() {
		
	}
	
	public void hurt(float force) {
		wear -= force;
		wearFrac = wear/maxWear;
		modifiers[0] = wearFrac < .25f ? "-dmg3" :
						wearFrac < .5f ? "-dmg2" :
						wearFrac < .75f ? "-dmg1" : "";
	}
	
	//What happens when the box wears out?
	public void destroy() {
		if (s == null)
			return;
		if (sizeH > .4) 
		for (int i = 0; i < 4; ++i) {
			new Actor(new Vec2(sizeW/2,sizeH/2))
				.place(s, b.getWorldCenter(), b.getAngle(),
							b.getLinearVelocity(), b.getAngularVelocity());
		}
	}
	
	public String getImage() {
		String s = baseImage;
		for (String m : modifiers)
			if (m != null)
				s += m;
		return s;
	}
}
