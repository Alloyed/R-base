package physics.actors;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;

/* I'm gonna convey the shit outta you*/
public class Conveyor extends Prop {
	public ArrayList<Actor> toScoot;
	float k = .4f;
	Vec2 scoot;
	@Override
	public void makeFixture() {
		super.makeFixture();
		baseImage = "belt";
		fd.isSensor = true;
		toScoot = new ArrayList<Actor>();
		scoot = new Vec2(0,-250);
	}
	
	@Override
	public void force() {
		for (Actor a : toScoot) {
			a.b.applyForce(scoot.sub(a.b.getLinearVelocity()).mul(k), a.b.getWorldCenter());
		}
	}
	
	@Override
	public void beginContact(Contact c, Actor other) {
		toScoot.add(other);
	}
	
	@Override
	public void endContact(Contact c, Actor other) {
		toScoot.remove(other);
	}
}
