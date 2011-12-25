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
import physics.actors.Map;

/*The representation of the in-game world. 
 * It's more useless right now than it should be.
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
	public int orangebots=0, bluebots=0;
	static int nextId = 0;
	
	public Stage() {
		w = new World(new Vec2(0, 0), true);
		actors = new HashMap<Integer, Actor>();
		activeActors = new LinkedList<Actor>();
		nextId = 1;
		w.setContactListener(new HEYLISTEN());
	}
	
	//Makes a deep copy of a stage. 
	//Doesn't really work on the level of Actors
	public Stage(Stage stage) {
		w = new World(new Vec2(0, 0), true);
		actors = new HashMap<Integer, Actor>();
		activeActors = new LinkedList<Actor>();
		nextId = 1;
		w.setContactListener(new HEYLISTEN());
		for (Actor a: stage.actors.values()) {
			Actor newa = addActor(a.getClass(), a.id, a.team,
						a.size,
						(a.b == null ? new Vec2(0, 0) : a.b.getWorldCenter()));
			if (!stage.activeActors.contains(a))
				store(newa);
		}
	}
	
	public Actor addActor(Class<?> type, int id, Team t, Vec2 size, Vec2 pos) {
		try {
			Actor a = (Actor) type.newInstance();
			a.setTeam(t);
			a.id = id;
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
		return addActor(type, getNewId(), Team.NUETRAL, size, pos);
	}
	
	/*Run one step of the simulation, simulating frame seconds of time.*/
	public void step(float dt) {
		for (Actor a: activeActors) {
			a.force();
		}
		
		w.step(dt, 8, 3);
		for (Body b = w.getBodyList(); b != null; b = b.getNext()) {
			Actor a = (Actor) b.getUserData();
			if (a.toStore) {
				store(a);
			} else if (a.wear <= 1) {
				a.destroy();
				delete(a);
			}
		}
		
		w.clearForces();
	}
	
	//Returns the next free ID
	public static int getNewId() {
		return nextId++;
	}
	
	//Returns the object corresponding with that id
	public Object get(int id) {
		if (id == -1)
			return this;
		Actor a = actors.get(id);
		return a;
	}
	
	//Anybody win game?
	public boolean won() {
		return orangebots == 0 || bluebots == 0;
	}
	
	public Team whoWon() {
		if (orangebots == 0)
			return Team.BLUE;
		else if (bluebots == 0)
			return Team.ORANGE;
		return Team.NUETRAL;
	}
	
	//Deletes the actor and it's internal refs
	public void delete(Actor a) {
		w.destroyBody(a.b);
		actors.remove(a.id);
		activeActors.remove(a);
	}
	
	//"Stores" the actor, for later use.
	public void store(Actor a) {
		activeActors.remove(a);
		w.destroyBody(a.b);
	}
	
	//Derf
	public void chat(String origin, Long time, String message) {
		Console.chat(origin, time, message);
	}
}