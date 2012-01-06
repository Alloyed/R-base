package physics.map;

import java.util.Random;

import org.jbox2d.common.Vec2;

import physics.Props.Conveyor;

public class ConveyorRoom implements Room{
	public void gen(Map m, Random rand, float x0, float y0) {
		Vec2 head = new Vec2(x0+(m.size/2), y0+(m.size/2));
		float ang = 0;
		for (int i = 0; i < 5; ++i) {
			ang += rand.nextFloat()*Math.PI - (Math.PI/2);
			m.addProp(Conveyor.class, new Vec2(3,1), 
					head.add(
							new Vec2(1.5f*(float)Math.cos(ang), 
									1.5f*(float)Math.sin(ang))), 
					ang);
			head = head.add(new Vec2(3f*(float)Math.cos(ang), 3f*(float)Math.sin(ang)));
		}
	}
}
