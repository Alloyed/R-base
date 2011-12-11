package physics.actors;

import org.jbox2d.common.Vec2;

/*Convenience class, holds info pertinent to a moving cursor*/
public class PlayerState {
	public boolean upPressed, downPressed, leftPressed, rightPressed, ROTATE_FORCE;
	public Vec2 aim = new Vec2(0, 0);
}
