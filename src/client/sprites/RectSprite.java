package client.sprites;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import client.ui.Loop;

import physics.Props.Prop;
import physics.actors.Actor;

public class RectSprite implements Sprite {
	Loop c;
	Color color;
	final float width = 1, height = 1, length;

	public RectSprite(Loop l, Color color) {
		this.c = l;
//		p = c.p;
		this.color = color;
		length = (float) Math.sqrt(2);
	}

	public void draw(Graphics pg, float x, float y, float max) {
		pg.pushTransform();
		pg.scale(.3f, .3f); // TODO: unhardcode this
//		pg.noStroke();
//		pg.fill(color);
		pg.setColor(color);
		pg.fillRect(x, y, 64, 64);
		pg.popTransform();
	}

	public void draw(Graphics g, Actor a) {
		
		// This does linear interpolation: new*alpha + old*( 1.0 - alpha );
		Vec2 pos = a.b.getWorldCenter().mul(Actor.alpha)
				.add(a.oldPos.mul(1 - Actor.alpha));
		float angle = a.b.getAngle() * Actor.alpha + a.oldAng
				* (1 - Actor.alpha);
		g.pushTransform();
		{
			c.cam.translate(g, pos.x, pos.y);
			c.cam.scale(g, a.size.x * width, a.size.y * height);
			c.cam.rotate(g, angle);
			g.setColor(color);
			g.fillRect(-32, -32, 64, 64);
//			if (a.isHeld) {
//				p.fill(150, 120, 70, 100);
//				p.ellipse(0, 0, length, length);
//			}
		}
		g.popTransform();

//		p.fill(255);
//		if (a.isImportant) {
//			Vec2 spot = c.cam.worldToScreen(pos.add(new Vec2(-a.size.x / 2,
//					a.size.y)));
//			p.text(a.label, spot.x, spot.y);
//		}
	}
	@Override
	public void draw(Graphics g, Prop pr) {
		
		Vec2 pos = pr.pos;
		float angle = pr.angle;
		g.pushTransform();
		g.resetTransform();
		{
			c.cam.translate(g, pos.x, pos.y);
			c.cam.rotate(g, angle);
			c.cam.scale(g, pr.width / width, pr.height / height);
			
			g.setColor(color);
			g.fillRect(-32, -32, 64, 64);
//			if (a.isHeld) {
//				p.fill(150, 120, 70, 100);
//				p.ellipse(0, 0, length, length);
//			}
		}
		g.popTransform();
	}
	public void draw(Vec2 pos, float ang) {
		
	}
}
