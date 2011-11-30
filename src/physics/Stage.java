package physics;

import java.util.LinkedList;
import java.util.Random;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import physics.actors.Actor;
import physics.actors.Bullet;
import physics.actors.Prop;
import physics.actors.Robot;

/*The representation of the in-game world. 
 * It's more useless right now than it should be.
 */
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
			if (A instanceof Bullet || B instanceof Bullet)
				force *= 10;
			A.hurt(force);
			B.hurt(force);
			if (A instanceof Robot && B.sizeH < .6 && B.b.getLinearVelocity().length() < 4) {
				((Robot)A).take(B);
			}
		}

		@Override
		public void preSolve(Contact arg0, Manifold arg1) {
		}
	}
	
	public static float fps = 60;
	public static float frame = 1/60f;
	public World w;
	public LinkedList<Actor> actors;
	static int nextId = 0;
	public Stage() {
		w = new World(new Vec2(0, 0), true);
		actors = new LinkedList<Actor>();
		nextId = 0;
		w.setContactListener(new HEYLISTEN());
	}

	public void startGame(Long seed) {
		Random rand = new Random(seed); //We need the seed because DETERMINISM!
		//boundaries. These numbers were pulled straight from my ass.
		addActor(Prop.class, new Vec2(16.5f,4), new Vec2(6.25f, -4));
		addActor(Prop.class, new Vec2(4,16.5f), new Vec2(-4, 4.6875f));
		addActor(Prop.class, new Vec2(4,16.5f), new Vec2(16.5f, 4.6875f));
		addActor(Prop.class, new Vec2(16.5f,4), new Vec2(6.25f,13.375f));
		//Random boxes. Fun!
		for (int i=0;i<30;++i)
			addActor(Actor.class, new Vec2(1, 1), 
					new Vec2(rand.nextFloat()*12,
							rand.nextFloat()*12));
	}
	
	public Actor addActor(Class<?> type, int id, Vec2 size, Vec2 pos) {
		try {
			Actor a = (Actor) type.newInstance();
			a.create(size);
			a.place(this, pos);
			return a;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Actor addActor(Class<?> type, Vec2 size, Vec2 pos) {
		return addActor(type, getNewId(), size, pos);
	}
	
	/*Run one step of the simulation, right now one thirtieth of a second.*/
	public void step() {
		for (Actor a: actors) {
			a.force();
		}
		
		for (Body b = w.getBodyList(); b != null; b = b.getNext()) {
			Fixture f = b.getFixtureList();
			float friction;
			if (f == null) {
				friction = 9.8f*.5f;
			} else {
				friction = b.getFixtureList().getFriction() *
						(b.getMass() * 9.8f); //Am i even doing this right?
			}
			b.setLinearDamping(friction);
			b.setAngularDamping(friction);
		}
		w.step(frame, 8, 3);
		for (Body b = w.getBodyList(); b != null; b = b.getNext()) {
			Actor a = (Actor) b.getUserData();
			if (a.toStore) {
				actors.remove(a);
				w.destroyBody(b);
			} else if (a.wear <= 1) {
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

	public boolean won() {
		return false;
	}
}
