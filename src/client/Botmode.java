package client;

import org.jbox2d.common.Vec2;

import physics.Actor;
import physics.Player;
import processing.core.PConstants;

public class Botmode extends UI{
	Player pc;
	
	public Botmode(Runner r) {
		super(r);
		pc = new Player();
		pc.place(r.stage, new Vec2(1,1));
	}
	
	@Override
	public void draw() {
		pc.state.aim = new Vec2((r.mouseX+r.zeroX)/r.meterScale, 
				(r.mouseY+r.zeroY)/r.meterScale);
		
		r.background(20);
		r.setCam(pc.b.getWorldCenter(), pc.b.getAngle());
		for (Actor a:r.stage.actors) {
			r.draw(a);
		}
		
		//crosshairs
		r.pushMatrix();
			r.noFill();
			r.stroke(255);
			r.strokeWeight(2);
			r.camtranslate(pc.state.aim.x,pc.state.aim.y);
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
		r.currentMode.hide();
		pc.state.ROTATE_FORCE = r.settings.ROTATE_FORCE;
		pc.label = r.settings.USERNAME;
		r.noCursor();
		r.gooey.getGroup("botmode").show();
		r.currentMode = this;
	}
	
	@Override
	public void hide() {
		r.gooey.getGroup("botmode").hide();
	}
}
