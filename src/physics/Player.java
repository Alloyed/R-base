package physics;

import org.jbox2d.common.Vec2;

public class Player extends Actor {
	public PlayerState state;
	final int speed=75;
	
	final float HALF_PI = (float) (Math.PI/2f);
	public Player(Stage s) {
		super(s);
		image = "player.png";
		state = new PlayerState();
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
