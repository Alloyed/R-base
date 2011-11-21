package physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class Actor {
	public Body b;
	public Stage s;
	public float sizeW, sizeH; //Can't find an easy way to get this out of a fixture
	public boolean important = false; 
	public String label="Box";
	public String image="box.png";
	
	public Actor(Stage s, Vec2 pos, Vec2 size) {
		this.s = s;
		this.sizeW = size.x;
		this.sizeH = size.y;
		
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
	
	//Actors are moving, but useless boxes by default.
	void force() {
		
	}

}
