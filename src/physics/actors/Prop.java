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
		label = "prop";
		baseImage = "prop";
	}
	
	@Override
	public void makeBody() {
		super.makeBody();
		d.type = BodyType.STATIC;
		fd.friction = .5f;
		fd.restitution = 1.1f; //Wee!
	}
}
