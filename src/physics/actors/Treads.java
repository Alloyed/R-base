package physics.actors;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

import physics.Stage;

/*the Robot's treads.*/
public class Treads extends Actor {
	public Treads(int id) {
		super(id);
		baseImage = "playerBottom";
		maxWear = 99999;
		wear    = 99999;
	}
	
	public Treads() {
		this(Stage.getNewId());
	}
	
	@Override
	public void makeBody(BodyDef d, FixtureDef fd) {
		fd.filter.groupIndex = -10;
	}
}
