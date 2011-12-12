package client.ui;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


import client.Client;
import client.sprites.Sprite;

import physics.Console;
import physics.Team;
import physics.actors.*;

public class Botmode  extends BasicGameState {
	public Robot pc;
	Loop r;
//	public PGraphics inv, health;
	private Vec2 lerped;
	
	public void inv() {
		//TODO: make boolet from scrap metal
		Console.dbg.println("INVENTORIZED");
	}
	
	public void health() {
		//TODO: make health from scrap metal
		Console.dbg.println("HEALTH AT "+ pc.wear + "/" + pc.maxWear);
	}
	
	public Botmode(Loop l) {
		super();
		r = l;
		start();
//		ControllerGroup m = r.gooey.addGroup(group, 0, 0);
		//this sets off a segfault, it's harmless though
//		inv = p.createGraphics(100, 30, PConstants.JAVA2D); 
//		Button b = r.gooey.addButton("inv",0,0,p.height-30,100,30);
//		b.setImage(inv);
//		b.moveTo(m);
		
//		health = p.createGraphics(100, 30, PConstants.JAVA2D);
//		b = r.gooey.addButton("health",0,p.width-100,p.height-30,100,30);
//		b.setImage(health);
//		b.moveTo(m);
	}
	
	public void start() {
		pc = (Robot) r.stage.addActor(Robot.class, 0, 
				r.settings.team == 0 ? Team.ORANGE : Team.BLUE, 
				new Vec2(1, 1), new Vec2(1,1));
		lerped = new Vec2(0,0);
	}
	
	public void draw() {
		/*
		
		*/
	}
	
	public void keyPressed() {
		/*
		if (p.key == 'w')
			pc.state.upPressed = true;
		else if (p.key == 'a')
			pc.state.leftPressed = true;
		else if (p.key == 's')
			pc.state.downPressed = true;
		else if (p.key == 'd')
			pc.state.rightPressed = true;
		else if (p.key == PConstants.ESC)
			r.menu.show();
		else if (p.key == 'q')
			inv();
			*/
	}
	
	public void keyReleased() {
		/*
		if (p.key == 'e') 
			pc.toggleHold();
		if (p.key == 'w')
			pc.state.upPressed = false;
		else if (p.key == 'a')
			pc.state.leftPressed = false;
		else if (p.key == 's')
			pc.state.downPressed = false;
		else if (p.key == 'd')
			pc.state.rightPressed = false;
			*/
	}
	
	public void mousePressed() {
		/*
		 * TODO: keep from firing when moused over the UI.
		 * There is a function to do this in the ControlP5 docs.
		 * It does not work with groups or tabs.
		 * SDFSDFSDFASDFAJFGGASDJSDF
		 */
		pc.fire();
	}
	
	public void mouseReleased() {
		
	}
	
	public void show() {
		pc.state.ROTATE_FORCE = r.settings.ROTATE_FORCE;
		pc.label = r.settings.USERNAME;
		//p.noCursor();
	}
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sg, Graphics g)
			throws SlickException {
		/*
		if (pc.isDead()) { 
			r.ghostmode.start(pc.oldPos);
			r.ghostmode.show();
		}
		*/

		g.setBackground(r.skin.getColor("bg"));
		r.cam.set(pc.b.getWorldCenter(), pc.b.getAngle());
		for (Actor a:r.stage.activeActors) {
			r.draw(g, a);
		}
		
		
		//crosshairs
		g.pushTransform();
			g.setColor(Color.white);
			r.cam.translate(g, lerped.x,lerped.y);
			g.drawRect(-2, -2, 4, 4);
		g.popTransform();
		/*
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
		*/
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sg, int dt)
			throws SlickException {
		r.update(dt);
		Input input = gc.getInput();
		int mouseX = input.getAbsoluteMouseX();
		int mouseY = input.getAbsoluteMouseY();
		Vec2 oldAim = pc.state.aim;
		pc.state.aim = new Vec2((mouseX+r.cam.zeroX)/r.cam.meterScale,
				(mouseY+r.cam.zeroY)/r.cam.meterScale);
		lerped = oldAim.mul(Actor.alpha).add(pc.state.aim.mul(1-Actor.alpha));
		keys(input);
	}
	
	public void keys(Input in) {
		pc.state.upPressed = in.isKeyDown(Input.KEY_W);
		pc.state.leftPressed = in.isKeyDown(Input.KEY_A);
		pc.state.rightPressed = in.isKeyDown(Input.KEY_D);
		pc.state.downPressed = in.isKeyDown(Input.KEY_S);
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
