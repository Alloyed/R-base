/*
 * ROBOTGAME IS SERIOUS BUSINESS
 * 
 * SERIOUS
 * 
 * WITH COMMENTS
 *  
 */

import controlP5.*; //gooey
import processing.core.*; //graphix
//Physix
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.*;

/*
 * TODO: Work out classes for robots, etc.
 */
public class Main extends PApplet {
	private static final long serialVersionUID = 1L;
	World physics;
	Body pc; //this should be nicer
	ControlP5 gooey;
	boolean ROTATE_FORCE = true; //does W point up, or to the cursor?
	boolean UP_PRESSED, LEFT_PRESSED, RIGHT_PRESSED, DOWN_PRESSED; //This would be a good example of 'user data' for bodies
	float speed = 5; //vroom vroom
	Vec2 angtoVec(float ang, float mag)
	{
		return new Vec2(mag*cos(ang),mag*sin(ang));
	}
	
	public void setup()
	{
		//Graphix stuf
		size(800,600);
		frameRate(60);
		background(0);
		//Physix stuf
		physics = new World(new Vec2(0,0),false);
		
		BodyDef d = new BodyDef();
		d.position.set(1,1); //pos
		d.type = BodyType.DYNAMIC;
		PolygonShape s = new PolygonShape();
		s.setAsBox(1, 1); //size
		FixtureDef fd = new FixtureDef();
		fd.shape = s;
		fd.density = .5f;
		fd.friction = .9f; //Dat coefficient
		pc = physics.createBody(d);
		pc.createFixture(fd);
		//Gooey Stuf
		gooey = new ControlP5(this);
		gooey.addButton("quit"); //calls method /w name quit
		
	}
	
	public void quit()
	{
		exit();
	}
	
	public void draw()
	{
		Vec2 move = new Vec2(0,0);
		//Physix
		Vec2 dir = new Vec2(mouseX,mouseY).sub(pc.getWorldCenter().mul(64));
		float ang = atan2(dir.y,dir.x);
		pc.setTransform(pc.getWorldCenter(),ang);
		
		if (UP_PRESSED)
			move.addLocal(0, -speed);
		if (LEFT_PRESSED)
			move.addLocal(-speed,0);
		if (RIGHT_PRESSED)
			move.addLocal(speed,0);
		if (DOWN_PRESSED)
			move.addLocal(0, speed);
		if (ROTATE_FORCE) {
			move.set(move.x*cos(ang)-move.y*sin(ang),move.x*sin(ang)+move.y*cos(ang));
		}
		pc.applyForce(move, new Vec2());
		physics.step(1f/60f, 8, 3);
		physics.clearForces(); //The book said to so I will
		//Drawing
		background(0);
		Vec2 v = pc.getPosition();
		pushMatrix();
		fill(255);
		translate(v.x*64,v.y*64);
		rotate(pc.getAngle());
		rect(-32,-32,64,64);
		fill(100);
		rect(0,-16,32,32);
		popMatrix();
		gooey.draw();
	}
	
	public void keyPressed()
	{
		if (key == 'w')
			UP_PRESSED = true;
		else if (key == 'a')
			LEFT_PRESSED = true;
		else if (key == 's')
			DOWN_PRESSED = true;
		else if (key == 'd')
			RIGHT_PRESSED = true;
			
	}
	
	public void keyReleased()
	{
		if (key == 'w')
			UP_PRESSED = false;
		else if (key == 'a')
			LEFT_PRESSED = false;
		else if (key == 's')
			DOWN_PRESSED = false;
		else if (key == 'd')
			RIGHT_PRESSED = false;
	}
	
}
