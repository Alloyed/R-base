package physics.Props;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;

import physics.actors.Actor;


/* I'm gonna convey the shit outta you*/
public class Conveyor extends Prop {
	public ArrayList<Actor> toScoot;
	float k = 10f;
	Vec2 scoot;
	@Override
	public void makeFixture() {
		super.makeFixture();
		label = "conveyor belt";
		baseImage = "belt";
		fd.isSensor = true;
		toScoot = new ArrayList<Actor>();
		scoot = new Vec2(25*(float)Math.cos(angle),25*(float)Math.sin(angle));
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
