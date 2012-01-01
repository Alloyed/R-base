package client.ui;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
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
		//group = "godmode";
		//r.gooey.addGroup(group, 0, 0);
	}
	
	final static int id = 3;
	@Override
	public int getID() {
		return id;
	}
	
	public void start() {
		cursor = (Ghost) r.stage.addActor(Ghost.class, 0, Team.get(r.settings.team), new Vec2(1,1), new Vec2(1,1));
		Console.chat.println("\\You are the commander. Help your team kill the other team!");
		selected = new ArrayList<Actor>();
	}

	public void keyPressed() {
	/*	if (p.key == 'w')
=======
	@Override
	public void draw() {
		p.background(r.skin.getColor("bg"));
		r.cam.set(cursor.b.getWorldCenter(), cursor.b.getAngle());
		for (Actor a:r.stage.activeActors) {
			r.draw(a);
		}
		if (selecting != null) {
			p.rectMode(CORNERS);
			p.noFill();
			p.stroke(255);
			Vec2 tmp = r.cam.worldToScreen(selecting);
			p.rect(tmp.x, tmp.y, p.mouseX, p.mouseY);
		}
	}

	@Override
	public void keyPressed(int keycode) {
		if (p.key == 'w')
>>>>>>> graphics
			cursor.state.upPressed = true;
		else if (p.key == 'a')
			cursor.state.leftPressed = true;
		else if (p.key == 's')
			cursor.state.downPressed = true;
		else if (p.key == 'd')
			cursor.state.rightPressed = true;
		else if (p.key == ESC)
			r.menu.show();
<<<<<<< HEAD
	*/}
	/*
	public void keyReleased() {
		if (p.key == 'w')
=======
		else if (p.keyCode == CONTROL)
			chain = true;
	}

	@Override
	public void keyReleased(int keycode) {
		if (p.key == 'w')
>>>>>>> graphics
			cursor.state.upPressed = false;
		else if (p.key == 'a')
			cursor.state.leftPressed = false;
		else if (p.key == 's')
			cursor.state.downPressed = false;
		else if (p.key == 'd')
<<<<<<< HEAD
			cursor.state.rightPressed = false;
=======
			cursor.state.rightPressed = false;
		else if (p.keyCode == CONTROL)
			chain = false;
>>>>>>> graphics
	}

	public void mousePressed() {
<<<<<<< HEAD
		//Vec2 pos = new Vec2((p.mouseX+r.cam.zeroX)/r.cam.meterScale, 
		//		(p.mouseY+r.cam.zeroY)/r.cam.meterScale);
		//r.stage.addActor(Actor.class, new Vec2(1,1), pos);
		
=======
		if (p.mouseButton == CENTER) {
			Vec2 pos = r.cam.screenToWorld(new Vec2(p.mouseX, p.mouseY));
			r.stage.addActor(Actor.class, new Vec2(1,1), pos);
		} else if (p.mouseButton == LEFT) {
			selecting = r.cam.screenToWorld(new Vec2(p.mouseX, p.mouseY));
		}
>>>>>>> graphics
	}

	public void mouseReleased() {
		if (p.mouseButton == LEFT && selecting != null) {
			//Select objects
			if (!chain) {
				unselect();
			}
			
			Vec2 lower = selecting;
			Vec2 upper = r.cam.screenToWorld(new Vec2(p.mouseX, p.mouseY));
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
		} else if (p.mouseButton == RIGHT && selected.size() > 0) {
			Vec2 to = r.cam.screenToWorld(new Vec2(p.mouseX,p.mouseY));
			for (Actor a : selected) {
				Vec2 from = a.b.getWorldCenter();
				Vec2 imp = to.sub(from);
				imp.normalize();
				imp.mulLocal(50);
				a.b.applyLinearImpulse(imp, from);
			}
			unselect();
		}
	}
	*/
	void unselect() {
		for (Actor a : selected) {
			a.isHeld = false;
		}
		selected.clear();
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {}

	@Override
	public void render(GameContainer gc, StateBasedGame sg, Graphics g)
			throws SlickException {
		g.setBackground(r.skin.getColor("bg"));
		r.cam.set(cursor.b.getWorldCenter(), cursor.b.getAngle());
		for (Actor a:r.stage.activeActors) {
			r.skin.draw(g, a);
		}
		
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}

}
