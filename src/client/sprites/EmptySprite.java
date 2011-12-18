package client.sprites;

import physics.actors.Actor;
import physics.actors.Prop;
import processing.core.PGraphics;

/*Does nothing.*/
public class EmptySprite implements Sprite {

	public void draw(Actor a) {
		;
	}

	public void draw(PGraphics pg, float x, float y, float max) {
		;
	}

	public void draw(Prop pr) {
		;
	}
}
