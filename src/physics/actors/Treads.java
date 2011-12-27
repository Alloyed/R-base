package physics.actors;


/*the Robot's treads.*/
public class Treads extends Actor {
	public Treads() {
		super();
		baseImage = "playerBottom";
		maxWear = 99999;
		wear    = maxWear;
	}
	
	@Override
	public void makeBody() {
		fd = null;
	}
}
