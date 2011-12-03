package client.ui;

import java.io.IOException;

import org.jbox2d.common.Vec2;

import processing.core.*;
import controlP5.*;

import client.Client;
import client.sprites.Sprite;

import physics.Console;
import physics.actors.*;

public class Botmode extends UI{
	public Robot pc;
	public PGraphics inv;
	
	public void inv() {
		//TODO
		Console.dbg.println("INVENTORIZED");
	}
	
	public Botmode(Client r) {
		super(r);
		pc = (Robot) r.stage.addActor(Robot.class, new Vec2(1, 1), new Vec2(1,1));
		
		group = "botmode";
		ControllerGroup m = r.gooey.addGroup(group, 0, 0);
		//this sets off a segfault, it's harmless though
		inv = r.createGraphics(100, 30, PConstants.JAVA2D); 
		Button b = r.gooey.addButton("inv",0,0,r.height-30,100,30);
		b.setImage(inv); 
		b.moveTo(m);
	}
	
	@Override
	public void draw() {
		Vec2 oldAim = pc.state.aim;
		pc.state.aim = new Vec2((r.mouseX+r.cam.zeroX)/r.cam.meterScale, 
				(r.mouseY+r.cam.zeroY)/r.cam.meterScale);
		Vec2 lerped = oldAim.mul(Actor.alpha).add(pc.state.aim.mul(1-Actor.alpha));
		r.background(20);
		r.cam.set(pc.b.getWorldCenter(), pc.b.getAngle());
		for (Actor a:r.stage.activeActors) {
			r.draw(a);
		}
		
		if(r.net != null )
			try {
				r.net.sendEvent(pc.state);
			} catch (IOException e) {
				
			}
		
		//crosshairs
		r.pushMatrix();
			r.noFill();
			r.stroke(255);
			r.strokeWeight(2);
			r.cam.translate(lerped.x,lerped.y);
			r.rect(-2, -2, 4, 4);
		r.popMatrix();
		
		//Button.
		inv.beginDraw();
			
			inv.smooth();
			inv.background(100);
			Sprite s;
			if (!pc.inventory.isEmpty()) {
				Actor a = pc.inventory.get(0);
				s = r.skin.get(a);
			} else {
				s = r.skin.sprites.get("none");
			}
			s.draw(inv,15,15,50);
			inv.fill(255);
			inv.text(""+pc.inventory.size(),70,20);
		inv.endDraw();
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
		else if (r.key == 'q')
			inv();
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
		/*
		 * TODO: keep from firing when moused over the UI.
		 * There is a function to do this in the ControlP5 docs.
		 * It does not work with groups or tabs.
		 * SDFSDFSDFASDFAJFGGASDJSDF
		 */
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
	
	public void hide() {
		super.hide();
	}
}
