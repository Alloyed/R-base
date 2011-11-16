package client;

/*
 * ROBOTGAME IS SERIOUS BUSINESS
 * 
 * SERIOUS
 * 
 * WITH COMMENTS
 *  
 */

//Util
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.net.InetAddress;
//Physix
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.*;
//gooey
import controlP5.*;
//graphix
import physics.PlayerState;
import processing.core.*;
import processing.opengl.*;

@SuppressWarnings("unused")
public class Runner extends PApplet {
	private static final long serialVersionUID = 1L;
	// Config options
	Settings settings;

	// Game state
	Client client;
	World physics;
	ControlP5 gooey;
	boolean menuOn = true;
	ArrayList<ControllerInterface> mainMenu;
	PImage logo;
	float sc = 1;

	// This stuff would be a good example of 'user data' for bodies
	Body pc;
	PlayerState pcState;
	float speed = 75; // vroom vroom

	/* Gooey methods. TODO:find some way to move this to another class. */
	public void quit() {
		exit();
	}

	public void resume() {
		gooey.controller("resume").setLabel("resume");
		toggleMenu();
	}

	public void rotate(boolean hi) {
		settings.ROTATE_FORCE = hi;
		pcState.ROTATE_FORCE = hi; //TODO: make an holder object or something
	}
	
	//TODO: make a reset button
	public void windowW(String s) {
		settings.WINDOW_WIDTH = Integer.parseInt(s);
	}

	public void windowH(String s) {
		settings.WINDOW_HEIGHT = Integer.parseInt(s);
	}
	
	public void ip(String s) {
		settings.IP = s;
	}
	
	public void port(String s) {
		settings.PORT = Integer.parseInt(s);
	}
	
	void connect() {
		gooey.controller("ip").update();
		gooey.controller("port").update();
		try {
			client = new Client(InetAddress.getByName(settings.IP),settings.PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void exit() {
		settings.save();
		super.exit();
	}
	
	void initControls() {
		// TODO: find some nicer way to do this
		((Toggle) gooey.controller("rotate")).setValue(settings.ROTATE_FORCE);
		((Textfield) gooey.controller("windowW"))
				.setValue(settings.WINDOW_WIDTH.toString());
		((Textfield) gooey.controller("windowH"))
				.setValue(settings.WINDOW_HEIGHT.toString());
		((Textfield) gooey.controller("ip"))
			.setValue(settings.IP);
		((Textfield) gooey.controller("port"))
			.setValue(settings.PORT.toString());
	}
	
	@Override
	public void setup() {
		settings = new Settings("ClientSettings.xml");
		// Graphix stuf
		String renderer = (settings.USE_OPENGL ? OPENGL : P2D); //Just in Case
		size(settings.WINDOW_WIDTH, settings.WINDOW_HEIGHT, renderer);
		background(0);
		smooth();
		textMode(SCREEN);
		frameRate(30);
		sc = width < height ? width / 800f : height / 600f;
		// Physix stuf
		physics = new World(new Vec2(0, 0), true);

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
		pcState = new PlayerState();
		pcState.aim = new Vec2(0,0);
		
		// Gooey Stuf
		gooey = new ControlP5(this);
		gooey.load("controlP5.xml"); // See that for the gooey options.
		initControls();

		for (ControllerInterface c : gooey.getControllerList()) {
			if (c instanceof Textfield)
				((Textfield) c).setAutoClear(false);
		}
		// NOTE: controller names correspond with method names
		gooey.controller("resume").setLabel("start");
		logo = loadImage("logo2.png");
		imageMode(CENTER);
		
	}

	@Override
	public void draw() {

		doPhysics(pcState);

		if (menuOn) {
			background(color(25, 50, 50));
			image(logo, width / 2f, height / 2f);
		} else {
			drawWorld();
			pcState.aim.x = mouseX;
			pcState.aim.y = mouseY;
		}
		gooey.draw();
		fps();
	}

	void doPhysics(PlayerState s) {
		Vec2 dir = s.aim.sub(pc.getWorldCenter().mul(64));
		float ang = atan2(dir.y, dir.x);
		pc.setTransform(pc.getWorldCenter(), ang);

		Vec2 move = new Vec2(0, 0);
		if (s.upPressed)
			move.addLocal(0, -speed);
		if (s.leftPressed)
			move.addLocal(-speed, 0);
		if (s.rightPressed)
			move.addLocal(speed, 0);
		if (s.downPressed)
			move.addLocal(0, speed);
		if (s.ROTATE_FORCE) {
			ang += HALF_PI;
			move.set(move.x * cos(ang) - move.y * sin(ang),
					move.x * sin(ang) + move.y * cos(ang));
		}

		pc.applyForce(move, pc.getWorldCenter());

		pc.setLinearDamping(pc.getFixtureList().getFriction()
				* (pc.getMass() * 9.8f)); // How... Normal.

		physics.step(1f / 30f, 8, 3);
		physics.clearForces();
	}

	void drawWorld() {
		background(20);

		Vec2 v = pc.getPosition();
		pushMatrix();
			fill(255);
			noStroke();
			translate(v.x * 64, v.y * 64);
			scale(sc);
			rotate(pc.getAngle());
			rect(-32, -32, 64, 64);
			fill(100);
			rect(0, -16, 32, 32);
		popMatrix();
		pushMatrix();
			noFill();
			stroke(255);
			strokeWeight(2);
			translate(pcState.aim.x,pcState.aim.y);
			rect(-2, -2, 4, 4);
		popMatrix();
	}

	public void fps() {
		fill(255);
		text("FPS: " + (int)frameRate, width - 50, height);
	}
	
	
	void toggleMenu() {
		menuOn = !menuOn;
		if (menuOn) {
			gooey.show();
			cursor();
		} else {
			gooey.hide();
			noCursor();
		}
	}

	@Override
	public void keyPressed() {
		if (key == ESC) {
			toggleMenu();
			key = 0; // No quitting, quitter
		}
		if (!menuOn) {
			if (key == 'w')
				pcState.upPressed = true;
			else if (key == 'a')
				pcState.leftPressed = true;
			else if (key == 's')
				pcState.downPressed = true;
			else if (key == 'd')
				pcState.rightPressed = true;
		}
	}

	@Override
	public void keyReleased() {
		if (!menuOn) {
			if (key == 'w')
				pcState.upPressed = false;
			else if (key == 'a')
				pcState.leftPressed = false;
			else if (key == 's')
				pcState.downPressed = false;
			else if (key == 'd')
				pcState.rightPressed = false;
		}
	}
	

	public static void main(String[] args) {
		PApplet.main(new String[] { "--present", "--hide-stop", "client.Runner" });
	}
}
