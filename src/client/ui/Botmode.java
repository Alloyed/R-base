package client.ui;

import java.io.IOException;

import org.jbox2d.common.Vec2;

import client.Main;

import physics.actors.Actor;
import physics.actors.Robot;
import processing.core.PConstants;

public class Botmode extends UI{
	public Robot pc;
	
	public Botmode(Main r) {
		super(r);
		group = "botmode";
		pc = (Robot) r.stage.addActor(Robot.class, new Vec2(1, 1), new Vec2(1,1));
		r.gooey.addGroup(group, 0, 0);
	}
	
	@Override
	public void draw() {
		pc.state.aim = new Vec2((r.mouseX+r.cam.zeroX)/r.cam.meterScale, 
				(r.mouseY+r.cam.zeroY)/r.cam.meterScale);
		
		r.background(20);
		r.cam.set(pc.b.getWorldCenter(), pc.b.getAngle());
		for (Actor a:r.stage.actors) {
			r.draw(a);
		}
		
		if(r.client != null )
			try {
				r.client.sendEvent(pc.state);
			} catch (IOException e) {
				
			}
		
		//crosshairs
		r.pushMatrix();
			r.noFill();
			r.stroke(255);
			r.strokeWeight(2);
			r.cam.translate(pc.state.aim.x,pc.state.aim.y);
			r.rect(-2, -2, 4, 4);
		r.popMatrix();
		
	}
	
	@Override
	public void keyPressed() {
		if (r.key == 'w')
			pc.state.upPressed = true;
		else if (r.key == 'a')
			pc.state.leftPressed = true;
		else if (r.key == 's')
			pc.state.downPressed = true;
		else if (r.key == 'd')
			pc.state.rightPressed = true;
		else if (r.key == PConstants.ESC)
			r.menu.show();
	}
	
	@Override
	public void keyReleased() {
		if (r.key == 'e') 
			pc.toggleHold();
		if (r.key == 'w')
			pc.state.upPressed = false;
		else if (r.key == 'a')
			pc.state.leftPressed = false;
		else if (r.key == 's')
			pc.state.downPressed = false;
		else if (r.key == 'd')
			pc.state.rightPressed = false;
		
	}
	
	@Override
	public void mousePressed() {
		pc.fire();
	}
	
	@Override
	public void mouseReleased() {
		
	}
	
	@Override
	public void show() {
		pc.state.ROTATE_FORCE = r.settings.ROTATE_FORCE;
		pc.label = r.settings.USERNAME;
		super.show();
		r.noCursor();
	}
}
