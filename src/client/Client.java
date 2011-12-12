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
import org.newdawn.slick.*;
import org.newdawn.slick.opengl.renderer.Renderer;

import client.sprites.*;
import client.ui.*;

@SuppressWarnings("unused")
public class Client implements Game {
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
	//public PApplet p;
	public Camera cam;
//	public ControlP5 gooey;
	int mode = 0;
//	public PFont font;
//	ControlFont cfont;
	public Skin skin;

	private Chat chat;
	
	/* Gooey methods. */
//	public void quit() { p.exit(); }
	
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
	public Client() {
//		this.p = p;
//		p.registerDraw(this);
//		p.registerDispose(this);
	}
	
	//Is looped over to draw things
	long time, oldtime;
	float physAccum = 0, netAccum = 0;

	
	public void draw(Actor a) {
		Sprite s = skin.get(a);
		s.draw(a);
	}
	
	public void fps() {
//		p.fill(255);
//		p.text("FPS: " + (int)p.frameRate  + 
//				", Actors: " + stage.activeActors.size(),
//				0, 10);
	}

	public void keyPressed() {
		if (currentMode != menu || chat.isChatting) {
			chat.keyPressed();
		}
		if (!chat.isChatting) {
			currentMode.keyPressed();
		}
			
//		if (p.key == ESC) { //Keeps game from stopping at ESC
//			p.key = 0;
//		}
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
//		if (!gooey.getGroup(currentMode.group).isMouseOver()) {
//			;
//		}
	}

	@Override
	public boolean closeRequested() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(GameContainer p) throws SlickException {
		settings = new Settings();
		if (settings == null)
			settings = new Settings();
//		p.frameRate(30);
//		cam = new Camera(p);
		skin = new Skin(this);
		
		if (menu == null) {
			initPhysics();
			
			// Gooey Stuf
//			gooey = new ControlP5(p);
			chat = new Chat(this, 5);
			Console.chat = new PrintStream(chat);
			menu = new Menu(this);
			botmode = new Botmode(this);
			ghostmode = new Ghostmode(this);
			
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

	@Override
	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		currentMode.draw();
//		gooey.draw();
		fps();
//		p.rectMode(CORNERS);
//		p.noFill();
//		p.stroke(p.color(0,255,0));
		
	}


	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		time = System.nanoTime();
		float frameTime =  time - oldtime;
		oldtime = time;
		physAccum += frameTime/1000000f/1000f;
		netAccum  += frameTime/1000000f/1000f;
		//Networking
		if (net != null) {
			net.poll();
			while (netAccum >= Connection.frame) {
				net.send();
				netAccum -= Connection.frame;
			}
		}
		//Physix
		while (physAccum >= Stage.frame) {
			for (Actor a: stage.activeActors) {
				a.oldPos = new Vec2(a.b.getWorldCenter());
				a.oldAng = a.b.getAngle();
			}
			stage.step();
			physAccum -= Stage.frame;
		}
		Actor.alpha = physAccum / Stage.frame;
		
		
	}
	
	/**
	 * Entry point to our simple test
	 * 
	 * @param argv The arguments passed in
	 */
	public static void main(String argv[]) {
		try {
			Renderer.setRenderer(Renderer.VERTEX_ARRAY_RENDERER);
			Renderer.setLineStripRenderer(Renderer.QUAD_BASED_LINE_STRIP_RENDERER);
			
			AppGameContainer container = new AppGameContainer(new Client());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
