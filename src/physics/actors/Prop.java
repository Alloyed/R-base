package physics.actors;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;

import physics.Team;


/* Use for permanent squares in the world
 * TODO: add a texture mode to Sprite so we can make rectangles
 */
public class Prop {
	FixtureDef fd;
	Fixture f;
	Actor parent;
	String baseImage;
	String modifiers[];
	Team team;
	public float width, height;
	public Vec2 pos;
	public float angle;
	
	public Prop() {
		baseImage = "prop";
		modifiers = new String[5];
	}
	
	public void create(Vec2 pos, Vec2 size, float angle) {
		width = size.x;
		height = size.y;
		fd = new FixtureDef();
		this.pos = pos;
		this.angle = angle;
		makeFixture();
	}
	
	public void place(Map parent) {
		this.parent = parent;
		f = parent.b.createFixture(fd);
		f.setUserData(this);
	}
	
	public void makeFixture() {
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(width/2, height/2, pos, angle);
		fd.shape = ps;
		fd.friction = .5f;
		fd.restitution = 1.1f; //Wee!
	}
	
	public void force() {}
	
	public void beginContact(Contact c, Actor other) {}
	
	public void endContact(Contact c, Actor other) {}
	
	/*Getters and Setters*/
	public String getImage() {
		String s = baseImage;
		for (String m : modifiers)
			if (m != null)
				s += m;
		return s;
	}
	
	public void setTeam(Team t) {
		team = t;
		modifiers[3] = Team.get(t);
	}	
}
