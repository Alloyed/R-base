package client;

import physics.Actor;
import processing.core.PImage;
import processing.core.PShape;

/* lets you do/draws vectors or images
 * TODO: Find some way to convert PShapes to box2d shapes
 */
public class Sprite {
	Runner p;
	String file;
	boolean isVector = false;
	Object sprite;
	public Sprite(Runner p, String file) {
		this.p = p;
		this.file = file;
		if (file.endsWith(".svg"))
			isVector = true;
		if (isVector)
			sprite = p.loadShape(file);
		else
			sprite = p.loadImage(file);
	}
	
	/* Draws the specified Actor a. See Actor for the variables you can use to customize this. 
	 */
	@SuppressWarnings("static-access")
	public void draw(Actor a) {
		float x = a.b.getWorldCenter().x, y = a.b.getWorldCenter().y;
		p.pushStyle();
		p.pushMatrix(); {
			p.noStroke();
			p.imageMode(p.CENTER);
			p.shapeMode(p.CENTER);
			p.rectMode(p.CENTER);
			p.translate(x * p.meterScale, y * p.meterScale);
			p.scale(p.scale*a.sizeW,p.scale*a.sizeH);
			p.rotate(a.b.getAngle());
			p.noFill();
			p.stroke(0xff,0xff,0x00);
			if (a.isHeld)
				p.tint(p.color(150));
			if (isVector)
				p.shape((PShape)sprite,0,0);
			else
				p.image((PImage)sprite,0,0);
		}
		p.popStyle();
		p.popMatrix();
		p.fill(255);
		if (a.isImportant)
			p.text(a.label,(x * p.meterScale),((y+a.sizeH) * p.meterScale));
	}
}