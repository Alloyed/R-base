package physics;

import java.util.LinkedList;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

/*The representation of the in-game world. It's more useless right now than it should be.*/
public class Stage {
	class HEYLISTEN implements ContactListener {

		@Override
		public void beginContact(Contact arg0) {
		}

		@Override
		public void endContact(Contact arg0) {
		}

		@Override
		public void postSolve(Contact c, ContactImpulse imp) {
			float force = 0;
			for (float f : imp.normalImpulses)
				force += f;
			Actor A = (Actor)c.getFixtureA().getBody().getUserData();
			Actor B = (Actor)c.getFixtureB().getBody().getUserData();
			A.wear -= force;
			B.wear -= force;
			if (A instanceof Player && B.sizeH < .4) {
				B.wear = B.maxWear;
				B.store();
				((Player)A).inventory.add(B);
			}
		}

		@Override
		public void preSolve(Contact arg0, Manifold arg1) {
		}
	}
	public World w;
	public LinkedList<Actor> actors;
	static int nextId = 0;
	public Stage() {
		w = new World(new Vec2(0, 0), true);
		actors = new LinkedList<Actor>();
		nextId = 0;
		w.setContactListener(new HEYLISTEN());
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
		for (Body b = w.getBodyList(); b != null; b = b.getNext()) {
			Actor a = (Actor) b.getUserData();
			if (a.wear <= 1) {
				a.destroy();
				w.destroyBody(b);
				actors.remove(a);
			}
		}
			
		w.clearForces();
	}

	public static int getNewId() {
		return nextId++;
	}
}
