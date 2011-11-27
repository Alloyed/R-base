package client;

import org.jbox2d.common.Vec2;

import physics.Actor;
import physics.Console;
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
	final float width, height, length;
	
	public Sprite(Runner p, String file) {
		this.p = p;
		this.file = file;
		if (file.endsWith(".svg"))
			isVector = true;
		if (isVector) {
			PShape shape = p.loadShape(file);
			width = shape.width/64f;
			height = shape.height/64f;
			length = new Vec2(shape.width,shape.height).length();
			sprite = shape;
		} else {
			PImage image = p.loadImage(file);
			width = image.width/64f;
			height = image.height/64f;
			length = new Vec2(image.width,image.height).length();
			sprite = image;
		}
	}
	
	/* Draws the specified Actor a. 
	 * See Actor for the variables you can use to customize this. 
	 */
	@SuppressWarnings("static-access")
	public void draw(Actor a) {
		//new*alpha + old * ( 1.0 - alpha );
		if (a.b == null)
			Console.out.println("WHAT " + a.toString());
		Vec2 pos = a.b.getWorldCenter().mul(Actor.alpha) .add( a.oldPos.mul(1-Actor.alpha) );
		p.pushStyle();
		p.pushMatrix(); {
			p.noStroke();
			p.imageMode(p.CENTER);
			p.shapeMode(p.CENTER);
			p.rectMode(p.CENTER);
			p.camtranslate(pos.x, pos.y);
			p.camScale(a.sizeW * width, a.sizeH * height);
			p.camrotate(a.b.getAngle());
			
			if (isVector)
				p.shape((PShape)sprite,0,0);
			else
				p.image((PImage)sprite,0,0);
			if (a.isHeld) {
				p.fill(150,120,70,100);
				p.ellipse(0,0,length,length);
			}
		}
		p.popStyle();
		p.popMatrix();
		
		p.fill(255);
		if (a.isImportant) {
			Vec2 spot = p.worldToScreen(pos.add(new Vec2(-a.sizeW/2,a.sizeH)));
			p.text(a.label, spot.x, spot.y);
		}
	}
}