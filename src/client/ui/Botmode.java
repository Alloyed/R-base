package client.ui;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import TWLSlick.BasicTWLGameState;

import physics.Console;
import physics.Team;
import physics.actors.*;

public class Botmode  extends BasicTWLGameState {
	public Robot pc;
	Loop l;
//	public PGraphics inv, health;
	private Vec2 lerped;
	
	final static int id = 2;
	@Override
	public int getID() {
		return id;
	}
	
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
		this.l = l;
		start();
//		ControllerGroup m = r.gooey.addGroup(group, 0, 0);
		//this sets off a segfault, it's harmless though
		/*
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
		*/
	}
	
	public void start() {
		pc = (Robot)l.stage.addActor(Robot.class, 0, 
				l.settings.team == 0 ? Team.ORANGE : Team.BLUE,
				new Vec2(1, 1), new Vec2(1,1));
		lerped = new Vec2(0,0);
		Console.chat.println("\\You are a robot. Kill the other team!");
	}
	
	/*
	public void draw() {		
		if (pc.isDead()) { 
			r.ghostmode.start(pc.oldPos);
			r.ghostmode.show();
		}
		if (r.joypad == null) {
			pc.state.aim = r.cam.screenToWorld(new Vec2(p.mouseX, p.mouseY)).sub(pc.b.getPosition());
		} else {
			ControllStick s = r.joypad.getStick(0);  //Left stick
			ControllStick s1 = r.joypad.getStick(1); //Right stick + Left trigger
			ControllStick s2 = r.joypad.getStick(2); //Right trigger + Right stick
			pc.state.move = new Vec2(s.getY(), s.getX());
			if (pc.state.move.length() < .2f)
				pc.state.move.set(0,0);
			Vec2 newAim = new Vec2(s1.getX(), s2.getY());
			if (newAim.length()  > .1f)
				pc.state.aim = newAim;
		}
		
		p.background(r.menu.bg);
		r.cam.set(pc.b.getWorldCenter(), pc.b.getAngle());
		
		for (Actor a:r.stage.activeActors) {
			r.draw(a);
		}
		
		//crosshairs
		Vec2 aim = pc.state.aim.add(pc.b.getPosition());
		p.pushMatrix();
			p.noSmooth();
			p.noFill();
			p.stroke(255);
			p.strokeWeight(2);
			r.cam.translate(aim.x, aim.y);
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
	*/
	/*
	public void show() {
		pc.state.ROTATE_FORCE = r.settings.ROTATE_FORCE;
		pc.label = r.settings.USERNAME;
		//p.noCursor();
	}
	*/
		
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sg, Graphics g)
			throws SlickException {
		
		if (pc.isDead()) { 
			sg.enterState(Ghostmode.id);
		}

		g.setBackground(l.skin.getColor("bg"));
		l.cam.set(pc.b.getWorldCenter(), pc.b.getAngle());
		for (Actor a:l.stage.activeActors) {
			l.skin.draw(g, a);
		}
		
		Vec2 aim = lerped.add(pc.b.getPosition());
		//crosshairs
		g.pushTransform();
			g.setColor(Color.white);
			l.cam.translate(g, aim.x,aim.y);
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
		l.update(dt);
		Input input = gc.getInput();
		int mouseX = input.getAbsoluteMouseX();
		int mouseY = input.getAbsoluteMouseY();
		Vec2 oldAim = pc.state.aim;
		pc.state.aim = l.cam.screenToWorld(new Vec2(mouseX, mouseY)).sub(pc.b.getPosition());
		lerped = oldAim.mul(Actor.alpha).add(pc.state.aim.mul(1-Actor.alpha));
		//lerped = pc.state.aim;
		keys(gc, input);
	}
	
	boolean firePressed = false;
	public void keys(GameContainer gc, Input in) {
		pc.state.upPressed = in.isKeyDown(l.settings.UP);
		pc.state.leftPressed = in.isKeyDown(l.settings.LEFT);
		pc.state.rightPressed = in.isKeyDown(l.settings.RIGHT);
		pc.state.downPressed = in.isKeyDown(l.settings.DOWN);
		if (in.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && firePressed == false) {
			pc.fire();
			firePressed = true;
		} else if (firePressed && !in.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
			firePressed = false;
		}
		
		if (in.isKeyDown(Input.KEY_Q)) {
			gc.exit();
		}
	}
}
