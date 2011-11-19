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
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.*;
//gooey
import controlP5.*;
//graphix
import physics.Actor;
import physics.Building;
import physics.Player;
import physics.PlayerState;
import physics.Stage;
import processing.core.*;
import processing.opengl.*;

@SuppressWarnings("unused")
public class Runner extends PApplet {
	private static final long serialVersionUID = 1L;
	final String[] servers = {"localhost", "10.200.5.28", "10.200.5.29", "10.200.5.30"};
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
	float meterScale = 64;
	ControlP5 gooey;
	int mode = 0;
	PFont font;
	ControlFont cfont;
	HashMap<String,Sprite> sprites;
	
	/* Gooey methods.*/
	public void quit() {
		exit();
	}
	public void resume() {
		botMode.show();
	}
	void connect() {
		gooey.getController("/settings/ip").update();
		gooey.getController("/settings/port").update();
		println(settings.IP+ " "+settings.PORT);
		try {
			client = new Client(InetAddress.getByName(settings.IP),Integer.parseInt(settings.PORT));
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
	
	void initControls() {
		ControlGroup m = gooey.addGroup("menu", 0, 0);
		gooey.begin(m);
		gooey.addButton("resume");
		gooey.addButton("quit");
		gooey.addButton("connect");
		gooey.addButton("reset");
		gooey.end(m);
		gooey.addControllersFor("/settings", settings);
		gooey.moveTo(m, settings);
		
		gooey.addGroup("botmode", 0, 0);
		gooey.addGroup("godmode", 0, 0);
	}
	
	@Override
	public void setup() {
		if (settings == null)
			settings = new Settings("ClientSettings.xml");
		// Graphix stuf
		String renderer = (settings.USE_OPENGL ? OPENGL : P2D); //Just in Case
		size(Integer.parseInt(settings.WINDOW_WIDTH), Integer.parseInt(settings.WINDOW_HEIGHT), renderer);
		background(0);
		smooth();
		hint(ENABLE_OPENGL_4X_SMOOTH);
		frameRate(30);
		scale = width < height ? width / 800f : height / 600f;
		meterScale = scale*64f;
		
		if (menu == null) {
			sprites = new HashMap<String,Sprite>();
			for (File f: new File("data/images").listFiles()) {
				sprites.put(f.getName(), new Sprite(this, f.toString()));
			}
			// Physix stuf
			stage = new Stage();
			for (int i=0;i<5;++i)
				new Building(stage,
						new Vec2(random(0,width)/meterScale,random(0,height)/meterScale),
						new Vec2(.5f,.25f));
			for (int i=0;i<30;++i)
				new Actor(stage,
						new Vec2(random(0,width)/meterScale,random(0,height)/meterScale),
						1);
			
			godMode = new Godmode(this);
			botMode = new Botmode(this);
			
			// Gooey Stuf
			font = createFont("uni05_53.ttf",8,false);
			textFont(font);
			gooey = new ControlP5(this);
			initControls();

			for (ControllerInterface c : gooey.getControllerList()) {
				if (c instanceof Textfield)
					((Textfield) c).setAutoClear(false);
			}
			menu = new Menu(this);
			currentMode = menu;
			menu.show();
		}
	}

	@Override
	public void draw() {
		stage.step();
		currentMode.draw();
		gooey.draw();
		fps();
	}
	
	void draw(Actor a) {
		Sprite s = sprites.get(a.image);
		s.draw(a);
	}

	public void fps() {
		textMode(SCREEN);
		fill(255);
		text("FPS: " + (int)frameRate  + ", Actors: " + stage.actors.size(), width - 150, height);
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
		PApplet.main(new String[] { "--present", "--hide-stop", "client.Runner" });
	}
}
