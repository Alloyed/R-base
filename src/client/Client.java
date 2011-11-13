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
import java.util.ArrayList;
//Physix
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.*;
//gooey
import controlP5.*;
//graphix
import processing.core.*;
import processing.opengl.*;

public class Client extends PApplet {
	private static final long serialVersionUID = 1L;
	// Config options
	Settings settings;

	// Game state
	World physics;
	ControlP5 gooey;
	boolean menuOn = true;
	ArrayList<ControllerInterface> mainMenu;
	PImage logo;

	// This stuff would be a good example of 'user data' for bodies
	Body pc;
	Vec2 aim;
	boolean upPressed, leftPressed, rightPressed, downPressed;
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
	}

	public void windowW(String s) {
		settings.WINDOW_WIDTH = Integer.parseInt(s);
	}

	public void windowH(String s) {
		settings.WINDOW_HEIGHT = Integer.parseInt(s);
	}

	@Override
	public void setup() {
		settings = new Settings("ClientSettings.xml");
		// Graphix stuf
		size(settings.WINDOW_WIDTH, settings.WINDOW_HEIGHT, P2D);
		frameRate(60);
		background(0);
		smooth();
		textMode(SCREEN);
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
		aim = new Vec2(0, 0);

		// Gooey Stuf
		gooey = new ControlP5(this);
		gooey.load("controlP5.xml"); // See that for the gooey options.
		// TODO: find some nicer way to do this
		((Toggle) gooey.controller("rotate")).setValue(settings.ROTATE_FORCE);
		((Textfield) gooey.controller("windowW"))
				.setValue(settings.WINDOW_WIDTH.toString());
		((Textfield) gooey.controller("windowH"))
				.setValue(settings.WINDOW_HEIGHT.toString());

		for (ControllerInterface c : gooey.getControllerList()) {
			if (c instanceof Textfield)
				((Textfield) c).setAutoClear(false);
		}
		// NOTE: controller names correspond with method names
		gooey.controller("resume").setLabel("start");
		logo = loadImage("logo.png");
		imageMode(CENTER);
	}

	@Override
	public void draw() {

		if (!menuOn) {
			aim.x = mouseX;
			aim.y = mouseY;
		}
		doPhysics();

		// TRANSPARENCY!
		drawWorld();
		if (menuOn) {
			background(color(25, 50, 50));
			image(logo, width / 2f, height / 2f);
		}
		gooey.draw();
		fps();
	}

	public void exit() {
		settings.save();
		super.exit();
	}

	void doPhysics() {
		Vec2 dir = aim.sub(pc.getWorldCenter().mul(64));
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
		if (settings.ROTATE_FORCE) {
			ang += HALF_PI;
			move.set(move.x * cos(ang) - move.y * sin(ang),
					move.x * sin(ang) + move.y * cos(ang));
		}

		pc.applyForce(move, pc.getWorldCenter());

		pc.setLinearDamping(pc.getFixtureList().getFriction()
				* (pc.getMass() * 9.8f)); // How... Normal.

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

	public void fps() {
		fill(255);
		text("FPS: " + frameRate, width - 110, height);
	}

	void toggleMenu() {
		menuOn = !menuOn;
		if (menuOn)
			gooey.show();
		else
			gooey.hide();
	}

	@Override
	public void keyPressed() {
		if (key == ESC) {
			toggleMenu();
			key = 0; // No quitting, quitter
		}
		if (!menuOn) {
			if (key == 'w')
				upPressed = true;
			else if (key == 'a')
				leftPressed = true;
			else if (key == 's')
				downPressed = true;
			else if (key == 'd')
				rightPressed = true;
		}
	}

	@Override
	public void keyReleased() {
		if (!menuOn) {
			if (key == 'w')
				upPressed = false;
			else if (key == 'a')
				leftPressed = false;
			else if (key == 's')
				downPressed = false;
			else if (key == 'd')
				rightPressed = false;
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "--present", "--hide-stop", "client.Client" });
	}
}
