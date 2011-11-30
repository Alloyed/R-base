package physics.actors;

import org.jbox2d.dynamics.*;


/* Use for permanent squares in the world
 * TODO: add a texture mode to Sprite so we can make rectangles
 */
public class Prop extends Actor {
	
	public Prop() {
		super();
	}

	public void makeBody(BodyDef d,FixtureDef fd) {
		d.type = BodyType.STATIC;
		fd.density = .1f;
		fd.friction = .5f;
		fd.restitution = .8f; //Wee!
		wear = 999999999;
	}
}
