package physics.actors;


public class Floor extends Actor {
	public Floor() {
		super();
		baseImage = "floor";
	}
	
	@Override
	public void makeBody() {
		this.fd = null;
	}
}
