package physics.actors;

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

import com.kitfox.svg.*;

public class Map extends Actor {
	public ArrayList<Prop> props;

	public Map() {
		super();
		props = new ArrayList<Prop>();
	}
	
	@Override
	public void makeBody() {
		baseImage = "map";
		d.type = BodyType.STATIC;
		fd = null; // We will make the fixtures later
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

	public Prop addProp(Class<? extends Prop> type, Vec2 size, Vec2 pos) {
		try {
			Prop a = type.newInstance();
			a.create(pos, size);
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

	}

	public void genFloor(Random rand, int x, int y) {
		float size = 20f;
		float x0 = x * size, y0 = y * size;
		addProp(Floor.class, new Vec2(size, size), new Vec2(x0 + (size / 2), y0
				+ (size / 2)));
	}

	public void genRoom(Random rand, int x, int y, int maxx, int maxy) {
		float size = 20f;
		float w = 1, hw = .5f;

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
		// A booster. GOTTA GO FAST
		addProp(physics.actors.Conveyor.class, new Vec2(5, 3), new Vec2(x0
				+ (size / 2f), y0 + (size / 2f)));

		// Random boxes. Fun!
		for (int i = 0; i < 10; ++i)
			s.addActor(Actor.class, new Vec2(1, 1),
					new Vec2(x0 + hw + (rand.nextFloat() * (size - w)),
							 y0 + hw + (rand.nextFloat() * (size - w))));

	}	
}
