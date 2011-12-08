package client.ui;

import org.jbox2d.common.Vec2;

import physics.actors.Actor;
import physics.actors.Ghost;
import processing.core.PConstants;
import client.Client;

/*One Giant TODO*/
public class Godmode extends UI {
	public Ghost cursor;
	
	public Godmode(Client r) {
		super(r);
		group = "godmode";
		r.gooey.addGroup(group, 0, 0);
		cursor = (Ghost) r.stage.addActor(Ghost.class, new Vec2(1,1), new Vec2(1,1));
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
	public void keyPressed() {
		if (p.key == 'w')
			cursor.state.upPressed = true;
		else if (p.key == 'a')
			cursor.state.leftPressed = true;
		else if (p.key == 's')
			cursor.state.downPressed = true;
		else if (p.key == 'd')
			cursor.state.rightPressed = true;
		else if (p.key == PConstants.ESC)
			r.menu.show();
	}

	@Override
	public void keyReleased() {
		if (p.key == 'w')
			cursor.state.upPressed = false;
		else if (p.key == 'a')
			cursor.state.leftPressed = false;
		else if (p.key == 's')
			cursor.state.downPressed = false;
		else if (p.key == 'd')
			cursor.state.rightPressed = false;
	}

	@Override
	public void mousePressed() {
		Vec2 pos = new Vec2((p.mouseX+r.cam.zeroX)/r.cam.meterScale, 
				(p.mouseY+r.cam.zeroY)/r.cam.meterScale);
		r.stage.addActor(Actor.class, new Vec2(1,1), pos);
		
	}

	@Override
	public void mouseReleased() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		p.cursor();
	}
}
