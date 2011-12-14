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
import java.io.PrintStream;
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

@SuppressWarnings("unused")
public class Client implements PConstants {
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
	public Botmode botmode;
	public Ghostmode ghostmode;
	public UI currentMode;
	
	//Gui stuff
	public PApplet p;
	public Camera cam;
	public ControlP5 gooey;
	int mode = 0;
	public PFont font;
	ControlFont cfont;
	public Skin skin;

	private Chat chat;
	
	/* Gooey methods. */
	public void quit() { p.exit(); }
	
	public void resume() { menu.lastMode.show(); }
	
	public void connect() {
		menu.connect();

	}
	
	public void exit() { dispose(); }
	
	public void dispose() { settings.save(); }
	
	public void inv() { ((Botmode)currentMode).inv(); }
	
	public void health() { ((Botmode)currentMode).health(); }; 
	
	//End Gooey methods
	
	public void initPhysics() {
		stage = new Stage();
		stage.startGame(32l);
	}
	
	//Initializes everything
	public Client(PApplet p) {
		this.p = p;
		p.registerDraw(this);
		p.registerDispose(this);
	}
	
	public void setup() {
		if (settings == null)
			settings = new Settings(p);
		// Graphix stuf
		p.background(0);
		p.smooth();
		p.frameRate(100);
		cam = new Camera(p);
		skin = new Skin(this);
		
		if (menu == null) {
			initPhysics();
			
			// Gooey Stuf
			gooey = new ControlP5(p);
			chat = new Chat(this, 5);
			Console.chat = new PrintStream(chat);
			menu = new Menu(this);
			botmode = new Botmode(this);
			ghostmode = new Ghostmode(this);
			for (ControllerInterface c : gooey.getControllerList()) {
				if (c instanceof Textfield)
					((Textfield) c).setAutoClear(false);
			}
			
			net = new Network(this);
		}
		
		oldtime = System.nanoTime();
		Console.chat("System",oldtime,"Welcome to R-base!");
		skin.start();
		botmode.start();
		currentMode = botmode;
		menu.setTeam(settings.team == 0 ? Team.ORANGE : Team.BLUE);
		menu.show();
	}

	//Is looped over to draw things
	long time, oldtime;
	float physAccum = 0, netAccum = 0;
	public void draw() {
		if (font != null) {
			p.textFont(font);
		}
		//Timing
		time = System.nanoTime();
		float frameTime =  time - oldtime;
		frameTime /= 1000000f;
		if (frameTime > 2500)
			frameTime = 2500;
		oldtime = time;
		physAccum += frameTime;
		netAccum  += frameTime;
		//Networking
		if (net != null) {
			net.poll();
			while (netAccum >= Connection.frame*1000) {
				net.send();
				netAccum -= Connection.frame*1000;
			}
		}
		//Physix
		int i = 0;
		while (physAccum > Stage.frame*1000) {
			for (Actor a: stage.activeActors) {
				a.oldPos = new Vec2(a.b.getWorldCenter());
				a.oldAng = a.b.getAngle();
			}
			stage.step();
			physAccum -= Stage.frame*1000;
			i++;
		}
		Actor.alpha = (physAccum / (Stage.frame*1000.0f));
		Actor.alpha = 0;
		if (i == 3) {
			Console.dbg.println("Skip: " + physAccum);
		}
		currentMode.draw();
		gooey.draw();
		fps(i);
		p.rectMode(CORNERS);
		p.noFill();
		//p.stroke(p.color(0,255,0));
		p.stroke(p.color(30,30,30,(float)Actor.alpha*255f));
		p.strokeWeight(30);
		//p.rect(0, 0, p.width, p.height);
	}
	
	public void draw(Actor a) {
		Sprite s = skin.get(a);
		s.draw(a);
	}
	
	public void fps(int i) {
		p.fill(255);
		p.text("Frames: " + i  +
				", FPS: " + p.frameRate + 
				", Actors: " + stage.activeActors.size(),
				0, 10);
	}

	public void keyPressed() {
		if (currentMode != menu || chat.isChatting) {
			chat.keyPressed();
		}
		if (!chat.isChatting) {
			currentMode.keyPressed();
		}
			
		if (p.key == ESC) { //Keeps game from stopping at ESC
			p.key = 0;
		} else if (p.key == CODED && p.keyCode == 114) {
			p.saveFrame("screen-###.png");
			Console.chat("System", 0 , "Screenshot saved.");
		}
	}

	public void keyReleased() {
		if (!chat.isChatting) {
			currentMode.keyReleased();
		}
	}
	
	
	public void mousePressed() {
		currentMode.mousePressed();
	}
	
	public void mouseReleased() {
		if (!gooey.getGroup(currentMode.group).isMouseOver()) {
			;
		}
	}
}
