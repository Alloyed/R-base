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
	//dimensions of the box. I can't find an easy way to get this out of a fixture
	public float sizeW, sizeH; 
	//Important things, right now, get their labels drawn.
	public boolean isImportant = false;
	//What gets drawn next to the box to identify it, if the box's labeled important
	public String label="Box"; 
	//The filename of the sprite used to represent it
	public String image="box.png";
	//Has the actor been picked up by another?
	public boolean isHeld;
	//Health, damage, etc.
	public float wear;
	//The starting health.
	public final float maxWear = 130;
	
	BodyDef d;
	FixtureDef fd;
	
	//Makes an actor: TODO: make addActor() methods in Stage
	public Actor(Vec2 size) {
		this.sizeW = size.x;
		this.sizeH = size.y;
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
	public void place(Stage st, Vec2 pos) {
		s = st;
		d.position.set(pos);
		b = s.w.createBody(d);
		b.createFixture(fd);
		b.setUserData(this);
		s.actors.add(this);
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
	
	//Actors are moving, but useless boxes by default. Change this to give them their own free will
	void force() {
		
	}
	
	//What happens when the box wears out?
	public void destroy() {
		if (sizeH > .4) 
		for (int i = 0; i < 4; ++i) {
			new Actor(new Vec2(sizeW/4,sizeH/4))
				.place(s, b.getWorldCenter());
		}
	}
}
