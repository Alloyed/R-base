package client.sprites;

import org.jbox2d.common.Vec2;

import client.Client;

import physics.actors.Actor;
import physics.actors.Prop;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;

/* draws vectors or images. 
 * Vectors are rendered as rasters for performance reasons
 * TODO: Render svgs at different resolutions, 
 * 		allow for straight vector rendering again
 */
public class ImageSprite implements Sprite, PConstants {
	Client c;
	PApplet p;
	String file;
	boolean isVector = false;
	public PImage sprite;
	final float width, height, length;
	public ImageSprite(Client c, String file) {
		this.c = c;
		this.p = c.p;
		this.file = file;
		if (file.endsWith(".svg") || file.endsWith(".svgz")) {
			isVector = true;
			PShape shape = p.loadShape(file);
			width = shape.width/64f;
			height = shape.height/64f;
			length = new Vec2(shape.width,shape.height).length();
			//TODO: Find some way to differentiate hitbox size and drawn size
			//Images are being clipped as of right now
			PGraphics pg = p.createGraphics((int)(shape.width*1f), (int)(shape.height*1f), PConstants.JAVA2D); 
			pg.beginDraw();
				pg.smooth();
				pg.shape(shape, 0, 0);
			pg.endDraw();
			sprite = pg;
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
			pg.image(sprite,x,y);
			pg.popMatrix();
	}
	

	public void draw(Actor a) {
		//This does linear interpolation: new*alpha + old*( 1.0 - alpha );
		Vec2 pos = a.b.getWorldCenter().mul(Actor.alpha)
					.add(a.oldPos.mul(1 - Actor.alpha) );
		float angle = a.b.getAngle()*Actor.alpha + 
						a.oldAng * (1 - Actor.alpha);
		p.pushStyle();
		p.pushMatrix(); {
			p.noStroke();
			p.imageMode(CENTER);
			p.shapeMode(CENTER);
			p.rectMode(CENTER);
			p.ellipseMode(CENTER);
			
			c.cam.translate(pos.x, pos.y);
			c.cam.rotate(angle);
			c.cam.scale(a.size.x / width, a.size.y / height);
			p.image(sprite,0,0);
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
	
	public void draw(Prop a) {
		//This should never move
		Vec2 pos = a.pos;
		float angle = a.angle;
		p.pushStyle();
		p.pushMatrix(); {
			p.noStroke();
			p.imageMode(CENTER);
			p.shapeMode(CENTER);
			p.rectMode(CENTER);
			p.ellipseMode(CENTER);
			
			c.cam.translate(pos.x, pos.y);
			c.cam.rotate(angle);
			c.cam.scale(a.width / width, a.height / height);
			p.image(sprite,0,0);
		}
		p.popStyle();
		p.popMatrix();
			
		p.fill(255);
	}
}