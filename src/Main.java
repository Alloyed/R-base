/*
 * ROBOTGAME IS SERIOUS BUSINESS
 * 
 * SERIOUS
 * 
 * WITH COMMENTS
 *  
 *  TODO:Work out classes for robots, bullets, levels, etc., get multiple bodies, flesh out UI, all of networking, find something for craig and darshan to do
 *  PROBABLE PROBLEMS: No built-in "window" in gui, cannot control mouse acceleration
 *  Scales: Box2d works in meters/kilograms/seconds, in the window, 64 px = 1 m.
 *  	a robot is 1 m long/wide, and need some density/friction/restitution/speed adjustments to be fun to play as.
 */

//Util
import java.util.ArrayList;
//Physix
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.*;
//gooey
import controlP5.*;
//graphix
import processing.core.*; 


public class Main extends PApplet {
	private static final long serialVersionUID = 1L;
	//Config options
	Boolean ROTATE_FORCE = true; // does W point up, or to the cursor?
	Integer WINDOW_WIDTH=800,WINDOW_HEIGHT=600;
	Settings settings;
	
	//Game state
	World physics;
	ControlP5 gooey;
	boolean menuOn = true;
	ArrayList<ControllerInterface> mainMenu;
	
	// This stuff would be a good example of 'user data' for bodies
	Body pc;
	boolean upPressed, leftPressed, rightPressed, downPressed;
	float speed = 75; // vroom vroom
	

	public void quit() {
		exit();
	}

	public void resume() {
		gooey.controller("resume").setLabel("resume");
		toggleMenu();
	}

	@Override
	public void setup() {
		settings = new Settings("data/Settings.xml");
		//This should be simpler to do, but changing it probably involves Deep Java Magic
		if (settings.get("ROTATE_FORCE")!= null)
			ROTATE_FORCE = (Boolean)settings.get("ROTATE_FORCE");
		if (settings.get("WINDOW_HEIGHT")!= null)
			WINDOW_HEIGHT = (Integer)settings.get("WINDOW_HEIGHT");
		if (settings.get("WINDOW_WIDTH")!= null)
			WINDOW_WIDTH = (Integer)settings.get("WINDOW_WIDTH");
		// Graphix stuf
		size(WINDOW_WIDTH, WINDOW_HEIGHT);
		frameRate(60);
		background(0);
		smooth();
		// Physix stuf
		physics = new World(new Vec2(0, 0), false);

		BodyDef d = new BodyDef();
		d.position.set(1, 1); // pos
		d.type = BodyType.DYNAMIC;
		PolygonShape s = new PolygonShape();
		s.setAsBox(1, 1); // size
		FixtureDef fd = new FixtureDef();
		fd.shape = s;
		fd.density = .8f;
		fd.friction = .1f;

		pc = physics.createBody(d);
		pc.createFixture(fd);

		// Gooey Stuf
		gooey = new ControlP5(this);
		gooey.load("controlP5.xml"); // See that for the gooey options.
		// NOTE: controller names correspond with method names
		mainMenu = new ArrayList<ControllerInterface>();
		for (ControllerInterface c : gooey.getControllerList())
			mainMenu.add(c);
		gooey.controller("resume").setLabel("start");
	}

	@Override
	public void draw() {
		doPhysics();
		if (!menuOn) { // GAME!
			drawWorld();
		} else { // MENU!
			background(150);
		}
	}
	
	public void exit()
	{
		settings.save();
		super.exit();
	}
	
	void doPhysics()
	{
		
		Vec2 dir = new Vec2(mouseX, mouseY).sub(pc.getWorldCenter().mul(64));
		float ang = atan2(dir.y, dir.x);
		pc.setTransform(pc.getWorldCenter(), ang);
		
		Vec2 move = new Vec2(0, 0);
		if (upPressed)
			move.addLocal(0, -speed);
		if (leftPressed)
			move.addLocal(-speed, 0);
		if (rightPressed)
			move.addLocal(speed, 0);
		if (downPressed)
			move.addLocal(0, speed);
		if (ROTATE_FORCE) {
			ang += HALF_PI;
			move.set(move.x * cos(ang) - move.y * sin(ang), 
					move.x * sin(ang) + move.y * cos(ang));
		}
		
		pc.applyForce(move, pc.getWorldCenter());
		
		pc.setLinearDamping(pc.getFixtureList().getFriction()*pc.getMass()*9.8f); //How... Normal.
		
		physics.step(1f / 60f, 8, 3);
		physics.clearForces();
	}
	
	void drawWorld() {
		background(20);
		Vec2 v = pc.getPosition();
		pushMatrix();
		fill(255);
		translate(v.x * 64, v.y * 64);
		rotate(pc.getAngle());
		rect(-32, -32, 64, 64);
		fill(100);
		rect(0, -16, 32, 32);
		popMatrix();
	}

	void toggleMenu() {
		menuOn = !menuOn;
		for (ControllerInterface c : mainMenu) {
			if (menuOn)
				c.show();
			else
				c.hide();
		}
	}
	
	

	@Override
	public void keyPressed() {
		if (key == 'w')
			upPressed = true;
		else if (key == 'a')
			leftPressed = true;
		else if (key == 's')
			downPressed = true;
		else if (key == 'd')
			rightPressed = true;
	}

	@Override
	public void keyReleased() {
		if (key == TAB)
			toggleMenu();
		else if (key == 'o')
			gooey.save();
		else if (key == 'w')
			upPressed = false;
		else if (key == 'a')
			leftPressed = false;
		else if (key == 's')
			downPressed = false;
		else if (key == 'd')
			rightPressed = false;
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "--present", "Main" });
	}
}
