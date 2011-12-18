package client.ui;

import org.jbox2d.common.Vec2;

import processing.core.*;
import controlP5.*;

import client.Client;
import client.sprites.Sprite;

import physics.Console;
import physics.Team;
import physics.actors.*;

public class Botmode extends UI{
	public Robot pc;
	public PGraphics inv, health;
	
	public void inv() {
		//TODO: make boolet from scrap metal
		Console.dbg.println("INVENTORIZED");
	}
	
	public void health() {
		//TODO: make health from scrap metal
		Console.dbg.println("HEALTH AT "+ pc.wear + "/" + pc.maxWear);
	}
	
	public Botmode(Client r) {
		super(r);
		
		group = "botmode";
		ControllerGroup m = r.gooey.addGroup(group, 0, 0);
		//this sets off a segfault, it's harmless though
		inv = p.createGraphics(100, 30, PConstants.JAVA2D);
		inv.beginDraw();
		inv.background(0);
		inv.endDraw();
		Button b = r.gooey.addButton("inv",0,0,p.height-30,100,30);
		b.setImage(inv);
		b.moveTo(m);
		
		health = p.createGraphics(100, 30, PConstants.JAVA2D);
		health.beginDraw();
		health.background(0);
		health.endDraw();
		b = r.gooey.addButton("health",0,p.width-100,p.height-30,100,30);
		b.setImage(health);
		b.moveTo(m);
		hide();
	}
	
	public void start() {
		pc = (Robot)r.stage.addActor(Robot.class, 0, 
				r.settings.team == 0 ? Team.ORANGE : Team.BLUE,
				new Vec2(1, 1), new Vec2(1,1));
	}
	
	@Override
	public void draw() {
		
		if (pc.isDead()) { 
			r.ghostmode.start(pc.oldPos);
			r.ghostmode.show();
		}
		
		Vec2 oldAim = pc.state.aim;
		pc.state.aim = r.cam.screenToWorld(new Vec2(p.mouseX, p.mouseY));
		Vec2 lerped = oldAim.mul(1-Actor.alpha).add(pc.state.aim.mul(Actor.alpha));
		//Optimization, Valve Style!
		p.background(r.menu.bg);
		r.cam.set(pc.b.getWorldCenter(), pc.b.getAngle());
		for (Actor a:r.stage.activeActors) {
			r.draw(a);
		}
		
		//crosshairs
		p.pushMatrix();
			p.noSmooth();
			p.noFill();
			p.stroke(255);
			p.strokeWeight(2);
			r.cam.translate(lerped.x,lerped.y);
			p.rect(-2, -2, 2, 2);
			p.smooth();
		p.popMatrix();
		
		//Button.
		inv.beginDraw();
			
			inv.smooth();
			inv.background(100);
			Sprite s;
			if (!pc.inventory.isEmpty()) {
				Actor a = pc.inventory.get(0);
				s = r.skin.get(a);
			} else {
				s = r.skin.get("none");
			}
			s.draw(inv,15,15,50);
			inv.fill(255);
			inv.text(""+pc.inventory.size(),70,20);
		inv.endDraw();
		//Button 2.0
		health.beginDraw();
			health.smooth();
			health.background(100);
			health.fill(255);
			health.text((int)pc.wear+"/"+(int)pc.maxWear,10,20);
		health.endDraw();
		
	}
	
	@Override
	public void keyPressed() {
		if (p.keyCode == r.settings.UP)
			pc.state.upPressed = true;
		else if (p.keyCode == r.settings.LEFT)
			pc.state.leftPressed = true;
		else if (p.keyCode == r.settings.DOWN)
			pc.state.downPressed = true;
		else if (p.keyCode == r.settings.RIGHT)
			pc.state.rightPressed = true;
		else if (p.key == PConstants.ESC)
			r.menu.show();
		else if (p.key == 'q')
			inv();
	}
	
	@Override
	public void keyReleased() {
		if (p.keyCode == r.settings.USE)
			pc.toggleHold();
		if (p.keyCode == r.settings.UP)
			pc.state.upPressed = false;
		else if (p.keyCode == r.settings.LEFT)
			pc.state.leftPressed = false;
		else if (p.keyCode == r.settings.DOWN)
			pc.state.downPressed = false;
		else if (p.keyCode == r.settings.RIGHT)
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
		p.noCursor();
	}
	
	public void hide() {
		super.hide();
	}
}
