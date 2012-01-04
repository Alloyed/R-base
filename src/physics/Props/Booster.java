package physics.Props;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;

import physics.actors.Actor;

/* Speeds things up */
public class Booster extends Prop {
	public ArrayList<Actor> toScoot;
	@Override
	public void makeFixture() {
		super.makeFixture();
		baseImage = "belt";
		fd.isSensor = true;
		toScoot = new ArrayList<Actor>();
	}
	
	@Override
	public void force() {
		for (Actor a : toScoot) {
			Vec2 b = new Vec2(a.b.getLinearVelocity());
			b.normalize();
			b.mulLocal(10);
			a.b.applyLinearImpulse(b, a.b.getWorldCenter());
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
