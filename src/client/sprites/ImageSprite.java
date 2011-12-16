package client.sprites;

import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.InkscapeLoader;
import org.newdawn.slick.svg.SimpleDiagramRenderer;
import org.newdawn.slick.util.ResourceLoader;

import client.ui.Loop;

import physics.Console;
import physics.actors.Actor;
/* draws vectors or images */
public class ImageSprite implements Sprite {
	Loop c;
	String file;
	boolean isVector = false;
	public Object sprite;
	float width, height, length;
	public ImageSprite(Loop c, String file) {
		this.c = c;
		this.file = file;
		if (file.endsWith(".svg") || file.endsWith(".svgz"))
			isVector = true;
		try {
		if (isVector) {
			InputStream in = ResourceLoader.getResourceAsStream(file);
			if (file.endsWith(".svgz"))
				in = new GZIPInputStream(in);
			Diagram d = InkscapeLoader.load(in,true);
			width = d.getWidth()/64f;
			height = d.getHeight()/64f;
			length = new Vec2(d.getWidth(),d.getHeight()).length();
			sprite = new SimpleDiagramRenderer(d);
		} else {
			Image image = new Image(file);
			width = image.getWidth()/64f;
			height = image.getHeight()/64f;
			length = new Vec2(image.getWidth(),image.getHeight()).length();
			sprite = image;
		}
		} catch (Exception e) { e.printStackTrace(Console.dbg);}
	}
	

	public void draw(Graphics pg, float x, float y, float max) {
			pg.pushTransform();
			pg.resetTransform();
			pg.translate(x, y);
			pg.scale(.3f, .3f); //TODO: unhardcode this
			if (isVector)
				SimpleDiagramRenderer.render(pg, (Diagram)sprite);
			else
				pg.drawImage((Image)sprite,0,0);
			pg.popTransform();
	}
	

	public void draw(Graphics g, Actor a) {
		//This does linear interpolation: new*alpha + old*( 1.0 - alpha );
		Vec2 pos = a.b.getWorldCenter().mul(Actor.alpha) .add( a.oldPos.mul(1 - Actor.alpha) );
		float angle = a.b.getAngle()*Actor.alpha + a.oldAng * (1 - Actor.alpha);
		g.pushTransform();
		g.resetTransform();  {			
			c.cam.translate(g, pos.x, pos.y);
			c.cam.scale(g, a.size.x * width, a.size.y * height);
			c.cam.rotate(g, angle);
			if (isVector) {
				if (sprite != null) {
					((SimpleDiagramRenderer)sprite).render(g);
				} else {
					//Console.dbg.println(file + " Has no sprites");
				}
			} else {
				g.drawImage((Image)sprite,0,0);
			}
			if (a.isHeld) {
//				p.fill(150,120,70,100);
//				p.ellipse(0,0,length,length);
			}
		}
		g.popTransform();
		
//		p.fill(255);
//		if (a.isImportant) {
//			Vec2 spot = c.cam.worldToScreen(pos.add(new Vec2(-a.size.x/2,a.size.y)));
//			p.text(a.label, spot.x, spot.y);
//		}
	}
}