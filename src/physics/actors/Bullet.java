package physics.actors;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;


public class Bullet extends Actor {
	
	public Bullet() {
		super();
		maxWear = 1000;
		wear = maxWear;
		baseImage = "bullet";
	}
	
	public void makeBody(BodyDef d, FixtureDef fd) {
		CircleShape s = new CircleShape();
		s.m_radius = sizeH / 2f;
		fd.shape = s;
		fd.friction = .9f;
		fd.density = 10;
		d.bullet = true;
	}
}
