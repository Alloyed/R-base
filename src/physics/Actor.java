package physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class Actor {
	public Body b;
	public Stage s;
	public float size;
	public String image="box.png";
	public Actor(Stage s, Vec2 pos, float size) {
		this.s = s;
		this.size = size;
		BodyDef d = new BodyDef();
		d.position.set(pos);
		d.type = BodyType.DYNAMIC;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(size/2f, size/2f);
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.density = 1.6f;
		fd.friction = .3f;

		b = s.w.createBody(d);
		b.createFixture(fd);
		s.actors.add(this);
	}
	public Actor(Stage s, Vec2 pos) {
		this(s,pos,1);
	}
	public Actor(Stage s) {
		this(s,new Vec2(1,1),1);
	}
	
	//Actors are moving, but useless boxes by default.
	void force() {
		
	}

}
