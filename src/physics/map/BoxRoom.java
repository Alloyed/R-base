package physics.map;

import java.util.Random;

import org.jbox2d.common.Vec2;

import physics.Stage;
import physics.actors.Actor;

public class BoxRoom implements Room {
	// Random boxes. Fun!
	public void gen(Map m, Random rand, float x0, float y0) {
		Stage s = m.s;
		float size = m.size;
		float w = m.w;
		float hw = m.hw;
		for (int i = 0; i < 10; ++i)
			s.addActor(Actor.class, new Vec2(1, 1),
					new Vec2(x0 + hw + (rand.nextFloat() * (size - w)),
					 y0 + hw + (rand.nextFloat() * (size - w))));
	}
}
