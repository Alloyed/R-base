package client.ui;



import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import physics.Console;
import physics.Team;
import physics.actors.Actor;
import physics.actors.Robot;
import TWLSlick.BasicTWLGameState;

/**
 * This is robot mode, for players who are robots. 
 * Beep. Beep. Beep.
 * 
 * @author kyle
 *
 */
public class Botmode extends BasicTWLGameState {
	public Robot pc;
	Loop loop;
	
//	public PGraphics inv, health;
//	private Vec2 lerped;
	
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
		this.loop = l;
	}
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
	
	public void start() {
		pc = new Robot();
		pc.create(new Vec2(1, 1), new Vec2(1, 1));
		pc.setTeam(loop.settings.team == 0 ? Team.ORANGE : Team.BLUE);
		pc.place(loop.stage);
//		lerped = new Vec2(0,0);
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
	public void init(GameContainer gc, StateBasedGame sg)
			throws SlickException {
		
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sg) 
			throws SlickException {
		super.enter(gc, sg);
		start();
		//gc.getInput().addKeyListener(this);
	}
	
	@Override
	public void leave(GameContainer gc, StateBasedGame sg) 
			throws SlickException {
		super.leave(gc, sg);
		pc = null; 
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sg, Graphics g)
			throws SlickException {
		loop.cam.set(pc.b.getWorldCenter(), pc.b.getAngle());
		
		if (pc.wear <= 1) {
			sg.enterState(Ghostmode.id);
		}
		
		//TODO:Find some nicer way to trigger sounds
		for (Actor a:loop.stage.activeActors) {
			if (a.hit) {
				a.hit = false;
				loop.skin.playAt("hit", pc.b.getLocalPoint(a.b.getWorldCenter()));
			}
		}
		
		loop.render(gc, sg, g);
		
		//crosshairs
		//FIXME: This is broken still
		/*
		Vec2 aim = lerped.add(pc.b.getWorldCenter());
		g.pushTransform();
			g.setColor(Color.white);
			l.cam.translate(g, aim.x,aim.y);
			g.drawRect(-2, -2, 4, 4);
		g.popTransform();
		*/
		
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
		Input input = gc.getInput();
		int mouseX = input.getAbsoluteMouseX();
		int mouseY = input.getAbsoluteMouseY();
//		Vec2 oldAim = pc.state.aim;
		pc.ps.aim = loop.cam.screenToWorld(new Vec2(mouseX, mouseY)).sub(pc.b.getPosition());
//		lerped = oldAim.mul(1-Actor.alpha).add(pc.state.aim.mul(Actor.alpha));
//		lerped = pc.state.aim;
		keys(gc, input);
		loop.update(dt);
	}
	
	@Override
	public void keyPressed(int code, char c) {
		loop.keyPressed(code, c);
	}

	@Override
	public void keyReleased(int code, char c) {
		loop.keyReleased(code, c);
		if (code == loop.settings.USE) {
			pc.toggleHold();
		}
		if (code == loop.settings.CHAT) {
			cb.startChat();
		}
	}
	
	@Override
	public void mousePressed(int btn, int x, int y) {
		if (btn == Input.MOUSE_LEFT_BUTTON)
			pc.fire();
	}
	
	public void keys(GameContainer gc, Input in) {
		//TODO: make back into events
		pc.ps.upPressed    = in.isKeyDown(loop.settings.UP);
		pc.ps.leftPressed  = in.isKeyDown(loop.settings.LEFT);
		pc.ps.rightPressed = in.isKeyDown(loop.settings.RIGHT);
		pc.ps.downPressed  = in.isKeyDown(loop.settings.DOWN);		
	}
	
	ChatBox cb;
	@Override
	protected void createRootPane() {
		super.createRootPane();
		cb = new ChatBox(loop);
		cb.add(rootPane);
	}
	
    @Override
    protected void layoutRootPane() {
    	cb.layout(rootPane);
    }
}
