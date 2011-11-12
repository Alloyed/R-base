/*
 * ROBOTGAME IS SERIOUS BUSINESS
 * 
 * SERIOUS
 * 
 * WITH COMMENTS
 *  
 *  TODO:Work out classes for robots, levels, etc. fix "toward cursor" mode, get friction and multiple bodies
 *  PROBABLE PROBLEMS: No "window" in gui, cannot control mouse acceleration
 */

import java.util.ArrayList;

import controlP5.*; //gooey
import processing.core.*; //graphix
//Physix
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.*;


public class Main extends PApplet {
	private static final long serialVersionUID = 1L;
	World physics;
	ControlP5 gooey;
	boolean ROTATE_FORCE = false; //does W point up, or to the cursor?
	boolean menuOn = true; //Are we looking at the menu?
	//This stuff would be a good example of 'user data' for bodies
	boolean UP_PRESSED, LEFT_PRESSED, RIGHT_PRESSED, DOWN_PRESSED; 
	Body pc;
	float speed = 5; //vroom vroom
	ArrayList<ControllerInterface> mainMenu;
	
	Vec2 angtoVec(float ang, float mag)
	{
		return new Vec2(mag*cos(ang),mag*sin(ang));
	}
	
	public void quit()
	{
		exit();
	}
	
	public void resume()
	{
		gooey.controller("resume").setLabel("resume");
		menuOn = !menuOn;
		doMenu();
	}
	
	public void setup()
	{
		//Graphix stuf
		size(800,600);
		frameRate(60);
		background(0);
		smooth();
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
		fd.friction = .4f; //Dat coefficient
		
		pc = physics.createBody(d);
		pc.createFixture(fd);
		
		//Gooey Stuf
		gooey = new ControlP5(this);
		gooey.load("controlP5.xml");//See that for the gooey options. controller names correspond with method names
		mainMenu = new ArrayList<ControllerInterface>();
		for (ControllerInterface c : gooey.getControllerList())
			mainMenu.add(c);
		gooey.controller("resume").setLabel("start");
	}
	
	public void draw()
	{	
		//Physix
		Vec2 move = new Vec2(0,0);
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
		physics.clearForces();
	
		if (!menuOn) { //GAME!
			worldDraw();
		} else { //MENU!
			background(150);
		}
	}
	
	//This will be much more important later, I imagine.
	void worldDraw()
	{
		background(20);
		Vec2 v = pc.getPosition();
		pushMatrix();
		fill(255);
		translate(v.x*64,v.y*64);
		rotate(pc.getAngle());
		rect(-32,-32,64,64);
		fill(100);
		rect(0,-16,32,32);
		popMatrix();
	}
	
	//This is called when the menu is turned on or off
	void doMenu() 
	{
		for (ControllerInterface c: mainMenu) {
			if (menuOn)
				c.show();
			else
				c.hide();
		}
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
		if (key == TAB) {
			menuOn = !menuOn;
			doMenu();
		} else if (key == 'o')
			gooey.save();
		else
		if (key == 'w')
			UP_PRESSED = false;
		else if (key == 'a')
			LEFT_PRESSED = false;
		else if (key == 's')
			DOWN_PRESSED = false;
		else if (key == 'd')
			RIGHT_PRESSED = false;
	}
	
	public static void main(String[] args)
	{
		 PApplet.main(new String[] { "--present", "Main" });
	}
}
