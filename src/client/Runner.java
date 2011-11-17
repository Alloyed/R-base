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
import physics.Player;
import physics.PlayerState;
import physics.Stage;
import processing.core.*;
import processing.opengl.*;

@SuppressWarnings("unused")
public class Runner extends PApplet {
	private static final long serialVersionUID = 1L;
	// Config options
	Settings settings;
	
	// Game state
	Client client;
	Stage stage;
	Player pc;
	
	//Gui stuff
	float scale = 1;
	float meterScale = 64;
	ControlP5 gooey;
	boolean menuOn = true;
	ArrayList<ControllerInterface> mainMenu;
	PImage logo;
	PFont font;
	ControlFont cfont;
	HashMap<String,Sprite> sprites;
	
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
		pc.state.ROTATE_FORCE = hi; //TODO: make an holder object or something
	}
	public void userName(String s) {
		settings.USERNAME = s;
		pc.label = s;
	}
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
		println(settings.IP+ " "+settings.PORT);
		try {
			client = new Client(InetAddress.getByName(settings.IP),settings.PORT);
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
		((Textfield) gooey.controller("userName"))
			.setValue(settings.USERNAME.toString());
	}
	
	@Override
	public void setup() {
		if (settings == null)
			settings = new Settings("ClientSettings.xml");
		// Graphix stuf
		String renderer = (settings.USE_OPENGL ? OPENGL : P2D); //Just in Case
		size(settings.WINDOW_WIDTH, settings.WINDOW_HEIGHT, renderer);
		background(0);
		smooth();
		hint(ENABLE_OPENGL_4X_SMOOTH);
		frameRate(30);
		scale = width < height ? width / 800f : height / 600f;
		meterScale = scale*64f;
		
		if (logo == null) {
			sprites = new HashMap<String,Sprite>();
			for (File f: new File("data/images").listFiles()) {
				sprites.put(f.getName(), new Sprite(this, f.toString()));
			}
			// Physix stuf
			stage = new Stage();
			for (int i=0;i<50;++i)
				new Actor(stage,new Vec2(random(0,width)/meterScale,random(0,height)/meterScale),1f);
			pc = new Player(stage);
			
			// Gooey Stuf
			font = createFont("uni05_53.ttf",8,false);
			textFont(font);
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
	}

	@Override
	public void draw() {
		stage.step();
		if (menuOn) {
			background(color(25, 50, 50));
			image(logo, width / 2f, height / 2f);
		} else {
			pc.state.aim.x = mouseX/meterScale;
			pc.state.aim.y = mouseY/meterScale;
			drawWorld();
		}
		gooey.draw();
		fps();
	}
	
	void draw(Actor a) {
		Sprite s = sprites.get(a.image);
		s.draw(a);
	}
	
	void drawWorld() {
		background(20);
		for (Actor a:stage.actors) {
			draw(a);
		}
		//crosshairs
		pushMatrix();
			noFill();
			stroke(255);
			strokeWeight(2);
			translate(pc.state.aim.x*meterScale,pc.state.aim.y*meterScale);
			rect(-2, -2, 4, 4);
		popMatrix();
	}

	public void fps() {
		textMode(SCREEN);
		fill(255);
		text("FPS: " + (int)frameRate  + ", Actors: " + stage.actors.size(), width - 150, height);
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
			if (key == 'e')
				//TODO pick up objects instead of spawning them, toggle on/drop
				pc.pickup(new Actor(stage,new Vec2(mouseX/meterScale,mouseY/meterScale),2));
			if (key == 'w')
				pc.state.upPressed = true;
			else if (key == 'a')
				pc.state.leftPressed = true;
			else if (key == 's')
				pc.state.downPressed = true;
			else if (key == 'd')
				pc.state.rightPressed = true;
		}
	}

	@Override
	public void keyReleased() {
		if (!menuOn) {
			if (key == 'e')
				pc.drop();
			if (key == 'w')
				pc.state.upPressed = false;
			else if (key == 'a')
				pc.state.leftPressed = false;
			else if (key == 's')
				pc.state.downPressed = false;
			else if (key == 'd')
				pc.state.rightPressed = false;
		}
	}
	
	
	public void mousePressed() {
		if (!menuOn) ;
			//pc.fire();
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "--present", "--hide-stop", "client.Runner" });
	}
}
