package physics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import physics.actors.Actor;
import physics.actors.Floor;
import physics.actors.Prop;

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

	public void startGame(Long seed) {
		Random rand = new Random(seed); //We need the seed because DETERMINISM!
		//Two different loops because we have no layers
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; j++) {
				genFloor(rand, i, j);
			}
		}
		
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; j++) {
				genRoom(rand, i, j, 4, 4);
			}
		}
		
	}
	
	public void genFloor(Random rand, int x, int y) {
		float size = 20f;
		float x0 = x*size, y0 = y*size;
		addActor(Floor.class, 
				new Vec2(size,size), new Vec2(x0 + (size/2), y0 + (size/2)))
					.setTeam(Team.get(rand.nextInt(3)-1));
	}
	
	public void genRoom(Random rand, int x, int y, int maxx, int maxy) {
		float size = 20f;
		float w = 1, hw = .5f;
		
		float x0 = x*size, y0 = y*size;
		//Right wall
		if (x == 0) {
			addActor(Prop.class, new Vec2(w,size), new Vec2(x0, y0+(size/2)));
		}
		
		//Top wall
		if (y == 0) {
			addActor(Prop.class, new Vec2(size,w), new Vec2(x0+(size/2), y0));
		}

		//Left wall
		if (x == maxx) {
			addActor(Prop.class, new Vec2(w,size), new Vec2(x0+size, y0+(size/2)));
		} else {
			addActor(Prop.class, new Vec2(w, size/3), new Vec2(x0+size, y0+(size/6)));
			addActor(Prop.class, new Vec2(w, size/3), new Vec2(x0+size, y0+size-(size/6)));
		}
		
		//Bottom wall
		if (y == maxy) {
			addActor(Prop.class, new Vec2(size, w), new Vec2(x0+(size/2),y0+size));
		} else {
			addActor(Prop.class, new Vec2(size/3, w), new Vec2(x0+(size/6), y0+size));
			addActor(Prop.class, new Vec2(size/3, w), new Vec2(x0+size-(size/6), y0+size));
		}
		//Random boxes. Fun!
		for (int i=0;i<10;++i)
			addActor(Actor.class, new Vec2(1, 1), 
					new Vec2(x0+hw+(rand.nextFloat()*(size-w)),
							y0+hw+(rand.nextFloat()*(size-w))));
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
	public void step() {
		for (Actor a: activeActors) {
			a.force();
		}
		
		w.step(frame, 8, 3);
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
		return false;
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


