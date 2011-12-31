package client.ui;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import physics.Console;
import physics.Team;
import physics.actors.Actor;
import physics.actors.Ghost;
import client.Client;

/*this is Observer mode, the mode you should start as.*/
public class Ghostmode  extends BasicGameState {
	public Ghost cursor;
	public Loop r;
	public Ghostmode(Loop l) {
		r = l;
	}
	
	public void start(Vec2 pos) {
		cursor = (Ghost) r.stage.addActor(Ghost.class, 0, Team.get(r.settings.team), new Vec2(1,1), pos);
		Console.chat.println("\\Press '.' to spawn in Robot mode, and ',' to spawn in Commander mode.");
	}
	/*
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

	public void mousePressed() {}

	public void mouseReleased() {}
	*/
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sg, Graphics g)
			throws SlickException {
		g.setBackground(r.skin.getColor("bg"));
		r.cam.set(cursor.b.getWorldCenter(), cursor.b.getAngle());
		for (Actor a:r.stage.activeActors) {
			r.draw(g, a);
		}
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
