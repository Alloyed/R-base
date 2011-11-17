package physics;

import org.jbox2d.common.Vec2;

public class Player extends Actor {
	public PlayerState state;
	final int speed=75;
	Actor other;
	
	final float HALF_PI = (float) (Math.PI/2f);
	public Player(Stage s) {
		super(s);
		important = true;
		label = "Robot";
		image = "player-blue.png";
		state = new PlayerState();
	}
	
	//TODO: Join player and given actor
	public void pickup(Actor a) {
		other = a;
	}
	
	public void drop() {
		other = null;
	}
	
	public void fire() {
		if (other == null) {
			//TODO:Fire bullets
		} else {
			Vec2 v = other.b.getWorldCenter().sub(b.getWorldCenter());
			v.normalize();
			other.b.applyForce(v.mul(50), b.getWorldCenter());
		}
	}
	
	void force() {
		Vec2 dir = state.aim.sub(b.getWorldCenter());
		float ang = (float)Math.atan2(dir.y, dir.x);
		b.setTransform(b.getWorldCenter(), ang);

		Vec2 move = new Vec2(0, 0);
		if (state.upPressed)
			move.addLocal(0, -speed);
		if (state.leftPressed)
			move.addLocal(-speed, 0);
		if (state.rightPressed)
			move.addLocal(speed, 0);
		if (state.downPressed)
			move.addLocal(0, speed);
		if (state.ROTATE_FORCE) {
			ang += HALF_PI;
			move.set(move.x * (float)Math.cos(ang) - move.y * (float)Math.sin(ang),
					move.x * (float)Math.sin(ang) + move.y * (float)Math.cos(ang));
		}

		b.applyForce(move, b.getWorldCenter());
	}
}
