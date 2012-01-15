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

import com.esotericsoftware.kryonet.rmi.ObjectSpace;

import physics.actors.Actor;

/**
 * The representation of the in-game world.
 * 
 */
public class Stage implements Stagelike {
	public void say() {System.err.println("SUP NIGGA");}
	class HEYLISTEN implements ContactListener {
		@Override
		public void beginContact(Contact c) {
			Actor A = (Actor)c.getFixtureA().getBody().getUserData();
			Actor B = (Actor)c.getFixtureB().getBody().getUserData();
			A.beginContact(c, B);
			//B.beginContact(c, A);
		}
		@Override
		public void endContact(Contact c) {
			Actor A = (Actor)c.getFixtureA().getBody().getUserData();
			Actor B = (Actor)c.getFixtureB().getBody().getUserData();
			A.endContact(c, B);
			//B.endContact(c, A);
		}
		@Override
		public void postSolve(Contact c, ContactImpulse imp) {
			Actor A = (Actor)c.getFixtureA().getBody().getUserData();
			Actor B = (Actor)c.getFixtureB().getBody().getUserData();
			A.postSolve(c, imp, B);
			//B.postSolve(c, imp, A);
		}
		@Override
		public void preSolve(Contact c, Manifold m) {
			Actor A = (Actor)c.getFixtureA().getBody().getUserData();
			Actor B = (Actor)c.getFixtureB().getBody().getUserData();
			A.preSolve(c, m, B);
			//B.preSolve(c, m, A);
		}
	}
	
	public static float fps = 60;
	public static float frame = 1/60f;
	//Time, right now, is measured by the number of frames that have been calculated.
	//Measured in 60ths of a second then.
	public long time = 0;
	public World w;
	public HashMap<Integer, Actor> actors; //Every actor, retrievable by id.
	public LinkedList<Actor> activeActors; //Every actor in the world right now
	public LinkedList<Actor> toAdd;
	public int orangebots=0, bluebots=0;
	int nextId = 1;
	public ObjectSpace space;
	
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
	 * @return the id of the actor.
	 */
	public int addActor(Class<? extends Actor> type, int id, Team t, Vec2 size, Vec2 pos) {
		try {
			Actor a = type.newInstance();
			a.setTeam(t);
			a.id = id;
			a.create(size, pos);
			a.place(this);
			Console.dbg.println(a.toString() + " created");
			return a.id;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int addActor(Class<? extends Actor> type, Vec2 size, Vec2 pos) {
		return addActor(type, getNewId(), Team.NUETRAL, size, pos);
	}
	
	@SuppressWarnings("unchecked")
	public void addDef(AddDef map) {
		try {
			addActor((Class<? extends Actor>) Class.forName(map.type),
					map.id,
					map.team,
					map.size,
					map.pos);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/*FIXME: ugh what the fuck is going on here*/
	@SuppressWarnings("unchecked")
	public int addActor(String type, Vec2 size, Vec2 pos) {
		Console.dbg.println("HI");
		try {
			return addActor((Class<? extends Actor>) Class.forName(type), getNewId(), Team.NUETRAL, size, pos);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	@SuppressWarnings("unchecked")
	public int addActor(String type, int id, Team t, Vec2 size, Vec2 pos) {
		Console.dbg.println("HI");
		try {
			return addActor((Class<? extends Actor>) Class.forName(type), id, t, size, pos);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Runs one step of the simulation
	 * @param dt the amount of time in the step.
	 */
	public void step(float dt) {
		for (Actor a : toAdd)
			activeActors.add(a);
		toAdd.clear();
		
		for (Actor a : activeActors) {
			a.force();
		}
		
		w.step(dt, 8, 3);
		time ++;
		for (Body b = w.getBodyList(); b != null; b = b.getNext()) {
			Actor a = (Actor) b.getUserData();
			if (a.toStore) {
				a.toStore = false;
				store(a);
			} else if (a.wear <= 1) {
				a.destroy();
				delete(a);
			}
			a.state.snap(a);
		}
		
		w.clearForces();
	}
	
	/**
	 * returns the next free ID number.
	 * @return
	 */
	public int getNewId() {
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
}