package client.ui;

import java.io.PrintStream;

import newNet.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import physics.Console;
import physics.Stage;
import physics.actors.Actor;
import physics.actors.PlayerState;
import physics.actors.Robot;
import client.Camera;
import client.Chat;
import client.CNet;
import client.Settings;
import client.sprites.Skin;

/**
 * Holds all the stuff pertinent to the actual game.
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
	public CNet net;
	
	/**
	 * Inits everything.
	 * Generating the map and the skin right now might be bad idea.
	 * @param gc
	 */
	public Loop(GameContainer gc) {
		cam = new Camera(gc);
		settings = new Settings();
		stage = new Stage();
		net = new CNet(this);
		net.setStage(stage);
		skin = new Skin(this);
		chat = new Chat(5);
		Console.chat = new PrintStream(chat);
	}
	
	int accum = 0;
	/**
	 * Updates the world.
	 * Has broken linear interpolation.
	 * TODO: an equivalent render();
	 * @param dt
	 */
	public void update(StateBasedGame sg, int dt) {
		/*
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
		*/
		net.update();
		if (sg.getCurrentStateID() != net.us.currentMode) {
			Console.dbg.println("STATE CHANGE TO "+ net.us.currentMode);
			sg.enterState(net.us.currentMode);
		}
		//TIME FOR LOCKSTEP
		Actor a = stage.actors.get(net.us.id);
		if (net.us.id != 0 && a != null && a instanceof Robot) {
			net.cl.sendTCP((PlayerState)a.state);
			for (Player p : net.players) {
				if (p != net.us)
					net.waitingfor.add(p);
			}
			
			//while (!net.waitingfor.isEmpty()) {net.update();}
		}
		
		
		stage.step(Stage.frame);
	}
	
	void keyPressed(int key, char c) {
		if (key == Input.KEY_TAB) {
			for (Player p : net.players) {
				Console.dbg.println(p.toString());
			}
		}
	}
	
	void keyReleased(int key, char c) {
		
	}

	public void render(GameContainer gc, StateBasedGame sg, Graphics g) {
		g.setBackground(skin.getColor("bg"));
		for (Actor a : stage.activeActors) {
			skin.draw(g, a);
		}
	}
	
}
