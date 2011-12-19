package client.sprites;

import org.jbox2d.common.Vec2;

import client.Client;

import physics.actors.Actor;
import physics.actors.Prop;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class RectSprite implements Sprite, PConstants {
	Client c;
	PApplet p;
	int color;
	final float width = 1, height = 1, length;

	public RectSprite(Client c, int color) {
		this.c = c;
		p = c.p;
		this.color = color;
		length = (float) Math.sqrt(2);
	}

	public void draw(PGraphics pg, float x, float y, float max) {
		pg.pushMatrix();
		pg.scale(.3f); // TODO: unhardcode this
		pg.noStroke();
		pg.fill(color);
		pg.rect(0, 0, 64, 64);
		pg.popMatrix();
	}

	public void draw(Actor a) {
		// This does linear interpolation: new*alpha + old*( 1.0 - alpha );
		Vec2 pos = a.b.getWorldCenter().mul((float)Actor.alpha)
				.add(a.oldPos.mul(1 - (float)Actor.alpha));
		float angle = a.b.getAngle() * (float)Actor.alpha + a.oldAng
				* (float)(1 - Actor.alpha);
		p.pushStyle();
		p.pushMatrix();
		{
			p.noStroke();
			p.fill(color);
			p.imageMode(CENTER);
			p.shapeMode(CENTER);
			p.rectMode(CENTER);
			p.ellipseMode(CENTER);

			c.cam.translate(pos.x, pos.y);
			c.cam.scale(a.size.x * width, a.size.y * height);
			c.cam.rotate(angle);

			p.rect(0, 0, 64, 64);
			if (a.isHeld) {
				p.fill(150, 120, 70, 100);
				p.ellipse(0, 0, length, length);
			}
		}
		p.popStyle();
		p.popMatrix();

		p.fill(255);
		if (a.isImportant) {
			Vec2 spot = c.cam.worldToScreen(pos.add(new Vec2(-a.size.x / 2,
					a.size.y)));
			p.text(a.label, spot.x, spot.y);
		}
	}

	@Override
	public void draw(Prop pr) {
		Vec2 pos = pr.pos;
		float angle = pr.angle;
		p.pushStyle();
		p.pushMatrix();
		{
			p.noStroke();
			p.fill(color);
			p.imageMode(CENTER);
			p.shapeMode(CENTER);
			p.rectMode(CENTER);
			p.ellipseMode(CENTER);
			
			c.cam.translate(pos.x, pos.y);
			c.cam.rotate(angle);
			c.cam.scale(pr.width * width, pr.height * height);
			

			p.rect(0, 0, 64, 64);
		}
		p.popStyle();
		p.popMatrix();

		p.fill(255);
	}
}
