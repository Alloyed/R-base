package physics;

import java.util.LinkedList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

/*The representation of the in-game world. It's more useless right now than it should be.*/
public class Stage {
	public World w;
	public LinkedList<Actor> actors;
	int nextId;
	public Stage() {
		w = new World(new Vec2(0, 0), true);
		actors = new LinkedList<Actor>();
		nextId = 0;
	}
	
	/*Run one step of the simulation, right now one thirtieth of a second.*/
	public void step() {
		for (Actor a: actors) {
			a.force();
		}
		
		for (Body b = w.getBodyList(); b != null; b = b.getNext()) {
			float friction = b.getFixtureList().getFriction() * (b.getMass() * 9.8f); //Am i even doing this right?
			b.setLinearDamping(friction);
			b.setAngularDamping(friction);
		}
		w.step(1f / 30f, 8, 3);
		w.clearForces();
	}

	public int getNewId() {
		return nextId++;
	}
}
