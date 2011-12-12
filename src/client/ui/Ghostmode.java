package client.ui;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

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
	}
	
	public void keyPressed() {
		/*if (p.key == 'w')
			cursor.state.upPressed = true;
		else if (p.key == 'a')
			cursor.state.leftPressed = true;
		else if (p.key == 's')
			cursor.state.downPressed = true;
		else if (p.key == 'd')
			cursor.state.rightPressed = true;
		else if (p.key == PConstants.ESC)
			r.menu.show();
			*/
	}

	public void keyReleased() {
		/*
		if (p.key == 'w')
			cursor.state.upPressed = false;
		else if (p.key == 'a')
			cursor.state.leftPressed = false;
		else if (p.key == 's')
			cursor.state.downPressed = false;
		else if (p.key == 'd')
			cursor.state.rightPressed = false;
	*/
	}

	public void mousePressed() {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased() {
		// TODO Auto-generated method stub
		
	}
	
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
