package physics.actors;


/**
 * An invisible observer/cursor.
 * Could extend Robot, or Robot could extend this, but I'm a bad coder.
 * 
 * @author kyle
 *
 */
public class Ghost extends Actor {
	public PlayerState state;
	final int speed=60;
	
	public Ghost() {
		super();
		label = "ghost";
		baseImage = "none";
		state = new PlayerState(this);
	}
	
	@Override
	public void makeBody() {
		this.fd = null;
	}
	
	@Override
	public void force() {
		state.force(this, speed);
	}

}
