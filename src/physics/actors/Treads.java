package physics.actors;


/*the Robot's treads.*/
public class Treads extends Actor {
	public Treads() {
		super();
		label = "treads";
		baseImage = "playerBottom";
	}
	/*
	@Override
	public void makeBody() {
		fd = null;
	}
	*/
	@Override
	public void hurt(float dmg) {}
}
