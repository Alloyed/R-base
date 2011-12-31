package client.sprites;

import org.newdawn.slick.Graphics;

import physics.actors.Actor;
import physics.actors.Prop;

/*Does nothing.*/
public class EmptySprite implements Sprite {

	public void draw(Graphics g, Actor a) {}

	public void draw(Graphics pg, float x, float y, float max) {}

	public void draw(Graphics g, Prop pr) {}
}
