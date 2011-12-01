package client.sprites;

import org.jbox2d.common.Vec2;

import client.Main;

import physics.actors.Actor;
import processing.core.PImage;
import processing.core.PShape;

/* draws vectors or images */
public class ImageSprite implements Sprite {
	Main p;
	String file;
	boolean isVector = false;
	Object sprite;
	final float width, height, length;
	public ImageSprite(Main p, String file) {
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
		//This does linear interpolation: new*alpha + old*( 1.0 - alpha );
		Vec2 pos = a.b.getWorldCenter().mul(Actor.alpha) .add( a.oldPos.mul(1-Actor.alpha) );
		p.pushStyle();
		p.pushMatrix(); {
			p.noStroke();
			p.imageMode(p.CENTER);
			p.shapeMode(p.CENTER);
			p.rectMode(p.CENTER);
			p.cam.translate(pos.x, pos.y);
			p.cam.scale(a.sizeW * width, a.sizeH * height);
			p.cam.rotate(a.b.getAngle());
			
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
			Vec2 spot = p.cam.worldToScreen(pos.add(new Vec2(-a.sizeW/2,a.sizeH)));
			p.text(a.label, spot.x, spot.y);
		}
	}
}