package physics;

import java.util.HashMap;
import java.util.LinkedList;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import physics.actors.Actor;

/**
 * The representation of the in-game world.
 * 
 */
public class Stage {
	class HEYLISTEN implements ContactListener {
		@Override
		public void beginContact(Contact c) {
			Actor A = (Actor)c.getFixtureA().getBody().getUserData();
			Actor B = (Actor)c.getFixtureB().getBody().getUserData();
			A.beginContact(c, B);
			B.beginContact(c, A);
		}
		@Override
		public void endContact(Contact c) {
			Actor A = (Actor)c.getFixtureA().getBody().getUserData();
			Actor B = (Actor)c.getFixtureB().getBody().getUserData();
			A.endContact(c, B);
			B.endContact(c, A);
		}
		@Override
		public void postSolve(Contact c, ContactImpulse imp) {
			Actor A = (Actor)c.getFixtureA().getBody().getUserData();
			Actor B = (Actor)c.getFixtureB().getBody().getUserData();
			A.postSolve(c, imp, B);
			B.postSolve(c, imp, A);
		}
		@Override
		public void preSolve(Contact c, Manifold m) {
			Actor A = (Actor)c.getFixtureA().getBody().getUserData();
			Actor B = (Actor)c.getFixtureB().getBody().getUserData();
			A.preSolve(c, m, B);
			B.preSolve(c, m, A);
		}
	}
	
	public static float fps = 60;
	public static float frame = 1/60f;
	public long frameNum;
	public World w;
	public HashMap<Integer, Actor> actors; //Every actor, retrievable by id.
	public LinkedList<Actor> activeActors; //Every actor in the world right now
	public LinkedList<Actor> toAdd;
	public int orangebots=0, bluebots=0;
	static int nextId = 0;
	
	public Stage() {
		w = new World(new Vec2(0, 0), true);
		actors = new HashMap<Integer, Actor>();
		activeActors = new LinkedList<Actor>();
		toAdd = new LinkedList<Actor>();
		nextId = 1;
		w.setContactListener(new HEYLISTEN());
	}
	
	/**
	 * Adds an actor to the stage.
	 * @param type The type of Actor
	 * @param id the id to store it as. if unsure, use getNewId()
	 * @param t the team the Actor is part of
	 * @param size the size of the actor. this is a scale, so (1, 1) is normal size
	 * @param pos the absolute position in the world for the actor
	 * @return the resulting actor.
	 */
	public Actor addActor(Class<? extends Actor> type, int id, Team t, Vec2 size, Vec2 pos) {
		try {
			Actor a = type.newInstance();
			a.setTeam(t);
			a.id = id;
			a.create(size, pos);
			a.place(this);
			return a;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Actor addActor(Class<? extends Actor> type, Vec2 size, Vec2 pos) {
		return addActor(type, getNewId(), Team.NUETRAL, size, pos);
	}
	
	/**
	 * Runs one step of the simulation
	 * @param dt the amount of time in the step.
	 */
	public void step(float dt) {
		for (Actor a : toAdd)
			a.place(this);
		toAdd.clear();
		
		for (Actor a : activeActors) {
			a.force();
		}
		
		w.step(dt, 8, 3);
		for (Body b = w.getBodyList(); b != null; b = b.getNext()) {
			Actor a = (Actor) b.getUserData();
			if (a.toStore) {
				a.toStore = false;
				store(a);
			} else if (a.wear <= 1) {
				a.destroy();
				delete(a);
			}
		}
		
		w.clearForces();
	}
	
	/**
	 * returns the next free ID number.
	 * @return
	 */
	public static int getNewId() {
		return nextId++;
	}
	
	/**
	 * Returns the object with that id.
	 * -1 is always a copy of Stage
	 * 0 should always be the player this client is playing as
	 * @param id
	 * @return
	 */
	public Object get(int id) {
		if (id == -1)
			return this;
		Actor a = actors.get(id);
		return a;
	}
	
	/**
	 * Has anybody won/tied yet?
	 * @return
	 */
	public boolean won() {
		return orangebots == 0 || bluebots == 0;
	}
	
	/**
	 * If somebody has won, then who?
	 * Team.NUETRAL in this case is either a tie or nobody
	 * @return
	 */
	public Team whoWon() {
		if (orangebots == 0)
			return Team.BLUE;
		else if (bluebots == 0)
			return Team.ORANGE;
		return Team.NUETRAL;
	}
	
	/**
	 * Deletes the actor from the stage.
	 * If there are any refs to it anywhere, that would probably be a memory leak.
	 * @param a
	 */
	public void delete(Actor a) {
		w.destroyBody(a.b);
		actors.remove(a.id);
		activeActors.remove(a);
	}
	
	/**
	 * 
	 * Stores the actor, on the assumption that it will be wanted later.
	 * 
	 * @param a
	 */
	public void store(Actor a) {
		activeActors.remove(a);
		w.destroyBody(a.b);
	}
	
	/**
	 * FIXME: I don't remember why this is here tbh.
	 * @param origin
	 * @param time
	 * @param message
	 */
	public void chat(String origin, Long time, String message) {
		Console.chat(origin, time, message);
	}
	
}