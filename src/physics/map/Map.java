package physics.map;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.contacts.Contact;

import physics.*;
import physics.Props.Floor;
import physics.Props.Prop;
import physics.actors.Actor;

import com.kitfox.svg.*;
/**
 * Holds anything in the background.
 * TODO: load from .svg
 * @author kyle
 *
 */
public class Map extends Actor {
	public ArrayList<Prop> props;
	public ArrayList<Room> rooms;
	PortalRoom thinking;
	
	public Map() {
		super();
		Console.dbg.println("MAPALIZE");

		dmg = 0;
		props = new ArrayList<Prop>();
		rooms = new ArrayList<Room>();
		rooms.add(new BoxRoom());
		rooms.add(new ConveyorRoom());
		//rooms.add(new CannonRoom());
		rooms.add(new BoosterRoom());
		thinking = new PortalRoom();
		rooms.add(thinking);
	}
	
	@Override
	public void makeBody() {
		label = "game map";
		baseImage = "map";
		d.type = BodyType.STATIC;
		fd = null; // We make the fixtures later
	}
	
	@Override
	public void force() {
		for (Prop p: props)
			p.force();
	}
	
	@Override
	public void beginContact(Contact c, Actor other) {
		
		if (c.getFixtureA().getBody() == b)
			((Prop)c.getFixtureA().getUserData()).beginContact(c, other);
		else
			((Prop)c.getFixtureB().getUserData()).beginContact(c, other);
			
	}

	@Override
	public void endContact(Contact c, Actor other) {
		
		if (c.getFixtureA().getBody() == b)
			((Prop)c.getFixtureA().getUserData()).endContact(c, other);
		else
			((Prop)c.getFixtureB().getUserData()).endContact(c, other);
		
	}
	
	@Override
	public void preSolve(Contact arg0, Manifold arg1, Actor other) {}
	
	@Override
	public void postSolve(Contact c, ContactImpulse imp, Actor other) {}
	
	@Override
	public void hurt(float force) {}
	
	@Override
	public void destroy() {}
	
	/* TODO */
	public void load(String file) {
		SVGUniverse u = new SVGUniverse();
		try {
			u.loadSVG(new URL(file));
		} catch (MalformedURLException e) {
			e.printStackTrace(Console.dbg);
		}
	}

	public Prop addProp(Class<? extends Prop> type, Vec2 size, Vec2 pos, float ang) {
		try {
			Prop a = type.newInstance();
			a.create(pos, size, ang);
			a.place(this);
			props.add(a);
			return a;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Prop addProp(Class<? extends Prop> type, Vec2 size, Vec2 pos) {
		return addProp(type, size, pos, 0);
	}

	public void startGame(Long seed) {
		Random rand = new Random(seed); // We need the seed because DETERMINISM!
		// Two different loops because we have no layers
		int maxX = 10, maxY = 2;
		for (int i = 0; i < maxX; ++i) {
			for (int j = 0; j < maxY; j++) {
				genFloor(rand, i, j);
			}
		}
		
		for (int i = 0; i < maxX; ++i) {
			for (int j = 0; j < maxY; j++) {
				genRoom(rand, i, j, maxX - 1, maxY - 1);
			}
		}
		thinking.link();
	}

	public void genFloor(Random rand, int x, int y) {
		float x0 = x * size, y0 = y * size;
		addProp(Floor.class, new Vec2(size, size), new Vec2(x0 + (size / 2), y0
				+ (size / 2)));
	}
	
	final float size = 20f;
	final float w = 1, hw = .5f;
	public void genRoom(Random rand, int x, int y, int maxx, int maxy) {
		float x0 = x * size, y0 = y * size;
		// Right wall
		if (x == 0) {
			addProp(Prop.class, new Vec2(w, size),
					new Vec2(x0, y0 + (size / 2)));
		}

		// Top wall
		if (y == 0) {
			addProp(Prop.class, new Vec2(size, w),
					new Vec2(x0 + (size / 2), y0));
		}

		// Left wall
		if (x == maxx) {
			addProp(Prop.class, new Vec2(w, size), new Vec2(x0 + size, y0
					+ (size / 2)));
		} else {
			addProp(Prop.class, new Vec2(w, size / 3), new Vec2(x0 + size, y0
					+ (size / 6)));
			addProp(Prop.class, new Vec2(w, size / 3), new Vec2(x0 + size, y0
					+ size - (size / 6)));
		}

		// Bottom wall
		if (y == maxy) {
			addProp(Prop.class, new Vec2(size, w), new Vec2(x0 + (size / 2),
					y0 + size));
		} else {
			addProp(Prop.class, new Vec2(size / 3, w), new Vec2(x0
					+ (size / 6), y0 + size));
			addProp(Prop.class, new Vec2(size / 3, w), new Vec2(x0 + size
					- (size / 6), y0 + size));
		}
		rooms.get(rand.nextInt(rooms.size())).gen(this, rand, x0, y0);

	}
	
}
