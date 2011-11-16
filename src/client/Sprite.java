package client;

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
	
	@SuppressWarnings("static-access")
	public void draw(float x, float y, float ang, float size) {
		p.pushStyle();
		p.pushMatrix(); {
			p.noStroke();
			p.imageMode(p.CENTER);
			p.shapeMode(p.CENTER);
			p.translate(x * p.meterScale, y * p.meterScale);
			p.scale(p.scale*size);
			p.rotate(ang);
			p.fill(255);
			p.rect(0,0,32,32);
			if (isVector)
				p.shape((PShape)sprite,0,0);
			else
				p.image((PImage)sprite,0,0);
		}
		p.popStyle();
		p.popMatrix();
	}
}
