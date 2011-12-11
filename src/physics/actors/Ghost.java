package physics.actors;


import org.jbox2d.common.Vec2;


/*An observer/cursor. Could extend Robot, or Robot could extend this, but that would be more annoying*/
public class Ghost extends Actor {
	public PlayerState state;
	final int speed=60;
	public Ghost() {
		super();
		baseImage = "none";
		state = new PlayerState();
	}
	
	@Override
	public void makeBody() {
		this.fd = null;
	}
	
	public void force() {
		Vec2 move = new Vec2(0, 0);
		if (state.upPressed)
			move.addLocal(0, -1);
		if (state.leftPressed)
			move.addLocal(-1, 0);
		if (state.rightPressed)
			move.addLocal(1, 0);
		if (state.downPressed)
			move.addLocal(0, 1);
		move.normalize();

		b.applyForce(move.mul(speed), b.getWorldCenter());
	}

}
