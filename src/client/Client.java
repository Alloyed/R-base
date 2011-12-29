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
import java.awt.Graphics2D;
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
import procontroll.*;
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
	public Godmode godmode;
	public UI currentMode;
	
	//Gui stuff
	public PApplet p;
	public Camera cam;
	public ControlP5 gooey;
	int mode = 0;
	public PFont font;
	ControlFont cfont;
	public Skin skin;
	public ControllDevice joypad;

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
		((Map)stage.addActor(Map.class, -2, Team.NUETRAL, new Vec2(0,0), new Vec2(0,0))).startGame(123l);
	}
	
	//Initializes everything
	public Client(PApplet p) {
		this.p = p;
		p.registerDraw(this);
		p.registerDispose(this);
		settings = new Settings(p);
	}
	
	public void setup() {
		// Graphix stuf
		p.background(0);
		p.smooth();
		p.frameRate(60);
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
			godmode = new Godmode(this);
			for (ControllerInterface c : gooey.getControllerList()) {
				if (c instanceof Textfield)
					((Textfield) c).setAutoClear(false);
			}
			try {
				for (int i = 0; i < ControllIO.getInstance(p).getNumberOfDevices(); ++i) {
					ControllDevice d = ControllIO.getInstance(p).getDevice(i);
					if (!(d.getName().equals("Mouse") || d.getName().equals("Keyboard"))) {
						joypad = d;
						break;
					}
				}
			} catch (Exception e) {} //No joypad
			net = new Network(this);
		}
		
		oldtime = System.nanoTime();
		skin.start();
		ghostmode.start(new Vec2(1,1));
		currentMode = ghostmode;
		menu.setTeam(settings.team == 0 ? Team.ORANGE : Team.BLUE);
		menu.show();
	}
	

	//Is looped over to draw things
	long time, oldtime;
	float physAccum = 0, netAccum = 0;
	public void draw() {
		if (font != null) {
			p.textFont(font);
			font = null;
		}
		
		if (joypad != null) {
			for (int i = 0; i < joypad.getNumberOfButtons(); ++i) {
				joypad.getButton(i).pressed();
			}
		}
		
		//Timing
		time = System.nanoTime();
		float frameTime =  time - oldtime;
		frameTime /= 1000000f;
		//if (frameTime > 2500)
		//	frameTime = 2500;
		physAccum += frameTime;
		netAccum  += frameTime;
		oldtime = time;
		
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
		if (settings.FIXED_TIMESTEP) {
			while (physAccum > Stage.frame*1000) {
				for (Actor a: stage.activeActors) {
					a.oldPos = new Vec2(a.b.getWorldCenter());
					a.oldAng = a.b.getAngle();
				}
				stage.step(Stage.frame);
				physAccum -= Stage.frame*1000f;
				i++;
			}
			Actor.alpha = (physAccum / (Stage.frame*1000.0f));
			Actor.alpha = 1;
		} else {
			i = 1;
			Console.out.println(frameTime/1000f + " " + Stage.frame);
			stage.step(frameTime / 1000f);
			Actor.alpha = 1;
		}
		
		//Drawing
		currentMode.draw();
		gooey.draw();
		fps(i);
	}
	
	public void draw(Actor a) {
		skin.get(a).draw(a);
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
		} else if (p.keyCode == settings.SCREENSHOT) {
			p.saveFrame("screen-###.png");
			Console.chat.println("\\Screenshot saved.");
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
		currentMode.mouseReleased();
		/*
		if (!gooey.getGroup(currentMode.group).isMouseOver()) {
			;
		}
		*/
	}
}
