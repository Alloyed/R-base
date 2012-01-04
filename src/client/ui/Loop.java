package client.ui;

import java.io.PrintStream;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import TWLSlick.RootPane;

import physics.Console;
import physics.Stage;
import physics.Team;
import physics.actors.Actor;
import physics.map.Map;
import client.Camera;
import client.Chat;
import client.Net;
import client.Settings;
import client.sprites.Skin;
import de.matthiasmann.twl.Button;

/**
 * Holds all the stuff pertinent to the game's main loop.
 * Holds some other stuff that would normally be global or static too.
 * Yes, I know it's a bad idea, and no, I don't care.
 * 
 * @author kyle
 *
 */
public class Loop {
	public Camera cam;
	public Chat chat;
	public Settings settings;
	public Stage stage;
	public Skin skin;
	public Net net;
	
	/**
	 * Inits everything.
	 * Generating the map and the skin right now might be bad idea.
	 * @param gc
	 */
	public Loop(GameContainer gc) {
		cam = new Camera(gc);
		settings = new Settings();
		stage = new Stage();
		net = new Net();
		((Map)stage.addActor(Map.class, -2, 
				Team.NUETRAL, new Vec2(0,0), 
				new Vec2(0,0))).startGame(System.currentTimeMillis());
		skin = new Skin(this);
		//gc.setVSync(true);
	}
	
	int accum = 0;
	/**
	 * Updates the world.
	 * Has broken linear interpolation.
	 * TODO: an equivalent render();
	 * @param dt
	 */
	public void update(int dt) {
		net.update();
		accum += dt;
		while (accum > 16) {
			stage.step(Stage.frame);
			accum -= 16;
		}
		Actor.alpha = accum / 16;
		
		for (Actor a : stage.activeActors) {
			a.oldPos = a.b.getWorldCenter();
			a.oldAng = a.b.getAngle();
		}
	}
	
	void keyPressed(int key, char c) {
		chat.keyPressed(key, c);
	}
	
	void keyReleased(int key, char c) {
		
	}

	public void render(GameContainer gc, StateBasedGame sg, Graphics g) {
		g.setBackground(skin.getColor("bg"));
		for (Actor a : stage.activeActors) {
			skin.draw(g, a);
		}
	}
	
	public void createRootPane(RootPane rootPane) {
		chat = new Chat(this, 5);
		chat.createRootPane(rootPane);
		Console.chat = new PrintStream(chat);
	}
	
	public void layoutRootPane(RootPane rootPane) {
		chat.layoutRootPane(rootPane);
	}
}
