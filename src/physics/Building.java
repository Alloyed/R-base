package physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/* Use for permanent squares in the world
 * TODO: add a texture mode to Sprite so we can make rectangles
 */
public class Building extends Actor {
	
	public Building(Stage s, Vec2 pos, Vec2 size) {
		super(s,pos,size);
	}
	
	public Building(Stage s, Vec2 pos, float size) {
		super(s,pos,new Vec2(size,size));
	}

	public void makeBody(BodyDef d,FixtureDef fd) {
		d.type = BodyType.STATIC;
		fd.density = .1f;
		fd.friction = 0;
		fd.restitution = 3f; //Wee!
	}
}
