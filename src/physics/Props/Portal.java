package physics.Props;

import java.util.LinkedList;

import org.jbox2d.dynamics.contacts.Contact;

import physics.actors.Actor;
import physics.map.Map;

public class Portal extends Prop {
	public Portal linked;
	public LinkedList<Actor> unportal = new LinkedList<Actor>();
	public LinkedList<Actor> portal = new LinkedList<Actor>();
	@Override
	public void makeFixture() {
		super.makeFixture();
		label = "portal";
		baseImage = "portal";
		fd.isSensor = true;
	}
	
	@Override
	public void beginContact(Contact c, Actor other) {
		if (other instanceof Map || unportal.contains(other))
			return;
		portal.add(other);
		linked.unportal.add(other);
	}
	
	public void force() {
		for (Actor a : portal)
			a.b.setTransform(linked.pos, a.b.getAngle());
		portal.clear();
	}
	
	@Override
	public void endContact(Contact c, Actor other) {
		unportal.remove(other);
	}
	
	public void link(Portal other) {
		this.linked = other;
		//Uncomment to pair portals
		//other.linked = this;
	}
}
