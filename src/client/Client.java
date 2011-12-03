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
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.InetAddress;
//Physix
import physics.*;
import physics.actors.*;

import org.jbox2d.dynamics.*;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.*;

import client.sprites.*;
import client.ui.*;
//gooey
import controlP5.*;
//graphix
import processing.core.*;
import processing.opengl.*;

@SuppressWarnings("unused")
public class Client extends PApplet {
	private static final long serialVersionUID = 1L;

	// Config options
	public Settings settings;
	public final String[] servers = { "localhost", 
										"10.200.5.28", 
										"10.200.5.29", 
										"10.200.5.30" };
	
	// Game state
	public Network net;
	public Stage stage;
	
	//Views
	public Menu menu;
	public UI currentMode;
	
	//Gui stuff
	public Camera cam;
	public ControlP5 gooey;
	int mode = 0;
	PFont font;
	ControlFont cfont;
	public Skin skin;
	
	/* Gooey methods. */
	public void quit() {
		exit();
	}
	
	public void resume() {
		//TODO: Start button. starts local server and connects to it
		menu.lastMode.show();
	}
	
	void connect() {
		if (net != null) {
			net.close();
			net = null;
			return;
		}
		Console.out.println("Connecting to " + settings.IP + ":" + settings.PORT);
		net = new Network(settings.IP,
				Integer.parseInt(settings.PORT),
				new StatusListener() {
					public void setStatus(boolean connected) {
						//TODO: move all the game starting stuff here
						gooey.getController("connect")
							.setCaptionLabel( connected ? "disconnect" : "connect");
					}
				});
		net.start();
	}
	
	public void reset() {
		setup();
	}
	
	public void exit() {
		settings.save();
		super.exit();
	}
	
	public void inv() {
		((Botmode)currentMode).inv();
	}
	
	//End Gooey methods
	
	public void initPhysics() {
		stage = new Stage();
		stage.startGame(32l);
	}
	
	//Initializes everything
	@Override
	public void setup() {
		if (settings == null)
			settings = new Settings("ClientSettings.xml");
		// Graphix stuf
		String renderer = (settings.USE_OPENGL ? OPENGL : JAVA2D); //Just in Case
		size(Integer.parseInt(settings.WINDOW_WIDTH), 
				Integer.parseInt(settings.WINDOW_HEIGHT), renderer);
		background(0);
		smooth();
		frameRate(30);
		cam = new Camera(this);
		skin = new Skin(this);
		
		if (menu == null) {
			initPhysics();
			
			// Gooey Stuf
			font = createFont("uni05_53.ttf",8,false);
			textFont(font);
			gooey = new ControlP5(this);
			menu = new Menu(this);
			
			for (ControllerInterface c : gooey.getControllerList()) {
				if (c instanceof Textfield)
					((Textfield) c).setAutoClear(false);
			}
			
			currentMode = new Botmode(this);
			menu.show();
		}
		oldtime = System.nanoTime();
	}

	//Is looped over to draw things
	long time, oldtime;
	float accum;
	@Override
	public void draw() {
		//Physics
		time = System.nanoTime();
		float frameTime =  time - oldtime;
		oldtime = time;
		accum += frameTime/1000000f/1000f;
		while (accum >= Stage.frame) {
			for (Actor a: stage.activeActors) {
				a.oldPos = new Vec2(a.b.getWorldCenter());
				a.oldAng = a.b.getAngle();
			}
			stage.step();
			accum -= Stage.frame;
		}
		Actor.alpha = accum / Stage.frame;
		
		currentMode.draw();
		gooey.draw();
		fps();
	}
	
	public void draw(Actor a) {
		Sprite s = skin.get(a);
		s.draw(a);
	}
	
	public void fps() {
		fill(255);
		text("FPS: " + (int)frameRate  + 
				", Actors: " + stage.activeActors.size(),
				width - 150, height);
	}

	@Override
	public void keyPressed() {
		currentMode.keyPressed();
		if (key == ESC) { //Keeps game from stopping at ESC
			key = 0;
		}
	}

	@Override
	public void keyReleased() {
		currentMode.keyReleased();
	}
	
	
	public void mousePressed() {
		currentMode.mousePressed();
	}
	
	public void mouseReleased() {
		if (!gooey.getGroup(currentMode.group).isMouseOver()) {
			;
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "--present", 
									"--hide-stop",
									"client.Client" });
	}
}
