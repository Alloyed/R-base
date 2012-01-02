package client.sprites;

import java.awt.Graphics2D;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URI;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.util.BufferedImageUtil;
import org.newdawn.slick.util.ResourceLoader;

import com.kitfox.svg.*;

import client.ui.Loop;

import physics.Console;
import physics.actors.Actor;
import physics.actors.Prop;

/**
 * draws vectors or images. 
 * Vectors are rendered as rasters for performance reasons
 * TODO: Render svgs at different resolutions, 
 * 		allow for straight vector rendering again
 * NVM the sprites actually look pretty ok
 */

public class ImageSprite implements Sprite {
	Loop c;
	SVGUniverse u;
	String file;
	boolean isVector = false;
	public Image sprite;
	float width, height, sw, sh, length;
	public ImageSprite(Loop c, String file) {
		this.c = c;
		this.file = file;
		try {
			if (file.endsWith(".svg") || file.endsWith(".svgz")) {
				isVector = true;
				InputStream in = ResourceLoader.getResourceAsStream(file);
				u = new SVGUniverse();
				URI ind = u.loadSVG(in, file);
				SVGDiagram d = u.getDiagram(ind);
				width = d.getWidth();
				height = d.getHeight();
				if (width > height) {
					sh = 1;
					sw = width/height;
				} else {
					sw = 1;
					sh = height/width;
				}
				length = new Vec2(d.getWidth(),d.getHeight()).length();
				BufferedImage buf = new BufferedImage((int)d.getWidth()*2, (int)d.getHeight()*2, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = buf.createGraphics();
				g.setColor(java.awt.Color.white);
				d.setIgnoringClipHeuristic(true);
				g.translate(d.getWidth()/4, d.getHeight()/4);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
									RenderingHints.VALUE_ANTIALIAS_ON);
				d.render(g);
				g.dispose();
				buf.flush();
				sprite = new Image(BufferedImageUtil.getTexture("txt-"+file, buf));
			} else {
				Image image = new Image(file);
				width = image.getWidth();
				height = image.getHeight();
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
//			if (isVector)
//				SimpleDiagramRenderer.render(pg, (Diagram)sprite);
//			else
				pg.drawImage(sprite,0,0);
			pg.popTransform();
	}
	

	public void draw(Graphics g, Actor a) {
		//This does linear interpolation: new*alpha + old*( 1.0 - alpha );
		Vec2 pos = a.b.getWorldCenter().mul(Actor.alpha)
				.add(a.oldPos.mul(1 - Actor.alpha) );
		//Vec2 pos = a.b.getWorldCenter();
		float angle = a.b.getAngle()*Actor.alpha + 
					a.oldAng * (1 - Actor.alpha);

		//float angle = a.b.getAngle();
		g.pushTransform();
		g.resetTransform();  {
			
			
			c.cam.translate(g, pos.x, pos.y);
			c.cam.rotate(g, angle);
			c.cam.scale(g, a.size.x / sw, a.size.y / sh);
			
			g.setColor(Color.black);
			g.drawImage(sprite, -width*.75f, -height*.75f);
			if (a.isHeld) {
				g.setAntiAlias(true);
				g.setColor(new Color(150,120,70,50));
				g.drawOval(-length/2, -length/2, length, length);
				g.fillOval(-length/2, -length/2, length, length);
			}
		} g.popTransform();
		
			if (a.isImportant) {
				Vec2 spot = c.cam.worldToScreen(pos.add(new Vec2(-a.size.x/2,a.size.y)));
				//TODO: The fuck is wrong with that font?
				g.drawString(a.label, spot.x, spot.y);
			}
		}
		
	public void draw(Graphics g, Prop a) {
		//This should never move
		Vec2 pos = a.pos;
		float angle = a.angle;
		g.pushTransform();
		g.resetTransform();  {
			
			c.cam.translate(g, pos.x, pos.y);
			c.cam.rotate(g, angle);
			c.cam.scale(g, a.width / sw, a.height / sh);
			
			g.drawImage(sprite, -width*.75f, -height*.75f);
		} g.popTransform();
		
	}
}