package physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

public class Bullet extends Actor {
	public final int maxWear = 2;
	public Bullet(float size) {
		super(size);
		baseImage = "bullet";
	}
	public void makeBody(BodyDef d, FixtureDef fd) {
		CircleShape s = new CircleShape();
		s.m_radius = sizeH/2f;
		fd.shape = s;
		//lol, heavy.
		fd.friction = .4f;
		fd.density = 50;
		d.bullet = true;
	}
}
