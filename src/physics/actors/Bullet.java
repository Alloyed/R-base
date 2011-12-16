package physics.actors;

import org.jbox2d.collision.shapes.CircleShape;


public class Bullet extends Actor {
	
	public Bullet() {
		super();
		maxWear = 1000;
		wear = maxWear;
		baseImage = "bullet";
	}
	
	@Override
	public void makeBody() {
		CircleShape s = new CircleShape();
		s.m_radius = size.length() / 2f;
		fd.shape = s;
		fd.friction = .3f;
		fd.density = 50;
		d.bullet = true;
	}
	
	@Override
	public void destroy() {
		
	}
}
