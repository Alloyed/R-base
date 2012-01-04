package physics.Props;


import java.util.LinkedList;

import org.jbox2d.common.*;

import physics.Stage;
import physics.actors.Actor;
import physics.actors.Bullet;

public class Cannon extends Prop {
	float timer;
	float angle;
	public LinkedList<Actor> toFire = new LinkedList<Actor>();
	@Override
	public void makeFixture() {
		super.makeFixture();
		label = "Cannon";
		baseImage = "cannon";
		timer = 0;
		angle = 0;
	}
	
	@Override
	public void force() {
		LinkedList<Actor> unfired = new LinkedList<Actor>();
		for (Actor a : toFire) {
			if (a.b == null) {
				unfired.add(a);
			} else {
				a.b.applyLinearImpulse(
						a.b.getWorldCenter().sub(pos)
						.mul(50).mul(a.b.getMass()), 
						a.b.getWorldCenter());
			}
		}
		toFire.clear();
		toFire.addAll(unfired);
		
		timer += Stage.frame;
		if (timer > 1) {
			timer = 0;
			Stage s = parent.s;
			Vec2 fire = new Vec2(2*MathUtils.cos(angle), 
									2*MathUtils.sin(angle));
			toFire.add(s.addActor(Bullet.class, new Vec2(1, 1), pos.add(fire)));
			angle += MathUtils.QUARTER_PI;
		}
	}
	
}
