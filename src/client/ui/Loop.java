package client.ui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import physics.Stage;
import physics.actors.Actor;

import client.Camera;
import client.Settings;
import client.sprites.Skin;

public class Loop {
	public Camera cam;
	public Settings settings;
	public Stage stage;
	public Skin skin;
	
	public Loop(GameContainer gc) {
		cam = new Camera(gc);
		settings = new Settings();
		stage = new Stage();
		stage.startGame(123l);
		skin = new Skin(this);
		skin.start();
	}
	
	public void draw(Graphics g, Actor a) {
		skin.get(a).draw(g, a);
	}

	public void update(int dt) {
		for (int i = dt; i > 16; i-=16) {
			stage.step();
		}
	}
}
