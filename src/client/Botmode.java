package client;

import physics.Actor;
import physics.Player;

public class Botmode extends UI{
	Player pc;
	
	public Botmode(Runner r) {
		super(r);
		pc = new Player(r.stage);
	}
	
	@Override
	public void draw() {
		pc.state.aim.x = r.mouseX/r.meterScale;
		pc.state.aim.y = r.mouseY/r.meterScale;
		
		r.background(20);
		for (Actor a:r.stage.actors) {
			r.draw(a);
		}
		
		//crosshairs
		r.pushMatrix();
			r.noFill();
			r.stroke(255);
			r.strokeWeight(2);
			r.translate(pc.state.aim.x*r.meterScale,pc.state.aim.y*r.meterScale);
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
		else if (r.key == r.ESC)
			r.menu.show();
		return; //Don't go to the Gooey
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
