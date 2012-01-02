package client.ui;

import java.util.ArrayList;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
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

/*One Giant TODO*/
public class Godmode  extends BasicTWLGameState {
	public Ghost cursor;
	public Loop r;
	ArrayList<Actor> selected;
	public Godmode(Loop l) {
		super();
		r = l;
	}
	
	final static int id = 3;
	@Override
	public int getID() {
		return id;
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sg) 
			throws SlickException {
		super.enter(gc, sg);
		start();
	}
	
	public void start() {
		cursor = (Ghost) r.stage.addActor(Ghost.class, 0, Team.get(r.settings.team), new Vec2(1,1), new Vec2(1,1));
		Console.chat.println("\\You are the commander. Help your team kill the other team!");
		selected = new ArrayList<Actor>();
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
		
		if (selecting != null) {
			Input in = gc.getInput();
			Vec2 start = r.cam.worldToScreen(selecting);
			g.drawRect(start.x, start.y, in.getAbsoluteMouseX()-start.x, in.getAbsoluteMouseY()-start.y);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sg, int dt)
			throws SlickException {
		r.update(dt);
		if (cursor == null)
			start();
		Input input = gc.getInput();
		int mouseX = input.getAbsoluteMouseX();
		int mouseY = input.getAbsoluteMouseY();
		//Vec2 oldAim = cursor.state.aim;
		cursor.state.aim = r.cam.screenToWorld(new Vec2(mouseX, mouseY)).sub(cursor.b.getPosition());
		keys(gc, sg, gc.getInput());
	}
	
	public void keys(GameContainer gc, StateBasedGame sg, Input in) {
		if (cursor != null) {
			cursor.state.upPressed = in.isKeyDown(r.settings.UP);
			cursor.state.leftPressed = in.isKeyDown(r.settings.LEFT);
			cursor.state.rightPressed = in.isKeyDown(r.settings.RIGHT);
			cursor.state.downPressed = in.isKeyDown(r.settings.DOWN);
		}
	}
	
	Vec2 selecting;
	boolean chain = false;
	@Override
	public void mousePressed(int btn, int x, int y) {
		if (btn == Input.MOUSE_MIDDLE_BUTTON) {
			Vec2 pos = cursor.state.aim.add(cursor.b.getWorldCenter());
			r.stage.addActor(Actor.class, new Vec2(1,1), pos);
		} else if (btn == Input.MOUSE_LEFT_BUTTON) {
			selecting = r.cam.screenToWorld(new Vec2(x, y));
		} else if (btn == Input.MOUSE_RIGHT_BUTTON) {
			//TODO:Orders/interactions.
		}
		
	}
	
	@Override
	public void mouseReleased(int btn, int x, int y) {
		if (btn == Input.MOUSE_LEFT_BUTTON && selecting != null) {
			//Select objects
			if (!chain) {
				unselect();
			}
			
			Vec2 lower = selecting;
			Vec2 upper = r.cam.screenToWorld(new Vec2(x, y));
			//AABB needs to order the coords so that lower is in top left
			if (lower.x > upper.x) { 
				float t = lower.x;
				lower.x = upper.x;
				upper.x = t;
			}
			if (lower.y > upper.y) { 
				float t = lower.y;
				lower.y = upper.y;
				upper.y = t;
			}
			
			AABB box = new AABB(lower, upper);
			QueryCallback q = new QueryCallback() {
				public boolean reportFixture(Fixture f) {
					Object data = f.getBody().getUserData();
					if (data instanceof Actor) {
						Actor a = (Actor)data;
						if (!a.isHeld && a.b.getType() == BodyType.DYNAMIC) {
							a.isHeld = true;
							selected.add(a);
							return true;
						}
					}
					return true;
				}
			};
			r.stage.w.queryAABB(q, box);
			selecting = null;
		}
	}
	
	@Override 
	public void keyPressed(int key, char c) {
		
	}

	void unselect() {
		for (Actor a : selected) {
			a.isHeld = false;
		}
		selected.clear();
	}

}
