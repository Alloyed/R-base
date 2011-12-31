package client.ui;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import physics.Stage;
import physics.Team;
import physics.actors.Actor;
import physics.actors.Map;

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
		((Map)stage.addActor(Map.class, -2, 
				Team.NUETRAL, new Vec2(0,0), 
				new Vec2(0,0))).startGame(123l);
		skin = new Skin(this);
		skin.start();
	}
	
	public void draw(Graphics g, Actor a) {
		skin.get(a).draw(g, a);
	}
	
	int accum = 0;
	public void update(int dt) {
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
}
