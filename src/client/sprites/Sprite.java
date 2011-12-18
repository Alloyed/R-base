package client.sprites;

import physics.actors.Actor;
import physics.actors.Prop;
import processing.core.PGraphics;

/*A sprite draws an actor to the screen.*/
public interface Sprite {
	/*
	 * Draw a reference image of the object 
	 * onto a PGraphics at (x,y) with a max size of max. Use for UI objects.
	 */
	public void draw(PGraphics pg, float x, float y, float max);
	/* 
	 * Draws the specified Actor a. 
	 * See Actor for the variables you can use to customize this. 
	 */
	public void draw(Actor a);
	/*
	 * Draws the Prop pr, used by MapSprite.
	 */
	public void draw(Prop pr);
}
