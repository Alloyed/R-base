package client.ui;

import org.jbox2d.common.Vec2;

import physics.Console;
import physics.Team;
import physics.actors.Actor;
import physics.actors.Ghost;
import processing.core.PConstants;
import client.Client;

/*this is Observer mode, the mode you should start as.*/
public class Ghostmode extends UI {
	public Ghost cursor;
	
	public Ghostmode(Client r) {
		super(r);
		group = "ghostmode";
		r.gooey.addGroup(group, 0, 0);
	}
	
	public void start(Vec2 pos) {
		cursor = (Ghost) r.stage.addActor(Ghost.class, 0, Team.get(r.settings.team), new Vec2(1,1), pos);
		Console.chat.println("\\Press '.' to spawn in Robot mode, and ',' to spawn in Commander mode.");
	}
	
	@Override
	public void draw() {
		p.background(r.skin.getColor("bg"));
		r.cam.set(cursor.b.getWorldCenter(), cursor.b.getAngle());
		for (Actor a:r.stage.activeActors) {
			r.draw(a);
		}
		
	}

	@Override
	public void keyPressed(int keycode) {
		if (keycode == r.settings.UP)
			cursor.state.upPressed = true;
		else if (keycode == r.settings.LEFT)
			cursor.state.leftPressed = true;
		else if (keycode == r.settings.DOWN)
			cursor.state.downPressed = true;
		else if (keycode == r.settings.RIGHT)
			cursor.state.rightPressed = true;
		else if (p.key == PConstants.ESC)
			r.menu.show();
	}

	@Override
	public void keyReleased(int keycode) {
		if (keycode == r.settings.UP)
			cursor.state.upPressed = false;
		else if (keycode == r.settings.LEFT)
			cursor.state.leftPressed = false;
		else if (keycode == r.settings.DOWN)
			cursor.state.downPressed = false;
		else if (keycode == r.settings.RIGHT)
			cursor.state.rightPressed = false;
		else if (p.key == '.') {
			r.botmode.start();
			r.botmode.show();
		} else if (p.key == ',') {
			r.godmode.start();
			r.godmode.show();
		}
	}

	@Override
	public void mousePressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void show() {
		super.show();
		p.cursor();
	}
	
}
