package physics;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

public class Ghost extends Actor {
	public Ghost() {
		super();
		baseImage = "none";
	}
	
	@Override
	public void makeBody(BodyDef d, FixtureDef fd) {
		this.fd = null;
	}

}
