package client.ui;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import physics.Console;
import physics.Team;
import physics.actors.Actor;
import physics.actors.Ghost;
import TWLSlick.BasicTWLGameState;

/*this is Observer mode, the mode you should start as.*/
public class Ghostmode  extends BasicTWLGameState {
	public Ghost cursor;
	public Loop r;
	public Ghostmode(Loop l) {
		r = l;
	}
	
	final static int id = 1;
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return id;
	}
	
	public void start(Vec2 pos) {
		cursor = (Ghost) r.stage.addActor(Ghost.class, 0, Team.get(r.settings.team), new Vec2(1,1), pos);
		Console.chat.println("\\Press '.' to spawn in Robot mode, and ',' to spawn in Commander mode.");
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		start(new Vec2(1,1));
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sg, Graphics g)
			throws SlickException {
		g.setBackground(r.skin.getColor("bg"));
		if (cursor != null)
			r.cam.set(cursor.b.getWorldCenter(), cursor.b.getAngle());
		for (Actor a:r.stage.activeActors) {
			r.skin.draw(g, a);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sg, int dt)
			throws SlickException {
		if (cursor == null) {
			start(new Vec2(1, 1));
		}
		r.update(dt);
		keys(gc, sg, gc.getInput());
	}
	
	public void keys(GameContainer gc, StateBasedGame sg, Input in) {
		if (cursor != null) {
			cursor.state.upPressed = in.isKeyDown(r.settings.UP);
			cursor.state.leftPressed = in.isKeyDown(r.settings.LEFT);
			cursor.state.rightPressed = in.isKeyDown(r.settings.RIGHT);
			cursor.state.downPressed = in.isKeyDown(r.settings.DOWN);
		}
		if (in.isKeyDown(Input.KEY_PERIOD)) {
			sg.enterState(Botmode.id);
		}
		if (in.isKeyDown(Input.KEY_COMMA)) {
			sg.enterState(Godmode.id);
		}
	}

}
