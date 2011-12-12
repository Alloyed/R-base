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
import org.newdawn.slick.state.StateBasedGame;

import client.sprites.*;
import client.ui.*;

@SuppressWarnings("unused")
public class Client extends StateBasedGame {
	public Client(String name) {
		super(name);
	}

	private static final long serialVersionUID = 1L;

	// Config options

	
	// Game state
	
	
	//Views
//	public Menu menu;
//	public Botmode botmode;
//	public Ghostmode ghostmode;
//	public UI currentMode;
	
	//Gui stuff
	//public PApplet p;
	
//	public ControlP5 gooey;
	int mode = 0;
//	public PFont font;
//	ControlFont cfont;
	
	
	/* Gooey methods. */
//	public void quit() { p.exit(); }
	
//	public void resume() { menu.lastMode.show(); }
	
	public void connect() {
//		menu.connect();

	}
	
//	public void exit() { dispose(); }
	
//	public void dispose() { settings.save(); }
	
//	public void inv() { ((Botmode)currentMode).inv(); }
	
//	public void health() { ((Botmode)currentMode).health(); }; 
	
	//End Gooey methods
	
	//Initializes everything
//	public Client() {
//		this.p = p;
//		p.registerDraw(this);
//		p.registerDispose(this);
//	}
	
	//Is looped over to draw things
	long time, oldtime;
	float physAccum = 0, netAccum = 0;
	
	public void fps() {
//		p.fill(255);
//		p.text("FPS: " + (int)p.frameRate  + 
//				", Actors: " + stage.activeActors.size(),
//				0, 10);
	}

//	public void keyPressed() {
//		if (currentMode != menu || chat.isChatting) {
//			chat.keyPressed();
//		}
//		if (!chat.isChatting) {
//			currentMode.keyPressed();
//		}
//			
//		if (p.key == ESC) { //Keeps game from stopping at ESC
//			p.key = 0;
//		}
//	}
/*
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
*/
	/*
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
*/
	
	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
			Loop l = new Loop(arg0);
			addState(new Botmode(l));
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
			
			AppGameContainer container = new AppGameContainer(new Client("R-base"));
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
