package physics.actors;

import org.jbox2d.dynamics.*;


/* Use for permanent squares in the world
 * TODO: add a texture mode to Sprite so we can make rectangles
 */
public class Prop extends Actor {
	
	public Prop() {
		super();
		maxWear = 999999999;
		wear    = 999999999;
	}

	public void makeBody(BodyDef d,FixtureDef fd) {
		d.type = BodyType.STATIC;
		fd.friction = .5f;
		fd.restitution = 3f; //Wee!
	}
}
