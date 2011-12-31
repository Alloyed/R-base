package client;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import physics.Console;

//import processing.core.PApplet;

public class Camera {
	public float scale = 1;
	public float meterScale = 64; //1 m = meterScale pixels
	public float zeroX;
	public float zeroY;
	float camAngle;
	GameContainer p;
	float x, y;
	public Camera(GameContainer p) {
		this.p = p;
		scale = p.getWidth() < p.getHeight() ? p.getWidth() / 1680f : p.getHeight() / 1050f;
		meterScale *= scale;
		Console.dbg.println(p.getHeight() +"  "+meterScale);
	}
	
	public void translate(Graphics g, float x, float y) {
		this.x = (x * meterScale) - zeroX;
		this.y = (y * meterScale) - zeroY;
		g.translate(this.x,this.y);
	}
	
	public void scale(Graphics g, float x, float y) {
		g.scale(scale * x,scale * y); //wat
	}
	
	public void rotate(Graphics g, float theta) {
		g.rotate(0, 0,(float) Math.toDegrees(theta));
	}
		
	//Moves pos to center
	public void set(Vec2 pos, float ang) {
		zeroX = 1f * ((pos.x * meterScale) - (p.getWidth() / 2f)) + 0f * zeroX;
		zeroY = 1f * ((pos.y * meterScale) - (p.getHeight() / 2f)) + 0f * zeroY;
		camAngle = ang;
	}
	
	public Vec2 screenToWorld(Vec2 in) {
		return new Vec2((in.x+zeroX)/meterScale,(in.y+zeroY)/meterScale);
	}
	
	public Vec2 worldToScreen(Vec2 in) {
		return new Vec2(in.x*meterScale-zeroX,in.y*meterScale-zeroY);
	}
}
