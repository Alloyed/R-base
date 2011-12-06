package client.ui;

import org.jbox2d.common.Vec2;

import physics.actors.Actor;
import physics.actors.Ghost;
import processing.core.PConstants;
import client.Client;

/*this is Observer mode, the mode you should start as.*/
public class Ghostmode extends UI {
	public Ghost cursor;
	
	public Ghostmode(Client r, Vec2 pos) {
		super(r);
		group = "ghostmode";
		cursor = (Ghost) r.stage.addActor(Ghost.class, pos, new Vec2(1,1));
		r.gooey.addGroup(group, 0, 0);
	}

	@Override
	public void draw() {
		p.background(20);
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
