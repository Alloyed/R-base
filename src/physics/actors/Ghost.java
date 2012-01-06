package physics.actors;


/**
 * An invisible observer/cursor.
 * Could extend Robot, or Robot could extend this, but I'm a bad coder.
 * 
 * @author kyle
 *
 */
public class Ghost extends Actor {
	public PlayerState ps;
	final int speed=60;
	
	public Ghost() {
		super();
		label = "ghost";
		baseImage = "none";
		ps = new PlayerState(this);
		state = ps;
	}
	
	@Override
	public void makeBody() {
		this.fd = null;
	}
	
	@Override
	public void force() {
		ps.force(this, speed);
	}

}
