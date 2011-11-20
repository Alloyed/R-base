package physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/*The main representation of an in-game object, right now*/
public class Actor {
	public Body b; //The physical representation of the object.
	public Stage s;
	public int id; //Unique ID
	public float sizeW, sizeH; //width and height of the box. I Can't find an easy way to get this out of a fixture
	public boolean isImportant = false; //Important things, right now, get their labels drawn.
	public String label="Box"; //What gets drawn next to the box to identify it, if it's important
	public String image="box.png"; //The filename of the sprite used to represent it
	public boolean isHeld; //Has the actor been picked up by another?
	
	//Makes an actor: TODO: make addActor() methods in Stage
	public Actor(Stage s, Vec2 pos, Vec2 size) {
		this.s = s;
		this.sizeW = size.x;
		this.sizeH = size.y;
		id = s.getNewId();
		BodyDef d = new BodyDef();
		FixtureDef fd = new FixtureDef();
		d.position.set(pos);
		d.type = BodyType.DYNAMIC;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(sizeW/2f, sizeH/2f);
		fd.shape = shape;
		fd.density = 1.6f;
		fd.friction = .3f;
		
		makeBody(d,fd);
		b = s.w.createBody(d);
		b.createFixture(fd);
		b.setUserData(this);
		s.actors.add(this);
	}
	
	public Actor(Stage s, Vec2 pos, float size) {
		this(s,pos,new Vec2(size, size));
	}
	
	public Actor(Stage s, Vec2 pos) {
		this(s,pos,new Vec2(1,1));
	}
	
	public Actor(Stage s) {
		this(s,new Vec2(1,1),new Vec2(1,1));
	}
	
	//Override this to do what you want to the defs before they are used
	public void makeBody(BodyDef d, FixtureDef fd) {
	
	}
	
	//Actors are moving, but useless boxes by default. Change this to give them their own free will
	void force() {
		
	}

}
