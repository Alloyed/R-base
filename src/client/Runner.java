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
import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.InetAddress;
//Physix
import physics.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.*;
//gooey
import controlP5.*;
//graphix
import processing.core.*;
import processing.opengl.*;

@SuppressWarnings("unused")
public class Runner extends PApplet {
	private static final long serialVersionUID = 1L;
	//TODO: store this in the settings file
	final String[] servers = Client.getServers(); //{ "localhost", "10.200.5.28", "10.200.5.29", "10.200.5.30" };
	// Config options
	Settings settings;
	
	// Game state
	Client client;
	Stage stage;
	
	//Views
	Menu menu;
	Botmode botMode;
	Godmode godMode;
	UI currentMode;
	
	//Gui stuff
	float scale = 1;
	float meterScale = 64; //1 m = meterScale pixels
	ControlP5 gooey;
	int mode = 0;
	PFont font;
	ControlFont cfont;
	Skin skin;
	
	/* Gooey methods.*/
	public void quit() {
		exit();
	}
	
	public void resume() {
		botMode.show();
	}
	
	void connect() {
		Console.out.println(settings.IP + " " + settings.PORT);
		try {
			if (client != null) {
				client.s.close();
				client = null;
			}
			client = new Client(InetAddress.getByName(settings.IP),
					Integer.parseInt(settings.PORT));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void reset() {
		setup();
	}
	
	public void exit() {
		settings.save();
		super.exit();
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
		frameRate(60);
		scale = width < height ? width / 800f : height / 600f;
		meterScale = scale*64f;
		skin = new Skin(this);
		
		if (menu == null) {
			initPhysics();
			
			// Gooey Stuf
			font = createFont("uni05_53.ttf",8,false);
			textFont(font);
			gooey = new ControlP5(this);
			menu = new Menu(this);
			godMode = new Godmode(this);
			botMode = new Botmode(this);
			
			for (ControllerInterface c : gooey.getControllerList()) {
				if (c instanceof Textfield)
					((Textfield) c).setAutoClear(false);
			}
			
			currentMode = menu;
			menu.show();
		}
		oldtime = System.nanoTime()/1000000l;
	}

	//Is looped over to draw things
	long time, oldtime;
	float accum;
	@Override
	public void draw() {
		time = System.nanoTime()/1000000l;
		float frameTime =  time - oldtime;
		oldtime = time;
		accum += frameTime/1000f;
		while (accum >= Stage.frame) {
			for (Actor a: stage.actors)
				a.oldPos = new Vec2(a.b.getWorldCenter());
			stage.step();
			accum -= Stage.frame;
		}
		Actor.alpha = accum / Stage.frame;
		currentMode.draw();
		if(client != null)
			try {
				client.sendEvent(botMode.pc.state);
			} catch (IOException e) {
				
			}
		gooey.draw();
		fps();
	}
	
	void draw(Actor a) {
		Sprite s = skin.sprites.get(a.getImage());
		if (s == null) {
			Console.out.print("WARNING: sprite " + a.getImage() + " doesn't exist. ");
			s = skin.sprites.get(a.baseImage);
			if (s == null) {
				Console.out.println("Using default instead.");
				s = skin.sprites.get("box");
			} else {
				Console.out.println("Using base instead.");
			}
			skin.sprites.put(a.getImage(),s);
		} 
		s.draw(a);
	}
	
	//Camera stuf, Horribly hacky
	Robot robot;
	float zeroX, zeroY;
	float camAngle;
	 
	public void camtranslate(float x, float y) {
		translate((x * meterScale) - zeroX,(y * meterScale) - zeroY);
	}
	
	public void camScale(float x, float y) {
		scale(scale * x,scale * y); //wat
	}
	
	//Note: does not work
	public void camrotate(float theta) {
		rotate(theta);
	}
	
	//Moves pos to center, not top right
	public void setCam(Vec2 pos, float ang) {
		zeroX = 1f * ((pos.x * meterScale) - (width / 2f)) + 0f * zeroX;
		zeroY = 1f * ((pos.y * meterScale) - (height / 2f)) + 0f * zeroY;
		camAngle = ang;
	}
	
	public Vec2 screenToWorld(Vec2 in) {
		return new Vec2((in.x-zeroX)/meterScale,(in.y-zeroY)/meterScale);
	}
	
	public Vec2 worldToScreen(Vec2 in) {
		return new Vec2(in.x*meterScale-zeroX,in.y*meterScale-zeroY);
	}
	
	
	public void fps() {
		fill(255);
		text("FPS: " + (int)frameRate  + 
				", Actors: " + stage.actors.size() + 
				", Bullets: " + botMode.pc.inventory.size(), 
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
		
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "--present", 
									"--hide-stop",
									"client.Runner" });
	}
}
