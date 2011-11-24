package client;

import org.jbox2d.common.Vec2;

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
	final float width, height;
	
	public Sprite(Runner p, String file) {
		this.p = p;
		this.file = file;
		if (file.endsWith(".svg"))
			isVector = true;
		if (isVector) {
			PShape shape = p.loadShape(file);
			width = shape.width/64f;
			height = shape.height/64f;
			sprite = shape;
		} else {
			PImage image = p.loadImage(file);
			width = image.width/64f;
			height = image.height/64f;
			sprite = image;
		}
	}
	
	/* Draws the specified Actor a. 
	 * See Actor for the variables you can use to customize this. 
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
			p.camtranslate(x, y);
			p.camScale(a.sizeW * width, a.sizeH * height);
			p.camrotate(a.b.getAngle());
			
			p.noFill();
			p.stroke(0xff,0xff,0x00);
			if (a.isHeld)
				p.tint(p.color(150,150,255));
			if (isVector)
				p.shape((PShape)sprite,0,0);
			else
				p.image((PImage)sprite,0,0);
		}
		p.popStyle();
		
		p.fill(255);
		if (a.isImportant) {
			Vec2 spot = p.worldToScreen(a.b.getWorldCenter().
											add(new Vec2(-a.sizeW/2,a.sizeH)));
			p.text(a.label, spot.x, spot.y);
		}
		p.popMatrix();
	}
}