package physics.actors;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import physics.Stage;

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
	public float wear;
	float maxWear = 200;
	public float wearFrac;
	//This is for lerping, clients only.
	public static float alpha;
	public Vec2 oldPos;
	
	BodyDef d;
	FixtureDef fd;
	public boolean toStore = false;
	
	//Makes an actor: TODO: make addActor() methods in Stage
	public Actor() {

		modifiers = new String[5];
		wear = maxWear;
		d = new BodyDef();
		fd = new FixtureDef();
		d.type = BodyType.DYNAMIC;

		fd.density = 1.6f;
		fd.friction = .3f;
		
		makeBody(d, fd);
		id = Stage.getNewId();
	}
	
	public void create(Vec2 size) {
		if (fd == null)
			return;
		this.sizeW = size.x;
		this.sizeH = size.y;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(sizeW / 2f, sizeH / 2f);
		fd.shape = shape;
	}
	
	public void create(float size) {
		create(new Vec2(size,size));
	}
	
	//Places the Actor on the stage, in a given position
	public void place(Stage st, Vec2 pos, float ang, Vec2 vel, float velAng) {
		s = st;
		d.position.set(pos);
		oldPos = new Vec2(pos);
		d.angle = ang;
		d.linearVelocity.set(vel);
		d.angularVelocity = velAng;
		b = s.w.createBody(d);
		if (fd != null)
			b.createFixture(fd);
		b.setUserData(this);
		s.actors.add(this);
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
	public void makeBody(BodyDef d, FixtureDef fd) {
	
	}
	
	//Actors are moving, but useless boxes by default.
	//Change this to give them their own free will
	public void force() {
		
	}
	
	public void hurt(float force) {
		wear -= force;
		wearFrac = wear / maxWear;
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
			Actor a = new Actor();
			a.create(new Vec2(sizeW / 2,sizeH / 2));
			a.place(s, b.getWorldCenter(), b.getAngle(),
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
