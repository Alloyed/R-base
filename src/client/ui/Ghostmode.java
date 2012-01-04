package client.ui;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import physics.Console;
import physics.Team;
import physics.actors.Ghost;
import TWLSlick.BasicTWLGameState;

/**
 * This is observer mode, where you can see what's happening w/o affecting anything.
 * The name ghost comes from SS13 where you literally are a ghost, haunting the chaplain with your off-topic nonsense
 * 
 * @author kyle
 *
 */
public class Ghostmode  extends BasicTWLGameState {
	public Ghost cursor;
	public Loop r;
	public Ghostmode(Loop l) {
		r = l;
	}
	
	final static int id = 1;
	@Override
	public int getID() {
		return id;
	}
	
	public void start(Vec2 pos) {
		cursor = (Ghost) r.stage.addActor(Ghost.class, 0, Team.get(r.settings.team), new Vec2(1,1), pos);
		Console.chat.println("\\Press '.' to spawn in Robot mode, and ',' to spawn in Commander mode.");
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sg)
			throws SlickException {
		
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sg) 
			throws SlickException {
		super.enter(gc, sg);
		start(new Vec2(1,1));
	}
	
	@Override
	public void leave(GameContainer gc, StateBasedGame sg) 
			throws SlickException {
		super.leave(gc, sg);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sg, Graphics g)
			throws SlickException {
		r.cam.set(cursor.b.getWorldCenter(), cursor.b.getAngle());
		r.render(gc, sg, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sg, int dt)
			throws SlickException {
		r.update(dt);
		keys(gc, sg, gc.getInput());
	}
	
	public void keys(GameContainer gc, StateBasedGame sg, Input in) {
		cursor.state.upPressed    = in.isKeyDown(r.settings.UP);
		cursor.state.leftPressed  = in.isKeyDown(r.settings.LEFT);
		cursor.state.rightPressed = in.isKeyDown(r.settings.RIGHT);
		cursor.state.downPressed  = in.isKeyDown(r.settings.DOWN);
		if (in.isKeyDown(Input.KEY_PERIOD)) {
			sg.enterState(Botmode.id);
			return;
		}
		if (in.isKeyDown(Input.KEY_COMMA)) {
			sg.enterState(Godmode.id);
			return;
		}
	}
	
	@Override
	public void keyPressed(int code, char c) {
		r.keyPressed(code, c);
	}

	@Override
	public void keyReleased(int code, char c) {
		r.keyReleased(code, c);
	}
	
	@Override
	protected void createRootPane() {
		super.createRootPane();
		r.createRootPane(rootPane);
	}
	
    @Override
    protected void layoutRootPane() {
    	r.layoutRootPane(rootPane);
    }
}
