package physics.map;

import java.util.LinkedList;
import java.util.Random;

import org.jbox2d.common.Vec2;

import physics.Props.Portal;

public class PortalRoom implements Room {
	LinkedList<Portal> portals = new LinkedList<Portal>();
	public void gen(Map m, Random rand, float x0, float y0) {
		Portal p = (Portal) m.addProp(Portal.class, new Vec2(1, 1),
			new Vec2(x0+(m.size/2), y0+(m.size/2)));
		portals.add(p);
	}
	
	public void link() {
		Portal first = portals.peekLast();
		for (Portal p : portals) {
			p.link(first);
			first = p;
		}
	}
}
