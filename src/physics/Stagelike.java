package physics;

import org.jbox2d.common.Vec2;


/**
 * A thing that can be used like a Stage.
 * For the netcode.
 * @author kyle
 *
 */
public interface Stagelike {
	public int addActor(String type, int id, Team t, Vec2 size, Vec2 pos);
	public int addActor(String type, Vec2 size, Vec2 pos);
	public void say();
}
