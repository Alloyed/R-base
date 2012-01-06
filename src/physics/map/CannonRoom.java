package physics.map;

import java.util.Random;

import org.jbox2d.common.Vec2;

import physics.Props.Cannon;

public class CannonRoom implements Room {
	public void gen(Map m, Random rand, float x0, float y0) {
		m.addProp(Cannon.class, 
				new Vec2(2,2),
				new Vec2(x0+(m.size/2), y0+(m.size/2)));
	}
}
