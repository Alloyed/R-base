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
	public Loop loop;
	ArrayList<Actor> selected;
	public Godmode(Loop l) {
		super();
		loop = l;
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
		cursor = (Ghost)loop.stage.get( loop.stage.addActor(Ghost.class, 0, Team.get(loop.settings.team), new Vec2(1,1), new Vec2(1, 1)));
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
		loop.cam.set(cursor.b.getWorldCenter(), cursor.b.getAngle());
		loop.render(gc, sg, g);
		
		if (selecting != null) {
			Input in = gc.getInput();
			Vec2 start = loop.cam.worldToScreen(selecting);
			g.drawRect(start.x, start.y, in.getAbsoluteMouseX()-start.x, in.getAbsoluteMouseY()-start.y);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sg, int dt)
			throws SlickException {
		loop.update(sg, dt);
		if (cursor == null)
			start();
		Input input = gc.getInput();
		int mouseX = input.getAbsoluteMouseX();
		int mouseY = input.getAbsoluteMouseY();
		//Vec2 oldAim = cursor.state.aim;
		cursor.ps.aim = loop.cam.screenToWorld(new Vec2(mouseX, mouseY)).sub(cursor.b.getPosition());
		keys(gc, sg, gc.getInput());
	}
	
	public void keys(GameContainer gc, StateBasedGame sg, Input in) {
		if (cursor != null) {
			cursor.ps.upPressed = in.isKeyDown(loop.settings.UP);
			cursor.ps.leftPressed = in.isKeyDown(loop.settings.LEFT);
			cursor.ps.rightPressed = in.isKeyDown(loop.settings.RIGHT);
			cursor.ps.downPressed = in.isKeyDown(loop.settings.DOWN);
		}
	}
	
	Vec2 selecting;
	boolean chain = false;
	@Override
	public void mousePressed(int btn, int x, int y) {
		if (btn == Input.MOUSE_MIDDLE_BUTTON) {
			Vec2 pos = cursor.ps.aim.add(cursor.b.getWorldCenter());
			loop.stage.addActor(Actor.class, new Vec2(1,1), pos);
		} else if (btn == Input.MOUSE_LEFT_BUTTON) {
			selecting = loop.cam.screenToWorld(new Vec2(x, y));
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
			Vec2 upper = loop.cam.screenToWorld(new Vec2(x, y));
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
			loop.stage.w.queryAABB(q, box);
			selecting = null;
		}
	}
	
	@Override 
	public void keyPressed(int key, char c) {
		if (key == loop.settings.CHAT)
			cb.startChat();
	}

	void unselect() {
		for (Actor a : selected) {
			a.isHeld = false;
		}
		selected.clear();
	}
	
	ChatBox cb;
	@Override
	protected void createRootPane() {
		super.createRootPane();
		cb = new ChatBox(loop);
		cb.add(rootPane);
	}
	
    @Override
    protected void layoutRootPane() {
    	cb.layout(rootPane);
    }
}
