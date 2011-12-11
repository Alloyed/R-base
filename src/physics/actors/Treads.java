package physics.actors;

import physics.Stage;

/*the Robot's treads.*/
public class Treads extends Actor {
	public Treads(int id) {
		super(id);
		baseImage = "playerBottom";
		maxWear = 99999;
		wear    = maxWear;
	}
	
	public Treads() {
		this(Stage.getNewId());
	}
	
	@Override
	public void makeBody() {
		fd = null;
	}
}
