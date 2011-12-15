package client.sprites;

import org.jbox2d.common.Vec2;

import client.Client;

import physics.actors.Actor;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;

/* draws vectors or images */
public class ImageSprite implements Sprite {
	Client c;
	PApplet p;
	String file;
	boolean isVector = false;
	public Object sprite;
	final float width, height, length;
	public ImageSprite(Client c, String file) {
		this.c = c;
		this.p = c.p;
		this.file = file;
		if (file.endsWith(".svg") || file.endsWith(".svgz"))
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
	

	public void draw(PGraphics pg, float x, float y, float max) {
			pg.pushMatrix();
			pg.scale(.3f); //TODO: unhardcode this
			if (isVector)
				pg.shape((PShape)sprite,x,y);
			else
				pg.image((PImage)sprite,x,y);
			pg.popMatrix();
	}
	

	@SuppressWarnings("static-access")
	public void draw(Actor a) {
		//This does linear interpolation: new*alpha + old*( 1.0 - alpha );
		Vec2 pos = a.b.getWorldCenter().mul(Actor.alpha) .add( a.oldPos.mul(1 - Actor.alpha) );
		float angle = a.b.getAngle()*Actor.alpha + a.oldAng * (1 - Actor.alpha);
		p.pushStyle();
		p.pushMatrix(); {
			p.noStroke();
			p.imageMode(p.CENTER);
			p.shapeMode(p.CENTER);
			p.rectMode(p.CENTER);
			p.ellipseMode(p.CENTER);
			
			c.cam.translate(pos.x, pos.y);
			c.cam.scale(a.size.x * width, a.size.y * height);
			c.cam.rotate(angle);
			if (a.getImage() == "playerBottom") {
			}
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
			Vec2 spot = c.cam.worldToScreen(pos.add(new Vec2(-a.size.x/2,a.size.y)));
			p.text(a.label, spot.x, spot.y);
		}
	}
}