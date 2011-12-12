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

/*One Giant TODO*/
public class Godmode  extends BasicGameState {
	public Ghost cursor;
	public Loop r;
	
	public Godmode(Loop l) {
		super();
		r = l;
		//r.gooey.addGroup(group, 0, 0);
		cursor = (Ghost) r.stage.addActor(Ghost.class, 0, Team.get(r.settings.team), new Vec2(1,1), new Vec2(1,1));
	}

	public void keyPressed() {
	/*	if (p.key == 'w')
			cursor.state.upPressed = true;
		else if (p.key == 'a')
			cursor.state.leftPressed = true;
		else if (p.key == 's')
			cursor.state.downPressed = true;
		else if (p.key == 'd')
			cursor.state.rightPressed = true;
		else if (p.key == PConstants.ESC)
			r.menu.show();
	*/}

	public void keyReleased() {
	/*	if (p.key == 'w')
			cursor.state.upPressed = false;
		else if (p.key == 'a')
			cursor.state.leftPressed = false;
		else if (p.key == 's')
			cursor.state.downPressed = false;
		else if (p.key == 'd')
		*/	cursor.state.rightPressed = false;
	}

	public void mousePressed() {
		//Vec2 pos = new Vec2((p.mouseX+r.cam.zeroX)/r.cam.meterScale, 
		//		(p.mouseY+r.cam.zeroY)/r.cam.meterScale);
		//r.stage.addActor(Actor.class, new Vec2(1,1), pos);
		
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
