package physics.actors;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 * Holds input, and how to respond to it.
 * 
 * @author kyle
 *
 */
public class PlayerState extends Snapshot {
	public boolean upPressed, downPressed, leftPressed, rightPressed, ROTATE_FORCE;
	public Vec2 aim = new Vec2(0, 0), move = new Vec2(0, 0);
	
	public PlayerState(Actor a) {
		super(a);
	}
	
	/**
	 * Tells the Actor which way to go.
	 * @param a the Actor
	 * @param speed the max force (ie. not really a speed)
	 */
	public void force(Actor a, float speed) {
		Body b = a.b;
		PlayerState state = this;
		Vec2 dir = aim;
		float ang = MathUtils.fastAtan2(dir.y, dir.x);
		b.setTransform(b.getWorldCenter(), ang);
		
		Vec2 move = new Vec2(0, 0);
		//Keyboard movement
		if (state.move.x == 0 && state.move.y == 0) {
			if (state.upPressed)
				move.addLocal(0, -1);
			if (state.leftPressed)
				move.addLocal(-1, 0);
			if (state.rightPressed)
				move.addLocal(1, 0);
			if (state.downPressed)
				move.addLocal(0, 1);
			move.normalize();
			if (state.ROTATE_FORCE) {
				ang += MathUtils.HALF_PI;
				move.set(move.x * MathUtils.cos(ang)
					   - move.y * MathUtils.sin(ang),
						 move.x * MathUtils.sin(ang)
					   + move.y * MathUtils.cos(ang));
			}
		} else { //Joystick movement
			move.set(state.move);
			if (state.ROTATE_FORCE) {
				ang += MathUtils.HALF_PI;
				move.set(move.x * MathUtils.cos(ang)
					   - move.y * MathUtils.sin(ang),
					     move.x * MathUtils.sin(ang)
					   + move.y * MathUtils.cos(ang));
			}
		}
		b.applyForce(move.mul(speed), b.getWorldCenter());
		
		snap(a);
	}
}
