package client;

import org.jbox2d.common.Vec2;

import processing.core.PApplet;

public class Camera {
	public float scale = 1;
	public float meterScale = 64; //1 m = meterScale pixels
	public float zeroX;
	public float zeroY;
	float camAngle;
	PApplet p;
		 
	public Camera(PApplet p) {
		this.p = p;
		scale = p.width < p.height ? p.width / 1680f : p.height / 1050f;
		meterScale *= scale;
	}
	
	public void translate(float x, float y) {
		p.translate((x * meterScale) - zeroX,(y * meterScale) - zeroY);
	}
	
	public void scale(float x, float y) {
		p.scale(scale * x,scale * y); //wat
	}
	
	//Note: does not work
	public void rotate(float theta) {
		p.rotate(theta);
	}
		
	//Moves pos to center
	public void set(Vec2 pos, float ang) {
		zeroX = 1f * ((pos.x * meterScale) - (p.width / 2f)) + 0f * zeroX;
		zeroY = 1f * ((pos.y * meterScale) - (p.height / 2f)) + 0f * zeroY;
		camAngle = ang;
	}
	
	public Vec2 screenToWorld(Vec2 in) {
		return new Vec2((in.x-zeroX)/meterScale,(in.y-zeroY)/meterScale);
	}
	
	public Vec2 worldToScreen(Vec2 in) {
		return new Vec2(in.x*meterScale-zeroX,in.y*meterScale-zeroY);
	}
}
