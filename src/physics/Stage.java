package physics;

import java.util.LinkedList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class Stage {
	public World w;
	public LinkedList<Actor> actors;
	
	public Stage() {
		w = new World(new Vec2(0, 0), true);
		actors = new LinkedList<Actor>();
	}
	
	public void step() {
		for (Actor a: actors) {
			a.force();
		}
		
		for (Body b = w.getBodyList(); b != null; b = b.getNext()) {
			b.setLinearDamping(b.getFixtureList().getFriction()
					* (b.getMass() * 9.8f)); // How... Normal.
		}
		w.step(1f / 30f, 8, 3);
		w.clearForces();
	}
}
