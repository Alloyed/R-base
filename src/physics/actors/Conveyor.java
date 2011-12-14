package physics.actors;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;

/* I'm gonna convey the shit outta you*/
public class Conveyor extends Actor {
	public ArrayList<Actor> toScoot;
	@Override
	public void makeBody() {
		super.makeBody();
		fd.isSensor = true;
		toScoot = new ArrayList<Actor>();
	}
	
	@Override
	public void force() {
		for (Actor a : toScoot)
			a.b.applyForce(new Vec2(0,-1000f), a.b.getWorldCenter());
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
